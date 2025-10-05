package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.PetType;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.models.Pet;
import de.noctivag.skyblock.models.PetBag;
import de.noctivag.skyblock.models.PlayerProfile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class PetService {

    private final SkyblockPluginRefactored plugin;
    private final Random random = new Random();

    public PetService(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
    }

    public CompletableFuture<Pet> createPet(PetType petType, Rarity rarity) {
        return CompletableFuture.supplyAsync(() -> {
            Pet pet = new Pet(petType, rarity);
            
            if (plugin.getSettingsConfig().isDebugMode()) {
                plugin.getLogger().info("Created pet: " + petType.getDisplayName() + " (" + rarity.name() + ")");
            }
            
            return pet;
        });
    }

    public CompletableFuture<Pet> createRandomPet() {
        return CompletableFuture.supplyAsync(() -> {
            // Weighted random pet generation
            PetType petType = getRandomPetType();
            Rarity rarity = getRandomRarity();
            
            return new Pet(petType, rarity);
        });
    }

    private PetType getRandomPetType() {
        PetType[] petTypes = PetType.values();
        return petTypes[random.nextInt(petTypes.length)];
    }

    private Rarity getRandomRarity() {
        // Weighted rarity distribution
        double roll = random.nextDouble();
        
        if (roll < 0.01) { // 1% chance
            return Rarity.LEGENDARY;
        } else if (roll < 0.05) { // 4% chance
            return Rarity.EPIC;
        } else if (roll < 0.15) { // 10% chance
            return Rarity.RARE;
        } else if (roll < 0.35) { // 20% chance
            return Rarity.UNCOMMON;
        } else { // 65% chance
            return Rarity.COMMON;
        }
    }

    public CompletableFuture<Boolean> addPetToPlayer(Player player, Pet pet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
                if (playerProfileService == null) {
                    return false;
                }

                PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
                if (profile == null) {
                    return false;
                }

                // Get or create pet bag
                PetBag petBag = profile.getPetBag();
                if (petBag == null) {
                    petBag = new PetBag();
                    profile.setPetBag(petBag);
                }

                // Add pet to bag
                petBag.addPet(pet);

                player.sendMessage("§aDu hast ein neues Pet erhalten: " + pet.getPetType().getDisplayName() + " (" + pet.getRarity().getDisplayName() + ")");

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Added pet " + pet.getPetType().name() + " (" + pet.getRarity().name() + ") to player " + player.getName());
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error adding pet to player: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> setActivePet(Player player, Pet pet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
                if (playerProfileService == null) {
                    return false;
                }

                PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
                if (profile == null) {
                    return false;
                }

                PetBag petBag = profile.getPetBag();
                if (petBag == null) {
                    return false;
                }

                // Set active pet
                petBag.setActivePet(pet);

                player.sendMessage("§aAktives Pet geändert zu: " + pet.getPetType().getDisplayName() + " (Level " + pet.getLevel() + ")");

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Player " + player.getName() + " set active pet to " + pet.getPetType().name());
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error setting active pet: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> feedPet(Player player, Pet pet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!pet.canBeFed()) {
                    player.sendMessage("§cDieses Pet kann noch nicht gefüttert werden!");
                    return false;
                }

                pet.feed();
                player.sendMessage("§aPet gefüttert! +50 XP für " + pet.getPetType().getDisplayName());

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Player " + player.getName() + " fed pet " + pet.getPetType().name());
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error feeding pet: " + e.getMessage());
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> upgradePet(Player player, Pet pet, int levels) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (pet.getLevel() >= pet.getPetType().getMaxLevel()) {
                    player.sendMessage("§cDieses Pet hat bereits das maximale Level erreicht!");
                    return false;
                }

                int newLevel = Math.min(pet.getLevel() + levels, pet.getPetType().getMaxLevel());
                pet.setLevel(newLevel);

                player.sendMessage("§aPet auf Level " + newLevel + " aufgewertet!");

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Player " + player.getName() + " upgraded pet " + pet.getPetType().name() + " to level " + newLevel);
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error upgrading pet: " + e.getMessage());
                return false;
            }
        });
    }

    public Map<String, Double> getPlayerStatBonuses(Player player) {
        PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        if (playerProfileService == null) {
            return Map.of();
        }

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            return Map.of();
        }

        PetBag petBag = profile.getPetBag();
        if (petBag == null) {
            return Map.of();
        }

        return petBag.getTotalStatBonuses();
    }

    public List<Pet> getPlayerPets(Player player) {
        PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        if (playerProfileService == null) {
            return List.of();
        }

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            return List.of();
        }

        PetBag petBag = profile.getPetBag();
        if (petBag == null) {
            return List.of();
        }

        return petBag.getAllPets();
    }

    public Pet getPlayerActivePet(Player player) {
        PlayerProfileService playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        if (playerProfileService == null) {
            return null;
        }

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            return null;
        }

        PetBag petBag = profile.getPetBag();
        if (petBag == null) {
            return null;
        }

        return petBag.getActivePet();
    }

    /**
     * Upgraded die Rarity eines Pets
     * @param player Der Spieler
     * @param pet Das zu upgradende Pet
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> upgradePetRarity(Player player, Pet pet) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Prüfe ob das Pet upgegradet werden kann
                if (pet.getRarity() == Rarity.LEGENDARY) {
                    return false;
                }

                // Upgrade die Rarity
                pet.upgradeRarity();

                player.sendMessage("§aPet Rarity upgegradet zu: " + pet.getRarity().getGermanName());

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Player " + player.getName() + " upgraded pet " + pet.getPetType().name() + " rarity to " + pet.getRarity().name());
                }

                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error upgrading pet rarity: " + e.getMessage());
                return false;
            }
        });
    }
}
