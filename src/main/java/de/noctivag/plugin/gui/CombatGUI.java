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
 * Combat GUI - Kampf und Events
 */
public class CombatGUI {
    
    private final Plugin plugin;
    
    public CombatGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openCombatGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lCombat & Events");
        
        // Combat Features
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lPvP Arena",
            "§7Spieler gegen Spieler",
            "§7• 1v1 Duels",
            "§7• Team Battles",
            "§7• Tournament Mode",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.ZOMBIE_HEAD, "§4§lMob Arena",
            "§7Kampf gegen Mobs",
            "§7• Wave Survival",
            "§7• Boss Fights",
            "§7• Mob Challenges",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.NETHER_STAR, "§d§lBoss Battles",
            "§7Boss-Kämpfe",
            "§7• World Bosses",
            "§7• Dungeon Bosses",
            "§7• Raid Bosses",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.DRAGON_EGG, "§5§lDragon Fights",
            "§7Drachen-Kämpfe",
            "§7• Ender Dragon",
            "§7• Custom Dragons",
            "§7• Dragon Raids",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.WITHER_SKELETON_SKULL, "§8§lWither Fights",
            "§7Wither-Kämpfe",
            "§7• Wither Boss",
            "§7• Wither Army",
            "§7• Wither Raids",
            "",
            "§eKlicke zum Öffnen");
            
        // Events
        setItem(gui, 19, Material.FIREWORK_ROCKET, "§6§lSpecial Events",
            "§7Besondere Events",
            "§7• Seasonal Events",
            "§7• Community Events",
            "§7• Tournament Events",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.CLOCK, "§e§lDaily Events",
            "§7Tägliche Events",
            "§7• Daily Challenges",
            "§7• Daily Bosses",
            "§7• Daily Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CLOCK, "§a§lWeekly Events",
            "§7Wöchentliche Events",
            "§7• Weekly Challenges",
            "§7• Weekly Bosses",
            "§7• Weekly Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.END_CRYSTAL, "§9§lMonthly Events",
            "§7Monatliche Events",
            "§7• Monthly Challenges",
            "§7• Monthly Bosses",
            "§7• Monthly Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§d§lEvent Calendar",
            "§7Event-Kalender",
            "§7• Upcoming Events",
            "§7• Event History",
            "§7• Event Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        // Combat Stats
        setItem(gui, 28, Material.BOOK, "§b§lCombat Stats",
            "§7Deine Kampf-Statistiken",
            "§7• PvP Wins: §a0",
            "§7• Mobs Killed: §a0",
            "§7• Bosses Defeated: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.EMERALD, "§a§lCombat Rewards",
            "§7Kampf-Belohnungen",
            "§7• Victory Rewards",
            "§7• Achievement Rewards",
            "§7• Special Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.ENCHANTED_BOOK, "§d§lCombat Upgrades",
            "§7Kampf-Verbesserungen",
            "§7• Weapon Upgrades",
            "§7• Armor Upgrades",
            "§7• Skill Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        // Combat Settings
        setItem(gui, 37, Material.REDSTONE, "§c§lCombat Settings",
            "§7Kampf-Einstellungen",
            "§7• PvP Toggle",
            "§7• Damage Settings",
            "§7• Combat Logging",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.COMPASS, "§e§lCombat Guide",
            "§7Kampf-Anleitung",
            "§7• Combat Tips",
            "§7• Strategy Guide",
            "§7• Equipment Guide",
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
