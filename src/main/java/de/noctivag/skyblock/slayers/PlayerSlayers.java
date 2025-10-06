package de.noctivag.skyblock.slayers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player's slayer data and progress
 */
public class PlayerSlayers {
    private final UUID playerId;
    private final Map<SlayerType, SlayerData> slayerData;
    private final long lastUpdated;

    public PlayerSlayers(UUID playerId) {
        this.playerId = playerId;
        this.slayerData = new HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
        
        // Initialize all slayer types with default data
        for (SlayerType slayerType : SlayerType.values()) {
            slayerData.put(slayerType, new SlayerData(slayerType));
        }
    }

    public PlayerSlayers(UUID playerId, Map<SlayerType, SlayerData> slayerData) {
        this.playerId = playerId;
        this.slayerData = new HashMap<>(slayerData);
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters
    public UUID getPlayerId() { return playerId; }
    public Map<SlayerType, SlayerData> getSlayerData() { return new HashMap<>(slayerData); }
    public long getLastUpdated() { return lastUpdated; }

    /**
     * Get slayer data for a specific type
     */
    public SlayerData getSlayerData(SlayerType slayerType) {
        return slayerData.getOrDefault(slayerType, new SlayerData(slayerType));
    }

    /**
     * Set slayer data for a specific type
     */
    public void setSlayerData(SlayerType slayerType, SlayerData data) {
        slayerData.put(slayerType, data);
    }

    /**
     * Get current tier for a slayer type
     */
    public int getCurrentTier(SlayerType slayerType) {
        return getSlayerData(slayerType).getCurrentTier();
    }

    /**
     * Get total XP for a slayer type
     */
    public long getTotalXP(SlayerType slayerType) {
        return getSlayerData(slayerType).getTotalXP();
    }

    /**
     * Add XP to a slayer type
     */
    public boolean addXP(SlayerType slayerType, long xp) {
        SlayerData data = getSlayerData(slayerType);
        long newXP = data.getTotalXP() + xp;
        
        // Check for tier up
        int newTier = calculateTierFromXP(slayerType, newXP);
        boolean tieredUp = newTier > data.getCurrentTier();
        
        if (tieredUp) {
            data.setCurrentTier(newTier);
        }
        
        data.setTotalXP(newXP);
        return tieredUp;
    }

    /**
     * Get XP progress for current tier
     */
    public long getXPProgress(SlayerType slayerType) {
        SlayerData data = getSlayerData(slayerType);
        int currentTier = data.getCurrentTier();
        
        if (currentTier >= slayerType.getMaxTier()) {
            return 0; // Max tier reached
        }
        
        long currentTierXP = getXPForTier(slayerType, currentTier);
        
        return data.getTotalXP() - currentTierXP;
    }

    /**
     * Get XP required for next tier
     */
    public long getXPRequiredForNextTier(SlayerType slayerType) {
        SlayerData data = getSlayerData(slayerType);
        int currentTier = data.getCurrentTier();
        
        if (currentTier >= slayerType.getMaxTier()) {
            return 0; // Max tier reached
        }
        
        long currentTierXP = getXPForTier(slayerType, currentTier);
        long nextTierXP = getXPForTier(slayerType, currentTier + 1);
        
        return nextTierXP - currentTierXP;
    }

    /**
     * Get total slayer XP across all types
     */
    public long getTotalSlayerXP() {
        return slayerData.values().stream().mapToLong(SlayerData::getTotalXP).sum();
    }

    /**
     * Get total slayer tier across all types
     */
    public int getTotalSlayerTier() {
        return slayerData.values().stream().mapToInt(SlayerData::getCurrentTier).sum();
    }

    /**
     * Get highest tier slayer
     */
    public SlayerType getHighestTierSlayer() {
        SlayerType highest = SlayerType.ZOMBIE;
        int highestTier = 0;
        
        for (Map.Entry<SlayerType, SlayerData> entry : slayerData.entrySet()) {
            if (entry.getValue().getCurrentTier() > highestTier) {
                highestTier = entry.getValue().getCurrentTier();
                highest = entry.getKey();
            }
        }
        
        return highest;
    }

    /**
     * Get lowest tier slayer
     */
    public SlayerType getLowestTierSlayer() {
        SlayerType lowest = SlayerType.ZOMBIE;
        int lowestTier = Integer.MAX_VALUE;
        
        for (Map.Entry<SlayerType, SlayerData> entry : slayerData.entrySet()) {
            if (entry.getValue().getCurrentTier() < lowestTier) {
                lowestTier = entry.getValue().getCurrentTier();
                lowest = entry.getKey();
            }
        }
        
        return lowest;
    }

    /**
     * Get slayer statistics
     */
    public SlayerStatistics getSlayerStatistics() {
        return new SlayerStatistics(this);
    }

    /**
     * Calculate tier from total XP
     */
    private int calculateTierFromXP(SlayerType slayerType, long totalXP) {
        int tier = 0;
        for (int i = 1; i <= slayerType.getMaxTier(); i++) {
            if (totalXP >= getXPForTier(slayerType, i)) {
                tier = i;
            } else {
                break;
            }
        }
        return tier;
    }

    /**
     * Get XP required for a specific tier
     */
    private long getXPForTier(SlayerType slayerType, int tier) {
        long totalXP = 0;
        for (int i = 1; i < tier; i++) {
            totalXP += slayerType.getTierRequirement(i);
        }
        return totalXP;
    }

    /**
     * Slayer data inner class
     */
    public static class SlayerData {
        private final SlayerType slayerType;
        private int currentTier;
        private long totalXP;
        private long totalQuestsCompleted;
        private long totalBossesKilled;
        private long totalDamageDealt;
        private long totalDamageTaken;
        private int totalDeaths;

        public SlayerData(SlayerType slayerType) {
            this.slayerType = slayerType;
            this.currentTier = 0;
            this.totalXP = 0;
            this.totalQuestsCompleted = 0;
            this.totalBossesKilled = 0;
            this.totalDamageDealt = 0;
            this.totalDamageTaken = 0;
            this.totalDeaths = 0;
        }

        public SlayerData(SlayerType slayerType, int currentTier, long totalXP, long totalQuestsCompleted,
                         long totalBossesKilled, long totalDamageDealt, long totalDamageTaken, int totalDeaths) {
            this.slayerType = slayerType;
            this.currentTier = currentTier;
            this.totalXP = totalXP;
            this.totalQuestsCompleted = totalQuestsCompleted;
            this.totalBossesKilled = totalBossesKilled;
            this.totalDamageDealt = totalDamageDealt;
            this.totalDamageTaken = totalDamageTaken;
            this.totalDeaths = totalDeaths;
        }

        // Getters
        public SlayerType getSlayerType() { return slayerType; }
        public int getCurrentTier() { return currentTier; }
        public long getTotalXP() { return totalXP; }
        public long getTotalQuestsCompleted() { return totalQuestsCompleted; }
        public long getTotalBossesKilled() { return totalBossesKilled; }
        public long getTotalDamageDealt() { return totalDamageDealt; }
        public long getTotalDamageTaken() { return totalDamageTaken; }
        public int getTotalDeaths() { return totalDeaths; }

        // Setters
        public void setCurrentTier(int currentTier) { this.currentTier = currentTier; }
        public void setTotalXP(long totalXP) { this.totalXP = totalXP; }
        public void setTotalQuestsCompleted(long totalQuestsCompleted) { this.totalQuestsCompleted = totalQuestsCompleted; }
        public void setTotalBossesKilled(long totalBossesKilled) { this.totalBossesKilled = totalBossesKilled; }
        public void setTotalDamageDealt(long totalDamageDealt) { this.totalDamageDealt = totalDamageDealt; }
        public void setTotalDamageTaken(long totalDamageTaken) { this.totalDamageTaken = totalDamageTaken; }
        public void setTotalDeaths(int totalDeaths) { this.totalDeaths = totalDeaths; }

        // Progress methods
        public void addQuestCompleted() { this.totalQuestsCompleted++; }
        public void addBossKilled() { this.totalBossesKilled++; }
        public void addDamageDealt(long damage) { this.totalDamageDealt += damage; }
        public void addDamageTaken(long damage) { this.totalDamageTaken += damage; }
        public void addDeath() { this.totalDeaths++; }

        /**
         * Get K/D ratio
         */
        public double getKDRatio() {
            if (totalDeaths == 0) return totalBossesKilled;
            return (double) totalBossesKilled / totalDeaths;
        }

        /**
         * Get efficiency score
         */
        public double getEfficiencyScore() {
            if (totalQuestsCompleted == 0) return 0.0;
            return (double) totalBossesKilled / totalQuestsCompleted;
        }
    }

    /**
     * Slayer statistics inner class
     */
    public static class SlayerStatistics {
        private final PlayerSlayers playerSlayers;
        private final int totalTier;
        private final long totalXP;
        private final long totalQuestsCompleted;
        private final long totalBossesKilled;
        private final long totalDamageDealt;
        private final long totalDamageTaken;
        private final int totalDeaths;

        public SlayerStatistics(PlayerSlayers playerSlayers) {
            this.playerSlayers = playerSlayers;
            this.totalTier = playerSlayers.getTotalSlayerTier();
            this.totalXP = playerSlayers.getTotalSlayerXP();
            this.totalQuestsCompleted = playerSlayers.slayerData.values().stream()
                    .mapToLong(SlayerData::getTotalQuestsCompleted).sum();
            this.totalBossesKilled = playerSlayers.slayerData.values().stream()
                    .mapToLong(SlayerData::getTotalBossesKilled).sum();
            this.totalDamageDealt = playerSlayers.slayerData.values().stream()
                    .mapToLong(SlayerData::getTotalDamageDealt).sum();
            this.totalDamageTaken = playerSlayers.slayerData.values().stream()
                    .mapToLong(SlayerData::getTotalDamageTaken).sum();
            this.totalDeaths = playerSlayers.slayerData.values().stream()
                    .mapToInt(SlayerData::getTotalDeaths).sum();
        }

        // Getters
        public PlayerSlayers getPlayerSlayers() { return playerSlayers; }
        public int getTotalTier() { return totalTier; }
        public long getTotalXP() { return totalXP; }
        public long getTotalQuestsCompleted() { return totalQuestsCompleted; }
        public long getTotalBossesKilled() { return totalBossesKilled; }
        public long getTotalDamageDealt() { return totalDamageDealt; }
        public long getTotalDamageTaken() { return totalDamageTaken; }
        public int getTotalDeaths() { return totalDeaths; }

        /**
         * Get overall K/D ratio
         */
        public double getOverallKDRatio() {
            if (totalDeaths == 0) return totalBossesKilled;
            return (double) totalBossesKilled / totalDeaths;
        }

        /**
         * Get overall efficiency score
         */
        public double getOverallEfficiencyScore() {
            if (totalQuestsCompleted == 0) return 0.0;
            return (double) totalBossesKilled / totalQuestsCompleted;
        }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Slayer Statistics:",
                "",
                "&7Total Tier: &a" + totalTier,
                "&7Total XP: &a" + formatNumber(totalXP),
                "&7Quests Completed: &a" + totalQuestsCompleted,
                "&7Bosses Killed: &a" + totalBossesKilled,
                "&7K/D Ratio: &a" + String.format("%.2f", getOverallKDRatio()),
                "&7Efficiency: &a" + String.format("%.2f", getOverallEfficiencyScore())
            };
        }

        /**
         * Format large numbers
         */
        private String formatNumber(long number) {
            if (number >= 1_000_000_000_000L) {
                return String.format("%.1fT", number / 1_000_000_000_000.0);
            } else if (number >= 1_000_000_000L) {
                return String.format("%.1fB", number / 1_000_000_000.0);
            } else if (number >= 1_000_000L) {
                return String.format("%.1fM", number / 1_000_000.0);
            } else if (number >= 1_000L) {
                return String.format("%.1fK", number / 1_000.0);
            } else {
                return String.valueOf(number);
            }
        }
    }
}
