package de.noctivag.skyblock.enchants;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an enchanted book
 */
public class EnchantedBook {

    private final CustomEnchantment enchantment;
    private final int level;

    public EnchantedBook(CustomEnchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = Math.min(level, enchantment.getMaxLevel());
    }

    /**
     * Create the ItemStack for this enchanted book
     */
    public ItemStack create() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        if (meta != null) {
            String color = enchantment.getType() == CustomEnchantment.EnchantType.ULTIMATE ? "§d§l" : "§9";
            String name = color + enchantment.getDisplayName() + " " + toRoman(level);
            
            if (enchantment.getType() == CustomEnchantment.EnchantType.ULTIMATE) {
                name += " §6§l✪";
            }

            meta.setDisplayName(name);

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7" + enchantment.getDescription());
            lore.add("");
            
            if (enchantment.getType() == CustomEnchantment.EnchantType.ULTIMATE) {
                lore.add("§d§lULTIMATE ENCHANTMENT");
                lore.add("§7Only one Ultimate Enchantment");
                lore.add("§7can be applied per item!");
                lore.add("");
            }
            
            lore.add("§7Applicable to:");
            lore.add("§7- §b" + getApplicableToString());
            lore.add("");
            lore.add("§7Combine in Anvil to apply!");
            lore.add("");
            
            String rarity = enchantment.getType() == CustomEnchantment.EnchantType.ULTIMATE ? 
                          "§d§lMYTHIC" : level >= 5 ? "§5§lEPIC" : "§9§lRARE";
            lore.add(rarity + " ENCHANTED BOOK");

            meta.setLore(lore);
            book.setItemMeta(meta);
        }

        return book;
    }

    private String getApplicableToString() {
        return switch (enchantment.getApplicableTo()) {
            case SWORD -> "Swords";
            case BOW -> "Bows";
            case ARMOR -> "Armor";
            case HELMET -> "Helmets";
            case CHESTPLATE -> "Chestplates";
            case LEGGINGS -> "Leggings";
            case BOOTS -> "Boots";
            case TOOL -> "Tools";
            case PICKAXE -> "Pickaxes";
            case AXE -> "Axes";
            case SHOVEL -> "Shovels";
            case HOE -> "Hoes";
            case FISHING_ROD -> "Fishing Rods";
            case WEAPON_ARMOR -> "Weapons & Armor";
            case ALL -> "All Items";
        };
    }

    private String toRoman(int number) {
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (number >= 1 && number <= 10) {
            return romanNumerals[number];
        }
        return String.valueOf(number);
    }

    public CustomEnchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }
}
