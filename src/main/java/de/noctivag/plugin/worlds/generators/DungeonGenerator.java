package de.noctivag.plugin.worlds.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Dungeon Generator - Erstellt Welten für Dungeon-Instanzen
 */
public class DungeonGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Erstelle eine leere Welt mit Bedrock-Boden
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Bedrock am Boden
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Luft von Y=1 bis Y=100
                for (int y = 1; y <= 100; y++) {
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
        // Keine Höhlen in Dungeon-Welten
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Kein Terrain in Dungeon-Welten
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
