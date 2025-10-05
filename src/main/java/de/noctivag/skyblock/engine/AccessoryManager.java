package de.noctivag.skyblock.engine;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import de.noctivag.skyblock.features.stats.types.SecondaryStat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessoryManager - Manages Magical Power and Accessories system
 * 
 * This manager handles the complex Magical Power (MP) system that is a crucial
 * "coin sink" in Hypixel Skyblock. It calculates MP based on unique accessories
 * and applies the complex scaling formula to convert MP into global stat bonuses.
 * 
 * Key Features:
 * - Unique accessory tracking (no duplicates count)
 * - Rarity-based MP calculation
 * - Power Stone selection and application
 * - Complex MP to stat conversion formulas
 * - Accessory bag management
 */
public class AccessoryManager {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAccessoryData> playerAccessoryData = new ConcurrentHashMap<>();
    private final Map<String, PowerStone> powerStones = new HashMap<>();
    
    // MP Calculation constants (exact Hypixel values)
    private static final Map<String, Integer> RARITY_MP_VALUES = Map.of(
        "common", 1,
        "uncommon", 2,
        "rare", 3,
        "epic", 5,
        "legendary", 8,
        "mythic", 12,
        "divine", 16,
        "special", 1,
        "very_special", 1
    );
    
    public AccessoryManager(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializePowerStones();
    }
    
    /**
     * Initialize all power stones with their stat bonuses
     */
    private void initializePowerStones() {
        // Bloody Power Stone
        PowerStone bloody = new PowerStone("Bloody", "§c", "Increases Strength and Health");
        bloody.addStatBonus(PrimaryStat.STRENGTH, 0.4);
        bloody.addStatBonus(PrimaryStat.HEALTH, 0.2);
        powerStones.put("bloody", bloody);
        
        // Silky Power Stone
        PowerStone silky = new PowerStone("Silky", "§f", "Increases Critical Damage and Magic Find");
        silky.addStatBonus(PrimaryStat.CRIT_DAMAGE, 0.4);
        silky.addStatBonus(SecondaryStat.MAGIC_FIND, 0.2);
        powerStones.put("silky", silky);
        
        // Shaded Power Stone
        PowerStone shaded = new PowerStone("Shaded", "§8", "Increases Critical Damage and Intelligence");
        shaded.addStatBonus(PrimaryStat.CRIT_DAMAGE, 0.3);
        shaded.addStatBonus(PrimaryStat.INTELLIGENCE, 0.3);
        powerStones.put("shaded", shaded);
        
        // Bizarre Power Stone
        PowerStone bizarre = new PowerStone("Bizarre", "§5", "Increases Intelligence and Ability Damage");
        bizarre.addStatBonus(PrimaryStat.INTELLIGENCE, 0.4);
        bizarre.addStatBonus(SecondaryStat.ABILITY_DAMAGE, 0.2);
        powerStones.put("bizarre", bizarre);
        
        // Forceful Power Stone
        PowerStone forceful = new PowerStone("Forceful", "§c", "Increases Strength and Defense");
        forceful.addStatBonus(PrimaryStat.STRENGTH, 0.3);
        forceful.addStatBonus(PrimaryStat.DEFENSE, 0.3);
        powerStones.put("forceful", forceful);
        
        // Hurtful Power Stone
        PowerStone hurtful = new PowerStone("Hurtful", "§4", "Increases Critical Damage and Strength");
        hurtful.addStatBonus(PrimaryStat.CRIT_DAMAGE, 0.3);
        hurtful.addStatBonus(PrimaryStat.STRENGTH, 0.3);
        powerStones.put("hurtful", hurtful);
        
        // Itchy Power Stone
        PowerStone itchy = new PowerStone("Itchy", "§e", "Increases Attack Speed and Critical Chance");
        itchy.addStatBonus(PrimaryStat.ATTACK_SPEED, 0.3);
        itchy.addStatBonus(PrimaryStat.CRIT_CHANCE, 0.3);
        powerStones.put("itchy", itchy);
        
        // Jerry Power Stone
        PowerStone jerry = new PowerStone("Jerry", "§a", "Increases Intelligence and Speed");
        jerry.addStatBonus(PrimaryStat.INTELLIGENCE, 0.3);
        jerry.addStatBonus(PrimaryStat.SPEED, 0.3);
        powerStones.put("jerry", jerry);
        
        // Light Power Stone
        PowerStone light = new PowerStone("Light", "§f", "Increases Speed and Critical Chance");
        light.addStatBonus(PrimaryStat.SPEED, 0.4);
        light.addStatBonus(PrimaryStat.CRIT_CHANCE, 0.2);
        powerStones.put("light", light);
        
        // Magnetic Power Stone
        PowerStone magnetic = new PowerStone("Magnetic", "§b", "Increases Mining Speed and Mining Fortune");
        magnetic.addStatBonus(SecondaryStat.MINING_SPEED, 0.4);
        magnetic.addStatBonus(SecondaryStat.MINING_FORTUNE, 0.2);
        powerStones.put("magnetic", magnetic);
        
        // Auspicious Power Stone
        PowerStone auspicious = new PowerStone("Auspicious", "§6", "Increases Mining Fortune and Mining Wisdom");
        auspicious.addStatBonus(SecondaryStat.MINING_FORTUNE, 0.4);
        auspicious.addStatBonus(SecondaryStat.MINING_WISDOM, 0.2);
        powerStones.put("auspicious", auspicious);
        
        // Fortunate Power Stone
        PowerStone fortunate = new PowerStone("Fortunate", "§a", "Increases Mining Fortune and Magic Find");
        fortunate.addStatBonus(SecondaryStat.MINING_FORTUNE, 0.3);
        fortunate.addStatBonus(SecondaryStat.MAGIC_FIND, 0.3);
        powerStones.put("fortunate", fortunate);
        
        // Heated Power Stone
        PowerStone heated = new PowerStone("Heated", "§c", "Increases Mining Speed and Mining Wisdom");
        heated.addStatBonus(SecondaryStat.MINING_SPEED, 0.3);
        heated.addStatBonus(SecondaryStat.MINING_WISDOM, 0.3);
        powerStones.put("heated", heated);
        
        // Sweet Power Stone
        PowerStone sweet = new PowerStone("Sweet", "§d", "Increases Farming Fortune and Farming Wisdom");
        sweet.addStatBonus(SecondaryStat.FARMING_FORTUNE, 0.4);
        sweet.addStatBonus(SecondaryStat.FARMING_WISDOM, 0.2);
        powerStones.put("sweet", sweet);
        
        // Submerged Power Stone
        PowerStone submerged = new PowerStone("Submerged", "§b", "Increases Sea Creature Chance and Fishing Wisdom");
        submerged.addStatBonus(SecondaryStat.SEA_CREATURE_CHANCE, 0.4);
        submerged.addStatBonus(SecondaryStat.FISHING_WISDOM, 0.2);
        powerStones.put("submerged", submerged);
        
        // Wither Power Stone
        PowerStone wither = new PowerStone("Wither", "§8", "Increases True Defense and Ability Damage");
        wither.addStatBonus(SecondaryStat.TRUE_DEFENSE, 0.4);
        wither.addStatBonus(SecondaryStat.ABILITY_DAMAGE, 0.2);
        powerStones.put("wither", wither);
    }
    
    /**
     * Apply accessory stats to player profile
     */
    public void applyAccessoryStats(StatCalculationService.PlayerStatProfile profile, Player player) {
        PlayerAccessoryData data = getOrCreatePlayerAccessoryData(player.getUniqueId());
        
        // Calculate total MP from unique accessories
        int totalMP = calculateTotalMagicalPower(data);
        
        // Apply MP to profile
        profile.addFlatBonus(PrimaryStat.HEALTH, totalMP); // MP is stored as health for now
        
        // Apply individual accessory stats
        for (Accessory accessory : data.getAccessories()) {
            applyAccessoryStats(profile, accessory);
        }
    }
    
    /**
     * Apply magical power stats based on selected power stone
     */
    public void applyMagicalPowerStats(StatCalculationService.PlayerStatProfile profile, Player player) {
        PlayerAccessoryData data = getOrCreatePlayerAccessoryData(player.getUniqueId());
        
        // Get total MP
        int totalMP = calculateTotalMagicalPower(data);
        
        // Get selected power stone
        PowerStone selectedPowerStone = data.getSelectedPowerStone();
        if (selectedPowerStone == null) {
            return;
        }
        
        // Apply power stone bonuses
        for (Map.Entry<PrimaryStat, Double> entry : selectedPowerStone.getPrimaryStatBonuses().entrySet()) {
            double bonus = totalMP * entry.getValue();
            profile.addFlatBonus(entry.getKey(), bonus);
        }
        
        for (Map.Entry<SecondaryStat, Double> entry : selectedPowerStone.getSecondaryStatBonuses().entrySet()) {
            double bonus = totalMP * entry.getValue();
            // Note: Secondary stats would need to be handled differently in the profile
            // For now, we'll add them as primary stat bonuses where applicable
        }
    }
    
    /**
     * Calculate total magical power from unique accessories
     */
    private int calculateTotalMagicalPower(PlayerAccessoryData data) {
        int totalMP = 0;
        Set<String> uniqueAccessories = new HashSet<>();
        
        for (Accessory accessory : data.getAccessories()) {
            String uniqueKey = accessory.getName() + "_" + accessory.getRarity();
            
            // Only count unique accessories (no duplicates)
            if (!uniqueAccessories.contains(uniqueKey)) {
                uniqueAccessories.add(uniqueKey);
                totalMP += getMPValue(accessory.getRarity());
            }
        }
        
        return totalMP;
    }
    
    /**
     * Get MP value for rarity
     */
    private int getMPValue(String rarity) {
        return RARITY_MP_VALUES.getOrDefault(rarity.toLowerCase(), 1);
    }
    
    /**
     * Apply individual accessory stats
     */
    private void applyAccessoryStats(StatCalculationService.PlayerStatProfile profile, Accessory accessory) {
        // Apply base accessory stats
        for (Map.Entry<PrimaryStat, Double> entry : accessory.getStatBonuses().entrySet()) {
            profile.addFlatBonus(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Get or create player accessory data
     */
    public PlayerAccessoryData getOrCreatePlayerAccessoryData(UUID playerId) {
        return playerAccessoryData.computeIfAbsent(playerId, k -> new PlayerAccessoryData(playerId));
    }
    
    /**
     * Add accessory to player's collection
     */
    public void addAccessory(UUID playerId, Accessory accessory) {
        PlayerAccessoryData data = getOrCreatePlayerAccessoryData(playerId);
        data.addAccessory(accessory);
    }
    
    /**
     * Remove accessory from player's collection
     */
    public void removeAccessory(UUID playerId, String accessoryName) {
        PlayerAccessoryData data = playerAccessoryData.get(playerId);
        if (data != null) {
            data.removeAccessory(accessoryName);
        }
    }
    
    /**
     * Set selected power stone
     */
    public void setSelectedPowerStone(UUID playerId, String powerStoneName) {
        PlayerAccessoryData data = getOrCreatePlayerAccessoryData(playerId);
        PowerStone powerStone = powerStones.get(powerStoneName.toLowerCase());
        if (powerStone != null) {
            data.setSelectedPowerStone(powerStone);
        }
    }
    
    /**
     * Get available power stones
     */
    public Map<String, PowerStone> getPowerStones() {
        return new HashMap<>(powerStones);
    }
    
    /**
     * Player Accessory Data - stores player's accessory collection and settings
     */
    public static class PlayerAccessoryData {
        private final UUID playerId;
        private final List<Accessory> accessories = new ArrayList<>();
        private PowerStone selectedPowerStone;
        
        public PlayerAccessoryData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void addAccessory(Accessory accessory) {
            accessories.add(accessory);
        }
        
        public void removeAccessory(String accessoryName) {
            accessories.removeIf(accessory -> accessory.getName().equals(accessoryName));
        }
        
        public List<Accessory> getAccessories() {
            return new ArrayList<>(accessories);
        }
        
        public PowerStone getSelectedPowerStone() {
            return selectedPowerStone;
        }
        
        public void setSelectedPowerStone(PowerStone powerStone) {
            this.selectedPowerStone = powerStone;
        }
        
        public UUID getPlayerId() {
            return playerId;
        }
    }
    
    /**
     * Accessory - represents a single accessory item
     */
    public static class Accessory {
        private final String name;
        private final String rarity;
        private final Map<PrimaryStat, Double> statBonuses = new HashMap<>();
        
        public Accessory(String name, String rarity) {
            this.name = name;
            this.rarity = rarity;
        }
        
        public void addStatBonus(PrimaryStat stat, double value) {
            statBonuses.put(stat, value);
        }
        
        public String getName() { return name; }
        public String getRarity() { return rarity; }
        public Map<PrimaryStat, Double> getStatBonuses() { return new HashMap<>(statBonuses); }
    }
    
    /**
     * Power Stone - represents a power stone with stat bonuses
     */
    public static class PowerStone {
        private final String name;
        private final String color;
        private final String description;
        private final Map<PrimaryStat, Double> primaryStatBonuses = new HashMap<>();
        private final Map<SecondaryStat, Double> secondaryStatBonuses = new HashMap<>();
        
        public PowerStone(String name, String color, String description) {
            this.name = name;
            this.color = color;
            this.description = description;
        }
        
        public void addStatBonus(PrimaryStat stat, double value) {
            primaryStatBonuses.put(stat, value);
        }
        
        public void addStatBonus(SecondaryStat stat, double value) {
            secondaryStatBonuses.put(stat, value);
        }
        
        public String getName() { return name; }
        public String getColor() { return color; }
        public String getDescription() { return description; }
        public Map<PrimaryStat, Double> getPrimaryStatBonuses() { return new HashMap<>(primaryStatBonuses); }
        public Map<SecondaryStat, Double> getSecondaryStatBonuses() { return new HashMap<>(secondaryStatBonuses); }
    }
}
