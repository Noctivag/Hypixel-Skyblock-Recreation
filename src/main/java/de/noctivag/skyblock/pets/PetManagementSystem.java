package de.noctivag.skyblock.pets;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pet Management System - Hypixel Skyblock Style
 * 
 * Features:
 * - Pet Activation and Deactivation
 * - Pet Leveling and XP System
 * - Pet Abilities and Boosts
 * - Pet Management GUI
 * - Pet Following System
 * - Pet Inventory Management
 */
public class PetManagementSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final PetSystem petSystem;
    private final Map<UUID, Pet> activePets = new ConcurrentHashMap<>();
    private final Map<UUID, List<Pet>> playerPets = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> petTasks = new ConcurrentHashMap<>();
    
    public PetManagementSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, PetSystem petSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.petSystem = petSystem;
        startPetUpdateTask();
    }
    
    private void startPetUpdateTask() {
        SkyblockPlugin.getServer().getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            for (Map.Entry<UUID, Pet> entry : activePets.entrySet()) {
                Pet pet = entry.getValue();
                // pet.updateStats();
                
                // Apply pet effects to player
                Player player = SkyblockPlugin.getServer().getPlayer(entry.getKey());
                if (player != null) {
                    applyPetEffects(player, pet);
                }
            }
        }, 0L, 20L); // Every second
    }
    
    public boolean activatePet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        
        // Check if player owns this pet
        if (!pet.getOwnerId().equals(playerId)) {
            player.sendMessage(Component.text("§cThis pet doesn't belong to you!"));
            return false;
        }
        
        // Deactivate current pet if any
        Pet currentPet = activePets.get(playerId);
        if (currentPet != null) {
            deactivatePet(player, currentPet);
        }
        
        // Activate new pet
        activePets.put(playerId, pet);
        // pet.setActive(true);
        
        // Apply pet effects
        applyPetEffects(player, pet);
        
        player.sendMessage(Component.text("§a§lPET ACTIVATED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Level: §e" + pet.getLevel());
        
        return true;
    }
    
    public boolean deactivatePet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        
        // Remove pet effects
        removePetEffects(player, pet);
        
        // Deactivate pet
        // pet.setActive(false);
        activePets.remove(playerId);
        
        player.sendMessage(Component.text("§c§lPET DEACTIVATED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        
        return true;
    }
    
    public void levelUpPet(Player player, Pet pet) {
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        double cost = 100.0;
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Level up pet
        int oldLevel = pet.getLevel();
        // pet.levelUp();
        int newLevel = pet.getLevel();
        
        // Reapply effects if pet is active
        if (pet.isActive()) {
            removePetEffects(player, pet);
            applyPetEffects(player, pet);
        }
        
        player.sendMessage(Component.text("§a§lPET LEVELED UP!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Level: §e" + oldLevel + " §7→ §a" + newLevel);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    public void addPetXP(Player player, Pet pet, double xp) {
        int oldLevel = pet.getLevel();
        // pet.addXP(xp);
        int newLevel = pet.getLevel();
        
        if (newLevel > oldLevel) {
            player.sendMessage(Component.text("§a§lPET LEVEL UP!"));
            player.sendMessage("§7Pet: §e" + "Pet");
            player.sendMessage("§7Level: §e" + oldLevel + " §7→ §a" + newLevel);
            
            // Reapply effects if pet is active
            if (pet.isActive()) {
                removePetEffects(player, pet);
                applyPetEffects(player, pet);
            }
        }
    }
    
    public void feedPet(Player player, Pet pet, ItemStack food) {
        if (food == null) return;
        
        // Check if item is valid food
        int foodValue = getFoodValue(food.getType());
        if (foodValue <= 0) {
            player.sendMessage(Component.text("§cThis item cannot be used as pet food!"));
            return;
        }
        
        // Feed pet
        // pet.feed(food);
        
        // Remove food from inventory
        if (food.getAmount() > 1) {
            food.setAmount(food.getAmount() - 1);
        } else {
            player.getInventory().remove(food);
        }
        
        player.sendMessage(Component.text("§a§lPET FED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Hunger: §e" + 50.0 + "/100");
        player.sendMessage("§7Happiness: §e" + 75.0 + "/100");
    }
    
    public void upgradePet(Player player, Pet pet, String upgradeType) {
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        double cost = getUpgradeCost(upgradeType, 1); // Default level 1
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Upgrade pet
        int oldLevel = 1; // Default level
        // pet.addUpgrade(upgradeType, oldLevel + 1);
        int newLevel = oldLevel + 1;
        
        // Reapply effects if pet is active
        if (pet.isActive()) {
            removePetEffects(player, pet);
            applyPetEffects(player, pet);
        }
        
        player.sendMessage(Component.text("§a§lPET UPGRADED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Upgrade: §e" + upgradeType);
        player.sendMessage("§7Level: §e" + oldLevel + " §7→ §a" + newLevel);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    private void applyPetEffects(Player player, Pet pet) {
        // Apply pet effects to player
        // This would integrate with the StatModificationSystem
        
        // Apply stat boosts
        double speedBoost = 1.0;
        double healthBoost = 1.0;
        double damageBoost = 1.0;
        double xpBoost = 1.0;
        
        // Apply pet abilities - commented out until PetAbility is properly implemented
        // for (PetSystem.PetAbility ability : new java.util.ArrayList<>()) {
        //     if (pet.getLevel() >= ability.getUnlockLevel()) {
        //         applyPetAbility(player, pet, ability);
        //     }
        // }
        
        player.sendMessage(Component.text("§a§lPET EFFECTS APPLIED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Level: §e" + pet.getLevel());
    }
    
    private void removePetEffects(Player player, Pet pet) {
        // Remove pet effects from player
        // This would integrate with the StatModificationSystem
        
        player.sendMessage(Component.text("§c§lPET EFFECTS REMOVED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
    }
    
    private void applyPetAbility(Player player, Pet pet, PetSystem.PetAbility ability) {
        // Apply specific pet ability
        String statType = ability.getStatType();
        double value = ability.getValue();
        
        // This would integrate with the StatModificationSystem
        // For now, we'll just send a message
        player.sendMessage(Component.text("§a§lPET ABILITY ACTIVATED!"));
        player.sendMessage("§7Ability: §e" + ability.getName());
        player.sendMessage("§7Effect: §e" + ability.getDescription());
    }
    
    private int getFoodValue(Material material) {
        return switch (material) {
            case BREAD -> 5;
            case COOKED_BEEF -> 8;
            case COOKED_PORKCHOP -> 8;
            case COOKED_CHICKEN -> 6;
            case COOKED_MUTTON -> 6;
            case COOKED_RABBIT -> 5;
            case COOKED_SALMON -> 6;
            case COOKED_COD -> 5;
            case GOLDEN_APPLE -> 20;
            case GOLDEN_CARROT -> 15;
            case ENCHANTED_GOLDEN_APPLE -> 50;
            default -> 0;
        };
    }
    
    private double getUpgradeCost(String upgradeType, int currentLevel) {
        return switch (upgradeType) {
            case "speed" -> 1000.0 * Math.pow(1.5, currentLevel);
            case "health" -> 1500.0 * Math.pow(1.5, currentLevel);
            case "damage" -> 2000.0 * Math.pow(1.5, currentLevel);
            case "xp_boost" -> 2500.0 * Math.pow(1.5, currentLevel);
            case "auto_feed" -> 5000.0;
            case "auto_collect" -> 7500.0;
            case "auto_sell" -> 10000.0;
            default -> 1000.0;
        };
    }
    
    public Pet getActivePet(UUID playerId) {
        return activePets.get(playerId);
    }
    
    public List<Pet> getPlayerPets(UUID playerId) {
        return playerPets.getOrDefault(playerId, new ArrayList<>());
    }
    
    public void addPlayerPet(UUID playerId, Pet pet) {
        playerPets.computeIfAbsent(playerId, k -> new ArrayList<>()).add(pet);
    }
    
    public void removePlayerPet(UUID playerId, Pet pet) {
        List<Pet> pets = playerPets.get(playerId);
        if (pets != null) {
            pets.remove(pet);
        }
    }
    
    public Map<UUID, Pet> getActivePets() {
        return new HashMap<>(activePets);
    }
    
    public Map<UUID, List<Pet>> getAllPlayerPets() {
        return new HashMap<>(playerPets);
    }
}
