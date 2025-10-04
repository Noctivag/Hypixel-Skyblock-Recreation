package de.noctivag.skyblock.engine.collections.types;

import java.util.Arrays;

/**
 * Collection Milestone
 * 
 * Represents a milestone in a collection that unlocks rewards.
 * Each milestone has a required amount and associated rewards.
 */
public class CollectionMilestone {
    
    private final int requiredAmount;
    private final CollectionReward[] rewards;
    private final String description;
    
    public CollectionMilestone(int requiredAmount, CollectionReward[] rewards) {
        this.requiredAmount = requiredAmount;
        this.rewards = rewards != null ? rewards : new CollectionReward[0];
        this.description = generateDescription();
    }
    
    public CollectionMilestone(int requiredAmount, CollectionReward[] rewards, String description) {
        this.requiredAmount = requiredAmount;
        this.rewards = rewards != null ? rewards : new CollectionReward[0];
        this.description = description != null ? description : generateDescription();
    }
    
    public int getRequiredAmount() {
        return requiredAmount;
    }
    
    public CollectionReward[] getRewards() {
        return Arrays.copyOf(rewards, rewards.length);
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if a collection amount meets this milestone
     */
    public boolean isReached(int collectionAmount) {
        return collectionAmount >= requiredAmount;
    }
    
    /**
     * Get progress towards this milestone
     */
    public double getProgress(int collectionAmount) {
        if (requiredAmount <= 0) return 1.0;
        return Math.min((double) collectionAmount / requiredAmount, 1.0);
    }
    
    /**
     * Get remaining amount needed for this milestone
     */
    public int getRemainingAmount(int collectionAmount) {
        return Math.max(0, requiredAmount - collectionAmount);
    }
    
    /**
     * Check if this milestone is the next one to reach
     */
    public boolean isNextMilestone(int collectionAmount) {
        return collectionAmount < requiredAmount;
    }
    
    /**
     * Get formatted progress string
     */
    public String getFormattedProgress(int collectionAmount) {
        if (isReached(collectionAmount)) {
            return String.format("✅ %s (Completed)", description);
        } else {
            int remaining = getRemainingAmount(collectionAmount);
            double progress = getProgress(collectionAmount);
            return String.format("⏳ %s (%,d remaining, %.1f%%)", 
                description, remaining, progress * 100);
        }
    }
    
    /**
     * Get all reward types in this milestone
     */
    public CollectionRewardType[] getRewardTypes() {
        return Arrays.stream(rewards)
            .map(CollectionReward::getType)
            .distinct()
            .toArray(CollectionRewardType[]::new);
    }
    
    /**
     * Get rewards of a specific type
     */
    public CollectionReward[] getRewardsOfType(CollectionRewardType type) {
        return Arrays.stream(rewards)
            .filter(reward -> reward.getType() == type)
            .toArray(CollectionReward[]::new);
    }
    
    /**
     * Check if this milestone has a specific reward type
     */
    public boolean hasRewardType(CollectionRewardType type) {
        return Arrays.stream(rewards)
            .anyMatch(reward -> reward.getType() == type);
    }
    
    /**
     * Get total reward value
     */
    public int getTotalRewardValue() {
        return Arrays.stream(rewards)
            .mapToInt(CollectionReward::getAmount)
            .sum();
    }
    
    /**
     * Generate description based on required amount
     */
    private String generateDescription() {
        if (requiredAmount >= 1000000) {
            return String.format("Collect %,d items (1M+)", requiredAmount);
        } else if (requiredAmount >= 100000) {
            return String.format("Collect %,d items (100K+)", requiredAmount);
        } else if (requiredAmount >= 10000) {
            return String.format("Collect %,d items (10K+)", requiredAmount);
        } else if (requiredAmount >= 1000) {
            return String.format("Collect %,d items (1K+)", requiredAmount);
        } else if (requiredAmount >= 100) {
            return String.format("Collect %,d items (100+)", requiredAmount);
        } else {
            return String.format("Collect %,d items", requiredAmount);
        }
    }
    
    /**
     * Get milestone tier based on required amount
     */
    public MilestoneTier getTier() {
        if (requiredAmount >= 1000000) {
            return MilestoneTier.LEGENDARY;
        } else if (requiredAmount >= 100000) {
            return MilestoneTier.EPIC;
        } else if (requiredAmount >= 10000) {
            return MilestoneTier.RARE;
        } else if (requiredAmount >= 1000) {
            return MilestoneTier.UNCOMMON;
        } else {
            return MilestoneTier.COMMON;
        }
    }
    
    /**
     * Get milestone color based on tier
     */
    public String getTierColor() {
        return switch (getTier()) {
            case COMMON -> "§7"; // Gray
            case UNCOMMON -> "§a"; // Green
            case RARE -> "§9"; // Blue
            case EPIC -> "§5"; // Purple
            case LEGENDARY -> "§6"; // Gold
        };
    }
    
    /**
     * Get formatted milestone with tier color
     */
    public String getFormattedMilestone() {
        return getTierColor() + description;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CollectionMilestone that = (CollectionMilestone) obj;
        return requiredAmount == that.requiredAmount;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(requiredAmount);
    }
    
    @Override
    public String toString() {
        return String.format("CollectionMilestone{requiredAmount=%,d, rewards=%d, description='%s'}", 
            requiredAmount, rewards.length, description);
    }
    
    /**
     * Milestone tiers for categorization
     */
    public enum MilestoneTier {
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare"),
        EPIC("Epic"),
        LEGENDARY("Legendary");
        
        private final String displayName;
        
        MilestoneTier(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
