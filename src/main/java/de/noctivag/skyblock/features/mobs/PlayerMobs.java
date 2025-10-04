package de.noctivag.skyblock.features.mobs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.mobs.types.CompleteMobType;
import de.noctivag.skyblock.features.mobs.types.MobCategory;
import de.noctivag.skyblock.features.mobs.types.MobRarity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class PlayerMobs {
    private final UUID playerId;
    private final Map<CompleteMobType, Integer> mobKills; // Mob -> Kill Count
    private final Map<CompleteMobType, Long> lastKillTime; // Mob -> Last Kill Timestamp
    private final Map<CompleteMobType, Integer> mobDamage; // Mob -> Total Damage Dealt
    private final Map<CompleteMobType, Integer> mobDeaths; // Mob -> Death Count
    private final Map<MobCategory, Integer> categoryKills; // Category -> Total Kills
    private final Map<MobRarity, Integer> rarityKills; // Rarity -> Total Kills

    public PlayerMobs(Player player) {
        this.playerId = player.getUniqueId();
        this.mobKills = new HashMap<>();
        this.lastKillTime = new HashMap<>();
        this.mobDamage = new HashMap<>();
        this.mobDeaths = new HashMap<>();
        this.categoryKills = new HashMap<>();
        this.rarityKills = new HashMap<>();
        // Load existing data from database or file here
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Record a mob kill
     */
    public void recordMobKill(CompleteMobType mobType) {
        mobKills.put(mobType, mobKills.getOrDefault(mobType, 0) + 1);
        lastKillTime.put(mobType, System.currentTimeMillis());
        
        // Update category and rarity counts
        MobCategory category = mobType.getCategory();
        MobRarity rarity = mobType.getRarity();
        
        categoryKills.put(category, categoryKills.getOrDefault(category, 0) + 1);
        rarityKills.put(rarity, rarityKills.getOrDefault(rarity, 0) + 1);
    }

    /**
     * Record damage dealt to a mob
     */
    public void recordMobDamage(CompleteMobType mobType, int damage) {
        mobDamage.put(mobType, mobDamage.getOrDefault(mobType, 0) + damage);
    }

    /**
     * Record death by a mob
     */
    public void recordMobDeath(CompleteMobType mobType) {
        mobDeaths.put(mobType, mobDeaths.getOrDefault(mobType, 0) + 1);
    }

    /**
     * Get kill count for a specific mob
     */
    public int getMobKills(CompleteMobType mobType) {
        return mobKills.getOrDefault(mobType, 0);
    }

    /**
     * Get total damage dealt to a specific mob
     */
    public int getMobDamage(CompleteMobType mobType) {
        return mobDamage.getOrDefault(mobType, 0);
    }

    /**
     * Get death count by a specific mob
     */
    public int getMobDeaths(CompleteMobType mobType) {
        return mobDeaths.getOrDefault(mobType, 0);
    }

    /**
     * Get last kill time for a specific mob
     */
    public long getLastKillTime(CompleteMobType mobType) {
        return lastKillTime.getOrDefault(mobType, 0L);
    }

    /**
     * Get total kills in a category
     */
    public int getCategoryKills(MobCategory category) {
        return categoryKills.getOrDefault(category, 0);
    }

    /**
     * Get total kills of a rarity
     */
    public int getRarityKills(MobRarity rarity) {
        return rarityKills.getOrDefault(rarity, 0);
    }

    /**
     * Get total kills across all mobs
     */
    public int getTotalKills() {
        return mobKills.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total deaths across all mobs
     */
    public int getTotalDeaths() {
        return mobDeaths.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total damage dealt across all mobs
     */
    public int getTotalDamage() {
        return mobDamage.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get all mob kills
     */
    public Map<CompleteMobType, Integer> getAllMobKills() {
        return new HashMap<>(mobKills);
    }

    /**
     * Get all mob damage
     */
    public Map<CompleteMobType, Integer> getAllMobDamage() {
        return new HashMap<>(mobDamage);
    }

    /**
     * Get all mob deaths
     */
    public Map<CompleteMobType, Integer> getAllMobDeaths() {
        return new HashMap<>(mobDeaths);
    }

    /**
     * Get category kills
     */
    public Map<MobCategory, Integer> getCategoryKills() {
        return new HashMap<>(categoryKills);
    }

    /**
     * Get rarity kills
     */
    public Map<MobRarity, Integer> getRarityKills() {
        return new HashMap<>(rarityKills);
    }

    /**
     * Get mobs by kill count range
     */
    public List<CompleteMobType> getMobsByKillRange(int minKills, int maxKills) {
        return mobKills.entrySet().stream()
                .filter(entry -> entry.getValue() >= minKills && entry.getValue() <= maxKills)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Get most killed mob
     */
    public CompleteMobType getMostKilledMob() {
        return mobKills.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get least killed mob (with at least 1 kill)
     */
    public CompleteMobType getLeastKilledMob() {
        return mobKills.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get kill/death ratio for a specific mob
     */
    public double getKillDeathRatio(CompleteMobType mobType) {
        int kills = getMobKills(mobType);
        int deaths = getMobDeaths(mobType);
        
        if (deaths == 0) {
            return kills > 0 ? Double.POSITIVE_INFINITY : 0.0;
        }
        
        return (double) kills / deaths;
    }

    /**
     * Get overall kill/death ratio
     */
    public double getOverallKillDeathRatio() {
        int totalKills = getTotalKills();
        int totalDeaths = getTotalDeaths();
        
        if (totalDeaths == 0) {
            return totalKills > 0 ? Double.POSITIVE_INFINITY : 0.0;
        }
        
        return (double) totalKills / totalDeaths;
    }

    /**
     * Get completion percentage for a category
     */
    public double getCategoryCompletionPercentage(MobCategory category) {
        int totalMobsInCategory = CompleteMobType.getMobCountByCategory(category);
        
        if (totalMobsInCategory == 0) {
            return 0.0;
        }
        
        // Calculate based on unique mobs killed in category
        long uniqueMobsKilled = mobKills.keySet().stream()
                .filter(mob -> mob.getCategory() == category && getMobKills(mob) > 0)
                .count();
        
        return (double) uniqueMobsKilled / totalMobsInCategory * 100.0;
    }

    /**
     * Get completion percentage for a rarity
     */
    public double getRarityCompletionPercentage(MobRarity rarity) {
        int totalMobsOfRarity = CompleteMobType.getMobCountByRarity(rarity);
        
        if (totalMobsOfRarity == 0) {
            return 0.0;
        }
        
        // Calculate based on unique mobs killed of rarity
        long uniqueMobsKilled = mobKills.keySet().stream()
                .filter(mob -> mob.getRarity() == rarity && getMobKills(mob) > 0)
                .count();
        
        return (double) uniqueMobsKilled / totalMobsOfRarity * 100.0;
    }

    /**
     * Get overall completion percentage
     */
    public double getOverallCompletionPercentage() {
        int totalMobs = CompleteMobType.getTotalMobCount();
        long uniqueMobsKilled = mobKills.keySet().stream()
                .filter(mob -> getMobKills(mob) > 0)
                .count();
        
        return (double) uniqueMobsKilled / totalMobs * 100.0;
    }

    /**
     * Get statistics summary
     */
    public String getStatisticsSummary() {
        return String.format(
                "Total Kills: %d | Total Deaths: %d | K/D Ratio: %.2f | Completion: %.1f%%",
                getTotalKills(),
                getTotalDeaths(),
                getOverallKillDeathRatio(),
                getOverallCompletionPercentage()
        );
    }
}
