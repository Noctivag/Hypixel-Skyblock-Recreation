package de.noctivag.skyblock.messages;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinMessageManager {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, String> joinMessages;
    private final File file;
    private FileConfiguration config;

    // Missing fields
    private boolean enabled = true;
    private String defaultMessage = "§7[§a+§7] §e%player% §7hat den Server betreten";

    public JoinMessageManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.joinMessages = new HashMap<>();
        this.file = new File(SkyblockPlugin.getDataFolder(), "join_messages.yml");
        loadMessages();
    }

    private void loadMessages() {
        if (!file.exists()) {
            SkyblockPlugin.saveResource("join_messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        joinMessages.clear();

        // Initialize defaults from main config if present
        try {
            Object configManager = SkyblockPlugin.getConfigManager();
            if (configManager != null) {
                // TODO: Implement proper ConfigManager interface
                // defaultMessage = ((ConfigManager) configManager).getConfig().getString("join-messages.default-message", defaultMessage);
                // enabled = ((ConfigManager) configManager).getConfig().getBoolean("join-messages.enabled", true);
            }
        } catch (Exception ignored) {
            // If SkyblockPlugin config isn't ready, keep defaults
        }

        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    String message = config.getString("messages." + key);
                    joinMessages.put(uuid, message);
                } catch (IllegalArgumentException e) {
                    SkyblockPlugin.getLogger().warning("Ungültige UUID in join_messages.yml: " + key);
                }
            }
        }
    }

    public void reload() {
        loadMessages();
    }

    public void saveData() {
        if (config == null) config = new YamlConfiguration();
        // Clear existing messages section
        config.set("messages", null);
        for (Map.Entry<UUID, String> entry : joinMessages.entrySet()) {
            config.set("messages." + entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Konnte join_messages.yml nicht speichern: " + e.getMessage());
        }
    }

    public String getJoinMessage(Player player) {
        return joinMessages.getOrDefault(player.getUniqueId(),
            // TODO: Implement proper ConfigManager interface
            // ((ConfigManager) SkyblockPlugin.getConfigManager()).getConfig().getString("join-messages.default-message",
            "&7[&a+&7] &e%player% &7hat den Server betreten")
            .replace("%player%", player.getName());
    }

    public void sendMessage(Player player) {
        if (!enabled) return;

        String message = joinMessages.getOrDefault(player.getUniqueId(), defaultMessage);
        if (message == null || message.isEmpty()) return;

        Component formattedMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(message
            .replace("%player%", player.getName())
            .replace("%displayname%", LegacyComponentSerializer.legacySection().serialize(player.displayName())));

        for (Player onlinePlayer : SkyblockPlugin.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(formattedMessage);
        }
    }

    public void setJoinMessage(Player player, String message) {
        if (message == null || message.isEmpty()) {
            joinMessages.remove(player.getUniqueId());
        } else {
            joinMessages.put(player.getUniqueId(), message);
        }
        saveData();
    }

    public void clearJoinMessage(Player player) {
        joinMessages.remove(player.getUniqueId());
        saveData();
    }

    // Convenience API used by JoinMessageCommand
    public void setCustomMessage(String playerName, String message) {
        Player p = Bukkit.getPlayer(playerName);
        if (p != null) setJoinMessage(p, message);
        // if offline, we don't support setting by name for now
    }

    public boolean hasCustomMessage(String playerName) {
        Player p = Bukkit.getPlayer(playerName);
        return p != null && joinMessages.containsKey(p.getUniqueId());
    }

    public void removeCustomMessage(String playerName) {
        Player p = Bukkit.getPlayer(playerName);
        if (p != null) clearJoinMessage(p);
    }

    public boolean isMessageDisabled(String playerName) {
        Player p = Bukkit.getPlayer(playerName);
        if (p == null) return false;
        UUID id = p.getUniqueId();
        return !config.getBoolean("enabled." + id.toString(), true);
    }

    public void setMessageEnabled(String playerName, boolean enabled) {
        Player p = Bukkit.getPlayer(playerName);
        if (p == null) return;
        UUID id = p.getUniqueId();
        if (config == null) loadMessages();
        config.set("enabled." + id.toString(), enabled);
        try {
            config.save(file);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Konnte join_messages.yml nicht speichern: " + e.getMessage());
        }
    }

    public void setDefaultMessage(String message) {
        if (config == null) loadMessages();
        config.set("default", message);
        try {
            config.save(file);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Konnte join_messages.yml nicht speichern: " + e.getMessage());
        }
    }
}
