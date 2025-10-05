package de.noctivag.skyblock.features.weapons.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Weapons class for storing player weapon data
 */
public class PlayerWeapons {
    private UUID playerId;
    private Map<WeaponType, Integer> weaponLevels;
    private Map<WeaponType, Long> weaponExperience;
    private Map<WeaponType, Boolean> weaponUnlocked;
    private Map<WeaponType, WeaponRarity> weaponRarities;

    public PlayerWeapons(UUID playerId) {
        this.playerId = playerId;
        this.weaponLevels = new HashMap<>();
        this.weaponExperience = new HashMap<>();
        this.weaponUnlocked = new HashMap<>();
        this.weaponRarities = new HashMap<>();
        
        // Initialize all weapons as locked
        for (WeaponType weaponType : WeaponType.values()) {
            weaponLevels.put(weaponType, 0);
            weaponExperience.put(weaponType, 0L);
            weaponUnlocked.put(weaponType, false);
            weaponRarities.put(weaponType, WeaponRarity.COMMON);
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<WeaponType, Integer> getWeaponLevels() {
        return weaponLevels;
    }

    public Map<WeaponType, Long> getWeaponExperience() {
        return weaponExperience;
    }

    public Map<WeaponType, Boolean> getWeaponUnlocked() {
        return weaponUnlocked;
    }

    public Map<WeaponType, WeaponRarity> getWeaponRarities() {
        return weaponRarities;
    }

    public int getWeaponLevel(WeaponType weaponType) {
        return weaponLevels.getOrDefault(weaponType, 0);
    }

    public long getWeaponExperience(WeaponType weaponType) {
        return weaponExperience.getOrDefault(weaponType, 0L);
    }

    public boolean isWeaponUnlocked(WeaponType weaponType) {
        return weaponUnlocked.getOrDefault(weaponType, false);
    }

    public WeaponRarity getWeaponRarity(WeaponType weaponType) {
        return weaponRarities.getOrDefault(weaponType, WeaponRarity.COMMON);
    }

    public void setWeaponLevel(WeaponType weaponType, int level) {
        weaponLevels.put(weaponType, level);
    }

    public void setWeaponExperience(WeaponType weaponType, long experience) {
        weaponExperience.put(weaponType, experience);
    }

    public void setWeaponUnlocked(WeaponType weaponType, boolean unlocked) {
        weaponUnlocked.put(weaponType, unlocked);
    }

    public void setWeaponRarity(WeaponType weaponType, WeaponRarity rarity) {
        weaponRarities.put(weaponType, rarity);
    }

    public void addWeaponExperience(WeaponType weaponType, long experience) {
        long currentExp = getWeaponExperience(weaponType);
        weaponExperience.put(weaponType, currentExp + experience);
    }
}
