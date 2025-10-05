package de.noctivag.skyblock.pets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Pet Bag - Represents a player's pet collection
 */
public class PetBag {
    
    private final UUID ownerId;
    private final List<Pet> pets;
    private Pet activePet;
    
    public PetBag(UUID ownerId) {
        this.ownerId = ownerId;
        this.pets = new ArrayList<>();
        this.activePet = null;
    }
    
    /**
     * Get the owner ID
     */
    public UUID getOwnerId() {
        return ownerId;
    }
    
    /**
     * Get all pets
     */
    public List<Pet> getPets() {
        return new ArrayList<>(pets);
    }
    
    /**
     * Add a pet
     */
    public boolean addPet(Pet pet) {
        if (pet.getOwnerId().equals(ownerId)) {
            pets.add(pet);
            return true;
        }
        return false;
    }
    
    /**
     * Remove a pet
     */
    public boolean removePet(Pet pet) {
        if (pets.remove(pet)) {
            if (activePet != null && activePet.getPetId().equals(pet.getPetId())) {
                activePet = null;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Get a pet by ID
     */
    public Pet getPet(UUID petId) {
        for (Pet pet : pets) {
            if (pet.getPetId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }
    
    /**
     * Get the active pet
     */
    public Pet getActivePet() {
        return activePet;
    }
    
    /**
     * Set the active pet
     */
    public boolean setActivePet(Pet pet) {
        if (pet != null && pets.contains(pet)) {
            // Deactivate current active pet
            if (activePet != null) {
                activePet.setActive(false);
            }
            
            // Set new active pet
            activePet = pet;
            pet.setActive(true);
            return true;
        }
        return false;
    }
    
    /**
     * Deactivate the active pet
     */
    public void deactivateActivePet() {
        if (activePet != null) {
            activePet.setActive(false);
            activePet = null;
        }
    }
    
    /**
     * Get the number of pets
     */
    public int getPetCount() {
        return pets.size();
    }
    
    /**
     * Check if the bag is empty
     */
    public boolean isEmpty() {
        return pets.isEmpty();
    }
}

