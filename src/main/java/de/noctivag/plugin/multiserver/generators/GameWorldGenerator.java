package de.noctivag.plugin.multiserver.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * World Generator für Game Server (Dungeons, etc.)
 */
public class GameWorldGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Erstelle eine flache Oberfläche für Game Server
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Setze Bedrock als Basis
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Setze Stone als Hauptmaterial
                for (int y = 1; y < 60; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Setze Grass als Oberfläche
                chunkData.setBlock(x, 60, z, Material.GRASS_BLOCK);
            }
        }
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock wird bereits in generateSurface gesetzt
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Höhlen für Game Server
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Noise-Generierung für Game Server
    }
}
