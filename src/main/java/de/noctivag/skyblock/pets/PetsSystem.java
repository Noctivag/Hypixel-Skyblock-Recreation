package de.noctivag.skyblock.pets;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Pets System - Manages pet collection, leveling, and abilities
 */
public class PetsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerPets> playerPets;
    private final BukkitTask petTask;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public PetsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.playerPets = new ConcurrentHashMap<>();
        
        // Start pet management task
        this.petTask = new BukkitRunnable() {
            @Override
            public void run() {
                processPetUpdates();
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing PetsSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerPets(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("PetsSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down PetsSystem...");

        // Cancel pet task
        if (petTask != null && !petTask.isCancelled()) {
            petTask.cancel();
        }

        // Save all player data
        saveAllPlayerPets();

        playerPets.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("PetsSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "PetsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Pets system is always enabled when running
    }

    /**
     * Load player pets from database
     */
    public void loadPlayerPets(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            PlayerPets pets = new PlayerPets(playerId);
            playerPets.put(playerId, pets);
            plugin.getLogger().info("Loaded pets for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load pets for player " + playerId + ": " + e.getMessage());
            // Create new pets if loading fails
            playerPets.put(playerId, new PlayerPets(playerId));
        }
    }

    /**
     * Save player pets to database
     */
    public void savePlayerPets(UUID playerId) {
        try {
            // Save to database (placeholder - implement actual database saving)
            plugin.getLogger().info("Saved pets for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save pets for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player pets
     */
    public void saveAllPlayerPets() {
        for (UUID playerId : playerPets.keySet()) {
            savePlayerPets(playerId);
        }
    }

    /**
     * Get player pets
     */
    public PlayerPets getPlayerPets(UUID playerId) {
        return playerPets.get(playerId);
    }

    /**
     * Get player pets (by Player object)
     */
    public PlayerPets getPlayerPets(Player player) {
        return getPlayerPets(player.getUniqueId());
    }

    /**
     * Create a new pet for player
     */
    public Pet createPet(Player player, PetType petType, int level, long xp) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) {
            loadPlayerPets(player.getUniqueId());
            playerPets = getPlayerPets(player);
        }

        // Generate unique pet ID
        String petId = player.getUniqueId().toString() + "_" + petType.name() + "_" + System.currentTimeMillis();
        
        // Create pet
        Pet pet = new Pet(petId, player.getUniqueId(), petType, level, xp, false);
        
        // Add to player's collection
        if (playerPets.addPet(pet)) {
            player.sendMessage("§aNew pet obtained: " + pet.getDisplayName());
            return pet;
        }
        
        return null;
    }

    /**
     * Give pet to player
     */
    public boolean givePet(Player player, PetType petType, int level, long xp) {
        Pet pet = createPet(player, petType, level, xp);
        return pet != null;
    }

    /**
     * Remove pet from player
     */
    public boolean removePet(Player player, String petId) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        return playerPets.removePet(petId);
    }

    /**
     * Set active pet for player
     */
    public boolean setActivePet(Player player, String petId) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        boolean success = playerPets.setActivePet(petId);
        if (success) {
            Pet pet = playerPets.getPet(petId);
            if (pet != null) {
                player.sendMessage("§aActive pet set to: " + pet.getDisplayName());
            } else {
                player.sendMessage("§aNo active pet selected");
            }
        }
        
        return success;
    }

    /**
     * Get active pet for player
     */
    public Pet getActivePet(Player player) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return null;
        
        return playerPets.getActivePet();
    }

    /**
     * Add XP to pet
     */
    public boolean addPetXP(Player player, String petId, long xp) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        Pet pet = playerPets.getPet(petId);
        if (pet == null) return false;
        
        // Calculate new level and XP
        long newXP = pet.getXp() + xp;
        int newLevel = calculateLevelFromXP(pet.getPetType(), newXP);
        
        // Create updated pet
        Pet updatedPet = new Pet(pet.getPetId(), pet.getOwnerId(), pet.getPetType(), 
                                newLevel, newXP, pet.isActive(), pet.getCreatedAt());
        
        // Replace pet in collection
        playerPets.removePet(petId);
        playerPets.addPet(updatedPet);
        
        // Check for level up
        if (newLevel > pet.getLevel()) {
            player.sendMessage("§a§lPET LEVEL UP! §r" + updatedPet.getDisplayName() + " §aLevel " + newLevel);
        }
        
        return true;
    }

    /**
     * Get pet by ID
     */
    public Pet getPet(String petId) {
        for (PlayerPets playerPets : this.playerPets.values()) {
            Pet pet = playerPets.getPet(petId);
            if (pet != null) return pet;
        }
        return null;
    }

    /**
     * Get all pets for player
     */
    public List<Pet> getAllPets(Player player) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return new ArrayList<>();
        
        return new ArrayList<>(playerPets.getPets().values());
    }

    /**
     * Get pets by type for player
     */
    public List<Pet> getPetsByType(Player player, PetType petType) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return new ArrayList<>();
        
        return playerPets.getPetsByType(petType);
    }

    /**
     * Get pets by category for player
     */
    public List<Pet> getPetsByCategory(Player player, String category) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return new ArrayList<>();
        
        return playerPets.getPetsByCategory(category);
    }

    /**
     * Get pets by rarity for player
     */
    public List<Pet> getPetsByRarity(Player player, String rarity) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return new ArrayList<>();
        
        return playerPets.getPetsByRarity(rarity);
    }

    /**
     * Get pet statistics for player
     */
    public PlayerPets.PetStatistics getPetStatistics(Player player) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return null;
        
        return playerPets.getPetStatistics();
    }

    /**
     * Get pet type by name
     */
    public PetType getPetTypeByName(String name) {
        for (PetType petType : PetType.values()) {
            if (petType.getDisplayName().equalsIgnoreCase(name)) {
                return petType;
            }
        }
        return null;
    }

    /**
     * Get all pet types
     */
    public PetType[] getAllPetTypes() {
        return PetType.values();
    }

    /**
     * Get pet types by category
     */
    public PetType[] getPetTypesByCategory(String category) {
        return PetType.getByCategory(category);
    }

    /**
     * Get all categories
     */
    public String[] getAllCategories() {
        return PetType.getAllCategories();
    }

    /**
     * Get pet types by rarity
     */
    public PetType[] getPetTypesByRarity(String rarity) {
        return java.util.Arrays.stream(PetType.values())
                .filter(petType -> petType.getRarity().equalsIgnoreCase(rarity))
                .toArray(PetType[]::new);
    }

    /**
     * Check if player has pet of type
     */
    public boolean hasPetOfType(Player player, PetType petType) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        return playerPets.hasPetOfType(petType);
    }

    /**
     * Check if player has pet of category
     */
    public boolean hasPetOfCategory(Player player, String category) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        return playerPets.hasPetOfCategory(category);
    }

    /**
     * Check if player has pet of rarity
     */
    public boolean hasPetOfRarity(Player player, String rarity) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return false;
        
        return playerPets.hasPetOfRarity(rarity);
    }

    /**
     * Get total pet count for player
     */
    public int getTotalPetCount(Player player) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return 0;
        
        return playerPets.getTotalPetCount();
    }

    /**
     * Get collection completion percentage for player
     */
    public double getCollectionCompletionPercentage(Player player) {
        PlayerPets playerPets = getPlayerPets(player);
        if (playerPets == null) return 0.0;
        
        return playerPets.getCollectionCompletionPercentage();
    }

    /**
     * Process pet updates
     */
    private void processPetUpdates() {
        // Process pet logic here
        // This would include pet abilities, XP gain, etc.
        // For example, passive XP gain, ability effects, etc.
        // Currently just iterate through pets to keep them active
        for (PlayerPets playerPets : this.playerPets.values()) {
            // Pet processing logic would go here
            if (playerPets.getTotalPetCount() == 0) {
                // Handle empty pet collections if needed
            }
        }
    }

    /**
     * Calculate level from XP
     */
    private int calculateLevelFromXP(PetType petType, long xp) {
        int level = 1;
        for (int i = 2; i <= petType.getMaxLevel(); i++) {
            if (xp >= petType.getXPRequirement(i)) {
                level = i;
            } else {
                break;
            }
        }
        return level;
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerPets(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerPets(player.getUniqueId());
    }
}
