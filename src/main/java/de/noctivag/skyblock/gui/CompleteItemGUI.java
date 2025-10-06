package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * CompleteItemGUI - Displays all items
 */
public class CompleteItemGUI extends Menu {
    
    public CompleteItemGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lComplete Items", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Placeholder implementation
        setCloseButton(49);
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        if (slot == 49) {
            close();
        }
    }

    /**
     * Open item GUI for player
     */
    public void openItemGUI(Player player) {
        // Create new instance with player
        CompleteItemGUI gui = new CompleteItemGUI(plugin, player);
        gui.open();
    }

    /**
     * Open dragon weapons GUI for player
     */
    public void openDragonWeaponsGUI(Player player) {
        // Create new instance with player
        CompleteItemGUI gui = new CompleteItemGUI(plugin, player);
        gui.fillBorders();
        gui.setCloseButton(49);
        gui.open();
    }

    /**
     * Open dungeon weapons GUI for player
     */
    public void openDungeonWeaponsGUI(Player player) {
        // Create new instance with player
        CompleteItemGUI gui = new CompleteItemGUI(plugin, player);
        gui.fillBorders();
        gui.setCloseButton(49);
        gui.open();
    }

    /**
     * Open mining tools GUI for player
     */
    public void openMiningToolsGUI(Player player) {
        // Create new instance with player
        CompleteItemGUI gui = new CompleteItemGUI(plugin, player);
        gui.fillBorders();
        gui.setCloseButton(49);
        gui.open();
    }
}
