package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Magical Power System - Advanced Accessories and Talisman Management
 * 
 * This system handles the complete accessories ecosystem including:
 * - Cumulative accessory bonuses
 * - Magical Power calculation and application
 * - Power Stone integration
 * - Passive effect management
 * - Accessory bag functionality
 * 
 * Key Features:
 * - Precise magical power calculations
 * - Global stat multipliers from power stones
 * - Passive effect tracking and application
 * - Performance-optimized accessory processing
 */
public class MagicalPowerSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAccessoryProfile> playerProfiles = new ConcurrentHashMap<>();
    private final Map<UUID, Set<AccessoryEffect>> activeEffects = new ConcurrentHashMap<>();
    
    /**
     * Accessory Rarity Enum
     */
    public enum AccessoryRarity {
        COMMON("§fCommon", 1, 1.0),
        UNCOMMON("§aUncommon", 2, 1.2),
        RARE("§9Rare", 3, 1.5),
        EPIC("§5Epic", 4, 2.0),
        LEGENDARY("§6Legendary", 5, 3.0),
        MYTHIC("§dMythic", 6, 4.0),
        DIVINE("§bDivine", 7, 5.0),
        SPECIAL("§cSpecial", 8, 6.0),
        VERY_SPECIAL("§cVery Special", 9, 8.0);
        
        private final String displayName;
        private final int magicalPowerValue;
        private final double statMultiplier;
        
        AccessoryRarity(String displayName, int magicalPowerValue, double statMultiplier) {
            this.displayName = displayName;
            this.magicalPowerValue = magicalPowerValue;
            this.statMultiplier = statMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public int getMagicalPowerValue() { return magicalPowerValue; }
        public double getStatMultiplier() { return statMultiplier; }
    }
    
    /**
     * Accessory Type Enum
     */
    public enum AccessoryType {
        TALISMAN("Talisman"),
        RING("Ring"),
        ARTIFACT("Artifact"),
        RELIC("Relic"),
        CLOAK("Cloak"),
        NECKLACE("Necklace"),
        BRACELET("Bracelet"),
        GLOVE("Glove");
        
        private final String name;
        
        AccessoryType(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
    }
    
    /**
     * Power Stone Type Enum
     */
    public enum PowerStoneType {
        BLOOD("Blood", "§4Blood", "Increases damage and critical chance"),
        COMBAT("Combat", "§cCombat", "Increases combat-related stats"),
        DEFENSE("Defense", "§aDefense", "Increases defensive capabilities"),
        SPEED("Speed", "§eSpeed", "Increases movement and attack speed"),
        INTELLIGENCE("Intelligence", "§bIntelligence", "Increases magical abilities"),
        LUCK("Luck", "§aLuck", "Increases luck and fortune"),
        FISHING("Fishing", "§9Fishing", "Increases fishing-related stats"),
        MINING("Mining", "§7Mining", "Increases mining-related stats"),
        FORAGING("Foraging", "§2Foraging", "Increases foraging-related stats"),
        FARMING("Farming", "§6Farming", "Increases farming-related stats");
        
        private final String name;
        private final String displayName;
        private final String description;
        
        PowerStoneType(String name, String displayName, String description) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Accessory Effect Enum
     */
    public enum AccessoryEffect {
        FIRE_IMMUNITY("Fire Immunity", "Immunity to fire damage"),
        WATER_BREATHING("Water Breathing", "Breathe underwater indefinitely"),
        NIGHT_VISION("Night Vision", "See in the dark"),
        POISON_IMMUNITY("Poison Immunity", "Immunity to poison effects"),
        WITHER_IMMUNITY("Wither Immunity", "Immunity to wither effects"),
        FALL_DAMAGE_IMMUNITY("Fall Damage Immunity", "No fall damage"),
        EXTENDED_POTION_DURATION("Extended Potion Duration", "Potions last longer"),
        INCREASED_XP_GAIN("Increased XP Gain", "Gain more experience"),
        INCREASED_COIN_GAIN("Increased Coin Gain", "Gain more coins"),
        INCREASED_DROP_RATE("Increased Drop Rate", "Higher chance for rare drops");
        
        private final String name;
        private final String description;
        
        AccessoryEffect(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    /**
     * Accessory Definition
     */
    public static class AccessoryDefinition {
        private final String id;
        private final String name;
        private final String displayName;
        private final AccessoryType type;
        private final AccessoryRarity rarity;
        private final Map<String, Double> stats;
        private final Set<AccessoryEffect> effects;
        private final int magicalPowerValue;
        
        public AccessoryDefinition(String id, String name, String displayName, AccessoryType type, 
                                 AccessoryRarity rarity, Map<String, Double> stats, 
                                 Set<AccessoryEffect> effects) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.type = type;
            this.rarity = rarity;
            this.stats = new HashMap<>(stats);
            this.effects = new HashSet<>(effects);
            this.magicalPowerValue = rarity.getMagicalPowerValue();
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public AccessoryType getType() { return type; }
        public AccessoryRarity getRarity() { return rarity; }
        public Map<String, Double> getStats() { return new HashMap<>(stats); }
        public Set<AccessoryEffect> getEffects() { return new HashSet<>(effects); }
        public int getMagicalPowerValue() { return magicalPowerValue; }
    }
    
    /**
     * Power Stone Configuration
     */
    public static class PowerStoneConfig {
        private final PowerStoneType type;
        private final Map<String, Double> statMultipliers;
        private final int requiredMagicalPower;
        private final String description;
        
        public PowerStoneConfig(PowerStoneType type, Map<String, Double> statMultipliers, 
                              int requiredMagicalPower, String description) {
            this.type = type;
            this.statMultipliers = new HashMap<>(statMultipliers);
            this.requiredMagicalPower = requiredMagicalPower;
            this.description = description;
        }
        
        // Getters
        public PowerStoneType getType() { return type; }
        public Map<String, Double> getStatMultipliers() { return new HashMap<>(statMultipliers); }
        public int getRequiredMagicalPower() { return requiredMagicalPower; }
        public String getDescription() { return description; }
    }
    
    // Accessory definitions
    private final Map<String, AccessoryDefinition> accessoryDefinitions = new HashMap<>();
    
    // Power stone configurations
    private final Map<PowerStoneType, PowerStoneConfig> powerStoneConfigs = new HashMap<>();
    
    public MagicalPowerSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAccessoryDefinitions();
        initializePowerStoneConfigs();
        startUpdateTask();
    }
    
    /**
     * Initialize all accessory definitions
     */
    private void initializeAccessoryDefinitions() {
        // Common Talismans
        accessoryDefinitions.put("TALISMAN_OF_POWER", new AccessoryDefinition(
            "TALISMAN_OF_POWER", "Talisman of Power", "§6Talisman of Power",
            AccessoryType.TALISMAN, AccessoryRarity.UNCOMMON,
            Map.of(
                "STRENGTH", 5.0,
                "DEFENSE", 5.0,
                "SPEED", 5.0,
                "INTELLIGENCE", 5.0
            ),
            Set.of(AccessoryEffect.INCREASED_XP_GAIN)
        ));
        
        accessoryDefinitions.put("RING_OF_LOVE", new AccessoryDefinition(
            "RING_OF_LOVE", "Ring of Love", "§cRing of Love",
            AccessoryType.RING, AccessoryRarity.RARE,
            Map.of(
                "HEALTH", 50.0,
                "DEFENSE", 10.0
            ),
            Set.of(AccessoryEffect.POISON_IMMUNITY)
        ));
        
        accessoryDefinitions.put("ARTIFACT_OF_POWER", new AccessoryDefinition(
            "ARTIFACT_OF_POWER", "Artifact of Power", "§6Artifact of Power",
            AccessoryType.ARTIFACT, AccessoryRarity.EPIC,
            Map.of(
                "STRENGTH", 15.0,
                "DEFENSE", 15.0,
                "SPEED", 15.0,
                "INTELLIGENCE", 15.0,
                "CRITICAL_CHANCE", 5.0,
                "CRITICAL_DAMAGE", 10.0
            ),
            Set.of(AccessoryEffect.INCREASED_XP_GAIN, AccessoryEffect.INCREASED_COIN_GAIN)
        ));
        
        accessoryDefinitions.put("RELIC_OF_POWER", new AccessoryDefinition(
            "RELIC_OF_POWER", "Relic of Power", "§6Relic of Power",
            AccessoryType.RELIC, AccessoryRarity.LEGENDARY,
            Map.of(
                "STRENGTH", 25.0,
                "DEFENSE", 25.0,
                "SPEED", 25.0,
                "INTELLIGENCE", 25.0,
                "CRITICAL_CHANCE", 10.0,
                "CRITICAL_DAMAGE", 20.0,
                "FEROCITY", 5.0
            ),
            Set.of(AccessoryEffect.INCREASED_XP_GAIN, AccessoryEffect.INCREASED_COIN_GAIN, 
                   AccessoryEffect.INCREASED_DROP_RATE)
        ));
        
        // Add more accessories as needed...
    }
    
    /**
     * Initialize power stone configurations
     */
    private void initializePowerStoneConfigs() {
        powerStoneConfigs.put(PowerStoneType.BLOOD, new PowerStoneConfig(
            PowerStoneType.BLOOD,
            Map.of(
                "STRENGTH", 0.1,      // 10% per 100 MP
                "CRITICAL_CHANCE", 0.05,  // 5% per 100 MP
                "CRITICAL_DAMAGE", 0.15   // 15% per 100 MP
            ),
            100, "Increases damage and critical chance"
        ));
        
        powerStoneConfigs.put(PowerStoneType.COMBAT, new PowerStoneConfig(
            PowerStoneType.COMBAT,
            Map.of(
                "STRENGTH", 0.08,     // 8% per 100 MP
                "DEFENSE", 0.06,      // 6% per 100 MP
                "CRITICAL_CHANCE", 0.04,  // 4% per 100 MP
                "CRITICAL_DAMAGE", 0.12   // 12% per 100 MP
            ),
            150, "Increases combat-related stats"
        ));
        
        powerStoneConfigs.put(PowerStoneType.DEFENSE, new PowerStoneConfig(
            PowerStoneType.DEFENSE,
            Map.of(
                "DEFENSE", 0.2,       // 20% per 100 MP
                "HEALTH", 0.1,        // 10% per 100 MP
                "SPEED", 0.05         // 5% per 100 MP
            ),
            200, "Increases defensive capabilities"
        ));
        
        powerStoneConfigs.put(PowerStoneType.SPEED, new PowerStoneConfig(
            PowerStoneType.SPEED,
            Map.of(
                "SPEED", 0.15,        // 15% per 100 MP
                "ATTACK_SPEED", 0.1,  // 10% per 100 MP
                "CRITICAL_CHANCE", 0.03   // 3% per 100 MP
            ),
            120, "Increases movement and attack speed"
        ));
        
        powerStoneConfigs.put(PowerStoneType.INTELLIGENCE, new PowerStoneConfig(
            PowerStoneType.INTELLIGENCE,
            Map.of(
                "INTELLIGENCE", 0.25, // 25% per 100 MP
                "MANA", 0.2,          // 20% per 100 MP
                "SPEED", 0.03         // 3% per 100 MP
            ),
            180, "Increases magical abilities"
        ));
        
        // Add more power stones as needed...
    }
    
    /**
     * Get player accessory profile
     */
    public PlayerAccessoryProfile getPlayerProfile(UUID playerId) {
        return playerProfiles.computeIfAbsent(playerId, k -> new PlayerAccessoryProfile(playerId));
    }
    
    /**
     * Calculate total magical power for a player
     */
    public int calculateTotalMagicalPower(UUID playerId) {
        PlayerAccessoryProfile profile = getPlayerProfile(playerId);
        return profile.getOwnedAccessories().stream()
            .mapToInt(accessoryId -> {
                AccessoryDefinition def = accessoryDefinitions.get(accessoryId);
                return def != null ? def.getMagicalPowerValue() : 0;
            })
            .sum();
    }
    
    /**
     * Calculate power stone multipliers
     */
    public Map<String, Double> calculatePowerStoneMultipliers(UUID playerId, PowerStoneType powerStone) {
        PowerStoneConfig config = powerStoneConfigs.get(powerStone);
        if (config == null) {
            return new HashMap<>();
        }
        
        int magicalPower = calculateTotalMagicalPower(playerId);
        if (magicalPower < config.getRequiredMagicalPower()) {
            return new HashMap<>();
        }
        
        Map<String, Double> multipliers = new HashMap<>();
        double powerRatio = magicalPower / 100.0; // Per 100 MP
        
        for (Map.Entry<String, Double> entry : config.getStatMultipliers().entrySet()) {
            String stat = entry.getKey();
            double baseMultiplier = entry.getValue();
            multipliers.put(stat, baseMultiplier * powerRatio);
        }
        
        return multipliers;
    }
    
    /**
     * Get all active accessory effects for a player
     */
    public Set<AccessoryEffect> getActiveEffects(UUID playerId) {
        return activeEffects.computeIfAbsent(playerId, k -> new HashSet<>());
    }
    
    /**
     * Update player accessory effects
     */
    public void updatePlayerEffects(UUID playerId) {
        PlayerAccessoryProfile profile = getPlayerProfile(playerId);
        Set<AccessoryEffect> effects = new HashSet<>();
        
        for (String accessoryId : profile.getOwnedAccessories()) {
            AccessoryDefinition def = accessoryDefinitions.get(accessoryId);
            if (def != null) {
                effects.addAll(def.getEffects());
            }
        }
        
        activeEffects.put(playerId, effects);
    }
    
    /**
     * Add accessory to player
     */
    public boolean addAccessory(UUID playerId, String accessoryId) {
        AccessoryDefinition def = accessoryDefinitions.get(accessoryId);
        if (def == null) {
            return false;
        }
        
        PlayerAccessoryProfile profile = getPlayerProfile(playerId);
        profile.addAccessory(accessoryId);
        updatePlayerEffects(playerId);
        
        return true;
    }
    
    /**
     * Remove accessory from player
     */
    public boolean removeAccessory(UUID playerId, String accessoryId) {
        PlayerAccessoryProfile profile = getPlayerProfile(playerId);
        boolean removed = profile.removeAccessory(accessoryId);
        
        if (removed) {
            updatePlayerEffects(playerId);
        }
        
        return removed;
    }
    
    /**
     * Get accessory definition
     */
    public AccessoryDefinition getAccessoryDefinition(String accessoryId) {
        return accessoryDefinitions.get(accessoryId);
    }
    
    /**
     * Get all accessory definitions
     */
    public Map<String, AccessoryDefinition> getAllAccessoryDefinitions() {
        return new HashMap<>(accessoryDefinitions);
    }
    
    /**
     * Get power stone configuration
     */
    public PowerStoneConfig getPowerStoneConfig(PowerStoneType type) {
        return powerStoneConfigs.get(type);
    }
    
    /**
     * Start update task for active effects
     */
    private void startUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID playerId = player.getUniqueId();
                    Set<AccessoryEffect> effects = getActiveEffects(playerId);
                    
                    // Apply effects to player
                    applyEffectsToPlayer(player, effects);
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Every second
    }
    
    /**
     * Apply accessory effects to player
     */
    private void applyEffectsToPlayer(Player player, Set<AccessoryEffect> effects) {
        // This would apply the actual effects to the player
        // For now, it's a placeholder implementation
        
        for (AccessoryEffect effect : effects) {
            switch (effect) {
                case FIRE_IMMUNITY:
                    // Apply fire immunity
                    break;
                case WATER_BREATHING:
                    // Apply water breathing
                    break;
                case NIGHT_VISION:
                    // Apply night vision
                    break;
                case POISON_IMMUNITY:
                    // Apply poison immunity
                    break;
                case WITHER_IMMUNITY:
                    // Apply wither immunity
                    break;
                case FALL_DAMAGE_IMMUNITY:
                    // Apply fall damage immunity
                    break;
                case EXTENDED_POTION_DURATION:
                    // Apply extended potion duration
                    break;
                case INCREASED_XP_GAIN:
                    // Apply increased XP gain
                    break;
                case INCREASED_COIN_GAIN:
                    // Apply increased coin gain
                    break;
                case INCREASED_DROP_RATE:
                    // Apply increased drop rate
                    break;
            }
        }
    }
    
    /**
     * Player Accessory Profile - stores player's accessory data
     */
    public static class PlayerAccessoryProfile {
        private final UUID playerId;
        private final Set<String> ownedAccessories;
        private final Set<String> activeAccessories;
        private final Map<String, Integer> accessoryLevels;
        
        public PlayerAccessoryProfile(UUID playerId) {
            this.playerId = playerId;
            this.ownedAccessories = new HashSet<>();
            this.activeAccessories = new HashSet<>();
            this.accessoryLevels = new HashMap<>();
        }
        
        public void addAccessory(String accessoryId) {
            ownedAccessories.add(accessoryId);
            activeAccessories.add(accessoryId);
            accessoryLevels.put(accessoryId, 1);
        }
        
        public boolean removeAccessory(String accessoryId) {
            boolean removed = ownedAccessories.remove(accessoryId);
            if (removed) {
                activeAccessories.remove(accessoryId);
                accessoryLevels.remove(accessoryId);
            }
            return removed;
        }
        
        public void setAccessoryActive(String accessoryId, boolean active) {
            if (ownedAccessories.contains(accessoryId)) {
                if (active) {
                    activeAccessories.add(accessoryId);
                } else {
                    activeAccessories.remove(accessoryId);
                }
            }
        }
        
        public void setAccessoryLevel(String accessoryId, int level) {
            if (ownedAccessories.contains(accessoryId)) {
                accessoryLevels.put(accessoryId, Math.max(1, level));
            }
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Set<String> getOwnedAccessories() { return new HashSet<>(ownedAccessories); }
        public Set<String> getActiveAccessories() { return new HashSet<>(activeAccessories); }
        public int getAccessoryLevel(String accessoryId) { return accessoryLevels.getOrDefault(accessoryId, 1); }
        public boolean ownsAccessory(String accessoryId) { return ownedAccessories.contains(accessoryId); }
        public boolean isAccessoryActive(String accessoryId) { return activeAccessories.contains(accessoryId); }
    }
}
