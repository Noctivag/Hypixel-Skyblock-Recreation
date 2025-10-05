package de.noctivag.skyblock.data;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {
    private final SkyblockPlugin SkyblockPlugin;
    private File file;
    private FileConfiguration config;

    public DataManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void initialize() {
        loadData();
    }

    public void loadData() {
        file = new File(SkyblockPlugin.getDataFolder(), "prefixes.yml");

        if (!file.exists()) {
            try {
                // Ensure parent directory exists
                if (!SkyblockPlugin.getDataFolder().exists()) {
                    boolean dirsCreated = SkyblockPlugin.getDataFolder().mkdirs();
                    if (!dirsCreated && !SkyblockPlugin.getDataFolder().exists()) {
                        SkyblockPlugin.getLogger().warning("Could not create SkyblockPlugin data folder: " + SkyblockPlugin.getDataFolder().getAbsolutePath());
                    }
                }

                boolean created = file.createNewFile();
                if (created) {
                    SkyblockPlugin.getLogger().info("prefixes.yml wurde neu erstellt.");
                } else {
                    SkyblockPlugin.getLogger().info("prefixes.yml exists or could not be created (createNewFile returned false)");
                }
            } catch (IOException e) {
                SkyblockPlugin.getLogger().severe("Konnte prefixes.yml nicht erstellen!");
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Exception while creating prefixes.yml", e);
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
            SkyblockPlugin.getLogger().info("prefixes.yml gespeichert.");
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Konnte prefixes.yml nicht speichern!");
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Exception while saving prefixes.yml", e);
        }
    }
}
