package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SkyblockSkills {
    private final Map<SkyblockManager.SkyblockSkill, Double> skillXP = new HashMap<>();
    private final Map<SkyblockManager.SkyblockSkill, Integer> skillLevels = new HashMap<>();
    
    public SkyblockSkills() {
        // Initialize all skills at level 0
        for (SkyblockManager.SkyblockSkill skill : SkyblockManager.SkyblockSkill.values()) {
            skillXP.put(skill, 0.0);
            skillLevels.put(skill, 0);
        }
    }
    
    public void addXP(SkyblockManager.SkyblockSkill skill, double xp) {
        double currentXP = skillXP.get(skill);
        double newXP = currentXP + xp;
        skillXP.put(skill, newXP);
        
        // Check for level up
        int newLevel = calculateLevel(newXP);
        int currentLevel = skillLevels.get(skill);
        
        if (newLevel > currentLevel) {
            skillLevels.put(skill, newLevel);
        }
    }
    
    public int getLevel(SkyblockManager.SkyblockSkill skill) {
        return skillLevels.get(skill);
    }
    
    public double getXP(SkyblockManager.SkyblockSkill skill) {
        return skillXP.get(skill);
    }
    
    public double getXPToNextLevel(SkyblockManager.SkyblockSkill skill) {
        int currentLevel = getLevel(skill);
        double currentXP = getXP(skill);
        double xpForNextLevel = getXPForLevel(currentLevel + 1);
        return xpForNextLevel - currentXP;
    }
    
    public double getXPProgress(SkyblockManager.SkyblockSkill skill) {
        int currentLevel = getLevel(skill);
        double currentXP = getXP(skill);
        double xpForCurrentLevel = getXPForLevel(currentLevel);
        double xpForNextLevel = getXPForLevel(currentLevel + 1);
        
        return (currentXP - xpForCurrentLevel) / (xpForNextLevel - xpForCurrentLevel);
    }
    
    private int calculateLevel(double xp) {
        // Hypixel-like level calculation
        if (xp < 50) return 0;
        if (xp < 125) return 1;
        if (xp < 200) return 2;
        if (xp < 300) return 3;
        if (xp < 500) return 4;
        if (xp < 750) return 5;
        if (xp < 1000) return 6;
        if (xp < 1500) return 7;
        if (xp < 2000) return 8;
        if (xp < 3500) return 9;
        if (xp < 5000) return 10;
        if (xp < 7500) return 11;
        if (xp < 10000) return 12;
        if (xp < 15000) return 13;
        if (xp < 20000) return 14;
        if (xp < 30000) return 15;
        if (xp < 50000) return 16;
        if (xp < 75000) return 17;
        if (xp < 100000) return 18;
        if (xp < 200000) return 19;
        if (xp < 300000) return 20;
        if (xp < 400000) return 21;
        if (xp < 500000) return 22;
        if (xp < 600000) return 23;
        if (xp < 700000) return 24;
        if (xp < 800000) return 25;
        if (xp < 900000) return 26;
        if (xp < 1000000) return 27;
        if (xp < 1100000) return 28;
        if (xp < 1200000) return 29;
        if (xp < 1300000) return 30;
        if (xp < 1400000) return 31;
        if (xp < 1500000) return 32;
        if (xp < 1600000) return 33;
        if (xp < 1700000) return 34;
        if (xp < 1800000) return 35;
        if (xp < 1900000) return 36;
        if (xp < 2000000) return 37;
        if (xp < 2100000) return 38;
        if (xp < 2200000) return 39;
        if (xp < 2300000) return 40;
        if (xp < 2400000) return 41;
        if (xp < 2500000) return 42;
        if (xp < 2600000) return 43;
        if (xp < 2700000) return 44;
        if (xp < 2800000) return 45;
        if (xp < 2900000) return 46;
        if (xp < 3000000) return 47;
        if (xp < 3100000) return 48;
        if (xp < 3200000) return 49;
        return 50; // Max level
    }
    
    private double getXPForLevel(int level) {
        // Return XP required for a specific level
        if (level <= 0) return 0;
        if (level == 1) return 50;
        if (level == 2) return 125;
        if (level == 3) return 200;
        if (level == 4) return 300;
        if (level == 5) return 500;
        if (level == 6) return 750;
        if (level == 7) return 1000;
        if (level == 8) return 1500;
        if (level == 9) return 2000;
        if (level == 10) return 3500;
        if (level == 11) return 5000;
        if (level == 12) return 7500;
        if (level == 13) return 10000;
        if (level == 14) return 15000;
        if (level == 15) return 20000;
        if (level == 16) return 30000;
        if (level == 17) return 50000;
        if (level == 18) return 75000;
        if (level == 19) return 100000;
        if (level == 20) return 200000;
        if (level == 21) return 300000;
        if (level == 22) return 400000;
        if (level == 23) return 500000;
        if (level == 24) return 600000;
        if (level == 25) return 700000;
        if (level == 26) return 800000;
        if (level == 27) return 900000;
        if (level == 28) return 1000000;
        if (level == 29) return 1100000;
        if (level == 30) return 1200000;
        if (level == 31) return 1300000;
        if (level == 32) return 1400000;
        if (level == 33) return 1500000;
        if (level == 34) return 1600000;
        if (level == 35) return 1700000;
        if (level == 36) return 1800000;
        if (level == 37) return 1900000;
        if (level == 38) return 2000000;
        if (level == 39) return 2100000;
        if (level == 40) return 2200000;
        if (level == 41) return 2300000;
        if (level == 42) return 2400000;
        if (level == 43) return 2500000;
        if (level == 44) return 2600000;
        if (level == 45) return 2700000;
        if (level == 46) return 2800000;
        if (level == 47) return 2900000;
        if (level == 48) return 3000000;
        if (level == 49) return 3100000;
        if (level == 50) return 3200000;
        return 3200000; // Max XP
    }
    
    public int getTotalLevel() {
        return skillLevels.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getTotalXP() {
        return skillXP.values().stream().mapToDouble(Double::doubleValue).sum();
    }
    
    public void save() {
        // Save skills data
        // Implementation would go here
    }
}
