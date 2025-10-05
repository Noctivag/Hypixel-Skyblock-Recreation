package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.services.MinionManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {
    private final SkyblockPluginRefactored SkyblockPlugin;

    public PlayerListener(SkyblockPluginRefactored SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Ensure Skyblock profile exists for this player to avoid null pointer issues in other systems
        try {
            // Use ServiceManager to get PlayerProfileService
            if (SkyblockPlugin.getServiceManager() != null) {
                var playerProfileService = SkyblockPlugin.getServiceManager().getService(PlayerProfileService.class);
                if (playerProfileService != null) {
                    playerProfileService.loadProfile(player.getUniqueId());
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error while ensuring Skyblock profile for player " + player.getName() + ": " + e.getMessage());
        }

        String joinMessage = "§aWillkommen auf dem Server, " + player.getName() + "!";
        event.joinMessage(Component.text(joinMessage));

        // Load Minions for player (if system available)
        try {
            if (SkyblockPlugin.getServiceManager() != null) {
                var minionManager = SkyblockPlugin.getServiceManager().getService(MinionManager.class);
                if (minionManager != null) {
                    // Minion data will be loaded through PlayerProfileService
                }
            }
        } catch (Exception ignored) {}

        // Debug mode info
        if (SkyblockPlugin.getSettingsConfig().isDebugMode() && player.hasPermission("SkyblockPlugin.debug")) {
            player.sendMessage(Component.text("§7[Debug] Spielerdaten geladen"));
        }

        // Welcome message if first join
        if (!player.hasPlayedBefore()) {
            player.sendMessage(Component.text("§aWillkommen auf dem Server!"));
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // Teleport tracking can be implemented through services if needed
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Death tracking can be implemented through services if needed
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = "§c" + player.getName() + " hat den Server verlassen.";
        event.quitMessage(Component.text(quitMessage));

        // Save player data through services
        try {
            if (SkyblockPlugin.getServiceManager() != null) {
                var playerProfileService = SkyblockPlugin.getServiceManager().getService(PlayerProfileService.class);
                if (playerProfileService != null) {
                    playerProfileService.saveProfile(player.getUniqueId());
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error while saving player data for " + player.getName() + ": " + e.getMessage());
        }
    }
}