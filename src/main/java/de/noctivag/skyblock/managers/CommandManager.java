package de.noctivag.skyblock.managers;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final File commandsFile;
    private FileConfiguration commandsConfig;
    private final Map<UUID, Map<String, Long>> playerCooldowns = new HashMap<>();

    public CommandManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.commandsFile = new File(SkyblockPlugin.getDataFolder(), "commands.yml");
        load();
    }

    private void load() {
        if (!commandsFile.exists()) {
            try {
                commandsFile.getParentFile().mkdirs();
                commandsFile.createNewFile();
            } catch (IOException e) {
                SkyblockPlugin.getLogger().severe("Could not create commands.yml: " + e.getMessage());
            }
        }
        commandsConfig = YamlConfiguration.loadConfiguration(commandsFile);
        if (!commandsConfig.contains("commands")) {
            // Default command settings
            commandsConfig.set("commands.tpa.cooldown", 30);
            commandsConfig.set("commands.tpa.cost", 0);
            commandsConfig.set("commands.tpa.enabled", true);
            commandsConfig.set("commands.rtp.cooldown", 60);
            commandsConfig.set("commands.rtp.cost", 100);
            commandsConfig.set("commands.rtp.enabled", true);
            commandsConfig.set("commands.back.cooldown", 5);
            commandsConfig.set("commands.back.cost", 0);
            commandsConfig.set("commands.back.enabled", true);
            save();
        }
    }

    public void save() {
        try {
            commandsConfig.save(commandsFile);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Could not save commands.yml: " + e.getMessage());
        }
    }

    public boolean isCommandEnabled(String command) {
        return commandsConfig.getBoolean("commands." + command + ".enabled", true);
    }

    public int getCooldown(String command) {
        return commandsConfig.getInt("commands." + command + ".cooldown", 0);
    }

    public int getCost(String command) {
        return commandsConfig.getInt("commands." + command + ".cost", 0);
    }

    public void setCooldown(String command, int seconds) {
        commandsConfig.set("commands." + command + ".cooldown", seconds);
        save();
    }

    public void setCost(String command, int cost) {
        commandsConfig.set("commands." + command + ".cost", cost);
        save();
    }

    public void setEnabled(String command, boolean enabled) {
        commandsConfig.set("commands." + command + ".enabled", enabled);
        save();
    }

    public boolean isOnCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        Map<String, Long> cooldowns = playerCooldowns.get(uuid);
        if (cooldowns == null) return false;

        Long lastUsed = cooldowns.get(command);
        if (lastUsed == null) return false;

        int cooldownSeconds = getCooldown(command);
        if (cooldownSeconds <= 0) return false;

        long timePassed = (java.lang.System.currentTimeMillis() - lastUsed) / 1000;
        return timePassed < cooldownSeconds;
    }

    public int getRemainingCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        Map<String, Long> cooldowns = playerCooldowns.get(uuid);
        if (cooldowns == null) return 0;

        Long lastUsed = cooldowns.get(command);
        if (lastUsed == null) return 0;

        int cooldownSeconds = getCooldown(command);
        if (cooldownSeconds <= 0) return 0;

        long timePassed = (java.lang.System.currentTimeMillis() - lastUsed) / 1000;
        return Math.max(0, (int) (cooldownSeconds - timePassed));
    }

    public void setCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        playerCooldowns.computeIfAbsent(uuid, k -> new HashMap<>()).put(command, java.lang.System.currentTimeMillis());
    }

    public void clearCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        Map<String, Long> cooldowns = playerCooldowns.get(uuid);
        if (cooldowns != null) {
            cooldowns.remove(command);
        }
    }
}
