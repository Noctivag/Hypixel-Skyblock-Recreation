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
 * Market Analysis GUI - Marktanalyse und Trends
 */
public class MarketAnalysisGUI {
    
    private final Plugin plugin;
    
    public MarketAnalysisGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openMarketAnalysisGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lMarket Analysis");
        
        // Market Overview
        setItem(gui, 10, Material.PAPER, "§a§lMarket Overview",
            "§7Markt-Übersicht",
            "§7• Market Status",
            "§7• Market Trends",
            "§7• Market Performance",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND, "§b§lPrice Trends",
            "§7Preis-Trends",
            "§7• Rising Prices",
            "§7• Falling Prices",
            "§7• Stable Prices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lVolume Analysis",
            "§7Volumen-Analyse",
            "§7• Trading Volume",
            "§7• Volume Trends",
            "§7• Volume Patterns",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.GOLD_INGOT, "§6§lMarket Cap",
            "§7Marktkapitalisierung",
            "§7• Total Market Cap",
            "§7• Market Cap Trends",
            "§7• Market Cap Analysis",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lMarket Indicators",
            "§7Markt-Indikatoren",
            "§7• Market Health",
            "§7• Market Sentiment",
            "§7• Market Predictions",
            "",
            "§eKlicke zum Öffnen");
            
        // Item Analysis
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lWeapon Analysis",
            "§7Waffen-Analyse",
            "§7• Weapon Prices",
            "§7• Weapon Trends",
            "§7• Weapon Demand",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_CHESTPLATE, "§b§lArmor Analysis",
            "§7Rüstungs-Analyse",
            "§7• Armor Prices",
            "§7• Armor Trends",
            "§7• Armor Demand",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTED_BOOK, "§d§lEnchantment Analysis",
            "§7Verzauberungs-Analyse",
            "§7• Enchantment Prices",
            "§7• Enchantment Trends",
            "§7• Enchantment Demand",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.EMERALD, "§a§lMaterial Analysis",
            "§7Material-Analyse",
            "§7• Material Prices",
            "§7• Material Trends",
            "§7• Material Demand",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§5§lSpecial Item Analysis",
            "§7Besondere Item-Analyse",
            "§7• Special Item Prices",
            "§7• Special Item Trends",
            "§7• Special Item Demand",
            "",
            "§eKlicke zum Öffnen");
            
        // Market Tools
        setItem(gui, 28, Material.CLOCK, "§e§lHistorical Data",
            "§7Historische Daten",
            "§7• Price History",
            "§7• Volume History",
            "§7• Trend History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.CLOCK, "§6§lMarket Calendar",
            "§7Markt-Kalender",
            "§7• Market Events",
            "§7• Seasonal Trends",
            "§7• Event Impact",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lMarket Alerts",
            "§7Markt-Alerts",
            "§7• Price Alerts",
            "§7• Volume Alerts",
            "§7• Trend Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§b§lMarket Predictions",
            "§7Markt-Vorhersagen",
            "§7• Price Predictions",
            "§7• Trend Predictions",
            "§7• Market Forecasts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§d§lMarket Reports",
            "§7Markt-Berichte",
            "§7• Daily Reports",
            "§7• Weekly Reports",
            "§7• Monthly Reports",
            "",
            "§eKlicke zum Öffnen");
            
        // Market Information
        setItem(gui, 37, Material.BOOK, "§b§lMarket Guide",
            "§7Markt-Anleitung",
            "§7• Market Basics",
            "§7• Analysis Tips",
            "§7• Trading Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lMarket Statistics",
            "§7Markt-Statistiken",
            "§7• Market Activity: §aHigh",
            "§7• Market Volatility: §aMedium",
            "§7• Market Growth: §a+5%",
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
