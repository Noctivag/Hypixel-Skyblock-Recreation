package de.noctivag.skyblock.skyblock.pets;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pets System für Hypixel SkyBlock
 */
public class PetsSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, Pet> activePets = new ConcurrentHashMap<>();
    private final Map<UUID, List<Pet>> playerPets = new ConcurrentHashMap<>();
    
    // Pet Definition
    public static class Pet {
        private final String id;
        private final String name;
        private final PetType type;
        private final PetRarity rarity;
        private final int level;
        private final int xp;
        private final Map<String, Double> stats;
        private final List<PetAbility> abilities;
        private final boolean isActive;
        
        public Pet(String id, String name, PetType type, PetRarity rarity, int level, int xp, 
                  Map<String, Double> stats, List<PetAbility> abilities, boolean isActive) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rarity = rarity;
            this.level = level;
            this.xp = xp;
            this.stats = stats;
            this.abilities = abilities;
            this.isActive = isActive;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public PetType getType() { return type; }
        public PetRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public int getXp() { return xp; }
        public Map<String, Double> getStats() { return stats; }
        public List<PetAbility> getAbilities() { return abilities; }
        public boolean isActive() { return isActive; }
        
        public int getXpForNextLevel() {
            return getXpRequiredForLevel(level + 1) - xp;
        }
        
        public int getXpRequiredForLevel(int targetLevel) {
            if (targetLevel <= 1) return 0;
            return (int) (100 * Math.pow(1.5, targetLevel - 1));
        }
    }
    
    // Pet Types
    public enum PetType {
        COMBAT("Combat", "⚔", "Kampf-Pets erhöhen Kampf-Statistiken"),
        MINING("Mining", "⛏", "Mining-Pets erhöhen Mining-Statistiken"),
        FORAGING("Foraging", "🌲", "Foraging-Pets erhöhen Foraging-Statistiken"),
        FARMING("Farming", "🌾", "Farming-Pets erhöhen Farming-Statistiken"),
        FISHING("Fishing", "🎣", "Fishing-Pets erhöhen Fishing-Statistiken"),
        ENCHANTING("Enchanting", "✨", "Enchanting-Pets erhöhen Enchanting-Statistiken"),
        ALCHEMY("Alchemy", "🧪", "Alchemy-Pets erhöhen Alchemy-Statistiken"),
        TAMING("Taming", "🐾", "Taming-Pets erhöhen Taming-Statistiken");
        
        private final String name;
        private final String icon;
        private final String description;
        
        PetType(String name, String icon, String description) {
            this.name = name;
            this.icon = icon;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getDescription() { return description; }
    }
    
    // Pet Rarity
    public enum PetRarity {
        COMMON("§fGewöhnlich", 0),
        UNCOMMON("§aUngewöhnlich", 1),
        RARE("§9Selten", 2),
        EPIC("§5Episch", 3),
        LEGENDARY("§6Legendär", 4),
        MYTHIC("§dMythisch", 5);
        
        private final String displayName;
        private final int level;
        
        PetRarity(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }
        
        public String getDisplayName() { return displayName; }
        public int getLevel() { return level; }
    }
    
    // Pet Ability
    public static class PetAbility {
        private final String name;
        private final String description;
        private final String effect;
        private final int requiredLevel;
        private final boolean isActive;
        
        public PetAbility(String name, String description, String effect, int requiredLevel, boolean isActive) {
            this.name = name;
            this.description = description;
            this.effect = effect;
            this.requiredLevel = requiredLevel;
            this.isActive = isActive;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getEffect() { return effect; }
        public int getRequiredLevel() { return requiredLevel; }
        public boolean isActive() { return isActive; }
    }
    
    // Pet Registry
    private final Map<String, Pet> petRegistry = new HashMap<>();
    
    public PetsSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        SkyblockPlugin.getServer().getPluginManager().registerEvents(this, SkyblockPlugin);
        initializePets();
    }
    
    /**
     * Initialisiert alle Pets
     */
    private void initializePets() {
        // Combat Pets
        registerPet(new Pet("zombie_pet", "Zombie Pet", PetType.COMBAT, PetRarity.COMMON, 1, 0,
            Map.of("damage", 0.1, "health", 0.05, "defense", 0.05),
            Arrays.asList(
                new PetAbility("Undead", "Erhöht Schaden gegen Zombies", "+10% Schaden gegen Zombies", 1, true),
                new PetAbility("Regeneration", "Regeneriert Gesundheit", "+1 HP pro Sekunde", 10, false)
            ), false));
        
        registerPet(new Pet("skeleton_pet", "Skeleton Pet", PetType.COMBAT, PetRarity.UNCOMMON, 1, 0,
            Map.of("damage", 0.15, "crit_chance", 0.05, "crit_damage", 0.1),
            Arrays.asList(
                new PetAbility("Bone Arrows", "Pfeile verursachen mehr Schaden", "+20% Pfeilschaden", 1, true),
                new PetAbility("Skeleton Army", "Beschwört Skelette", "Beschwört 2 Skelette", 15, false)
            ), false));
        
        registerPet(new Pet("spider_pet", "Spider Pet", PetType.COMBAT, PetRarity.RARE, 1, 0,
            Map.of("damage", 0.2, "speed", 0.1, "jump_boost", 0.05),
            Arrays.asList(
                new PetAbility("Web Shot", "Schießt Spinnennetze", "Verlangsamt Feinde", 1, true),
                new PetAbility("Spider Climb", "Kann an Wänden klettern", "Klettergeschwindigkeit +50%", 12, false)
            ), false));
        
        registerPet(new Pet("enderman_pet", "Enderman Pet", PetType.COMBAT, PetRarity.EPIC, 1, 0,
            Map.of("damage", 0.25, "teleport", 0.1, "void_damage", 0.15),
            Arrays.asList(
                new PetAbility("Teleport", "Teleportiert bei niedriger Gesundheit", "Teleportiert bei <20% HP", 1, true),
                new PetAbility("Void Walker", "Immun gegen Void-Schaden", "Kein Void-Schaden", 20, false)
            ), false));
        
        registerPet(new Pet("dragon_pet", "Dragon Pet", PetType.COMBAT, PetRarity.LEGENDARY, 1, 0,
            Map.of("damage", 0.3, "health", 0.2, "fire_resistance", 1.0),
            Arrays.asList(
                new PetAbility("Dragon Breath", "Feueratem-Attacke", "Verursacht Brandschaden", 1, true),
                new PetAbility("Flight", "Ermöglicht Flug", "Flugfähigkeit", 25, false)
            ), false));
        
        // Mining Pets
        registerPet(new Pet("rock_pet", "Rock Pet", PetType.MINING, PetRarity.COMMON, 1, 0,
            Map.of("mining_speed", 0.1, "mining_fortune", 0.05, "defense", 0.1),
            Arrays.asList(
                new PetAbility("Stone Skin", "Erhöht Verteidigung", "+10% Verteidigung", 1, true),
                new PetAbility("Mining Boost", "Erhöht Mining-Geschwindigkeit", "+20% Mining-Geschwindigkeit", 8, false)
            ), false));
        
        registerPet(new Pet("silverfish_pet", "Silverfish Pet", PetType.MINING, PetRarity.UNCOMMON, 1, 0,
            Map.of("mining_speed", 0.15, "mining_fortune", 0.1, "exp_boost", 0.05),
            Arrays.asList(
                new PetAbility("Tunnel Vision", "Bricht Blöcke schneller", "+15% Mining-Geschwindigkeit", 1, true),
                new PetAbility("Ore Sense", "Findet Erze", "Erze leuchten", 10, false)
            ), false));
        
        registerPet(new Pet("golem_pet", "Golem Pet", PetType.MINING, PetRarity.RARE, 1, 0,
            Map.of("mining_speed", 0.2, "mining_fortune", 0.15, "health", 0.1),
            Arrays.asList(
                new PetAbility("Iron Fist", "Bricht Blöcke sofort", "Sofortiger Blockabbau", 1, true),
                new PetAbility("Mining Master", "Erhöht alle Mining-Statistiken", "+25% Mining-Boni", 15, false)
            ), false));
        
        // Foraging Pets
        registerPet(new Pet("oak_pet", "Oak Pet", PetType.FORAGING, PetRarity.COMMON, 1, 0,
            Map.of("foraging_speed", 0.1, "foraging_fortune", 0.05, "health", 0.05),
            Arrays.asList(
                new PetAbility("Tree Hugger", "Erhöht Foraging-Geschwindigkeit", "+10% Foraging-Geschwindigkeit", 1, true),
                new PetAbility("Nature's Gift", "Erhöht Foraging-Fortune", "+15% Foraging-Fortune", 8, false)
            ), false));
        
        registerPet(new Pet("jungle_pet", "Jungle Pet", PetType.FORAGING, PetRarity.UNCOMMON, 1, 0,
            Map.of("foraging_speed", 0.15, "foraging_fortune", 0.1, "speed", 0.05),
            Arrays.asList(
                new PetAbility("Jungle Speed", "Erhöht Bewegungsgeschwindigkeit", "+10% Geschwindigkeit", 1, true),
                new PetAbility("Vine Swing", "Schwingt an Lianen", "Lianen-Schwingen", 12, false)
            ), false));
        
        // Farming Pets
        registerPet(new Pet("cow_pet", "Cow Pet", PetType.FARMING, PetRarity.COMMON, 1, 0,
            Map.of("farming_speed", 0.1, "farming_fortune", 0.05, "health", 0.05),
            Arrays.asList(
                new PetAbility("Milk Production", "Erhöht Farming-Geschwindigkeit", "+10% Farming-Geschwindigkeit", 1, true),
                new PetAbility("Fertilizer", "Erhöht Farming-Fortune", "+15% Farming-Fortune", 8, false)
            ), false));
        
        registerPet(new Pet("chicken_pet", "Chicken Pet", PetType.FARMING, PetRarity.UNCOMMON, 1, 0,
            Map.of("farming_speed", 0.15, "farming_fortune", 0.1, "exp_boost", 0.05),
            Arrays.asList(
                new PetAbility("Egg Production", "Legt Eier", "Legt alle 30 Sekunden ein Ei", 1, true),
                new PetAbility("Flying", "Kann fliegen", "Flugfähigkeit", 15, false)
            ), false));
        
        // Fishing Pets
        registerPet(new Pet("squid_pet", "Squid Pet", PetType.FISHING, PetRarity.COMMON, 1, 0,
            Map.of("fishing_speed", 0.1, "fishing_fortune", 0.05, "water_breathing", 1.0),
            Arrays.asList(
                new PetAbility("Water Breathing", "Atmet unter Wasser", "Unterwasser-Atmung", 1, true),
                new PetAbility("Ink Cloud", "Erstellt Tintenwolke", "Verwirrt Feinde", 10, false)
            ), false));
        
        registerPet(new Pet("dolphin_pet", "Dolphin Pet", PetType.FISHING, PetRarity.RARE, 1, 0,
            Map.of("fishing_speed", 0.2, "fishing_fortune", 0.15, "swim_speed", 0.2),
            Arrays.asList(
                new PetAbility("Dolphin's Grace", "Erhöht Schwimmgeschwindigkeit", "+20% Schwimmgeschwindigkeit", 1, true),
                new PetAbility("Treasure Hunter", "Findet Schätze", "Schätze leuchten", 15, false)
            ), false));
    }
    
    /**
     * Registriert ein Pet
     */
    private void registerPet(Pet pet) {
        petRegistry.put(pet.getId(), pet);
    }
    
    /**
     * Gibt ein Pet an einen Spieler
     */
    public boolean givePetToPlayer(Player player, String petId) {
        Pet pet = petRegistry.get(petId);
        if (pet == null) {
            player.sendMessage(Component.text("§cPet nicht gefunden!"));
            return false;
        }
        
        List<Pet> pets = playerPets.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        pets.add(pet);
        
        player.sendMessage(Component.text("§aPet erhalten!"));
        player.sendMessage("§e" + pet.getRarity().getDisplayName() + " " + pet.getName());
        player.sendMessage("§7" + pet.getType().getDescription());
        
        return true;
    }
    
    /**
     * Aktiviert ein Pet für einen Spieler
     */
    public boolean activatePet(Player player, String petId) {
        List<Pet> pets = playerPets.get(player.getUniqueId());
        if (pets == null) {
            player.sendMessage(Component.text("§cDu hast keine Pets!"));
            return false;
        }
        
        Pet pet = pets.stream()
            .filter(p -> p.getId().equals(petId))
            .findFirst()
            .orElse(null);
        
        if (pet == null) {
            player.sendMessage(Component.text("§cPet nicht gefunden!"));
            return false;
        }
        
        // Deaktiviere aktuelles Pet
        if (activePets.containsKey(player.getUniqueId())) {
            Pet currentPet = activePets.get(player.getUniqueId());
            currentPet = new Pet(currentPet.getId(), currentPet.getName(), currentPet.getType(), 
                               currentPet.getRarity(), currentPet.getLevel(), currentPet.getXp(), 
                               currentPet.getStats(), currentPet.getAbilities(), false);
        }
        
        // Aktiviere neues Pet
        Pet activePet = new Pet(pet.getId(), pet.getName(), pet.getType(), pet.getRarity(), 
                              pet.getLevel(), pet.getXp(), pet.getStats(), pet.getAbilities(), true);
        activePets.put(player.getUniqueId(), activePet);
        
        player.sendMessage(Component.text("§aPet aktiviert!"));
        player.sendMessage("§e" + activePet.getRarity().getDisplayName() + " " + activePet.getName());
        player.sendMessage("§7Level " + activePet.getLevel());
        
        return true;
    }
    
    /**
     * Fügt XP zu einem Pet hinzu
     */
    public void addPetXP(UUID playerId, PetType petType, int xp) {
        Pet activePet = activePets.get(playerId);
        if (activePet != null && activePet.getType() == petType) {
            // Hier würde die XP-Logik implementiert werden
            // und das Pet-Level erhöht werden
        }
    }
    
    /**
     * Event-Handler für Entity-Death (für Pet XP)
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            UUID playerId = player.getUniqueId();
            
            Pet activePet = activePets.get(playerId);
            if (activePet != null && activePet.getType() == PetType.COMBAT) {
                addPetXP(playerId, PetType.COMBAT, 10);
            }
        }
    }
    
    /**
     * Gibt alle Pets eines Spielers zurück
     */
    public List<Pet> getPlayerPets(UUID playerId) {
        return new ArrayList<>(playerPets.getOrDefault(playerId, new ArrayList<>()));
    }
    
    /**
     * Gibt das aktive Pet eines Spielers zurück
     */
    public Pet getActivePet(UUID playerId) {
        return activePets.get(playerId);
    }
    
    /**
     * Gibt alle registrierten Pets zurück
     */
    public Map<String, Pet> getAllPets() {
        return new HashMap<>(petRegistry);
    }
    
    /**
     * Gibt Pets eines bestimmten Typs zurück
     */
    public List<Pet> getPetsByType(PetType type) {
        return petRegistry.values().stream()
            .filter(pet -> pet.getType() == type)
            .toList();
    }
    
    /**
     * Gibt Pets einer bestimmten Rarity zurück
     */
    public List<Pet> getPetsByRarity(PetRarity rarity) {
        return petRegistry.values().stream()
            .filter(pet -> pet.getRarity() == rarity)
            .toList();
    }
}
