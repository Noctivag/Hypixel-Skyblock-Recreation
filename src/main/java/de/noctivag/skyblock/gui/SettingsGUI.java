package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Settings GUI - Spieler-Einstellungen
 */
public class SettingsGUI extends Menu {
    
    public SettingsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Einstellungen", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Spiel-Einstellungen
        inventory.setItem(10, createItem(Material.REDSTONE_TORCH, "§aSpiel-Einstellungen", 
            "§7Passe deine Spiel-Erfahrung an",
            "§7• Chat-Einstellungen",
            "§7• Sound-Einstellungen",
            "§7• Grafik-Einstellungen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Interface-Einstellungen
        inventory.setItem(12, createItem(Material.COMPASS, "§aInterface-Einstellungen", 
            "§7Passe das Interface an",
            "§7• GUI-Animationen",
            "§7• Tooltip-Einstellungen",
            "§7• HUD-Anzeigen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Chat-Einstellungen
        inventory.setItem(14, createItem(Material.PAPER, "§aChat-Einstellungen", 
            "§7Verwalte deine Chat-Optionen",
            "§7• Chat-Filter",
            "§7• Nachrichten-Typen",
            "§7• Benachrichtigungen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Privatsphäre-Einstellungen
        inventory.setItem(16, createItem(Material.ENDER_CHEST, "§aPrivatsphäre", 
            "§7Verwalte deine Privatsphäre",
            "§7• Freundschaftsanfragen",
            "§7• Party-Einladungen",
            "§7• Trading-Anfragen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Benachrichtigungen
        inventory.setItem(28, createItem(Material.BELL, "§aBenachrichtigungen", 
            "§7Verwalte deine Benachrichtigungen",
            "§7• Event-Benachrichtigungen",
            "§7• Freund-Online-Status",
            "§7• Auktion-Updates",
            "",
            "§eKlicke zum Öffnen"));
        
        // Hotkeys
        inventory.setItem(30, createItem(Material.LEVER, "§aHotkeys", 
            "§7Verwalte deine Tastenkürzel",
            "§7• Schnellzugriff-Menüs",
            "§7• Chat-Befehle",
            "§7• Aktionen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Zurücksetzen
        inventory.setItem(32, createItem(Material.TNT, "§cEinstellungen zurücksetzen", 
            "§7Setze alle Einstellungen",
            "§7auf Standard zurück",
            "",
            "§eKlicke zum Bestätigen"));
        
        // Schließen-Button
        inventory.setItem(49, createItem(Material.BARRIER, "§cSchließen"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10:
                player.sendMessage("§cSpiel-Einstellungen noch nicht implementiert!");
                break;
            case 12:
                player.sendMessage("§cInterface-Einstellungen noch nicht implementiert!");
                break;
            case 14:
                player.sendMessage("§cChat-Einstellungen noch nicht implementiert!");
                break;
            case 16:
                player.sendMessage("§cPrivatsphäre-Einstellungen noch nicht implementiert!");
                break;
            case 28:
                player.sendMessage("§cBenachrichtigungen noch nicht implementiert!");
                break;
            case 30:
                player.sendMessage("§cHotkeys noch nicht implementiert!");
                break;
            case 32:
                player.sendMessage("§cEinstellungen zurücksetzen noch nicht implementiert!");
                break;
            case 49:
                close();
                break;
        }
    }
}

