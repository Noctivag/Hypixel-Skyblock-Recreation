package de.noctivag.skyblock.features.collections;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.collections.types.CollectionType;

/**
 * Represents progress in a specific collection
 */
public class CollectionProgress {
    
    private final CollectionType type;
    private long totalItems;
    private int level;
    private long lastUpdateTime;
    
    // Milestone thresholds for each level
    private static final long[] MILESTONE_THRESHOLDS = {
        50, 100, 250, 500, 1000, 2500, 5000, 10000, 25000, 50000,
        100000, 250000, 500000, 1000000, 2500000, 5000000, 10000000,
        25000000, 50000000, 100000000, 250000000, 500000000, 1000000000
    };
    
    public CollectionProgress(CollectionType type) {
        this.type = type;
        this.totalItems = 0;
        this.level = 0;
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
    }
    
    /**
     * Add items to the collection
     */
    public void addItems(long amount) {
        this.totalItems += amount;
        this.lastUpdateTime = java.lang.System.currentTimeMillis();
        
        // Check if level increased
        int newLevel = calculateLevel();
        if (newLevel > this.level) {
            this.level = newLevel;
        }
    }
    
    /**
     * Calculate current level based on total items
     */
    private int calculateLevel() {
        for (int i = 0; i < MILESTONE_THRESHOLDS.length; i++) {
            if (totalItems < MILESTONE_THRESHOLDS[i]) {
                return i;
            }
        }
        return MILESTONE_THRESHOLDS.length;
    }
    
    /**
     * Get progress to next level
     */
    public LevelProgress getLevelProgress() {
        int currentLevel = this.level;
        long currentThreshold = currentLevel > 0 ? MILESTONE_THRESHOLDS[currentLevel - 1] : 0;
        long nextThreshold = currentLevel < MILESTONE_THRESHOLDS.length ? MILESTONE_THRESHOLDS[currentLevel] : Long.MAX_VALUE;
        
        long progress = totalItems - currentThreshold;
        long needed = nextThreshold - currentThreshold;
        
        return new LevelProgress(currentLevel, progress, needed, nextThreshold);
    }
    
    /**
     * Get items needed for next level
     */
    public long getItemsNeededForNextLevel() {
        LevelProgress progress = getLevelProgress();
        return progress.getNeeded();
    }
    
    /**
     * Get progress percentage to next level
     */
    public double getProgressPercentage() {
        LevelProgress progress = getLevelProgress();
        if (progress.getNeeded() <= 0) return 100.0;
        return (double) progress.getProgress() / progress.getNeeded() * 100.0;
    }
    
    /**
     * Check if collection is maxed out
     */
    public boolean isMaxedOut() {
        return level >= MILESTONE_THRESHOLDS.length;
    }
    
    /**
     * Get collection efficiency (items per hour)
     */
    public double getEfficiency() {
        if (lastUpdateTime == 0) return 0.0;
        
        long timeElapsed = java.lang.System.currentTimeMillis() - lastUpdateTime;
        double hoursElapsed = timeElapsed / (1000.0 * 60 * 60);
        
        return hoursElapsed > 0 ? totalItems / hoursElapsed : 0.0;
    }
    
    // Getters
    public CollectionType getType() {
        return type;
    }
    
    public long getTotalItems() {
        return totalItems;
    }
    
    public int getLevel() {
        return level;
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Level progress information
     */
    public static class LevelProgress {
        private final int currentLevel;
        private final long progress;
        private final long needed;
        private final long nextThreshold;
        
        public LevelProgress(int currentLevel, long progress, long needed, long nextThreshold) {
            this.currentLevel = currentLevel;
            this.progress = progress;
            this.needed = needed;
            this.nextThreshold = nextThreshold;
        }
        
        public int getCurrentLevel() {
            return currentLevel;
        }
        
        public long getProgress() {
            return progress;
        }
        
        public long getNeeded() {
            return needed;
        }
        
        public long getNextThreshold() {
            return nextThreshold;
        }
        
        public double getPercentage() {
            if (needed <= 0) return 100.0;
            return (double) progress / needed * 100.0;
        }
        
        @Override
        public String toString() {
            return String.format("Level %d: %d/%d (%.1f%%)", 
                currentLevel, progress, needed, getPercentage());
        }
    }
}
