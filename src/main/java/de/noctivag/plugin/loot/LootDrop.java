package de.noctivag.plugin.loot;

import org.bukkit.inventory.ItemStack;

/**
 * Loot Drop - Represents a successful loot drop
 */
public class LootDrop {
    
    private final MobDropConfig dropConfig;
    private final double dropChance;
    private final int amount;
    private final ItemStack itemStack;
    private final long dropTime;
    
    public LootDrop(MobDropConfig dropConfig, double dropChance) {
        this.dropConfig = dropConfig;
        this.dropChance = dropChance;
        this.amount = calculateAmount(dropConfig);
        this.itemStack = createItemStack(dropConfig, amount);
        this.dropTime = System.currentTimeMillis();
    }
    
    public LootDrop(MobDropConfig dropConfig, double dropChance, int amount) {
        this.dropConfig = dropConfig;
        this.dropChance = dropChance;
        this.amount = amount;
        this.itemStack = createItemStack(dropConfig, amount);
        this.dropTime = System.currentTimeMillis();
    }
    
    private int calculateAmount(MobDropConfig dropConfig) {
        // Calculate drop amount based on config
        return dropConfig.getMinAmount() + (int)(Math.random() * (dropConfig.getMaxAmount() - dropConfig.getMinAmount() + 1));
    }
    
    private ItemStack createItemStack(MobDropConfig dropConfig, int amount) {
        // Create ItemStack based on drop config
        // This would integrate with the actual item creation system
        return new ItemStack(org.bukkit.Material.DIAMOND, amount);
    }
    
    // Getters
    public MobDropConfig getDropConfig() { return dropConfig; }
    public double getDropChance() { return dropChance; }
    public int getAmount() { return amount; }
    public ItemStack getItemStack() { return itemStack; }
    public long getDropTime() { return dropTime; }
    
    public String getItemId() {
        return dropConfig != null ? dropConfig.getItemId() : "unknown";
    }
    
    public String getDropPoolId() {
        return dropConfig != null ? dropConfig.getDropPoolId() : "unknown";
    }
}
