package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Party GUI - Party-System Verwaltung
 */
public class PartyGUI extends Menu {
    
    public PartyGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Party", 54);
    }
    
    public PartyGUI(SkyblockPlugin plugin) {
        super(plugin, null, "§8Party", 54);
    }
    
    public void openGUI(Player player) {
        open();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Party erstellen
        inventory.setItem(20, createItem(Material.CAKE, "§aParty erstellen", 
            "§7Erstelle eine neue Party",
            "§7und lade Freunde ein",
            "",
            "§eKlicke zum Erstellen"));
        
        // Party beitreten
        inventory.setItem(22, createItem(Material.EMERALD, "§aParty beitreten", 
            "§7Tritt einer bestehenden",
            "§7Party bei",
            "",
            "§eKlicke zum Beitreten"));
        
        // Meine Party
        if (isInParty()) {
            inventory.setItem(24, createItem(Material.PLAYER_HEAD, "§aMeine Party", 
                "§7Verwalte deine aktuelle Party",
                "§7Mitglieder: §a" + getPartyMemberCount(),
                "§7Leader: §a" + getPartyLeader(),
                "",
                "§eKlicke zum Verwalten"));
        } else {
            inventory.setItem(24, createItem(Material.BARRIER, "§cKeine Party", 
                "§7Du bist in keiner Party",
                "",
                "§eErstelle oder trete einer Party bei"));
        }
        
        // Party-Einstellungen
        inventory.setItem(31, createItem(Material.REDSTONE, "§aParty-Einstellungen", 
            "§7Verwalte deine Party-",
            "§7Einstellungen und Berechtigungen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Party verlassen
        if (isInParty()) {
            inventory.setItem(33, createItem(Material.TNT, "§cParty verlassen", 
                "§7Verlasse deine aktuelle Party",
                "",
                "§eKlicke zum Bestätigen"));
        }
        
        // Schließen-Button
        inventory.setItem(49, createItem(Material.BARRIER, "§cSchließen"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 20:
                createParty();
                break;
            case 22:
                player.sendMessage("§cParty beitreten noch nicht implementiert!");
                break;
            case 24:
                if (isInParty()) {
                    player.sendMessage("§cParty-Verwaltung noch nicht implementiert!");
                }
                break;
            case 31:
                player.sendMessage("§cParty-Einstellungen noch nicht implementiert!");
                break;
            case 33:
                if (isInParty()) {
                    leaveParty();
                }
                break;
            case 49:
                close();
                break;
        }
    }
    
    private void createParty() {
        // TODO: Implementiere echte Party-Erstellung
        player.sendMessage("§aParty erfolgreich erstellt!");
        setupItems(); // Aktualisiere das Menü
    }
    
    private void leaveParty() {
        // TODO: Implementiere echte Party-Verlassen-Logik
        player.sendMessage("§cDu hast die Party verlassen!");
        setupItems(); // Aktualisiere das Menü
    }
    
    // Hilfsmethoden für Party-Daten (Placeholder)
    private boolean isInParty() {
        // TODO: Implementiere echte Party-Status-Logik
        return false;
    }
    
    private int getPartyMemberCount() {
        // TODO: Implementiere echte Party-Mitglieder-Logik
        return 1;
    }
    
    private String getPartyLeader() {
        // TODO: Implementiere echte Party-Leader-Logik
        return "Niemand";
    }
}

