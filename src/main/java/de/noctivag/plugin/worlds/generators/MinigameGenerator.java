package de.noctivag.plugin.worlds.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Minigame Generator - Erstellt normale Welten für Minigames
 */
public class MinigameGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Erstelle eine normale Welt mit flachem Terrain
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Stein von Y=1 bis Y=60
                for (int y = 1; y <= 60; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Gras auf der obersten Schicht
                chunkData.setBlock(x, 60, z, Material.GRASS_BLOCK);
                
                // Luft von Y=61 bis Y=100
                for (int y = 61; y <= 100; y++) {
                    chunkData.setBlock(x, y, z, Material.AIR);
                }
            }
        }
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock bereits in generateSurface gesetzt
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Höhlen in Minigame-Welten
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Kein Terrain in Minigame-Welten
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
        return false;
    }
    
    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}
