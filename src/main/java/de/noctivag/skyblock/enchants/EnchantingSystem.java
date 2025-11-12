package de.noctivag.skyblock.enchants;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Complete Enchanting System for Hypixel Skyblock
 * Handles applying, removing, and managing custom enchantments
 */
public class EnchantingSystem {

    private final SkyblockPlugin plugin;
    private final Map<UUID, Map<CustomEnchantment, Integer>> playerEnchants = new HashMap<>();
    
    public EnchantingSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Apply an enchantment to an item
     */
    public boolean applyEnchantment(Player player, ItemStack item, CustomEnchantment enchant, int level) {
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cInvalid item!");
            return false;
        }

        // Check if level is valid
        if (level < 1 || level > enchant.getMaxLevel()) {
            player.sendMessage("§cInvalid enchantment level! Max: " + enchant.getMaxLevel());
            return false;
        }

        // Check if item type is compatible
        String itemType = item.getType().name();
        if (!enchant.canApplyTo(itemType)) {
            player.sendMessage("§cThis enchantment cannot be applied to this item!");
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        Map<CustomEnchantment, Integer> existingEnchants = getEnchantments(item);

        // Check for ultimate enchantment conflicts
        if (enchant.getType() == CustomEnchantment.EnchantType.ULTIMATE) {
            // Remove existing ultimate enchantment
            existingEnchants.entrySet().removeIf(entry -> {
                if (entry.getKey().getType() == CustomEnchantment.EnchantType.ULTIMATE) {
                    removeEnchantmentFromLore(lore, entry.getKey());
                    return true;
                }
                return false;
            });
        }

        // Check for One For All (removes ALL other enchants)
        if (enchant == CustomEnchantment.ULTIMATE_ONE_FOR_ALL) {
            lore.removeIf(line -> line.contains("§9") || line.contains("§5"));
            existingEnchants.clear();
            player.sendMessage("§6One For All removed all other enchantments!");
        }

        // Add or update enchantment
        addEnchantmentToLore(lore, enchant, level);
        existingEnchants.put(enchant, level);

        meta.setLore(lore);
        item.setItemMeta(meta);

        String color = enchant.getType() == CustomEnchantment.EnchantType.ULTIMATE ? "§d" : "§9";
        player.sendMessage("§aApplied " + color + enchant.getDisplayName() + " " + toRoman(level) + " §ato your item!");
        return true;
    }

    /**
     * Get all enchantments on an item
     */
    public Map<CustomEnchantment, Integer> getEnchantments(ItemStack item) {
        Map<CustomEnchantment, Integer> enchants = new HashMap<>();
        if (item == null || !item.hasItemMeta()) return enchants;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return enchants;

        for (String line : meta.getLore()) {
            for (CustomEnchantment enchant : CustomEnchantment.values()) {
                if (line.contains(enchant.getDisplayName())) {
                    // Extract level from roman numeral
                    int level = extractLevel(line);
                    if (level > 0) {
                        enchants.put(enchant, level);
                    }
                    break;
                }
            }
        }

        return enchants;
    }

    /**
     * Remove an enchantment from an item
     */
    public boolean removeEnchantment(Player player, ItemStack item, CustomEnchantment enchant) {
        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;

        List<String> lore = new ArrayList<>(meta.getLore());
        boolean removed = removeEnchantmentFromLore(lore, enchant);

        if (removed) {
            meta.setLore(lore);
            item.setItemMeta(meta);
            player.sendMessage("§aRemoved " + enchant.getDisplayName() + "!");
            return true;
        }

        player.sendMessage("§cEnchantment not found on item!");
        return false;
    }

    /**
     * Add enchantment to lore
     */
    private void addEnchantmentToLore(List<String> lore, CustomEnchantment enchant, int level) {
        // Remove existing if present
        removeEnchantmentFromLore(lore, enchant);

        // Find where to insert (after stats, before rarity)
        int insertIndex = 0;
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (line.contains("COMMON") || line.contains("UNCOMMON") || line.contains("RARE") ||
                line.contains("EPIC") || line.contains("LEGENDARY") || line.contains("MYTHIC")) {
                insertIndex = i;
                break;
            }
        }

        // Create enchantment line
        String color = enchant.getType() == CustomEnchantment.EnchantType.ULTIMATE ? "§d§l" : "§9";
        String line = color + enchant.getDisplayName() + " " + toRoman(level);
        
        if (enchant.getType() == CustomEnchantment.EnchantType.ULTIMATE) {
            line += " §6§l✪";
        }

        if (insertIndex > 0) {
            // Add separator if needed
            if (!lore.get(insertIndex - 1).isEmpty()) {
                lore.add(insertIndex, "");
                insertIndex++;
            }
            lore.add(insertIndex, line);
        } else {
            lore.add(line);
        }
    }

    /**
     * Remove enchantment from lore
     */
    private boolean removeEnchantmentFromLore(List<String> lore, CustomEnchantment enchant) {
        return lore.removeIf(line -> line.contains(enchant.getDisplayName()));
    }

    /**
     * Extract level from roman numeral in string
     */
    private int extractLevel(String line) {
        String[] parts = line.split(" ");
        for (String part : parts) {
            int level = fromRoman(part.replaceAll("§.", ""));
            if (level > 0) return level;
        }
        return 0;
    }

    /**
     * Convert number to Roman numeral
     */
    private String toRoman(int number) {
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        if (number >= 1 && number <= 10) {
            return romanNumerals[number];
        }
        return String.valueOf(number);
    }

    /**
     * Convert Roman numeral to number
     */
    private int fromRoman(String roman) {
        Map<String, Integer> romanMap = Map.of(
            "I", 1, "II", 2, "III", 3, "IV", 4, "V", 5,
            "VI", 6, "VII", 7, "VIII", 8, "IX", 9, "X", 10
        );
        return romanMap.getOrDefault(roman, 0);
    }

    /**
     * Calculate cost of enchanting
     */
    public int calculateCost(CustomEnchantment enchant, int level) {
        int baseCost = enchant.getType() == CustomEnchantment.EnchantType.ULTIMATE ? 100000 : 1000;
        return baseCost * level * level; // Exponential scaling
    }

    /**
     * Get enchantment power for magical calculations
     */
    public int getEnchantmentPower(ItemStack item) {
        Map<CustomEnchantment, Integer> enchants = getEnchantments(item);
        int power = 0;

        for (Map.Entry<CustomEnchantment, Integer> entry : enchants.entrySet()) {
            int value = entry.getValue();
            if (entry.getKey().getType() == CustomEnchantment.EnchantType.ULTIMATE) {
                power += value * 10; // Ultimate enchants worth 10x more
            } else {
                power += value;
            }
        }

        return power;
    }
}
