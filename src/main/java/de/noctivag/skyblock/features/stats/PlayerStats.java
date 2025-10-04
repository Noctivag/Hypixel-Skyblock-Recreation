package de.noctivag.skyblock.features.stats;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import de.noctivag.skyblock.features.stats.types.SecondaryStat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a player's stats
 */
public class PlayerStats {
    
    private final UUID playerId;
    private final Map<PrimaryStat, Double> primaryStats = new ConcurrentHashMap<>();
    private final Map<SecondaryStat, Double> secondaryStats = new ConcurrentHashMap<>();
    
    public PlayerStats(UUID playerId) {
        this.playerId = playerId;
        initializeDefaultStats();
    }
    
    /**
     * Initialize default stats
     */
    private void initializeDefaultStats() {
        // Initialize primary stats with base values
        for (PrimaryStat stat : PrimaryStat.values()) {
            primaryStats.put(stat, stat.getBaseValue());
        }
        
        // Initialize secondary stats with base values
        for (SecondaryStat stat : SecondaryStat.values()) {
            secondaryStats.put(stat, stat.getBaseValue());
        }
    }
    
    /**
     * Get primary stat value
     */
    public double getStat(PrimaryStat stat) {
        return primaryStats.getOrDefault(stat, stat.getBaseValue());
    }
    
    /**
     * Get secondary stat value
     */
    public double getStat(SecondaryStat stat) {
        return secondaryStats.getOrDefault(stat, stat.getBaseValue());
    }
    
    /**
     * Set primary stat value
     */
    public void setStat(PrimaryStat stat, double value) {
        primaryStats.put(stat, Math.max(0, Math.min(value, stat.getMaxValue())));
    }
    
    /**
     * Set secondary stat value
     */
    public void setStat(SecondaryStat stat, double value) {
        secondaryStats.put(stat, Math.max(0, Math.min(value, stat.getMaxValue())));
    }
    
    /**
     * Add to primary stat
     */
    public void addStat(PrimaryStat stat, double amount) {
        double current = getStat(stat);
        setStat(stat, current + amount);
    }
    
    /**
     * Add to secondary stat
     */
    public void addStat(SecondaryStat stat, double amount) {
        double current = getStat(stat);
        setStat(stat, current + amount);
    }
    
    /**
     * Get formatted stat display
     */
    public String getFormattedStat(PrimaryStat stat) {
        double value = getStat(stat);
        String suffix = getStatSuffix(stat);
        return stat.getColorCode() + stat.getIcon() + " " + stat.getDisplayName() + ": " + 
               String.format("%.1f", value) + suffix;
    }
    
    /**
     * Get formatted stat display
     */
    public String getFormattedStat(SecondaryStat stat) {
        double value = getStat(stat);
        String suffix = getStatSuffix(stat);
        return stat.getColorCode() + stat.getIcon() + " " + stat.getDisplayName() + ": " + 
               String.format("%.1f", value) + suffix;
    }
    
    /**
     * Get stat suffix
     */
    private String getStatSuffix(PrimaryStat stat) {
        return switch (stat) {
            case HEALTH -> " HP";
            case DEFENSE -> "";
            case STRENGTH -> "";
            case INTELLIGENCE -> " MP";
            case SPEED -> "%";
            case CRIT_CHANCE -> "%";
            case CRIT_DAMAGE -> "%";
            case ATTACK_SPEED -> "%";
            case FEROCITY -> "%";
        };
    }
    
    /**
     * Get stat suffix
     */
    private String getStatSuffix(SecondaryStat stat) {
        return switch (stat) {
            case MINING_SPEED -> "";
            case MINING_FORTUNE, FARMING_FORTUNE -> "";
            case MINING_WISDOM, FARMING_WISDOM, FISHING_WISDOM, COMBAT_WISDOM, 
                 TAMING_WISDOM, FORAGING_WISDOM, ENCHANTING_WISDOM, ALCHEMY_WISDOM, 
                 RUNECRAFTING_WISDOM, DUNGEONEERING_WISDOM, SOCIAL_WISDOM, CARPENTRY_WISDOM -> "%";
            case SEA_CREATURE_CHANCE, PET_LUCK -> "%";
            case MAGIC_FIND, TRUE_DEFENSE, ABILITY_DAMAGE, TRUE_DAMAGE -> "";
        };
    }
    
    /**
     * Get all primary stats
     */
    public Map<PrimaryStat, Double> getAllPrimaryStats() {
        return new HashMap<>(primaryStats);
    }
    
    /**
     * Get all secondary stats
     */
    public Map<SecondaryStat, Double> getAllSecondaryStats() {
        return new HashMap<>(secondaryStats);
    }
    
    /**
     * Get stats by category
     */
    public Map<String, Double> getStatsByCategory(String category) {
        Map<String, Double> stats = new HashMap<>();
        
        for (PrimaryStat stat : PrimaryStat.values()) {
            if (stat.getCategory().getDisplayName().equalsIgnoreCase(category)) {
                stats.put(stat.getDisplayName(), getStat(stat));
            }
        }
        
        for (SecondaryStat stat : SecondaryStat.values()) {
            if (stat.getCategory().getDisplayName().equalsIgnoreCase(category)) {
                stats.put(stat.getDisplayName(), getStat(stat));
            }
        }
        
        return stats;
    }
    
    /**
     * Get total stats value
     */
    public double getTotalStatsValue() {
        double total = 0;
        
        for (double value : primaryStats.values()) {
            total += value;
        }
        
        for (double value : secondaryStats.values()) {
            total += value;
        }
        
        return total;
    }
    
    /**
     * Get stats summary
     */
    public List<String> getStatsSummary() {
        List<String> summary = new ArrayList<>();
        
        summary.add("§6=== PRIMARY STATS ===");
        for (PrimaryStat stat : PrimaryStat.values()) {
            summary.add(getFormattedStat(stat));
        }
        
        summary.add("");
        summary.add("§6=== SECONDARY STATS ===");
        for (SecondaryStat stat : SecondaryStat.values()) {
            if (getStat(stat) > 0) { // Only show non-zero stats
                summary.add(getFormattedStat(stat));
            }
        }
        
        return summary;
    }
    
    // Getters
    public UUID getPlayerId() {
        return playerId;
    }
    
    @Override
    public String toString() {
        return "PlayerStats{" +
                "playerId=" + playerId +
                ", primaryStats=" + primaryStats.size() +
                ", secondaryStats=" + secondaryStats.size() +
                '}';
    }
}
