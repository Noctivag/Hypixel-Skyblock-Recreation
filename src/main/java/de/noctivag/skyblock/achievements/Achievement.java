package de.noctivag.skyblock.achievements;

/**
 * Achievement enum representing different achievements
 */
public enum Achievement {
    
    // Combat Achievements
    FIRST_KILL("first_kill", "First Blood", "Kill your first mob", AchievementCategory.COMBAT, AchievementRarity.COMMON, 10),
    KILL_100_MOBS("kill_100_mobs", "Mob Slayer", "Kill 100 mobs", AchievementCategory.COMBAT, AchievementRarity.UNCOMMON, 50),
    KILL_1000_MOBS("kill_1000_mobs", "Mob Destroyer", "Kill 1000 mobs", AchievementCategory.COMBAT, AchievementRarity.RARE, 100),
    
    // Mining Achievements
    MINE_100_BLOCKS("mine_100_blocks", "Digger", "Mine 100 blocks", AchievementCategory.MINING, AchievementRarity.COMMON, 10),
    MINE_1000_BLOCKS("mine_1000_blocks", "Miner", "Mine 1000 blocks", AchievementCategory.MINING, AchievementRarity.UNCOMMON, 50),
    MINE_10000_BLOCKS("mine_10000_blocks", "Mining Master", "Mine 10000 blocks", AchievementCategory.MINING, AchievementRarity.RARE, 100),
    
    // Level Achievements
    REACH_LEVEL_10("reach_level_10", "Getting Started", "Reach level 10", AchievementCategory.GENERAL, AchievementRarity.COMMON, 25),
    REACH_LEVEL_50("reach_level_50", "Halfway There", "Reach level 50", AchievementCategory.GENERAL, AchievementRarity.RARE, 100),
    
    // Money Achievements
    FIRST_THOUSAND("first_thousand", "First Thousand", "Earn 1000 coins", AchievementCategory.GENERAL, AchievementRarity.COMMON, 25),
    MILLIONAIRE("millionaire", "Millionaire", "Earn 1 million coins", AchievementCategory.GENERAL, AchievementRarity.LEGENDARY, 500),
    
    // Exploration Achievements
    EXPLORER("explorer", "Explorer", "Visit 10 different locations", AchievementCategory.EXPLORATION, AchievementRarity.UNCOMMON, 50),
    
    // PvP Achievements
    PVP_MASTER("pvp_master", "PvP Master", "Win 10 PvP battles", AchievementCategory.PVP, AchievementRarity.RARE, 100),
    
    // Mining Expert
    MINING_EXPERT("mining_expert", "Mining Expert", "Mine 50000 blocks", AchievementCategory.MINING, AchievementRarity.LEGENDARY, 250);
    
    private final String id;
    private final String name;
    private final String description;
    private final AchievementCategory category;
    private final AchievementRarity rarity;
    private final int points;
    
    Achievement(String id, String name, String description, 
                AchievementCategory category, AchievementRarity rarity, 
                int points) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.points = points;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public AchievementCategory getCategory() {
        return category;
    }
    
    public AchievementRarity getRarity() {
        return rarity;
    }
    
    public int getPoints() {
        return points;
    }
    
    // Enum methods that were expected by the code
    // Note: name() method is final in Enum class and cannot be overridden
    // getId() method is already defined above
    
    // Additional methods expected by the code
    public int getCoinReward() {
        return 100; // Default reward
    }
    
    public void award(org.bukkit.entity.Player player) {
        // Default award implementation
        player.sendMessage("§aAchievement unlocked: " + getDisplayName());
    }
    
    public boolean checkUnlock(org.bukkit.entity.Player player, int progress) {
        // Default unlock check implementation
        return false; // Default: not unlocked
    }
    
    public String getDisplayName() {
        return getName();
    }
    
    public org.bukkit.Material getIcon() {
        return org.bukkit.Material.EMERALD; // Default icon
    }
    
    public int getTierForProgress(int progress) {
        return 1; // Default tier
    }
    
    public int getMaxTier() {
        return 1; // Default max tier
    }
    
    public java.util.List<Integer> getTierThresholds() {
        return java.util.Arrays.asList(100); // Default threshold
    }
}
