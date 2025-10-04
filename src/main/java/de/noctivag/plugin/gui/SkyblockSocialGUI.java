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
 * Skyblock Social GUI - Skyblock-Soziales und -Gemeinschaft
 */
public class SkyblockSocialGUI {
    
    private final Plugin plugin;
    
    public SkyblockSocialGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openSkyblockSocialGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lSkyblock Social");
        
        // Social Features
        setItem(gui, 10, Material.PLAYER_HEAD, "§a§lSkyblock Friends",
            "§7Skyblock-Freunde",
            "§7• Skyblock Friends: §a0",
            "§7• Friend Requests: §a0",
            "§7• Friend Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.CAKE, "§d§lSkyblock Parties",
            "§7Skyblock-Partys",
            "§7• Skyblock Parties: §a0",
            "§7• Party Invitations: §a0",
            "§7• Party Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lSkyblock Trading",
            "§7Skyblock-Handel",
            "§7• Skyblock Trading",
            "§7• Skyblock Exchange",
            "§7• Skyblock Gifts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§b§lSkyblock Chat",
            "§7Skyblock-Chat",
            "§7• Skyblock Messages",
            "§7• Skyblock Communication",
            "§7• Skyblock Social",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§e§lSkyblock Events",
            "§7Skyblock-Events",
            "§7• Skyblock Events",
            "§7• Skyblock Competitions",
            "§7• Skyblock Activities",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Community
        setItem(gui, 19, Material.DIAMOND, "§b§lSkyblock Community",
            "§7Skyblock-Gemeinschaft",
            "§7• Community Hub",
            "§7• Community Events",
            "§7• Community Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lSkyblock Guilds",
            "§7Skyblock-Gilden",
            "§7• Skyblock Guilds",
            "§7• Guild Management",
            "§7• Guild Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lSkyblock Teams",
            "§7Skyblock-Teams",
            "§7• Skyblock Teams",
            "§7• Team Management",
            "§7• Team Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lSkyblock Alliances",
            "§7Skyblock-Allianzen",
            "§7• Skyblock Alliances",
            "§7• Alliance Management",
            "§7• Alliance Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lSkyblock Clans",
            "§7Skyblock-Clans",
            "§7• Skyblock Clans",
            "§7• Clan Management",
            "§7• Clan Activities",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Social Tools
        setItem(gui, 28, Material.CLOCK, "§e§lSkyblock Social History",
            "§7Skyblock-Sozial-Verlauf",
            "§7• Social Activities",
            "§7• Social History",
            "§7• Social Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BELL, "§a§lSkyblock Social Notifications",
            "§7Skyblock-Sozial-Benachrichtigungen",
            "§7• Social Alerts",
            "§7• Social Notifications",
            "§7• Social Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.PAPER, "§b§lSkyblock Social Statistics",
            "§7Skyblock-Sozial-Statistiken",
            "§7• Social Activity",
            "§7• Social Engagement",
            "§7• Social Performance",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lSkyblock Social Discovery",
            "§7Skyblock-Sozial-Entdeckung",
            "§7• Find Skyblock Friends",
            "§7• Discover Skyblock Events",
            "§7• Explore Skyblock Community",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lSkyblock Social Guide",
            "§7Skyblock-Sozial-Anleitung",
            "§7• Social Basics",
            "§7• Social Tips",
            "§7• Social Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Social Information
        setItem(gui, 37, Material.BOOK, "§b§lSkyblock Social Information",
            "§7Skyblock-Sozial-Informationen",
            "§7• Social Rules",
            "§7• Social Terms",
            "§7• Social Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSkyblock Social Statistics",
            "§7Skyblock-Sozial-Statistiken",
            "§7• Social Score: §a0",
            "§7• Social Level: §a0",
            "§7• Social Rank: §a0",
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
