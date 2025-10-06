package de.noctivag.skyblock.minions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a player's minion data and management
 */
public class PlayerMinions {
    private final UUID playerId;
    private final Map<String, PlacedMinion> placedMinions;
    private final Map<MinionType, Integer> minionCounts;
    private final int maxMinionSlots;
    private final long lastUpdated;

    public PlayerMinions(UUID playerId) {
        this.playerId = playerId;
        this.placedMinions = new ConcurrentHashMap<>();
        this.minionCounts = new ConcurrentHashMap<>();
        this.maxMinionSlots = 5; // Default 5 minion slots
        this.lastUpdated = System.currentTimeMillis();
        
        // Initialize minion counts
        for (MinionType minionType : MinionType.values()) {
            minionCounts.put(minionType, 0);
        }
    }

    public PlayerMinions(UUID playerId, Map<String, PlacedMinion> placedMinions, 
                        Map<MinionType, Integer> minionCounts, int maxMinionSlots) {
        this.playerId = playerId;
        this.placedMinions = new ConcurrentHashMap<>(placedMinions);
        this.minionCounts = new ConcurrentHashMap<>(minionCounts);
        this.maxMinionSlots = maxMinionSlots;
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * Place a minion
     */
    public boolean placeMinion(PlacedMinion minion) {
        if (getTotalPlacedMinions() >= maxMinionSlots) {
            return false; // No more minion slots available
        }
        
        String minionId = minion.getMinionId();
        if (placedMinions.containsKey(minionId)) {
            return false; // Minion already placed
        }
        
        placedMinions.put(minionId, minion);
        return true;
    }

    /**
     * Remove a minion
     */
    public boolean removeMinion(String minionId) {
        PlacedMinion removed = placedMinions.remove(minionId);
        return removed != null;
    }

    /**
     * Get a placed minion by ID
     */
    public PlacedMinion getPlacedMinion(String minionId) {
        return placedMinions.get(minionId);
    }

    /**
     * Get all placed minions
     */
    public Collection<PlacedMinion> getAllPlacedMinions() {
        return new ArrayList<>(placedMinions.values());
    }

    /**
     * Get placed minions by type
     */
    public List<PlacedMinion> getPlacedMinionsByType(MinionType minionType) {
        return placedMinions.values().stream()
                .filter(minion -> minion.getMinionTier().getMinionType() == minionType)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get total number of placed minions
     */
    public int getTotalPlacedMinions() {
        return placedMinions.size();
    }

    /**
     * Get available minion slots
     */
    public int getAvailableMinionSlots() {
        return maxMinionSlots - getTotalPlacedMinions();
    }

    /**
     * Get max minion slots
     */
    public int getMaxMinionSlots() {
        return maxMinionSlots;
    }

    /**
     * Add minion count
     */
    public void addMinionCount(MinionType minionType, int amount) {
        minionCounts.put(minionType, minionCounts.getOrDefault(minionType, 0) + amount);
    }

    /**
     * Set minion count
     */
    public void setMinionCount(MinionType minionType, int count) {
        minionCounts.put(minionType, Math.max(0, count));
    }

    /**
     * Get minion count
     */
    public int getMinionCount(MinionType minionType) {
        return minionCounts.getOrDefault(minionType, 0);
    }

    /**
     * Get total minion count across all types
     */
    public int getTotalMinionCount() {
        return minionCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get minion counts by category
     */
    public Map<MinionType, Integer> getMinionCountsByCategory(String category) {
        Map<MinionType, Integer> categoryCounts = new HashMap<>();
        for (Map.Entry<MinionType, Integer> entry : minionCounts.entrySet()) {
            if (entry.getKey().getCategory().equalsIgnoreCase(category)) {
                categoryCounts.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryCounts;
    }

    /**
     * Get total minion count by category
     */
    public int getTotalMinionCountByCategory(String category) {
        return getMinionCountsByCategory(category).values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get the most common minion type
     */
    public MinionType getMostCommonMinionType() {
        MinionType mostCommon = MinionType.COBBLESTONE_MINION;
        int maxCount = 0;
        
        for (Map.Entry<MinionType, Integer> entry : minionCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommon = entry.getKey();
            }
        }
        
        return mostCommon;
    }

    /**
     * Get the least common minion type
     */
    public MinionType getLeastCommonMinionType() {
        MinionType leastCommon = MinionType.COBBLESTONE_MINION;
        int minCount = Integer.MAX_VALUE;
        
        for (Map.Entry<MinionType, Integer> entry : minionCounts.entrySet()) {
            if (entry.getValue() < minCount && entry.getValue() > 0) {
                minCount = entry.getValue();
                leastCommon = entry.getKey();
            }
        }
        
        return leastCommon;
    }

    /**
     * Get minion efficiency (total production rate)
     */
    public double getTotalMinionEfficiency() {
        double totalEfficiency = 0.0;
        for (PlacedMinion minion : placedMinions.values()) {
            totalEfficiency += minion.getProductionRate();
        }
        return totalEfficiency;
    }

    /**
     * Get minion efficiency by category
     */
    public double getMinionEfficiencyByCategory(String category) {
        double efficiency = 0.0;
        for (PlacedMinion minion : placedMinions.values()) {
            if (minion.getMinionTier().getMinionType().getCategory().equalsIgnoreCase(category)) {
                efficiency += minion.getProductionRate();
            }
        }
        return efficiency;
    }

    /**
     * Get estimated daily production
     */
    public double getEstimatedDailyProduction() {
        return getTotalMinionEfficiency() * 24; // 24 hours in a day
    }

    /**
     * Get estimated weekly production
     */
    public double getEstimatedWeeklyProduction() {
        return getEstimatedDailyProduction() * 7; // 7 days in a week
    }

    /**
     * Get estimated monthly production
     */
    public double getEstimatedMonthlyProduction() {
        return getEstimatedWeeklyProduction() * 4; // 4 weeks in a month
    }

    /**
     * Get minion statistics
     */
    public MinionStatistics getMinionStatistics() {
        return new MinionStatistics(this);
    }

    /**
     * Check if player can place more minions
     */
    public boolean canPlaceMoreMinions() {
        return getAvailableMinionSlots() > 0;
    }

    /**
     * Check if player has a specific minion type
     */
    public boolean hasMinionType(MinionType minionType) {
        return getMinionCount(minionType) > 0;
    }

    /**
     * Check if player has placed a specific minion type
     */
    public boolean hasPlacedMinionType(MinionType minionType) {
        return !getPlacedMinionsByType(minionType).isEmpty();
    }

    /**
     * Get minion level distribution
     */
    public Map<Integer, Integer> getMinionLevelDistribution() {
        Map<Integer, Integer> distribution = new HashMap<>();
        for (PlacedMinion minion : placedMinions.values()) {
            int tier = minion.getMinionTier().getTier();
            distribution.put(tier, distribution.getOrDefault(tier, 0) + 1);
        }
        return distribution;
    }

    /**
     * Get minion category distribution
     */
    public Map<String, Integer> getMinionCategoryDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        for (PlacedMinion minion : placedMinions.values()) {
            String category = minion.getMinionTier().getMinionType().getCategory();
            distribution.put(category, distribution.getOrDefault(category, 0) + 1);
        }
        return distribution;
    }

    /**
     * Get all minion data as maps
     */
    public Map<String, PlacedMinion> getAllPlacedMinionsMap() {
        return new HashMap<>(placedMinions);
    }

    public Map<MinionType, Integer> getAllMinionCounts() {
        return new HashMap<>(minionCounts);
    }

    /**
     * Get last updated timestamp
     */
    public long getLastUpdated() {
        return lastUpdated;
    }

    // Getters
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Minion statistics class
     */
    public static class MinionStatistics {
        private final PlayerMinions playerMinions;
        private final int totalPlacedMinions;
        private final int totalMinionCount;
        private final double totalEfficiency;
        private final double dailyProduction;
        private final double weeklyProduction;
        private final double monthlyProduction;

        public MinionStatistics(PlayerMinions playerMinions) {
            this.playerMinions = playerMinions;
            this.totalPlacedMinions = playerMinions.getTotalPlacedMinions();
            this.totalMinionCount = playerMinions.getTotalMinionCount();
            this.totalEfficiency = playerMinions.getTotalMinionEfficiency();
            this.dailyProduction = playerMinions.getEstimatedDailyProduction();
            this.weeklyProduction = playerMinions.getEstimatedWeeklyProduction();
            this.monthlyProduction = playerMinions.getEstimatedMonthlyProduction();
        }

        // Getters
        public PlayerMinions getPlayerMinions() { return playerMinions; }
        public int getTotalPlacedMinions() { return totalPlacedMinions; }
        public int getTotalMinionCount() { return totalMinionCount; }
        public double getTotalEfficiency() { return totalEfficiency; }
        public double getDailyProduction() { return dailyProduction; }
        public double getWeeklyProduction() { return weeklyProduction; }
        public double getMonthlyProduction() { return monthlyProduction; }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Minion Statistics",
                "",
                "&7Placed Minions: &a" + totalPlacedMinions + "/" + playerMinions.getMaxMinionSlots(),
                "&7Total Minions: &a" + totalMinionCount,
                "&7Total Efficiency: &a" + String.format("%.2f", totalEfficiency) + "/hour",
                "&7Daily Production: &a" + String.format("%.0f", dailyProduction) + " items",
                "&7Weekly Production: &a" + String.format("%.0f", weeklyProduction) + " items",
                "&7Monthly Production: &a" + String.format("%.0f", monthlyProduction) + " items"
            };
        }
    }
}
