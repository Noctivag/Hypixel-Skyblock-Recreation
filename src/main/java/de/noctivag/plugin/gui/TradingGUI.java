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
 * Trading GUI - Handel und Tausch
 */
public class TradingGUI {
    
    private final Plugin plugin;
    
    public TradingGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openTradingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lTrading");
        
        // Trading Options
        setItem(gui, 10, Material.EMERALD, "§a§lStart Trade",
            "§7Handel starten",
            "§7• Trade with Player",
            "§7• Send Trade Request",
            "§7• Accept Trade Request",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.PLAYER_HEAD, "§b§lTrade Requests",
            "§7Handels-Anfragen",
            "§7• Incoming Requests: §a0",
            "§7• Outgoing Requests: §a0",
            "§7• Trade History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.CHEST, "§6§lTrade Inventory",
            "§7Handels-Inventar",
            "§7• Trade Items",
            "§7• Trade Coins",
            "§7• Trade Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.CLOCK, "§e§lTrade History",
            "§7Handels-Verlauf",
            "§7• Recent Trades",
            "§7• Trade Statistics",
            "§7• Trade Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BOOK, "§b§lTrade Rules",
            "§7Handels-Regeln",
            "§7• Trading Rules",
            "§7• Trade Etiquette",
            "§7• Trade Safety",
            "",
            "§eKlicke zum Öffnen");
            
        // Trading Features
        setItem(gui, 19, Material.DIAMOND, "§b§lSecure Trading",
            "§7Sicherer Handel",
            "§7• Secure Trade System",
            "§7• Trade Protection",
            "§7• Trade Verification",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.GOLD_INGOT, "§6§lCoin Trading",
            "§7Münzen-Handel",
            "§7• Coin Exchange",
            "§7• Currency Trading",
            "§7• Exchange Rates",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.EMERALD_BLOCK, "§a§lBulk Trading",
            "§7Massen-Handel",
            "§7• Bulk Item Trading",
            "§7• Mass Coin Trading",
            "§7• Bulk Trade Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHER_STAR, "§d§lSpecial Trading",
            "§7Besonderer Handel",
            "§7• Rare Item Trading",
            "§7• Event Item Trading",
            "§7• Limited Item Trading",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.ENCHANTED_BOOK, "§5§lEnchantment Trading",
            "§7Verzauberungs-Handel",
            "§7• Enchantment Trading",
            "§7• Enchantment Exchange",
            "§7• Custom Enchantment Trading",
            "",
            "§eKlicke zum Öffnen");
            
        // Trading Tools
        setItem(gui, 28, Material.COMPASS, "§e§lFind Traders",
            "§7Händler finden",
            "§7• Online Traders",
            "§7• Active Traders",
            "§7• Trader Search",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BELL, "§a§lTrade Notifications",
            "§7Handels-Benachrichtigungen",
            "§7• Trade Alerts",
            "§7• Notification Settings",
            "§7• Trade Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.CLOCK, "§6§lTrade Schedule",
            "§7Handels-Zeitplan",
            "§7• Scheduled Trades",
            "§7• Trade Appointments",
            "§7• Trade Calendar",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.BOOK, "§b§lTrade Contracts",
            "§7Handels-Verträge",
            "§7• Trade Agreements",
            "§7• Contract Management",
            "§7• Trade Terms",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.EMERALD, "§a§lTrade Insurance",
            "§7Handels-Versicherung",
            "§7• Trade Protection",
            "§7• Insurance Coverage",
            "§7• Claim Management",
            "",
            "§eKlicke zum Öffnen");
            
        // Trading Information
        setItem(gui, 37, Material.BOOK, "§b§lTrading Guide",
            "§7Handels-Anleitung",
            "§7• How to Trade",
            "§7• Trading Tips",
            "§7• Trading Safety",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lTrading Statistics",
            "§7Handels-Statistiken",
            "§7• Total Trades: §a0",
            "§7• Success Rate: §a0%",
            "§7• Total Value: §a$0",
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
