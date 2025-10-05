package de.noctivag.skyblock.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Custom Item - Represents a custom item
 */
public class CustomItem {
    
    private final CustomItemType itemType;
    private final int amount;
    private final List<String> lore;
    
    public CustomItem(CustomItemType itemType, int amount, List<String> lore) {
        this.itemType = itemType;
        this.amount = amount;
        this.lore = lore;
    }
    
    /**
     * Get the item type
     */
    public CustomItemType getItemType() {
        return itemType;
    }
    
    /**
     * Get the amount
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * Get the lore
     */
    public List<String> getLore() {
        return lore;
    }
    
    /**
     * Create an ItemStack from this custom item
     */
    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(itemType.getMaterial(), amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(itemType.getName());
            meta.setLore(lore);
            meta.setCustomModelData(itemType.getCustomModelData());
            if (itemType.isGlowing()) {
                meta.setEnchantmentGlintOverride(true);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}

