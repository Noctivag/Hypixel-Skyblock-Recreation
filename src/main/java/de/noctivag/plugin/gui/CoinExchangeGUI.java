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
 * Coin Exchange GUI - Münzen-Austausch und Währungsumrechnung
 */
public class CoinExchangeGUI {
    
    private final Plugin plugin;
    
    public CoinExchangeGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openCoinExchangeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lCoin Exchange");
        
        // Exchange Options
        setItem(gui, 10, Material.GOLD_INGOT, "§6§lGold Exchange",
            "§7Gold-Austausch",
            "§7• Gold to Coins",
            "§7• Coins to Gold",
            "§7• Exchange Rate: 1:1",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lEmerald Exchange",
            "§7Smaragd-Austausch",
            "§7• Emerald to Coins",
            "§7• Coins to Emerald",
            "§7• Exchange Rate: 1:10",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lDiamond Exchange",
            "§7Diamant-Austausch",
            "§7• Diamond to Coins",
            "§7• Coins to Diamond",
            "§7• Exchange Rate: 1:100",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.NETHERITE_INGOT, "§5§lNetherite Exchange",
            "§7Netherit-Austausch",
            "§7• Netherite to Coins",
            "§7• Coins to Netherite",
            "§7• Exchange Rate: 1:1000",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial Exchange",
            "§7Besonderer Austausch",
            "§7• Special Items",
            "§7• Event Items",
            "§7• Limited Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Exchange Services
        setItem(gui, 19, Material.EMERALD_BLOCK, "§a§lBulk Exchange",
            "§7Massen-Austausch",
            "§7• Bulk Gold Exchange",
            "§7• Bulk Emerald Exchange",
            "§7• Bulk Diamond Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.GOLD_BLOCK, "§6§lPremium Exchange",
            "§7Premium-Austausch",
            "§7• Premium Rates",
            "§7• VIP Exchange",
            "§7• Special Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.DIAMOND_BLOCK, "§b§lElite Exchange",
            "§7Elite-Austausch",
            "§7• Elite Rates",
            "§7• Master Exchange",
            "§7• Legendary Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHERITE_BLOCK, "§5§lMythic Exchange",
            "§7Mythischer Austausch",
            "§7• Mythic Rates",
            "§7• Ancient Exchange",
            "§7• Divine Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§d§lUltimate Exchange",
            "§7Ultimativer Austausch",
            "§7• Ultimate Rates",
            "§7• Cosmic Exchange",
            "§7• Transcendent Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        // Exchange Tools
        setItem(gui, 28, Material.CLOCK, "§e§lExchange History",
            "§7Austausch-Verlauf",
            "§7• Recent Exchanges",
            "§7• Exchange Statistics",
            "§7• Exchange Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.PAPER, "§b§lExchange Rates",
            "§7Wechselkurse",
            "§7• Current Rates",
            "§7• Rate History",
            "§7• Rate Predictions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lRate Alerts",
            "§7Kurs-Alerts",
            "§7• Rate Change Alerts",
            "§7• Rate Threshold Alerts",
            "§7• Rate Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lExchange Calculator",
            "§7Austausch-Rechner",
            "§7• Exchange Calculator",
            "§7• Rate Calculator",
            "§7• Profit Calculator",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lExchange Guide",
            "§7Austausch-Anleitung",
            "§7• Exchange Basics",
            "§7• Exchange Tips",
            "§7• Exchange Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Exchange Information
        setItem(gui, 37, Material.BOOK, "§b§lExchange Information",
            "§7Austausch-Informationen",
            "§7• Exchange Rules",
            "§7• Exchange Terms",
            "§7• Exchange Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lExchange Statistics",
            "§7Austausch-Statistiken",
            "§7• Total Exchanges: §a0",
            "§7• Exchange Volume: §a$0",
            "§7• Exchange Success Rate: §a0%",
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
