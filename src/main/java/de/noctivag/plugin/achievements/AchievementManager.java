package de.noctivag.plugin.achievements;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
public class AchievementManager {
    private final Plugin plugin;
    private final Map<UUID, Set<Achievement>> unlockedAchievements;
    private final Map<UUID, Map<Achievement, Integer>> achievementProgress;
    private final File achievementFile;
    private FileConfiguration achievementConfig;

    public AchievementManager(Plugin plugin) {
        this.plugin = plugin;
        this.unlockedAchievements = new HashMap<>();
        this.achievementProgress = new HashMap<>();
        this.achievementFile = new File(plugin.getDataFolder(), "achievements.yml");
        loadData();
    }

    private void loadData() {
        if (!achievementFile.exists()) {
            plugin.saveResource("achievements.yml", false);
        }
        achievementConfig = YamlConfiguration.loadConfiguration(achievementFile);

        // Lade freigeschaltete Achievements
        if (achievementConfig.contains("unlocked")) {
            for (String uuidStr : achievementConfig.getConfigurationSection("unlocked").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                Set<Achievement> achievements = new HashSet<>();
                List<String> achievementList = achievementConfig.getStringList("unlocked." + uuidStr);
                for (String achievementName : achievementList) {
                    try {
                        achievements.add(Achievement.valueOf(achievementName));
                    } catch (IllegalArgumentException ignored) {}
                }
                unlockedAchievements.put(uuid, achievements);
            }
        }

        // Lade Achievement-Fortschritte
        if (achievementConfig.contains("progress")) {
            for (String uuidStr : achievementConfig.getConfigurationSection("progress").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                Map<Achievement, Integer> progress = new HashMap<>();
                ConfigurationSection playerSection = achievementConfig.getConfigurationSection("progress." + uuidStr);
                if (playerSection != null) {
                    for (String achievementName : playerSection.getKeys(false)) {
                        try {
                            Achievement achievement = Achievement.valueOf(achievementName);
                            progress.put(achievement, playerSection.getInt(achievementName));
                        } catch (IllegalArgumentException ignored) {}
                    }
                }
                achievementProgress.put(uuid, progress);
            }
        }
    }

    public void saveData() {
        achievementConfig = new YamlConfiguration();

        // Speichere freigeschaltete Achievements
        for (Map.Entry<UUID, Set<Achievement>> entry : unlockedAchievements.entrySet()) {
            List<String> achievementList = new ArrayList<>();
            for (Achievement achievement : entry.getValue()) {
                achievementList.add(achievement.name());
            }
            achievementConfig.set("unlocked." + entry.getKey().toString(), achievementList);
        }

        // Speichere Achievement-Fortschritte
        for (Map.Entry<UUID, Map<Achievement, Integer>> entry : achievementProgress.entrySet()) {
            String uuidPath = "progress." + entry.getKey().toString();
            for (Map.Entry<Achievement, Integer> progressEntry : entry.getValue().entrySet()) {
                achievementConfig.set(uuidPath + "." + progressEntry.getKey().name(), progressEntry.getValue());
            }
        }

        try {
            achievementConfig.save(achievementFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Fehler beim Speichern der Achievements: " + e.getMessage());
        }
    }

    public boolean hasAchievement(Player player, Achievement achievement) {
        Set<Achievement> playerAchievements = unlockedAchievements.getOrDefault(player.getUniqueId(), new HashSet<>());
        return playerAchievements.contains(achievement);
    }

    public void awardAchievement(Player player, Achievement achievement) {
        if (!hasAchievement(player, achievement)) {
            Set<Achievement> playerAchievements = unlockedAchievements.computeIfAbsent(
                player.getUniqueId(), k -> new HashSet<>());
            playerAchievements.add(achievement);

            // Gebe Belohnungen
            // TODO: Implement proper PlayerDataManager interface
            // ((PlayerDataManager) plugin.getPlayerDataManager()).addExp(player, achievement.getExpReward());
            plugin.getEconomyManager().depositMoney(player, achievement.getCoinReward());

            // Zeige Erfolg an
            achievement.award(player);

            // Speichere Daten
            saveData();
        }
    }

    public void updateProgress(Player player, Achievement achievement, int progress) {
        UUID uuid = player.getUniqueId();
        Map<Achievement, Integer> playerProgress = achievementProgress.computeIfAbsent(uuid, k -> new HashMap<>());
        playerProgress.put(achievement, progress);

        // Prüfe ob Achievement freigeschaltet werden soll
        checkAchievementUnlock(player, achievement, progress);

        saveData();
    }

    public int getProgress(Player player, Achievement achievement) {
        UUID uuid = player.getUniqueId();
        Map<Achievement, Integer> playerProgress = achievementProgress.get(uuid);
        return playerProgress != null ? playerProgress.getOrDefault(achievement, 0) : 0;
    }

    private void checkAchievementUnlock(Player player, Achievement achievement, int progress) {
        if (achievement.checkUnlock(player, progress)) {
            awardAchievement(player, achievement);
        }
    }

    public Set<Achievement> getUnlockedAchievements(Player player) {
        return unlockedAchievements.getOrDefault(player.getUniqueId(), new HashSet<>());
    }

    public double getCompletionPercentage(Player player) {
        int total = Achievement.values().length;
        int unlocked = getUnlockedAchievements(player).size();
        return (double) unlocked / total * 100;
    }
    
    // Missing method implementations for compilation fixes
    public int getCompletedAchievements(Player player) {
        return getUnlockedAchievements(player).size();
    }
    
    public int getTotalAchievements() {
        return Achievement.values().length;
    }
}
