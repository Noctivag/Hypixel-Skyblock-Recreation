package de.noctivag.plugin.fishing;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Fishing System - Basic implementation
 */
public class FishingSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public FishingSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openFishingRodsGUI(Player player) {
        player.sendMessage("§aFishing Rods GUI geöffnet!");
    }
}
