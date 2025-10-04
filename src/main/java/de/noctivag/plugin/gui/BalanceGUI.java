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
 * Balance GUI - Kontostand und Finanzen
 */
public class BalanceGUI {
    
    private final Plugin plugin;
    
    public BalanceGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openBalanceGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBalance");
        
        // Balance Information
        setItem(gui, 10, Material.EMERALD, "§a§lCurrent Balance",
            "§7Aktueller Kontostand",
            "§7• Coins: §a$0",
            "§7• Bank: §a$0",
            "§7• Total: §a$0",
            "",
            "§eKlicke zum Aktualisieren");
            
        setItem(gui, 11, Material.GOLD_INGOT, "§6§lBank Account",
            "§7Bank-Konto",
            "§7• Bank Balance: §a$0",
            "§7• Interest Rate: §a5%",
            "§7• Next Interest: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lSavings Account",
            "§7Sparkonto",
            "§7• Savings: §a$0",
            "§7• Interest Rate: §a10%",
            "§7• Next Interest: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EMERALD_BLOCK, "§a§lInvestment Account",
            "§7Anlagekonto",
            "§7• Investments: §a$0",
            "§7• Return Rate: §a15%",
            "§7• Next Return: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.GOLD_BLOCK, "§6§lPremium Account",
            "§7Premium-Konto",
            "§7• Premium Balance: §a$0",
            "§7• Bonus Rate: §a20%",
            "§7• Next Bonus: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        // Transaction History
        setItem(gui, 19, Material.BOOK, "§b§lTransaction History",
            "§7Transaktions-Verlauf",
            "§7• Recent Transactions",
            "§7• Transaction Details",
            "§7• Transaction Search",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.PAPER, "§e§lRecent Transactions",
            "§7Letzte Transaktionen",
            "§7• Last 10 Transactions",
            "§7• Transaction Types",
            "§7• Transaction Amounts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CLOCK, "§6§lTransaction Statistics",
            "§7Transaktions-Statistiken",
            "§7• Total Transactions: §a0",
            "§7• Total Income: §a$0",
            "§7• Total Expenses: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.PAPER, "§d§lFinancial Charts",
            "§7Finanz-Charts",
            "§7• Balance History",
            "§7• Income Charts",
            "§7• Expense Charts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CLOCK, "§a§lMonthly Report",
            "§7Monatsbericht",
            "§7• Monthly Income: §a$0",
            "§7• Monthly Expenses: §a$0",
            "§7• Monthly Profit: §a$0",
            "",
            "§eKlicke zum Öffnen");
            
        // Financial Tools
        setItem(gui, 28, Material.EMERALD, "§a§lTransfer Money",
            "§7Geld überweisen",
            "§7• Transfer to Player",
            "§7• Transfer to Bank",
            "§7• Transfer History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLD_INGOT, "§6§lExchange Rates",
            "§7Wechselkurse",
            "§7• Coin Exchange",
            "§7• Currency Rates",
            "§7• Exchange History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.DIAMOND, "§b§lFinancial Planning",
            "§7Finanzplanung",
            "§7• Budget Planning",
            "§7• Investment Planning",
            "§7• Savings Goals",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.NETHER_STAR, "§d§lFinancial Advisor",
            "§7Finanzberater",
            "§7• Investment Advice",
            "§7• Savings Tips",
            "§7• Financial Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.EMERALD_BLOCK, "§a§lFinancial Goals",
            "§7Finanzziele",
            "§7• Savings Goals",
            "§7• Investment Goals",
            "§7• Financial Milestones",
            "",
            "§eKlicke zum Öffnen");
            
        // Financial Information
        setItem(gui, 37, Material.BOOK, "§b§lFinancial Guide",
            "§7Finanz-Anleitung",
            "§7• Economy System",
            "§7• Banking System",
            "§7• Investment System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lFinancial Statistics",
            "§7Finanz-Statistiken",
            "§7• Total Balance: §a$0",
            "§7• Daily Income: §a$0",
            "§7• Daily Expenses: §a$0",
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
