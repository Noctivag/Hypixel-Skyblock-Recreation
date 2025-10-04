package de.noctivag.skyblock.features.minions;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

/**
 * Represents a resource produced by a minion
 */
public class MinionResource {
    
    private final String name;
    private final int amount;
    private final Material material;
    private final String displayName;
    
    public MinionResource(String name, int amount) {
        this.name = name;
        this.amount = amount;
        this.material = getMaterialFromName(name);
        this.displayName = getDisplayNameFromName(name);
    }
    
    public MinionResource(String name, int amount, Material material, String displayName) {
        this.name = name;
        this.amount = amount;
        this.material = material;
        this.displayName = displayName;
    }
    
    /**
     * Get material from resource name
     */
    private Material getMaterialFromName(String name) {
        return switch (name.toLowerCase()) {
            case "wheat" -> Material.WHEAT;
            case "carrot" -> Material.CARROT;
            case "potato" -> Material.POTATO;
            case "pumpkin" -> Material.PUMPKIN;
            case "melon" -> Material.MELON_SLICE;
            case "red mushroom" -> Material.RED_MUSHROOM;
            case "cocoa beans" -> Material.COCOA_BEANS;
            case "cactus" -> Material.CACTUS;
            case "sugar cane" -> Material.SUGAR_CANE;
            case "nether wart" -> Material.NETHER_WART;
            case "cobblestone" -> Material.COBBLESTONE;
            case "coal" -> Material.COAL;
            case "iron ingot" -> Material.IRON_INGOT;
            case "gold ingot" -> Material.GOLD_INGOT;
            case "diamond" -> Material.DIAMOND;
            case "lapis lazuli" -> Material.LAPIS_LAZULI;
            case "emerald" -> Material.EMERALD;
            case "redstone" -> Material.REDSTONE;
            case "nether quartz" -> Material.QUARTZ;
            case "obsidian" -> Material.OBSIDIAN;
            case "glowstone" -> Material.GLOWSTONE;
            case "gravel" -> Material.GRAVEL;
            case "sand" -> Material.SAND;
            case "end stone" -> Material.END_STONE;
            case "snowball" -> Material.SNOWBALL;
            case "rotten flesh" -> Material.ROTTEN_FLESH;
            case "bone" -> Material.BONE;
            case "string" -> Material.STRING;
            case "gunpowder" -> Material.GUNPOWDER;
            case "ender pearl" -> Material.ENDER_PEARL;
            case "blaze rod" -> Material.BLAZE_ROD;
            case "magma cream" -> Material.MAGMA_CREAM;
            case "ghast tear" -> Material.GHAST_TEAR;
            case "slimeball" -> Material.SLIME_BALL;
            case "porkchop" -> Material.PORKCHOP;
            case "leather" -> Material.LEATHER;
            case "raw chicken" -> Material.CHICKEN;
            case "mutton" -> Material.MUTTON;
            case "rabbit hide" -> Material.RABBIT_HIDE;
            case "oak wood" -> Material.OAK_LOG;
            case "spruce wood" -> Material.SPRUCE_LOG;
            case "birch wood" -> Material.BIRCH_LOG;
            case "jungle wood" -> Material.JUNGLE_LOG;
            case "acacia wood" -> Material.ACACIA_LOG;
            case "dark oak wood" -> Material.DARK_OAK_LOG;
            case "raw fish" -> Material.COD;
            case "flower" -> Material.POPPY;
            default -> Material.AIR;
        };
    }
    
    /**
     * Get display name from resource name
     */
    private String getDisplayNameFromName(String name) {
        return switch (name.toLowerCase()) {
            case "wheat" -> "Wheat";
            case "carrot" -> "Carrot";
            case "potato" -> "Potato";
            case "pumpkin" -> "Pumpkin";
            case "melon" -> "Melon Slice";
            case "red mushroom" -> "Red Mushroom";
            case "cocoa beans" -> "Cocoa Beans";
            case "cactus" -> "Cactus";
            case "sugar cane" -> "Sugar Cane";
            case "nether wart" -> "Nether Wart";
            case "cobblestone" -> "Cobblestone";
            case "coal" -> "Coal";
            case "iron ingot" -> "Iron Ingot";
            case "gold ingot" -> "Gold Ingot";
            case "diamond" -> "Diamond";
            case "lapis lazuli" -> "Lapis Lazuli";
            case "emerald" -> "Emerald";
            case "redstone" -> "Redstone";
            case "nether quartz" -> "Nether Quartz";
            case "obsidian" -> "Obsidian";
            case "glowstone" -> "Glowstone";
            case "gravel" -> "Gravel";
            case "sand" -> "Sand";
            case "end stone" -> "End Stone";
            case "snowball" -> "Snowball";
            case "rotten flesh" -> "Rotten Flesh";
            case "bone" -> "Bone";
            case "string" -> "String";
            case "gunpowder" -> "Gunpowder";
            case "ender pearl" -> "Ender Pearl";
            case "blaze rod" -> "Blaze Rod";
            case "magma cream" -> "Magma Cream";
            case "ghast tear" -> "Ghast Tear";
            case "slimeball" -> "Slimeball";
            case "porkchop" -> "Raw Porkchop";
            case "leather" -> "Leather";
            case "raw chicken" -> "Raw Chicken";
            case "mutton" -> "Raw Mutton";
            case "rabbit hide" -> "Rabbit Hide";
            case "oak wood" -> "Oak Wood";
            case "spruce wood" -> "Spruce Wood";
            case "birch wood" -> "Birch Wood";
            case "jungle wood" -> "Jungle Wood";
            case "acacia wood" -> "Acacia Wood";
            case "dark oak wood" -> "Dark Oak Wood";
            case "raw fish" -> "Raw Fish";
            case "flower" -> "Flower";
            default -> name;
        };
    }
    
    public String getName() {
        return name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return amount + "x " + displayName;
    }
}
