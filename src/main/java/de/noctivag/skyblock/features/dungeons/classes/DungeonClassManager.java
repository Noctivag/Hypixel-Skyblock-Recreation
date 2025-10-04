package de.noctivag.skyblock.features.dungeons.classes;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages dungeon classes and their abilities
 */
public class DungeonClassManager implements Service {
    
    private final Map<DungeonClassType, DungeonClass> classes = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonClassType> playerClasses = new ConcurrentHashMap<>();
    private final Map<UUID, ClassProgress> playerProgress = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public DungeonClassManager() {
        initializeClasses();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all dungeon classes
            for (DungeonClassType classType : DungeonClassType.values()) {
                classes.put(classType, new DungeonClass(classType));
            }
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save player progress
            savePlayerProgress();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public String getName() {
        return "DungeonClassManager";
    }
    
    /**
     * Assign a class to a player
     */
    public CompletableFuture<Boolean> assignClass(UUID playerId, DungeonClassType classType) {
        return CompletableFuture.supplyAsync(() -> {
            // Check if player has unlocked this class
            if (!hasClassUnlocked(playerId, classType)) {
                return false;
            }
            
            playerClasses.put(playerId, classType);
            return true;
        });
    }
    
    /**
     * Get player's current class
     */
    public DungeonClassType getPlayerClass(UUID playerId) {
        return playerClasses.get(playerId);
    }
    
    /**
     * Get class information
     */
    public DungeonClass getClass(DungeonClassType classType) {
        return classes.get(classType);
    }
    
    /**
     * Unlock a class for a player
     */
    public void unlockClass(UUID playerId, DungeonClassType classType) {
        ClassProgress progress = playerProgress.computeIfAbsent(playerId, k -> new ClassProgress());
        progress.unlockClass(classType);
    }
    
    /**
     * Check if player has unlocked a class
     */
    public boolean hasClassUnlocked(UUID playerId, DungeonClassType classType) {
        if (classType == DungeonClassType.BERSERKER) return true; // Default class
        return playerProgress.getOrDefault(playerId, new ClassProgress()).hasClassUnlocked(classType);
    }
    
    /**
     * Get player's class progress
     */
    public ClassProgress getPlayerProgress(UUID playerId) {
        return playerProgress.computeIfAbsent(playerId, k -> new ClassProgress());
    }
    
    /**
     * Use class ability
     */
    public CompletableFuture<Boolean> useAbility(UUID playerId, int abilitySlot) {
        return CompletableFuture.supplyAsync(() -> {
            DungeonClassType classType = getPlayerClass(playerId);
            if (classType == null) return false;
            
            DungeonClass dungeonClass = getClass(classType);
            if (dungeonClass == null) return false;
            
            return dungeonClass.useAbility(playerId, abilitySlot);
        });
    }
    
    /**
     * Initialize all dungeon classes
     */
    private void initializeClasses() {
        // Berserker - Melee DPS
        DungeonClass berserker = new DungeonClass(DungeonClassType.BERSERKER)
            .setDescription("Melee damage dealer with high health and defense")
            .setHealthMultiplier(1.2)
            .setDefenseMultiplier(1.3)
            .setStrengthMultiplier(1.4)
            .addAbility(new ClassAbility("Rage", "Increases damage by 50% for 10 seconds", 30))
            .addAbility(new ClassAbility("Berserk", "Increases attack speed by 100% for 8 seconds", 45))
            .addAbility(new ClassAbility("Warcry", "Grants all nearby players +25% damage for 15 seconds", 60));
        
        // Mage - Magic DPS
        DungeonClass mage = new DungeonClass(DungeonClassType.MAGE)
            .setDescription("Magic damage dealer with powerful spells")
            .setIntelligenceMultiplier(2.0)
            .setManaMultiplier(1.5)
            .setCritDamageMultiplier(1.3)
            .addAbility(new ClassAbility("Meteor", "Summons a meteor that deals massive damage", 40))
            .addAbility(new ClassAbility("Teleport", "Teleports to target location", 20))
            .addAbility(new ClassAbility("Mana Shield", "Absorbs damage with mana for 10 seconds", 50));
        
        // Archer - Ranged DPS
        DungeonClass archer = new DungeonClass(DungeonClassType.ARCHER)
            .setDescription("Ranged damage dealer with high critical hit chance")
            .setCritChanceMultiplier(1.5)
            .setCritDamageMultiplier(1.6)
            .setSpeedMultiplier(1.2)
            .addAbility(new ClassAbility("Arrow Storm", "Fires multiple arrows in all directions", 35))
            .addAbility(new ClassAbility("Eagle Eye", "Increases critical hit chance by 100% for 15 seconds", 40))
            .addAbility(new ClassAbility("Explosive Shot", "Next arrow explodes on impact", 25));
        
        // Tank - Defense and Support
        DungeonClass tank = new DungeonClass(DungeonClassType.TANK)
            .setDescription("Defensive class that protects allies")
            .setHealthMultiplier(1.8)
            .setDefenseMultiplier(2.0)
            .setKnockbackResistance(0.8)
            .addAbility(new ClassAbility("Shield Wall", "Reduces damage taken by 75% for 8 seconds", 45))
            .addAbility(new ClassAbility("Taunt", "Forces all enemies to attack you for 10 seconds", 30))
            .addAbility(new ClassAbility("Regenerate", "Heals 50% of max health over 15 seconds", 60));
        
        // Healer - Support and Healing
        DungeonClass healer = new DungeonClass(DungeonClassType.HEALER)
            .setDescription("Support class that heals and buffs allies")
            .setIntelligenceMultiplier(1.3)
            .setManaMultiplier(2.0)
            .setRegenerationMultiplier(1.5)
            .addAbility(new ClassAbility("Heal", "Instantly heals target player for 50% of their max health", 20))
            .addAbility(new ClassAbility("Group Heal", "Heals all nearby players for 25% of their max health", 35))
            .addAbility(new ClassAbility("Blessing", "Grants all nearby players +30% damage for 20 seconds", 50));
        
        classes.put(DungeonClassType.BERSERKER, berserker);
        classes.put(DungeonClassType.MAGE, mage);
        classes.put(DungeonClassType.ARCHER, archer);
        classes.put(DungeonClassType.TANK, tank);
        classes.put(DungeonClassType.HEALER, healer);
    }
    
    private void savePlayerProgress() {
        // TODO: Save player progress to database
    }
}
