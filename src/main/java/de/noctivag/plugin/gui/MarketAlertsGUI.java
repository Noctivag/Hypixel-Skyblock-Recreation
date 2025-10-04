package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Market Alerts GUI - Markt-Alerts und Benachrichtigungen
 */
public class MarketAlertsGUI {
    
    private final Plugin plugin;
    
    public MarketAlertsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openMarketAlertsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lMarket Alerts");
        
        // Alert Types
        setItem(gui, 10, Material.BELL, "§e§lPrice Alerts",
            "§7Preis-Alerts",
            "§7• Active Alerts: §a0",
            "§7• Price Thresholds",
            "§7• Alert Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.CLOCK, "§6§lVolume Alerts",
            "§7Volumen-Alerts",
            "§7• Active Alerts: §a0",
            "§7• Volume Thresholds",
            "§7• Volume Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.PAPER, "§b§lTrend Alerts",
            "§7Trend-Alerts",
            "§7• Active Alerts: §a0",
            "§7• Trend Changes",
            "§7• Trend Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EMERALD, "§a§lMarket Alerts",
            "§7Markt-Alerts",
            "§7• Active Alerts: §a0",
            "§7• Market Changes",
            "§7• Market Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial Alerts",
            "§7Besondere Alerts",
            "§7• Active Alerts: §a0",
            "§7• Special Events",
            "§7• Special Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        // Alert Management
        setItem(gui, 19, Material.EMERALD, "§a§lCreate Alert",
            "§7Alert erstellen",
            "§7• New Price Alert",
            "§7• New Volume Alert",
            "§7• New Trend Alert",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.REDSTONE, "§c§lManage Alerts",
            "§7Alerts verwalten",
            "§7• Edit Alerts",
            "§7• Delete Alerts",
            "§7• Alert Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CLOCK, "§e§lAlert History",
            "§7Alert-Verlauf",
            "§7• Recent Alerts",
            "§7• Alert Statistics",
            "§7• Alert Performance",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BOOK, "§b§lAlert Templates",
            "§7Alert-Vorlagen",
            "§7• Pre-made Alerts",
            "§7• Custom Templates",
            "§7• Template Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.ANVIL, "§6§lAlert Settings",
            "§7Alert-Einstellungen",
            "§7• Notification Settings",
            "§7• Alert Frequency",
            "§7• Alert Preferences",
            "",
            "§eKlicke zum Öffnen");
            
        // Alert Categories
        setItem(gui, 28, Material.DIAMOND_SWORD, "§c§lWeapon Alerts",
            "§7Waffen-Alerts",
            "§7• Sword Alerts",
            "§7• Bow Alerts",
            "§7• Special Weapon Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND_CHESTPLATE, "§b§lArmor Alerts",
            "§7Rüstungs-Alerts",
            "§7• Helmet Alerts",
            "§7• Chestplate Alerts",
            "§7• Complete Set Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.ENCHANTED_BOOK, "§d§lEnchantment Alerts",
            "§7Verzauberungs-Alerts",
            "§7• Enchantment Alerts",
            "§7• Rare Enchant Alerts",
            "§7• Custom Enchant Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.EMERALD, "§a§lMaterial Alerts",
            "§7Material-Alerts",
            "§7• Ore Alerts",
            "§7• Gem Alerts",
            "§7• Special Material Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.NETHER_STAR, "§5§lSpecial Item Alerts",
            "§7Besondere Item-Alerts",
            "§7• Rare Item Alerts",
            "§7• Event Item Alerts",
            "§7• Limited Item Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        // Alert Tools
        setItem(gui, 37, Material.BOOK, "§b§lAlert Guide",
            "§7Alert-Anleitung",
            "§7• How to Create Alerts",
            "§7• Alert Tips",
            "§7• Alert Best Practices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lAlert Statistics",
            "§7Alert-Statistiken",
            "§7• Total Alerts: §a0",
            "§7• Active Alerts: §a0",
            "§7• Alert Success Rate: §a0%",
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
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
