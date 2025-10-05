package de.noctivag.skyblock.bank;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Bank System - Basic implementation
 */
public class BankSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final DatabaseManager databaseManager;

    public BankSystem(SkyblockPlugin SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void openDepositGUI(Player player) {
        player.sendMessage(Component.text("§aDeposit GUI geöffnet!"));
    }

    public void openWithdrawGUI(Player player) {
        player.sendMessage(Component.text("§aWithdraw GUI geöffnet!"));
    }
}
