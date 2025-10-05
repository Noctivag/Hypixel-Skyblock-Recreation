package de.noctivag.skyblock.rewards.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.rewards.gui.DailyRewardGUIListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RewardChatListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final DailyRewardGUIListener guiListener;

    public RewardChatListener(SkyblockPlugin SkyblockPlugin, DailyRewardGUIListener guiListener) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.guiListener = guiListener;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("SkyblockPlugin.admin")) return;

        // Überprüfe, ob der Spieler gerade Belohnungen bearbeitet
        if (guiListener.isEditingRewards(event.getPlayer())) {
            event.setCancelled(true);
            String msg = event.getMessage();
            guiListener.handleChatInput(event.getPlayer(), msg);
        }
    }
}
