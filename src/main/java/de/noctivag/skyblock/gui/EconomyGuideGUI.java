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
 * Economy Guide GUI - Wirtschafts-Anleitung und -Tipps
 */
public class EconomyGuideGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public EconomyGuideGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openEconomyGuideGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lEconomy Guide"));
        
        // Getting Started
        setItem(gui, 10, Material.BOOK, "§a§lGetting Started",
            "§7Erste Schritte",
            "§7• Economy Basics",
            "§7• First Steps",
            "§7• Beginner Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lEarning Money",
            "§7Geld verdienen",
            "§7• Money Sources",
            "§7• Earning Tips",
            "§7• Earning Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.GOLD_INGOT, "§6§lSpending Money",
            "§7Geld ausgeben",
            "§7• Spending Tips",
            "§7• Smart Spending",
            "§7• Spending Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.DIAMOND, "§b§lSaving Money",
            "§7Geld sparen",
            "§7• Saving Tips",
            "§7• Saving Strategies",
            "§7• Long-term Saving",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lInvesting Money",
            "§7Geld investieren",
            "§7• Investment Tips",
            "§7• Investment Strategies",
            "§7• Risk Management",
            "",
            "§eKlicke zum Öffnen");
            
        // Trading Guide
        setItem(gui, 19, Material.CHEST, "§6§lTrading Guide",
            "§7Handels-Anleitung",
            "§7• Trading Basics",
            "§7• Trading Tips",
            "§7• Trading Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lMarket Guide",
            "§7Markt-Anleitung",
            "§7• Market Basics",
            "§7• Market Tips",
            "§7• Market Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lAuction Guide",
            "§7Auktions-Anleitung",
            "§7• Auction Basics",
            "§7• Auction Tips",
            "§7• Auction Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lBazaar Guide",
            "§7Bazaar-Anleitung",
            "§7• Bazaar Basics",
            "§7• Bazaar Tips",
            "§7• Bazaar Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lExchange Guide",
            "§7Austausch-Anleitung",
            "§7• Exchange Basics",
            "§7• Exchange Tips",
            "§7• Exchange Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Guide
        setItem(gui, 28, Material.PAPER, "§e§lMarket Analysis",
            "§7Marktanalyse",
            "§7• Analysis Basics",
            "§7• Analysis Tips",
            "§7• Analysis Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.CLOCK, "§6§lPrice History",
            "§7Preishistorie",
            "§7• History Basics",
            "§7• History Tips",
            "§7• History Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lMarket Alerts",
            "§7Markt-Alerts",
            "§7• Alert Basics",
            "§7• Alert Tips",
            "§7• Alert Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§b§lMarket Predictions",
            "§7Markt-Vorhersagen",
            "§7• Prediction Basics",
            "§7• Prediction Tips",
            "§7• Prediction Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§d§lMarket Reports",
            "§7Markt-Berichte",
            "§7• Report Basics",
            "§7• Report Tips",
            "§7• Report Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Economy Information
        setItem(gui, 37, Material.BOOK, "§b§lEconomy Information",
            "§7Wirtschafts-Informationen",
            "§7• Economy Rules",
            "§7• Economy Terms",
            "§7• Economy Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lEconomy Statistics",
            "§7Wirtschafts-Statistiken",
            "§7• Economy Status: §aHealthy",
            "§7• Market Activity: §aHigh",
            "§7• Economic Growth: §a+5%",
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
