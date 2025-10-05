package de.noctivag.skyblock.config;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.configuration.file.FileConfiguration;

public class SettingsConfig {

    private final SkyblockPluginRefactored plugin;
    private boolean hubSpawnSystemEnabled;
    private boolean rollingRestartEnabled;
    private int rollingRestartIntervalMinutes;
    private boolean debugMode;
    private boolean bazaarEnabled;
    private boolean slayerEnabled;
    private boolean dungeonsEnabled;
    private boolean magicalPowerEnabled;
    private boolean customMobsEnabled;

    public SettingsConfig(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig(); // Ensure config.yml exists
        FileConfiguration config = plugin.getConfig();

        hubSpawnSystemEnabled = config.getBoolean("settings.hub-spawn-system.enabled", true);
        rollingRestartEnabled = config.getBoolean("settings.rolling-restart.enabled", true);
        rollingRestartIntervalMinutes = config.getInt("settings.rolling-restart.interval-minutes", 60);
        debugMode = config.getBoolean("settings.debug-mode", false);
        bazaarEnabled = config.getBoolean("settings.bazaar.enabled", true);
        slayerEnabled = config.getBoolean("settings.slayer.enabled", true);
        dungeonsEnabled = config.getBoolean("settings.dungeons.enabled", true);
        magicalPowerEnabled = config.getBoolean("settings.magical-power.enabled", true);
        customMobsEnabled = config.getBoolean("settings.custom-mobs.enabled", true);

        plugin.getLogger().info("SettingsConfig loaded.");
        if (debugMode) {
            plugin.getLogger().info("  Hub Spawn System Enabled: " + hubSpawnSystemEnabled);
            plugin.getLogger().info("  Rolling Restart Enabled: " + rollingRestartEnabled);
            plugin.getLogger().info("  Rolling Restart Interval: " + rollingRestartIntervalMinutes + " minutes");
            plugin.getLogger().info("  Bazaar Enabled: " + bazaarEnabled);
            plugin.getLogger().info("  Slayer Enabled: " + slayerEnabled);
            plugin.getLogger().info("  Dungeons Enabled: " + dungeonsEnabled);
            plugin.getLogger().info("  Magical Power Enabled: " + magicalPowerEnabled);
            plugin.getLogger().info("  Custom Mobs Enabled: " + customMobsEnabled);
        }
    }

    // Getters
    public boolean isHubSpawnSystemEnabled() {
        return hubSpawnSystemEnabled;
    }

    public boolean isRollingRestartEnabled() {
        return rollingRestartEnabled;
    }

    public int getRollingRestartIntervalMinutes() {
        return rollingRestartIntervalMinutes;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isBazaarEnabled() {
        return bazaarEnabled;
    }

    public boolean isSlayerEnabled() {
        return slayerEnabled;
    }

    public boolean isDungeonsEnabled() {
        return dungeonsEnabled;
    }

    public boolean isMagicalPowerEnabled() {
        return magicalPowerEnabled;
    }

    public boolean isCustomMobsEnabled() {
        return customMobsEnabled;
    }
}
