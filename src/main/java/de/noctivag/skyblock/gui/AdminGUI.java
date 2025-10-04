package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Admin GUI - Admin-Tools und Verwaltung
 */
public class AdminGUI {
    
    private final SkyblockPlugin plugin;
    
    public AdminGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openAdminGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lAdmin Tools");
        
        // Player Management
        setItem(gui, 10, Material.PLAYER_HEAD, "§a§lPlayer Management",
            "§7Spieler-Verwaltung",
            "§7• Player Info",
            "§7• Player Teleportation",
            "§7• Player Inventory",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND_SWORD, "§c§lModeration Tools",
            "§7Moderations-Tools",
            "§7• Kick/Ban Players",
            "§7• Mute/Unmute Players",
            "§7• Warning System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lEconomy Management",
            "§7Wirtschafts-Verwaltung",
            "§7• Player Balances",
            "§7• Economy Settings",
            "§7• Market Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EXPERIENCE_BOTTLE, "§b§lSkill Management",
            "§7Skill-Verwaltung",
            "§7• Player Skills",
            "§7• Skill Settings",
            "§7• XP Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinion Management",
            "§7Minion-Verwaltung",
            "§7• Player Minions",
            "§7• Minion Settings",
            "§7• Minion Spawning",
            "",
            "§eKlicke zum Öffnen");
            
        // Server Management
        setItem(gui, 19, Material.COMMAND_BLOCK, "§d§lCommand Management",
            "§7Befehl-Verwaltung",
            "§7• Command Execution",
            "§7• Command History",
            "§7• Command Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.REDSTONE, "§c§lServer Settings",
            "§7Server-Einstellungen",
            "§7• Server Configuration",
            "§7• Plugin Settings",
            "§7• Performance Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CLOCK, "§e§lEvent Management",
            "§7Event-Verwaltung",
            "§7• Server Events",
            "§7• Event Scheduling",
            "§7• Event Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BOOK, "§b§lLog Management",
            "§7Log-Verwaltung",
            "§7• Server Logs",
            "§7• Player Logs",
            "§7• Error Logs",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BARRIER, "§c§lMaintenance Mode",
            "§7Wartungsmodus",
            "§7• Server Maintenance",
            "§7• Maintenance Settings",
            "§7• Maintenance Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        // Database Management
        setItem(gui, 28, Material.CHEST, "§6§lDatabase Management",
            "§7Datenbank-Verwaltung",
            "§7• Database Status",
            "§7• Database Backup",
            "§7• Database Restore",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.EMERALD, "§a§lMulti-Server Management",
            "§7Multi-Server-Verwaltung",
            "§7• Server Status",
            "§7• Cross-Server Data",
            "§7• Server Synchronization",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§d§lAdvanced Tools",
            "§7Erweiterte Tools",
            "§7• Advanced Commands",
            "§7• System Monitoring",
            "§7• Performance Analysis",
            "",
            "§eKlicke zum Öffnen");
            
        // Admin Information
        setItem(gui, 37, Material.BOOK, "§b§lAdmin Guide",
            "§7Admin-Anleitung",
            "§7• Admin Commands",
            "§7• Admin Tools",
            "§7• Admin Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lAdmin Statistics",
            "§7Admin-Statistiken",
            "§7• Server Status",
            "§7• Player Count",
            "§7• System Performance",
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
