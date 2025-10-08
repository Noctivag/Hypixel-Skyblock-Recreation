package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import de.noctivag.skyblock.core.stats.PlayerStatData;

/**
 * Health and Mana System - Manages player health and mana
 */
public class HealthManaSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerStatData> playerStats = new ConcurrentHashMap<>();

    public HealthManaSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Initialisiert Health/Mana Stats für einen Spieler
     */
    public void initializePlayer(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerStatData stats = playerStats.computeIfAbsent(playerId, PlayerStatData::new);
        // Setze Health/Mana auf Standardwerte (kann angepasst werden)
        stats.setMaxHealth(100.0);
        stats.setHealth(100.0);
        stats.setMaxMana(100.0);
        stats.setMana(100.0);
        stats.setManaRegen(1.0);
        // Setze Bukkit-Healthbar
        player.setHealth(20.0);
        player.setFoodLevel(20);
    }

    /**
     * Zugriff auf die Stat-Daten eines Spielers
     */
    public PlayerStatData getPlayerStats(UUID playerId) {
        return playerStats.get(playerId);
    }

    /**
     * Speichert die Stat-Daten eines Spielers (Platzhalter für DB)
     */
    public void savePlayerStats(UUID playerId) {
        PlayerStatData stats = playerStats.get(playerId);
        if (stats != null) {
            plugin.getLogger().info("Saved health/mana stats for: " + playerId);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        initializePlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerStats(player.getUniqueId());
    }
}