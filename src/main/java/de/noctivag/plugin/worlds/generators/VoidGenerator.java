package de.noctivag.plugin.worlds.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Void Generator - Erstellt leere Welten (für Hub, Arenen, etc.)
 */
public class VoidGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Erstelle eine leere Welt
        // Nur Bedrock am Boden (Y=0)
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock bereits in generateSurface gesetzt
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Höhlen in Void-Welten
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Kein Terrain in Void-Welten
    }
}
