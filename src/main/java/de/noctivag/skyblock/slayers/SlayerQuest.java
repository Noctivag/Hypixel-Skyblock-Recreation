package de.noctivag.skyblock.slayers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Represents an active slayer quest
 */
public class SlayerQuest {
    private final String questId;
    private final UUID playerId;
    private final SlayerTier slayerTier;
    private final long startTime;
    private final Location spawnLocation;
    private final SlayerQuestStatus status;
    private final SlayerQuestProgress progress;

    public SlayerQuest(String questId, UUID playerId, SlayerTier slayerTier, Location spawnLocation) {
        this.questId = questId;
        this.playerId = playerId;
        this.slayerTier = slayerTier;
        this.startTime = System.currentTimeMillis();
        this.spawnLocation = spawnLocation.clone();
        this.status = SlayerQuestStatus.ACTIVE;
        this.progress = new SlayerQuestProgress();
    }

    public SlayerQuest(String questId, UUID playerId, SlayerTier slayerTier, Location spawnLocation,
                      long startTime, SlayerQuestStatus status, SlayerQuestProgress progress) {
        this.questId = questId;
        this.playerId = playerId;
        this.slayerTier = slayerTier;
        this.startTime = startTime;
        this.spawnLocation = spawnLocation.clone();
        this.status = status;
        this.progress = progress;
    }
    
    // Getters
    public String getQuestId() { return questId; }
    public UUID getPlayerId() { return playerId; }
    public SlayerTier getSlayerTier() { return slayerTier; }
    public long getStartTime() { return startTime; }
    public Location getSpawnLocation() { return spawnLocation.clone(); }
    public SlayerQuestStatus getStatus() { return status; }
    public SlayerQuestProgress getProgress() { return progress; }

    /**
     * Get the actual player object
     */
    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(playerId);
    }

    /**
     * Check if player is online
     */
    public boolean isPlayerOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    /**
     * Get quest duration
     */
    public long getQuestDuration() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Get quest duration in seconds
     */
    public long getQuestDurationSeconds() {
        return getQuestDuration() / 1000;
    }

    /**
     * Get quest duration in minutes
     */
    public long getQuestDurationMinutes() {
        return getQuestDurationSeconds() / 60;
    }

    /**
     * Get formatted quest duration
     */
    public String getFormattedDuration() {
        long minutes = getQuestDurationMinutes();
        long seconds = getQuestDurationSeconds() % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    /**
     * Get quest display name
     */
    public String getDisplayName() {
        return slayerTier.getFullDisplayName();
    }

    /**
     * Get quest description
     */
    public String getDescription() {
        return "Defeat " + slayerTier.getBossName() + " in " + slayerTier.getSlayerType().getSpawnLocation();
    }

    /**
     * Get quest lore
     */
    public String[] getQuestLore() {
        return new String[]{
            "&7" + getDescription(),
            "",
            "&7Boss: &c" + slayerTier.getBossName(),
            "&7Health: &c" + formatNumber(slayerTier.getHealth()),
            "&7Damage: &c" + slayerTier.getDamage(),
            "&7Defense: &9" + slayerTier.getDefense(),
            "",
            "&7Progress: &a" + progress.getProgressPercentage() + "%",
            "&7Mobs Killed: &a" + progress.getMobsKilled() + "/" + progress.getMobsRequired(),
            "&7Boss Spawned: " + (progress.isBossSpawned() ? "&aYes" : "&cNo"),
            "&7Boss Killed: " + (progress.isBossKilled() ? "&aYes" : "&cNo"),
            "",
            "&7Duration: &a" + getFormattedDuration(),
            "&7Status: " + getStatusColor() + status.getDisplayName(),
            "",
            "&7Reward: &a" + formatNumber(slayerTier.getReward()) + " coins",
            "&7XP Reward: &b" + formatNumber(slayerTier.getXpReward()) + " XP"
        };
    }

    /**
     * Get status color
     */
    private String getStatusColor() {
        switch (status) {
            case ACTIVE: return "&a";
            case COMPLETED: return "&a";
            case FAILED: return "&c";
            case CANCELLED: return "&7";
            case EXPIRED: return "&e";
            default: return "&7";
        }
    }

    /**
     * Check if quest is completed
     */
    public boolean isCompleted() {
        return status == SlayerQuestStatus.COMPLETED || progress.isBossKilled();
    }

    /**
     * Check if quest is failed
     */
    public boolean isFailed() {
        return status == SlayerQuestStatus.FAILED || status == SlayerQuestStatus.EXPIRED;
    }

    /**
     * Check if quest is active
     */
    public boolean isActive() {
        return status == SlayerQuestStatus.ACTIVE;
    }

    /**
     * Get quest completion percentage
     */
    public double getCompletionPercentage() {
        return progress.getProgressPercentage();
    }

    /**
     * Get estimated time remaining
     */
    public long getEstimatedTimeRemaining() {
        if (isCompleted() || isFailed()) return 0;
        
        int estimatedTotalTime = slayerTier.getEstimatedCompletionTime() * 60; // Convert to seconds
        long elapsedTime = getQuestDurationSeconds();
        
        return Math.max(0, estimatedTotalTime - elapsedTime);
    }

    /**
     * Get formatted estimated time remaining
     */
    public String getFormattedTimeRemaining() {
        long seconds = getEstimatedTimeRemaining();
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SlayerQuest that = (SlayerQuest) obj;
        return questId.equals(that.questId);
    }

    @Override
    public int hashCode() {
        return questId.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName() + " (" + status.getDisplayName() + ")";
    }

    /**
     * Enum for slayer quest status
     */
    public enum SlayerQuestStatus {
        ACTIVE("Active", "Quest is currently active"),
        COMPLETED("Completed", "Quest completed successfully"),
        FAILED("Failed", "Quest failed"),
        CANCELLED("Cancelled", "Quest was cancelled"),
        EXPIRED("Expired", "Quest expired due to timeout");

        private final String displayName;
        private final String description;

        SlayerQuestStatus(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    /**
     * Slayer quest progress inner class
     */
    public static class SlayerQuestProgress {
        private int mobsKilled;
        private int mobsRequired;
        private boolean bossSpawned;
        private boolean bossKilled;
        private long damageDealt;
        private long damageTaken;
        private int deaths;

        public SlayerQuestProgress() {
            this.mobsKilled = 0;
            this.mobsRequired = 100; // Default requirement
            this.bossSpawned = false;
            this.bossKilled = false;
            this.damageDealt = 0;
            this.damageTaken = 0;
            this.deaths = 0;
        }

        public SlayerQuestProgress(int mobsKilled, int mobsRequired, boolean bossSpawned, 
                                 boolean bossKilled, long damageDealt, long damageTaken, int deaths) {
            this.mobsKilled = mobsKilled;
            this.mobsRequired = mobsRequired;
            this.bossSpawned = bossSpawned;
            this.bossKilled = bossKilled;
            this.damageDealt = damageDealt;
            this.damageTaken = damageTaken;
            this.deaths = deaths;
        }

        // Getters
        public int getMobsKilled() { return mobsKilled; }
        public int getMobsRequired() { return mobsRequired; }
        public boolean isBossSpawned() { return bossSpawned; }
        public boolean isBossKilled() { return bossKilled; }
        public long getDamageDealt() { return damageDealt; }
        public long getDamageTaken() { return damageTaken; }
        public int getDeaths() { return deaths; }

        // Setters
        public void setMobsKilled(int mobsKilled) { this.mobsKilled = mobsKilled; }
        public void setMobsRequired(int mobsRequired) { this.mobsRequired = mobsRequired; }
        public void setBossSpawned(boolean bossSpawned) { this.bossSpawned = bossSpawned; }
        public void setBossKilled(boolean bossKilled) { this.bossKilled = bossKilled; }
        public void setDamageDealt(long damageDealt) { this.damageDealt = damageDealt; }
        public void setDamageTaken(long damageTaken) { this.damageTaken = damageTaken; }
        public void setDeaths(int deaths) { this.deaths = deaths; }

        // Progress methods
        public void addMobKill() { this.mobsKilled++; }
        public void addDamageDealt(long damage) { this.damageDealt += damage; }
        public void addDamageTaken(long damage) { this.damageTaken += damage; }
        public void addDeath() { this.deaths++; }

        /**
         * Get progress percentage
         */
        public double getProgressPercentage() {
            if (bossKilled) return 100.0;
            if (bossSpawned) return 75.0;
            if (mobsRequired == 0) return 0.0;
            return Math.min(75.0, (double) mobsKilled / mobsRequired * 75.0);
        }

        /**
         * Check if boss should spawn
         */
        public boolean shouldSpawnBoss() {
            return !bossSpawned && mobsKilled >= mobsRequired;
        }

        /**
         * Get progress description
         */
        public String getProgressDescription() {
            if (bossKilled) return "&aQuest Completed!";
            if (bossSpawned) return "&cBoss Active - Defeat the boss!";
            return "&7Kill " + (mobsRequired - mobsKilled) + " more mobs to spawn boss";
        }
    }
}