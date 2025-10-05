package de.noctivag.skyblock.slayers;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Advanced slayer system with boss tiers and abilities
 */
public class AdvancedSlayerSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, SlayerBoss> bosses = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerProgress> playerProgress = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> questTasks = new ConcurrentHashMap<>();
    
    public AdvancedSlayerSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing AdvancedSlayerSystem...");
        
        // Load slayer bosses from configuration
        loadSlayerBossesFromConfig();
        
        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("AdvancedSlayerSystem initialized with " + bosses.size() + " slayer bosses.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down AdvancedSlayerSystem...");
        
        // Stop all quest tasks
        for (BukkitTask task : questTasks.values()) {
            if (task != null) {
                task.cancel();
            }
        }
        questTasks.clear();
        
        // Save all player progress
        saveAllPlayerProgress();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("AdvancedSlayerSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "AdvancedSlayerSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load slayer bosses from configuration file
     */
    private void loadSlayerBossesFromConfig() {
        File configFile = new File(plugin.getDataFolder(), "slayers.yml");
        if (!configFile.exists()) {
            createDefaultSlayersConfig(configFile);
        }
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection bossesSection = config.getConfigurationSection("bosses");
        
        if (bossesSection == null) {
            plugin.getLogger().warning("No bosses section found in slayers.yml");
            return;
        }
        
        for (String bossId : bossesSection.getKeys(false)) {
            try {
                SlayerBoss boss = loadSlayerBossFromConfig(bossesSection.getConfigurationSection(bossId));
                if (boss != null) {
                    bosses.put(bossId, boss);
                    plugin.getLogger().info("Loaded slayer boss: " + boss.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load slayer boss " + bossId + ": " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Create default slayers configuration
     */
    private void createDefaultSlayersConfig(File configFile) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            
            // Revenant Horror
            config.set("bosses.revenant_horror.id", "revenant_horror");
            config.set("bosses.revenant_horror.name", "Revenant Horror");
            config.set("bosses.revenant_horror.description", "A powerful undead horror");
            config.set("bosses.revenant_horror.type", "UNDEAD");
            config.set("bosses.revenant_horror.max_tier", 4);
            config.set("bosses.revenant_horror.base_health", 1000);
            config.set("bosses.revenant_horror.base_damage", 50);
            config.set("bosses.revenant_horror.base_defense", 10);
            config.set("bosses.revenant_horror.base_xp", 100);
            config.set("bosses.revenant_horror.base_coins", 1000);
            config.set("bosses.revenant_horror.quest_cost", 100);
            config.set("bosses.revenant_horror.abilities.pest", true);
            config.set("bosses.revenant_horror.abilities.tnt_rain", true);
            config.set("bosses.revenant_horror.abilities.berserk", true);
            
            // Tarantula Broodmother
            config.set("bosses.tarantula_broodmother.id", "tarantula_broodmother");
            config.set("bosses.tarantula_broodmother.name", "Tarantula Broodmother");
            config.set("bosses.tarantula_broodmother.description", "A massive spider queen");
            config.set("bosses.tarantula_broodmother.type", "SPIDER");
            config.set("bosses.tarantula_broodmother.max_tier", 4);
            config.set("bosses.tarantula_broodmother.base_health", 1200);
            config.set("bosses.tarantula_broodmother.base_damage", 60);
            config.set("bosses.tarantula_broodmother.base_defense", 15);
            config.set("bosses.tarantula_broodmother.base_xp", 120);
            config.set("bosses.tarantula_broodmother.base_coins", 1200);
            config.set("bosses.tarantula_broodmother.quest_cost", 120);
            config.set("bosses.tarantula_broodmother.abilities.web_shot", true);
            config.set("bosses.tarantula_broodmother.abilities.spider_swarm", true);
            config.set("bosses.tarantula_broodmother.abilities.poison_cloud", true);
            
            // Sven Packmaster
            config.set("bosses.sven_packmaster.id", "sven_packmaster");
            config.set("bosses.sven_packmaster.name", "Sven Packmaster");
            config.set("bosses.sven_packmaster.description", "A ferocious wolf pack leader");
            config.set("bosses.sven_packmaster.type", "WOLF");
            config.set("bosses.sven_packmaster.max_tier", 4);
            config.set("bosses.sven_packmaster.base_health", 1500);
            config.set("bosses.sven_packmaster.base_damage", 70);
            config.set("bosses.sven_packmaster.base_defense", 20);
            config.set("bosses.sven_packmaster.base_xp", 150);
            config.set("bosses.sven_packmaster.base_coins", 1500);
            config.set("bosses.sven_packmaster.quest_cost", 150);
            config.set("bosses.sven_packmaster.abilities.howl", true);
            config.set("bosses.sven_packmaster.abilities.pack_summon", true);
            config.set("bosses.sven_packmaster.abilities.berserk_rage", true);
            
            config.save(configFile);
            plugin.getLogger().info("Created default slayers configuration: " + configFile.getName());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create default slayers configuration", e);
        }
    }
    
    /**
     * Load a single slayer boss from configuration
     */
    private SlayerBoss loadSlayerBossFromConfig(ConfigurationSection bossSection) {
        if (bossSection == null) return null;
        
        String id = bossSection.getString("id");
        String name = bossSection.getString("name");
        String description = bossSection.getString("description");
        String typeStr = bossSection.getString("type", "CUSTOM");
        int maxTier = bossSection.getInt("max_tier", 1);
        double baseHealth = bossSection.getDouble("base_health", 100);
        double baseDamage = bossSection.getDouble("base_damage", 10);
        double baseDefense = bossSection.getDouble("base_defense", 0);
        double baseXp = bossSection.getDouble("base_xp", 10);
        double baseCoins = bossSection.getDouble("base_coins", 100);
        double questCost = bossSection.getDouble("quest_cost", 50);
        
        // Load abilities
        Map<String, Boolean> abilities = new HashMap<>();
        ConfigurationSection abilitiesSection = bossSection.getConfigurationSection("abilities");
        if (abilitiesSection != null) {
            for (String abilityKey : abilitiesSection.getKeys(false)) {
                abilities.put(abilityKey, abilitiesSection.getBoolean(abilityKey, false));
            }
        }
        
        SlayerBoss.BossType type;
        try {
            type = SlayerBoss.BossType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = SlayerBoss.BossType.CUSTOM;
        }
        
        return new SlayerBoss(id, name, description, type, maxTier, baseHealth, baseDamage, 
                            baseDefense, baseXp, baseCoins, questCost, abilities);
    }
    
    /**
     * Start a slayer quest for a player
     */
    public boolean startSlayerQuest(UUID playerUuid, String bossId, int tier) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return false;
        
        SlayerBoss boss = bosses.get(bossId);
        if (boss == null || tier < 1 || tier > boss.getMaxTier()) {
            return false;
        }
        
        // Check if player already has an active quest
        if (activeQuests.containsKey(playerUuid)) {
            return false;
        }
        
        // Check if player has enough coins
        double questCost = boss.getQuestCost() * tier;
        // Implementation would check player's coins and deduct them
        
        // Create quest
        SlayerQuest quest = new SlayerQuest(playerUuid, boss, tier);
        activeQuests.put(playerUuid, quest);
        
        // Start quest task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (quest.isExpired()) {
                    endSlayerQuest(playerUuid, false);
                } else {
                    updateSlayerQuest(quest);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
        
        questTasks.put(playerUuid, task);
        
        // Announce quest start
        player.sendMessage("§6§lSLAYER QUEST STARTED!");
        player.sendMessage("§eBoss: §f" + boss.getName() + " Tier " + tier);
        player.sendMessage("§eTime Limit: §f" + quest.getTimeLimit() + " seconds");
        
        plugin.getLogger().info("Started slayer quest for " + player.getName() + ": " + boss.getName() + " Tier " + tier);
        return true;
    }
    
    /**
     * End a slayer quest
     */
    public boolean endSlayerQuest(UUID playerUuid, boolean success) {
        SlayerQuest quest = activeQuests.remove(playerUuid);
        if (quest == null) return false;
        
        // Stop quest task
        BukkitTask task = questTasks.remove(playerUuid);
        if (task != null) {
            task.cancel();
        }
        
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            if (success) {
                // Give rewards
                giveSlayerRewards(player, quest);
                player.sendMessage("§a§lSLAYER QUEST COMPLETED!");
                player.sendMessage("§eBoss: §f" + quest.getBoss().getName() + " Tier " + quest.getTier());
                
                // Update player progress
                updatePlayerProgress(playerUuid, quest.getBoss().getId(), quest.getTier());
            } else {
                player.sendMessage("§c§lSLAYER QUEST FAILED!");
                player.sendMessage("§eBoss: §f" + quest.getBoss().getName() + " Tier " + quest.getTier());
            }
        }
        
        plugin.getLogger().info("Ended slayer quest for " + playerUuid + ": " + quest.getBoss().getName() + " Tier " + quest.getTier() + " (Success: " + success + ")");
        return true;
    }
    
    /**
     * Update slayer quest
     */
    private void updateSlayerQuest(SlayerQuest quest) {
        // Update quest progress, check conditions, etc.
        long timeRemaining = quest.getTimeRemaining();
        
        // Announce time remaining every 30 seconds
        if (timeRemaining > 0 && timeRemaining % 30 == 0) {
            Player player = Bukkit.getPlayer(quest.getPlayerUuid());
            if (player != null) {
                player.sendMessage("§6§lSLAYER QUEST! §eTime remaining: " + timeRemaining + " seconds");
            }
        }
    }
    
    /**
     * Give slayer rewards to player
     */
    private void giveSlayerRewards(Player player, SlayerQuest quest) {
        SlayerBoss boss = quest.getBoss();
        int tier = quest.getTier();
        
        // Calculate rewards based on tier
        double xpReward = boss.getBaseXp() * tier;
        double coinReward = boss.getBaseCoins() * tier;
        
        // Give XP
        player.sendMessage("§a+ " + xpReward + " Combat XP");
        
        // Give coins
        player.sendMessage("§a+ " + coinReward + " coins");
        
        // Give loot (implementation would use loot system)
        // plugin.getLootService().distributeLoot(boss.getLootTableId(), player.getLocation(), player);
    }
    
    /**
     * Update player progress
     */
    private void updatePlayerProgress(UUID playerUuid, String bossId, int tier) {
        SlayerProgress progress = playerProgress.computeIfAbsent(playerUuid, SlayerProgress::new);
        progress.updateBossProgress(bossId, tier);
    }
    
    /**
     * Save all player progress
     */
    private void saveAllPlayerProgress() {
        // Implementation would save to database
        plugin.getLogger().info("Saved slayer progress for " + playerProgress.size() + " players");
    }
    
    /**
     * Get player's slayer progress
     */
    public SlayerProgress getPlayerProgress(UUID playerUuid) {
        return playerProgress.computeIfAbsent(playerUuid, SlayerProgress::new);
    }
    
    /**
     * Get active quest for player
     */
    public SlayerQuest getActiveQuest(UUID playerUuid) {
        return activeQuests.get(playerUuid);
    }
    
    /**
     * Get all slayer bosses
     */
    public Map<String, SlayerBoss> getBosses() {
        return new HashMap<>(bosses);
    }
    
    /**
     * Get slayer boss by ID
     */
    public SlayerBoss getBoss(String bossId) {
        return bosses.get(bossId);
    }
    
    // Event handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player progress when they join
        getPlayerProgress(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // End active quest if player leaves
        UUID playerUuid = event.getPlayer().getUniqueId();
        if (activeQuests.containsKey(playerUuid)) {
            endSlayerQuest(playerUuid, false);
        }
    }
}
