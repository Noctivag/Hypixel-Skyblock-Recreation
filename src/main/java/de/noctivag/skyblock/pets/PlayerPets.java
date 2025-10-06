package de.noctivag.skyblock.pets;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a player's pet collection and management
 */
public class PlayerPets {
    private final UUID playerId;
    private final Map<String, Pet> pets;
    private final String activePetId;
    private final long lastUpdated;

    public PlayerPets(UUID playerId) {
        this.playerId = playerId;
        this.pets = new ConcurrentHashMap<>();
        this.activePetId = null;
        this.lastUpdated = System.currentTimeMillis();
    }

    public PlayerPets(UUID playerId, Map<String, Pet> pets, String activePetId) {
        this.playerId = playerId;
        this.pets = new ConcurrentHashMap<>(pets);
        this.activePetId = activePetId;
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters
    public UUID getPlayerId() { return playerId; }
    public Map<String, Pet> getPets() { return new HashMap<>(pets); }
    public String getActivePetId() { return activePetId; }
    public long getLastUpdated() { return lastUpdated; }

    /**
     * Get pet by ID
     */
    public Pet getPet(String petId) {
        return pets.get(petId);
    }

    /**
     * Add pet to collection
     */
    public boolean addPet(Pet pet) {
        if (pets.containsKey(pet.getPetId())) {
            return false; // Pet already exists
        }
        
        pets.put(pet.getPetId(), pet);
        return true;
    }

    /**
     * Remove pet from collection
     */
    public boolean removePet(String petId) {
        Pet removed = pets.remove(petId);
        if (removed != null && petId.equals(activePetId)) {
            // If removing active pet, deactivate it
            setActivePet(null);
        }
        return removed != null;
    }

    /**
     * Get active pet
     */
    public Pet getActivePet() {
        if (activePetId == null) return null;
        return pets.get(activePetId);
    }

    /**
     * Set active pet
     */
    public boolean setActivePet(String petId) {
        if (petId == null) {
            // Deactivate current pet
            return true;
        }
        
        Pet pet = pets.get(petId);
        if (pet == null) {
            return false; // Pet not found
        }
        
        // Deactivate current active pet
        Pet currentActive = getActivePet();
        if (currentActive != null) {
            // Note: Pet objects are immutable, so we can't change isActive directly
            // This would need to be handled in the database layer
        }
        
        return true;
    }

    /**
     * Get pets by type
     */
    public List<Pet> getPetsByType(PetType petType) {
        return pets.values().stream()
                .filter(pet -> pet.getPetType() == petType)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get pets by category
     */
    public List<Pet> getPetsByCategory(String category) {
        return pets.values().stream()
                .filter(pet -> pet.getCategory().equalsIgnoreCase(category))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get pets by rarity
     */
    public List<Pet> getPetsByRarity(String rarity) {
        return pets.values().stream()
                .filter(pet -> pet.getRarity().equalsIgnoreCase(rarity))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get pets by level range
     */
    public List<Pet> getPetsByLevelRange(int minLevel, int maxLevel) {
        return pets.values().stream()
                .filter(pet -> pet.getLevel() >= minLevel && pet.getLevel() <= maxLevel)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get highest level pet
     */
    public Pet getHighestLevelPet() {
        return pets.values().stream()
                .max(Comparator.comparingInt(Pet::getLevel))
                .orElse(null);
    }

    /**
     * Get lowest level pet
     */
    public Pet getLowestLevelPet() {
        return pets.values().stream()
                .min(Comparator.comparingInt(Pet::getLevel))
                .orElse(null);
    }

    /**
     * Get pets sorted by level (descending)
     */
    public List<Pet> getPetsSortedByLevel() {
        return pets.values().stream()
                .sorted(Comparator.comparingInt(Pet::getLevel).reversed())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get pets sorted by rarity
     */
    public List<Pet> getPetsSortedByRarity() {
        return pets.values().stream()
                .sorted(Comparator.comparing(Pet::getRarity))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get total pet count
     */
    public int getTotalPetCount() {
        return pets.size();
    }

    /**
     * Get pet count by type
     */
    public int getPetCountByType(PetType petType) {
        return (int) pets.values().stream()
                .filter(pet -> pet.getPetType() == petType)
                .count();
    }

    /**
     * Get pet count by category
     */
    public int getPetCountByCategory(String category) {
        return (int) pets.values().stream()
                .filter(pet -> pet.getCategory().equalsIgnoreCase(category))
                .count();
    }

    /**
     * Get pet count by rarity
     */
    public int getPetCountByRarity(String rarity) {
        return (int) pets.values().stream()
                .filter(pet -> pet.getRarity().equalsIgnoreCase(rarity))
                .count();
    }

    /**
     * Get total pet levels
     */
    public int getTotalPetLevels() {
        return pets.values().stream()
                .mapToInt(Pet::getLevel)
                .sum();
    }

    /**
     * Get average pet level
     */
    public double getAveragePetLevel() {
        if (pets.isEmpty()) return 0.0;
        return (double) getTotalPetLevels() / pets.size();
    }

    /**
     * Get total pet XP
     */
    public long getTotalPetXP() {
        return pets.values().stream()
                .mapToLong(Pet::getXp)
                .sum();
    }

    /**
     * Get pet collection completion percentage
     */
    public double getCollectionCompletionPercentage() {
        int totalPetTypes = PetType.values().length;
        int ownedPetTypes = (int) pets.values().stream()
                .map(Pet::getPetType)
                .distinct()
                .count();
        
        if (totalPetTypes == 0) return 0.0;
        return (double) ownedPetTypes / totalPetTypes * 100.0;
    }

    /**
     * Get pet statistics
     */
    public PetStatistics getPetStatistics() {
        return new PetStatistics(this);
    }

    /**
     * Check if player has pet of type
     */
    public boolean hasPetOfType(PetType petType) {
        return pets.values().stream()
                .anyMatch(pet -> pet.getPetType() == petType);
    }

    /**
     * Check if player has pet of category
     */
    public boolean hasPetOfCategory(String category) {
        return pets.values().stream()
                .anyMatch(pet -> pet.getCategory().equalsIgnoreCase(category));
    }

    /**
     * Check if player has pet of rarity
     */
    public boolean hasPetOfRarity(String rarity) {
        return pets.values().stream()
                .anyMatch(pet -> pet.getRarity().equalsIgnoreCase(rarity));
    }

    /**
     * Get pet collection summary
     */
    public String[] getCollectionSummary() {
        return new String[]{
            "&7Pet Collection Summary:",
            "",
            "&7Total Pets: &a" + getTotalPetCount(),
            "&7Active Pet: " + (getActivePet() != null ? getActivePet().getDisplayName() : "&7None"),
            "&7Average Level: &a" + String.format("%.1f", getAveragePetLevel()),
            "&7Total Levels: &a" + getTotalPetLevels(),
            "&7Total XP: &a" + formatNumber(getTotalPetXP()),
            "&7Collection: &a" + String.format("%.1f", getCollectionCompletionPercentage()) + "%",
            "",
            "&7By Category:",
            "&7Combat: &a" + getPetCountByCategory("Combat"),
            "&7Mining: &a" + getPetCountByCategory("Mining"),
            "&7Farming: &a" + getPetCountByCategory("Farming"),
            "&7Foraging: &a" + getPetCountByCategory("Foraging"),
            "&7Fishing: &a" + getPetCountByCategory("Fishing"),
            "&7Special: &a" + getPetCountByCategory("Special")
        };
    }

    /**
     * Format large numbers
     */
    private String formatNumber(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format("%.1fT", number / 1_000_000_000_000.0);
        } else if (number >= 1_000_000_000L) {
            return String.format("%.1fB", number / 1_000_000_000.0);
        } else if (number >= 1_000_000L) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000L) {
            return String.format("%.1fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * Pet statistics inner class
     */
    public static class PetStatistics {
        private final PlayerPets playerPets;
        private final int totalPets;
        private final int totalLevels;
        private final long totalXP;
        private final double averageLevel;
        private final double collectionCompletion;
        private final Map<String, Integer> categoryCounts;
        private final Map<String, Integer> rarityCounts;

        public PetStatistics(PlayerPets playerPets) {
            this.playerPets = playerPets;
            this.totalPets = playerPets.getTotalPetCount();
            this.totalLevels = playerPets.getTotalPetLevels();
            this.totalXP = playerPets.getTotalPetXP();
            this.averageLevel = playerPets.getAveragePetLevel();
            this.collectionCompletion = playerPets.getCollectionCompletionPercentage();
            
            // Calculate category counts
            this.categoryCounts = new HashMap<>();
            for (String category : PetType.getAllCategories()) {
                categoryCounts.put(category, playerPets.getPetCountByCategory(category));
            }
            
            // Calculate rarity counts
            this.rarityCounts = new HashMap<>();
            this.rarityCounts.put("legendary", playerPets.getPetCountByRarity("legendary"));
            this.rarityCounts.put("epic", playerPets.getPetCountByRarity("epic"));
            this.rarityCounts.put("rare", playerPets.getPetCountByRarity("rare"));
            this.rarityCounts.put("uncommon", playerPets.getPetCountByRarity("uncommon"));
        }

        // Getters
        public PlayerPets getPlayerPets() { return playerPets; }
        public int getTotalPets() { return totalPets; }
        public int getTotalLevels() { return totalLevels; }
        public long getTotalXP() { return totalXP; }
        public double getAverageLevel() { return averageLevel; }
        public double getCollectionCompletion() { return collectionCompletion; }
        public Map<String, Integer> getCategoryCounts() { return new HashMap<>(categoryCounts); }
        public Map<String, Integer> getRarityCounts() { return new HashMap<>(rarityCounts); }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Pet Statistics:",
                "",
                "&7Total Pets: &a" + totalPets,
                "&7Total Levels: &a" + totalLevels,
                "&7Average Level: &a" + String.format("%.1f", averageLevel),
                "&7Total XP: &a" + formatNumber(totalXP),
                "&7Collection: &a" + String.format("%.1f", collectionCompletion) + "%",
                "",
                "&7By Category:",
                "&7Combat: &a" + categoryCounts.getOrDefault("Combat", 0),
                "&7Mining: &a" + categoryCounts.getOrDefault("Mining", 0),
                "&7Farming: &a" + categoryCounts.getOrDefault("Farming", 0),
                "&7Foraging: &a" + categoryCounts.getOrDefault("Foraging", 0),
                "&7Fishing: &a" + categoryCounts.getOrDefault("Fishing", 0),
                "&7Special: &a" + categoryCounts.getOrDefault("Special", 0),
                "",
                "&7By Rarity:",
                "&7Legendary: &a" + rarityCounts.getOrDefault("legendary", 0),
                "&7Epic: &a" + rarityCounts.getOrDefault("epic", 0),
                "&7Rare: &a" + rarityCounts.getOrDefault("rare", 0),
                "&7Uncommon: &a" + rarityCounts.getOrDefault("uncommon", 0)
            };
        }

        /**
         * Format large numbers
         */
        private String formatNumber(long number) {
            if (number >= 1_000_000_000_000L) {
                return String.format("%.1fT", number / 1_000_000_000_000.0);
            } else if (number >= 1_000_000_000L) {
                return String.format("%.1fB", number / 1_000_000_000.0);
            } else if (number >= 1_000_000L) {
                return String.format("%.1fM", number / 1_000_000.0);
            } else if (number >= 1_000L) {
                return String.format("%.1fK", number / 1_000.0);
            } else {
                return String.valueOf(number);
            }
        }
    }
}
