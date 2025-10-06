package de.noctivag.skyblock.zones;

import java.util.List;

/**
 * Configuration for mob spawning in a zone
 */
public class MobSpawnConfig {
    
    private final String mobId;
    private final int weight;
    private final int minLevel;
    private final int maxLevel;
    private final int spawnRadius;
    private final int maxCount;
    private final List<String> spawnConditions;
    
    public MobSpawnConfig(String mobId, int weight, int minLevel, int maxLevel, 
                         int spawnRadius, int maxCount, List<String> spawnConditions) {
        this.mobId = mobId;
        this.weight = weight;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.spawnRadius = spawnRadius;
        this.maxCount = maxCount;
        this.spawnConditions = spawnConditions;
    }
    
    /**
     * Check if this mob can spawn based on conditions
     */
    public boolean canSpawn(org.bukkit.entity.Player player) {
        // Check level requirements
        // This would integrate with a level system
        // For now, we'll just return true
        
        // Check spawn conditions
        for (String condition : spawnConditions) {
            if (!checkSpawnCondition(condition, player)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Check a specific spawn condition
     */
    private boolean checkSpawnCondition(String condition, org.bukkit.entity.Player player) {
        if (condition.startsWith("TIME:")) {
            String time = condition.substring(5);
            long worldTime = player.getWorld().getTime();
            
            switch (time.toUpperCase()) {
                case "DAY":
                    return worldTime >= 0 && worldTime < 12000;
                case "NIGHT":
                    return worldTime >= 12000 && worldTime < 24000;
                case "DAWN":
                    return worldTime >= 0 && worldTime < 3000;
                case "DUSK":
                    return worldTime >= 18000 && worldTime < 24000;
                default:
                    return true;
            }
        }
        
        if (condition.startsWith("WEATHER:")) {
            String weather = condition.substring(8);
            switch (weather.toUpperCase()) {
                case "CLEAR":
                    return !player.getWorld().hasStorm();
                case "RAIN":
                    return player.getWorld().hasStorm();
                case "THUNDER":
                    return player.getWorld().isThundering();
                default:
                    return true;
            }
        }
        
        if (condition.startsWith("BIOME:")) {
            String biome = condition.substring(6);
            return player.getLocation().getBlock().getBiome().getKey().getKey().equalsIgnoreCase(biome);
        }
        
        return true; // Unknown condition, allow spawn
    }
    
    // Getters
    public String getMobId() { return mobId; }
    public int getWeight() { return weight; }
    public int getMinLevel() { return minLevel; }
    public int getMaxLevel() { return maxLevel; }
    public int getSpawnRadius() { return spawnRadius; }
    public int getMaxCount() { return maxCount; }
    public List<String> getSpawnConditions() { return spawnConditions; }
}
