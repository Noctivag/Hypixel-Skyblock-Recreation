package de.noctivag.skyblock.bank;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Bank System - Basic implementation
 */
public class BankSystem {
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;

    public BankSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void openDepositGUI(Player player) {
        player.sendMessage("§aDeposit GUI geöffnet!");
    }

    public void openWithdrawGUI(Player player) {
        player.sendMessage("§aWithdraw GUI geöffnet!");
    }
}
