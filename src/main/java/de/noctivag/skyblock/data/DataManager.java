package de.noctivag.skyblock.data;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {
    private final SkyblockPlugin plugin;
    private File file;
    private FileConfiguration config;

    public DataManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        loadData();
    }

    public void loadData() {
        file = new File(plugin.getDataFolder(), "prefixes.yml");

        if (!file.exists()) {
            try {
                // Ensure parent directory exists
                if (!plugin.getDataFolder().exists()) {
                    boolean dirsCreated = plugin.getDataFolder().mkdirs();
                    if (!dirsCreated && !plugin.getDataFolder().exists()) {
                        plugin.getLogger().warning("Could not create plugin data folder: " + plugin.getDataFolder().getAbsolutePath());
                    }
                }

                boolean created = file.createNewFile();
                if (created) {
                    plugin.getLogger().info("prefixes.yml wurde neu erstellt.");
                } else {
                    plugin.getLogger().info("prefixes.yml exists or could not be created (createNewFile returned false)");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Konnte prefixes.yml nicht erstellen!");
                plugin.getLogger().log(Level.SEVERE, "Exception while creating prefixes.yml", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveData() {
        if (config == null || file == null) return;
        try {
            config.save(file);
            plugin.getLogger().info("prefixes.yml gespeichert.");
        } catch (IOException e) {
            plugin.getLogger().severe("Konnte prefixes.yml nicht speichern!");
            plugin.getLogger().log(Level.SEVERE, "Exception while saving prefixes.yml", e);
        }
    }
}
