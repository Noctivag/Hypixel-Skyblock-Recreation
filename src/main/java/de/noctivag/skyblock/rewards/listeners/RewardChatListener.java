package de.noctivag.skyblock.rewards.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.rewards.gui.DailyRewardGUIListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RewardChatListener implements Listener {
    private final SkyblockPlugin plugin;
    private final DailyRewardGUIListener guiListener;

    public RewardChatListener(SkyblockPlugin plugin, DailyRewardGUIListener guiListener) {
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
