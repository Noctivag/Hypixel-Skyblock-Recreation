package de.noctivag.skyblock.pets;

import java.util.UUID;

/**
 * Pet - Represents a pet
 */
public class Pet {
    
    private final UUID petId;
    private final UUID ownerId;
    private final PetType petType;
    private int level;
    private double experience;
    private boolean active;
    
    public Pet(UUID petId, UUID ownerId, PetType petType) {
        this.petId = petId;
        this.ownerId = ownerId;
        this.petType = petType;
        this.level = 1;
        this.experience = 0.0;
        this.active = false;
    }
    
    /**
     * Get the pet ID
     */
    public UUID getPetId() {
        return petId;
    }
    
    /**
     * Get the owner ID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Get the pet type
     */
    public PetType getPetType() {
        return petType;
    }
    
    /**
     * Get the level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Set the level
     */
    public void setLevel(int level) {
        this.level = Math.min(level, petType.getMaxLevel());
    }
    
    /**
     * Get the experience
     */
    public double getExperience() {
        return experience;
    }
    
    /**
     * Set the experience
     */
    public void setExperience(double experience) {
        this.experience = experience;
    }
    
    /**
     * Add experience
     */
    public void addExperience(double amount) {
        this.experience += amount;
        
        // Check for level up
        double requiredExp = getRequiredExperience(level + 1);
        if (experience >= requiredExp && level < petType.getMaxLevel()) {
            level++;
            experience -= requiredExp;
        }
    }
    
    /**
     * Get the required experience for a level
     */
    private double getRequiredExperience(int level) {
        return level * 100.0; // Simple formula: level * 100
    }
    
    /**
     * Check if the pet is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the pet as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Get the current health
     */
    public double getCurrentHealth() {
        return petType.getBaseHealth() * (1 + (level - 1) * 0.1);
    }
    
    /**
     * Get the current damage
     */
    public double getCurrentDamage() {
        return petType.getBaseDamage() * (1 + (level - 1) * 0.1);
    }
    
    /**
     * Get the current defense
     */
    public double getCurrentDefense() {
        return petType.getBaseDefense() * (1 + (level - 1) * 0.1);
    }
}