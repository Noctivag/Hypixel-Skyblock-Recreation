package de.noctivag.plugin.bank;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.entity.Player;

/**
 * Bank System - Basic implementation
 */
public class BankSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;

    public BankSystem(Plugin plugin, DatabaseManager databaseManager) {
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
