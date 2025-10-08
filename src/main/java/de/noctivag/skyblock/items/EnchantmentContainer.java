package de.noctivag.skyblock.items;

import java.util.HashMap;
import java.util.Map;

/**
 * Container für Enchantments auf einem Item
 */
public class EnchantmentContainer {
    private final Map<EnchantmentType, Integer> enchantments = new HashMap<>();

    public void add(EnchantmentType type, int level) {
        enchantments.put(type, level);
    }
    public int getLevel(EnchantmentType type) {
        return enchantments.getOrDefault(type, 0);
    }
    public Map<EnchantmentType, Integer> getAll() {
        return enchantments;
    }
}
