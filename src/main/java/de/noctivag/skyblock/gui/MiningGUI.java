package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Mining GUI - Bergbau-Features und Tools
 */
public class MiningGUI extends Menu {
    
    public MiningGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Bergbau", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Bergbau-Skill
        inventory.setItem(10, createItem(Material.DIAMOND_PICKAXE, "§aBergbau-Skill", 
            "§7Level: §a" + getMiningLevel(),
            "§7XP: §a" + getMiningXP(),
            "§7Geschürft: §a" + getMinedBlocks() + " Blöcke",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Tools
        inventory.setItem(12, createItem(Material.IRON_PICKAXE, "§aBergbau-Tools", 
            "§7Verwalte deine Bergbau-",
            "§7Tools und Upgrades",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Orte
        inventory.setItem(14, createItem(Material.COBBLESTONE, "§aBergbau-Orte", 
            "§7Verfügbare Bergbau-",
            "§7Standorte und Minen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Statistiken
        inventory.setItem(16, createItem(Material.BOOK, "§aBergbau-Statistiken", 
            "§7Zeige deine Bergbau-",
            "§7Fortschritte und Erfolge",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Belohnungen
        inventory.setItem(28, createItem(Material.CHEST, "§aBergbau-Belohnungen", 
            "§7Sammle Belohnungen für",
            "§7deine Bergbau-Fortschritte",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Quests
        inventory.setItem(30, createItem(Material.MAP, "§aBergbau-Quests", 
            "§7Verfügbare Bergbau-",
            "§7Quests und Missionen",
            "",
            "§eKlicke zum Öffnen"));
        
        // Bergbau-Upgrades
        inventory.setItem(32, createItem(Material.ANVIL, "§aBergbau-Upgrades", 
            "§7Verbessere deine",
            "§7Bergbau-Effizienz",
            "",
            "§eKlicke zum Öffnen"));
        
        // Schließen-Button
        inventory.setItem(49, createItem(Material.BARRIER, "§cSchließen"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10:
                player.sendMessage("§cBergbau-Skill-Menü noch nicht implementiert!");
                break;
            case 12:
                player.sendMessage("§cBergbau-Tools noch nicht implementiert!");
                break;
            case 14:
                player.sendMessage("§cBergbau-Orte noch nicht implementiert!");
                break;
            case 16:
                player.sendMessage("§cBergbau-Statistiken noch nicht implementiert!");
                break;
            case 28:
                player.sendMessage("§cBergbau-Belohnungen noch nicht implementiert!");
                break;
            case 30:
                player.sendMessage("§cBergbau-Quests noch nicht implementiert!");
                break;
            case 32:
                player.sendMessage("§cBergbau-Upgrades noch nicht implementiert!");
                break;
            case 49:
                close();
                break;
        }
    }
    
    // Hilfsmethoden für Bergbau-Daten (Placeholder)
    private int getMiningLevel() {
        // TODO: Implementiere echte Bergbau-Level-Logik
        return 1;
    }
    
    private int getMiningXP() {
        // TODO: Implementiere echte Bergbau-XP-Logik
        return 0;
    }
    
    private int getMinedBlocks() {
        // TODO: Implementiere echte Geschürfte-Blöcke-Logik
        return 0;
    }
}

