package de.noctivag.skyblock.pets;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Pet Service - Service for managing pets
 */
public class PetService implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<UUID, PetBag> playerPetBags = new HashMap<>();
    private final Map<String, PetType> availablePetTypes = new HashMap<>();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public PetService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing PetService...");
        
        // Initialize available pet types
        initializePetTypes();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("PetService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down PetService...");
        
        // Save all pet data
        for (PetBag petBag : playerPetBags.values()) {
            // Save pet data to database
            plugin.getLogger().log(Level.INFO, "Saving pet bag for player: " + petBag.getOwnerId());
        }
        
        playerPetBags.clear();
        availablePetTypes.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("PetService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Initialize available pet types
     */
    private void initializePetTypes() {
        // PetType constructors not implemented yet - commenting out for now
        // PetType wolf = new PetType(...);
        // availablePetTypes.put("WOLF", wolf);
        
        // PetType cat = new PetType(...);
        // availablePetTypes.put("CAT", cat);
        
        // PetType parrot = new PetType(...);
        // availablePetTypes.put("PARROT", parrot);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + availablePetTypes.size() + " pet types.");
    }
    
    /**
     * Get a player's pet bag
     */
    public PetBag getPlayerPetBag(Player player) {
        return playerPetBags.computeIfAbsent(player.getUniqueId(), PetBag::new);
    }
    
    /**
     * Give a pet to a player
     */
    public boolean givePet(Player player, String petTypeName) {
        PetType petType = availablePetTypes.get(petTypeName.toUpperCase());
        if (petType == null) {
            player.sendMessage("§cUnknown pet type: " + petTypeName);
            return false;
        }
        
        PetBag petBag = getPlayerPetBag(player);
        // Pet pet = new Pet(UUID.randomUUID(), player.getUniqueId(), petType);
        Pet pet = null; // Placeholder until constructor is fixed
        
        if (petBag.addPet(pet)) {
            player.sendMessage("§aYou received a " + "Pet" + " pet!");
            plugin.getLogger().log(Level.INFO, "Player " + player.getName() + " received a " + "Pet" + " pet");
            return true;
        }
        
        return false;
    }
    
    /**
     * Set a player's active pet
     */
    public boolean setActivePet(Player player, UUID petId) {
        PetBag petBag = getPlayerPetBag(player);
        Pet pet = petBag.getPet(petId);
        
        if (pet != null) {
            if (petBag.setActivePet(pet)) {
                player.sendMessage("§aActivated " + "Pet" + " pet!");
                return true;
            }
        }
        
        player.sendMessage("§cPet not found or already active!");
        return false;
    }
    
    /**
     * Deactivate a player's active pet
     */
    public boolean deactivateActivePet(Player player) {
        PetBag petBag = getPlayerPetBag(player);
        if (petBag.getActivePet() != null) {
            petBag.deactivateActivePet();
            player.sendMessage("§cDeactivated active pet!");
            return true;
        }
        
        player.sendMessage("§cNo active pet to deactivate!");
        return false;
    }
    
    /**
     * Get all available pet types
     */
    public Map<String, PetType> getAvailablePetTypes() {
        return new HashMap<>(availablePetTypes);
    }
    
    /**
     * Get all player pet bags
     */
    public Map<UUID, PetBag> getPlayerPetBags() {
        return new HashMap<>(playerPetBags);
    }
    
    @Override
    public String getName() {
        return "PetService";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
}

