package de.noctivag.skyblock.combat;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Combat System - Basic implementation
 */
public class CombatSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public CombatSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openBowsGUI(Player player) {
        player.sendMessage("§aBows GUI geöffnet!");
    }
}
