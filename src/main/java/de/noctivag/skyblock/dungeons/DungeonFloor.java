package de.noctivag.skyblock.dungeons;

import org.bukkit.Material;

/**
 * Enum representing all dungeon floors in Hypixel Skyblock
 */
public enum DungeonFloor {
    F1("Floor 1", "&7", "1", "The Entrance", Material.STONE_BRICKS, 1, 100, 5, 10, 1000),
    F2("Floor 2", "&a", "2", "The Catacombs", Material.MOSSY_STONE_BRICKS, 2, 150, 7, 15, 2000),
    F3("Floor 3", "&9", "3", "The Catacombs", Material.CRACKED_STONE_BRICKS, 3, 200, 10, 20, 3000),
    F4("Floor 4", "&5", "4", "The Catacombs", Material.CHISELED_STONE_BRICKS, 4, 250, 12, 25, 4000),
    F5("Floor 5", "&6", "5", "The Catacombs", Material.POLISHED_ANDESITE, 5, 300, 15, 30, 5000),
    F6("Floor 6", "&c", "6", "The Catacombs", Material.POLISHED_DIORITE, 6, 350, 17, 35, 6000),
    F7("Floor 7", "&4", "7", "The Catacombs", Material.POLISHED_GRANITE, 7, 400, 20, 40, 7000),
    M1("Master Mode 1", "&d", "M1", "Master Mode", Material.NETHER_BRICKS, 8, 500, 25, 50, 10000),
    M2("Master Mode 2", "&d", "M2", "Master Mode", Material.RED_NETHER_BRICKS, 9, 600, 30, 60, 15000),
    M3("Master Mode 3", "&d", "M3", "Master Mode", Material.BLACKSTONE, 10, 700, 35, 70, 20000),
    M4("Master Mode 4", "&d", "M4", "Master Mode", Material.POLISHED_BLACKSTONE, 11, 800, 40, 80, 25000),
    M5("Master Mode 5", "&d", "M5", "Master Mode", Material.POLISHED_BLACKSTONE_BRICKS, 12, 900, 45, 90, 30000),
    M6("Master Mode 6", "&d", "M6", "Master Mode", Material.CRIMSON_PLANKS, 13, 1000, 50, 100, 35000),
    M7("Master Mode 7", "&d", "M7", "Master Mode", Material.WARPED_PLANKS, 14, 1100, 55, 110, 40000);

    private final String displayName;
    private final String color;
    private final String icon;
    private final String description;
    private final Material material;
    private final int floorNumber;
    private final int recommendedLevel;
    private final int minPlayers;
    private final int maxPlayers;
    private final long baseReward;

    DungeonFloor(String displayName, String color, String icon, String description, Material material, 
                 int floorNumber, int recommendedLevel, int minPlayers, int maxPlayers, long baseReward) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.description = description;
        this.material = material;
        this.floorNumber = floorNumber;
        this.recommendedLevel = recommendedLevel;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.baseReward = baseReward;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public int getFloorNumber() { return floorNumber; }
    public int getRecommendedLevel() { return recommendedLevel; }
    public int getMinPlayers() { return minPlayers; }
    public int getMaxPlayers() { return maxPlayers; }
    public long getBaseReward() { return baseReward; }

    /**
     * Get colored display name
     */
    public String getColoredDisplayName() {
        return color + icon + " " + displayName;
    }

    /**
     * Get floor lore
     */
    public String[] getFloorLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Recommended Level: &a" + recommendedLevel,
            "&7Players: &a" + minPlayers + "-" + maxPlayers,
            "&7Base Reward: &e" + formatNumber(baseReward) + " coins",
            "",
            "&7Difficulty: " + getDifficultyColor() + getDifficultyName(),
            "&7Boss: " + getBossName(),
            "",
            "&eClick to enter this floor"
        };
    }

    /**
     * Get difficulty name
     */
    public String getDifficultyName() {
        if (floorNumber <= 3) return "Easy";
        if (floorNumber <= 6) return "Medium";
        if (floorNumber <= 7) return "Hard";
        return "Master";
    }

    /**
     * Get difficulty color
     */
    public String getDifficultyColor() {
        switch (getDifficultyName()) {
            case "Easy": return "&a";
            case "Medium": return "&e";
            case "Hard": return "&c";
            case "Master": return "&d";
            default: return "&7";
        }
    }

    /**
     * Get boss name for this floor
     */
    public String getBossName() {
        switch (this) {
            case F1: return "&7Bonzo";
            case F2: return "&aScarf";
            case F3: return "&9The Professor";
            case F4: return "&5Thorn";
            case F5: return "&6Livid";
            case F6: return "&cSadan";
            case F7: return "&4Necron";
            case M1: return "&dMaster Mode Bonzo";
            case M2: return "&dMaster Mode Scarf";
            case M3: return "&dMaster Mode Professor";
            case M4: return "&dMaster Mode Thorn";
            case M5: return "&dMaster Mode Livid";
            case M6: return "&dMaster Mode Sadan";
            case M7: return "&dMaster Mode Necron";
            default: return "&7Unknown Boss";
        }
    }

    /**
     * Check if this is a master mode floor
     */
    public boolean isMasterMode() {
        return floorNumber >= 8;
    }

    /**
     * Get the normal mode equivalent
     */
    public DungeonFloor getNormalModeEquivalent() {
        if (!isMasterMode()) return this;
        
        int normalFloor = floorNumber - 7;
        for (DungeonFloor floor : values()) {
            if (floor.getFloorNumber() == normalFloor) {
                return floor;
            }
        }
        return F1;
    }

    /**
     * Get the master mode equivalent
     */
    public DungeonFloor getMasterModeEquivalent() {
        if (isMasterMode()) return this;
        
        int masterFloor = floorNumber + 7;
        for (DungeonFloor floor : values()) {
            if (floor.getFloorNumber() == masterFloor) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Get next floor
     */
    public DungeonFloor getNextFloor() {
        int nextFloor = floorNumber + 1;
        for (DungeonFloor floor : values()) {
            if (floor.getFloorNumber() == nextFloor) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Get previous floor
     */
    public DungeonFloor getPreviousFloor() {
        int prevFloor = floorNumber - 1;
        for (DungeonFloor floor : values()) {
            if (floor.getFloorNumber() == prevFloor) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Get floor by number
     */
    public static DungeonFloor getByNumber(int floorNumber) {
        for (DungeonFloor floor : values()) {
            if (floor.getFloorNumber() == floorNumber) {
                return floor;
            }
        }
        return null;
    }

    /**
     * Get all normal floors
     */
    public static DungeonFloor[] getNormalFloors() {
        return java.util.Arrays.stream(values())
                .filter(floor -> !floor.isMasterMode())
                .toArray(DungeonFloor[]::new);
    }

    /**
     * Get all master mode floors
     */
    public static DungeonFloor[] getMasterModeFloors() {
        return java.util.Arrays.stream(values())
                .filter(DungeonFloor::isMasterMode)
                .toArray(DungeonFloor[]::new);
    }

    /**
     * Get floors by difficulty
     */
    public static DungeonFloor[] getFloorsByDifficulty(String difficulty) {
        return java.util.Arrays.stream(values())
                .filter(floor -> floor.getDifficultyName().equalsIgnoreCase(difficulty))
                .toArray(DungeonFloor[]::new);
    }

    /**
     * Format large numbers
     */
    private String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }
}
