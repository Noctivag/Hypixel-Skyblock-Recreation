package de.noctivag.skyblock.features.dungeons.classes;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * Tracks a player's progress with dungeon classes
 */
public class ClassProgress {
    
    private final Set<DungeonClassType> unlockedClasses = new HashSet<>();
    private final Set<DungeonClassType> masteredClasses = new HashSet<>();
    
    private int totalDungeonsCompleted = 0;
    private int totalScore = 0;
    private int totalTimeSpent = 0; // in seconds
    
    public ClassProgress() {
        // Berserker is unlocked by default
        unlockedClasses.add(DungeonClassType.BERSERKER);
    }
    
    /**
     * Unlock a class for the player
     */
    public void unlockClass(DungeonClassType classType) {
        unlockedClasses.add(classType);
    }
    
    /**
     * Check if player has unlocked a class
     */
    public boolean hasClassUnlocked(DungeonClassType classType) {
        return unlockedClasses.contains(classType);
    }
    
    /**
     * Master a class (complete X dungeons with that class)
     */
    public void masterClass(DungeonClassType classType) {
        masteredClasses.add(classType);
    }
    
    /**
     * Check if player has mastered a class
     */
    public boolean hasMasteredClass(DungeonClassType classType) {
        return masteredClasses.contains(classType);
    }
    
    /**
     * Add dungeon completion stats
     */
    public void addDungeonCompletion(int score, int timeSpent) {
        this.totalDungeonsCompleted++;
        this.totalScore += score;
        this.totalTimeSpent += timeSpent;
    }
    
    /**
     * Get average score
     */
    public double getAverageScore() {
        if (totalDungeonsCompleted == 0) return 0.0;
        return (double) totalScore / totalDungeonsCompleted;
    }
    
    /**
     * Get average time spent
     */
    public double getAverageTimeSpent() {
        if (totalDungeonsCompleted == 0) return 0.0;
        return (double) totalTimeSpent / totalDungeonsCompleted;
    }
    
    // Getters
    public Set<DungeonClassType> getUnlockedClasses() {
        return new HashSet<>(unlockedClasses);
    }
    
    public Set<DungeonClassType> getMasteredClasses() {
        return new HashSet<>(masteredClasses);
    }
    
    public int getTotalDungeonsCompleted() {
        return totalDungeonsCompleted;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }
}
