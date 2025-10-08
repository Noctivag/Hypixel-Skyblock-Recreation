package de.noctivag.skyblock.items;

import de.noctivag.skyblock.stats.StatType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility für die Konvertierung von SkyblockItem zu Bukkit ItemStack mit dynamischer Lore
 */
public class SkyblockItemBuilder {
    public static ItemStack build(SkyblockItem item, Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text(item.getName()));
        List<Component> lore = new ArrayList<>();
        // Reforge
        if (item.getReforgeType() != null) {
            lore.add(Component.text("§6Reforge: " + item.getReforgeType().name()));
            for (var mod : item.getReforgeType().getModifier().getStats().entrySet()) {
                if (mod.getValue() != 0) {
                    lore.add(Component.text("§7" + mod.getKey().name().replace('_', ' ') + ": §b" + mod.getValue() + " (Reforge)"));
                }
            }
        }
        // Stats
        for (Map.Entry<StatType, Double> entry : item.getAllStats().entrySet()) {
            if (entry.getValue() != 0) {
                lore.add(Component.text("§7" + entry.getKey().name().replace('_', ' ') + ": §b" + entry.getValue()));
            }
        }
        // Enchantments
        if (!item.getEnchantments().getAll().isEmpty()) {
            lore.add(Component.text("§9Verzauberungen:"));
            item.getEnchantments().getAll().forEach((ench, lvl) ->
                lore.add(Component.text("§7" + ench.name() + " " + lvl + " (" + ench.getStat().name().replace('_', ' ') + ": +" + (ench.getValue() * lvl) + ")"))
            );
        }
        // Hot Potato Books
        if (item.getHotPotatoBooks() > 0) {
            lore.add(Component.text("§cHot Potato Books: " + item.getHotPotatoBooks() + "/10"));
        }
        // Ultimate Enchantments
        if (!item.getUltimateEnchantments().getAll().isEmpty()) {
            lore.add(Component.text("§dUltimate-Verzauberungen:"));
            item.getUltimateEnchantments().getAll().forEach((ench, lvl) ->
                lore.add(Component.text("§7" + ench.name() + " " + lvl + " (" + ench.getStat().name().replace('_', ' ') + ": +" + (ench.getValue() * lvl) + ")"))
            );
        }
        // Dungeon Stars
        if (item.getDungeonStar() != null && item.getDungeonStar().getStars() > 0) {
            lore.add(Component.text("§6Dungeon Stars: " + item.getDungeonStar().getStarDisplay()));
        }
        // Rarity
        lore.add(Component.text("§8Seltenheit: " + item.getRarity()));
        // Ability
        if (item.getAbility() != null) {
            lore.add(Component.text("§aFähigkeit: " + item.getAbility()));
        }
        meta.lore(lore);
        stack.setItemMeta(meta);
        return stack;
    }
}
