package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.gui.stats.PlayerStatsGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().title().toString();
        if (title.contains("Skyblock-Stats")) {
            event.setCancelled(true);
        }
    }
}
