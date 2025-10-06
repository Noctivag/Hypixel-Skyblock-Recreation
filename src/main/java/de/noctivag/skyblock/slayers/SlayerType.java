package de.noctivag.skyblock.slayers;

import org.bukkit.Material;

/**
 * Enum representing all slayer types in Hypixel Skyblock
 */
public enum SlayerType {
    ZOMBIE("Zombie Slayer", "&2", "🧟", "Undead creatures that rise from the grave", Material.ROTTEN_FLESH, 
           new String[]{"Revenant Horror", "Atoned Horror", "Soul of the Alpha"}, 
           new String[]{"Crypt Ghoul", "Golden Ghoul", "Diamond Ghoul", "Revenant Horror", "Atoned Horror", "Soul of the Alpha"}),
    
    SPIDER("Spider Slayer", "&8", "🕷", "Eight-legged arachnids that lurk in the shadows", Material.SPIDER_EYE, 
           new String[]{"Tarantula Broodfather", "Mutant Tarantula", "Spider Queen"}, 
           new String[]{"Cave Spider", "Spider Jockey", "Tarantula Broodfather", "Mutant Tarantula", "Spider Queen"}),
    
    WOLF("Wolf Slayer", "&7", "🐺", "Feral wolves that hunt in packs", Material.BONE, 
         new String[]{"Sven Packmaster", "Sven Alpha", "Sven Beastmaster"}, 
         new String[]{"Wolf", "Pack Spirit", "Sven Packmaster", "Sven Alpha", "Sven Beastmaster"}),
    
    ENDERMAN("Enderman Slayer", "&5", "👁", "Mysterious beings from the End dimension", Material.ENDER_PEARL, 
             new String[]{"Voidgloom Seraph", "Voidgloom Seraph II", "Voidgloom Seraph III"}, 
             new String[]{"Enderman", "Voidling", "Voidgloom Seraph", "Voidgloom Seraph II", "Voidgloom Seraph III"}),
    
    BLAZE("Blaze Slayer", "&6", "🔥", "Fiery beings from the Nether realm", Material.BLAZE_ROD, 
          new String[]{"Inferno Demonlord", "Inferno Demonlord II", "Inferno Demonlord III"}, 
          new String[]{"Blaze", "Blaze Jockey", "Inferno Demonlord", "Inferno Demonlord II", "Inferno Demonlord III"});

    private final String displayName;
    private final String color;
    private final String icon;
    private final String description;
    private final Material material;
    private final String[] bosses;
    private final String[] mobs;

    SlayerType(String displayName, String color, String icon, String description, Material material, 
               String[] bosses, String[] mobs) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.description = description;
        this.material = material;
        this.bosses = bosses;
        this.mobs = mobs;
    }

    public String getDisplayName() { return displayName; }
    public String getColor() { return color; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public String[] getBosses() { return bosses; }
    public String[] getMobs() { return mobs; }

    /**
     * Get colored display name
     */
    public String getColoredDisplayName() {
        return color + icon + " " + displayName;
    }

    /**
     * Get slayer lore
     */
    public String[] getSlayerLore() {
        return new String[]{
            "&7" + description,
            "",
            "&7Bosses:",
            "&c• " + bosses[0],
            "&c• " + bosses[1],
            "&c• " + bosses[2],
            "",
            "&7Mobs:",
            "&e• " + mobs[0],
            "&e• " + mobs[1],
            "&e• " + mobs[2],
            "&e• " + mobs[3],
            "&e• " + mobs[4],
            "&e• " + mobs[5],
            "",
            "&eClick to start slayer quest"
        };
    }

    /**
     * Get slayer tier information
     */
    public SlayerTier getTier(int tier) {
        return new SlayerTier(this, tier);
    }

    /**
     * Get max tier for this slayer type
     */
    public int getMaxTier() {
        return 9; // All slayers go up to tier IX
    }

    /**
     * Get boss for a specific tier
     */
    public String getBossForTier(int tier) {
        if (tier >= 1 && tier <= 3) return bosses[0];
        if (tier >= 4 && tier <= 6) return bosses[1];
        if (tier >= 7 && tier <= 9) return bosses[2];
        return bosses[0];
    }

    /**
     * Get tier requirements
     */
    public long getTierRequirement(int tier) {
        switch (this) {
            case ZOMBIE:
                return getZombieTierRequirement(tier);
            case SPIDER:
                return getSpiderTierRequirement(tier);
            case WOLF:
                return getWolfTierRequirement(tier);
            case ENDERMAN:
                return getEndermanTierRequirement(tier);
            case BLAZE:
                return getBlazeTierRequirement(tier);
            default:
                return 1000 * tier;
        }
    }

    private long getZombieTierRequirement(int tier) {
        switch (tier) {
            case 1: return 1000;
            case 2: return 2000;
            case 3: return 5000;
            case 4: return 10000;
            case 5: return 20000;
            case 6: return 50000;
            case 7: return 100000;
            case 8: return 200000;
            case 9: return 500000;
            default: return 0;
        }
    }

    private long getSpiderTierRequirement(int tier) {
        switch (tier) {
            case 1: return 1000;
            case 2: return 2000;
            case 3: return 5000;
            case 4: return 10000;
            case 5: return 20000;
            case 6: return 50000;
            case 7: return 100000;
            case 8: return 200000;
            case 9: return 500000;
            default: return 0;
        }
    }

    private long getWolfTierRequirement(int tier) {
        switch (tier) {
            case 1: return 1000;
            case 2: return 2000;
            case 3: return 5000;
            case 4: return 10000;
            case 5: return 20000;
            case 6: return 50000;
            case 7: return 100000;
            case 8: return 200000;
            case 9: return 500000;
            default: return 0;
        }
    }

    private long getEndermanTierRequirement(int tier) {
        switch (tier) {
            case 1: return 1000;
            case 2: return 2000;
            case 3: return 5000;
            case 4: return 10000;
            case 5: return 20000;
            case 6: return 50000;
            case 7: return 100000;
            case 8: return 200000;
            case 9: return 500000;
            default: return 0;
        }
    }

    private long getBlazeTierRequirement(int tier) {
        switch (tier) {
            case 1: return 1000;
            case 2: return 2000;
            case 3: return 5000;
            case 4: return 10000;
            case 5: return 20000;
            case 6: return 50000;
            case 7: return 100000;
            case 8: return 200000;
            case 9: return 500000;
            default: return 0;
        }
    }

    /**
     * Get slayer cost for a specific tier
     */
    public long getSlayerCost(int tier) {
        return 1000 * tier; // Base cost increases with tier
    }

    /**
     * Get slayer reward for a specific tier
     */
    public long getSlayerReward(int tier) {
        return 5000 * tier; // Base reward increases with tier
    }

    /**
     * Get slayer XP reward for a specific tier
     */
    public long getSlayerXPReward(int tier) {
        return 100 * tier; // XP reward increases with tier
    }

    /**
     * Get slayer spawn location
     */
    public String getSpawnLocation() {
        switch (this) {
            case ZOMBIE: return "Graveyard";
            case SPIDER: return "Spider's Den";
            case WOLF: return "The Park";
            case ENDERMAN: return "The End";
            case BLAZE: return "Crimson Isle";
            default: return "Hub";
        }
    }

    /**
     * Get slayer spawn world
     */
    public String getSpawnWorld() {
        switch (this) {
            case ZOMBIE: return "hub";
            case SPIDER: return "spiders_den";
            case WOLF: return "the_park";
            case ENDERMAN: return "the_end";
            case BLAZE: return "crimson_isle";
            default: return "hub";
        }
    }

    /**
     * Get slayer difficulty
     */
    public String getDifficulty() {
        switch (this) {
            case ZOMBIE: return "Easy";
            case SPIDER: return "Medium";
            case WOLF: return "Medium";
            case ENDERMAN: return "Hard";
            case BLAZE: return "Hard";
            default: return "Easy";
        }
    }

    /**
     * Get slayer difficulty color
     */
    public String getDifficultyColor() {
        switch (getDifficulty()) {
            case "Easy": return "&a";
            case "Medium": return "&e";
            case "Hard": return "&c";
            default: return "&7";
        }
    }
}
