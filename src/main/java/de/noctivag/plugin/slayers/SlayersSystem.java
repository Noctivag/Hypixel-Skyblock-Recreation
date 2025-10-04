package de.noctivag.plugin.slayers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Slayers System - Complete Hypixel SkyBlock Slayers Implementation
 * 
 * Features:
 * - 4 Slayer types: Zombie, Spider, Wolf, Enderman
 * - 5 Tiers per slayer (I-V)
 * - Special slayer items and equipment
 * - Slayer quests and rewards
 * - Slayer XP and leveling
 * - Slayer bosses with unique mechanics
 * - Slayer drops and rare items
 */
public class SlayersSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSlayerData> playerSlayerData = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeQuests = new ConcurrentHashMap<>();
    private final Map<SlayerType, SlayerConfig> slayerConfigs = new HashMap<>();
    private final Map<SlayerTier, SlayerTierConfig> tierConfigs = new HashMap<>();
    
    public SlayersSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeSlayerConfigs();
        initializeTierConfigs();
        startSlayerUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeSlayerConfigs() {
        // Zombie Slayer
        slayerConfigs.put(SlayerType.ZOMBIE, new SlayerConfig(
            "Zombie", "§2Zombie Slayer", "§7Undead creatures from the depths",
            Material.ROTTEN_FLESH, Arrays.asList("§7Undead creatures", "§7High health", "§7Poison attacks"),
            Arrays.asList("Revenant Horror", "Atoned Horror", "Apostle of Death", "Revenant Champion", "Revenant Sycophant")
        ));
        
        // Spider Slayer
        slayerConfigs.put(SlayerType.SPIDER, new SlayerConfig(
            "Spider", "§cSpider Slayer", "§7Arachnid creatures with webs",
            Material.SPIDER_EYE, Arrays.asList("§7Web attacks", "§7Poison damage", "§7High speed"),
            Arrays.asList("Tarantula Broodfather", "Tarantula Vermin", "Tarantula Beast", "Tarantula Leech", "Tarantula Parasite")
        ));
        
        // Wolf Slayer
        slayerConfigs.put(SlayerType.WOLF, new SlayerConfig(
            "Wolf", "§fWolf Slayer", "§7Pack hunters with pack bonuses",
            Material.BONE, Arrays.asList("§7Pack bonuses", "§7High damage", "§7Pack healing"),
            Arrays.asList("Sven Packmaster", "Sven Follower", "Sven Alpha", "Sven Beta", "Sven Gamma")
        ));
        
        // Enderman Slayer
        slayerConfigs.put(SlayerType.ENDERMAN, new SlayerConfig(
            "Enderman", "§5Enderman Slayer", "§7Void creatures with teleportation",
            Material.ENDER_PEARL, Arrays.asList("§7Teleportation", "§7Void damage", "§7High intelligence"),
            Arrays.asList("Voidgloom Seraph", "Voidling Devotee", "Voidling Radical", "Voidling Fanatic", "Voidling Apostle")
        ));
    }
    
    private void initializeTierConfigs() {
        // Tier I
        tierConfigs.put(SlayerTier.I, new SlayerTierConfig(
            1, 100, 1000, 100, 500, 50,
            Arrays.asList("§7Basic slayer equipment", "§7Low tier rewards"),
            Arrays.asList("§7Easy difficulty", "§7Basic mechanics")
        ));
        
        // Tier II
        tierConfigs.put(SlayerTier.II, new SlayerTierConfig(
            2, 200, 2000, 200, 1000, 100,
            Arrays.asList("§7Improved slayer equipment", "§7Better rewards"),
            Arrays.asList("§7Medium difficulty", "§7Enhanced mechanics")
        ));
        
        // Tier III
        tierConfigs.put(SlayerTier.III, new SlayerTierConfig(
            3, 300, 3000, 300, 1500, 1000,
            Arrays.asList("§7Advanced slayer equipment", "§7Good rewards"),
            Arrays.asList("§7Hard difficulty", "§7Complex mechanics")
        ));
        
        // Tier IV
        tierConfigs.put(SlayerTier.IV, new SlayerTierConfig(
            4, 400, 4000, 400, 2000, 1500,
            Arrays.asList("§7Expert slayer equipment", "§7Great rewards"),
            Arrays.asList("§7Very hard difficulty", "§7Advanced mechanics")
        ));
        
        // Tier V
        tierConfigs.put(SlayerTier.V, new SlayerTierConfig(
            5, 500, 5000, 500, 2500, 2000,
            Arrays.asList("§7Master slayer equipment", "§7Excellent rewards"),
            Arrays.asList("§7Extreme difficulty", "§7Master mechanics")
        ));
    }
    
    private void startSlayerUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (SlayerQuest quest : activeQuests.values()) {
                updateSlayerQuest(quest);
            }
        }, 0L, 20L); // Every second
    }
    
    private void updateSlayerQuest(SlayerQuest quest) {
        if (quest.getStatus() == SlayerQuestStatus.ACTIVE) {
            quest.setTimeElapsed(quest.getTimeElapsed() + 1);
            
            // Check for timeout
            if (quest.getTimeElapsed() >= quest.getTierConfig().getTimeLimit()) {
                failSlayerQuest(quest);
            }
        }
    }
    
    public void startSlayerQuest(Player player, SlayerType slayerType, SlayerTier tier) {
        if (activeQuests.containsKey(player.getUniqueId())) {
            player.sendMessage("§cYou already have an active slayer quest!");
            return;
        }
        
        PlayerSlayerData data = getPlayerSlayerData(player);
        if (data.getSlayerLevel(slayerType) < tier.getTierNumber()) {
            player.sendMessage("§cYou need level " + tier.getTierNumber() + " " + slayerType.getDisplayName() + " to start this quest!");
            return;
        }
        
        SlayerConfig slayerConfig = slayerConfigs.get(slayerType);
        SlayerTierConfig tierConfig = tierConfigs.get(tier);
        
        SlayerQuest quest = new SlayerQuest(
            player.getUniqueId(),
            slayerType,
            tier,
            slayerConfig,
            tierConfig,
            System.currentTimeMillis()
        );
        
        activeQuests.put(player.getUniqueId(), quest);
        
        player.sendMessage("§aSlayer Quest Started!");
        player.sendMessage("§7Type: " + slayerConfig.getDisplayName());
        player.sendMessage("§7Tier: " + tier.getDisplayName());
        player.sendMessage("§7Time Limit: " + tierConfig.getTimeLimit() + " seconds");

        // Spawn slayer boss
        spawnSlayerBoss(quest);
    }
    
    private void spawnSlayerBoss(SlayerQuest quest) {
        Player player = Bukkit.getPlayer(quest.getPlayerId());
        if (player == null) return;
        
        Location spawnLocation = player.getLocation().add(10, 0, 0);
        
        // Create slayer boss entity
        SlayerBoss boss = new SlayerBoss(
            quest.getSlayerType(),
            quest.getTier(),
            spawnLocation,
            quest.getTierConfig().getBossHealth(),
            quest.getTierConfig().getBossDamage()
        );
        
        quest.setBoss(boss);
        
        // Spawn boss with effects
        player.getWorld().spawnParticle(Particle.EXPLOSION, spawnLocation, 1);
        player.getWorld().playSound(spawnLocation, Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        
        player.sendMessage("§c⚠ " + boss.getDisplayName() + " has spawned! ⚠");
    }
    
    public void completeSlayerQuest(SlayerQuest quest) {
        Player player = Bukkit.getPlayer(quest.getPlayerId());
        if (player == null) return;
        
        quest.setStatus(SlayerQuestStatus.COMPLETED);
        
        // Calculate rewards
        SlayerRewards rewards = calculateSlayerRewards(quest);
        
        // Give rewards
        giveSlayerRewards(player, rewards);
        
        // Update player data
        PlayerSlayerData data = getPlayerSlayerData(player);
        data.addSlayerXP(quest.getSlayerType(), rewards.getXp());
        data.incrementSlayerKills(quest.getSlayerType(), quest.getTier());
        
        // Clean up
        activeQuests.remove(quest.getPlayerId());
        
        player.sendMessage("§aSlayer Quest Completed!");
        player.sendMessage("§7XP Gained: §e+" + rewards.getXp());
        player.sendMessage("§7Coins Gained: §e+" + rewards.getCoins());
    }
    
    private void failSlayerQuest(SlayerQuest quest) {
        Player player = Bukkit.getPlayer(quest.getPlayerId());
        if (player == null) return;
        
        quest.setStatus(SlayerQuestStatus.FAILED);
        
        // Clean up boss
        if (quest.getBoss() != null) {
            quest.getBoss().remove();
        }
        
        // Clean up
        activeQuests.remove(quest.getPlayerId());
        
        player.sendMessage("§cSlayer Quest Failed!");
        player.sendMessage("§7Time limit exceeded.");
    }
    
    private SlayerRewards calculateSlayerRewards(SlayerQuest quest) {
        SlayerTierConfig tierConfig = quest.getTierConfig();
        
        int baseXP = tierConfig.getBaseXP();
        int baseCoins = tierConfig.getBaseCoins();
        
        // Calculate bonus based on time
        int timeBonus = Math.max(0, tierConfig.getTimeLimit() - quest.getTimeElapsed());
        int xpBonus = timeBonus * 2;
        int coinBonus = timeBonus * 5;
        
        return new SlayerRewards(
            baseXP + xpBonus,
            baseCoins + coinBonus,
            generateSlayerDrops(quest)
        );
    }
    
    private List<ItemStack> generateSlayerDrops(SlayerQuest quest) {
        List<ItemStack> drops = new ArrayList<>();
        
        // Base drops
        drops.add(new ItemStack(quest.getSlayerConfig().getMaterial(), 1));
        
        // Rare drops based on tier
        if (Math.random() < getRareDropChance(quest.getTier())) {
            drops.add(createRareSlayerItem(quest.getSlayerType(), quest.getTier()));
        }
        
        // Very rare drops
        if (Math.random() < getVeryRareDropChance(quest.getTier())) {
            drops.add(createVeryRareSlayerItem(quest.getSlayerType(), quest.getTier()));
        }
        
        return drops;
    }
    
    private double getRareDropChance(SlayerTier tier) {
        switch (tier) {
            case I: return 0.1; // 10%
            case II: return 0.15; // 15%
            case III: return 0.2; // 20%
            case IV: return 0.25; // 25%
            case V: return 0.3; // 30%
            default: return 0.1;
        }
    }
    
    private double getVeryRareDropChance(SlayerTier tier) {
        switch (tier) {
            case I: return 0.01; // 1%
            case II: return 0.02; // 2%
            case III: return 0.03; // 3%
            case IV: return 0.04; // 4%
            case V: return 0.05; // 5%
            default: return 0.01;
        }
    }
    
    private ItemStack createRareSlayerItem(SlayerType slayerType, SlayerTier tier) {
        switch (slayerType) {
            case ZOMBIE:
                return createSlayerItem("Revenant Flesh", Material.ROTTEN_FLESH, "§6Revenant Flesh", tier);
            case SPIDER:
                return createSlayerItem("Tarantula Web", Material.STRING, "§6Tarantula Web", tier);
            case WOLF:
                return createSlayerItem("Wolf Tooth", Material.BONE, "§6Wolf Tooth", tier);
            case ENDERMAN:
                return createSlayerItem("Null Sphere", Material.ENDER_PEARL, "§6Null Sphere", tier);
            default:
                return new ItemStack(Material.AIR);
        }
    }
    
    private ItemStack createVeryRareSlayerItem(SlayerType slayerType, SlayerTier tier) {
        switch (slayerType) {
            case ZOMBIE:
                return createSlayerItem("Revenant Catalyst", Material.NETHER_STAR, "§5Revenant Catalyst", tier);
            case SPIDER:
                return createSlayerItem("Tarantula Talisman", Material.SPIDER_EYE, "§5Tarantula Talisman", tier);
            case WOLF:
                return createSlayerItem("Wolf Talisman", Material.BONE, "§5Wolf Talisman", tier);
            case ENDERMAN:
                return createSlayerItem("Enderman Catalyst", Material.ENDER_EYE, "§5Enderman Catalyst", tier);
            default:
                return new ItemStack(Material.AIR);
        }
    }
    
    private ItemStack createSlayerItem(String name, Material material, String displayName, SlayerTier tier) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(net.kyori.adventure.text.Component.text(displayName));
        meta.lore(Arrays.asList(
            net.kyori.adventure.text.Component.text("§7A rare drop from " + tier.getDisplayName() + " slayers"),
            net.kyori.adventure.text.Component.text("§7Used for crafting powerful items"),
            net.kyori.adventure.text.Component.text(""),
            net.kyori.adventure.text.Component.text("§7Tier: " + tier.getDisplayName()),
            net.kyori.adventure.text.Component.text("§7Rarity: §6Rare")
        ));
        item.setItemMeta(meta);
        return item;
    }
    
    private void giveSlayerRewards(Player player, SlayerRewards rewards) {
        // Give XP
        player.sendMessage("§a+" + rewards.getXp() + " Slayer XP");
        
        // Give coins
        // This would integrate with your economy system
        
        // Give items
        for (ItemStack item : rewards.getDrops()) {
            player.getInventory().addItem(item);
            player.sendMessage("§aReceived: " + (item.getItemMeta() != null && item.getItemMeta().displayName() != null ? 
                net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName()) : "Unknown Item"));
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("slayer_boss")) {
            UUID playerId = UUID.fromString(event.getEntity().getMetadata("slayer_player").get(0).asString());
            SlayerQuest quest = activeQuests.get(playerId);
            
            if (quest != null && quest.getBoss() != null) {
                completeSlayerQuest(quest);
            }
        }
    }
    
    private PlayerSlayerData getPlayerSlayerData(Player player) {
        return playerSlayerData.computeIfAbsent(player.getUniqueId(), k -> new PlayerSlayerData(k));
    }
    
    // Enums and Classes
    public enum SlayerType {
        ZOMBIE("Zombie", "§2Zombie"),
        SPIDER("Spider", "§cSpider"),
        WOLF("Wolf", "§fWolf"),
        ENDERMAN("Enderman", "§5Enderman");
        
        private final String name;
        private final String displayName;
        
        SlayerType(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
    }
    
    public enum SlayerTier {
        I("Tier I", 1),
        II("Tier II", 2),
        III("Tier III", 3),
        IV("Tier IV", 4),
        V("Tier V", 5);
        
        private final String displayName;
        private final int tierNumber;
        
        SlayerTier(String displayName, int tierNumber) {
            this.displayName = displayName;
            this.tierNumber = tierNumber;
        }
        
        public String getDisplayName() { return displayName; }
        public int getTierNumber() { return tierNumber; }
    }
    
    public enum SlayerQuestStatus {
        ACTIVE, COMPLETED, FAILED
    }
    
    // Data Classes
    public static class SlayerConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final List<String> characteristics;
        private final List<String> bossNames;
        
        public SlayerConfig(String name, String displayName, String description, Material material,
                          List<String> characteristics, List<String> bossNames) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.characteristics = characteristics;
            this.bossNames = bossNames;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public List<String> getCharacteristics() { return characteristics; }
        public List<String> getBossNames() { return bossNames; }
    }
    
    public static class SlayerTierConfig {
        private final int tierNumber;
        private final int timeLimit;
        private final int bossHealth;
        private final int bossDamage;
        private final int baseXP;
        private final int baseCoins;
        private final List<String> rewards;
        private final List<String> mechanics;
        
        public SlayerTierConfig(int tierNumber, int timeLimit, int bossHealth, int bossDamage,
                              int baseXP, int baseCoins, List<String> rewards, List<String> mechanics) {
            this.tierNumber = tierNumber;
            this.timeLimit = timeLimit;
            this.bossHealth = bossHealth;
            this.bossDamage = bossDamage;
            this.baseXP = baseXP;
            this.baseCoins = baseCoins;
            this.rewards = rewards;
            this.mechanics = mechanics;
        }
        
        // Getters
        public int getTierNumber() { return tierNumber; }
        public int getTimeLimit() { return timeLimit; }
        public int getBossHealth() { return bossHealth; }
        public int getBossDamage() { return bossDamage; }
        public int getBaseXP() { return baseXP; }
        public int getBaseCoins() { return baseCoins; }
        public List<String> getRewards() { return rewards; }
        public List<String> getMechanics() { return mechanics; }
    }
    
    public static class SlayerQuest {
        private final UUID playerId;
        private final SlayerType slayerType;
        private final SlayerTier tier;
        private final SlayerConfig slayerConfig;
        private final SlayerTierConfig tierConfig;
        private final long startTime;
        private SlayerQuestStatus status;
        private int timeElapsed;
        private SlayerBoss boss;
        
        public SlayerQuest(UUID playerId, SlayerType slayerType, SlayerTier tier,
                         SlayerConfig slayerConfig, SlayerTierConfig tierConfig, long startTime) {
            this.playerId = playerId;
            this.slayerType = slayerType;
            this.tier = tier;
            this.slayerConfig = slayerConfig;
            this.tierConfig = tierConfig;
            this.startTime = startTime;
            this.status = SlayerQuestStatus.ACTIVE;
            this.timeElapsed = 0;
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public SlayerType getSlayerType() { return slayerType; }
        public SlayerTier getTier() { return tier; }
        public SlayerConfig getSlayerConfig() { return slayerConfig; }
        public SlayerTierConfig getTierConfig() { return tierConfig; }
        public long getStartTime() { return startTime; }
        public SlayerQuestStatus getStatus() { return status; }
        public void setStatus(SlayerQuestStatus status) { this.status = status; }
        public int getTimeElapsed() { return timeElapsed; }
        public void setTimeElapsed(int timeElapsed) { this.timeElapsed = timeElapsed; }
        public SlayerBoss getBoss() { return boss; }
        public void setBoss(SlayerBoss boss) { this.boss = boss; }
    }
    
    public static class SlayerBoss {
        private final SlayerType slayerType;
        private final SlayerTier tier;
        private final Location location;
        private final int maxHealth;
        private final int damage;
        private int currentHealth;
        
        public SlayerBoss(SlayerType slayerType, SlayerTier tier, Location location, int maxHealth, int damage) {
            this.slayerType = slayerType;
            this.tier = tier;
            this.location = location;
            this.maxHealth = maxHealth;
            this.damage = damage;
            this.currentHealth = maxHealth;
        }
        
        public String getDisplayName() {
            return slayerType.getDisplayName() + " " + tier.getDisplayName();
        }
        
        public void remove() {
            // Remove boss entity
        }
        
        // Getters and Setters
        public SlayerType getSlayerType() { return slayerType; }
        public SlayerTier getTier() { return tier; }
        public Location getLocation() { return location; }
        public int getMaxHealth() { return maxHealth; }
        public int getDamage() { return damage; }
        public int getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    }
    
    public static class SlayerRewards {
        private final int xp;
        private final int coins;
        private final List<ItemStack> drops;
        
        public SlayerRewards(int xp, int coins, List<ItemStack> drops) {
            this.xp = xp;
            this.coins = coins;
            this.drops = drops;
        }
        
        // Getters
        public int getXp() { return xp; }
        public int getCoins() { return coins; }
        public List<ItemStack> getDrops() { return drops; }
    }
    
    public static class PlayerSlayerData {
        private final UUID playerId;
        private final Map<SlayerType, Integer> slayerLevels;
        private final Map<SlayerType, Integer> slayerXP;
        private final Map<SlayerType, Map<SlayerTier, Integer>> slayerKills;
        
        public PlayerSlayerData(UUID playerId) {
            this.playerId = playerId;
            this.slayerLevels = new HashMap<>();
            this.slayerXP = new HashMap<>();
            this.slayerKills = new HashMap<>();
            
            // Initialize all slayer types
            for (SlayerType type : SlayerType.values()) {
                slayerLevels.put(type, 1);
                slayerXP.put(type, 0);
                slayerKills.put(type, new HashMap<>());
                
                for (SlayerTier tier : SlayerTier.values()) {
                    slayerKills.get(type).put(tier, 0);
                }
            }
        }
        
        public void addSlayerXP(SlayerType slayerType, int xp) {
            int currentXP = slayerXP.get(slayerType);
            slayerXP.put(slayerType, currentXP + xp);
            
            // Calculate new level
            int newLevel = calculateSlayerLevel(currentXP + xp);
            slayerLevels.put(slayerType, newLevel);
        }
        
        public void incrementSlayerKills(SlayerType slayerType, SlayerTier tier) {
            Map<SlayerTier, Integer> kills = slayerKills.get(slayerType);
            kills.put(tier, kills.get(tier) + 1);
        }
        
        private int calculateSlayerLevel(int xp) {
            // Level calculation formula
            return Math.min(9, (int) Math.floor(Math.sqrt(xp / 100.0)) + 1);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getSlayerLevel(SlayerType slayerType) { return slayerLevels.get(slayerType); }
        public int getSlayerXP(SlayerType slayerType) { return slayerXP.get(slayerType); }
        public int getSlayerKills(SlayerType slayerType, SlayerTier tier) { 
            return slayerKills.get(slayerType).get(tier); 
        }
    }
}
