package de.noctivag.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.talismans.AdvancedTalismanSystem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AccessoryIntegrationSystem - Integration between all accessory systems
 * 
 * Features:
 * - Integrates AccessoryBagSystem with TalismanSystem
 * - Calculates total player stats from all accessories
 * - Manages accessory effects and bonuses
 * - Provides unified accessory management
 */
public class AccessoryIntegrationSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final AccessoryBagSystem accessoryBagSystem;
    private final EnrichmentSystem enrichmentSystem;
    private final AdvancedTalismanSystem talismanSystem;
    
    private final Map<UUID, PlayerAccessoryStats> playerStats = new ConcurrentHashMap<>();
    
    public AccessoryIntegrationSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                                    AccessoryBagSystem accessoryBagSystem, EnrichmentSystem enrichmentSystem,
                                    AdvancedTalismanSystem talismanSystem) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.accessoryBagSystem = accessoryBagSystem;
        this.enrichmentSystem = enrichmentSystem;
        this.talismanSystem = talismanSystem;
    }
    
    public void updatePlayerStats(UUID playerId) {
        PlayerAccessoryStats stats = playerStats.computeIfAbsent(playerId, k -> new PlayerAccessoryStats(playerId));
        
        // Get accessories from bag
        AccessoryBagSystem.AccessoryBag bag = accessoryBagSystem.getPlayerBag(playerId);
        List<String> activeAccessories = bag.getActiveAccessories();
        
        // Reset stats
        stats.reset();
        
        // Calculate stats from accessories
        for (String accessoryId : activeAccessories) {
            addAccessoryStats(stats, accessoryId);
        }
        
        // Add enrichment bonuses
        Map<EnrichmentSystem.EnrichmentType, Double> enrichmentBonuses = enrichmentSystem.getTotalEnrichmentBonuses(playerId);
        for (Map.Entry<EnrichmentSystem.EnrichmentType, Double> entry : enrichmentBonuses.entrySet()) {
            addEnrichmentBonus(stats, entry.getKey(), entry.getValue());
        }
        
        // Update magical power
        int magicalPower = accessoryBagSystem.calculateMagicalPower(activeAccessories);
        stats.setMagicalPower(magicalPower);
        
        // Calculate available accessory powers
        Map<String, Double> accessoryPowers = accessoryBagSystem.calculateAccessoryPowers(magicalPower);
        stats.setAccessoryPowers(accessoryPowers);
    }
    
    private void addAccessoryStats(PlayerAccessoryStats stats, String accessoryId) {
        // Get accessory config from bag system
        AccessoryBagSystem.AccessoryConfig config = getAccessoryConfig(accessoryId);
        if (config != null) {
        // Add base stats based on accessory type
        String category = config.getCategory();
        // Note: AccessoryConfig doesn't have getEffectValue method yet
        // This will need to be implemented in AccessoryBagSystem
            
            // Add basic stats based on category
            switch (category) {
                case "combat":
                    stats.addDamageBonus(5.0); // Default value
                    break;
                case "mining":
                    stats.addMiningSpeedBonus(5.0); // Default value
                    break;
                case "farming":
                    stats.addFarmingXPBonus(5.0); // Default value
                    break;
                case "foraging":
                    stats.addForagingXPBonus(5.0); // Default value
                    break;
                case "fishing":
                    stats.addFishingXPBonus(5.0); // Default value
                    break;
                case "misc":
                    addMiscStats(stats, config, 5.0); // Default value
                    break;
            }
        }
    }
    
    private void addMiscStats(PlayerAccessoryStats stats, AccessoryBagSystem.AccessoryConfig config, double effectValue) {
        String name = config.getName().toLowerCase();
        
        if (name.contains("speed")) {
            stats.addSpeedBonus(effectValue);
        } else if (name.contains("health")) {
            stats.addHealthBonus(effectValue);
        } else if (name.contains("defense")) {
            stats.addDefenseBonus(effectValue);
        } else if (name.contains("strength")) {
            stats.addStrengthBonus(effectValue);
        } else if (name.contains("intelligence")) {
            stats.addIntelligenceBonus(effectValue);
        } else if (name.contains("mana")) {
            stats.addManaBonus(effectValue);
        } else if (name.contains("lucky")) {
            stats.addLuckBonus(effectValue);
        } else if (name.contains("experience")) {
            stats.addXPBonus(effectValue);
        }
    }
    
    private void addEnrichmentBonus(PlayerAccessoryStats stats, EnrichmentSystem.EnrichmentType type, double bonus) {
        switch (type) {
            case DAMAGE:
                stats.addDamageBonus(bonus);
                break;
            case STRENGTH:
                stats.addStrengthBonus(bonus);
                break;
            case CRITICAL_CHANCE:
                stats.addCriticalChanceBonus(bonus);
                break;
            case CRITICAL_DAMAGE:
                stats.addCriticalDamageBonus(bonus);
                break;
            case INTELLIGENCE:
                stats.addIntelligenceBonus(bonus);
                break;
            case HEALTH:
                stats.addHealthBonus(bonus);
                break;
            case DEFENSE:
                stats.addDefenseBonus(bonus);
                break;
            case MINING_SPEED:
                stats.addMiningSpeedBonus(bonus);
                break;
            case FARMING_FORTUNE:
                stats.addFarmingFortuneBonus(bonus);
                break;
            case FORAGING_FORTUNE:
                stats.addForagingFortuneBonus(bonus);
                break;
            case MINING_FORTUNE:
                stats.addMiningFortuneBonus(bonus);
                break;
            case SPEED:
                stats.addSpeedBonus(bonus);
                break;
            case MAGIC_FIND:
                stats.addMagicFindBonus(bonus);
                break;
            case PET_LUCK:
                stats.addPetLuckBonus(bonus);
                break;
            case SEA_CREATURE_CHANCE:
                stats.addSeaCreatureChanceBonus(bonus);
                break;
        }
    }
    
    private AccessoryBagSystem.AccessoryConfig getAccessoryConfig(String accessoryId) {
        // This would get the config from AccessoryBagSystem
        // Return accessory stats from player's equipped accessories
        if (((de.noctivag.plugin.Plugin) plugin).getAccessorySystem() != null) {
            // TODO: Get player from context or parameter
            return null; // Placeholder - needs player context
        }
        return null;
    }
    
    public PlayerAccessoryStats getPlayerStats(UUID playerId) {
        updatePlayerStats(playerId);
        return playerStats.get(playerId);
    }
    
    public int getTotalMagicalPower(UUID playerId) {
        PlayerAccessoryStats stats = getPlayerStats(playerId);
        return stats.getMagicalPower();
    }
    
    public Map<String, Double> getTotalStatBonuses(UUID playerId) {
        PlayerAccessoryStats stats = getPlayerStats(playerId);
        return stats.getAllBonuses();
    }
    
    public void openUnifiedAccessoryGUI(Player player) {
        // This would open a unified GUI showing all accessories, their effects, and magical power
        accessoryBagSystem.openAccessoryBagGUI(player);
    }
    
    public static class PlayerAccessoryStats {
        private final UUID playerId;
        private int magicalPower = 0;
        private final Map<String, Double> bonuses = new HashMap<>();
        private Map<String, Double> accessoryPowers = new HashMap<>();
        
        public PlayerAccessoryStats(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void reset() {
            bonuses.clear();
            magicalPower = 0;
            accessoryPowers.clear();
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getMagicalPower() { return magicalPower; }
        public void setMagicalPower(int magicalPower) { this.magicalPower = magicalPower; }
        
        public void setAccessoryPowers(Map<String, Double> accessoryPowers) {
            this.accessoryPowers = accessoryPowers;
        }
        
        public Map<String, Double> getAccessoryPowers() {
            return accessoryPowers;
        }
        
        // Stat bonus methods
        public void addDamageBonus(double bonus) {
            bonuses.merge("damage", bonus, Double::sum);
        }
        
        public void addStrengthBonus(double bonus) {
            bonuses.merge("strength", bonus, Double::sum);
        }
        
        public void addCriticalChanceBonus(double bonus) {
            bonuses.merge("critical_chance", bonus, Double::sum);
        }
        
        public void addCriticalDamageBonus(double bonus) {
            bonuses.merge("critical_damage", bonus, Double::sum);
        }
        
        public void addIntelligenceBonus(double bonus) {
            bonuses.merge("intelligence", bonus, Double::sum);
        }
        
        public void addHealthBonus(double bonus) {
            bonuses.merge("health", bonus, Double::sum);
        }
        
        public void addDefenseBonus(double bonus) {
            bonuses.merge("defense", bonus, Double::sum);
        }
        
        public void addMiningSpeedBonus(double bonus) {
            bonuses.merge("mining_speed", bonus, Double::sum);
        }
        
        public void addFarmingXPBonus(double bonus) {
            bonuses.merge("farming_xp", bonus, Double::sum);
        }
        
        public void addForagingXPBonus(double bonus) {
            bonuses.merge("foraging_xp", bonus, Double::sum);
        }
        
        public void addFishingXPBonus(double bonus) {
            bonuses.merge("fishing_xp", bonus, Double::sum);
        }
        
        public void addFarmingFortuneBonus(double bonus) {
            bonuses.merge("farming_fortune", bonus, Double::sum);
        }
        
        public void addForagingFortuneBonus(double bonus) {
            bonuses.merge("foraging_fortune", bonus, Double::sum);
        }
        
        public void addMiningFortuneBonus(double bonus) {
            bonuses.merge("mining_fortune", bonus, Double::sum);
        }
        
        public void addSpeedBonus(double bonus) {
            bonuses.merge("speed", bonus, Double::sum);
        }
        
        public void addManaBonus(double bonus) {
            bonuses.merge("mana", bonus, Double::sum);
        }
        
        public void addLuckBonus(double bonus) {
            bonuses.merge("luck", bonus, Double::sum);
        }
        
        public void addXPBonus(double bonus) {
            bonuses.merge("xp", bonus, Double::sum);
        }
        
        public void addMagicFindBonus(double bonus) {
            bonuses.merge("magic_find", bonus, Double::sum);
        }
        
        public void addPetLuckBonus(double bonus) {
            bonuses.merge("pet_luck", bonus, Double::sum);
        }
        
        public void addSeaCreatureChanceBonus(double bonus) {
            bonuses.merge("sea_creature_chance", bonus, Double::sum);
        }
        
        public double getBonus(String stat) {
            return bonuses.getOrDefault(stat, 0.0);
        }
        
        public Map<String, Double> getAllBonuses() {
            return new HashMap<>(bonuses);
        }
    }
}
