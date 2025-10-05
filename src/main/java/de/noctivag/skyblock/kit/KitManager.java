package de.noctivag.skyblock.kit;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.utils.ItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import net.kyori.adventure.text.Component;

public class KitManager {

    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, Kit> kits;
    private final Map<UUID, Map<String, Long>> cooldowns;
    private File kitFile;
    private FileConfiguration kitConfig;
    private final KitShop kitShop;

    public KitManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.kits = new HashMap<>();
        this.cooldowns = new HashMap<>();
        this.kitShop = new KitShop(SkyblockPlugin);
        loadKits();
    }

    private void loadKits() {
        kitFile = new File(SkyblockPlugin.getDataFolder(), "kits.yml");
        if (!kitFile.exists()) {
            SkyblockPlugin.saveResource("kits.yml", false);
        }
        kitConfig = YamlConfiguration.loadConfiguration(kitFile);

        ConfigurationSection kitsSection = kitConfig.getConfigurationSection("kits");
        if (kitsSection != null) {
            for (String kitName : kitsSection.getKeys(false)) {
                ConfigurationSection kitSection = kitsSection.getConfigurationSection(kitName);
                if (kitSection != null) {
                    List<ItemStack> items = new ArrayList<>();
                    // Items aus der Konfiguration laden
                    if (kitSection.contains("items")) {
                        for (String itemString : kitSection.getStringList("items")) {
                            ItemStack item = ItemUtils.parseItem(itemString);
                            if (item != null) {
                                items.add(item);
                            }
                        }
                    }

                    long cooldown = kitSection.getLong("cooldown", 0);
                    String permission = kitSection.getString("permission", "");
                    Kit kit = new Kit(kitName, items, cooldown, permission);
                    kits.put(kitName.toLowerCase(), kit);
                }
            }
        }
    }

    public void saveKits() {
        try {
            kitConfig.save(kitFile);
        } catch (IOException e) {
            SkyblockPlugin.getLogger().severe("Konnte Kits nicht speichern: " + e.getMessage());
        }
    }

    public void giveKit(Player player, String kitName) {
        Kit kit = kits.get(kitName.toLowerCase());
        if (kit == null) {
            player.sendMessage(Component.text("§cDieses Kit existiert nicht!"));
            return;
        }

        if (!kit.getPermission().isEmpty() && !player.hasPermission(kit.getPermission())) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für dieses Kit!"));
            return;
        }

        UUID playerUUID = player.getUniqueId();
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(playerUUID, k -> new HashMap<>());

        long lastUsed = playerCooldowns.getOrDefault(kitName.toLowerCase(), 0L);
        long currentTime = java.lang.System.currentTimeMillis();
        long cooldownTime = kit.getCooldown() * 1000; // Konvertiere zu Millisekunden

        if (currentTime - lastUsed < cooldownTime) {
            long remainingTime = (lastUsed + cooldownTime - currentTime) / 1000;
            player.sendMessage("§cDu musst noch " + remainingTime + " Sekunden warten!");
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Component.text("§cDein Inventar ist voll!"));
            return;
        }

        for (ItemStack item : kit.getItems()) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }

        playerCooldowns.put(kitName.toLowerCase(), currentTime);
        player.sendMessage("§aDu hast das Kit §e" + kitName + " §aerhalten!");
    }

    public Set<String> getKitNames() {
        return kits.keySet();
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    // Expose the KitShop instance used by GUI/listeners
    public KitShop getKitShop() {
        return kitShop;
    }

    // Returns remaining cooldown in seconds for the given player and kit (0 if ready)
    public long getRemainingCooldown(Player player, String kitName) {
        UUID playerUUID = player.getUniqueId();
        Map<String, Long> playerCooldowns = cooldowns.getOrDefault(playerUUID, Collections.emptyMap());
        long lastUsed = playerCooldowns.getOrDefault(kitName.toLowerCase(), 0L);
        Kit kit = getKit(kitName);
        long cooldownSeconds = kit != null ? kit.getCooldown() : 0L;
        if (lastUsed == 0 || cooldownSeconds <= 0) return 0L;
        long expireAt = lastUsed + cooldownSeconds * 1000L;
        long remainingMillis = Math.max(0, expireAt - java.lang.System.currentTimeMillis());
        return remainingMillis / 1000L;
    }

    // Force-set a cooldown for a player for a kit (sets to now)
    public void setCooldown(Player player, String kitName) {
        UUID playerUUID = player.getUniqueId();
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(playerUUID, k -> new HashMap<>());
        playerCooldowns.put(kitName.toLowerCase(), java.lang.System.currentTimeMillis());
    }
}
