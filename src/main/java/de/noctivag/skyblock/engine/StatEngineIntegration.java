package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import de.noctivag.skyblock.features.stats.types.SecondaryStat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * StatEngineIntegration - Integrates all stat calculation components
 * 
 * This class serves as the main integration point for the entire stat calculation system.
 * It coordinates between the StatCalculationService, ReforgeMatrixManager, AccessoryManager,
 * and PreciseDamageCalculator to provide a unified stat calculation experience.
 * 
 * Key Features:
 * - Unified stat calculation API
 * - Event handling for stat updates
 * - Performance optimization with caching
 * - Integration with existing systems
 */
public class StatEngineIntegration implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final StatCalculationService statCalculationService;
    private final ReforgeMatrixManager reforgeMatrixManager;
    private final AccessoryManager accessoryManager;
    private final PreciseDamageCalculator damageCalculator;
    private final StatAggregationPipeline aggregationPipeline;
    
    private final Map<UUID, StatCalculationService.PlayerStatProfile> playerStatProfiles = new ConcurrentHashMap<>();
    private final Map<UUID, StatAggregationPipeline.PlayerStatAggregation> playerAggregations = new ConcurrentHashMap<>();
    
    public StatEngineIntegration(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        // Initialize all components
        this.statCalculationService = new StatCalculationService(plugin, databaseManager);
        this.reforgeMatrixManager = new ReforgeMatrixManager(plugin);
        this.accessoryManager = new AccessoryManager(plugin, databaseManager);
        this.damageCalculator = new PreciseDamageCalculator(plugin, statCalculationService);
        this.aggregationPipeline = new StatAggregationPipeline(plugin, databaseManager, 
                                                              statCalculationService, reforgeMatrixManager, accessoryManager);
        
        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        // Start update task
        startStatUpdateTask();
    }
    
    /**
     * Get player's current stat profile
     */
    public CompletableFuture<StatCalculationService.PlayerStatProfile> getPlayerStatProfile(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cache first
        StatCalculationService.PlayerStatProfile cachedProfile = playerStatProfiles.get(playerId);
        if (cachedProfile != null && !cachedProfile.isExpired()) {
            return CompletableFuture.completedFuture(cachedProfile);
        }
        
        // Calculate new profile
        return statCalculationService.calculatePlayerStatsAsync(player).thenApply(profile -> {
            playerStatProfiles.put(playerId, profile);
            return profile;
        });
    }
    
    /**
     * Get player's stat aggregation
     */
    public CompletableFuture<StatAggregationPipeline.PlayerStatAggregation> getPlayerStatAggregation(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Check cache first
        StatAggregationPipeline.PlayerStatAggregation cachedAggregation = playerAggregations.get(playerId);
        if (cachedAggregation != null) {
            return CompletableFuture.completedFuture(cachedAggregation);
        }
        
        // Calculate new aggregation
        return aggregationPipeline.executeAggregationPipeline(player).thenApply(aggregation -> {
            playerAggregations.put(playerId, aggregation);
            return aggregation;
        });
    }
    
    /**
     * Calculate damage for player attack
     */
    public CompletableFuture<PreciseDamageCalculator.DamageResult> calculateDamage(Player attacker, org.bukkit.entity.LivingEntity target) {
        return damageCalculator.calculateMeleeDamage(attacker, target);
    }
    
    /**
     * Get player's final stat value
     */
    public CompletableFuture<Double> getPlayerStat(Player player, PrimaryStat stat) {
        return getPlayerStatProfile(player).thenApply(profile -> profile.getFinalStat(stat));
    }
    
    /**
     * Get player's secondary stat value
     */
    public CompletableFuture<Double> getPlayerSecondaryStat(Player player, SecondaryStat stat) {
        return getPlayerStatAggregation(player).thenApply(aggregation -> aggregation.getFinalSecondaryStat(stat));
    }
    
    /**
     * Get player's derived stat value
     */
    public CompletableFuture<Double> getPlayerDerivedStat(Player player, String stat) {
        return getPlayerStatAggregation(player).thenApply(aggregation -> aggregation.getDerivedStat(stat));
    }
    
    /**
     * Force refresh player stats
     */
    public void refreshPlayerStats(Player player) {
        UUID playerId = player.getUniqueId();
        playerStatProfiles.remove(playerId);
        playerAggregations.remove(playerId);
        
        // Trigger recalculation
        getPlayerStatProfile(player);
        getPlayerStatAggregation(player);
    }
    
    /**
     * Start stat update task
     */
    private void startStatUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Update stats for all online players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Only update if player has been online for a while
                    if (player.getTicksLived() > 100) {
                        refreshPlayerStats(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L); // Every 5 seconds
    }
    
    /**
     * Handle player join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Initialize player stats
        getPlayerStatProfile(player);
        getPlayerStatAggregation(player);
    }
    
    /**
     * Handle player quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Clean up player data
        playerStatProfiles.remove(playerId);
        playerAggregations.remove(playerId);
    }
    
    /**
     * Handle damage events for stat-based damage calculation
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof org.bukkit.entity.LivingEntity) {
            Player attacker = (Player) event.getDamager();
            org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) event.getEntity();
            
            // Calculate precise damage
            calculateDamage(attacker, target).thenAccept(damageResult -> {
                // Apply the calculated damage
                event.setDamage(damageResult.getFinalDamage());
                
                // Send damage information to player
                sendDamageInfo(attacker, damageResult);
            });
        }
    }
    
    /**
     * Send damage information to player
     */
    private void sendDamageInfo(Player player, PreciseDamageCalculator.DamageResult damageResult) {
        // Send damage breakdown to player
        if (damageResult.isCritical()) {
            player.sendMessage("§c⚔️ " + String.format("%.1f", damageResult.getFinalDamage()) + " CRITICAL DAMAGE!");
        } else {
            player.sendMessage("§7⚔️ " + String.format("%.1f", damageResult.getFinalDamage()) + " damage");
        }
        
        // Send detailed breakdown if player has permission
        if (player.hasPermission("skyblock.stats.detailed")) {
            player.sendMessage("§7Damage Breakdown:");
            player.sendMessage("§7  Weapon: " + String.format("%.1f", damageResult.getWeaponDamage()));
            player.sendMessage("§7  Strength: " + String.format("%.2f", damageResult.getStrengthMultiplier()) + "x");
            if (damageResult.isCritical()) {
                player.sendMessage("§7  Critical: " + String.format("%.2f", damageResult.getCriticalMultiplier()) + "x");
            }
            if (damageResult.getFerocityAttacks() > 0) {
                player.sendMessage("§7  Ferocity: " + damageResult.getFerocityAttacks() + " additional attacks");
            }
        }
    }
    
    /**
     * Get all stat calculation components
     */
    public StatCalculationService getStatCalculationService() { return statCalculationService; }
    public ReforgeMatrixManager getReforgeMatrixManager() { return reforgeMatrixManager; }
    public AccessoryManager getAccessoryManager() { return accessoryManager; }
    public PreciseDamageCalculator getDamageCalculator() { return damageCalculator; }
    public StatAggregationPipeline getAggregationPipeline() { return aggregationPipeline; }
    
    /**
     * Get player stat profiles (for debugging)
     */
    public Map<UUID, StatCalculationService.PlayerStatProfile> getPlayerStatProfiles() {
        return new HashMap<>(playerStatProfiles);
    }
    
    /**
     * Get player aggregations (for debugging)
     */
    public Map<UUID, StatAggregationPipeline.PlayerStatAggregation> getPlayerAggregations() {
        return new HashMap<>(playerAggregations);
    }
}
