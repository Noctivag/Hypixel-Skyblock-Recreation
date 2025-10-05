package de.noctivag.skyblock.models;

import java.util.UUID;

/**
 * Spielerprofil-Model für das Caching-System
 * Repräsentiert alle wichtigen Spielerdaten
 */
public class PlayerProfile {
    
    private final UUID uuid;
    private String playerName;
    private long lastLogin;
    private long lastSave;
    private long playTime;
    private int level;
    private double coins;
    private String currentWorld;
    private boolean isOnline;
    private AccessoryBag accessoryBag;
    private PowerStone activePowerStone;
    private SlayerQuest activeSlayerQuest;
    private Map<String, Integer> slayerXP;
    private DungeonClass dungeonClass;
    private PetBag petBag;
    
    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
        this.lastLogin = System.currentTimeMillis();
        this.lastSave = System.currentTimeMillis();
        this.playTime = 0;
        this.level = 1;
        this.coins = 0.0;
        this.currentWorld = "hub_a";
        this.isOnline = false;
        this.accessoryBag = new AccessoryBag();
        this.activePowerStone = PowerStone.BERSERKER; // Standard Power Stone
        this.activeSlayerQuest = null;
        this.slayerXP = new HashMap<>();
        this.dungeonClass = DungeonClass.ARCHER; // Standard-Klasse
        this.petBag = new PetBag();
    }
    
    public PlayerProfile(UUID uuid, String playerName) {
        this(uuid);
        this.playerName = playerName;
    }
    
    // Getter-Methoden
    public UUID getUuid() {
        return uuid;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public long getLastLogin() {
        return lastLogin;
    }
    
    public long getLastSave() {
        return lastSave;
    }
    
    public long getPlayTime() {
        return playTime;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getCoins() {
        return coins;
    }
    
    public String getCurrentWorld() {
        return currentWorld;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    // Setter-Methoden
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public void setLastSave(long lastSave) {
        this.lastSave = lastSave;
    }
    
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setCoins(double coins) {
        this.coins = coins;
    }
    
    public void setCurrentWorld(String currentWorld) {
        this.currentWorld = currentWorld;
    }
    
    public void setOnline(boolean online) {
        isOnline = online;
    }
    
    public AccessoryBag getAccessoryBag() {
        return accessoryBag;
    }
    
    public void setAccessoryBag(AccessoryBag accessoryBag) {
        this.accessoryBag = accessoryBag;
    }
    
    public PowerStone getActivePowerStone() {
        return activePowerStone;
    }
    
    public void setActivePowerStone(PowerStone activePowerStone) {
        this.activePowerStone = activePowerStone;
    }
    
    public SlayerQuest getActiveSlayerQuest() {
        return activeSlayerQuest;
    }
    
    public void setActiveSlayerQuest(SlayerQuest activeSlayerQuest) {
        this.activeSlayerQuest = activeSlayerQuest;
    }
    
    public Map<String, Integer> getSlayerXP() {
        return slayerXP;
    }
    
    public void setSlayerXP(Map<String, Integer> slayerXP) {
        this.slayerXP = slayerXP;
    }
    
    public int getSlayerXP(String slayerType) {
        return slayerXP.getOrDefault(slayerType, 0);
    }
    
    public void addSlayerXP(String slayerType, int xp) {
        slayerXP.put(slayerType, slayerXP.getOrDefault(slayerType, 0) + xp);
    }
    
    public DungeonClass getDungeonClass() {
        return dungeonClass;
    }
    
    public void setDungeonClass(DungeonClass dungeonClass) {
        this.dungeonClass = dungeonClass;
    }
    
    public PetBag getPetBag() {
        return petBag;
    }
    
    public void setPetBag(PetBag petBag) {
        this.petBag = petBag;
    }
    
    // Utility-Methoden
    public void addCoins(double amount) {
        this.coins += amount;
    }
    
    public void removeCoins(double amount) {
        this.coins = Math.max(0, this.coins - amount);
    }
    
    public void addPlayTime(long time) {
        this.playTime += time;
    }
    
    public void levelUp() {
        this.level++;
    }
    
    @Override
    public String toString() {
        return "PlayerProfile{" +
                "uuid=" + uuid +
                ", playerName='" + playerName + '\'' +
                ", level=" + level +
                ", coins=" + coins +
                ", currentWorld='" + currentWorld + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }
}
