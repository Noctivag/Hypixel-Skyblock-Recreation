package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

/**
 * Friends GUI - Freunde und Party System
 */
public class FriendsGUI {
    
    private final Plugin plugin;
    
    public FriendsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openGUI(Player player) {
        openFriendsGUI(player);
    }
    
    public void openFriendsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lFriends & Party");
        
        // Friends Management
        setItem(gui, 10, Material.PLAYER_HEAD, "§a§lMy Friends",
            "§7Deine Freunde",
            "§7• Online Friends: §a0",
            "§7• Total Friends: §a0",
            "§7• Friend Requests: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lAdd Friend",
            "§7Freund hinzufügen",
            "§7• Spieler suchen",
            "§7• Freundschaftsanfrage senden",
            "§7• Freundschaftsanfrage annehmen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.REDSTONE, "§c§lRemove Friend",
            "§7Freund entfernen",
            "§7• Freundschaft beenden",
            "§7• Freund blockieren",
            "§7• Freundschaftsanfrage ablehnen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§b§lFriend Requests",
            "§7Freundschaftsanfragen",
            "§7• Eingegangene Anfragen",
            "§7• Gesendete Anfragen",
            "§7• Anfragen verwalten",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§e§lFind Friends",
            "§7Freunde finden",
            "§7• Online Spieler",
            "§7• Empfohlene Freunde",
            "§7• Gemeinsame Freunde",
            "",
            "§eKlicke zum Öffnen");
            
        // Party System
        setItem(gui, 19, Material.CAKE, "§d§lMy Party",
            "§7Deine Party",
            "§7• Party Members: §a0/4",
            "§7• Party Leader: §a" + player.getName(),
            "§7• Party Status: §aOffline",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.FIREWORK_ROCKET, "§6§lCreate Party",
            "§7Party erstellen",
            "§7• Neue Party gründen",
            "§7• Party-Einstellungen",
            "§7• Party-Regeln",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ARROW, "§a§lJoin Party",
            "§7Party beitreten",
            "§7• Party-Einladungen",
            "§7• Öffentliche Partys",
            "§7• Party beitreten",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BARRIER, "§c§lLeave Party",
            "§7Party verlassen",
            "§7• Party verlassen",
            "§7• Party auflösen",
            "§7• Party-Mitglieder entfernen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.ENCHANTED_BOOK, "§b§lParty Settings",
            "§7Party-Einstellungen",
            "§7• Party-Regeln",
            "§7• Party-Berechtigungen",
            "§7• Party-Chat",
            "",
            "§eKlicke zum Öffnen");
            
        // Social Features
        setItem(gui, 28, Material.WRITABLE_BOOK, "§e§lSocial Feed",
            "§7Sozialer Feed",
            "§7• Freundes-Aktivitäten",
            "§7• Party-Updates",
            "§7• Soziale Nachrichten",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.CLOCK, "§6§lRecent Activity",
            "§7Letzte Aktivitäten",
            "§7• Freundes-Aktivitäten",
            "§7• Party-Aktivitäten",
            "§7• Soziale Aktivitäten",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.EMERALD, "§a§lSocial Rewards",
            "§7Soziale Belohnungen",
            "§7• Freundes-Belohnungen",
            "§7• Party-Belohnungen",
            "§7• Soziale Meilensteine",
            "",
            "§eKlicke zum Öffnen");
            
        // Social Settings
        setItem(gui, 37, Material.REDSTONE, "§c§lPrivacy Settings",
            "§7Datenschutz-Einstellungen",
            "§7• Freundesliste sichtbar",
            "§7• Online-Status sichtbar",
            "§7• Party-Einladungen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.BOOK, "§b§lSocial Guide",
            "§7Sozialer Leitfaden",
            "§7• Freundes-System",
            "§7• Party-System",
            "§7• Soziale Features",
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
    
    private void setPlayerHead(Inventory gui, int slot, String playerName, String name, String... lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            meta.setOwner(playerName);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
