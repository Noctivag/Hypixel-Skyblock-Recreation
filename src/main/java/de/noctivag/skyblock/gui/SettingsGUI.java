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
 * Settings GUI - Einstellungen und Konfiguration
 */
public class SettingsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SettingsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openGUI(Player player) {
        openSettingsGUI(player);
    }
    
    public void openSettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSettings"));
        
        // General Settings
        setItem(gui, 10, Material.REDSTONE, "§c§lGeneral Settings",
            "§7Allgemeine Einstellungen",
            "§7• Language Settings",
            "§7• Display Settings",
            "§7• Sound Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lPrivacy Settings",
            "§7Datenschutz-Einstellungen",
            "§7• Friend Visibility",
            "§7• Online Status",
            "§7• Message Privacy",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BELL, "§e§lNotification Settings",
            "§7Benachrichtigungs-Einstellungen",
            "§7• Message Notifications",
            "§7• Sound Notifications",
            "§7• Visual Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.WRITABLE_BOOK, "§b§lChat Settings",
            "§7Chat-Einstellungen",
            "§7• Chat Format",
            "§7• Chat Colors",
            "§7• Chat Filters",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§d§lNavigation Settings",
            "§7Navigations-Einstellungen",
            "§7• GUI Navigation",
            "§7• Menu Settings",
            "§7• Hotkey Settings",
            "",
            "§eKlicke zum Öffnen");
            
        // Gameplay Settings
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Settings",
            "§7Kampf-Einstellungen",
            "§7• PvP Toggle",
            "§7• Damage Settings",
            "§7• Combat Logging",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EXPERIENCE_BOTTLE, "§e§lSkill Settings",
            "§7Skill-Einstellungen",
            "§7• Skill Display",
            "§7• XP Notifications",
            "§7• Skill Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.EMERALD, "§a§lEconomy Settings",
            "§7Wirtschafts-Einstellungen",
            "§7• Balance Display",
            "§7• Transaction Notifications",
            "§7• Market Alerts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.IRON_GOLEM_SPAWN_EGG, "§f§lMinion Settings",
            "§7Minion-Einstellungen",
            "§7• Minion Notifications",
            "§7• Collection Alerts",
            "§7• Minion Display",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BONE, "§6§lPet Settings",
            "§7Pet-Einstellungen",
            "§7• Pet Display",
            "§7• Pet Notifications",
            "§7• Pet Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        // Performance Settings
        setItem(gui, 28, Material.CLOCK, "§e§lPerformance Settings",
            "§7Performance-Einstellungen",
            "§7• GUI Performance",
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
            
        // Settings Management
        setItem(gui, 37, Material.EMERALD, "§a§lReset Settings",
            "§7Einstellungen zurücksetzen",
            "§7• Reset to Default",
            "§7• Reset Specific Settings",
            "§7• Backup Settings",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSettings History",
            "§7Einstellungs-Verlauf",
            "§7• Recent Changes",
            "§7• Settings Backup",
            "§7• Restore Settings",
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
            meta.lore(Arrays.asList(lore).stream()
                .map(line -> Component.text(line))
                .collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
