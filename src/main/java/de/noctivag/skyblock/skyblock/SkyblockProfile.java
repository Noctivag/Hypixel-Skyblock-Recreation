package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkyblockProfile {
    private static final Logger LOGGER = Logger.getLogger("BasicsPlugin");
    private final UUID uuid;
    private final String playerName;
    private final LocalDateTime createdAt;
    private LocalDateTime lastPlayed;
    private int playtime; // in minutes
    private double coins;
    private int islandLevel;
    private boolean firstJoin;

    public SkyblockProfile(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.createdAt = LocalDateTime.now();
        this.lastPlayed = LocalDateTime.now();
        this.playtime = 0;
        this.coins = 1000.0; // Starting coins
        this.islandLevel = 1;
        this.firstJoin = true;
    }

    public void updateLastPlayed() {
        this.lastPlayed = LocalDateTime.now();
    }

    public void addPlaytime(int minutes) {
        this.playtime += minutes;
    }

    public void addCoins(double amount) {
        this.coins += amount;
    }

    public boolean removeCoins(double amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }

    public void levelUpIsland() {
        this.islandLevel++;
    }

    public void setFirstJoin(boolean firstJoin) {
        this.firstJoin = firstJoin;
    }

    // Getters
    public UUID getUuid() { return uuid; }
    public String getPlayerName() { return playerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastPlayed() { return lastPlayed; }
    public int getPlaytime() { return playtime; }
    public double getCoins() { return coins; }
    public int getIslandLevel() { return islandLevel; }
    public boolean isFirstJoin() { return firstJoin; }

    public void save() {
        try {
            File dir = new File("plugins/BasicsPlugin/skyblock/profiles/");
            if (!dir.exists()) {
                boolean ok = dir.mkdirs();
                if (!ok && !dir.exists()) {
                    LOGGER.warning("Could not create profiles directory: " + dir.getAbsolutePath());
                }
            }

            File file = new File(dir, uuid.toString() + ".yml");

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("playerName", playerName);
            config.set("createdAt", createdAt.toString());
            config.set("lastPlayed", lastPlayed != null ? lastPlayed.toString() : LocalDateTime.now().toString());
            config.set("playtime", playtime);
            config.set("coins", coins);
            config.set("islandLevel", islandLevel);
            config.set("firstJoin", firstJoin);

            config.save(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save SkyblockProfile for " + uuid, e);
        }
    }

    public static SkyblockProfile load(UUID uuid) {
        try {
            File file = new File("plugins/BasicsPlugin/skyblock/profiles/" + uuid.toString() + ".yml");
            if (!file.exists()) return null;

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String name = config.getString("playerName");
            if (name == null) name = "Unknown";

            SkyblockProfile profile = new SkyblockProfile(uuid, name);

            String lastPlayedStr = config.getString("lastPlayed");
            if (lastPlayedStr != null && !lastPlayedStr.isEmpty()) {
                try {
                    profile.lastPlayed = LocalDateTime.parse(lastPlayedStr);
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Invalid lastPlayed format for " + uuid + ": " + lastPlayedStr, ex);
                    profile.lastPlayed = profile.createdAt;
                }
            } else {
                profile.lastPlayed = profile.createdAt;
            }

            profile.playtime = config.getInt("playtime", 0);
            profile.coins = config.getDouble("coins", 1000.0);
            profile.islandLevel = config.getInt("islandLevel", 1);
            profile.firstJoin = config.getBoolean("firstJoin", true);

            return profile;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load SkyblockProfile for " + uuid, e);
            return null;
        }
    }
}
