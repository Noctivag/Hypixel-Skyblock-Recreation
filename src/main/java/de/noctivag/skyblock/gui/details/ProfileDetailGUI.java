package de.noctivag.skyblock.gui.details;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

/**
 * Profile Detail GUI - Platzhalter für Profil-Detailansicht
 */
public class ProfileDetailGUI {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§eProfil-Details");
        ItemStack info = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = info.getItemMeta();
        meta.setDisplayName("§aProfil-Detailübersicht");
        meta.setLore(Arrays.asList("§7Hier werden alle Profildaten im Detail angezeigt.", "§7(Platzhalter-Ansicht)"));
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
