package de.noctivag.skyblock.gui.details;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

/**
 * Skills Detail GUI - Platzhalter für Skills-Detailansicht
 */
public class SkillsDetailGUI {
    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§bSkills-Details");
        // Beispiel: Custom Head für Combat XP (Hypixel-Style)
        String combatXpBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyYjYyIn19fQ==";
        ItemStack combatHead = de.noctivag.skyblock.util.CustomHeadUtil.getCustomHead("§cCombat XP", combatXpBase64);
        ItemMeta combatMeta = combatHead.getItemMeta();
        combatMeta.displayName(net.kyori.adventure.text.Component.text("§cCombat XP"));
        combatMeta.lore(java.util.List.of(
            net.kyori.adventure.text.Component.text("§7Dein aktueller Combat XP Fortschritt."),
            net.kyori.adventure.text.Component.text("§eKlicke für mehr!")
        ));
        combatHead.setItemMeta(combatMeta);
        inv.setItem(10, combatHead);

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta meta = info.getItemMeta();
        meta.displayName(net.kyori.adventure.text.Component.text("§aSkills-Detailübersicht"));
        meta.lore(java.util.List.of(
            net.kyori.adventure.text.Component.text("§7Hier werden alle Skills im Detail angezeigt."),
            net.kyori.adventure.text.Component.text("§7(Platzhalter-Ansicht)")
        ));
        info.setItemMeta(meta);
        inv.setItem(22, info);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(net.kyori.adventure.text.Component.text("§cSchließen"));
        close.setItemMeta(closeMeta);
        inv.setItem(49, close);
        player.openInventory(inv);
    }
}
