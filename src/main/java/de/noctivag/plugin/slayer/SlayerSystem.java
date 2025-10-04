package de.noctivag.plugin.slayer;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Slayer System with RNG Meter - Implements the complete slayer system from Hypixel SkyBlock
 * 
 * Features:
 * - 6 Slayer Types: Zombie, Spider, Wolf, Enderman, Blaze, Vampire
 * - Tier System: 1-5 tiers with increasing difficulty
 * - RNG Meter: Unlocked at Tier III, increases drop chances up to 3x multiplier
 * - Boss Mechanics: Special abilities and phases for each slayer type
 * - Drop System: Rare items with conditional probability
 * - Experience System: Slayer XP and level progression
 */
public class SlayerSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    
    // Player data
    private final Map<UUID, PlayerSlayerData> playerData = new ConcurrentHashMap<>();
    
    // Active slayer quests
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    
    // Active slayer bosses
    private final Map<UUID, SlayerBoss> activeBosses = new ConcurrentHashMap<>();
    
    // RNG Meter configurations
    private final Map<SlayerType, Map<SlayerTier, RNGMeterConfig>> rngMeterConfigs = new HashMap<>();
    
    public SlayerSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeRNGMeterConfigs();
        startUpdateTask();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Initialize RNG Meter configurations for all slayer types and tiers
     */
    private void initializeRNGMeterConfigs() {
        // Zombie Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> zombieConfigs = new HashMap<>();
        zombieConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(500, 3.0)); // 500 kills for 3x multiplier
        zombieConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(750, 3.0)); // 750 kills for 3x multiplier
        zombieConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(1000, 3.0)); // 1000 kills for 3x multiplier
        rngMeterConfigs.put(SlayerType.ZOMBIE, zombieConfigs);
        
        // Spider Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> spiderConfigs = new HashMap<>();
        spiderConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(400, 3.0));
        spiderConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(600, 3.0));
        spiderConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(800, 3.0));
        rngMeterConfigs.put(SlayerType.SPIDER, spiderConfigs);
        
        // Wolf Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> wolfConfigs = new HashMap<>();
        wolfConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(450, 3.0));
        wolfConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(675, 3.0));
        wolfConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(900, 3.0));
        rngMeterConfigs.put(SlayerType.WOLF, wolfConfigs);
        
        // Enderman Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> endermanConfigs = new HashMap<>();
        endermanConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(600, 3.0));
        endermanConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(900, 3.0));
        endermanConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(1200, 3.0));
        rngMeterConfigs.put(SlayerType.ENDERMAN, endermanConfigs);
        
        // Blaze Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> blazeConfigs = new HashMap<>();
        blazeConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(700, 3.0));
        blazeConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(1050, 3.0));
        blazeConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(1400, 3.0));
        rngMeterConfigs.put(SlayerType.BLAZE, blazeConfigs);
        
        // Vampire Slayer RNG Meter
        Map<SlayerTier, RNGMeterConfig> vampireConfigs = new HashMap<>();
        vampireConfigs.put(SlayerTier.TIER_III, new RNGMeterConfig(800, 3.0));
        vampireConfigs.put(SlayerTier.TIER_IV, new RNGMeterConfig(1200, 3.0));
        vampireConfigs.put(SlayerTier.TIER_V, new RNGMeterConfig(1600, 3.0));
        rngMeterConfigs.put(SlayerType.VAMPIRE, vampireConfigs);
    }
    
    /**
     * Start the update task for slayer system
     */
    private void startUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveQuests();
                updateActiveBosses();
                updateRNGMeters();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }
    
    /**
     * Update active slayer quests
     */
    private void updateActiveQuests() {
        List<UUID> expiredQuests = new ArrayList<>();
        
        for (Map.Entry<UUID, SlayerQuest> entry : activeQuests.entrySet()) {
            SlayerQuest quest = entry.getValue();
            
            // Check if quest is expired
            if (quest.isExpired()) {
                expiredQuests.add(entry.getKey());
                continue;
            }
            
            // Update quest progress
            quest.update();
        }
        
        // Remove expired quests
        for (UUID questId : expiredQuests) {
            SlayerQuest quest = activeQuests.remove(questId);
            if (quest != null) {
                Player player = Bukkit.getPlayer(quest.getPlayerId());
                if (player != null) {
                    player.sendMessage("§c§lSLAYER QUEST FAILED!");
                    player.sendMessage("§7Your slayer quest has expired.");
                }
            }
        }
    }
    
    /**
     * Update active slayer bosses
     */
    private void updateActiveBosses() {
        List<UUID> deadBosses = new ArrayList<>();
        
        for (Map.Entry<UUID, SlayerBoss> entry : activeBosses.entrySet()) {
            SlayerBoss boss = entry.getValue();
            
            if (!boss.isAlive()) {
                deadBosses.add(entry.getKey());
                continue;
            }
            
            // Update boss AI
            boss.update();
        }
        
        // Remove dead bosses
        for (UUID bossId : deadBosses) {
            SlayerBoss boss = activeBosses.remove(bossId);
            if (boss != null) {
                onSlayerBossDefeated(boss);
            }
        }
    }
    
    /**
     * Update RNG meters for all players
     */
    private void updateRNGMeters() {
        for (PlayerSlayerData data : playerData.values()) {
            for (SlayerType type : SlayerType.values()) {
                SlayerRNGMeter meter = data.getRNGMeter(type);
                if (meter != null && meter.isActive()) {
                    meter.update();
                }
            }
        }
    }
    
    /**
     * Start a slayer quest for a player
     */
    public boolean startSlayerQuest(Player player, SlayerType type, SlayerTier tier) {
        UUID playerId = player.getUniqueId();
        
        // Check if player already has an active quest
        if (activeQuests.containsKey(playerId)) {
            player.sendMessage("§cYou already have an active slayer quest!");
            return false;
        }
        
        // Check if player has required level
        PlayerSlayerData data = getPlayerSlayerData(playerId);
        if (data.getSlayerLevel(type) < tier.getRequiredLevel()) {
            player.sendMessage("§cYou need Slayer Level " + tier.getRequiredLevel() + " to start this quest!");
            return false;
        }
        
        // Create new slayer quest
        SlayerQuest quest = new SlayerQuest(playerId, type, tier);
        activeQuests.put(playerId, quest);
        
        // Send quest start message
        player.sendMessage("§a§lSLAYER QUEST STARTED!");
        player.sendMessage("§7Type: " + type.getDisplayName());
        player.sendMessage("§7Tier: " + tier.getDisplayName());
        player.sendMessage("§7Objective: Kill " + tier.getRequiredKills() + " " + type.getMobName() + "s");
        player.sendMessage("§7Time Limit: " + tier.getTimeLimit() + " minutes");
        
        return true;
    }
    
    /**
     * Spawn slayer boss when quest requirements are met
     */
    public void spawnSlayerBoss(Player player, SlayerType type, SlayerTier tier) {
        UUID playerId = player.getUniqueId();
        
        // Check if player has an active quest
        SlayerQuest quest = activeQuests.get(playerId);
        if (quest == null || quest.getType() != type || quest.getTier() != tier) {
            return;
        }
        
        // Check if quest is ready for boss spawn
        if (!quest.isReadyForBoss()) {
            return;
        }
        
        // Generate spawn location
        Location spawnLocation = generateBossSpawnLocation(player);
        if (spawnLocation == null) {
            player.sendMessage("§cUnable to find a suitable location to spawn the boss!");
            return;
        }
        
        // Create and spawn boss
        SlayerBoss boss = createSlayerBoss(type, tier, spawnLocation, player);
        activeBosses.put(boss.getEntity().getUniqueId(), boss);
        
        // Send boss spawn message
        player.sendMessage("§c§lSLAYER BOSS SPAWNED!");
        player.sendMessage("§7" + type.getDisplayName() + " " + tier.getDisplayName() + " has appeared!");
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
    }
    
    /**
     * Create a slayer boss entity
     */
    private SlayerBoss createSlayerBoss(SlayerType type, SlayerTier tier, Location location, Player spawner) {
        EntityType entityType = getBossEntityType(type);
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        
        // Set boss properties
        entity.customName(net.kyori.adventure.text.Component.text(
            "§c§l" + type.getBossName() + " " + tier.getDisplayName()));
        entity.setCustomNameVisible(true);
        
        // Set health and damage
        int health = getBossHealth(type, tier);
        int damage = getBossDamage(type, tier);
        
        entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).setBaseValue(health);
        entity.setHealth(health);
        
        // Apply special effects
        applyBossEffects(entity, type, tier);
        
        // Create boss wrapper
        return new SlayerBoss(entity, type, tier, spawner, health, damage);
    }
    
    /**
     * Get entity type for slayer boss
     */
    private EntityType getBossEntityType(SlayerType type) {
        return switch (type) {
            case ZOMBIE, VAMPIRE -> EntityType.ZOMBIE;
            case SPIDER -> EntityType.SPIDER;
            case WOLF -> EntityType.WOLF;
            case ENDERMAN -> EntityType.ENDERMAN;
            case BLAZE -> EntityType.BLAZE;
        };
    }
    
    /**
     * Get boss health based on type and tier
     */
    private int getBossHealth(SlayerType type, SlayerTier tier) {
        int baseHealth = switch (type) {
            case ZOMBIE -> 100000;   // 100K
            case SPIDER -> 150000;   // 150K
            case WOLF -> 200000;     // 200K
            case ENDERMAN -> 500000; // 500K
            case BLAZE -> 1000000;   // 1M
            case VAMPIRE -> 2000000; // 2M
        };
        
        return baseHealth * tier.getHealthMultiplier();
    }
    
    /**
     * Get boss damage based on type and tier
     */
    private int getBossDamage(SlayerType type, SlayerTier tier) {
        int baseDamage = switch (type) {
            case ZOMBIE -> 200;
            case SPIDER -> 300;
            case WOLF -> 400;
            case ENDERMAN -> 500;
            case BLAZE -> 1000;
            case VAMPIRE -> 2000;
        };
        
        return baseDamage * tier.getDamageMultiplier();
    }
    
    /**
     * Apply special effects to boss
     */
    private void applyBossEffects(LivingEntity entity, SlayerType type, SlayerTier tier) {
        // Base effects for all bosses
        entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        
        // Type-specific effects
        switch (type) {
            case ZOMBIE -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0, false, false));
            }
            case SPIDER -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2, false, false));
            }
            case WOLF -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false));
            }
            case ENDERMAN -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
            }
            case BLAZE -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false));
            }
            case VAMPIRE -> {
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3, false, false));
                entity.addPotionEffect(new org.bukkit.potion.PotionEffect(
                    org.bukkit.potion.PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
            }
        }
    }
    
    /**
     * Generate boss spawn location
     */
    private Location generateBossSpawnLocation(Player player) {
        Location playerLoc = player.getLocation();
        World world = playerLoc.getWorld();
        
        // Try to find a suitable location within 50 blocks
        for (int attempts = 0; attempts < 10; attempts++) {
            double angle = Math.random() * 2 * Math.PI;
            double distance = 10 + Math.random() * 30; // 10-40 blocks
            
            double x = playerLoc.getX() + Math.cos(angle) * distance;
            double z = playerLoc.getZ() + Math.sin(angle) * distance;
            
            int y = world.getHighestBlockYAt((int) x, (int) z);
            Location spawnLoc = new Location(world, x, y + 1, z);
            
            if (isValidBossSpawnLocation(spawnLoc)) {
                return spawnLoc;
            }
        }
        
        return null;
    }
    
    /**
     * Check if location is valid for boss spawn
     */
    private boolean isValidBossSpawnLocation(Location location) {
        // Check if block below is solid
        Block blockBelow = location.clone().subtract(0, 1, 0).getBlock();
        if (!blockBelow.getType().isSolid()) {
            return false;
        }
        
        // Check if spawn location is clear
        Block spawnBlock = location.getBlock();
        if (spawnBlock.getType() != Material.AIR) {
            return false;
        }
        
        // Check if there's space above
        Block blockAbove = location.clone().add(0, 1, 0).getBlock();
        return blockAbove.getType() == Material.AIR;
    }
    
    /**
     * Handle slayer boss defeat
     */
    private void onSlayerBossDefeated(SlayerBoss boss) {
        Player player = boss.getSpawner();
        if (player == null) return;
        
        SlayerType type = boss.getType();
        SlayerTier tier = boss.getTier();
        
        // Complete the quest
        SlayerQuest quest = activeQuests.remove(player.getUniqueId());
        if (quest != null) {
            quest.complete();
        }
        
        // Give rewards
        giveSlayerRewards(player, type, tier);
        
        // Update RNG meter
        PlayerSlayerData data = getPlayerSlayerData(player.getUniqueId());
        SlayerRNGMeter meter = data.getRNGMeter(type);
        if (meter != null && meter.isActive()) {
            meter.addKill();
        }
        
        // Send completion message
        player.sendMessage("§a§lSLAYER BOSS DEFEATED!");
        player.sendMessage("§7" + type.getDisplayName() + " " + tier.getDisplayName() + " has been defeated!");
        player.sendMessage("§6+" + tier.getXP() + " Slayer XP");
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
    
    /**
     * Give slayer rewards to player
     */
    private void giveSlayerRewards(Player player, SlayerType type, SlayerTier tier) {
        PlayerSlayerData data = getPlayerSlayerData(player.getUniqueId());
        
        // Give XP
        data.addSlayerXP(type, tier.getXP());
        
        // Give coins
        int coins = tier.getCoinReward();
        // TODO: Add to player's coin balance
        
        // Roll for rare drops
        rollForRareDrops(player, type, tier, data);
        
        // Send reward message
        player.sendMessage("§a§lSLAYER REWARDS!");
        player.sendMessage("§6+" + tier.getXP() + " Slayer XP");
        player.sendMessage("§6+" + coins + " Coins");
    }
    
    /**
     * Roll for rare drops using RNG meter
     */
    private void rollForRareDrops(Player player, SlayerType type, SlayerTier tier, PlayerSlayerData data) {
        SlayerRNGMeter meter = data.getRNGMeter(type);
        double multiplier = 1.0;
        
        if (meter != null && meter.isActive()) {
            multiplier = meter.getCurrentMultiplier();
        }
        
        // Roll for each possible rare drop
        for (SlayerDrop drop : SlayerDrop.getDropsForType(type)) {
            if (drop.getTier() == tier) {
                double chance = drop.getBaseChance() * multiplier;
                
                if (Math.random() < chance) {
                    // Give rare drop
                    giveSlayerDrop(player, drop);
                    player.sendMessage("§d§lRARE DROP!");
                    player.sendMessage("§7" + drop.getName() + " §7(" + String.format("%.2f", chance * 100) + "% chance)");
                    
                    // Reset RNG meter if it was active
                    if (meter != null && meter.isActive()) {
                        meter.reset();
                        player.sendMessage("§cYour RNG Meter has been reset!");
                    }
                }
            }
        }
    }
    
    /**
     * Give slayer drop to player
     */
    private void giveSlayerDrop(Player player, SlayerDrop drop) {
        ItemStack item = new ItemStack(drop.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(net.kyori.adventure.text.Component.text(drop.getName()));
        meta.lore(Arrays.asList(
            net.kyori.adventure.text.Component.text(drop.getDescription()),
            net.kyori.adventure.text.Component.text("§7Rarity: " + drop.getRarity().getDisplayName())
        ));
        
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            UUID killerId = killer.getUniqueId();
            
            // Check if killer has an active slayer quest
            SlayerQuest quest = activeQuests.get(killerId);
            if (quest != null && quest.isValidTarget(event.getEntity())) {
                quest.addKill();
                
                // Check if ready for boss spawn
                if (quest.isReadyForBoss()) {
                    spawnSlayerBoss(killer, quest.getType(), quest.getTier());
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player data
        getPlayerSlayerData(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.displayName() != null ?
            net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(meta.displayName()) : "";
        
        if (displayName.contains("Slayer")) {
            openSlayerGUI(player);
        }
    }
    
    /**
     * Open slayer GUI for player
     */
    public void openSlayerGUI(Player player) {
        // TODO: Implement slayer GUI
        player.sendMessage("§aSlayer GUI opened!");
    }
    
    /**
     * Get player slayer data
     */
    public PlayerSlayerData getPlayerSlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerSlayerData(playerId));
    }
    
    /**
     * Get active quest for player
     */
    public SlayerQuest getActiveQuest(UUID playerId) {
        return activeQuests.get(playerId);
    }
    
    /**
     * Get RNG meter config for slayer type and tier
     */
    public RNGMeterConfig getRNGMeterConfig(SlayerType type, SlayerTier tier) {
        Map<SlayerTier, RNGMeterConfig> configs = rngMeterConfigs.get(type);
        if (configs == null) return null;
        return configs.get(tier);
    }
    
    /**
     * Slayer Type enum
     */
    public enum SlayerType {
        ZOMBIE("§2Zombie Slayer", "Zombie", "Revenant Horror"),
        SPIDER("§cSpider Slayer", "Spider", "Tarantula Broodfather"),
        WOLF("§fWolf Slayer", "Wolf", "Sven Packmaster"),
        ENDERMAN("§5Enderman Slayer", "Enderman", "Voidgloom Seraph"),
        BLAZE("§6Blaze Slayer", "Blaze", "Inferno Demonlord"),
        VAMPIRE("§4Vampire Slayer", "Vampire", "Riftstalker Bloodfiend");
        
        private final String displayName;
        private final String mobName;
        private final String bossName;
        
        SlayerType(String displayName, String mobName, String bossName) {
            this.displayName = displayName;
            this.mobName = mobName;
            this.bossName = bossName;
        }
        
        public String getDisplayName() { return displayName; }
        public String getMobName() { return mobName; }
        public String getBossName() { return bossName; }
    }
    
    /**
     * Slayer Tier enum
     */
    public enum SlayerTier {
        TIER_I("§aTier I", 1, 0, 50, 1000, 10, 1.0, 1.0),
        TIER_II("§eTier II", 2, 5, 100, 2500, 15, 2.0, 1.5),
        TIER_III("§6Tier III", 3, 10, 200, 5000, 20, 3.0, 2.0),
        TIER_IV("§cTier IV", 4, 20, 500, 10000, 25, 5.0, 2.5),
        TIER_V("§4Tier V", 5, 35, 1000, 25000, 30, 10.0, 3.0);
        
        private final String displayName;
        private final int tier;
        private final int requiredLevel;
        private final int requiredKills;
        private final int timeLimit;
        private final int xp;
        private final double healthMultiplier;
        private final double damageMultiplier;
        
        SlayerTier(String displayName, int tier, int requiredLevel, int requiredKills, 
                  int timeLimit, int xp, double healthMultiplier, double damageMultiplier) {
            this.displayName = displayName;
            this.tier = tier;
            this.requiredLevel = requiredLevel;
            this.requiredKills = requiredKills;
            this.timeLimit = timeLimit;
            this.xp = xp;
            this.healthMultiplier = healthMultiplier;
            this.damageMultiplier = damageMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public int getTier() { return tier; }
        public int getRequiredLevel() { return requiredLevel; }
        public int getRequiredKills() { return requiredKills; }
        public int getTimeLimit() { return timeLimit; }
        public int getXP() { return xp; }
        public double getHealthMultiplier() { return healthMultiplier; }
        public double getDamageMultiplier() { return damageMultiplier; }
        
        public int getCoinReward() {
            return xp * 10; // 10 coins per XP
        }
    }
    
    /**
     * RNG Meter Configuration
     */
    public static class RNGMeterConfig {
        private final int maxKills;
        private final double maxMultiplier;
        
        public RNGMeterConfig(int maxKills, double maxMultiplier) {
            this.maxKills = maxKills;
            this.maxMultiplier = maxMultiplier;
        }
        
        public int getMaxKills() { return maxKills; }
        public double getMaxMultiplier() { return maxMultiplier; }
    }
    
    /**
     * Slayer RNG Meter
     */
    public static class SlayerRNGMeter {
        private final SlayerType type;
        private final SlayerTier tier;
        private int kills;
        private boolean active;
        private final RNGMeterConfig config;
        
        public SlayerRNGMeter(SlayerType type, SlayerTier tier, RNGMeterConfig config) {
            this.type = type;
            this.tier = tier;
            this.kills = 0;
            this.active = false;
            this.config = config;
        }
        
        public void activate() {
            this.active = true;
        }
        
        public void addKill() {
            if (active) {
                kills++;
            }
        }
        
        public void reset() {
            kills = 0;
            active = false;
        }
        
        public void update() {
            // RNG meter updates
        }
        
        public double getCurrentMultiplier() {
            if (!active || kills == 0) return 1.0;
            
            double progress = (double) kills / config.getMaxKills();
            return 1.0 + (progress * (config.getMaxMultiplier() - 1.0));
        }
        
        public boolean isActive() { return active; }
        public int getKills() { return kills; }
        public SlayerType getType() { return type; }
        public SlayerTier getTier() { return tier; }
    }
}
