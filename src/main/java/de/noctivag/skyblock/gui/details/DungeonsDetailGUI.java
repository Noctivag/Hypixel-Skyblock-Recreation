package de.noctivag.skyblock.gui.details;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

/**
 * Dungeons Detail GUI - Platzhalter für Dungeons-Detailansicht
 */
public class DungeonsDetailGUI {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§cDungeons-Details");
        ItemStack info = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName("§aDungeons-Detailübersicht");
        meta.setLore(Arrays.asList("§7Hier werden alle Dungeons im Detail angezeigt.", "§7(Platzhalter-Ansicht)"));
        info.setItemMeta(meta);
        inv.setItem(22, info);
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cSchließen");
        close.setItemMeta(closeMeta);
        inv.setItem(49, close);
        player.openInventory(inv);
    }
}
