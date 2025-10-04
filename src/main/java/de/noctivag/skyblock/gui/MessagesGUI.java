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
 * Messages GUI - Nachrichten und Kommunikation
 */
public class MessagesGUI {
    
    private final SkyblockPlugin plugin;
    
    public MessagesGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openMessagesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lMessages");
        
        // Message Types
        setItem(gui, 10, Material.WRITABLE_BOOK, "§a§lSend Message",
            "§7Nachricht senden",
            "§7• Private Message",
            "§7• Message to Player",
            "§7• Message History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.BOOK, "§b§lInbox",
            "§7Posteingang",
            "§7• Unread Messages: §a0",
            "§7• Total Messages: §a0",
            "§7• Message Archive",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.PAPER, "§e§lSent Messages",
            "§7Gesendete Nachrichten",
            "§7• Sent Messages: §a0",
            "§7• Message History",
            "§7• Message Status",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.ENCHANTED_BOOK, "§d§lMessage Templates",
            "§7Nachrichten-Vorlagen",
            "§7• Quick Messages",
            "§7• Custom Templates",
            "§7• Template Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.REDSTONE, "§c§lBlocked Players",
            "§7Blockierte Spieler",
            "§7• Blocked Players: §a0",
            "§7• Block Management",
            "§7• Unblock Players",
            "",
            "§eKlicke zum Öffnen");
            
        // Chat Features
        setItem(gui, 19, Material.ANVIL, "§6§lChat Settings",
            "§7Chat-Einstellungen",
            "§7• Chat Format",
            "§7• Chat Colors",
            "§7• Chat Filters",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.NAME_TAG, "§a§lChat Channels",
            "§7Chat-Kanäle",
            "§7• Global Chat",
            "§7• Local Chat",
            "§7• Private Chat",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.BELL, "§e§lNotifications",
            "§7Benachrichtigungen",
            "§7• Message Notifications",
            "§7• Sound Notifications",
            "§7• Visual Notifications",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.CLOCK, "§6§lMessage History",
            "§7Nachrichten-Verlauf",
            "§7• Recent Messages",
            "§7• Message Archive",
            "§7• Search Messages",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.EMERALD, "§a§lMessage Rewards",
            "§7Nachrichten-Belohnungen",
            "§7• Message Streaks",
            "§7• Activity Rewards",
            "§7• Social Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        // Communication Tools
        setItem(gui, 28, Material.COMPASS, "§b§lFind Players",
            "§7Spieler finden",
            "§7• Online Players",
            "§7• Player Search",
            "§7• Player Status",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.FIREWORK_ROCKET, "§d§lAnnouncements",
            "§7Ankündigungen",
            "§7• Server Announcements",
            "§7• Event Announcements",
            "§7• Personal Announcements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BOOK, "§e§lMessage Guide",
            "§7Nachrichten-Anleitung",
            "§7• Message System",
            "§7• Chat Commands",
            "§7• Communication Tips",
            "",
            "§eKlicke zum Öffnen");
            
        // Message Management
        setItem(gui, 37, Material.REDSTONE, "§c§lMessage Settings",
            "§7Nachrichten-Einstellungen",
            "§7• Privacy Settings",
            "§7• Message Filters",
            "§7• Auto-Reply",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lMessage Statistics",
            "§7Nachrichten-Statistiken",
            "§7• Messages Sent: §a0",
            "§7• Messages Received: §a0",
            "§7• Active Conversations: §a0",
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
