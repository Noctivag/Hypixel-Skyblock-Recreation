package de.noctivag.plugin.listeners;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.config.ConfigManager;
import de.noctivag.plugin.utils.ColorUtils;
import de.noctivag.plugin.utils.MenuItemManager;
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
    private final Plugin plugin;
    private final ConfigManager config;

    public PlayerListener(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Ensure Skyblock profile exists for this player to avoid null pointer issues in other systems
        try {
            if (plugin.getSkyblockManager() != null) {
                plugin.getSkyblockManager().ensureProfileLoaded(player);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error while ensuring Skyblock profile for player " + player.getName() + ": " + e.getMessage());
        }

        String joinMessage = config.getMessage("join-message")
            .replace("%player%", player.getName());
        event.joinMessage(ColorUtils.translate(joinMessage));

        // Load player data
        plugin.getEconomyManager().createAccount(player);
        plugin.getCosmeticsManager().loadPlayerEffects(player);

        // Load Minions for player (if system available)
        try {
            if (plugin.getAdvancedMinionSystem() != null) {
                // plugin.loadMinionData(player.getUniqueId()); // TODO: Implement method in Plugin class
            }
        } catch (Exception ignored) {}

        // Update display name and prefix
        if (plugin.getPrefixMap().containsKey(player.getName())) {
            updatePlayerDisplay(player);
        }

        // Debug mode info
        if (config.isDebugMode() && player.hasPermission("plugin.debug")) {
            player.sendMessage(Component.text("§7[Debug] Spielerdaten geladen"));
        }

        // Welcome message if first join
        if (!player.hasPlayedBefore()) {
            player.sendMessage(Component.text(ColorUtils.parseColor(config.getMessage("first-join-message"))));
            double startingBalance = plugin.getConfigManager().getConfig()
                .getDouble("economy.starting-balance", 100.0);
            plugin.getEconomyManager().setBalance(player, startingBalance);
        }

        // Give menu item to all players
        MenuItemManager.giveMenuItem(player);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.setLastLocation(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        plugin.setLastLocation(player, player.getLocation());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String quitMessage = config.getMessage("quit-message")
            .replace("%player%", player.getName());
        event.quitMessage(ColorUtils.translate(quitMessage));

        // Save and cleanup
        plugin.getCosmeticsManager().stopParticleEffect(player);
        // Stop any cosmetics sound via the cosmetics manager
        plugin.getCosmeticsManager().stopSoundEffect(player);
        plugin.getEconomyManager().saveBalances();

        // Save Minions for player
        try {
            if (plugin.getAdvancedMinionSystem() != null) {
                // plugin.saveMinionData(player.getUniqueId()); // TODO: Implement method in Plugin class
            }
        } catch (Exception ignored) {}

        // Persist Skyblock data for this player
        try {
            if (plugin.getSkyblockManager() != null) {
                var profile = plugin.getSkyblockManager().getProfile(player.getUniqueId());
                if (profile != null) profile.save();
                var island = plugin.getSkyblockManager().getIsland(player.getUniqueId());
                if (island != null) island.save();
                // Also update SQLite storage where available
                try { de.noctivag.plugin.data.SQLiteStorage.saveProfile(profile); } catch (Exception ignored) {}
                try { de.noctivag.plugin.data.SQLiteStorage.saveIsland(island); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to persist Skyblock data for " + player.getName() + ": " + e.getMessage());
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        // Check if cosmetics are disabled in this world
        if (plugin.getConfigManager().getConfig()
            .getStringList("cosmetics.particle-effects.disabled-worlds")
            .contains(worldName)) {
            plugin.getCosmeticsManager().stopParticleEffect(player);
            player.sendMessage(ColorUtils.parseColor(
                config.getMessage("cosmetics-disabled-in-world")));
        } else {
            // Restore effects if they were active
            plugin.getCosmeticsManager().loadPlayerEffects(player);
        }
    }

    private void updatePlayerDisplay(Player player) {
        String prefix = plugin.getPrefixMap().getOrDefault(player.getName(), "");
        String nick = plugin.getNickMap().getOrDefault(player.getName(), player.getName());

        Component displayName = ColorUtils.translate(
            prefix.isEmpty() ? nick : prefix + " " + nick
        );
        player.displayName(displayName);
        player.playerListName(displayName);
    }
}
