package de.noctivag.plugin.magic;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Magic System - Basic implementation
 */
public class MagicSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public MagicSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openMagicWeaponsGUI(Player player) {
        player.sendMessage("§aMagic Weapons GUI geöffnet!");
    }
}
