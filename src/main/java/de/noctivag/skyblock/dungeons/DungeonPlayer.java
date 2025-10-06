package de.noctivag.skyblock.dungeons;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a player in a dungeon session
 */
public class DungeonPlayer {
    private final UUID playerId;
    private final String playerName;
    private final DungeonClass dungeonClass;
    private final long joinTime;
    private final Map<String, Object> playerData;
    private final DungeonPlayerStats stats;

    public DungeonPlayer(Player player, DungeonClass dungeonClass) {
        this.playerId = player.getUniqueId();
        this.playerName = player.getName();
        this.dungeonClass = dungeonClass;
        this.joinTime = System.currentTimeMillis();
        this.playerData = new HashMap<>();
        this.stats = new DungeonPlayerStats(this);
    }

    public DungeonPlayer(UUID playerId, String playerName, DungeonClass dungeonClass, 
                        long joinTime, Map<String, Object> playerData) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.dungeonClass = dungeonClass;
        this.joinTime = joinTime;
        this.playerData = new HashMap<>(playerData);
        this.stats = new DungeonPlayerStats(this);
    }

    // Getters
    public UUID getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public DungeonClass getDungeonClass() { return dungeonClass; }
    public long getJoinTime() { return joinTime; }
    public Map<String, Object> getPlayerData() { return new HashMap<>(playerData); }
    public DungeonPlayerStats getStats() { return stats; }

    /**
     * Get the actual player object
     */
    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(playerId);
    }

    /**
     * Check if player is online
     */
    public boolean isOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    /**
     * Get time in session
     */
    public long getTimeInSession() {
        return System.currentTimeMillis() - joinTime;
    }

    /**
     * Get formatted time in session
     */
    public String getFormattedTimeInSession() {
        long seconds = getTimeInSession() / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";
        }
    }

    /**
     * Set player data
     */
    public void setData(String key, Object value) {
        playerData.put(key, value);
    }

    /**
     * Get player data
     */
    public Object getData(String key) {
        return playerData.get(key);
    }

    /**
     * Get player data with default value
     */
    public Object getData(String key, Object defaultValue) {
        return playerData.getOrDefault(key, defaultValue);
    }

    /**
     * Get player display name
     */
    public String getDisplayName() {
        return dungeonClass.getColoredDisplayName() + " " + playerName;
    }

    /**
     * Get player lore
     */
    public String[] getPlayerLore() {
        return new String[]{
            "&7Player: &a" + playerName,
            "&7Class: " + dungeonClass.getColoredDisplayName(),
            "&7Time in Session: &a" + getFormattedTimeInSession(),
            "&7Status: " + (isOnline() ? "&aOnline" : "&cOffline"),
            "",
            "&7Stats:",
            "&c❤ Health: &a" + stats.getCurrentHealth() + "/" + stats.getMaxHealth(),
            "&9🛡 Defense: &a" + stats.getDefense(),
            "&c⚔ Strength: &a" + stats.getStrength(),
            "&a⚡ Speed: &a" + stats.getSpeed(),
            "&b🧠 Intelligence: &a" + stats.getIntelligence(),
            "",
            "&7Performance:",
            "&6💥 Damage Dealt: &a" + stats.getDamageDealt(),
            "&c💔 Damage Taken: &a" + stats.getDamageTaken(),
            "&a❤ Heals Given: &a" + stats.getHealsGiven(),
            "&9🛡 Damage Blocked: &a" + stats.getDamageBlocked()
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DungeonPlayer that = (DungeonPlayer) obj;
        return playerId.equals(that.playerId);
    }

    @Override
    public int hashCode() {
        return playerId.hashCode();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    /**
     * Dungeon player statistics inner class
     */
    public static class DungeonPlayerStats {
        private final DungeonPlayer dungeonPlayer;
        private int currentHealth;
        private int maxHealth;
        private int defense;
        private int strength;
        private int speed;
        private int intelligence;
        private long damageDealt;
        private long damageTaken;
        private int healsGiven;
        private long damageBlocked;
        private int kills;
        private int deaths;
        private int assists;

        public DungeonPlayerStats(DungeonPlayer dungeonPlayer) {
            this.dungeonPlayer = dungeonPlayer;
            
            // Initialize stats based on class
            DungeonClass.ClassStatistics classStats = dungeonPlayer.getDungeonClass().getClassStatistics();
            this.maxHealth = classStats.getBaseHealth();
            this.currentHealth = maxHealth;
            this.defense = classStats.getBaseDefense();
            this.strength = classStats.getBaseStrength();
            this.speed = classStats.getBaseSpeed();
            this.intelligence = classStats.getBaseIntelligence();
            
            // Initialize performance stats
            this.damageDealt = 0;
            this.damageTaken = 0;
            this.healsGiven = 0;
            this.damageBlocked = 0;
            this.kills = 0;
            this.deaths = 0;
            this.assists = 0;
        }

        // Getters
        public DungeonPlayer getDungeonPlayer() { return dungeonPlayer; }
        public int getCurrentHealth() { return currentHealth; }
        public int getMaxHealth() { return maxHealth; }
        public int getDefense() { return defense; }
        public int getStrength() { return strength; }
        public int getSpeed() { return speed; }
        public int getIntelligence() { return intelligence; }
        public long getDamageDealt() { return damageDealt; }
        public long getDamageTaken() { return damageTaken; }
        public int getHealsGiven() { return healsGiven; }
        public long getDamageBlocked() { return damageBlocked; }
        public int getKills() { return kills; }
        public int getDeaths() { return deaths; }
        public int getAssists() { return assists; }

        // Setters
        public void setCurrentHealth(int currentHealth) { this.currentHealth = Math.max(0, Math.min(currentHealth, maxHealth)); }
        public void setMaxHealth(int maxHealth) { this.maxHealth = Math.max(1, maxHealth); }
        public void setDefense(int defense) { this.defense = Math.max(0, defense); }
        public void setStrength(int strength) { this.strength = Math.max(0, strength); }
        public void setSpeed(int speed) { this.speed = Math.max(0, speed); }
        public void setIntelligence(int intelligence) { this.intelligence = Math.max(0, intelligence); }

        // Performance stat methods
        public void addDamageDealt(long damage) { this.damageDealt += damage; }
        public void addDamageTaken(long damage) { this.damageTaken += damage; }
        public void addHealsGiven(int heals) { this.healsGiven += heals; }
        public void addDamageBlocked(long damage) { this.damageBlocked += damage; }
        public void addKill() { this.kills++; }
        public void addDeath() { this.deaths++; }
        public void addAssist() { this.assists++; }

        /**
         * Get health percentage
         */
        public double getHealthPercentage() {
            if (maxHealth == 0) return 0.0;
            return (double) currentHealth / maxHealth * 100.0;
        }

        /**
         * Check if player is alive
         */
        public boolean isAlive() {
            return currentHealth > 0;
        }

        /**
         * Heal player
         */
        public void heal(int amount) {
            setCurrentHealth(currentHealth + amount);
        }

        /**
         * Damage player
         */
        public void damage(int amount) {
            setCurrentHealth(currentHealth - amount);
        }

        /**
         * Get K/D ratio
         */
        public double getKDRatio() {
            if (deaths == 0) return kills;
            return (double) kills / deaths;
        }

        /**
         * Get performance score
         */
        public double getPerformanceScore() {
            double score = 0;
            score += kills * 100;
            score += assists * 50;
            score += damageDealt / 1000.0;
            score += healsGiven * 25;
            score += damageBlocked / 1000.0;
            score -= deaths * 50;
            return Math.max(0, score);
        }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Player Statistics:",
                "",
                "&7Health: &a" + currentHealth + "/" + maxHealth + " &7(" + String.format("%.1f", getHealthPercentage()) + "%)",
                "&7Defense: &a" + defense,
                "&7Strength: &a" + strength,
                "&7Speed: &a" + speed,
                "&7Intelligence: &a" + intelligence,
                "",
                "&7Performance:",
                "&7Kills: &a" + kills,
                "&7Deaths: &c" + deaths,
                "&7Assists: &e" + assists,
                "&7K/D Ratio: &a" + String.format("%.2f", getKDRatio()),
                "&7Performance Score: &a" + String.format("%.0f", getPerformanceScore())
            };
        }
    }
}
