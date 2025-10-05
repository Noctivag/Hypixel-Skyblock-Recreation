package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.config.ConfigManager;
import de.noctivag.skyblock.utils.ColorUtils;
import de.noctivag.skyblock.utils.MenuItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


public class PlayerListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final ConfigManager config;

    public PlayerListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.config = SkyblockPlugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Ensure Skyblock profile exists for this player to avoid null pointer issues in other systems
        try {
            if (SkyblockPlugin.getSkyblockManager() != null) {
                SkyblockPlugin.getSkyblockManager().ensureProfileLoaded(player);
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error while ensuring Skyblock profile for player " + player.getName() + ": " + e.getMessage());
        }

        String joinMessage = config.getMessage("join-message")
            .replace("%player%", player.getName());
        event.joinMessage(ColorUtils.translate(joinMessage));

        // Load player data
        SkyblockPlugin.getEconomyManager().createAccount(player);
        SkyblockPlugin.getCosmeticsManager().loadPlayerEffects(player);

        // Load Minions for player (if system available)
        try {
            if (SkyblockPlugin.getAdvancedMinionSystem() != null) {
                // SkyblockPlugin.loadMinionData(player.getUniqueId()); // TODO: Implement method in SkyblockPlugin class
            }
        } catch (Exception ignored) {}

        // Update display name and prefix
        if (SkyblockPlugin.getPrefixMap().containsKey(player.getName())) {
            updatePlayerDisplay(player);
        }

        // Debug mode info
        if (config.isDebugMode() && player.hasPermission("SkyblockPlugin.debug")) {
            player.sendMessage(Component.text("§7[Debug] Spielerdaten geladen"));
        }

        // Welcome message if first join
        if (!player.hasPlayedBefore()) {
            player.sendMessage(Component.text(ColorUtils.parseColor(config.getMessage("first-join-message"))));
            double startingBalance = SkyblockPlugin.getConfigManager().getConfig()
                .getDouble("economy.starting-balance", 100.0);
            SkyblockPlugin.getEconomyManager().setBalance(player, startingBalance);
        }

        // Give menu item to all players
        MenuItemManager.giveMenuItem(player);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        SkyblockPlugin.setLastLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        SkyblockPlugin.setLastLocation(player, player.getLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = config.getMessage("quit-message")
            .replace("%player%", player.getName());
        event.quitMessage(ColorUtils.translate(quitMessage));

        // Save and cleanup
        SkyblockPlugin.getCosmeticsManager().stopParticleEffect(player);
        // Stop any cosmetics sound via the cosmetics manager
        SkyblockPlugin.getCosmeticsManager().stopSoundEffect(player);
        SkyblockPlugin.getEconomyManager().saveBalances();

        // Save Minions for player
        try {
            if (SkyblockPlugin.getAdvancedMinionSystem() != null) {
                // SkyblockPlugin.saveMinionData(player.getUniqueId()); // TODO: Implement method in SkyblockPlugin class
            }
        } catch (Exception ignored) {}

        // Persist Skyblock data for this player
        try {
            if (SkyblockPlugin.getSkyblockManager() != null) {
                var profile = SkyblockPlugin.getSkyblockManager().getProfile(player.getUniqueId());
                if (profile != null) profile.save();
                var island = SkyblockPlugin.getSkyblockManager().getIsland(player.getUniqueId());
                if (island != null) island.save();
                // Also update SQLite storage where available
                try { de.noctivag.skyblock.data.SQLiteStorage.saveProfile(profile); } catch (Exception ignored) {}
                try { de.noctivag.skyblock.data.SQLiteStorage.saveIsland(island); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Failed to persist Skyblock data for " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        // Check if cosmetics are disabled in this world
        if (SkyblockPlugin.getConfigManager().getConfig()
            .getStringList("cosmetics.particle-effects.disabled-worlds")
            .contains(worldName)) {
            SkyblockPlugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage(ColorUtils.parseColor(
                config.getMessage("cosmetics-disabled-in-world")));
        } else {
            // Restore effects if they were active
            SkyblockPlugin.getCosmeticsManager().loadPlayerEffects(player);
        }
    }

    private void updatePlayerDisplay(Player player) {
        String prefix = SkyblockPlugin.getPrefixMap().getOrDefault(player.getName(), "");
        String nick = SkyblockPlugin.getNickMap().getOrDefault(player.getName(), player.getName());

        Component displayName = ColorUtils.translate(
            prefix.isEmpty() ? nick : prefix + " " + nick
        );
        player.displayName(displayName);
        player.playerListName(displayName);
    }
}
