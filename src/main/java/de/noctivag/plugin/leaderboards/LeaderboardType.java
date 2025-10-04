package de.noctivag.plugin.leaderboards;
import org.bukkit.inventory.ItemStack;

/**
 * Types of leaderboards available in the system
 */
public enum LeaderboardType {
    
    // Skill Leaderboards
    COMBAT_LEVEL("Combat Level", "combat_level", "Combat"),
    MINING_LEVEL("Mining Level", "mining_level", "Mining"),
    FORAGING_LEVEL("Foraging Level", "foraging_level", "Foraging"),
    FARMING_LEVEL("Farming Level", "farming_level", "Farming"),
    ENCHANTING_LEVEL("Enchanting Level", "enchanting_level", "Enchanting"),
    FISHING_LEVEL("Fishing Level", "fishing_level", "Fishing"),
    ALCHEMY_LEVEL("Alchemy Level", "alchemy_level", "Alchemy"),
    TAMING_LEVEL("Taming Level", "taming_level", "Taming"),
    CARPENTRY_LEVEL("Carpentry Level", "carpentry_level", "Carpentry"),
    RUNECRAFTING_LEVEL("Runecrafting Level", "runecrafting_level", "Runecrafting"),
    SOCIAL_LEVEL("Social Level", "social_level", "Social"),
    
    // Collection Leaderboards
    COBBLESTONE_COLLECTION("Cobblestone Collection", "cobblestone_collection", "Collection"),
    COAL_COLLECTION("Coal Collection", "coal_collection", "Collection"),
    IRON_COLLECTION("Iron Collection", "iron_collection", "Collection"),
    GOLD_COLLECTION("Gold Collection", "gold_collection", "Collection"),
    DIAMOND_COLLECTION("Diamond Collection", "diamond_collection", "Collection"),
    EMERALD_COLLECTION("Emerald Collection", "emerald_collection", "Collection"),
    REDSTONE_COLLECTION("Redstone Collection", "redstone_collection", "Collection"),
    LAPIS_COLLECTION("Lapis Collection", "lapis_collection", "Collection"),
    QUARTZ_COLLECTION("Quartz Collection", "quartz_collection", "Collection"),
    OBSIDIAN_COLLECTION("Obsidian Collection", "obsidian_collection", "Collection"),
    GLOWSTONE_COLLECTION("Glowstone Collection", "glowstone_collection", "Collection"),
    GRAVEL_COLLECTION("Gravel Collection", "gravel_collection", "Collection"),
    SAND_COLLECTION("Sand Collection", "sand_collection", "Collection"),
    RED_SAND_COLLECTION("Red Sand Collection", "red_sand_collection", "Collection"),
    END_STONE_COLLECTION("End Stone Collection", "end_stone_collection", "Collection"),
    NETHER_STAR_COLLECTION("Nether Star Collection", "nether_star_collection", "Collection"),
    
    // Minion Leaderboards
    MINION_COLLECTION("Minion Collection", "minion_collection", "Minions"),
    MINION_UPGRADES("Minion Upgrades", "minion_upgrades", "Minions"),
    MINION_EFFICIENCY("Minion Efficiency", "minion_efficiency", "Minions"),
    
    // Pet Leaderboards
    PET_COLLECTION("Pet Collection", "pet_collection", "Pets"),
    PET_LEVELS("Pet Levels", "pet_levels", "Pets"),
    PET_EXPERIENCE("Pet Experience", "pet_experience", "Pets"),
    
    // Dungeon Leaderboards
    DUNGEON_SCORE("Dungeon Score", "dungeon_score", "Dungeons"),
    DUNGEON_COMPLETIONS("Dungeon Completions", "dungeon_completions", "Dungeons"),
    DUNGEON_BEST_TIME("Dungeon Best Time", "dungeon_best_time", "Dungeons"),
    
    // Slayer Leaderboards
    SLAYER_SCORE("Slayer Score", "slayer_score", "Slayers"),
    SLAYER_COMPLETIONS("Slayer Completions", "slayer_completions", "Slayers"),
    SLAYER_BEST_TIME("Slayer Best Time", "slayer_best_time", "Slayers"),
    
    // Economy Leaderboards
    COINS("Coins", "coins", "Economy"),
    BANK_BALANCE("Bank Balance", "bank_balance", "Economy"),
    AUCTION_HOUSE_SALES("Auction House Sales", "auction_house_sales", "Economy"),
    BAZAAR_SALES("Bazaar Sales", "bazaar_sales", "Economy"),
    
    // Guild Leaderboards
    GUILD_LEVEL("Guild Level", "guild_level", "Guilds"),
    GUILD_EXPERIENCE("Guild Experience", "guild_experience", "Guilds"),
    GUILD_MEMBERS("Guild Members", "guild_members", "Guilds"),
    
    // Island Leaderboards
    ISLAND_LEVEL("Island Level", "island_level", "Islands"),
    ISLAND_EXPERIENCE("Island Experience", "island_experience", "Islands"),
    ISLAND_SIZE("Island Size", "island_size", "Islands"),
    
    // Achievement Leaderboards
    ACHIEVEMENTS("Achievements", "achievements", "Achievements"),
    ACHIEVEMENT_POINTS("Achievement Points", "achievement_points", "Achievements"),
    
    // Social Leaderboards
    FRIENDS("Friends", "friends", "Social"),
    PARTY_CREATIONS("Party Creations", "party_creations", "Social"),
    PARTY_JOINS("Party Joins", "party_joins", "Social"),
    
    // Overall Leaderboards
    OVERALL_LEVEL("Overall Level", "overall_level", "Overall"),
    OVERALL_EXPERIENCE("Overall Experience", "overall_experience", "Overall"),
    OVERALL_SCORE("Overall Score", "overall_score", "Overall");
    
    private final String displayName;
    private final String databaseKey;
    private final String category;
    
    LeaderboardType(String displayName, String databaseKey, String category) {
        this.displayName = displayName;
        this.databaseKey = databaseKey;
        this.category = category;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDatabaseKey() {
        return databaseKey;
    }
    
    public String getCategory() {
        return category;
    }
    
    public boolean isSkillLeaderboard() {
        return category.equals("Combat") || category.equals("Mining") || 
               category.equals("Foraging") || category.equals("Farming") ||
               category.equals("Enchanting") || category.equals("Fishing") ||
               category.equals("Alchemy") || category.equals("Taming") ||
               category.equals("Carpentry") || category.equals("Runecrafting") ||
               category.equals("Social");
    }
    
    public boolean isCollectionLeaderboard() {
        return category.equals("Collection");
    }
    
    public boolean isMinionLeaderboard() {
        return category.equals("Minions");
    }
    
    public boolean isPetLeaderboard() {
        return category.equals("Pets");
    }
    
    public boolean isDungeonLeaderboard() {
        return category.equals("Dungeons");
    }
    
    public boolean isSlayerLeaderboard() {
        return category.equals("Slayers");
    }
    
    public boolean isEconomyLeaderboard() {
        return category.equals("Economy");
    }
    
    public boolean isGuildLeaderboard() {
        return category.equals("Guilds");
    }
    
    public boolean isIslandLeaderboard() {
        return category.equals("Islands");
    }
    
    public boolean isAchievementLeaderboard() {
        return category.equals("Achievements");
    }
    
    public boolean isSocialLeaderboard() {
        return category.equals("Social");
    }
    
    public boolean isOverallLeaderboard() {
        return category.equals("Overall");
    }
}
