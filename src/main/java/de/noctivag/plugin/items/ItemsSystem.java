package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Items System - Basic implementation
 */
public class ItemsSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public ItemsSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openSpecialItemsGUI(Player player) {
        player.sendMessage("§aSpecial Items GUI geöffnet!");
    }
}
