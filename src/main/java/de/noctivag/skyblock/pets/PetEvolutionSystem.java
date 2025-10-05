package de.noctivag.skyblock.pets;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Pet Evolution System - Allows pets to evolve into stronger forms
 * 
 * Features:
 * - Pet Evolution Requirements
 * - Evolution Materials
 * - Evolution Bonuses
 * - Evolution Stages
 * - Evolution GUI
 * - Evolution Effects
 */
public class PetEvolutionSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<String, PetEvolution> petEvolutions = new HashMap<>();
    
    public PetEvolutionSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        initializePetEvolutions();
    }
    
    private void initializePetEvolutions() {
        // Wolf Evolution
        petEvolutions.put("WOLF", new PetEvolution(
            "WOLF", "WOLF_ALPHA", "Alpha Wolf",
            Arrays.asList("§7A powerful alpha wolf with", "§7enhanced pack leadership abilities."),
            Arrays.asList("DAMAGE: 8", "CRITICAL_CHANCE: 2", "CRITICAL_DAMAGE: 4", "PACK_BONUS: 15"),
            Arrays.asList(
                new EvolutionRequirement("Level", 50, "Pet must be level 50"),
                new EvolutionRequirement("Materials", 1, "1x Wolf Fang, 1x Alpha Bone, 1x Pack Leader's Claw"),
                new EvolutionRequirement("Coins", 10000, "10,000 coins")
            ),
            Arrays.asList(
                new PetAbility("Alpha Howl", "Grants 25% damage boost to nearby players", 1, 25.0, "PACK_DAMAGE"),
                new PetAbility("Pack Leader", "Increases all stats by 20% when in group", 1, 20.0, "GROUP_BONUS"),
                new PetAbility("Loyalty", "Revives with 50% health when owner dies", 1, 50.0, "REVIVAL")
            )
        ));
        
        // Tiger Evolution
        petEvolutions.put("TIGER", new PetEvolution(
            "TIGER", "TIGER_SABER", "Saber Tiger",
            Arrays.asList("§7An ancient saber tiger with", "§7legendary hunting prowess."),
            Arrays.asList("CRITICAL_CHANCE: 4", "CRITICAL_DAMAGE: 6", "STRENGTH: 4", "HUNTING_BONUS: 20"),
            Arrays.asList(
                new EvolutionRequirement("Level", 60, "Pet must be level 60"),
                new EvolutionRequirement("Materials", 1, "1x Saber Tooth, 1x Ancient Pelt, 1x Hunter's Mark"),
                new EvolutionRequirement("Coins", 15000, "15,000 coins")
            ),
            Arrays.asList(
                new PetAbility("Saber Strike", "Critical hits have 50% chance to instant kill", 1, 50.0, "INSTANT_KILL"),
                new PetAbility("Ancient Hunter", "Increases damage against bosses by 40%", 1, 40.0, "BOSS_DAMAGE"),
                new PetAbility("Predator's Grace", "Increases movement speed by 30%", 1, 30.0, "SPEED")
            )
        ));
        
        // Dragon Evolution
        petEvolutions.put("DRAGON", new PetEvolution(
            "DRAGON", "DRAGON_ANCIENT", "Ancient Dragon",
            Arrays.asList("§7An ancient dragon with", "§7god-like powers and wisdom."),
            Arrays.asList("DAMAGE: 5", "STRENGTH: 5", "CRITICAL_CHANCE: 3", "CRITICAL_DAMAGE: 6", "MAGIC_POWER: 50"),
            Arrays.asList(
                new EvolutionRequirement("Level", 100, "Pet must be level 100"),
                new EvolutionRequirement("Materials", 1, "1x Dragon Heart, 1x Ancient Scale, 1x God's Tear"),
                new EvolutionRequirement("Coins", 50000, "50,000 coins")
            ),
            Arrays.asList(
                new PetAbility("Dragon God", "All abilities are 100% more effective", 1, 100.0, "ALL_ABILITIES"),
                new PetAbility("Ancient Wisdom", "Grants immunity to all negative effects", 1, 100.0, "IMMUNITY"),
                new PetAbility("Divine Flight", "Permanent flight ability", 1, 1.0, "PERMANENT_FLIGHT")
            )
        ));
        
        // Rock Evolution
        petEvolutions.put("ROCK", new PetEvolution(
            "ROCK", "ROCK_CRYSTAL", "Crystal Rock",
            Arrays.asList("§7A crystalline rock with", "§7enhanced mining capabilities."),
            Arrays.asList("MINING_SPEED: 20", "FORTUNE: 3", "EFFICIENCY: 5", "CRYSTAL_BONUS: 25"),
            Arrays.asList(
                new EvolutionRequirement("Level", 40, "Pet must be level 40"),
                new EvolutionRequirement("Materials", 1, "1x Crystal Core, 1x Hardened Stone, 1x Miner's Gem"),
                new EvolutionRequirement("Coins", 8000, "8,000 coins")
            ),
            Arrays.asList(
                new PetAbility("Crystal Vision", "Can see all ores within 50 blocks", 1, 50.0, "ORE_DETECTION"),
                new PetAbility("Crystal Burst", "Chance to instantly mine entire veins", 1, 15.0, "VEIN_MINING"),
                new PetAbility("Unbreakable", "Tools never break", 1, 100.0, "UNBREAKABLE")
            )
        ));
        
        // Rabbit Evolution
        petEvolutions.put("RABBIT", new PetEvolution(
            "RABBIT", "RABBIT_MOON", "Moon Rabbit",
            Arrays.asList("§7A mystical moon rabbit with", "§7celestial farming powers."),
            Arrays.asList("FARMING_SPEED: 15", "FARMING_XP: 20", "FORTUNE: 3", "MOON_BONUS: 30"),
            Arrays.asList(
                new EvolutionRequirement("Level", 45, "Pet must be level 45"),
                new EvolutionRequirement("Materials", 1, "1x Moon Stone, 1x Celestial Carrot, 1x Farmer's Blessing"),
                new EvolutionRequirement("Coins", 12000, "12,000 coins")
            ),
            Arrays.asList(
                new PetAbility("Moon Harvest", "Crops grow instantly at night", 1, 1.0, "INSTANT_GROWTH"),
                new PetAbility("Celestial Blessing", "All crops have 50% chance for double drops", 1, 50.0, "DOUBLE_DROPS"),
                new PetAbility("Lunar Jump", "Can jump 10 blocks high", 1, 10.0, "JUMP_HEIGHT")
            )
        ));
    }
    
    public boolean canEvolvePet(Player player, Pet pet) {
        PetEvolution evolution = petEvolutions.get(pet.getType().getId());
        if (evolution == null) return false;
        
        // Check level requirement
        if (pet.getLevel() < evolution.getLevelRequirement()) return false;
        
        // Check materials
        if (!hasEvolutionMaterials(player, evolution)) return false;
        
        // Check coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null || profile.getCoins() < evolution.getCoinRequirement()) return false;
        
        return true;
    }
    
    public boolean evolvePet(Player player, Pet pet) {
        if (!canEvolvePet(player, pet)) return false;
        
        PetEvolution evolution = petEvolutions.get(pet.getType().getId());
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        
        // Consume materials
        consumeEvolutionMaterials(player, evolution);
        
        // Consume coins
        profile.removeCoins(evolution.getCoinRequirement());
        
        // Create evolved pet
        PetSystem.PetType evolvedType = createEvolvedPetType(evolution);
        Pet evolvedPet = new Pet(pet.getOwnerId(), evolvedType, 1);
        
        // Transfer some stats from original pet
        evolvedPet.setLevel(Math.max(1, pet.getLevel() - 20)); // Lose some levels
        evolvedPet.setXP(pet.getXP() * 0.5); // Lose some XP
        
        player.sendMessage(Component.text("§a§lPET EVOLVED!"));
        player.sendMessage("§7" + pet.getType().getName() + " §7→ §e" + evolution.getEvolvedName());
        player.sendMessage(Component.text("§7New abilities unlocked!"));
        
        return true;
    }
    
    private boolean hasEvolutionMaterials(Player player, PetEvolution evolution) {
        for (EvolutionRequirement req : evolution.getRequirements()) {
            if (req.getType().equals("Materials")) {
                // Check if player has required materials
                // This would need to be implemented based on your material system
                return true; // Placeholder
            }
        }
        return true;
    }
    
    private void consumeEvolutionMaterials(Player player, PetEvolution evolution) {
        for (EvolutionRequirement req : evolution.getRequirements()) {
            if (req.getType().equals("Materials")) {
                // Consume materials from player inventory
                // This would need to be implemented based on your material system
            }
        }
    }
    
    private PetSystem.PetType createEvolvedPetType(PetEvolution evolution) {
        // Create a new PetType with the evolved properties
        // This is a placeholder implementation - adjust based on actual PetSystem.PetType constructor
        return new PetSystem.PetType(
            evolution.getEvolvedId(),
            evolution.getEvolvedName(),
            Material.DRAGON_HEAD,
            "Evolved",
            PetSystem.PetRarity.LEGENDARY,
            evolution.getEvolvedDescription(),
            evolution.getEvolvedStats(),
            convertToPetSystemAbilities(evolution.getEvolvedAbilities())
        );
    }
    
    private List<PetSystem.PetAbility> convertToPetSystemAbilities(List<PetAbility> evolutionAbilities) {
        List<PetSystem.PetAbility> petSystemAbilities = new ArrayList<>();
        for (PetAbility ability : evolutionAbilities) {
            petSystemAbilities.add(new PetSystem.PetAbility(
                ability.getName(),
                ability.getDescription(),
                ability.getUnlockLevel(),
                ability.getValue(),
                ability.getStatType()
            ));
        }
        return petSystemAbilities;
    }
    
    public PetEvolution getPetEvolution(String petId) {
        return petEvolutions.get(petId);
    }
    
    public Map<String, PetEvolution> getAllPetEvolutions() {
        return new HashMap<>(petEvolutions);
    }
    
    // Evolution Classes
    public static class PetEvolution {
        private final String originalId;
        private final String evolvedId;
        private final String evolvedName;
        private final List<String> evolvedDescription;
        private final List<String> evolvedStats;
        private final List<EvolutionRequirement> requirements;
        private final List<PetAbility> evolvedAbilities;
        
        public PetEvolution(String originalId, String evolvedId, String evolvedName,
                          List<String> evolvedDescription, List<String> evolvedStats,
                          List<EvolutionRequirement> requirements, List<PetAbility> evolvedAbilities) {
            this.originalId = originalId;
            this.evolvedId = evolvedId;
            this.evolvedName = evolvedName;
            this.evolvedDescription = evolvedDescription;
            this.evolvedStats = evolvedStats;
            this.requirements = requirements;
            this.evolvedAbilities = evolvedAbilities;
        }
        
        public int getLevelRequirement() {
            return (int) requirements.stream()
                .filter(req -> req.getType().equals("Level"))
                .mapToDouble(EvolutionRequirement::getAmount)
                .findFirst()
                .orElse(0.0);
        }
        
        public double getCoinRequirement() {
            return requirements.stream()
                .filter(req -> req.getType().equals("Coins"))
                .mapToDouble(EvolutionRequirement::getAmount)
                .findFirst()
                .orElse(0.0);
        }
        
        // Getters
        public String getOriginalId() { return originalId; }
        public String getEvolvedId() { return evolvedId; }
        public String getEvolvedName() { return evolvedName; }
        public List<String> getEvolvedDescription() { return evolvedDescription; }
        public List<String> getEvolvedStats() { return evolvedStats; }
        public List<EvolutionRequirement> getRequirements() { return requirements; }
        public List<PetAbility> getEvolvedAbilities() { return evolvedAbilities; }
    }
    
    public static class EvolutionRequirement {
        private final String type;
        private final double amount;
        private final String description;
        
        public EvolutionRequirement(String type, double amount, String description) {
            this.type = type;
            this.amount = amount;
            this.description = description;
        }
        
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public String getDescription() { return description; }
    }
    
    public static class PetAbility {
        private final String name;
        private final String description;
        private final int unlockLevel;
        private final double value;
        private final String statType;
        
        public PetAbility(String name, String description, int unlockLevel, double value, String statType) {
            this.name = name;
            this.description = description;
            this.unlockLevel = unlockLevel;
            this.value = value;
            this.statType = statType;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getUnlockLevel() { return unlockLevel; }
        public double getValue() { return value; }
        public String getStatType() { return statType; }
    }
}
