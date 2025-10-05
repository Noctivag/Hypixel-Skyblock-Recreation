package de.noctivag.skyblock.items;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Items System - Basic implementation
 */
public class ItemsSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    public ItemsSystem(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openSpecialItemsGUI(Player player) {
        player.sendMessage(Component.text("§aSpecial Items GUI geöffnet!"));
    }
}
