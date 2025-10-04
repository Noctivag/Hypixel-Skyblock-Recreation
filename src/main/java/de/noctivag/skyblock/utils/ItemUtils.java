package de.noctivag.skyblock.utils;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static ItemStack parseItem(String itemString) {
        String[] parts = itemString.split(" ");
        Material material = Material.matchMaterial(parts[0]);
        if (material == null) {
            return null;
        }
        int amount = 1;
        if (parts.length > 1) {
            try {
                amount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<Component> lore = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                String part = parts[i];
                if (part.contains(":")) {
                    String[] enchantParts = part.split(":");
                    // Use NamespacedKey for modern enchantment lookup
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantParts[0].toLowerCase()));
                    if (enchantment == null) {
                        // fallback to deprecated getByName for custom/legacy
                        enchantment = Enchantment.getByName(enchantParts[0].toUpperCase());
                    }
                    if (enchantment != null) {
                        int level = 1;
                        if (enchantParts.length > 1) {
                            try {
                                level = Integer.parseInt(enchantParts[1]);
                            } catch (NumberFormatException e) {
                                // Ignore
                            }
                        }
                        meta.addEnchant(enchantment, level, true);
                    }
                } else if (part.startsWith("name:")) {
                    // Use Adventure API for display name
                    String legacyName = ChatColor.translateAlternateColorCodes('&', part.substring(5).replace("_", " "));
                    meta.displayName(LegacyComponentSerializer.legacySection().deserialize(legacyName));
                } else {
                    // Use Adventure API for lore
                    String legacyLore = ChatColor.translateAlternateColorCodes('&', part.replace("_", " "));
                    lore.add(LegacyComponentSerializer.legacySection().deserialize(legacyLore));
                }
            }
            if (!lore.isEmpty()) {
                meta.lore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
