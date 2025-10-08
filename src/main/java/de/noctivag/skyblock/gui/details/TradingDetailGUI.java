package de.noctivag.skyblock.gui.details;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

/**
 * Trading Detail GUI - Platzhalter für Trading-Detailansicht
 */
public class TradingDetailGUI {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§aTrading-Details");
        ItemStack info = new ItemStack(Material.EMERALD);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName("§aTrading-Detailübersicht");
        meta.setLore(Arrays.asList("§7Hier werden alle Handelsoptionen im Detail angezeigt.", "§7(Platzhalter-Ansicht)"));
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
