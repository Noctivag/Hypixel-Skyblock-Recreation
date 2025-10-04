package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.managers.RestartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class RestartGUIListener implements Listener {
    private final SkyblockPlugin plugin;
    private final RestartManager restartManager;

    public RestartGUIListener(SkyblockPlugin plugin, RestartManager restartManager) {
        this.plugin = plugin;
        this.restartManager = restartManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof RestartGUI)) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        int slot = event.getSlot();
        // Only allow admins to schedule
        if (!player.hasPermission("plugin.admin")) {
            player.sendMessage("§cKeine Berechtigung.");
            player.closeInventory();
            return;
        }

        switch (slot) {
            case 10 -> { // 60s
                restartManager.scheduleRestart(60);
                player.sendMessage("§aNeustart in 60 Sekunden geplant.");
                player.closeInventory();
            }
            case 12 -> { // 5 minutes
                restartManager.scheduleRestart(300);
                player.sendMessage("§aNeustart in 5 Minuten geplant.");
                player.closeInventory();
            }
            case 14 -> { // 1 hour
                restartManager.scheduleRestart(3600);
                player.sendMessage("§aNeustart in 1 Stunde geplant.");
                player.closeInventory();
            }
            case 16 -> { // cancel
                restartManager.cancelRestart();
                player.sendMessage("§aGeplanter Neustart wurde abgebrochen.");
                player.closeInventory();
            }
            case 22 -> { // close
                player.closeInventory();
            }
            default -> {
                // ignore other slots
            }
        }
    }
}
