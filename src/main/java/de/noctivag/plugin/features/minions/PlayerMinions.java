package de.noctivag.plugin.features.minions;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.minions.types.CompleteMinionType;
import de.noctivag.plugin.features.minions.types.MinionCategory;
import de.noctivag.plugin.features.minions.types.MinionRarity;
import de.noctivag.plugin.features.minions.upgrades.MinionUpgrade;
import de.noctivag.plugin.features.minions.upgrades.UpgradeType;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerMinions {
    private final UUID playerId;
    private final Map<CompleteMinionType, Integer> ownedMinions; // Minion -> Count
    private final Map<CompleteMinionType, Integer> minionLevels; // Minion -> Level
    private final Map<CompleteMinionType, List<MinionUpgrade>> minionUpgrades; // Minion -> Upgrades
    private final Map<CompleteMinionType, Long> lastCollectionTime; // Minion -> Last Collection Timestamp
    private final Map<CompleteMinionType, Integer> totalProduction; // Minion -> Total Items Produced
    private final Map<MinionCategory, Integer> categoryLevels; // Category -> Total Level
    private final Map<MinionRarity, Integer> rarityCounts; // Rarity -> Count
    private int totalMinionSlots;

    public PlayerMinions(Player player) {
        this.playerId = player.getUniqueId();
        this.ownedMinions = new HashMap<>();
        this.minionLevels = new HashMap<>();
        this.minionUpgrades = new HashMap<>();
        this.lastCollectionTime = new HashMap<>();
        this.totalProduction = new HashMap<>();
        this.categoryLevels = new HashMap<>();
        this.rarityCounts = new HashMap<>();
        this.totalMinionSlots = 5; // Default 5 slots
        // Load existing data from database or file here
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Add a minion to the player's collection
     */
    public void addMinion(CompleteMinionType minionType, int count) {
        ownedMinions.put(minionType, ownedMinions.getOrDefault(minionType, 0) + count);
        
        // Update category and rarity counts
        MinionCategory category = minionType.getCategory();
        MinionRarity rarity = minionType.getRarity();
        
        categoryLevels.put(category, categoryLevels.getOrDefault(category, 0) + count);
        rarityCounts.put(rarity, rarityCounts.getOrDefault(rarity, 0) + count);
    }

    /**
     * Remove a minion from the player's collection
     */
    public void removeMinion(CompleteMinionType minionType, int count) {
        int currentCount = ownedMinions.getOrDefault(minionType, 0);
        int newCount = Math.max(0, currentCount - count);
        
        if (newCount == 0) {
            ownedMinions.remove(minionType);
            minionLevels.remove(minionType);
            minionUpgrades.remove(minionType);
            lastCollectionTime.remove(minionType);
            totalProduction.remove(minionType);
        } else {
            ownedMinions.put(minionType, newCount);
        }
        
        // Update category and rarity counts
        MinionCategory category = minionType.getCategory();
        MinionRarity rarity = minionType.getRarity();
        
        categoryLevels.put(category, Math.max(0, categoryLevels.getOrDefault(category, 0) - count));
        rarityCounts.put(rarity, Math.max(0, rarityCounts.getOrDefault(rarity, 0) - count));
    }

    /**
     * Upgrade a minion level
     */
    public void upgradeMinionLevel(CompleteMinionType minionType) {
        int currentLevel = getMinionLevel(minionType);
        minionLevels.put(minionType, currentLevel + 1);
    }

    /**
     * Add upgrade to a minion
     */
    public void addMinionUpgrade(CompleteMinionType minionType, MinionUpgrade upgrade) {
        minionUpgrades.computeIfAbsent(minionType, k -> new ArrayList<>()).add(upgrade);
    }

    /**
     * Remove upgrade from a minion
     */
    public void removeMinionUpgrade(CompleteMinionType minionType, UpgradeType upgradeType) {
        List<MinionUpgrade> upgrades = minionUpgrades.get(minionType);
        if (upgrades != null) {
            upgrades.removeIf(upgrade -> upgrade.getType() == upgradeType);
        }
    }

    /**
     * Record minion collection
     */
    public void recordCollection(CompleteMinionType minionType, int amount) {
        lastCollectionTime.put(minionType, System.currentTimeMillis());
        totalProduction.put(minionType, totalProduction.getOrDefault(minionType, 0) + amount);
    }

    /**
     * Get owned count for a specific minion
     */
    public int getMinionCount(CompleteMinionType minionType) {
        return ownedMinions.getOrDefault(minionType, 0);
    }

    /**
     * Get level for a specific minion
     */
    public int getMinionLevel(CompleteMinionType minionType) {
        return minionLevels.getOrDefault(minionType, 1);
    }

    /**
     * Get upgrades for a specific minion
     */
    public List<MinionUpgrade> getMinionUpgrades(CompleteMinionType minionType) {
        return minionUpgrades.getOrDefault(minionType, new ArrayList<>());
    }

    /**
     * Get last collection time for a specific minion
     */
    public long getLastCollectionTime(CompleteMinionType minionType) {
        return lastCollectionTime.getOrDefault(minionType, 0L);
    }

    /**
     * Get total production for a specific minion
     */
    public int getTotalProduction(CompleteMinionType minionType) {
        return totalProduction.getOrDefault(minionType, 0);
    }

    /**
     * Get total minion slots
     */
    public int getTotalMinionSlots() {
        return totalMinionSlots;
    }

    /**
     * Set total minion slots
     */
    public void setTotalMinionSlots(int slots) {
        this.totalMinionSlots = Math.max(5, slots); // Minimum 5 slots
    }

    /**
     * Add minion slots
     */
    public void addMinionSlots(int slots) {
        this.totalMinionSlots += slots;
    }

    /**
     * Get used minion slots
     */
    public int getUsedMinionSlots() {
        return ownedMinions.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get available minion slots
     */
    public int getAvailableMinionSlots() {
        return totalMinionSlots - getUsedMinionSlots();
    }

    /**
     * Check if player can place more minions
     */
    public boolean canPlaceMinion() {
        return getAvailableMinionSlots() > 0;
    }

    /**
     * Get total minions owned
     */
    public int getTotalMinionsOwned() {
        return ownedMinions.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total minion levels
     */
    public int getTotalMinionLevels() {
        return minionLevels.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total production across all minions
     */
    public int getTotalProduction() {
        return totalProduction.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get category level
     */
    public int getCategoryLevel(MinionCategory category) {
        return categoryLevels.getOrDefault(category, 0);
    }

    /**
     * Get rarity count
     */
    public int getRarityCount(MinionRarity rarity) {
        return rarityCounts.getOrDefault(rarity, 0);
    }

    /**
     * Get all owned minions
     */
    public Map<CompleteMinionType, Integer> getAllOwnedMinions() {
        return new HashMap<>(ownedMinions);
    }

    /**
     * Get all minion levels
     */
    public Map<CompleteMinionType, Integer> getAllMinionLevels() {
        return new HashMap<>(minionLevels);
    }

    /**
     * Get all minion upgrades
     */
    public Map<CompleteMinionType, List<MinionUpgrade>> getAllMinionUpgrades() {
        Map<CompleteMinionType, List<MinionUpgrade>> result = new HashMap<>();
        for (Map.Entry<CompleteMinionType, List<MinionUpgrade>> entry : minionUpgrades.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return result;
    }

    /**
     * Get minions by category
     */
    public Map<CompleteMinionType, Integer> getMinionsByCategory(MinionCategory category) {
        Map<CompleteMinionType, Integer> result = new HashMap<>();
        for (Map.Entry<CompleteMinionType, Integer> entry : ownedMinions.entrySet()) {
            if (entry.getKey().getCategory() == category) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get minions by rarity
     */
    public Map<CompleteMinionType, Integer> getMinionsByRarity(MinionRarity rarity) {
        Map<CompleteMinionType, Integer> result = new HashMap<>();
        for (Map.Entry<CompleteMinionType, Integer> entry : ownedMinions.entrySet()) {
            if (entry.getKey().getRarity() == rarity) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    /**
     * Get completion percentage for a category
     */
    public double getCategoryCompletionPercentage(MinionCategory category) {
        int playerMinions = getCategoryLevel(category);
        int totalMinionsInCategory = CompleteMinionType.getMinionCountByCategory(category);
        
        if (totalMinionsInCategory == 0) {
            return 0.0;
        }
        
        return (double) playerMinions / totalMinionsInCategory * 100.0;
    }

    /**
     * Get completion percentage for a rarity
     */
    public double getRarityCompletionPercentage(MinionRarity rarity) {
        int playerMinions = getRarityCount(rarity);
        int totalMinionsOfRarity = CompleteMinionType.getMinionCountByRarity(rarity);
        
        if (totalMinionsOfRarity == 0) {
            return 0.0;
        }
        
        return (double) playerMinions / totalMinionsOfRarity * 100.0;
    }

    /**
     * Get overall completion percentage
     */
    public double getOverallCompletionPercentage() {
        int totalMinions = CompleteMinionType.getTotalMinionCount();
        long uniqueMinionsOwned = ownedMinions.keySet().stream()
                .filter(minion -> getMinionCount(minion) > 0)
                .count();
        
        return (double) uniqueMinionsOwned / totalMinions * 100.0;
    }

    /**
     * Get most owned minion
     */
    public CompleteMinionType getMostOwnedMinion() {
        return ownedMinions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get highest level minion
     */
    public CompleteMinionType getHighestLevelMinion() {
        return minionLevels.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get most productive minion
     */
    public CompleteMinionType getMostProductiveMinion() {
        return totalProduction.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Get statistics summary
     */
    public String getStatisticsSummary() {
        return String.format(
                "Total Minions: %d | Total Levels: %d | Total Production: %d | Completion: %.1f%%",
                getTotalMinionsOwned(),
                getTotalMinionLevels(),
                getTotalProduction(),
                getOverallCompletionPercentage()
        );
    }
}
