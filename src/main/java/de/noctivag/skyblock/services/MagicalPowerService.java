package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.PowerStone;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.models.AccessoryBag;
import de.noctivag.skyblock.models.PlayerProfile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MagicalPowerService {

    private final SkyblockPluginRefactored plugin;
    private final Map<Rarity, Integer> magicalPowerPerRarity = new HashMap<>();

    public MagicalPowerService(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        loadMagicalPowerConfig();
    }

    private void loadMagicalPowerConfig() {
        File configFile = new File(plugin.getDataFolder(), "powers.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Creating default powers.yml...");
            try (InputStream in = plugin.getResource("powers.yml")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath());
                } else {
                    plugin.getLogger().warning("Default powers.yml not found in plugin resources!");
                    createDefaultPowersConfig(configFile);
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create powers.yml: " + e.getMessage());
                createDefaultPowersConfig(configFile);
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Load magical power values for each rarity
        magicalPowerPerRarity.put(Rarity.COMMON, config.getInt("magical-power.common", 1));
        magicalPowerPerRarity.put(Rarity.UNCOMMON, config.getInt("magical-power.uncommon", 2));
        magicalPowerPerRarity.put(Rarity.RARE, config.getInt("magical-power.rare", 3));
        magicalPowerPerRarity.put(Rarity.EPIC, config.getInt("magical-power.epic", 5));
        magicalPowerPerRarity.put(Rarity.LEGENDARY, config.getInt("magical-power.legendary", 8));
        magicalPowerPerRarity.put(Rarity.MYTHIC, config.getInt("magical-power.mythic", 12));
        magicalPowerPerRarity.put(Rarity.DIVINE, config.getInt("magical-power.divine", 16));
        magicalPowerPerRarity.put(Rarity.SPECIAL, config.getInt("magical-power.special", 20));
        magicalPowerPerRarity.put(Rarity.VERY_SPECIAL, config.getInt("magical-power.very-special", 25));

        plugin.getLogger().info("MagicalPowerService loaded powers configuration.");
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("  Magical Power per Rarity: " + magicalPowerPerRarity);
        }
    }

    private void createDefaultPowersConfig(File configFile) {
        try {
            configFile.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            
            config.set("magical-power.common", 1);
            config.set("magical-power.uncommon", 2);
            config.set("magical-power.rare", 3);
            config.set("magical-power.epic", 5);
            config.set("magical-power.legendary", 8);
            config.set("magical-power.mythic", 12);
            config.set("magical-power.divine", 16);
            config.set("magical-power.special", 20);
            config.set("magical-power.very-special", 25);
            
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create default powers.yml: " + e.getMessage());
        }
    }

    public int calculateTotalMagicalPower(PlayerProfile profile) {
        if (profile == null || profile.getAccessoryBag() == null) {
            return 0;
        }

        int totalMP = 0;
        AccessoryBag accessoryBag = profile.getAccessoryBag();
        
        for (Map.Entry<String, Rarity> entry : accessoryBag.getAccessories().entrySet()) {
            Rarity rarity = entry.getValue();
            int mp = magicalPowerPerRarity.getOrDefault(rarity, 0);
            totalMP += mp;
        }

        return totalMP;
    }

    public Map<String, Integer> calculateStatBonuses(PlayerProfile profile) {
        Map<String, Integer> bonuses = new HashMap<>();
        
        if (profile == null || profile.getActivePowerStone() == null) {
            return bonuses;
        }

        int totalMP = calculateTotalMagicalPower(profile);
        PowerStone powerStone = profile.getActivePowerStone();

        // Calculate bonuses based on power stone and total MP
        switch (powerStone) {
            case BERSERKER:
                bonuses.put("strength", totalMP * 2);
                bonuses.put("crit_damage", totalMP);
                break;
            case HEALER:
                bonuses.put("health", totalMP * 3);
                bonuses.put("defense", totalMP);
                break;
            case MAGE:
                bonuses.put("intelligence", totalMP * 2);
                bonuses.put("mana", totalMP * 5);
                break;
            case ARCHER:
                bonuses.put("crit_chance", totalMP);
                bonuses.put("crit_damage", totalMP * 2);
                break;
            case TANK:
                bonuses.put("defense", totalMP * 3);
                bonuses.put("health", totalMP * 2);
                break;
        }

        return bonuses;
    }

    public int getMagicalPowerForRarity(Rarity rarity) {
        return magicalPowerPerRarity.getOrDefault(rarity, 0);
    }

    public void addAccessory(PlayerProfile profile, String accessoryName, Rarity rarity) {
        if (profile == null || profile.getAccessoryBag() == null) {
            return;
        }

        profile.getAccessoryBag().addAccessory(accessoryName, rarity);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Added accessory " + accessoryName + " (" + rarity.name() + 
                ") to player " + profile.getPlayerName() + ". New MP: " + calculateTotalMagicalPower(profile));
        }
    }

    public void removeAccessory(PlayerProfile profile, String accessoryName) {
        if (profile == null || profile.getAccessoryBag() == null) {
            return;
        }

        profile.getAccessoryBag().removeAccessory(accessoryName);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Removed accessory " + accessoryName + 
                " from player " + profile.getPlayerName() + ". New MP: " + calculateTotalMagicalPower(profile));
        }
    }

    public void setPowerStone(PlayerProfile profile, PowerStone powerStone) {
        if (profile == null) {
            return;
        }

        PowerStone oldPowerStone = profile.getActivePowerStone();
        profile.setActivePowerStone(powerStone);
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Player " + profile.getPlayerName() + 
                " changed power stone from " + (oldPowerStone != null ? oldPowerStone.name() : "null") + 
                " to " + powerStone.name());
        }
    }
}