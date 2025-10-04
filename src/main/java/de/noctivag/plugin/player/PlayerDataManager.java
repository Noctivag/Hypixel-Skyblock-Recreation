package de.noctivag.plugin.player;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Plugin plugin;
    private final Map<UUID, PlayerData> playerData;
    private final File dataFile;
    private FileConfiguration dataConfig;

    public PlayerDataManager(Plugin plugin) {
        this.plugin = plugin;
        this.playerData = new HashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            plugin.saveResource("playerdata.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        if (dataConfig.contains("players")) {
            for (String uuidStr : dataConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                int level = dataConfig.getInt("players." + uuidStr + ".level", 1);
                double exp = dataConfig.getDouble("players." + uuidStr + ".exp", 0.0);
                playerData.put(uuid, new PlayerData(level, exp));
            }
        }
    }

    public void saveData() {
        dataConfig = new YamlConfiguration();
        for (Map.Entry<UUID, PlayerData> entry : playerData.entrySet()) {
            String path = "players." + entry.getKey().toString();
            dataConfig.set(path + ".level", entry.getValue().getLevel());
            dataConfig.set(path + ".exp", entry.getValue().getExp());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Speichern der Spielerdaten: " + e.getMessage());
        }
    }

    public void addExp(Player player, double amount) {
        UUID uuid = player.getUniqueId();
        PlayerData data = playerData.computeIfAbsent(uuid, k -> new PlayerData(1, 0.0));

        data.addExp(amount);
        while (data.canLevelUp()) {
            data.levelUp();
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.showTitle(Title.title(
                Component.text("§6Level Up!"),
                Component.text("§eLevel " + data.getLevel() + " erreicht!"),
                Title.Times.times(
                    java.time.Duration.ofMillis(500),
                    java.time.Duration.ofMillis(3500),
                    java.time.Duration.ofMillis(1000)
                )
            ));

            // Belohnungen für Level-Up
            double coins = data.getLevel() * 100; // 100 Coins pro Level
            plugin.getEconomyManager().depositMoney(player, coins);
            player.sendMessage("§aLevel-Up Belohnung: §e" + plugin.getEconomyManager().formatMoney(coins));
        }

        // Aktualisiere Scoreboard
        // TODO: Implement proper ScoreboardManager interface
        // ((ScoreboardManager) plugin.getScoreboardManager()).updateScoreboard(player);
    }

    public int getLevel(Player player) {
        return playerData.computeIfAbsent(
            player.getUniqueId(),
            k -> new PlayerData(1, 0.0)
        ).getLevel();
    }

    public double getExp(Player player) {
        return playerData.computeIfAbsent(
            player.getUniqueId(),
            k -> new PlayerData(1, 0.0)
        ).getExp();
    }

    public double getExpToNextLevel(Player player) {
        return playerData.computeIfAbsent(
            player.getUniqueId(),
            k -> new PlayerData(1, 0.0)
        ).getExpToNextLevel();
    }

}
