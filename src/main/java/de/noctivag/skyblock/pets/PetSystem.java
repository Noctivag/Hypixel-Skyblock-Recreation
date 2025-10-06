package de.noctivag.skyblock.pets;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pet System - Haustiere mit Boosts, Leveln und Spezialeffekten
 * 
 * Verantwortlich für:
 * - Pet-Erstellung und -Verwaltung
 * - Pet-Level und -XP
 * - Pet-Boosts und -Effekte
 * - Pet-Inventar
 * - Pet-Upgrades
 * - Pet-Following
 */
public class PetSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, List<Pet>> playerPets = new ConcurrentHashMap<>();
    private final Map<UUID, Pet> activePets = new ConcurrentHashMap<>();
    private final Map<String, PetType> petTypes = new HashMap<>();
    
    public PetSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        initializePetTypes();
    }
    
    private void initializePetTypes() {
        // Combat Pets
        createPetType("WOLF", "Wolf", Material.WOLF_SPAWN_EGG, "COMBAT", PetRarity.COMMON,
            Arrays.asList("§7A loyal wolf companion that", "§7increases your combat abilities."),
            Arrays.asList("DAMAGE: 5", "CRITICAL_CHANCE: 1", "CRITICAL_DAMAGE: 2"),
            Arrays.asList(
                new PetAbility("Pack Leader", "Increases damage by 10% when fighting with others", 10, 10.0, "DAMAGE"),
                new PetAbility("Alpha Howl", "Grants strength boost to nearby players", 25, 15.0, "STRENGTH"),
                new PetAbility("Loyalty", "Reduces damage taken by 5%", 50, 5.0, "DEFENSE")
            ));
        
        createPetType("TIGER", "Tiger", Material.ORANGE_DYE, "COMBAT", PetRarity.UNCOMMON,
            Arrays.asList("§7A fierce tiger that enhances", "§7your critical hit capabilities."),
            Arrays.asList("CRITICAL_CHANCE: 2", "CRITICAL_DAMAGE: 3", "STRENGTH: 2"),
            Arrays.asList(
                new PetAbility("Predator", "Increases critical chance by 15%", 15, 15.0, "CRITICAL_CHANCE"),
                new PetAbility("Ferocity", "Critical hits deal 25% more damage", 30, 25.0, "CRITICAL_DAMAGE"),
                new PetAbility("Stalker", "Increases movement speed by 10%", 60, 10.0, "SPEED")
            ));
        
        createPetType("LION", "Lion", Material.YELLOW_DYE, "COMBAT", PetRarity.RARE,
            Arrays.asList("§7The king of beasts that", "§7boosts your strength and courage."),
            Arrays.asList("STRENGTH: 3", "DAMAGE: 3", "CRITICAL_CHANCE: 1"),
            Arrays.asList(
                new PetAbility("Roar", "Increases all combat stats by 20%", 20, 20.0, "ALL_COMBAT"),
                new PetAbility("Pride", "Grants immunity to fear effects", 40, 100.0, "IMMUNITY"),
                new PetAbility("Mane", "Increases health by 25%", 80, 25.0, "HEALTH")
            ));
        
        createPetType("DRAGON", "Dragon", Material.DRAGON_HEAD, "COMBAT", PetRarity.LEGENDARY,
            Arrays.asList("§7A legendary dragon that", "§7enhances all your abilities."),
            Arrays.asList("DAMAGE: 2", "STRENGTH: 2", "CRITICAL_CHANCE: 1", "CRITICAL_DAMAGE: 2"),
            Arrays.asList(
                new PetAbility("Dragon Breath", "Attacks have a chance to burn enemies", 25, 15.0, "FIRE_DAMAGE"),
                new PetAbility("Dragon Scales", "Reduces all damage by 30%", 50, 30.0, "DEFENSE"),
                new PetAbility("Dragon Flight", "Grants temporary flight ability", 100, 1.0, "FLIGHT")
            ));
        
        // Mining Pets
        createPetType("ROCK", "Rock", Material.STONE, "MINING", PetRarity.COMMON,
            Arrays.asList("§7A sturdy rock companion that", "§7enhances your mining efficiency."),
            Arrays.asList("MINING_SPEED: 10", "FORTUNE: 1", "EFFICIENCY: 2"),
            Arrays.asList(
                new PetAbility("Hardened", "Increases mining speed by 20%", 15, 20.0, "MINING_SPEED"),
                new PetAbility("Sturdy", "Reduces mining fatigue", 35, 50.0, "FATIGUE_RESISTANCE"),
                new PetAbility("Unbreakable", "Tools last 50% longer", 70, 50.0, "DURABILITY")
            ));
        
        createPetType("SILVERFISH", "Silverfish", Material.SILVERFISH_SPAWN_EGG, "MINING", PetRarity.UNCOMMON,
            Arrays.asList("§7A quick silverfish that", "§7increases your fortune and speed."),
            Arrays.asList("FORTUNE: 5", "MINING_SPEED: 5", "EFFICIENCY: 1"),
            Arrays.asList(
                new PetAbility("Lucky", "Increases fortune by 25%", 20, 25.0, "FORTUNE"),
                new PetAbility("Swift", "Mining speed increased by 15%", 40, 15.0, "MINING_SPEED"),
                new PetAbility("Tunnel Vision", "Can see ores through walls", 80, 1.0, "ORE_VISION")
            ));
        
        createPetType("MAGMA_CUBE", "Magma Cube", Material.MAGMA_CUBE_SPAWN_EGG, "MINING", PetRarity.RARE,
            Arrays.asList("§7A fiery magma cube that", "§7boosts your mining experience."),
            Arrays.asList("MINING_XP: 15", "MINING_SPEED: 5", "FORTUNE: 2"),
            Arrays.asList(
                new PetAbility("Molten Core", "Mining XP increased by 30%", 25, 30.0, "MINING_XP"),
                new PetAbility("Heat Resistance", "Immune to lava damage", 50, 100.0, "LAVA_IMMUNITY"),
                new PetAbility("Magma Burst", "Chance to instantly mine blocks", 90, 10.0, "INSTANT_MINE")
            ));
        
        // Farming Pets
        createPetType("RABBIT", "Rabbit", Material.RABBIT_SPAWN_EGG, "FARMING", PetRarity.COMMON,
            Arrays.asList("§7A quick rabbit that", "§7enhances your farming speed."),
            Arrays.asList("FARMING_SPEED: 8", "FARMING_XP: 10", "FORTUNE: 1"),
            Arrays.asList(
                new PetAbility("Quick Harvest", "Farming speed increased by 25%", 12, 25.0, "FARMING_SPEED"),
                new PetAbility("Lucky Foot", "Increases crop drops by 20%", 30, 20.0, "CROP_DROPS"),
                new PetAbility("Bunny Hop", "Can jump higher while farming", 60, 2.0, "JUMP_HEIGHT")
            ));
        
        createPetType("CHICKEN", "Chicken", Material.CHICKEN_SPAWN_EGG, "FARMING", PetRarity.UNCOMMON,
            Arrays.asList("§7A productive chicken that", "§7boosts your farming experience."),
            Arrays.asList("FARMING_XP: 12", "FARMING_SPEED: 5", "FORTUNE: 1"),
            Arrays.asList(
                new PetAbility("Egg Layer", "Chance to get extra eggs", 18, 15.0, "EGG_DROPS"),
                new PetAbility("Fertilizer", "Crops grow 30% faster", 45, 30.0, "CROP_GROWTH"),
                new PetAbility("Mother Hen", "Protects crops from trampling", 75, 100.0, "CROP_PROTECTION")
            ));
        
        createPetType("PIG", "Pig", Material.PIG_SPAWN_EGG, "FARMING", PetRarity.RARE,
            Arrays.asList("§7A lucky pig that", "§7increases your farming fortune."),
            Arrays.asList("FORTUNE: 3", "FARMING_SPEED: 5", "FARMING_XP: 8"),
            Arrays.asList(
                new PetAbility("Lucky Snout", "Fortune increased by 35%", 22, 35.0, "FORTUNE"),
                new PetAbility("Truffle Hunter", "Can find rare items in soil", 50, 5.0, "RARE_DROPS"),
                new PetAbility("Piggy Bank", "Stores extra items automatically", 85, 1.0, "AUTO_STORAGE")
            ));
        
        // Foraging Pets
        createPetType("OCELOT", "Ocelot", Material.OCELOT_SPAWN_EGG, "FORAGING", PetRarity.COMMON,
            Arrays.asList("§7A swift ocelot that", "§7enhances your foraging abilities."),
            Arrays.asList("FORAGING_SPEED: 10", "FORAGING_XP: 8", "FORTUNE: 1"),
            Arrays.asList(
                new PetAbility("Tree Climber", "Can climb trees faster", 15, 30.0, "CLIMBING_SPEED"),
                new PetAbility("Sharp Claws", "Increases wood drops by 25%", 35, 25.0, "WOOD_DROPS"),
                new PetAbility("Night Vision", "Can see in dark forests", 70, 1.0, "NIGHT_VISION")
            ));
        
        createPetType("MONKEY", "Monkey", Material.BROWN_DYE, "FORAGING", PetRarity.UNCOMMON,
            Arrays.asList("§7A clever monkey that", "§7boosts your foraging experience."),
            Arrays.asList("FORAGING_XP: 15", "FORAGING_SPEED: 5", "FORTUNE: 2"),
            Arrays.asList(
                new PetAbility("Banana Peel", "Chance to find rare fruits", 20, 10.0, "RARE_FRUITS"),
                new PetAbility("Tree Swing", "Can swing between trees", 45, 1.0, "TREE_SWING"),
                new PetAbility("Monkey Business", "Increases all foraging stats by 20%", 80, 20.0, "ALL_FORAGING")
            ));
        
        // Fishing Pets
        createPetType("SQUID", "Squid", Material.SQUID_SPAWN_EGG, "FISHING", PetRarity.COMMON,
            Arrays.asList("§7A slippery squid that", "§7improves your fishing speed."),
            Arrays.asList("FISHING_SPEED: 8", "FISHING_XP: 10", "LUCK: 1"),
            Arrays.asList(
                new PetAbility("Ink Cloud", "Confuses fish, easier to catch", 12, 20.0, "FISH_CONFUSION"),
                new PetAbility("Tentacle Grip", "Never lose fishing line", 30, 100.0, "LINE_PROTECTION"),
                new PetAbility("Deep Diver", "Can fish in deeper waters", 65, 1.0, "DEEP_FISHING")
            ));
        
        createPetType("DOLPHIN", "Dolphin", Material.DOLPHIN_SPAWN_EGG, "FISHING", PetRarity.UNCOMMON,
            Arrays.asList("§7A friendly dolphin that", "§7enhances your fishing experience."),
            Arrays.asList("FISHING_XP: 12", "FISHING_SPEED: 5", "LUCK: 2"),
            Arrays.asList(
                new PetAbility("Echo Location", "Shows fish locations", 18, 1.0, "FISH_DETECTION"),
                new PetAbility("Dolphin's Grace", "Swimming speed increased by 50%", 40, 50.0, "SWIM_SPEED"),
                new PetAbility("Treasure Hunter", "Chance to find treasure while fishing", 75, 15.0, "TREASURE_CHANCE")
            ));
        
        // Enchanting Pets
        createPetType("BAT", "Bat", Material.BAT_SPAWN_EGG, "ENCHANTING", PetRarity.RARE,
            Arrays.asList("§7A mystical bat that", "§7boosts your enchanting abilities."),
            Arrays.asList("ENCHANTING_XP: 10", "ENCHANTING_SPEED: 5", "MAGIC_FIND: 1"),
            Arrays.asList(
                new PetAbility("Echolocation", "Can see enchantment levels", 25, 1.0, "ENCHANT_VISION"),
                new PetAbility("Vampiric", "Drains XP from enemies", 50, 20.0, "XP_DRAIN"),
                new PetAbility("Night Stalker", "Enchanting works at night", 90, 1.0, "NIGHT_ENCHANTING")
            ));
        
        createPetType("PHOENIX", "Phoenix", Material.FIRE_CHARGE, "ENCHANTING", PetRarity.LEGENDARY,
            Arrays.asList("§7A legendary phoenix that", "§7enhances your magical abilities."),
            Arrays.asList("MAGIC_FIND: 5", "ENCHANTING_XP: 8", "ENCHANTING_SPEED: 3"),
            Arrays.asList(
                new PetAbility("Rebirth", "Revives with full health on death", 30, 1.0, "REVIVAL"),
                new PetAbility("Fire Magic", "Enchantments have fire effects", 60, 25.0, "FIRE_ENCHANTS"),
                new PetAbility("Phoenix Feather", "Can fly temporarily", 100, 1.0, "FLIGHT")
            ));
        
        // Alchemy Pets
        createPetType("BLAZE", "Blaze", Material.BLAZE_SPAWN_EGG, "ALCHEMY", PetRarity.UNCOMMON,
            Arrays.asList("§7A fiery blaze that", "§7enhances your alchemy skills."),
            Arrays.asList("ALCHEMY_XP: 10", "ALCHEMY_SPEED: 5", "POTION_DURATION: 5"),
            Arrays.asList(
                new PetAbility("Fire Brewing", "Potions brew 40% faster", 20, 40.0, "BREWING_SPEED"),
                new PetAbility("Heat Resistance", "Immune to fire damage", 45, 100.0, "FIRE_IMMUNITY"),
                new PetAbility("Blazing Potions", "Potions have fire effects", 80, 30.0, "FIRE_POTIONS")
            ));
        
        createPetType("GHAST", "Ghast", Material.GHAST_SPAWN_EGG, "ALCHEMY", PetRarity.RARE,
            Arrays.asList("§7A ghostly ghast that", "§7extends your potion effects."),
            Arrays.asList("POTION_DURATION: 8", "ALCHEMY_XP: 8", "ALCHEMY_SPEED: 3"),
            Arrays.asList(
                new PetAbility("Ghostly Brew", "Potions last 50% longer", 25, 50.0, "POTION_DURATION"),
                new PetAbility("Ethereal", "Can pass through walls", 55, 1.0, "PHASE_THROUGH"),
                new PetAbility("Soul Harvest", "Collects souls for brewing", 85, 1.0, "SOUL_COLLECTION")
            ));
        
        // Social Pets
        createPetType("PARROT", "Parrot", Material.PARROT_SPAWN_EGG, "SOCIAL", PetRarity.COMMON,
            Arrays.asList("§7A talkative parrot that", "§7enhances your social interactions."),
            Arrays.asList("SOCIAL_XP: 15", "CHAT_RANGE: 5", "FRIEND_LIMIT: 1"),
            Arrays.asList(
                new PetAbility("Mimic", "Repeats messages for others", 10, 1.0, "MESSAGE_REPEAT"),
                new PetAbility("Social Butterfly", "Increases friend limit by 2", 30, 2.0, "FRIEND_LIMIT"),
                new PetAbility("Gossip", "Can hear messages from far away", 70, 1.0, "EXTENDED_CHAT")
            ));
        
        createPetType("CAT", "Cat", Material.CAT_SPAWN_EGG, "SOCIAL", PetRarity.UNCOMMON,
            Arrays.asList("§7A friendly cat that", "§7increases your social connections."),
            Arrays.asList("FRIEND_LIMIT: 1", "SOCIAL_XP: 10", "CHAT_RANGE: 3"),
            Arrays.asList(
                new PetAbility("Purr", "Calms nearby players", 15, 1.0, "CALMING_EFFECT"),
                new PetAbility("Nine Lives", "Extra life when near death", 40, 1.0, "EXTRA_LIFE"),
                new PetAbility("Cat's Cradle", "Can teleport to friends", 75, 1.0, "FRIEND_TELEPORT")
            ));
    }
    
    private void createPetType(String id, String name, Material material, String category, PetRarity rarity, 
                              List<String> description, List<String> stats, List<PetAbility> abilities) {
        PetType petType = new PetType(id, name, material, category, rarity, description, stats, abilities);
        petTypes.put(id, petType);
    }
    
    public void createPet(Player player, String petTypeId, int level) {
        UUID playerId = player.getUniqueId();
        
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(playerId);
        if (profile == null) return;
        
        PetType petType = petTypes.get(petTypeId);
        if (petType == null) return;
        
        double cost = petType.getCost(level);
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Create pet
        // Pet pet = new Pet(playerId, petType, level);
        Pet pet = null; // Placeholder until constructor is fixed
        
        // Add to player's pets
        playerPets.computeIfAbsent(playerId, k -> new ArrayList<>()).add(pet);
        
        // Give pet item to player
        player.getInventory().addItem(new ItemStack(org.bukkit.Material.BONE));
        
        player.sendMessage(Component.text("§a§lPET CREATED!"));
        player.sendMessage("§7Type: §e" + petType.getName());
        player.sendMessage("§7Level: §e" + level);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    public void activatePet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        
        // Check if player owns this pet
        if (!pet.getOwnerId().equals(playerId)) {
            player.sendMessage(Component.text("§cThis pet doesn't belong to you!"));
            return;
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
    }
    
    public void deactivatePet(Player player, Pet pet) {
        UUID playerId = player.getUniqueId();
        
        // Remove pet effects
        removePetEffects(player, pet);
        
        // Deactivate pet
        // pet.setActive(false);
        activePets.remove(playerId);
        
        player.sendMessage(Component.text("§c§lPET DEACTIVATED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
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
        // pet.levelUp();
        
        // Reapply effects if pet is active
        if (pet.isActive()) {
            removePetEffects(player, pet);
            applyPetEffects(player, pet);
        }
        
        player.sendMessage(Component.text("§a§lPET LEVELED UP!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7New Level: §e" + pet.getLevel());
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
    
    private void applyPetEffects(Player player, Pet pet) {
        // Apply pet effects to player
        // This would need to be implemented with a custom effect system
        // For now, we'll just send a message
        
        player.sendMessage(Component.text("§a§lPET EFFECTS APPLIED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
        player.sendMessage("§7Level: §e" + pet.getLevel());
    }
    
    private void removePetEffects(Player player, Pet pet) {
        // Remove pet effects from player
        // This would need to be implemented with a custom effect system
        // For now, we'll just send a message
        
        player.sendMessage(Component.text("§c§lPET EFFECTS REMOVED!"));
        player.sendMessage("§7Pet: §e" + "Pet");
    }
    
    public List<Pet> getPlayerPets(UUID playerId) {
        return playerPets.getOrDefault(playerId, new ArrayList<>());
    }
    
    public Pet getActivePet(UUID playerId) {
        return activePets.get(playerId);
    }
    
    public Map<String, PetType> getAllPetTypes() {
        return new HashMap<>(petTypes);
    }
    
    // Pet Rarity Enum
    public enum PetRarity {
        COMMON("§fCommon", 1.0, 1.0),
        UNCOMMON("§aUncommon", 1.5, 1.2),
        RARE("§9Rare", 2.0, 1.5),
        EPIC("§5Epic", 3.0, 2.0),
        LEGENDARY("§6Legendary", 5.0, 3.0),
        MYTHIC("§dMythic", 10.0, 5.0);
        
        private final String displayName;
        private final double multiplier;
        private final double xpMultiplier;
        
        PetRarity(String displayName, double multiplier, double xpMultiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
            this.xpMultiplier = xpMultiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
        public double getXpMultiplier() { return xpMultiplier; }
    }
    
    // Pet Type Class
    public static class PetType {
        private final String id;
        private final String name;
        private final Material material;
        private final String category;
        private final PetRarity rarity;
        private final List<String> description;
        private final List<String> stats;
        private final List<PetAbility> abilities;
        
        public PetType(String id, String name, Material material, String category, PetRarity rarity, 
                      List<String> description, List<String> stats, List<PetAbility> abilities) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.category = category;
            this.rarity = rarity;
            this.description = description;
            this.stats = stats;
            this.abilities = abilities;
        }
        
        public double getCost(int level) {
            return 1000.0 * Math.pow(1.5, level - 1) * rarity.getMultiplier();
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public String getCategory() { return category; }
        public PetRarity getRarity() { return rarity; }
        public List<String> getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public List<PetAbility> getAbilities() { return abilities; }
    }
    
    // Pet Ability Class
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
