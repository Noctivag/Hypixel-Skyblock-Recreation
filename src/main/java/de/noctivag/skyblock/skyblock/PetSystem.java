package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pet System - Manages player pets
 */
public class PetSystem {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerPets> playerPets = new ConcurrentHashMap<>();

    public PetSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Initialize player pets
     */
    public void initializePlayer(Player player) {
        UUID playerId = player.getUniqueId();

        PlayerPets pets = playerPets.computeIfAbsent(playerId, k -> 
            new PlayerPets(playerId));
        
        plugin.getLogger().info("Initialized pets for player: " + player.getName());
    }
    
    /**
     * Get player pets
     */
    public PlayerPets getPlayerPets(UUID playerId) {
        return playerPets.get(playerId);
    }
    
    /**
     * Add pet
     */
    public void addPet(UUID playerId, String petName, int level) {
        PlayerPets pets = playerPets.get(playerId);
        if (pets != null) {
            pets.addPet(petName, level);
        }
    }
    
    /**
     * Player Pets data
     */
    public static class PlayerPets {
        private final UUID playerId;
        private final Map<String, Integer> pets;
        private String activePet;

        public PlayerPets(UUID playerId) {
            this.playerId = playerId;
            this.pets = new ConcurrentHashMap<>();
            this.activePet = null;
        }

        public void addPet(String petName, int level) {
            pets.put(petName, level);
        }
        
        public int getPetLevel(String petName) {
            return pets.getOrDefault(petName, 0);
        }
        
        public String getActivePet() {
            return activePet;
        }
        
        public void setActivePet(String petName) {
            this.activePet = petName;
        }
        
        public Map<String, Integer> getAllPets() {
            return new ConcurrentHashMap<>(pets);
        }
    }

    // Pet inner class for compatibility
    public static class Pet {
        private final String name;
        private final int level;

        public Pet(String name, int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() { return name; }
        public int getLevel() { return level; }
    }

    // Compatibility methods
    public java.util.List<Pet> getPlayerPets(Player player) {
        PlayerPets pets = getPlayerPets(player.getUniqueId());
        if (pets == null) return new java.util.ArrayList<>();
        return pets.getAllPets().entrySet().stream()
            .map(e -> new Pet(e.getKey(), e.getValue()))
            .collect(java.util.stream.Collectors.toList());
    }

    public Pet getActivePet(Player player) {
        PlayerPets pets = getPlayerPets(player.getUniqueId());
        if (pets == null || pets.getActivePet() == null) return null;
        String activePetName = pets.getActivePet();
        int level = pets.getPetLevel(activePetName);
        return new Pet(activePetName, level);
    }
}