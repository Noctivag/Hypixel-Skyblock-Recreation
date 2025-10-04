package de.noctivag.plugin.slayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Slayer Data - Stores slayer progression and RNG meter data for a player
 */
public class PlayerSlayerData {
    
    private final UUID playerId;
    
    // Slayer levels and XP for each type
    private final Map<SlayerSystem.SlayerType, Integer> slayerLevels = new HashMap<>();
    private final Map<SlayerSystem.SlayerType, Integer> slayerXP = new HashMap<>();
    
    // RNG meters for each slayer type
    private final Map<SlayerSystem.SlayerType, SlayerRNGMeter> rngMeters = new HashMap<>();
    
    // Statistics
    private final Map<SlayerSystem.SlayerType, Integer> totalKills = new HashMap<>();
    private final Map<SlayerSystem.SlayerType, Integer> bossKills = new HashMap<>();
    private final Map<SlayerSystem.SlayerType, Integer> rareDrops = new HashMap<>();
    
    public PlayerSlayerData(UUID playerId) {
        this.playerId = playerId;
        initializeData();
    }
    
    /**
     * Initialize default data for all slayer types
     */
    private void initializeData() {
        for (SlayerSystem.SlayerType type : SlayerSystem.SlayerType.values()) {
            slayerLevels.put(type, 1);
            slayerXP.put(type, 0);
            totalKills.put(type, 0);
            bossKills.put(type, 0);
            rareDrops.put(type, 0);
            
            // Initialize RNG meter for Tier III+ (when unlocked)
            SlayerRNGMeter meter = new SlayerRNGMeter(type, SlayerSystem.SlayerTier.TIER_III, 
                new SlayerSystem.RNGMeterConfig(500, 3.0));
            rngMeters.put(type, meter);
        }
    }
    
    /**
     * Add slayer XP for a specific type
     */
    public void addSlayerXP(SlayerSystem.SlayerType type, int xp) {
        int currentXP = slayerXP.get(type);
        slayerXP.put(type, currentXP + xp);
        
        // Check for level up
        checkLevelUp(type);
    }
    
    /**
     * Check if player can level up
     */
    private void checkLevelUp(SlayerSystem.SlayerType type) {
        int currentLevel = slayerLevels.get(type);
        int currentXP = slayerXP.get(type);
        int requiredXP = getRequiredXPForLevel(currentLevel + 1);
        
        if (currentXP >= requiredXP) {
            levelUp(type);
        }
    }
    
    /**
     * Level up slayer
     */
    private void levelUp(SlayerSystem.SlayerType type) {
        int newLevel = slayerLevels.get(type) + 1;
        slayerLevels.put(type, newLevel);
        
        // Unlock RNG meter at level 10 (Tier III requirement)
        if (newLevel >= 10) {
            SlayerRNGMeter meter = rngMeters.get(type);
            if (meter != null) {
                meter.activate();
            }
        }
    }
    
    /**
     * Get required XP for a specific level
     */
    private int getRequiredXPForLevel(int level) {
        if (level <= 1) return 0;
        
        // XP formula: 100 * (level - 1)^1.5
        return (int) (100 * Math.pow(level - 1, 1.5));
    }
    
    /**
     * Add a kill to statistics
     */
    public void addKill(SlayerSystem.SlayerType type) {
        totalKills.put(type, totalKills.get(type) + 1);
        
        // Update RNG meter if active
        SlayerRNGMeter meter = rngMeters.get(type);
        if (meter != null && meter.isActive()) {
            meter.addKill();
        }
    }
    
    /**
     * Add a boss kill to statistics
     */
    public void addBossKill(SlayerSystem.SlayerType type, SlayerSystem.SlayerTier tier) {
        bossKills.put(type, bossKills.get(type) + 1);
        
        // Add XP based on tier
        addSlayerXP(type, tier.getXP());
    }
    
    /**
     * Add a rare drop to statistics
     */
    public void addRareDrop(SlayerSystem.SlayerType type) {
        rareDrops.put(type, rareDrops.get(type) + 1);
        
        // Reset RNG meter
        SlayerRNGMeter meter = rngMeters.get(type);
        if (meter != null) {
            meter.reset();
        }
    }
    
    /**
     * Check if player can start a slayer quest
     */
    public boolean canStartQuest(SlayerSystem.SlayerType type, SlayerSystem.SlayerTier tier) {
        return slayerLevels.get(type) >= tier.getRequiredLevel();
    }
    
    /**
     * Get slayer level for a specific type
     */
    public int getSlayerLevel(SlayerSystem.SlayerType type) {
        return slayerLevels.getOrDefault(type, 1);
    }
    
    /**
     * Get slayer XP for a specific type
     */
    public int getSlayerXP(SlayerSystem.SlayerType type) {
        return slayerXP.getOrDefault(type, 0);
    }
    
    /**
     * Get total kills for a specific type
     */
    public int getTotalKills(SlayerSystem.SlayerType type) {
        return totalKills.getOrDefault(type, 0);
    }
    
    /**
     * Get boss kills for a specific type
     */
    public int getBossKills(SlayerSystem.SlayerType type) {
        return bossKills.getOrDefault(type, 0);
    }
    
    /**
     * Get rare drops for a specific type
     */
    public int getRareDrops(SlayerSystem.SlayerType type) {
        return rareDrops.getOrDefault(type, 0);
    }
    
    /**
     * Get RNG meter for a specific type
     */
    public SlayerRNGMeter getRNGMeter(SlayerSystem.SlayerType type) {
        return rngMeters.get(type);
    }
    
    /**
     * Get XP progress to next level
     */
    public int getXPToNextLevel(SlayerSystem.SlayerType type) {
        int currentLevel = slayerLevels.get(type);
        int currentXP = slayerXP.get(type);
        int requiredXP = getRequiredXPForLevel(currentLevel + 1);
        
        return Math.max(0, requiredXP - currentXP);
    }
    
    /**
     * Get XP progress percentage to next level
     */
    public double getXPProgressPercentage(SlayerSystem.SlayerType type) {
        int currentLevel = slayerLevels.get(type);
        int currentXP = slayerXP.get(type);
        int currentLevelXP = getRequiredXPForLevel(currentLevel);
        int nextLevelXP = getRequiredXPForLevel(currentLevel + 1);
        
        if (nextLevelXP == currentLevelXP) return 1.0;
        
        return (double) (currentXP - currentLevelXP) / (nextLevelXP - currentLevelXP);
    }
    
    /**
     * Get total slayer level across all types
     */
    public int getTotalSlayerLevel() {
        return slayerLevels.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get total slayer XP across all types
     */
    public int getTotalSlayerXP() {
        return slayerXP.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get total kills across all types
     */
    public int getTotalKills() {
        return totalKills.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get total boss kills across all types
     */
    public int getTotalBossKills() {
        return bossKills.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get total rare drops across all types
     */
    public int getTotalRareDrops() {
        return rareDrops.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get slayer rank based on total level
     */
    public String getSlayerRank() {
        int totalLevel = getTotalSlayerLevel();
        
        return switch (totalLevel / 10) {
            case 0 -> "§fBeginner";
            case 1 -> "§aNovice";
            case 2 -> "§eApprentice";
            case 3 -> "§6Adept";
            case 4 -> "§cExpert";
            case 5 -> "§5Master";
            case 6 -> "§dGrandmaster";
            case 7 -> "§4Legend";
            default -> "§6§lMythic";
        };
    }
    
    // Getters
    public UUID getPlayerId() { return playerId; }
    public Map<SlayerSystem.SlayerType, Integer> getSlayerLevels() { return new HashMap<>(slayerLevels); }
    public Map<SlayerSystem.SlayerType, Integer> getSlayerXP() { return new HashMap<>(slayerXP); }
    public Map<SlayerSystem.SlayerType, Integer> getTotalKills() { return new HashMap<>(totalKills); }
    public Map<SlayerSystem.SlayerType, Integer> getBossKills() { return new HashMap<>(bossKills); }
    public Map<SlayerSystem.SlayerType, Integer> getRareDrops() { return new HashMap<>(rareDrops); }
}
