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
 * Price History GUI - Preishistorie und Charts
 */
public class PriceHistoryGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public PriceHistoryGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openPriceHistoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lPrice History"));
        
        // Time Periods
        setItem(gui, 10, Material.CLOCK, "§e§lLast 24 Hours",
            "§7Letzte 24 Stunden",
            "§7• Hourly Prices",
            "§7• Price Changes",
            "§7• Volume Changes",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.CLOCK, "§a§lLast 7 Days",
            "§7Letzte 7 Tage",
            "§7• Daily Prices",
            "§7• Weekly Trends",
            "§7• Volume Trends",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.END_CRYSTAL, "§9§lLast 30 Days",
            "§7Letzte 30 Tage",
            "§7• Weekly Prices",
            "§7• Monthly Trends",
            "§7• Volume Analysis",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.GOLD_BLOCK, "§6§lLast 90 Days",
            "§7Letzte 90 Tage",
            "§7• Monthly Prices",
            "§7• Quarterly Trends",
            "§7• Volume Patterns",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lAll Time",
            "§7Gesamte Zeit",
            "§7• Historical Prices",
            "§7• Long-term Trends",
            "§7• Volume History",
            "",
            "§eKlicke zum Öffnen");
            
        // Item Categories
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lWeapons",
            "§7Waffen-Preishistorie",
            "§7• Sword Prices",
            "§7• Bow Prices",
            "§7• Special Weapon Prices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_CHESTPLATE, "§b§lArmor",
            "§7Rüstungs-Preishistorie",
            "§7• Helmet Prices",
            "§7• Chestplate Prices",
            "§7• Complete Set Prices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTED_BOOK, "§d§lEnchanted Books",
            "§7Verzauberte Bücher-Preishistorie",
            "§7• Enchantment Prices",
            "§7• Rare Enchant Prices",
            "§7• Custom Enchant Prices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.EMERALD, "§a§lMaterials",
            "§7Material-Preishistorie",
            "§7• Ore Prices",
            "§7• Gem Prices",
            "§7• Special Material Prices",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§5§lSpecial Items",
            "§7Besondere Items-Preishistorie",
            "§7• Rare Item Prices",
            "§7• Event Item Prices",
            "§7• Limited Item Prices",
            "",
            "§eKlicke zum Öffnen");
            
        // Chart Types
        setItem(gui, 28, Material.PAPER, "§e§lLine Charts",
            "§7Liniendiagramme",
            "§7• Price Line Charts",
            "§7• Volume Line Charts",
            "§7• Trend Line Charts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BARRIER, "§6§lBar Charts",
            "§7Balkendiagramme",
            "§7• Price Bar Charts",
            "§7• Volume Bar Charts",
            "§7• Comparison Bar Charts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.CAKE, "§d§lPie Charts",
            "§7Kreisdiagramme",
            "§7• Market Share Charts",
            "§7• Category Distribution",
            "§7• Volume Distribution",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§b§lCandlestick Charts",
            "§7Kerzendiagramme",
            "§7• Price Candlesticks",
            "§7• Volume Candlesticks",
            "§7• Trend Candlesticks",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§a§lHeat Maps",
            "§7Heat Maps",
            "§7• Price Heat Maps",
            "§7• Volume Heat Maps",
            "§7• Trend Heat Maps",
            "",
            "§eKlicke zum Öffnen");
            
        // Analysis Tools
        setItem(gui, 37, Material.BOOK, "§b§lPrice Analysis",
            "§7Preis-Analyse",
            "§7• Price Patterns",
            "§7• Price Predictions",
            "§7• Price Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPrice Statistics",
            "§7Preis-Statistiken",
            "§7• Average Price: §a$0",
            "§7• Price Range: §a$0 - $0",
            "§7• Price Volatility: §a0%",
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
