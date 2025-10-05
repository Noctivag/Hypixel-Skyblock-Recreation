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
 * Bank GUI - Bank und -Verwaltung
 */
public class BankGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public BankGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void open(Player player) {
        openBankGUI(player);
    }
    
    public void openBankGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lBank"));
        
        // Bank Features
        setItem(gui, 10, Material.GOLD_INGOT, "§6§lBank Account",
            "§7Bank-Konto",
            "§7• Current Balance: §a$0",
            "§7• Interest Rate: §a5%",
            "§7• Next Interest: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lSavings Account",
            "§7Sparkonto",
            "§7• Savings: §a$0",
            "§7• Interest Rate: §a10%",
            "§7• Next Interest: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lInvestment Account",
            "§7Anlagekonto",
            "§7• Investments: §a$0",
            "§7• Return Rate: §a15%",
            "§7• Next Return: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EMERALD_BLOCK, "§a§lPremium Account",
            "§7Premium-Konto",
            "§7• Premium Balance: §a$0",
            "§7• Bonus Rate: §a20%",
            "§7• Next Bonus: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial Account",
            "§7Besonderes Konto",
            "§7• Special Balance: §a$0",
            "§7• Special Rate: §a25%",
            "§7• Next Special: §a24h",
            "",
            "§eKlicke zum Öffnen");
            
        // Bank Services
        setItem(gui, 19, Material.EMERALD, "§a§lDeposit",
            "§7Einzahlung",
            "§7• Deposit Money",
            "§7• Deposit Items",
            "§7• Deposit History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.GOLD_INGOT, "§6§lWithdraw",
            "§7Auszahlung",
            "§7• Withdraw Money",
            "§7• Withdraw Items",
            "§7• Withdraw History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.DIAMOND, "§b§lTransfer",
            "§7Überweisung",
            "§7• Transfer Money",
            "§7• Transfer Items",
            "§7• Transfer History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.ENCHANTED_BOOK, "§d§lLoans",
            "§7Kredite",
            "§7• Apply for Loan",
            "§7• Loan Management",
            "§7• Loan History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§5§lInvestments",
            "§7Investitionen",
            "§7• Investment Options",
            "§7• Investment Management",
            "§7• Investment History",
            "",
            "§eKlicke zum Öffnen");
            
        // Bank Tools
        setItem(gui, 28, Material.CLOCK, "§e§lTransaction History",
            "§7Transaktions-Verlauf",
            "§7• Recent Transactions",
            "§7• Transaction Details",
            "§7• Transaction Search",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.PAPER, "§b§lFinancial Charts",
            "§7Finanz-Charts",
            "§7• Balance History",
            "§7• Income Charts",
            "§7• Expense Charts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lBank Alerts",
            "§7Bank-Alerts",
            "§7• Balance Alerts",
            "§7• Transaction Alerts",
            "§7• Interest Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lBank Calculator",
            "§7Bank-Rechner",
            "§7• Interest Calculator",
            "§7• Loan Calculator",
            "§7• Investment Calculator",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lBank Guide",
            "§7Bank-Anleitung",
            "§7• Banking Basics",
            "§7• Banking Tips",
            "§7• Banking Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Bank Information
        setItem(gui, 37, Material.BOOK, "§b§lBank Information",
            "§7Bank-Informationen",
            "§7• Bank Rules",
            "§7• Bank Terms",
            "§7• Bank Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lBank Statistics",
            "§7Bank-Statistiken",
            "§7• Total Balance: §a$0",
            "§7• Daily Interest: §a$0",
            "§7• Bank Level: §a0",
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
