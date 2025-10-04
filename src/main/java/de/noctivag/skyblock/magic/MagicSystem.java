package de.noctivag.skyblock.magic;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Magic System - Basic implementation
 */
public class MagicSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public MagicSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openMagicWeaponsGUI(Player player) {
        player.sendMessage("§aMagic Weapons GUI geöffnet!");
    }
}
