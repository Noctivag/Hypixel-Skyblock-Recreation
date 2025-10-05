package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.PetType;
import de.noctivag.skyblock.enums.Rarity;

import java.util.*;

public class PetBag {
    
    private final Map<String, Pet> pets;
    private Pet activePet;
    
    public PetBag() {
        this.pets = new HashMap<>();
        this.activePet = null;
    }
    
    public void addPet(Pet pet) {
        String petKey = pet.getPetType().name() + "_" + pet.getRarity().name();
        pets.put(petKey, pet);
    }
    
    public void removePet(PetType petType, Rarity rarity) {
        String petKey = petType.name() + "_" + rarity.name();
        Pet removedPet = pets.remove(petKey);
        
        // If the removed pet was active, clear active pet
        if (activePet != null && activePet.equals(removedPet)) {
            activePet = null;
        }
    }
    
    public Pet getPet(PetType petType, Rarity rarity) {
        String petKey = petType.name() + "_" + rarity.name();
        return pets.get(petKey);
    }
    
    public boolean hasPet(PetType petType, Rarity rarity) {
        String petKey = petType.name() + "_" + rarity.name();
        return pets.containsKey(petKey);
    }
    
    public void setActivePet(Pet pet) {
        if (pet != null && pets.containsValue(pet)) {
            // Deactivate current active pet
            if (activePet != null) {
                activePet.setActive(false);
            }
            
            // Set new active pet
            activePet = pet;
            pet.setActive(true);
        }
    }
    
    public void clearActivePet() {
        if (activePet != null) {
            activePet.setActive(false);
            activePet = null;
        }
    }
    
    public List<Pet> getAllPets() {
        return new ArrayList<>(pets.values());
    }
    
    public List<Pet> getPetsByType(PetType petType) {
        return pets.values().stream()
                .filter(pet -> pet.getPetType() == petType)
                .toList();
    }
    
    public List<Pet> getPetsByRarity(Rarity rarity) {
        return pets.values().stream()
                .filter(pet -> pet.getRarity() == rarity)
                .toList();
    }
    
    public List<Pet> getActivePets() {
        return pets.values().stream()
                .filter(Pet::isActive)
                .toList();
    }

    /**
     * Gibt das aktive Pet zurück
     * @return Das aktive Pet oder null
     */
    public Pet getActivePet() {
        return activePet;
    }
    
    public int getTotalPets() {
        return pets.size();
    }
    
    public int getPetsByRarityCount(Rarity rarity) {
        return (int) pets.values().stream()
                .filter(pet -> pet.getRarity() == rarity)
                .count();
    }
    
    public Pet getBestPetByType(PetType petType) {
        return pets.values().stream()
                .filter(pet -> pet.getPetType() == petType)
                .max(Comparator.comparing(Pet::getLevel)
                        .thenComparing(Pet::getRarity, Comparator.comparing(Rarity::ordinal)))
                .orElse(null);
    }
    
    public Pet getHighestLevelPet() {
        return pets.values().stream()
                .max(Comparator.comparing(Pet::getLevel)
                        .thenComparing(Pet::getRarity, Comparator.comparing(Rarity::ordinal)))
                .orElse(null);
    }
    
    public Map<String, Double> getTotalStatBonuses() {
        Map<String, Double> totalBonuses = new HashMap<>();
        
        if (activePet != null) {
            // Add active pet bonuses
            String[] statTypes = {"damage", "strength", "health", "defense", "speed", "crit_chance", 
                                "crit_damage", "farming_fortune", "mining_fortune", "luck", "experience"};
            
            for (String statType : statTypes) {
                double bonus = activePet.getStatBoost(statType);
                if (bonus > 0) {
                    totalBonuses.put(statType, totalBonuses.getOrDefault(statType, 0.0) + bonus);
                }
            }
        }
        
        return totalBonuses;
    }
    
    public void feedAllPets() {
        pets.values().forEach(pet -> {
            if (pet.canBeFed()) {
                pet.feed();
            }
        });
    }
    
    public List<Pet> getPetsThatCanBeFed() {
        return pets.values().stream()
                .filter(Pet::canBeFed)
                .toList();
    }
}
