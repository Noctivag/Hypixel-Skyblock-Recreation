package de.noctivag.plugin.combat;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Combat System - Basic implementation
 */
public class CombatSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public CombatSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openBowsGUI(Player player) {
        player.sendMessage("§aBows GUI geöffnet!");
    }
}
