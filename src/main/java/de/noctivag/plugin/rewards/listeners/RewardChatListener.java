package de.noctivag.plugin.rewards.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.rewards.gui.DailyRewardGUIListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RewardChatListener implements Listener {
    private final Plugin plugin;
    private final DailyRewardGUIListener guiListener;

    public RewardChatListener(Plugin plugin, DailyRewardGUIListener guiListener) {
        this.plugin = plugin;
        this.guiListener = guiListener;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("plugin.admin")) return;

        // Überprüfe, ob der Spieler gerade Belohnungen bearbeitet
        if (guiListener.isEditingRewards(event.getPlayer())) {
            event.setCancelled(true);
            String msg = event.getMessage();
            guiListener.handleChatInput(event.getPlayer(), msg);
        }
    }
}
