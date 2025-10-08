package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Skyblock Profile - Player's main profile data
 */
public class SkyblockProfile {

    private final UUID playerId;
    private String playerName;
    private long firstJoin;
    private long lastLogin;
    private long totalPlayTime;
    private int level;
    private double coins;
    private Map<String, Integer> skills;
    private Map<String, Integer> collections;

    public SkyblockProfile(UUID playerId) {
        this.playerId = playerId;
        this.firstJoin = System.currentTimeMillis();
        this.lastLogin = System.currentTimeMillis();
        this.totalPlayTime = 0;
        this.level = 1;
        this.coins = 0.0;
        this.skills = new HashMap<>();
        this.collections = new HashMap<>();

        // Initialize default skills
        initializeDefaultSkills();
    }

    /**
     * Initialize default skills
     */
    private void initializeDefaultSkills() {
        skills.put("mining", 0);
        skills.put("foraging", 0);
        skills.put("enchanting", 0);
        skills.put("farming", 0);
        skills.put("combat", 0);
        skills.put("fishing", 0);
        skills.put("alchemy", 0);
        skills.put("taming", 0);
    }

    // Getters and setters
    public UUID getPlayerId() { return playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public boolean isFirstJoin() {
        return firstJoin == lastLogin;
    }

    public long getFirstJoin() { return firstJoin; }
    public void setFirstJoin(long firstJoin) { this.firstJoin = firstJoin; }

    public long getLastLogin() { return lastLogin; }
    public void setLastLogin(long lastLogin) { this.lastLogin = lastLogin; }

    public long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public double getCoins() { return coins; }
    public void setCoins(double coins) { this.coins = coins; }

    public Map<String, Integer> getSkills() { return skills; }
    public void setSkills(Map<String, Integer> skills) { this.skills = skills; }

    public Map<String, Integer> getCollections() { return collections; }
    public void setCollections(Map<String, Integer> collections) { this.collections = collections; }

    /**
     * Get skill level
     */
    public int getSkillLevel(String skillName) {
        return skills.getOrDefault(skillName, 0);
    }

    /**
     * Set skill level
     */
    public void setSkillLevel(String skillName, int level) {
        skills.put(skillName, level);
    }

    /**
     * Get collection count
     */
    public int getCollectionCount(String itemName) {
        return collections.getOrDefault(itemName, 0);
    }

    /**
     * Set collection count
     */
    public void setCollectionCount(String itemName, int count) {
        collections.put(itemName, count);
    }

    // Statische Methode zum Laden eines Profils (Platzhalter)
    public static SkyblockProfile load(java.util.UUID uuid) {
        // TODO: Implementierung
        return null;
    }
    // Instanzmethode zum Speichern eines Profils (Platzhalter)
    public void save() {
        // TODO: Implementierung
    }
}
