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
 * Skyblock Settings GUI - Skyblock-Einstellungen und -Konfiguration
 */
public class SkyblockSettingsGUI {
    
    private final Plugin plugin;
    
    public SkyblockSettingsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openSkyblockSettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lSkyblock Settings");
        
        // General Settings
        setItem(gui, 10, Material.REDSTONE, "§c§lGeneral Settings",
            "§7Allgemeine Einstellungen",
            "§7• Skyblock Display",
            "§7• Skyblock Visibility",
            "§7• Skyblock Behavior",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lPrivacy Settings",
            "§7Datenschutz-Einstellungen",
            "§7• Skyblock Visibility",
            "§7• Skyblock Information",
            "§7• Skyblock Privacy",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BELL, "§e§lNotification Settings",
            "§7Benachrichtigungs-Einstellungen",
            "§7• Skyblock Notifications",
            "§7• Skyblock Alerts",
            "§7• Skyblock Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.WRITABLE_BOOK, "§b§lChat Settings",
            "§7Chat-Einstellungen",
            "§7• Skyblock Chat",
            "§7• Skyblock Messages",
            "§7• Skyblock Communication",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§d§lNavigation Settings",
            "§7Navigations-Einstellungen",
            "§7• Skyblock Navigation",
            "§7• Skyblock Movement",
            "§7• Skyblock AI",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Features
        setItem(gui, 19, Material.GRASS_BLOCK, "§a§lIsland Settings",
            "§7Insel-Einstellungen",
            "§7• Island Display",
            "§7• Island Management",
            "§7• Island Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinion Settings",
            "§7Minion-Einstellungen",
            "§7• Minion Display",
            "§7• Minion Management",
            "§7• Minion Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.EXPERIENCE_BOTTLE, "§b§lSkill Settings",
            "§7Skill-Einstellungen",
            "§7• Skill Display",
            "§7• Skill Management",
            "§7• Skill Leveling",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BOOK, "§d§lCollection Settings",
            "§7Sammlungs-Einstellungen",
            "§7• Collection Display",
            "§7• Collection Management",
            "§7• Collection Progress",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BONE, "§6§lPet Settings",
            "§7Haustier-Einstellungen",
            "§7• Pet Display",
            "§7• Pet Management",
            "§7• Pet Leveling",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Settings
        setItem(gui, 28, Material.CLOCK, "§e§lPerformance Settings",
            "§7Performance-Einstellungen",
            "§7• Skyblock Performance",
            "§7• Animation Settings",
            "§7• Effect Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.REDSTONE_BLOCK, "§c§lAdvanced Settings",
            "§7Erweiterte Einstellungen",
            "§7• Debug Mode",
            "§7• Verbose Logging",
            "§7• System Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BOOK, "§b§lHelp & Support",
            "§7Hilfe & Support",
            "§7• Settings Guide",
            "§7• FAQ",
            "§7• Contact Support",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.EMERALD, "§a§lReset Settings",
            "§7Einstellungen zurücksetzen",
            "§7• Reset to Default",
            "§7• Reset Specific Settings",
            "§7• Backup Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.CLOCK, "§e§lSettings History",
            "§7Einstellungs-Verlauf",
            "§7• Recent Changes",
            "§7• Settings Backup",
            "§7• Restore Settings",
            "",
            "§eKlicke zum Öffnen");
            
        // Settings Management
        setItem(gui, 37, Material.BOOK, "§b§lSettings Guide",
            "§7Einstellungs-Anleitung",
            "§7• Settings Basics",
            "§7• Settings Tips",
            "§7• Settings Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSettings Statistics",
            "§7Einstellungs-Statistiken",
            "§7• Active Settings: §a0",
            "§7• Settings Changes: §a0",
            "§7• Settings Usage: §a0%",
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
