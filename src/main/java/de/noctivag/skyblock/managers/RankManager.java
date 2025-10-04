package de.noctivag.skyblock.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RankManager {
    private final SkyblockPlugin plugin;
    private final File ranksFile;
    private FileConfiguration ranksConfig;

    public RankManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.ranksFile = new File(plugin.getDataFolder(), "ranks.yml");
        // load() moved to init() to avoid "this" escape during construction
    }

    // Call once after construction (e.g. from Plugin.onEnable)
    public void init() {
        load();
    }

    private void load() {
        if (!ranksFile.exists()) {
            // create default file
            try {
                ranksFile.getParentFile().mkdirs();
                ranksFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create ranks.yml: " + e.getMessage());
            }
        }
        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
        if (!ranksConfig.contains("ranks")) {
            ranksConfig.set("ranks.default.display", "Player");
            ranksConfig.set("ranks.default.permissions", Collections.emptyList());
            ranksConfig.set("ranks.moderator.display", "Moderator");
            ranksConfig.set("ranks.moderator.permissions", List.of("basicsplugin.kick", "basicsplugin.mute"));
            ranksConfig.set("ranks.admin.display", "Admin");
            ranksConfig.set("ranks.admin.permissions", List.of("basicsplugin.*"));
            save();
        }
    }

    public void save() {
        try {
            ranksConfig.save(ranksFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save ranks.yml: " + e.getMessage());
        }
    }

    public Set<String> getAllRankKeys() {
        return ranksConfig.getConfigurationSection("ranks").getKeys(false);
    }

    public String getDisplayName(String rankKey) {
        return ranksConfig.getString("ranks." + rankKey + ".display", rankKey);
    }

    public List<String> getPermissions(String rankKey) {
        return ranksConfig.getStringList("ranks." + rankKey + ".permissions");
    }

    public boolean hasPermission(String rankKey, String permission) {
        return getPermissions(rankKey).contains(permission);
    }

    public void addPermission(String rankKey, String permission) {
        List<String> perms = new ArrayList<>(getPermissions(rankKey));
        if (!perms.contains(permission)) {
            perms.add(permission);
            ranksConfig.set("ranks." + rankKey + ".permissions", perms);
            save();
        }
    }

    public void removePermission(String rankKey, String permission) {
        List<String> perms = new ArrayList<>(getPermissions(rankKey));
        if (perms.remove(permission)) {
            ranksConfig.set("ranks." + rankKey + ".permissions", perms);
            save();
        }
    }

    public String getPlayerRank(Player player) {
        return ranksConfig.getString("players." + player.getUniqueId() + ".rank", "default");
    }

    public void setPlayerRank(Player player, String rankKey) {
        ranksConfig.set("players." + player.getUniqueId() + ".rank", rankKey);
        // Also mirror into main config for ScoreboardManager compatibility
        // TODO: Implement proper ConfigManager interface
        // ((ConfigManager) plugin.getConfigManager()).getConfig().set("players." + player.getUniqueId() + ".rank", getDisplayName(rankKey));
        // ((ConfigManager) plugin.getConfigManager()).saveConfig("config");
        save();
    }
}
