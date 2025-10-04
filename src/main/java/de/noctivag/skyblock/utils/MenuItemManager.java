package de.noctivag.skyblock.utils;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;

public class MenuItemManager {
    
    public static void giveMenuItem(Player player) {
        ItemStack menuItem = createMenuItem();
        
        // Setze das Item in den letzten Slot (Slot 8)
        player.getInventory().setItem(8, menuItem);
        player.updateInventory();
    }
    
    public static ItemStack createMenuItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        
        // Use LegacyComponentSerializer to create Components from legacy color codes
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§6✧ Hauptmenü ✧"));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize("§7Rechtsklick um das Hauptmenü zu öffnen"),
            LegacyComponentSerializer.legacySection().deserialize("§eAlle Plugin-Features an einem Ort!")
        ));
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static boolean isMenuItem(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        Component comp = meta.displayName();
        if (comp == null) return false;
        String displayName = LegacyComponentSerializer.legacySection().serialize(comp);
        return displayName.contains("§6✧ Hauptmenü ✧") || displayName.contains("Hauptmenü");
    }
}
