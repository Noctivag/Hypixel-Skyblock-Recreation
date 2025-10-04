package de.noctivag.plugin.slayers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Slayer System - Basic implementation
 */
public class SlayerSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public SlayerSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openSlayerWeaponsGUI(Player player) {
        player.sendMessage("§aSlayer Weapons GUI geöffnet!");
    }
}
