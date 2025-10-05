package de.noctivag.skyblock.data;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.skyblock.SkyblockIsland;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleIslandStorage {
    private static final File FILE = new File("plugins", "BasicsPlugin_islands.yml");
    private static final YamlConfiguration config = YamlConfiguration.loadConfiguration(FILE);

    public static synchronized Map<UUID, SkyblockIsland> loadAllIslands() {
        Map<UUID, SkyblockIsland> islands = new HashMap<>();
        if (!config.contains("islands")) return islands;
        if (config.getConfigurationSection("islands") == null) return islands;
        for (String key : config.getConfigurationSection("islands").getKeys(false)) {
            try {
                if (config.getConfigurationSection("islands." + key) == null) continue;
                Map<String, Object> map = config.getConfigurationSection("islands." + key).getValues(false);
                if (map == null) continue;
                SkyblockIsland island = SkyblockIsland.fromMap(map);
                if (island != null) {
                    islands.put(island.getOwner(), island);
                }
            } catch (Exception ex) {
                Bukkit.getLogger().warning("Failed to load island: " + key + " - " + ex.getMessage());
            }
        }
        return islands;
    }

    public static synchronized void saveIsland(SkyblockIsland island) {
        if (island == null) return;
        config.set("islands." + island.getOwner().toString(), island.toMap());
        save();
    }

    public static synchronized void saveAll(Map<UUID, SkyblockIsland> islands) {
        config.set("islands", null);
        for (Map.Entry<UUID, SkyblockIsland> e : islands.entrySet()) {
            config.set("islands." + e.getKey().toString(), e.getValue().toMap());
        }
        save();
    }

    public static synchronized void addTrusted(UUID owner, UUID member) {
        if (owner == null || member == null) return;
        String path = "islands." + owner.toString() + ".trusted";
        java.util.List<String> list = config.getStringList(path);
        if (!list.contains(member.toString())) {
            list.add(member.toString());
            config.set(path, list);
            save();
        }
    }

    public static synchronized void removeTrusted(UUID owner, UUID member) {
        if (owner == null || member == null) return;
        String path = "islands." + owner.toString() + ".trusted";
        java.util.List<String> list = config.getStringList(path);
        if (list.remove(member.toString())) {
            config.set(path, list);
            save();
        }
    }

    private static void save() {
        try {
            if (!FILE.getParentFile().exists()) {
                boolean ok = FILE.getParentFile().mkdirs();
                if (!ok) Bukkit.getLogger().warning("Could not create plugins directory for SimpleIslandStorage");
            }
            config.save(FILE);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Failed saving island data: " + e.getMessage());
        }
    }
}
