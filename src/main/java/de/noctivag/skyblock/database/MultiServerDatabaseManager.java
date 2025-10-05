package de.noctivag.skyblock.database;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.accessories.PlayerAccessoryData;
import de.noctivag.skyblock.brewing.PlayerBrewingData;

import java.util.UUID;
import java.util.logging.Level;

public class MultiServerDatabaseManager extends DatabaseManager {

    public MultiServerDatabaseManager(SkyblockPlugin plugin) {
        super(plugin);
    }

    @Override
    public void savePlayerAccessoryData(UUID playerUuid, PlayerAccessoryData data) {
        plugin.getLogger().log(Level.INFO, "Saving accessory data for player: " + playerUuid);
    }

    @Override
    public PlayerAccessoryData loadPlayerAccessoryData(UUID playerUuid) {
        plugin.getLogger().log(Level.INFO, "Loading accessory data for player: " + playerUuid);
        return new PlayerAccessoryData(playerUuid);
    }

    @Override
    public void savePlayerBrewingData(UUID playerUuid, PlayerBrewingData data) {
        plugin.getLogger().log(Level.INFO, "Saving brewing data for player: " + playerUuid);
    }

    @Override
    public PlayerBrewingData loadPlayerBrewingData(UUID playerUuid) {
        plugin.getLogger().log(Level.INFO, "Loading brewing data for player: " + playerUuid);
        return new PlayerBrewingData(playerUuid);
    }

    public String getServerId() {
        return "default_server";
    }
}