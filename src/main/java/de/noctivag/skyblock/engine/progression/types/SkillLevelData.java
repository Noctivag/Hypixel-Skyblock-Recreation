package de.noctivag.skyblock.engine.progression.types;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Skill Level Data with Precise XP Lookup Tables
 * 
 * Implements the discontinuous XP curves as specified in the requirements.
 * Uses lookup tables instead of algebraic formulas to ensure accurate
 * endgame progression rates.
 */
public class SkillLevelData {
    
    private final HypixelSkillType skillType;
    private final Map<Integer, Double> levelXPRequirements;
    private final Map<Integer, Double> cumulativeXPRequirements;
    
    // Precise XP requirements based on Hypixel Skyblock data
    private static final double[] PRECISE_XP_REQUIREMENTS = {
        0,      // Level 0 (starting point)
        50,     // Level 1
        125,    // Level 2
        200,    // Level 3
        300,    // Level 4
        500,    // Level 5
        750,    // Level 6
        1000,   // Level 7
        1500,   // Level 8
        2000,   // Level 9
        3500,   // Level 10
        5000,   // Level 11
        7500,   // Level 12
        10000,  // Level 13
        15000,  // Level 14
        20000,  // Level 15
        30000,  // Level 16
        50000,  // Level 17
        75000,  // Level 18
        100000, // Level 19
        200000, // Level 20
        300000, // Level 21
        400000, // Level 22
        500000, // Level 23
        600000, // Level 24
        700000, // Level 25
        800000, // Level 26
        900000, // Level 27
        1000000, // Level 28
        1100000, // Level 29
        1200000, // Level 30
        1300000, // Level 31
        1400000, // Level 32
        1500000, // Level 33
        1600000, // Level 34
        1700000, // Level 35
        1800000, // Level 36
        1900000, // Level 37
        2000000, // Level 38
        2100000, // Level 39
        2200000, // Level 40
        2300000, // Level 41
        2400000, // Level 42
        2500000, // Level 43
        2600000, // Level 44
        2700000, // Level 45
        2800000, // Level 46
        2900000, // Level 47
        3000000, // Level 48
        3100000, // Level 49
        3200000, // Level 50
        3300000, // Level 51
        3400000, // Level 52
        3500000, // Level 53
        3600000, // Level 54
        3700000, // Level 55
        3800000, // Level 56
        3900000, // Level 57
        4000000, // Level 58
        4100000, // Level 59
        7000000  // Level 60 (final time gate)
    };
    
    public SkillLevelData(HypixelSkillType skillType) {
        this.skillType = skillType;
        this.levelXPRequirements = new HashMap<>();
        this.cumulativeXPRequirements = new HashMap<>();
        
        initializeXPRequirements();
    }
    
    /**
     * Initialize XP requirements based on skill type
     */
    private void initializeXPRequirements() {
        int maxLevel = skillType.getMaxLevel();
        
        // Initialize level XP requirements
        for (int level = 0; level <= maxLevel; level++) {
            double xpRequired = calculateXPForLevel(level);
            levelXPRequirements.put(level, xpRequired);
        }
        
        // Calculate cumulative XP requirements
        double cumulativeXP = 0;
        for (int level = 0; level <= maxLevel; level++) {
            cumulativeXP += levelXPRequirements.get(level);
            cumulativeXPRequirements.put(level, cumulativeXP);
        }
    }
    
    /**
     * Calculate XP required for a specific level
     */
    private double calculateXPForLevel(int level) {
        if (level <= 0) return 0;
        if (level > skillType.getMaxLevel()) return Double.MAX_VALUE;
        
        // Use precise lookup table for standard skills
        if (level <= 60 && level < PRECISE_XP_REQUIREMENTS.length) {
            return PRECISE_XP_REQUIREMENTS[level];
        }
        
        // For skills with different max levels, scale accordingly
        double scaleFactor = getScaleFactor();
        if (level <= 50) {
            // Scale down for 50-level skills
            return PRECISE_XP_REQUIREMENTS[level] * scaleFactor;
        } else if (level <= 60) {
            // Use full values for 60-level skills
            return PRECISE_XP_REQUIREMENTS[level];
        } else {
            // For runecrafting (25 levels), use much smaller values
            return PRECISE_XP_REQUIREMENTS[Math.min(level, 25)] * 0.1;
        }
    }
    
    /**
     * Get scale factor based on skill type
     */
    private double getScaleFactor() {
        return switch (skillType) {
            case COMBAT, CATACOMBS, SLAYER, FORAGING, FISHING, ALCHEMY, TAMING, CARPENTRY -> 0.8; // 50-level skills
            case MINING, FARMING, ENCHANTING -> 1.0; // 60-level skills
            case RUNECRAFTING -> 0.1; // 25-level skill
        };
    }
    
    /**
     * Get XP required for a specific level
     */
    public double getXPRequiredForLevel(int level) {
        return levelXPRequirements.getOrDefault(level, 0.0);
    }
    
    /**
     * Get cumulative XP required to reach a specific level
     */
    public double getCumulativeXPForLevel(int level) {
        return cumulativeXPRequirements.getOrDefault(level, 0.0);
    }
    
    /**
     * Get total XP required to reach max level
     */
    public double getTotalXPRequired() {
        return getCumulativeXPForLevel(skillType.getMaxLevel());
    }
    
    /**
     * Calculate level from total XP
     */
    public int calculateLevelFromXP(double totalXP) {
        int level = 0;
        double cumulativeXP = 0;
        
        for (int i = 1; i <= skillType.getMaxLevel(); i++) {
            double xpForLevel = getXPRequiredForLevel(i);
            if (cumulativeXP + xpForLevel <= totalXP) {
                cumulativeXP += xpForLevel;
                level = i;
            } else {
                break;
            }
        }
        
        return level;
    }
    
    /**
     * Get XP progress within current level
     */
    public double getXPProgressInLevel(double totalXP) {
        int currentLevel = calculateLevelFromXP(totalXP);
        if (currentLevel >= skillType.getMaxLevel()) {
            return 1.0; // Max level reached
        }
        
        double xpForCurrentLevel = getCumulativeXPForLevel(currentLevel);
        double xpForNextLevel = getCumulativeXPForLevel(currentLevel + 1);
        double xpInCurrentLevel = totalXP - xpForCurrentLevel;
        double xpNeededForLevel = xpForNextLevel - xpForCurrentLevel;
        
        return xpInCurrentLevel / xpNeededForLevel;
    }
    
    /**
     * Get XP remaining to next level
     */
    public double getXPToNextLevel(double totalXP) {
        int currentLevel = calculateLevelFromXP(totalXP);
        if (currentLevel >= skillType.getMaxLevel()) {
            return 0; // Max level reached
        }
        
        double xpForNextLevel = getCumulativeXPForLevel(currentLevel + 1);
        return xpForNextLevel - totalXP;
    }
    
    /**
     * Get skill type
     */
    public HypixelSkillType getSkillType() {
        return skillType;
    }
    
    /**
     * Get all level XP requirements
     */
    public Map<Integer, Double> getLevelXPRequirements() {
        return new HashMap<>(levelXPRequirements);
    }
    
    /**
     * Get all cumulative XP requirements
     */
    public Map<Integer, Double> getCumulativeXPRequirements() {
        return new HashMap<>(cumulativeXPRequirements);
    }
    
    /**
     * Get critical milestone levels for this skill
     */
    public int[] getMilestoneLevels() {
        return switch (skillType) {
            case MINING, FARMING, ENCHANTING -> new int[]{15, 25, 35, 45, 55, 60}; // 60-level skills
            case RUNECRAFTING -> new int[]{5, 10, 15, 20, 25}; // 25-level skill
            default -> new int[]{10, 20, 30, 40, 50}; // 50-level skills
        };
    }
    
    /**
     * Check if a level is a milestone
     */
    public boolean isMilestoneLevel(int level) {
        return Arrays.stream(getMilestoneLevels()).anyMatch(l -> l == level);
    }
}
