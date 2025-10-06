package de.noctivag.skyblock.pets;

import org.bukkit.Material;

/**
 * Enum representing all pet types in Hypixel Skyblock
 */
public enum PetType {
    // Combat Pets
    ENDERMAN("Enderman", "&5", "👁", "Combat", "Teleports and deals extra damage", Material.ENDER_PEARL, 
             new String[]{"Teleport", "Ender Shield", "Teleport Strike"}, 
             new String[]{"Combat XP", "Teleportation", "Enderman Damage"}),
    
    ZOMBIE("Zombie", "&2", "🧟", "Combat", "Undead companion that heals you", Material.ROTTEN_FLESH, 
           new String[]{"Zombie Armor", "Undead Army", "Zombie Sword"}, 
           new String[]{"Combat XP", "Healing", "Zombie Damage"}),
    
    SKELETON("Skeleton", "&7", "💀", "Combat", "Ranged combat specialist", Material.BONE, 
             new String[]{"Skeleton Master", "Bone Arrows", "Skeleton Army"}, 
             new String[]{"Combat XP", "Ranged Damage", "Skeleton Damage"}),
    
    SPIDER("Spider", "&8", "🕷", "Combat", "Web-slinging arachnid companion", Material.SPIDER_EYE, 
           new String[]{"Web Shot", "Spider Swarm", "Venom Strike"}, 
           new String[]{"Combat XP", "Web Effects", "Spider Damage"}),
    
    WOLF("Wolf", "&7", "🐺", "Combat", "Loyal wolf companion", Material.BONE, 
         new String[]{"Pack Leader", "Howl", "Wolf Pack"}, 
         new String[]{"Combat XP", "Pack Effects", "Wolf Damage"}),
    
    // Mining Pets
    ROCK("Rock", "&7", "🪨", "Mining", "Solid companion for mining", Material.STONE, 
         new String[]{"Mining Speed", "Ore Sense", "Rock Throw"}, 
         new String[]{"Mining XP", "Mining Speed", "Ore Detection"}),
    
    SILVERFISH("Silverfish", "&7", "🐛", "Mining", "Tiny mining companion", Material.STONE_BRICKS, 
               new String[]{"Mining Speed", "Ore Sense", "Silverfish Swarm"}, 
               new String[]{"Mining XP", "Mining Speed", "Ore Detection"}),
    
    ENDERMITE("Endermite", "&5", "🐛", "Mining", "End dimension mining pet", Material.ENDER_PEARL, 
              new String[]{"Mining Speed", "End Ore Sense", "Endermite Swarm"}, 
              new String[]{"Mining XP", "Mining Speed", "End Ore Detection"}),
    
    // Farming Pets
    RABBIT("Rabbit", "&f", "🐰", "Farming", "Fast farming companion", Material.RABBIT_FOOT, 
           new String[]{"Farming Speed", "Crop Growth", "Rabbit Jump"}, 
           new String[]{"Farming XP", "Farming Speed", "Crop Growth"}),
    
    CHICKEN("Chicken", "&f", "🐔", "Farming", "Egg-laying farming pet", Material.EGG, 
            new String[]{"Farming Speed", "Egg Production", "Chicken Flock"}, 
            new String[]{"Farming XP", "Farming Speed", "Egg Production"}),
    
    PIG("Pig", "&d", "🐷", "Farming", "Mud-loving farming companion", Material.PORKCHOP, 
        new String[]{"Farming Speed", "Mud Boost", "Pig Snort"}, 
        new String[]{"Farming XP", "Farming Speed", "Mud Effects"}),
    
    COW("Cow", "&f", "🐄", "Farming", "Milk-producing farming pet", Material.MILK_BUCKET, 
        new String[]{"Farming Speed", "Milk Production", "Cow Herd"}, 
        new String[]{"Farming XP", "Farming Speed", "Milk Production"}),
    
    // Foraging Pets
    MONKEY("Monkey", "&6", "🐵", "Foraging", "Tree-climbing foraging pet", Material.VINE, 
           new String[]{"Foraging Speed", "Tree Climb", "Monkey Business"}, 
           new String[]{"Foraging XP", "Foraging Speed", "Tree Climbing"}),
    
    OCELOT("Ocelot", "&6", "🐱", "Foraging", "Stealthy foraging companion", Material.LEATHER, 
           new String[]{"Foraging Speed", "Stealth", "Ocelot Pounce"}, 
           new String[]{"Foraging XP", "Foraging Speed", "Stealth Effects"}),
    
    // Fishing Pets
    SQUID("Squid", "&9", "🦑", "Fishing", "Underwater fishing companion", Material.INK_SAC, 
          new String[]{"Fishing Speed", "Ink Cloud", "Squid Ink"}, 
          new String[]{"Fishing XP", "Fishing Speed", "Ink Effects"}),
    
    GUARDIAN("Guardian", "&b", "🐡", "Fishing", "Ocean guardian fishing pet", Material.PRISMARINE_SHARD, 
             new String[]{"Fishing Speed", "Guardian Beam", "Ocean Protection"}, 
             new String[]{"Fishing XP", "Fishing Speed", "Ocean Effects"}),
    
    DOLPHIN("Dolphin", "&b", "🐬", "Fishing", "Intelligent fishing companion", Material.SALMON, 
            new String[]{"Fishing Speed", "Dolphin Grace", "Ocean Sense"}, 
            new String[]{"Fishing XP", "Fishing Speed", "Ocean Effects"}),
    
    // Special Pets
    DRAGON("Dragon", "&d", "🐉", "Special", "Legendary dragon companion", Material.DRAGON_EGG, 
           new String[]{"Dragon Breath", "Flight", "Dragon Roar"}, 
           new String[]{"All XP", "Flight", "Dragon Effects"}),
    
    PHOENIX("Phoenix", "&c", "🔥", "Special", "Reborn from ashes", Material.FIRE_CHARGE, 
            new String[]{"Phoenix Rebirth", "Fire Immunity", "Phoenix Fire"}, 
            new String[]{"All XP", "Fire Immunity", "Phoenix Effects"}),
    
    GRIFFIN("Griffin", "&6", "🦅", "Special", "Majestic flying companion", Material.FEATHER, 
            new String[]{"Griffin Flight", "Eagle Eye", "Griffin Strike"}, 
            new String[]{"All XP", "Flight", "Griffin Effects"}),
    
    TIGER("Tiger", "&6", "🐅", "Special", "Fierce tiger companion", Material.ORANGE_DYE, 
          new String[]{"Tiger Pounce", "Fierce Roar", "Tiger Claws"}, 
          new String[]{"All XP", "Combat Boost", "Tiger Effects"}),
    
    LION("Lion", "&6", "🦁", "Special", "King of the jungle", Material.GOLD_INGOT, 
         new String[]{"Lion Roar", "King's Pride", "Lion Mane"}, 
         new String[]{"All XP", "Leadership", "Lion Effects"}),
    
    ELEPHANT("Elephant", "&7", "🐘", "Special", "Gentle giant companion", Material.GRAY_DYE, 
             new String[]{"Elephant Memory", "Gentle Giant", "Elephant Trunk"}, 
             new String[]{"All XP", "Memory Boost", "Elephant Effects"}),
    
    GIRAFFE("Giraffe", "&e", "🦒", "Special", "Tall and graceful companion", Material.YELLOW_DYE, 
            new String[]{"Giraffe Reach", "Tall Vision", "Giraffe Neck"}, 
            new String[]{"All XP", "Reach Boost", "Giraffe Effects"}),
    
    PANDA("Panda", "&f", "🐼", "Special", "Cute and cuddly companion", Material.BAMBOO, 
          new String[]{"Panda Hug", "Bamboo Boost", "Panda Roll"}, 
          new String[]{"All XP", "Happiness", "Panda Effects"}),
    
    TURTLE("Turtle", "&a", "🐢", "Special", "Slow and steady companion", Material.TURTLE_EGG, 
           new String[]{"Turtle Shell", "Slow and Steady", "Turtle Wisdom"}, 
           new String[]{"All XP", "Defense", "Turtle Effects"}),
    
    BEE("Bee", "&e", "🐝", "Special", "Busy bee companion", Material.HONEYCOMB, 
        new String[]{"Bee Sting", "Honey Production", "Bee Swarm"}, 
        new String[]{"All XP", "Honey Effects", "Bee Effects"}),
    
    PARROT("Parrot", "&a", "🦜", "Special", "Colorful talking companion", Material.FEATHER, 
           new String[]{"Parrot Talk", "Colorful Feathers", "Parrot Mimic"}, 
           new String[]{"All XP", "Communication", "Parrot Effects"}),
    
    PENGUIN("Penguin", "&b", "🐧", "Special", "Cold weather companion", Material.SNOWBALL, 
            new String[]{"Penguin Slide", "Cold Resistance", "Penguin Waddle"}, 
            new String[]{"All XP", "Cold Resistance", "Penguin Effects"}),
    
    JELLYFISH("Jellyfish", "&d", "🪼", "Special", "Floating ocean companion", Material.SLIME_BALL, 
              new String[]{"Jellyfish Float", "Ocean Drift", "Jellyfish Sting"}, 
              new String[]{"All XP", "Ocean Effects", "Jellyfish Effects"});

    private final String displayName;
    private final String color;
    private final String icon;
    private final String category;
    private final String description;
    private final Material material;
    private final String[] abilities;
    private final String[] benefits;

    PetType(String displayName, String color, String icon, String category, String description, Material material, 
            String[] abilities, String[] benefits) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.category = category;
        this.description = description;
        this.material = material;
        this.abilities = abilities;
        this.benefits = benefits;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public String getIcon() { return icon; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public String[] getAbilities() { return abilities; }
    public String[] getBenefits() { return benefits; }

    /**
     * Get colored display name
     */
    public String getColoredDisplayName() {
        return color + icon + " " + displayName;
    }

    /**
     * Get pet lore
     */
    public String[] getPetLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Category: &a" + category,
            "&7Abilities:",
            "&a• " + abilities[0],
            "&a• " + abilities[1],
            "&a• " + abilities[2],
            "",
            "&7Benefits:",
            "&e• " + benefits[0],
            "&e• " + benefits[1],
            "&e• " + benefits[2],
            "",
            "&eClick to view pet details"
        };
    }

    /**
     * Get pets by category
     */
    public static PetType[] getByCategory(String category) {
        return java.util.Arrays.stream(values())
                .filter(pet -> pet.getCategory().equalsIgnoreCase(category))
                .toArray(PetType[]::new);
    }

    /**
     * Get all categories
     */
    public static String[] getAllCategories() {
        return java.util.Arrays.stream(values())
                .map(PetType::getCategory)
                .distinct()
                .toArray(String[]::new);
    }

    /**
     * Get pet rarity
     */
    public String getRarity() {
        switch (this) {
            case DRAGON:
            case PHOENIX:
            case GRIFFIN:
                return "legendary";
            case TIGER:
            case LION:
            case ELEPHANT:
            case GIRAFFE:
            case PANDA:
            case TURTLE:
            case BEE:
            case PARROT:
            case PENGUIN:
            case JELLYFISH:
                return "epic";
            case ENDERMAN:
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case WOLF:
            case ROCK:
            case SILVERFISH:
            case ENDERMITE:
            case RABBIT:
            case CHICKEN:
            case PIG:
            case COW:
            case MONKEY:
            case OCELOT:
            case SQUID:
            case GUARDIAN:
            case DOLPHIN:
                return "rare";
            default:
                return "uncommon";
        }
    }

    /**
     * Get pet rarity color
     */
    public String getRarityColor() {
        switch (getRarity()) {
            case "legendary": return "&6";
            case "epic": return "&5";
            case "rare": return "&b";
            case "uncommon": return "&a";
            default: return "&7";
        }
    }

    /**
     * Get pet tier requirements
     */
    public int getTierRequirement(int tier) {
        switch (getRarity()) {
            case "legendary":
                return 100 * tier; // 100, 200, 300, 400, 500
            case "epic":
                return 75 * tier; // 75, 150, 225, 300, 375
            case "rare":
                return 50 * tier; // 50, 100, 150, 200, 250
            case "uncommon":
                return 25 * tier; // 25, 50, 75, 100, 125
            default:
                return 10 * tier; // 10, 20, 30, 40, 50
        }
    }

    /**
     * Get pet upgrade cost
     */
    public long getUpgradeCost(int tier) {
        switch (getRarity()) {
            case "legendary":
                return 1000000L * tier; // 1M, 2M, 3M, 4M, 5M
            case "epic":
                return 500000L * tier; // 500K, 1M, 1.5M, 2M, 2.5M
            case "rare":
                return 250000L * tier; // 250K, 500K, 750K, 1M, 1.25M
            case "uncommon":
                return 100000L * tier; // 100K, 200K, 300K, 400K, 500K
            default:
                return 50000L * tier; // 50K, 100K, 150K, 200K, 250K
        }
    }

    /**
     * Get pet XP requirement for level
     */
    public long getXPRequirement(int level) {
        if (level <= 1) return 0;
        return (long) (100 * Math.pow(1.5, level - 1)); // Exponential growth
    }

    /**
     * Get pet max level
     */
    public int getMaxLevel() {
        return 100; // All pets can reach level 100
    }

    /**
     * Get pet tier from level
     */
    public int getTierFromLevel(int level) {
        if (level >= 100) return 5;
        if (level >= 80) return 4;
        if (level >= 60) return 3;
        if (level >= 40) return 2;
        if (level >= 20) return 1;
        return 0;
    }

    /**
     * Get pet tier display name
     */
    public String getTierDisplayName(int tier) {
        switch (tier) {
            case 0: return "Common";
            case 1: return "Uncommon";
            case 2: return "Rare";
            case 3: return "Epic";
            case 4: return "Legendary";
            case 5: return "Mythic";
            default: return "Unknown";
        }
    }

    /**
     * Get pet tier color
     */
    public String getTierColor(int tier) {
        switch (tier) {
            case 0: return "&7";
            case 1: return "&a";
            case 2: return "&9";
            case 3: return "&5";
            case 4: return "&6";
            case 5: return "&d";
            default: return "&7";
        }
    }
}