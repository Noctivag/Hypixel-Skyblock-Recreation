package de.noctivag.plugin.features.pets.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All pet types in Hypixel Skyblock
 */
public enum PetType {
    // Combat Pets
    DRAGON("Dragon", "🐉", PetCategory.COMBAT, PetRarity.LEGENDARY),
    WITHER_SKELETON("Wither Skeleton", "💀", PetCategory.COMBAT, PetRarity.LEGENDARY),
    TIGER("Tiger", "🐅", PetCategory.COMBAT, PetRarity.LEGENDARY),
    LION("Lion", "🦁", PetCategory.COMBAT, PetRarity.LEGENDARY),
    WOLF("Wolf", "🐺", PetCategory.COMBAT, PetRarity.LEGENDARY),
    PHOENIX("Phoenix", "🔥", PetCategory.COMBAT, PetRarity.LEGENDARY),
    GRIFFIN("Griffin", "🦅", PetCategory.COMBAT, PetRarity.LEGENDARY),
    YETI("Yeti", "❄️", PetCategory.COMBAT, PetRarity.LEGENDARY),
    
    // Farming Pets
    ELEPHANT("Elephant", "🐘", PetCategory.FARMING, PetRarity.LEGENDARY),
    RABBIT("Rabbit", "🐰", PetCategory.FARMING, PetRarity.RARE),
    CHICKEN("Chicken", "🐔", PetCategory.FARMING, PetRarity.COMMON),
    COW("Cow", "🐄", PetCategory.FARMING, PetRarity.COMMON),
    PIG("Pig", "🐷", PetCategory.FARMING, PetRarity.COMMON),
    SHEEP("Sheep", "🐑", PetCategory.FARMING, PetRarity.COMMON),
    MUSHROOM_COW("Mushroom Cow", "🍄", PetCategory.FARMING, PetRarity.RARE),
    BEE("Bee", "🐝", PetCategory.FARMING, PetRarity.RARE),
    
    // Mining Pets
    MOLE("Mole", "🦫", PetCategory.MINING, PetRarity.EPIC),
    ROCK("Rock", "🪨", PetCategory.MINING, PetRarity.RARE),
    SILVERFISH("Silverfish", "🐛", PetCategory.MINING, PetRarity.COMMON),
    MAGMA_CUBE("Magma Cube", "🌋", PetCategory.MINING, PetRarity.RARE),
    SPIDER("Spider", "🕷️", PetCategory.MINING, PetRarity.COMMON),
    ENDERMITE("Endermite", "🟣", PetCategory.MINING, PetRarity.EPIC),
    
    // Fishing Pets
    SQUID("Squid", "🦑", PetCategory.FISHING, PetRarity.COMMON),
    DOLPHIN("Dolphin", "🐬", PetCategory.FISHING, PetRarity.EPIC),
    BLUE_WHALE("Blue Whale", "🐋", PetCategory.FISHING, PetRarity.LEGENDARY),
    MAGMAFISH("Magmafish", "🔥", PetCategory.FISHING, PetRarity.RARE),
    FLYING_FISH("Flying Fish", "🐠", PetCategory.FISHING, PetRarity.EPIC),
    MONKEY("Monkey", "🐒", PetCategory.FISHING, PetRarity.RARE),
    
    // Foraging Pets
    OCELOT("Ocelot", "🐱", PetCategory.FORAGING, PetRarity.COMMON),
    PARROT("Parrot", "🦜", PetCategory.FORAGING, PetRarity.RARE),
    GIRAFFE("Giraffe", "🦒", PetCategory.FORAGING, PetRarity.EPIC),
    
    // Special Pets
    GHOUL("Ghoul", "👻", PetCategory.SPECIAL, PetRarity.EPIC),
    ENDERMAN("Enderman", "🔮", PetCategory.SPECIAL, PetRarity.LEGENDARY),
    BLAZE("Blaze", "🔥", PetCategory.SPECIAL, PetRarity.LEGENDARY),
    SKELETON_HORSE("Skeleton Horse", "💀", PetCategory.SPECIAL, PetRarity.LEGENDARY),
    ZOMBIE_HORSE("Zombie Horse", "🧟", PetCategory.SPECIAL, PetRarity.LEGENDARY),
    SKELETON("Skeleton", "💀", PetCategory.SPECIAL, PetRarity.RARE),
    ZOMBIE("Zombie", "🧟", PetCategory.SPECIAL, PetRarity.COMMON),
    
    // Event Pets
    REINDEER("Reindeer", "🦌", PetCategory.SOCIAL, PetRarity.LEGENDARY),
    SNOWMAN("Snowman", "⛄", PetCategory.SOCIAL, PetRarity.RARE),
    PENGUIN("Penguin", "🐧", PetCategory.SOCIAL, PetRarity.EPIC),
    TURTLE("Turtle", "🐢", PetCategory.SOCIAL, PetRarity.RARE),
    JELLYFISH("Jellyfish", "🪼", PetCategory.SOCIAL, PetRarity.EPIC);
    
    private final String displayName;
    private final String icon;
    private final PetCategory category;
    private final PetRarity rarity;
    
    PetType(String displayName, String icon, PetCategory category, PetRarity rarity) {
        this.displayName = displayName;
        this.icon = icon;
        this.category = category;
        this.rarity = rarity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public PetCategory getCategory() {
        return category;
    }
    
    public PetRarity getRarity() {
        return rarity;
    }
    
    /**
     * Get pets by category
     */
    public static List<PetType> getByCategory(PetCategory category) {
        return Arrays.stream(values())
            .filter(pet -> pet.getCategory() == category)
            .toList();
    }
    
    /**
     * Get pets by rarity
     */
    public static List<PetType> getByRarity(PetRarity rarity) {
        return Arrays.stream(values())
            .filter(pet -> pet.getRarity() == rarity)
            .toList();
    }
    
    /**
     * Get combat pets
     */
    public static List<PetType> getCombatPets() {
        return getByCategory(PetCategory.COMBAT);
    }
    
    /**
     * Get farming pets
     */
    public static List<PetType> getFarmingPets() {
        return getByCategory(PetCategory.FARMING);
    }
    
    /**
     * Get mining pets
     */
    public static List<PetType> getMiningPets() {
        return getByCategory(PetCategory.MINING);
    }
    
    /**
     * Get fishing pets
     */
    public static List<PetType> getFishingPets() {
        return getByCategory(PetCategory.FISHING);
    }
    
    /**
     * Get foraging pets
     */
    public static List<PetType> getForagingPets() {
        return getByCategory(PetCategory.FORAGING);
    }
    
    /**
     * Get special pets
     */
    public static List<PetType> getSpecialPets() {
        return getByCategory(PetCategory.SPECIAL);
    }
    
    /**
     * Get social pets
     */
    public static List<PetType> getSocialPets() {
        return getByCategory(PetCategory.SOCIAL);
    }
    
    /**
     * Get base stats for this pet
     */
    public PetBaseStats getBaseStats() {
        return switch (this) {
            // Combat Pets
            case DRAGON -> new PetBaseStats(15, 8, 8, 0, 2);
            case WITHER_SKELETON -> new PetBaseStats(12, 6, 10, 0, 1);
            case TIGER -> new PetBaseStats(8, 4, 6, 0, 4);
            case LION -> new PetBaseStats(10, 5, 8, 0, 3);
            case WOLF -> new PetBaseStats(7, 4, 6, 0, 5);
            case PHOENIX -> new PetBaseStats(12, 6, 7, 5, 3);
            case GRIFFIN -> new PetBaseStats(11, 5, 7, 3, 4);
            case YETI -> new PetBaseStats(14, 8, 5, 0, 1);
            
            // Farming Pets
            case ELEPHANT -> new PetBaseStats(18, 10, 0, 0, 0);
            case RABBIT -> new PetBaseStats(5, 3, 0, 0, 7);
            case CHICKEN -> new PetBaseStats(4, 2, 0, 0, 6);
            case COW -> new PetBaseStats(9, 5, 0, 0, 2);
            case PIG -> new PetBaseStats(6, 3, 0, 0, 4);
            case SHEEP -> new PetBaseStats(5, 3, 0, 0, 3);
            case MUSHROOM_COW -> new PetBaseStats(8, 4, 0, 0, 2);
            case BEE -> new PetBaseStats(6, 3, 0, 0, 5);
            
            // Mining Pets
            case MOLE -> new PetBaseStats(7, 4, 0, 0, 4);
            case ROCK -> new PetBaseStats(15, 12, 0, 0, 0);
            case SILVERFISH -> new PetBaseStats(4, 3, 0, 0, 8);
            case MAGMA_CUBE -> new PetBaseStats(9, 5, 0, 0, 2);
            case SPIDER -> new PetBaseStats(5, 3, 0, 0, 6);
            case ENDERMITE -> new PetBaseStats(6, 4, 0, 2, 5);
            
            // Fishing Pets
            case SQUID -> new PetBaseStats(6, 4, 0, 0, 5);
            case DOLPHIN -> new PetBaseStats(8, 5, 0, 0, 6);
            case BLUE_WHALE -> new PetBaseStats(25, 15, 0, 0, 1);
            case MAGMAFISH -> new PetBaseStats(7, 4, 0, 0, 4);
            case FLYING_FISH -> new PetBaseStats(6, 4, 0, 0, 7);
            case MONKEY -> new PetBaseStats(5, 3, 0, 0, 8);
            
            // Foraging Pets
            case OCELOT -> new PetBaseStats(4, 2, 0, 0, 7);
            case PARROT -> new PetBaseStats(5, 3, 0, 0, 6);
            case GIRAFFE -> new PetBaseStats(12, 6, 0, 0, 2);
            
            // Special Pets
            case GHOUL -> new PetBaseStats(9, 6, 4, 0, 3);
            case ENDERMAN -> new PetBaseStats(8, 5, 5, 8, 4);
            case BLAZE -> new PetBaseStats(8, 5, 4, 5, 3);
            case SKELETON_HORSE -> new PetBaseStats(10, 6, 6, 0, 3);
            case ZOMBIE_HORSE -> new PetBaseStats(11, 7, 5, 0, 2);
            case SKELETON -> new PetBaseStats(6, 4, 4, 0, 5);
            case ZOMBIE -> new PetBaseStats(7, 4, 3, 0, 3);
            
            // Event Pets
            case REINDEER -> new PetBaseStats(13, 7, 4, 0, 4);
            case SNOWMAN -> new PetBaseStats(8, 5, 0, 0, 3);
            case PENGUIN -> new PetBaseStats(7, 4, 0, 0, 5);
            case TURTLE -> new PetBaseStats(10, 8, 0, 0, 1);
            case JELLYFISH -> new PetBaseStats(6, 4, 0, 3, 4);
        };
    }
    
    /**
     * Get pet ability name
     */
    public String getAbilityName() {
        return switch (this) {
            case DRAGON -> "Dragon's Breath";
            case WITHER_SKELETON -> "Wither Touch";
            case TIGER -> "Pounce";
            case LION -> "Roar";
            case WOLF -> "Pack Hunt";
            case PHOENIX -> "Phoenix Rebirth";
            case GRIFFIN -> "Griffin's Fury";
            case YETI -> "Ice Blast";
            case ELEPHANT -> "Harvest";
            case RABBIT -> "Quick Harvest";
            case CHICKEN -> "Egg Layer";
            case COW -> "Milk Production";
            case PIG -> "Mud Bath";
            case SHEEP -> "Wool Shearing";
            case MUSHROOM_COW -> "Mushroom Growth";
            case BEE -> "Honey Production";
            case MOLE -> "Mole's Fortune";
            case ROCK -> "Rock Hard";
            case SILVERFISH -> "Cobble Generator";
            case MAGMA_CUBE -> "Magma Splash";
            case SPIDER -> "Web Trap";
            case ENDERMITE -> "Ender Mining";
            case SQUID -> "Ink Cloud";
            case DOLPHIN -> "Echo Location";
            case BLUE_WHALE -> "Whale's Blessing";
            case MAGMAFISH -> "Magma Fishing";
            case FLYING_FISH -> "Water Leap";
            case MONKEY -> "Banana Throw";
            case OCELOT -> "Stealth";
            case PARROT -> "Mimic";
            case GIRAFFE -> "Long Reach";
            case GHOUL -> "Undead Master";
            case ENDERMAN -> "Teleport";
            case BLAZE -> "Fire Blast";
            case SKELETON_HORSE -> "Bone Charge";
            case ZOMBIE_HORSE -> "Zombie Rush";
            case SKELETON -> "Bone Throw";
            case ZOMBIE -> "Zombie Bite";
            case REINDEER -> "Christmas Magic";
            case SNOWMAN -> "Snowball Fight";
            case PENGUIN -> "Ice Slide";
            case TURTLE -> "Shell Defense";
            case JELLYFISH -> "Electric Shock";
        };
    }
    
    /**
     * Get pet ability cooldown
     */
    public int getAbilityCooldown() {
        return switch (this) {
            case DRAGON, PHOENIX, GRIFFIN, YETI -> 60; // 1 minute
            case WITHER_SKELETON, LION, BLAZE -> 45; // 45 seconds
            case TIGER, WOLF, ELEPHANT -> 30; // 30 seconds
            case RABBIT, CHICKEN, SQUID, DOLPHIN -> 20; // 20 seconds
            case MOLE, SILVERFISH, SPIDER -> 15; // 15 seconds
            default -> 25; // Default 25 seconds
        };
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Pet base stats
     */
    public static class PetBaseStats {
        private final int health;
        private final int defense;
        private final int strength;
        private final int intelligence;
        private final int speed;
        
        public PetBaseStats(int health, int defense, int strength, int intelligence, int speed) {
            this.health = health;
            this.defense = defense;
            this.strength = strength;
            this.intelligence = intelligence;
            this.speed = speed;
        }
        
        public int getHealth() {
            return health;
        }
        
        public int getDefense() {
            return defense;
        }
        
        public int getStrength() {
            return strength;
        }
        
        public int getIntelligence() {
            return intelligence;
        }
        
        public int getSpeed() {
            return speed;
        }
    }

    // Base stats for pet calculations
    public double getBaseHealth() {
        return getBaseStats().getHealth();
    }

    public double getBaseDefense() {
        return getBaseStats().getDefense();
    }

    public double getBaseStrength() {
        return getBaseStats().getStrength();
    }

    public double getBaseSpeed() {
        return getBaseStats().getSpeed();
    }

    public double getBaseIntelligence() {
        return getBaseStats().getIntelligence();
    }

    public double getBaseCritChance() {
        return 5.0; // Default crit chance
    }

    public double getBaseCritDamage() {
        return 50.0; // Default crit damage
    }

    public double getBaseAttackSpeed() {
        return 1.0; // Default attack speed
    }

    public double getBaseFerocity() {
        return 0.0; // Default ferocity
    }

    public double getBaseMagicFind() {
        return 0.0; // Default magic find
    }

    public double getBasePetLuck() {
        return 0.0; // Default pet luck
    }

    public double getBaseSeaCreatureChance() {
        return 0.0; // Default sea creature chance
    }

    public double getBaseFishingSpeed() {
        return 0.0; // Default fishing speed
    }

    public double getBaseMiningSpeed() {
        return 0.0; // Default mining speed
    }

    public double getBaseFarmingFortune() {
        return 0.0; // Default farming fortune
    }

    public double getBaseForagingFortune() {
        return 0.0; // Default foraging fortune
    }
}
