package de.noctivag.skyblock.brewing;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Brewing ingredient
 */
public class BrewingIngredient {
    
    private final Material material;
    private final int amount;
    private final String name;
    private final String description;
    
    public BrewingIngredient(Material material, int amount, String name, String description) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.description = description;
    }
    
    /**
     * Create ingredient from ItemStack
     */
    public static BrewingIngredient fromItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        
        return new BrewingIngredient(
            itemStack.getType(),
            itemStack.getAmount(),
            itemStack.getType().name(),
            "A brewing ingredient"
        );
    }
    
    /**
     * Convert to ItemStack
     */
    public ItemStack toItemStack() {
        return new ItemStack(material, amount);
    }
    
    // Getters
    public Material getMaterial() { return material; }
    public int getAmount() { return amount; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}
