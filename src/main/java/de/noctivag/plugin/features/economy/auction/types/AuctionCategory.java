package de.noctivag.plugin.features.economy.auction.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Auction Category enum for auction categories
 */
public enum AuctionCategory {
    WEAPONS("Weapons", "Combat weapons and tools", Material.DIAMOND_SWORD),
    ARMOR("Armor", "Protective gear", Material.DIAMOND_CHESTPLATE),
    ACCESSORIES("Accessories", "Talismans and rings", Material.GOLDEN_APPLE),
    BLOCKS("Blocks", "Building materials", Material.STONE),
    FOOD("Food", "Consumable items", Material.BREAD),
    POTIONS("Potions", "Brewing ingredients", Material.POTION),
    ENCHANTED_BOOKS("Enchanted Books", "Enchantment books", Material.ENCHANTED_BOOK),
    MINIONS("Minions", "Automated collectors", Material.IRON_BLOCK),
    PETS("Pets", "Companion animals", Material.WOLF_SPAWN_EGG),
    MISCELLANEOUS("Miscellaneous", "Other items", Material.CHEST),
    WEAPONS_ARMOR("Weapons & Armor", "Combat equipment", Material.DIAMOND_SWORD),
    TOOLS("Tools", "Utility tools", Material.DIAMOND_PICKAXE),
    ENCHANTED("Enchanted", "Enchanted items", Material.ENCHANTED_BOOK),
    MATERIALS("Materials", "Raw materials", Material.COBBLESTONE);

    private final String name;
    private final String description;
    private final Material icon;

    AuctionCategory(String name, String description, Material icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getIcon() {
        return icon;
    }
}
