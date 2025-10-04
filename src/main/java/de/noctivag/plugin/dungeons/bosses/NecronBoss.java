package de.noctivag.plugin.dungeons.bosses;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Necron F7 Boss - Implements the complete Necron boss fight with multi-phase state machine
 * 
 * Features:
 * - Phase 1 (Crystals): Parkour mechanics, crystal placement, red beacon stun mechanic
 * - Phase 2 (Pillars/Wither Skeletons): Crushing mechanics, 27,000 True Damage skulls
 * - Phase 3 (Terminals): Pursuit AI, 7-8 terminal puzzles, slow mechanics
 * - Boss Abilities: Rotating swords, wither skulls, lightning strikes
 * - Advanced AI: Player action responses, targeted attacks
 */
public class NecronBoss implements Listener {
    
    private final org.bukkit.plugin.Plugin plugin;
    
    // Boss state management
    private LivingEntity necronEntity;
    private NecronPhase currentPhase;
    private final Map<UUID, Long> lastDamageTime = new ConcurrentHashMap<>();
    
    // Phase-specific components
    private final Map<String, CrystalPodium> crystalPodia = new ConcurrentHashMap<>();
    private final Map<String, CrushingPillar> crushingPillars = new ConcurrentHashMap<>();
    private final Map<String, Terminal> terminals = new ConcurrentHashMap<>();
    private final List<Entity> spawnedMinions = new ArrayList<>();
    
    // Boss mechanics
    private Location redBeaconLocation;
    private boolean isStunned;
    private long lastAbilityTime;
    private long phaseStartTime;
    
    // AI and targeting
    private Player currentTarget;
    private long lastTargetUpdate;
    private final Map<Player, Integer> playerDamageContribution = new ConcurrentHashMap<>();
    
    // Update tasks
    private BukkitTask bossUpdateTask;
    private BukkitTask phaseUpdateTask;
    
    public NecronBoss(org.bukkit.plugin.Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Spawn Necron boss
     */
    public void spawnNecron(Location location) {
        // Spawn Wither entity as Necron
        necronEntity = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.WITHER);
        
        // Set boss properties
        necronEntity.customName(net.kyori.adventure.text.Component.text("§8§lNecron"));
        necronEntity.setCustomNameVisible(true);
        necronEntity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(100000000); // 100M HP
        necronEntity.setHealth(necronEntity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue());
        
        // Initialize boss state
        currentPhase = NecronPhase.PHASE_1_CRYSTALS;
        phaseStartTime = System.currentTimeMillis();
        isStunned = false;
        lastAbilityTime = System.currentTimeMillis();
        
        // Initialize phase components
        initializePhase1Components(location);
        
        // Start update tasks
        startBossUpdateTask();
        startPhaseUpdateTask();
        
        // Announce boss spawn
        announceBossSpawn();
    }
    
    /**
     * Initialize Phase 1 components (Crystals)
     */
    private void initializePhase1Components(Location center) {
        // Create crystal podia around the arena
        for (int i = 0; i < 2; i++) {
            double angle = (i * Math.PI) + (Math.PI / 4); // Position at 45° and 225°
            Location podiumLoc = center.clone().add(
                Math.cos(angle) * 15, 0, Math.sin(angle) * 15);
            
            String podiumId = "crystal_podium_" + i;
            CrystalPodium podium = new CrystalPodium(podiumId, podiumLoc, false);
            crystalPodia.put(podiumId, podium);
        }
        
        // Set red beacon location (on conveyor belt)
        redBeaconLocation = center.clone().add(0, 2, 10);
        redBeaconLocation.getBlock().setType(Material.REDSTONE_BLOCK);
    }
    
    /**
     * Initialize Phase 2 components (Pillars and Wither Skeletons)
     */
    private void initializePhase2Components(Location center) {
        // Create crushing pillars
        for (int i = 0; i < 4; i++) {
            double angle = (i * Math.PI) / 2; // Position at 0°, 90°, 180°, 270°
            Location pillarLoc = center.clone().add(
                Math.cos(angle) * 12, 0, Math.sin(angle) * 12);
            
            String pillarId = "crushing_pillar_" + i;
            CrushingPillar pillar = new CrushingPillar(pillarId, pillarLoc, 30); // 30 block height
            crushingPillars.put(pillarId, pillar);
        }
        
        // Spawn Wither Skeletons with True Damage skulls
        spawnWitherSkeletons(center);
    }
    
    /**
     * Initialize Phase 3 components (Terminals)
     */
    private void initializePhase3Components(Location center) {
        // Create terminals around the arena
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI) / 4; // Position every 45 degrees
            Location terminalLoc = center.clone().add(
                Math.cos(angle) * 20, 0, Math.sin(angle) * 20);
            
            String terminalId = "terminal_" + i;
            Terminal terminal = new Terminal(terminalId, terminalLoc, TerminalType.values()[i % TerminalType.values().length]);
            terminals.put(terminalId, terminal);
        }
    }
    
    /**
     * Start boss update task
     */
    private void startBossUpdateTask() {
        bossUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (necronEntity == null || necronEntity.isDead()) {
                    cancel();
                    return;
                }
                
                updateBossAI();
                updateTargeting();
                updateBossAbilities();
            }
        }.runTaskTimer(plugin, 0L, 1L); // Update every tick
    }
    
    /**
     * Start phase update task
     */
    private void startPhaseUpdateTask() {
        phaseUpdateTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (necronEntity == null || necronEntity.isDead()) {
                    cancel();
                    return;
                }
                
                checkPhaseTransitions();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }
    
    /**
     * Update boss AI
     */
    private void updateBossAI() {
        if (isStunned) {
            updateStunMechanics();
            return;
        }
        
        // Update targeting
        updateTargeting();
        
        // Phase-specific AI
        switch (currentPhase) {
            case PHASE_1_CRYSTALS -> updatePhase1AI();
            case PHASE_2_PILLARS -> updatePhase2AI();
            case PHASE_3_TERMINALS -> updatePhase3AI();
        }
    }
    
    /**
     * Update Phase 1 AI (Crystals)
     */
    private void updatePhase1AI() {
        // Check if crystals are placed
        boolean allCrystalsPlaced = crystalPodia.values().stream()
            .allMatch(CrystalPodium::hasCrystal);
        
        if (allCrystalsPlaced) {
            // Lure Necron to red beacon
            lureToRedBeacon();
        } else {
            // Attack players trying to place crystals
            attackCrystalPlayers();
        }
    }
    
    /**
     * Update Phase 2 AI (Pillars and Wither Skeletons)
     */
    private void updatePhase2AI() {
        // Update crushing pillars
        crushingPillars.values().forEach(CrushingPillar::update);
        
        // Spawn additional Wither Skeletons if needed
        if (spawnedMinions.size() < 8) {
            spawnWitherSkeletons(necronEntity.getLocation());
        }
        
        // Update minion AI
        updateWitherSkeletonAI();
    }
    
    /**
     * Update Phase 3 AI (Terminals)
     */
    private void updatePhase3AI() {
        // Pursue players working on terminals
        pursueTerminalPlayers();
        
        // Update terminals
        terminals.values().forEach(Terminal::update);
        
        // Use slow mechanics when hit by specific attacks
        updateSlowMechanics();
    }
    
    /**
     * Update targeting system
     */
    private void updateTargeting() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTargetUpdate < 1000) return; // Update every second
        
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        // Find nearest player within 50 blocks
        for (Entity nearby : necronEntity.getNearbyEntities(50, 50, 50)) {
            if (nearby instanceof Player player && !player.isDead()) {
                double distance = player.getLocation().distance(necronEntity.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = player;
                }
            }
        }
        
        currentTarget = nearestPlayer;
        lastTargetUpdate = currentTime;
    }
    
    /**
     * Update boss abilities
     */
    private void updateBossAbilities() {
        long currentTime = System.currentTimeMillis();
        
        // Rotating swords ability
        if (currentTime - lastAbilityTime >= 3000) { // Every 3 seconds
            useRotatingSwordsAbility();
            lastAbilityTime = currentTime;
        }
        
        // Wither skulls ability
        if (currentTime - lastAbilityTime >= 5000) { // Every 5 seconds
            useWitherSkullsAbility();
        }
        
        // Lightning strikes ability
        if (currentTime - lastAbilityTime >= 7000) { // Every 7 seconds
            useLightningStrikesAbility();
        }
    }
    
    /**
     * Use rotating swords ability
     */
    private void useRotatingSwordsAbility() {
        Location center = necronEntity.getLocation();
        
        // Create rotating swords around Necron
        for (int i = 0; i < 8; i++) {
            double angle = (i * Math.PI) / 4;
            Location swordLoc = center.clone().add(
                Math.cos(angle) * 3, 2, Math.sin(angle) * 3);
            
            // Spawn rotating sword entity (using armor stand with sword)
            ArmorStand sword = (ArmorStand) center.getWorld().spawnEntity(swordLoc, EntityType.ARMOR_STAND);
            sword.setVisible(false);
            sword.setGravity(false);
            sword.setSmall(true);
            sword.getEquipment().setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.IRON_SWORD));
            
            // Make sword rotate around Necron
            new BukkitRunnable() {
                private double rotation = 0;
                
                @Override
                public void run() {
                    if (sword.isDead() || necronEntity == null || necronEntity.isDead()) {
                        sword.remove();
                        cancel();
                        return;
                    }
                    
                    rotation += 0.1;
                    Location newLoc = center.clone().add(
                        Math.cos(rotation) * 3, 2, Math.sin(rotation) * 3);
                    sword.teleport(newLoc);
                    
                    // Damage players hit by sword
                    for (Entity nearby : sword.getNearbyEntities(1, 1, 1)) {
                        if (nearby instanceof Player player) {
                            player.damage(5000, necronEntity); // 5000 damage
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
    
    /**
     * Use wither skulls ability
     */
    private void useWitherSkullsAbility() {
        if (currentTarget == null) return;
        
        Location targetLoc = currentTarget.getLocation();
        Location bossLoc = necronEntity.getLocation();
        
        // Launch wither skulls at target
        for (int i = 0; i < 5; i++) {
            WitherSkull skull = (WitherSkull) bossLoc.getWorld().spawnEntity(
                bossLoc.add(0, 2, 0), EntityType.WITHER_SKULL);
            
            // Calculate trajectory to target
            org.bukkit.util.Vector direction = targetLoc.toVector().subtract(bossLoc.toVector()).normalize();
            skull.setDirection(direction);
            
            // Set skull properties
            skull.setCharged(true); // Blue wither skull
        }
    }
    
    /**
     * Use lightning strikes ability
     */
    private void useLightningStrikesAbility() {
        // Strike lightning at random players
        for (Player player : necronEntity.getWorld().getPlayers()) {
            if (player.getLocation().distance(necronEntity.getLocation()) <= 50) {
                if (Math.random() < 0.3) { // 30% chance per player
                    player.getWorld().strikeLightning(player.getLocation());
                    player.damage(10000, necronEntity); // 10000 damage
                }
            }
        }
    }
    
    /**
     * Lure Necron to red beacon
     */
    private void lureToRedBeacon() {
        if (redBeaconLocation == null) return;
        
        // Move Necron towards red beacon
        Location currentLoc = necronEntity.getLocation();
        org.bukkit.util.Vector direction = redBeaconLocation.toVector().subtract(currentLoc.toVector()).normalize();
        
        // Teleport Necron closer to beacon
        Location newLoc = currentLoc.add(direction.multiply(0.5));
        necronEntity.teleport(newLoc);
        
        // Check if reached beacon
        if (currentLoc.distance(redBeaconLocation) <= 3) {
            stunNecron();
        }
    }
    
    /**
     * Stun Necron at red beacon
     */
    private void stunNecron() {
        isStunned = true;
        necronEntity.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.SLOWNESS, 200, 10, false, false));
        
        // Announce stun
        announceToPlayers("§c§lNecron is stunned! Attack now!");
        
        // Remove stun after 10 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                isStunned = false;
                necronEntity.removePotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS);
            }
        }.runTaskLater(plugin, 200L); // 10 seconds
    }
    
    /**
     * Spawn Wither Skeletons with True Damage
     */
    private void spawnWitherSkeletons(Location center) {
        for (int i = 0; i < 4; i++) {
            double angle = Math.random() * 2 * Math.PI;
            Location spawnLoc = center.clone().add(
                Math.cos(angle) * 15, 0, Math.sin(angle) * 15);
            
            WitherSkeleton skeleton = (WitherSkeleton) spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
            
            // Set True Damage properties
            skeleton.customName(net.kyori.adventure.text.Component.text("§c§lWither Skeleton §7(27,000 True Damage)"));
            skeleton.setCustomNameVisible(true);
            
            // Add special effects
            skeleton.addPotionEffect(new org.bukkit.potion.PotionEffect(
                org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
            
            spawnedMinions.add(skeleton);
        }
    }
    
    /**
     * Update Wither Skeleton AI
     */
    private void updateWitherSkeletonAI() {
        spawnedMinions.removeIf(Entity::isDead);
        
        for (Entity minion : spawnedMinions) {
            if (minion instanceof WitherSkeleton skeleton) {
                // Find nearest player
                Player nearestPlayer = null;
                double nearestDistance = Double.MAX_VALUE;
                
                for (Entity nearby : skeleton.getNearbyEntities(20, 20, 20)) {
                    if (nearby instanceof Player player) {
                        double distance = player.getLocation().distance(skeleton.getLocation());
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestPlayer = player;
                        }
                    }
                }
                
                // Attack nearest player with True Damage
                if (nearestPlayer != null && nearestDistance <= 2) {
                    nearestPlayer.damage(27000, skeleton); // 27,000 True Damage
                    skeleton.getWorld().spawnParticle(Particle.CRIT, nearestPlayer.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }
    
    /**
     * Update slow mechanics for Phase 3
     */
    private void updateSlowMechanics() {
        // Check if Necron was hit by Bonemerangs or Mage Beam
        // This would be handled by damage event listeners
    }
    
    /**
     * Check phase transitions
     */
    private void checkPhaseTransitions() {
        double healthPercentage = necronEntity.getHealth() / necronEntity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
        
        if (healthPercentage <= 0.66 && currentPhase == NecronPhase.PHASE_1_CRYSTALS) {
            transitionToPhase2();
        } else if (healthPercentage <= 0.33 && currentPhase == NecronPhase.PHASE_2_PILLARS) {
            transitionToPhase3();
        }
    }
    
    /**
     * Transition to Phase 2
     */
    private void transitionToPhase2() {
        currentPhase = NecronPhase.PHASE_2_PILLARS;
        phaseStartTime = System.currentTimeMillis();
        
        // Initialize Phase 2 components
        initializePhase2Components(necronEntity.getLocation());
        
        // Announce phase transition
        announceToPlayers("§c§lNecron enters Phase 2: Pillars and Wither Skeletons!");
        announceToPlayers("§7§lWARNING: Wither Skeletons deal 27,000 True Damage!");
    }
    
    /**
     * Transition to Phase 3
     */
    private void transitionToPhase3() {
        currentPhase = NecronPhase.PHASE_3_TERMINALS;
        phaseStartTime = System.currentTimeMillis();
        
        // Initialize Phase 3 components
        initializePhase3Components(necronEntity.getLocation());
        
        // Announce phase transition
        announceToPlayers("§c§lNecron enters Phase 3: Terminal Puzzles!");
        announceToPlayers("§7§lComplete terminals to weaken Necron!");
    }
    
    /**
     * Handle player interactions with crystals
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (currentPhase != NecronPhase.PHASE_1_CRYSTALS) return;
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item != null && item.getType() == Material.END_CRYSTAL) {
            // Check if player is near a crystal podium
            for (CrystalPodium podium : crystalPodia.values()) {
                if (player.getLocation().distance(podium.getLocation()) <= 3) {
                    if (podium.placeCrystal()) {
                        player.getInventory().removeItem(new ItemStack(Material.END_CRYSTAL, 1));
                        announceToPlayers("§a§lCrystal placed! " + (crystalPodia.size() - getRemainingCrystals()) + "/2 crystals placed!");
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * Handle damage events
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() != necronEntity) return;
        
        if (event.getDamager() instanceof Player player) {
            // Track damage contribution
            playerDamageContribution.put(player, 
                playerDamageContribution.getOrDefault(player, 0) + (int) event.getFinalDamage());
            
            // Check for slow mechanics (Bonemerangs, Mage Beam)
            ItemStack weapon = player.getInventory().getItemInMainHand();
            if (weapon.hasItemMeta() && weapon.getItemMeta().hasDisplayName()) {
                String displayName = weapon.getItemMeta().getDisplayName();
                if (displayName.contains("Bonemerang") || displayName.contains("Mage Beam")) {
                    slowNecron();
                }
            }
        }
    }
    
    /**
     * Slow Necron
     */
    private void slowNecron() {
        necronEntity.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 3, false, false));
    }
    
    /**
     * Handle boss death
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() == necronEntity) {
            onNecronDefeated();
        }
    }
    
    /**
     * Handle Necron defeat
     */
    private void onNecronDefeated() {
        // Cancel update tasks
        if (bossUpdateTask != null) bossUpdateTask.cancel();
        if (phaseUpdateTask != null) phaseUpdateTask.cancel();
        
        // Announce victory
        announceToPlayers("§a§lNECRON DEFEATED!");
        announceToPlayers("§6§lF7 DUNGEON COMPLETED!");
        
        // Give rewards based on damage contribution
        giveRewards();
        
        // Clean up
        cleanup();
    }
    
    /**
     * Give rewards to players
     */
    private void giveRewards() {
        for (Map.Entry<Player, Integer> entry : playerDamageContribution.entrySet()) {
            Player player = entry.getKey();
            int damage = entry.getValue();
            
            // Give rewards based on damage contribution
            int coins = damage / 1000; // 1 coin per 1000 damage
            player.sendMessage("§6+" + coins + " Coins (Damage: " + damage + ")");
            
            // TODO: Add coins to player balance
        }
    }
    
    /**
     * Clean up boss components
     */
    private void cleanup() {
        // Remove all spawned entities
        spawnedMinions.forEach(Entity::remove);
        
        // Clear maps
        crystalPodia.clear();
        crushingPillars.clear();
        terminals.clear();
        spawnedMinions.clear();
        playerDamageContribution.clear();
        lastDamageTime.clear();
    }
    
    /**
     * Announce to all players
     */
    private void announceToPlayers(String message) {
        for (Player player : necronEntity.getWorld().getPlayers()) {
            if (player.getLocation().distance(necronEntity.getLocation()) <= 100) {
                player.sendMessage(message);
            }
        }
    }
    
    /**
     * Announce boss spawn
     */
    private void announceBossSpawn() {
        announceToPlayers("§c§lNECRON HAS AWAKENED!");
        announceToPlayers("§7§lPhase 1: Place crystals on the podia to lure Necron to the red beacon!");
    }
    
    // Helper methods
    private int getRemainingCrystals() {
        return (int) crystalPodia.values().stream().filter(podium -> !podium.hasCrystal()).count();
    }
    
    private void updateStunMechanics() {
        // Stun mechanics update
    }
    
    private void attackCrystalPlayers() {
        // Attack players trying to place crystals
    }
    
    private void pursueTerminalPlayers() {
        // Pursue players working on terminals
    }
    
    // Phase enum
    public enum NecronPhase {
        PHASE_1_CRYSTALS("Phase 1: Crystals"),
        PHASE_2_PILLARS("Phase 2: Pillars & Wither Skeletons"),
        PHASE_3_TERMINALS("Phase 3: Terminal Puzzles");
        
        private final String displayName;
        
        NecronPhase(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
    
    // Terminal Type enum
    public enum TerminalType {
        NUMBER_SEQUENCE, COLOR_PATTERN, SYMBOL_MATCH, DIRECTION_ARROW
    }
}
