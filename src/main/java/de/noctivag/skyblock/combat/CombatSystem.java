package de.noctivag.skyblock.combat;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Combat System - Basic implementation
 */
public class CombatSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    public CombatSystem(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openBowsGUI(Player player) {
        player.sendMessage(Component.text("§aBows GUI geöffnet!"));
    }
}
