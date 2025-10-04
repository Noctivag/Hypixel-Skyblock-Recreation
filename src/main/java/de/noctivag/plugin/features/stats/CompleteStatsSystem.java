package de.noctivag.plugin.features.stats;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import de.noctivag.plugin.features.stats.types.PrimaryStat;
import de.noctivag.plugin.features.stats.types.SecondaryStat;
import de.noctivag.plugin.features.stats.types.StatCategory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete Stats System with all primary and secondary stats from Hypixel Skyblock
 */
public class CompleteStatsSystem implements Service {
    
    private final Map<UUID, PlayerStats> playerStats = new ConcurrentHashMap<>();
    private final Map<PrimaryStat, StatConfig> primaryStatConfigs = new ConcurrentHashMap<>();
    private final Map<SecondaryStat, StatConfig> secondaryStatConfigs = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all stat configurations
            initializePrimaryStats();
            initializeSecondaryStats();
            
            // Load player stats from database
            loadPlayerStats();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save player stats to database
            savePlayerStats();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.INITIALIZED || status == SystemStatus.ENABLED;
    }
    
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "CompleteStatsSystem";
    }
    
    /**
     * Initialize all primary stats
     */
    private void initializePrimaryStats() {
        // Health
        primaryStatConfigs.put(PrimaryStat.HEALTH, new StatConfig(
            PrimaryStat.HEALTH, "Health", "❤️", "Lebenspunkte", StatCategory.COMBAT,
            100.0, 10000.0, "Base + Equipment + Skills", "Increases maximum health"
        ));
        
        // Defense
        primaryStatConfigs.put(PrimaryStat.DEFENSE, new StatConfig(
            PrimaryStat.DEFENSE, "Defense", "🛡️", "Schadensreduktion", StatCategory.COMBAT,
            0.0, 1000.0, "Base + Armor + Accessories", "Reduces incoming damage"
        ));
        
        // Strength
        primaryStatConfigs.put(PrimaryStat.STRENGTH, new StatConfig(
            PrimaryStat.STRENGTH, "Strength", "💪", "Nahkampfschaden", StatCategory.COMBAT,
            0.0, 2000.0, "Base + Weapons + Accessories", "Increases melee damage"
        ));
        
        // Intelligence
        primaryStatConfigs.put(PrimaryStat.INTELLIGENCE, new StatConfig(
            PrimaryStat.INTELLIGENCE, "Intelligence", "🧠", "Mana-Kapazität", StatCategory.MAGIC,
            100.0, 5000.0, "Base + Equipment + Skills", "Increases maximum mana"
        ));
        
        // Speed
        primaryStatConfigs.put(PrimaryStat.SPEED, new StatConfig(
            PrimaryStat.SPEED, "Speed", "🏃", "Bewegungsgeschwindigkeit", StatCategory.UTILITY,
            100.0, 500.0, "Base + Equipment + Potions", "Increases movement speed"
        ));
        
        // Crit Chance
        primaryStatConfigs.put(PrimaryStat.CRIT_CHANCE, new StatConfig(
            PrimaryStat.CRIT_CHANCE, "Crit Chance", "⚡", "Kritische Trefferchance", StatCategory.COMBAT,
            0.0, 100.0, "Base + Equipment + Pets", "Chance for critical hits"
        ));
        
        // Crit Damage
        primaryStatConfigs.put(PrimaryStat.CRIT_DAMAGE, new StatConfig(
            PrimaryStat.CRIT_DAMAGE, "Crit Damage", "💥", "Kritischer Schadensmultiplikator", StatCategory.COMBAT,
            0.0, 1000.0, "Base + Equipment + Pets", "Critical hit damage multiplier"
        ));
        
        // Attack Speed
        primaryStatConfigs.put(PrimaryStat.ATTACK_SPEED, new StatConfig(
            PrimaryStat.ATTACK_SPEED, "Attack Speed", "⚔️", "Angriffsgeschwindigkeit", StatCategory.COMBAT,
            0.0, 100.0, "Base + Equipment", "Increases attack speed"
        ));
        
        // Ferocity
        primaryStatConfigs.put(PrimaryStat.FEROCITY, new StatConfig(
            PrimaryStat.FEROCITY, "Ferocity", "🔥", "Mehrfachangriffe", StatCategory.COMBAT,
            0.0, 200.0, "Base + Equipment + Pets", "Chance for multiple attacks"
        ));
    }
    
    /**
     * Initialize all secondary stats
     */
    private void initializeSecondaryStats() {
        // Mining Speed
        secondaryStatConfigs.put(SecondaryStat.MINING_SPEED, new StatConfig(
            SecondaryStat.MINING_SPEED, "Mining Speed", "⛏️", "Abbaugeschwindigkeit", StatCategory.MINING,
            0.0, 10000.0, "Equipment + Skills + Pets", "Increases mining efficiency"
        ));
        
        // Mining Fortune
        secondaryStatConfigs.put(SecondaryStat.MINING_FORTUNE, new StatConfig(
            SecondaryStat.MINING_FORTUNE, "Mining Fortune", "💎", "Mehrfache Drops", StatCategory.MINING,
            0.0, 1000.0, "Equipment + Skills + Pets", "Chance for multiple mining drops"
        ));
        
        // Farming Fortune
        secondaryStatConfigs.put(SecondaryStat.FARMING_FORTUNE, new StatConfig(
            SecondaryStat.FARMING_FORTUNE, "Farming Fortune", "🌾", "Mehrfache Ernten", StatCategory.FARMING,
            0.0, 1000.0, "Equipment + Skills + Pets", "Chance for multiple farming drops"
        ));
        
        // Sea Creature Chance
        secondaryStatConfigs.put(SecondaryStat.SEA_CREATURE_CHANCE, new StatConfig(
            SecondaryStat.SEA_CREATURE_CHANCE, "Sea Creature Chance", "🐙", "Meereskreatur-Spawn-Chance", StatCategory.FISHING,
            0.0, 100.0, "Equipment + Skills + Pets", "Chance to catch sea creatures"
        ));
        
        // Pet Luck
        secondaryStatConfigs.put(SecondaryStat.PET_LUCK, new StatConfig(
            SecondaryStat.PET_LUCK, "Pet Luck", "🐾", "Pet-Drop-Chance", StatCategory.PETS,
            0.0, 100.0, "Equipment + Skills + Pets", "Chance to obtain pets"
        ));
        
        // Magic Find
        secondaryStatConfigs.put(SecondaryStat.MAGIC_FIND, new StatConfig(
            SecondaryStat.MAGIC_FIND, "Magic Find", "✨", "Seltene Drop-Chance", StatCategory.COMBAT,
            0.0, 1000.0, "Equipment + Skills + Pets", "Chance for rare drops"
        ));
        
        // True Defense
        secondaryStatConfigs.put(SecondaryStat.TRUE_DEFENSE, new StatConfig(
            SecondaryStat.TRUE_DEFENSE, "True Defense", "🛡️", "Unumgehbare Verteidigung", StatCategory.COMBAT,
            0.0, 1000.0, "Equipment + Skills + Pets", "Defense that cannot be ignored"
        ));
        
        // Ability Damage
        secondaryStatConfigs.put(SecondaryStat.ABILITY_DAMAGE, new StatConfig(
            SecondaryStat.ABILITY_DAMAGE, "Ability Damage", "🔮", "Fähigkeits-Schaden", StatCategory.MAGIC,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases ability damage"
        ));
        
        // True Damage
        secondaryStatConfigs.put(SecondaryStat.TRUE_DAMAGE, new StatConfig(
            SecondaryStat.TRUE_DAMAGE, "True Damage", "💀", "Unumgehbarer Schaden", StatCategory.COMBAT,
            0.0, 1000.0, "Equipment + Skills + Pets", "Damage that ignores defense"
        ));
        
        // Mining Wisdom
        secondaryStatConfigs.put(SecondaryStat.MINING_WISDOM, new StatConfig(
            SecondaryStat.MINING_WISDOM, "Mining Wisdom", "📚", "Mining-XP-Bonus", StatCategory.MINING,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases mining experience gain"
        ));
        
        // Farming Wisdom
        secondaryStatConfigs.put(SecondaryStat.FARMING_WISDOM, new StatConfig(
            SecondaryStat.FARMING_WISDOM, "Farming Wisdom", "📚", "Farming-XP-Bonus", StatCategory.FARMING,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases farming experience gain"
        ));
        
        // Combat Wisdom
        secondaryStatConfigs.put(SecondaryStat.COMBAT_WISDOM, new StatConfig(
            SecondaryStat.COMBAT_WISDOM, "Combat Wisdom", "📚", "Combat-XP-Bonus", StatCategory.COMBAT,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases combat experience gain"
        ));
        
        // Foraging Wisdom
        secondaryStatConfigs.put(SecondaryStat.FORAGING_WISDOM, new StatConfig(
            SecondaryStat.FORAGING_WISDOM, "Foraging Wisdom", "📚", "Foraging-XP-Bonus", StatCategory.FORAGING,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases foraging experience gain"
        ));
        
        // Fishing Wisdom
        secondaryStatConfigs.put(SecondaryStat.FISHING_WISDOM, new StatConfig(
            SecondaryStat.FISHING_WISDOM, "Fishing Wisdom", "📚", "Fishing-XP-Bonus", StatCategory.FISHING,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases fishing experience gain"
        ));
        
        // Enchanting Wisdom
        secondaryStatConfigs.put(SecondaryStat.ENCHANTING_WISDOM, new StatConfig(
            SecondaryStat.ENCHANTING_WISDOM, "Enchanting Wisdom", "📚", "Enchanting-XP-Bonus", StatCategory.MAGIC,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases enchanting experience gain"
        ));
        
        // Alchemy Wisdom
        secondaryStatConfigs.put(SecondaryStat.ALCHEMY_WISDOM, new StatConfig(
            SecondaryStat.ALCHEMY_WISDOM, "Alchemy Wisdom", "📚", "Alchemy-XP-Bonus", StatCategory.MAGIC,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases alchemy experience gain"
        ));
        
        // Taming Wisdom
        secondaryStatConfigs.put(SecondaryStat.TAMING_WISDOM, new StatConfig(
            SecondaryStat.TAMING_WISDOM, "Taming Wisdom", "📚", "Taming-XP-Bonus", StatCategory.PETS,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases taming experience gain"
        ));
        
        // Dungeoneering Wisdom
        secondaryStatConfigs.put(SecondaryStat.DUNGEONEERING_WISDOM, new StatConfig(
            SecondaryStat.DUNGEONEERING_WISDOM, "Dungeoneering Wisdom", "📚", "Dungeoneering-XP-Bonus", StatCategory.DUNGEONS,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases dungeoneering experience gain"
        ));
        
        // Social Wisdom
        secondaryStatConfigs.put(SecondaryStat.SOCIAL_WISDOM, new StatConfig(
            SecondaryStat.SOCIAL_WISDOM, "Social Wisdom", "📚", "Social-XP-Bonus", StatCategory.SOCIAL,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases social experience gain"
        ));
        
        // Carpentry Wisdom
        secondaryStatConfigs.put(SecondaryStat.CARPENTRY_WISDOM, new StatConfig(
            SecondaryStat.CARPENTRY_WISDOM, "Carpentry Wisdom", "📚", "Carpentry-XP-Bonus", StatCategory.CRAFTING,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases carpentry experience gain"
        ));
        
        // Runecrafting Wisdom
        secondaryStatConfigs.put(SecondaryStat.RUNECRAFTING_WISDOM, new StatConfig(
            SecondaryStat.RUNECRAFTING_WISDOM, "Runecrafting Wisdom", "📚", "Runecrafting-XP-Bonus", StatCategory.MAGIC,
            0.0, 500.0, "Equipment + Skills + Pets", "Increases runecrafting experience gain"
        ));
    }
    
    /**
     * Get player stats
     */
    public PlayerStats getPlayerStats(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, k -> new PlayerStats(playerId));
    }
    
    /**
     * Calculate damage
     */
    public double calculateDamage(PlayerStats stats, double weaponDamage) {
        double strength = stats.getStat(PrimaryStat.STRENGTH);
        double critDamage = stats.getStat(PrimaryStat.CRIT_DAMAGE);
        double critChance = stats.getStat(PrimaryStat.CRIT_CHANCE);
        
        double baseDamage = (5 + weaponDamage + strength / 5) * (1 + strength / 100);
        
        // Apply critical hit
        if (Math.random() < critChance / 100) {
            baseDamage *= (1 + critDamage / 100);
        }
        
        return baseDamage;
    }
    
    /**
     * Calculate damage reduction
     */
    public double calculateDamageReduction(PlayerStats stats) {
        double defense = stats.getStat(PrimaryStat.DEFENSE);
        return defense / (defense + 100);
    }
    
    /**
     * Get stat configuration
     */
    public StatConfig getStatConfig(PrimaryStat stat) {
        return primaryStatConfigs.get(stat);
    }
    
    public StatConfig getStatConfig(SecondaryStat stat) {
        return secondaryStatConfigs.get(stat);
    }
    
    /**
     * Get all primary stats
     */
    public Map<PrimaryStat, StatConfig> getAllPrimaryStats() {
        return new HashMap<>(primaryStatConfigs);
    }
    
    /**
     * Get all secondary stats
     */
    public Map<SecondaryStat, StatConfig> getAllSecondaryStats() {
        return new HashMap<>(secondaryStatConfigs);
    }
    
    /**
     * Get stats by category
     */
    public List<StatConfig> getStatsByCategory(StatCategory category) {
        List<StatConfig> stats = new ArrayList<>();
        
        primaryStatConfigs.values().stream()
            .filter(config -> config.getCategory() == category)
            .forEach(stats::add);
        
        secondaryStatConfigs.values().stream()
            .filter(config -> config.getCategory() == category)
            .forEach(stats::add);
        
        return stats;
    }
    
    private void loadPlayerStats() {
        // TODO: Load from database
    }
    
    private void savePlayerStats() {
        // TODO: Save to database
    }
}
