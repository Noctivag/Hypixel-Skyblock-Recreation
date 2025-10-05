package de.noctivag.skyblock.worlds.generators;

import java.util.Map;

/**
 * World generation configuration class
 */
public class WorldGenerationConfig {
    
    private final String worldName;
    private final TerrainType terrainType;
    private final int size;
    private final boolean generateStructures;
    private final Map<String, Object> customSettings;
    
    public WorldGenerationConfig(String worldName, TerrainType terrainType, int size, boolean generateStructures, Map<String, Object> customSettings) {
        this.worldName = worldName;
        this.terrainType = terrainType;
        this.size = size;
        this.generateStructures = generateStructures;
        this.customSettings = customSettings;
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public TerrainType getTerrainType() {
        return terrainType;
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isGenerateStructures() {
        return generateStructures;
    }
    
    public Map<String, Object> getCustomSettings() {
        return customSettings;
    }
}
