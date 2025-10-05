package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Economy Settings GUI - Wirtschafts-Einstellungen und -Konfiguration
 */
public class EconomySettingsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public EconomySettingsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openEconomySettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lEconomy Settings"));
        
        // General Settings
        setItem(gui, 10, Material.REDSTONE, "§c§lGeneral Settings",
            "§7Allgemeine Einstellungen",
            "§7• Currency Settings",
            "§7• Display Settings",
            "§7• Language Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lPrivacy Settings",
            "§7Datenschutz-Einstellungen",
            "§7• Balance Visibility",
            "§7• Transaction Privacy",
            "§7• Market Privacy",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BELL, "§e§lNotification Settings",
            "§7Benachrichtigungs-Einstellungen",
            "§7• Transaction Notifications",
            "§7• Market Notifications",
            "§7• Price Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.WRITABLE_BOOK, "§b§lChat Settings",
            "§7Chat-Einstellungen",
            "§7• Chat Format",
            "§7• Chat Colors",
            "§7• Chat Filters",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§d§lNavigation Settings",
            "§7Navigations-Einstellungen",
            "§7• GUI Navigation",
            "§7• Menu Settings",
            "§7• Hotkey Settings",
            "",
            "§eKlicke zum Öffnen");
            
        // Trading Settings
        setItem(gui, 19, Material.CHEST, "§6§lTrading Settings",
            "§7Handels-Einstellungen",
            "§7• Trade Permissions",
            "§7• Trade Limits",
            "§7• Trade Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lMarket Settings",
            "§7Markt-Einstellungen",
            "§7• Market Access",
            "§7• Market Limits",
            "§7• Market Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lAuction Settings",
            "§7Auktions-Einstellungen",
            "§7• Auction Permissions",
            "§7• Auction Limits",
            "§7• Auction Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lBazaar Settings",
            "§7Bazaar-Einstellungen",
            "§7• Bazaar Permissions",
            "§7• Bazaar Limits",
            "§7• Bazaar Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lExchange Settings",
            "§7Austausch-Einstellungen",
            "§7• Exchange Permissions",
            "§7• Exchange Limits",
            "§7• Exchange Security",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Settings
        setItem(gui, 28, Material.PAPER, "§e§lMarket Analysis Settings",
            "§7Marktanalyse-Einstellungen",
            "§7• Analysis Permissions",
            "§7• Analysis Limits",
            "§7• Analysis Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.CLOCK, "§6§lPrice History Settings",
            "§7Preishistorie-Einstellungen",
            "§7• History Permissions",
            "§7• History Limits",
            "§7• History Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lMarket Alert Settings",
            "§7Markt-Alert-Einstellungen",
            "§7• Alert Permissions",
            "§7• Alert Limits",
            "§7• Alert Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§b§lMarket Prediction Settings",
            "§7Markt-Vorhersage-Einstellungen",
            "§7• Prediction Permissions",
            "§7• Prediction Limits",
            "§7• Prediction Security",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§d§lMarket Report Settings",
            "§7Markt-Bericht-Einstellungen",
            "§7• Report Permissions",
            "§7• Report Limits",
            "§7• Report Security",
            "",
            "§eKlicke zum Öffnen");
            
        // Settings Management
        setItem(gui, 37, Material.EMERALD, "§a§lReset Settings",
            "§7Einstellungen zurücksetzen",
            "§7• Reset to Default",
            "§7• Reset Specific Settings",
            "§7• Backup Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSettings History",
            "§7Einstellungs-Verlauf",
            "§7• Recent Changes",
            "§7• Settings Backup",
            "§7• Restore Settings",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
        
        player.openInventory(gui);
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
