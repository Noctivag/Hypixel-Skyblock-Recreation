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
 * Pet Settings GUI - Haustier-Einstellungen und -Konfiguration
 */
public class PetSettingsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public PetSettingsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openPetSettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lPet Settings"));
        
        // General Settings
        setItem(gui, 10, Material.REDSTONE, "§c§lGeneral Settings",
            "§7Allgemeine Einstellungen",
            "§7• Pet Display",
            "§7• Pet Visibility",
            "§7• Pet Behavior",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lPrivacy Settings",
            "§7Datenschutz-Einstellungen",
            "§7• Pet Visibility",
            "§7• Pet Information",
            "§7• Pet Privacy",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BELL, "§e§lNotification Settings",
            "§7Benachrichtigungs-Einstellungen",
            "§7• Pet Notifications",
            "§7• Pet Alerts",
            "§7• Pet Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.WRITABLE_BOOK, "§b§lChat Settings",
            "§7Chat-Einstellungen",
            "§7• Pet Chat",
            "§7• Pet Messages",
            "§7• Pet Communication",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§d§lNavigation Settings",
            "§7Navigations-Einstellungen",
            "§7• Pet Navigation",
            "§7• Pet Movement",
            "§7• Pet AI",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Behavior
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Settings",
            "§7Kampf-Einstellungen",
            "§7• Pet Combat",
            "§7• Pet Aggression",
            "§7• Pet Defense",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EXPERIENCE_BOTTLE, "§e§lLeveling Settings",
            "§7Leveling-Einstellungen",
            "§7• Auto Leveling",
            "§7• Level Notifications",
            "§7• XP Display",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.EMERALD, "§a§lCare Settings",
            "§7Pflege-Einstellungen",
            "§7• Auto Care",
            "§7• Care Notifications",
            "§7• Care Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.IRON_GOLEM_SPAWN_EGG, "§f§lMinion Settings",
            "§7Minion-Einstellungen",
            "§7• Pet Minions",
            "§7• Minion Behavior",
            "§7• Minion Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BONE, "§6§lPet Settings",
            "§7Haustier-Einstellungen",
            "§7• Pet Behavior",
            "§7• Pet AI",
            "§7• Pet Preferences",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Settings
        setItem(gui, 28, Material.CLOCK, "§e§lPerformance Settings",
            "§7Performance-Einstellungen",
            "§7• Pet Performance",
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
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
