package de.noctivag.skyblock.slayers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Slayer System - Basic implementation
 */
public class SlayerSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public SlayerSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openSlayerWeaponsGUI(Player player) {
        player.sendMessage("§aSlayer Weapons GUI geöffnet!");
    }
}
