package de.noctivag.skyblock.brewing;

import org.bukkit.inventory.ItemStack;

/**
 * Result of a brewing operation
 */
public class BrewingResult {
    
    private final boolean success;
    private final String message;
    private final ItemStack result;
    private final int experience;
    private final long brewingTime;
    
    public BrewingResult(boolean success, String message, ItemStack result, int experience, long brewingTime) {
        this.success = success;
        this.message = message;
        this.result = result;
        this.experience = experience;
        this.brewingTime = brewingTime;
    }
    
    /**
     * Create successful result
     */
    public static BrewingResult success(ItemStack result, int experience, long brewingTime) {
        return new BrewingResult(true, "Brewing completed successfully", result, experience, brewingTime);
    }
    
    /**
     * Create failed result
     */
    public static BrewingResult failure(String message) {
        return new BrewingResult(false, message, null, 0, 0);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ItemStack getResult() { return result; }
    public int getExperience() { return experience; }
    public long getBrewingTime() { return brewingTime; }
}
