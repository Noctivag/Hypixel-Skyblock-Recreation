package de.noctivag.plugin.loot;

/**
 * Mob Drop Configuration - Defines drop parameters for mobs
 */
public class MobDropConfig {
    
    private final String itemId;
    private final String dropPoolId;
    private final double dropChance;
    private final int minAmount;
    private final int maxAmount;
    private final boolean isPetDrop;
    private final boolean isRare;
    private final String displayName;
    
    public MobDropConfig(String itemId, String dropPoolId, double dropChance, 
                        int minAmount, int maxAmount, boolean isPetDrop, 
                        boolean isRare, String displayName) {
        this.itemId = itemId;
        this.dropPoolId = dropPoolId;
        this.dropChance = dropChance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.isPetDrop = isPetDrop;
        this.isRare = isRare;
        this.displayName = displayName;
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public String getDropPoolId() { return dropPoolId; }
    public double getDropChance() { return dropChance; }
    public int getMinAmount() { return minAmount; }
    public int getMaxAmount() { return maxAmount; }
    public boolean isPetDrop() { return isPetDrop; }
    public boolean isRare() { return isRare; }
    public String getDisplayName() { return displayName; }
}