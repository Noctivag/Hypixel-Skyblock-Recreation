package de.noctivag.skyblock.collections;

import org.bukkit.Material;

/**
 * Represents a collection milestone with rewards
 */
public class CollectionMilestone {
    private final CollectionType collectionType;
    private final int level;
    private final long requirement;
    private final String displayName;
    private final String description;
    private final Material rewardItem;
    private final int rewardAmount;
    private final String rewardType;
    private final String rewardDescription;

    public CollectionMilestone(CollectionType collectionType, int level, long requirement, 
                              String displayName, String description, Material rewardItem, 
                              int rewardAmount, String rewardType, String rewardDescription) {
        this.collectionType = collectionType;
        this.level = level;
        this.requirement = requirement;
        this.displayName = displayName;
        this.description = description;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
        this.rewardType = rewardType;
        this.rewardDescription = rewardDescription;
    }

    // Getters
    public CollectionType getCollectionType() { return collectionType; }
    public int getLevel() { return level; }
    public long getRequirement() { return requirement; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public Material getRewardItem() { return rewardItem; }
    public int getRewardAmount() { return rewardAmount; }
    public String getRewardType() { return rewardType; }
    public String getRewardDescription() { return rewardDescription; }

    /**
     * Get milestone display name with color
     */
    public String getColoredDisplayName() {
        return collectionType.getColor() + collectionType.getIcon() + " " + displayName;
    }

    /**
     * Get milestone description with formatting
     */
    public String getFormattedDescription() {
        return "&7" + description;
    }

    /**
     * Get reward description with formatting
     */
    public String getFormattedRewardDescription() {
        return "&a" + rewardDescription;
    }

    /**
     * Check if this milestone is unlocked for a given amount
     */
    public boolean isUnlocked(long currentAmount) {
        return currentAmount >= requirement;
    }

    /**
     * Get progress percentage for this milestone
     */
    public double getProgressPercentage(long currentAmount) {
        if (requirement == 0) return 100.0;
        return Math.min(100.0, (double) currentAmount / requirement * 100.0);
    }

    /**
     * Get progress bar string
     */
    public String getProgressBar(long currentAmount) {
        double percentage = getProgressPercentage(currentAmount);
        int filledBars = (int) (percentage / 10);
        int emptyBars = 10 - filledBars;
        
        StringBuilder bar = new StringBuilder("&a");
        for (int i = 0; i < filledBars; i++) {
            bar.append("█");
        }
        bar.append("&7");
        for (int i = 0; i < emptyBars; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }

    /**
     * Get milestone status
     */
    public String getStatus(long currentAmount) {
        if (isUnlocked(currentAmount)) {
            return "&a✓ UNLOCKED";
        } else {
            return "&7" + currentAmount + "/" + requirement;
        }
    }

    /**
     * Create default milestones for a collection type
     */
    public static CollectionMilestone[] createDefaultMilestones(CollectionType collectionType) {
        long[] requirements = collectionType.getMilestoneRequirements();
        CollectionMilestone[] milestones = new CollectionMilestone[requirements.length];
        
        for (int i = 0; i < requirements.length; i++) {
            long requirement = requirements[i];
            String displayName = "Level " + (i + 1);
            String description = "Collect " + formatNumber(requirement) + " " + collectionType.getDisplayName();
            
            // Default rewards based on collection type
            Material rewardItem = getDefaultRewardItem(collectionType, i);
            int rewardAmount = getDefaultRewardAmount(collectionType, i);
            String rewardType = getDefaultRewardType(collectionType, i);
            String rewardDescription = getDefaultRewardDescription(collectionType, i);
            
            milestones[i] = new CollectionMilestone(
                collectionType, i + 1, requirement, displayName, description,
                rewardItem, rewardAmount, rewardType, rewardDescription
            );
        }
        
        return milestones;
    }

    private static Material getDefaultRewardItem(CollectionType collectionType, int level) {
        switch (collectionType.getCategory()) {
            case "Mining":
                return Material.DIAMOND_PICKAXE;
            case "Farming":
                return Material.DIAMOND_HOE;
            case "Foraging":
                return Material.DIAMOND_AXE;
            case "Fishing":
                return Material.FISHING_ROD;
            case "Combat":
                return Material.DIAMOND_SWORD;
            default:
                return Material.DIAMOND;
        }
    }

    private static int getDefaultRewardAmount(CollectionType collectionType, int level) {
        return 1 + (level / 5); // Increase reward amount every 5 levels
    }

    private static String getDefaultRewardType(CollectionType collectionType, int level) {
        switch (collectionType.getCategory()) {
            case "Mining":
                return "Mining Tool";
            case "Farming":
                return "Farming Tool";
            case "Foraging":
                return "Foraging Tool";
            case "Fishing":
                return "Fishing Tool";
            case "Combat":
                return "Combat Weapon";
            default:
                return "Special Item";
        }
    }

    private static String getDefaultRewardDescription(CollectionType collectionType, int level) {
        String toolType = getDefaultRewardType(collectionType, level);
        return "Unlock " + toolType + " Recipe";
    }

    private static String formatNumber(long number) {
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
