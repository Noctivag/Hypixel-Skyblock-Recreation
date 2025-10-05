package de.noctivag.skyblock.worlds.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Custom World Generator - Flexibler Generator für eigene Welten
 * 
 * Verantwortlich für:
 * - Anpassbare Welt-Generierung
 * - Konfigurierbare Terrain-Typen
 * - Custom Biome-Support
 * - Performance-optimierte Generierung
 */
public class CustomWorldGenerator extends ChunkGenerator {
    
    private final WorldGenerationConfig config;
    
    public CustomWorldGenerator() {
        this.config = new WorldGenerationConfig();
    }
    
    public CustomWorldGenerator(WorldGenerationConfig config) {
        this.config = config != null ? config : new WorldGenerationConfig();
    }
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        switch (config.getTerrainType()) {
            case FLAT:
                generateFlatTerrain(chunkData);
                break;
            case NORMAL:
                generateNormalTerrain(worldInfo, random, chunkX, chunkZ, chunkData);
                break;
            case AMPLIFIED:
                generateAmplifiedTerrain(worldInfo, random, chunkX, chunkZ, chunkData);
                break;
            case VOID:
                generateVoidTerrain(chunkData);
                break;
            case CUSTOM:
                generateCustomTerrain(worldInfo, random, chunkX, chunkZ, chunkData);
                break;
        }
    }
    
    /**
     * Generiert flaches Terrain
     */
    private void generateFlatTerrain(ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Stein-Plattform
                for (int y = 1; y <= config.getFlatHeight(); y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Gras auf der obersten Schicht
                if (config.getFlatHeight() > 0) {
                    chunkData.setBlock(x, config.getFlatHeight(), z, Material.GRASS_BLOCK);
                }
            }
        }
    }
    
    /**
     * Generiert normales Terrain
     */
    private void generateNormalTerrain(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Generiere Höhe basierend auf Position
                int height = generateHeight(chunkX * 16 + x, chunkZ * 16 + z, random);
                
                // Fülle mit Stein
                for (int y = 1; y < height; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Oberfläche
                if (height > 0) {
                    chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                }
            }
        }
    }
    
    /**
     * Generiert verstärktes Terrain
     */
    private void generateAmplifiedTerrain(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Generiere verstärkte Höhe
                int height = generateAmplifiedHeight(chunkX * 16 + x, chunkZ * 16 + z, random);
                
                // Fülle mit Stein
                for (int y = 1; y < height; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Oberfläche
                if (height > 0) {
                    chunkData.setBlock(x, height, z, Material.GRASS_BLOCK);
                }
            }
        }
    }
    
    /**
     * Generiert leeres Terrain
     */
    private void generateVoidTerrain(ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Nur Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }
    }
    
    /**
     * Generiert benutzerdefiniertes Terrain
     */
    private void generateCustomTerrain(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Generiere benutzerdefinierte Höhe
                int height = generateCustomHeight(chunkX * 16 + x, chunkZ * 16 + z, random);
                
                // Fülle mit konfiguriertem Material
                Material fillMaterial = config.getFillMaterial();
                for (int y = 1; y < height; y++) {
                    chunkData.setBlock(x, y, z, fillMaterial);
                }
                
                // Oberfläche
                if (height > 0) {
                    Material surfaceMaterial = config.getSurfaceMaterial();
                    chunkData.setBlock(x, height, z, surfaceMaterial);
                }
            }
        }
    }
    
    /**
     * Generiert Höhe für normales Terrain
     */
    private int generateHeight(int x, int z, Random random) {
        double noise = generateNoise(x, z, config.getNoiseScale(), random);
        return (int) (config.getBaseHeight() + noise * config.getHeightVariation());
    }
    
    /**
     * Generiert Höhe für verstärktes Terrain
     */
    private int generateAmplifiedHeight(int x, int z, Random random) {
        double noise = generateNoise(x, z, config.getNoiseScale() * 0.5, random);
        return (int) (config.getBaseHeight() + noise * config.getHeightVariation() * 2);
    }
    
    /**
     * Generiert Höhe für benutzerdefiniertes Terrain
     */
    private int generateCustomHeight(int x, int z, Random random) {
        double noise = generateNoise(x, z, config.getCustomNoiseScale(), random);
        return (int) (config.getCustomBaseHeight() + noise * config.getCustomHeightVariation());
    }
    
    /**
     * Generiert Noise für Terrain
     */
    private double generateNoise(int x, int z, double scale, Random random) {
        // Einfache Noise-Implementierung
        double value = Math.sin(x * scale) * Math.cos(z * scale);
        value += Math.sin(x * scale * 2) * Math.cos(z * scale * 2) * 0.5;
        value += Math.sin(x * scale * 4) * Math.cos(z * scale * 4) * 0.25;
        return value;
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock bereits in generateSurface gesetzt
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        if (config.isGenerateCaves()) {
            generateCaves(chunkData, random);
        }
    }
    
    /**
     * Generiert Höhlen
     */
    private void generateCaves(ChunkData chunkData, Random random) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 1; y < 60; y++) {
                    if (random.nextDouble() < config.getCaveChance()) {
                        chunkData.setBlock(x, y, z, Material.AIR);
                    }
                }
            }
        }
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Kein zusätzliches Noise in Custom Generator
    }
    
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }
    
    @Override
    public boolean shouldGenerateBedrock() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateCaves() {
        return config.isGenerateCaves();
    }
    
    @Override
    public boolean shouldGenerateDecorations() {
        return config.isGenerateDecorations();
    }
    
    @Override
    public boolean shouldGenerateMobs() {
        return config.isGenerateMobs();
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return config.isGenerateStructures();
    }
    
    /**
     * World Generation Config Klasse
     */
    public static class WorldGenerationConfig {
        private TerrainType terrainType = TerrainType.NORMAL;
        private int flatHeight = 64;
        private int baseHeight = 64;
        private double heightVariation = 16.0;
        private double noiseScale = 0.01;
        private Material fillMaterial = Material.STONE;
        private Material surfaceMaterial = Material.GRASS_BLOCK;
        private int customBaseHeight = 64;
        private double customHeightVariation = 16.0;
        private double customNoiseScale = 0.01;
        private boolean generateCaves = true;
        private double caveChance = 0.1;
        private boolean generateDecorations = true;
        private boolean generateMobs = true;
        private boolean generateStructures = true;
        
        // Getters und Setters
        public TerrainType getTerrainType() { return terrainType; }
        public void setTerrainType(TerrainType terrainType) { this.terrainType = terrainType; }
        
        public int getFlatHeight() { return flatHeight; }
        public void setFlatHeight(int flatHeight) { this.flatHeight = flatHeight; }
        
        public int getBaseHeight() { return baseHeight; }
        public void setBaseHeight(int baseHeight) { this.baseHeight = baseHeight; }
        
        public double getHeightVariation() { return heightVariation; }
        public void setHeightVariation(double heightVariation) { this.heightVariation = heightVariation; }
        
        public double getNoiseScale() { return noiseScale; }
        public void setNoiseScale(double noiseScale) { this.noiseScale = noiseScale; }
        
        public Material getFillMaterial() { return fillMaterial; }
        public void setFillMaterial(Material fillMaterial) { this.fillMaterial = fillMaterial; }
        
        public Material getSurfaceMaterial() { return surfaceMaterial; }
        public void setSurfaceMaterial(Material surfaceMaterial) { this.surfaceMaterial = surfaceMaterial; }
        
        public int getCustomBaseHeight() { return customBaseHeight; }
        public void setCustomBaseHeight(int customBaseHeight) { this.customBaseHeight = customBaseHeight; }
        
        public double getCustomHeightVariation() { return customHeightVariation; }
        public void setCustomHeightVariation(double customHeightVariation) { this.customHeightVariation = customHeightVariation; }
        
        public double getCustomNoiseScale() { return customNoiseScale; }
        public void setCustomNoiseScale(double customNoiseScale) { this.customNoiseScale = customNoiseScale; }
        
        public boolean isGenerateCaves() { return generateCaves; }
        public void setGenerateCaves(boolean generateCaves) { this.generateCaves = generateCaves; }
        
        public double getCaveChance() { return caveChance; }
        public void setCaveChance(double caveChance) { this.caveChance = caveChance; }
        
        public boolean isGenerateDecorations() { return generateDecorations; }
        public void setGenerateDecorations(boolean generateDecorations) { this.generateDecorations = generateDecorations; }
        
        public boolean isGenerateMobs() { return generateMobs; }
        public void setGenerateMobs(boolean generateMobs) { this.generateMobs = generateMobs; }
        
        public boolean isGenerateStructures() { return generateStructures; }
        public void setGenerateStructures(boolean generateStructures) { this.generateStructures = generateStructures; }
    }
    
    /**
     * Terrain Type Enum
     */
    public enum TerrainType {
        FLAT,
        NORMAL,
        AMPLIFIED,
        VOID,
        CUSTOM
    }
}
