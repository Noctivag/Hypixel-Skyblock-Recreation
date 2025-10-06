package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Friends GUI - Verwaltung von Freunden und Party-System
 */
public class FriendsGUI extends Menu {
    
    public FriendsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Freunde", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Freunde-Liste
        inventory.setItem(22, createItem(Material.PLAYER_HEAD, "§aMeine Freunde", 
            "§7Verwalte deine Freunde",
            "§7Aktuelle Freunde: §a" + getFriendCount(),
            "",
            "§eKlicke zum Öffnen"));
        
        // Freund hinzufügen
        inventory.setItem(20, createItem(Material.EMERALD, "§aFreund hinzufügen", 
            "§7Füge einen neuen Freund hinzu",
            "",
            "§eKlicke zum Öffnen"));
        
        // Freundschaftsanfragen
        inventory.setItem(24, createItem(Material.BOOK, "§aFreundschaftsanfragen", 
            "§7Verwalte eingehende Anfragen",
            "§7Ausstehend: §a" + getPendingRequests(),
            "",
            "§eKlicke zum Öffnen"));
        
        // Party-System
        inventory.setItem(31, createItem(Material.CAKE, "§aParty", 
            "§7Verwalte deine Party",
            "§7Mitglieder: §a" + getPartyMembers(),
            "",
            "§eKlicke zum Öffnen"));
        
        // Schließen-Button
        inventory.setItem(49, createItem(Material.BARRIER, "§cSchließen"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 20:
                player.sendMessage("§cFreund hinzufügen noch nicht implementiert!");
                break;
            case 22:
                player.sendMessage("§cFreunde-Liste noch nicht implementiert!");
                break;
            case 24:
                player.sendMessage("§cFreundschaftsanfragen noch nicht implementiert!");
                break;
            case 31:
                player.sendMessage("§cParty-System noch nicht implementiert!");
                break;
            case 49:
                close();
                break;
        }
    }
    
    // Hilfsmethoden für Freund-Daten (Placeholder)
    private int getFriendCount() {
        // TODO: Implementiere echte Freund-Anzahl-Logik
        return 0;
    }
    
    private int getPendingRequests() {
        // TODO: Implementiere echte Anfragen-Logik
        return 0;
    }
    
    private int getPartyMembers() {
        // TODO: Implementiere echte Party-Mitglieder-Logik
        return 1;
    }
    
    public void openGUI(Player player) {
        open();
    }
}

