package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.PetType;
import de.noctivag.skyblock.enums.Rarity;

public class Pet {
    
    private final PetType petType;
    private Rarity rarity;
    private int level;
    private int experience;
    private boolean isActive;
    private long lastFed;
    
    public Pet(PetType petType, Rarity rarity) {
        this.petType = petType;
        this.rarity = rarity;
        this.level = 1;
        this.experience = 0;
        this.isActive = false;
        this.lastFed = System.currentTimeMillis();
    }
    
    public Pet(PetType petType, Rarity rarity, int level, int experience) {
        this.petType = petType;
        this.rarity = rarity;
        this.level = level;
        this.experience = experience;
        this.isActive = false;
        this.lastFed = System.currentTimeMillis();
    }
    
    public void addExperience(int exp) {
        this.experience += exp;
        
        // Check for level up
        int requiredExp = getRequiredExperienceForNextLevel();
        if (this.experience >= requiredExp && level < petType.getMaxLevel()) {
            levelUp();
        }
    }
    
    private void levelUp() {
        level++;
        experience = 0;
    }
    
    public int getRequiredExperienceForNextLevel() {
        if (level >= petType.getMaxLevel()) {
            return 0;
        }
        
        // Experience required increases with level
        return level * 100 + (level * level * 10);
    }
    
    public double getStatBoost(String statType) {
        double baseBoost = petType.getBaseStatBoost();
        double levelMultiplier = 1.0 + (level - 1) * 0.1; // 10% increase per level
        
        // Apply pet-specific bonuses
        double petMultiplier = getPetSpecificMultiplier(statType);
        
        return baseBoost * levelMultiplier * petMultiplier;
    }
    
    private double getPetSpecificMultiplier(String statType) {
        switch (petType) {
            case ENDER_DRAGON:
                if (statType.equals("damage") || statType.equals("strength")) {
                    return 2.0; // Double damage/strength
                }
                break;
            case ELEPHANT:
                if (statType.equals("farming_fortune") || statType.equals("health")) {
                    return 1.5; // 50% more farming fortune/health
                }
                break;
            case GRIFFIN:
                if (statType.equals("strength") || statType.equals("crit_damage")) {
                    return 1.3; // 30% more strength/crit damage
                }
                break;
            case TIGER:
                if (statType.equals("crit_chance") || statType.equals("crit_damage")) {
                    return 1.4; // 40% more crit stats
                }
                break;
            case LION:
                if (statType.equals("strength") || statType.equals("speed")) {
                    return 1.3; // 30% more strength/speed
                }
                break;
            case WOLF:
                if (statType.equals("crit_damage") || statType.equals("health")) {
                    return 1.3; // 30% more crit damage/health
                }
                break;
            case HORSE:
                if (statType.equals("speed") || statType.equals("jump_boost")) {
                    return 1.4; // 40% more speed/jump
                }
                break;
            case CAT:
                if (statType.equals("luck") || statType.equals("experience")) {
                    return 1.3; // 30% more luck/experience
                }
                break;
            case DOG:
                if (statType.equals("health") || statType.equals("defense")) {
                    return 1.3; // 30% more health/defense
                }
                break;
            case CHICKEN:
                if (statType.equals("farming_fortune")) {
                    return 1.5; // 50% more farming fortune
                }
                break;
            case COW:
                if (statType.equals("health")) {
                    return 1.4; // 40% more health
                }
                break;
            case PIG:
                if (statType.equals("speed")) {
                    return 1.4; // 40% more speed
                }
                break;
            case RABBIT:
                if (statType.equals("jump_boost")) {
                    return 1.5; // 50% more jump boost
                }
                break;
            case SHEEP:
                if (statType.equals("luck")) {
                    return 1.3; // 30% more luck
                }
                break;
            case BAT:
                if (statType.equals("night_vision")) {
                    return 1.0; // Night vision effect
                }
                break;
        }
        
        return 1.0; // Default multiplier
    }
    
    public boolean canBeFed() {
        // Can be fed once per hour
        return System.currentTimeMillis() - lastFed > 3600000; // 1 hour in milliseconds
    }
    
    public void feed() {
        if (canBeFed()) {
            lastFed = System.currentTimeMillis();
            addExperience(50); // Feeding gives 50 experience
        }
    }
    
    // Getters
    public PetType getPetType() {
        return petType;
    }
    
    public Rarity getRarity() {
        return rarity;
    }
    
    public int getLevel() {
        return level;
    }
    
    public int getExperience() {
        return experience;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public long getLastFed() {
        return lastFed;
    }
    
    // Setters
    public void setLevel(int level) {
        this.level = Math.min(level, petType.getMaxLevel());
    }
    
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public void setLastFed(long lastFed) {
        this.lastFed = lastFed;
    }
    
    /**
     * Upgraded die Rarity des Pets zur nächsten Stufe
     */
    public void upgradeRarity() {
        Rarity nextRarity = rarity.getNext();
        if (nextRarity != null) {
            this.rarity = nextRarity;
        }
    }
}
