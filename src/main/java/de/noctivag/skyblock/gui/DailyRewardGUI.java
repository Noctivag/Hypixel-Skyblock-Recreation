package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Daily Reward GUI - Tägliche Belohnungen
 */
public class DailyRewardGUI extends Menu {
    
    public DailyRewardGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Tägliche Belohnungen", 54);
    }
    
    public DailyRewardGUI(SkyblockPlugin plugin) {
        super(plugin, null, "§8Tägliche Belohnungen", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Heutige Belohnung
        if (canClaimToday()) {
            inventory.setItem(22, createItem(Material.GOLD_INGOT, "§6Heutige Belohnung", 
                "§7Tag " + getCurrentDay() + " von 30",
                "§7Belohnung: §a" + getTodaysReward(),
                "",
                "§eKlicke zum Abholen"));
        } else {
            inventory.setItem(22, createItem(Material.BARRIER, "§cBereits abgeholt", 
                "§7Du hast deine heutige Belohnung",
                "§7bereits abgeholt!",
                "",
                "§7Nächste Belohnung in: §a" + getTimeUntilNextReward()));
        }
        
        // Belohnungs-Kalender
        inventory.setItem(20, createItem(Material.CLOCK, "§aBelohnungs-Kalender", 
            "§7Zeige alle verfügbaren",
            "§7täglichen Belohnungen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Streak-Informationen
        inventory.setItem(24, createItem(Material.FIREWORK_ROCKET, "§aStreak-Informationen", 
            "§7Aktueller Streak: §a" + getCurrentStreak() + " Tage",
            "§7Bester Streak: §a" + getBestStreak() + " Tage",
            "§7Streak-Belohnungen: §a" + getStreakRewards(),
            "",
            "§eKlicke zum Öffnen"));
        
        // Belohnungs-Historie
        inventory.setItem(31, createItem(Material.BOOK, "§aBelohnungs-Historie", 
            "§7Zeige deine letzten",
            "§7abgeholten Belohnungen",
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
                player.sendMessage("§cBelohnungs-Kalender noch nicht implementiert!");
                break;
            case 22:
                if (canClaimToday()) {
                    claimTodaysReward();
                    player.sendMessage("§aBelohnung erfolgreich abgeholt!");
                    setupItems(); // Aktualisiere das Menü
                } else {
                    player.sendMessage("§cDu hast deine heutige Belohnung bereits abgeholt!");
                }
                break;
            case 24:
                player.sendMessage("§cStreak-Informationen noch nicht implementiert!");
                break;
            case 31:
                player.sendMessage("§cBelohnungs-Historie noch nicht implementiert!");
                break;
            case 49:
                close();
                break;
        }
    }
    
    // Hilfsmethoden für Daily Reward Daten (Placeholder)
    private boolean canClaimToday() {
        // TODO: Implementiere echte Claim-Logik
        return true;
    }
    
    private int getCurrentDay() {
        // TODO: Implementiere echte Tag-Logik
        return 1;
    }
    
    private String getTodaysReward() {
        // TODO: Implementiere echte Belohnungs-Logik
        return "1000 Münzen";
    }
    
    private String getTimeUntilNextReward() {
        // TODO: Implementiere echte Zeit-Logik
        return "23h 45m";
    }
    
    private int getCurrentStreak() {
        // TODO: Implementiere echte Streak-Logik
        return 1;
    }
    
    private int getBestStreak() {
        // TODO: Implementiere echte Best-Streak-Logik
        return 1;
    }
    
    private String getStreakRewards() {
        // TODO: Implementiere echte Streak-Belohnungs-Logik
        return "Verfügbar";
    }
    
    private void claimTodaysReward() {
        // TODO: Implementiere echte Claim-Logik
        // Hier sollten die Belohnungen an den Spieler vergeben werden
    }
}

