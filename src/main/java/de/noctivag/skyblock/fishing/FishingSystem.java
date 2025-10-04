package de.noctivag.skyblock.fishing;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Fishing System - Basic implementation
 */
public class FishingSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public FishingSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openFishingRodsGUI(Player player) {
        player.sendMessage("§aFishing Rods GUI geöffnet!");
    }
}
