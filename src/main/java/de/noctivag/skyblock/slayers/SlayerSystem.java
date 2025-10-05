package de.noctivag.skyblock.slayers;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Slayer System - Basic implementation
 */
public class SlayerSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    public SlayerSystem(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openSlayerWeaponsGUI(Player player) {
        player.sendMessage(Component.text("§aSlayer Weapons GUI geöffnet!"));
    }
}
