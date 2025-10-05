package de.noctivag.skyblock.features.pets.types;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Player Pets class for storing player pet data
 */
public class PlayerPets {
    private UUID playerId;
    private Map<PetType, Integer> petLevels;
    private Map<PetType, Long> petExperience;
    private Map<PetType, Boolean> petUnlocked;
    private Map<PetType, PetRarity> petRarities;
    private PetType activePet;

    public PlayerPets(UUID playerId) {
        this.playerId = playerId;
        this.petLevels = new HashMap<>();
        this.petExperience = new HashMap<>();
        this.petUnlocked = new HashMap<>();
        this.petRarities = new HashMap<>();
        this.activePet = null;
        
        // Initialize all pets as locked
        for (PetType petType : PetType.values()) {
            petLevels.put(petType, 0);
            petExperience.put(petType, 0L);
            petUnlocked.put(petType, false);
            petRarities.put(petType, PetRarity.COMMON);
        }
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Map<PetType, Integer> getPetLevels() {
        return petLevels;
    }

    public Map<PetType, Long> getPetExperience() {
        return petExperience;
    }

    public Map<PetType, Boolean> getPetUnlocked() {
        return petUnlocked;
    }

    public Map<PetType, PetRarity> getPetRarities() {
        return petRarities;
    }

    public PetType getActivePet() {
        return activePet;
    }

    public void setActivePet(PetType activePet) {
        this.activePet = activePet;
    }

    public int getPetLevel(PetType petType) {
        return petLevels.getOrDefault(petType, 0);
    }

    public long getPetExperience(PetType petType) {
        return petExperience.getOrDefault(petType, 0L);
    }

    public boolean isPetUnlocked(PetType petType) {
        return petUnlocked.getOrDefault(petType, false);
    }

    public PetRarity getPetRarity(PetType petType) {
        return petRarities.getOrDefault(petType, PetRarity.COMMON);
    }

    public void setPetLevel(PetType petType, int level) {
        petLevels.put(petType, level);
    }

    public void setPetExperience(PetType petType, long experience) {
        petExperience.put(petType, experience);
    }

    public void setPetUnlocked(PetType petType, boolean unlocked) {
        petUnlocked.put(petType, unlocked);
    }

    public void setPetRarity(PetType petType, PetRarity rarity) {
        petRarities.put(petType, rarity);
    }

    public void addPetExperience(PetType petType, long experience) {
        long currentExp = getPetExperience(petType);
        petExperience.put(petType, currentExp + experience);
    }
}
