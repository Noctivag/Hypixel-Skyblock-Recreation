package de.noctivag.skyblock.features.minions.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All available minion types in Hypixel Skyblock
 */
public enum MinionType {
    // Farming Minions
    WHEAT("Wheat", "🌾", MinionCategory.FARMING, "Wheat", 60),
    CARROT("Carrot", "🥕", MinionCategory.FARMING, "Carrot", 60),
    POTATO("Potato", "🥔", MinionCategory.FARMING, "Potato", 60),
    PUMPKIN("Pumpkin", "🎃", MinionCategory.FARMING, "Pumpkin", 60),
    MELON("Melon", "🍈", MinionCategory.FARMING, "Melon", 60),
    MUSHROOM("Mushroom", "🍄", MinionCategory.FARMING, "Red Mushroom", 60),
    COCOA("Cocoa", "🍫", MinionCategory.FARMING, "Cocoa Beans", 60),
    CACTUS("Cactus", "🌵", MinionCategory.FARMING, "Cactus", 60),
    SUGAR_CANE("Sugar Cane", "🎋", MinionCategory.FARMING, "Sugar Cane", 60),
    NETHER_WART("Nether Wart", "🔴", MinionCategory.FARMING, "Nether Wart", 60),
    
    // Mining Minions
    COBBLESTONE("Cobblestone", "🪨", MinionCategory.MINING, "Cobblestone", 30),
    COAL("Coal", "⚫", MinionCategory.MINING, "Coal", 60),
    IRON("Iron", "⚙️", MinionCategory.MINING, "Iron Ingot", 60),
    GOLD("Gold", "🟡", MinionCategory.MINING, "Gold Ingot", 60),
    DIAMOND("Diamond", "💎", MinionCategory.MINING, "Diamond", 120),
    LAPIS("Lapis", "🔵", MinionCategory.MINING, "Lapis Lazuli", 60),
    EMERALD("Emerald", "💚", MinionCategory.MINING, "Emerald", 120),
    REDSTONE("Redstone", "🔴", MinionCategory.MINING, "Redstone", 60),
    QUARTZ("Quartz", "⚪", MinionCategory.MINING, "Nether Quartz", 60),
    OBSIDIAN("Obsidian", "🖤", MinionCategory.MINING, "Obsidian", 120),
    GLOWSTONE("Glowstone", "💡", MinionCategory.MINING, "Glowstone", 60),
    GRAVEL("Gravel", "🪨", MinionCategory.MINING, "Gravel", 60),
    SAND("Sand", "🏖️", MinionCategory.MINING, "Sand", 60),
    END_STONE("End Stone", "🟤", MinionCategory.MINING, "End Stone", 60),
    SNOW("Snow", "❄️", MinionCategory.MINING, "Snowball", 60),
    
    // Combat Minions
    ZOMBIE("Zombie", "🧟", MinionCategory.COMBAT, "Rotten Flesh", 60),
    SKELETON("Skeleton", "💀", MinionCategory.COMBAT, "Bone", 60),
    SPIDER("Spider", "🕷️", MinionCategory.COMBAT, "String", 60),
    CAVESPIDER("Cave Spider", "🕸️", MinionCategory.COMBAT, "String", 60),
    CREEPER("Creeper", "💥", MinionCategory.COMBAT, "Gunpowder", 60),
    ENDERMAN("Enderman", "🟣", MinionCategory.COMBAT, "Ender Pearl", 120),
    BLAZE("Blaze", "🔥", MinionCategory.COMBAT, "Blaze Rod", 120),
    MAGMA_CUBE("Magma Cube", "🌋", MinionCategory.COMBAT, "Magma Cream", 60),
    GHAST("Ghast", "👻", MinionCategory.COMBAT, "Ghast Tear", 120),
    SLIME("Slime", "🟢", MinionCategory.COMBAT, "Slimeball", 60),
    PIG("Pig", "🐷", MinionCategory.COMBAT, "Porkchop", 60),
    COW("Cow", "🐄", MinionCategory.COMBAT, "Leather", 60),
    CHICKEN("Chicken", "🐔", MinionCategory.COMBAT, "Raw Chicken", 60),
    SHEEP("Sheep", "🐑", MinionCategory.COMBAT, "Mutton", 60),
    RABBIT("Rabbit", "🐰", MinionCategory.COMBAT, "Rabbit Hide", 60),
    
    // Foraging Minions
    OAK("Oak", "🌳", MinionCategory.FORAGING, "Oak Wood", 60),
    SPRUCE("Spruce", "🌲", MinionCategory.FORAGING, "Spruce Wood", 60),
    BIRCH("Birch", "🌳", MinionCategory.FORAGING, "Birch Wood", 60),
    JUNGLE("Jungle", "🌴", MinionCategory.FORAGING, "Jungle Wood", 60),
    ACACIA("Acacia", "🌵", MinionCategory.FORAGING, "Acacia Wood", 60),
    DARK_OAK("Dark Oak", "🌳", MinionCategory.FORAGING, "Dark Oak Wood", 60),
    
    // Fishing Minions
    FISHING("Fishing", "🎣", MinionCategory.FISHING, "Raw Fish", 60),
    
    // Special Minions
    FLOWER("Flower", "🌸", MinionCategory.SPECIAL, "Flower", 60),
    REVENANT("Revenant", "💀", MinionCategory.SPECIAL, "Revenant Viscera", 120),
    TARANTULA("Tarantula", "🕷️", MinionCategory.SPECIAL, "Tarantula Silk", 120),
    VOIDLING("Voidling", "🌑", MinionCategory.SPECIAL, "Void Fragment", 120);
    
    private final String displayName;
    private final String icon;
    private final MinionCategory category;
    private final String resourceName;
    private final int baseActionTime; // in seconds
    
    MinionType(String displayName, String icon, MinionCategory category, String resourceName, int baseActionTime) {
        this.displayName = displayName;
        this.icon = icon;
        this.category = category;
        this.resourceName = resourceName;
        this.baseActionTime = baseActionTime;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public MinionCategory getCategory() {
        return category;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public int getBaseActionTime() {
        return baseActionTime;
    }
    
    /**
     * Get minions by category
     */
    public static List<MinionType> getByCategory(MinionCategory category) {
        return Arrays.stream(values())
            .filter(minion -> minion.getCategory() == category)
            .toList();
    }
    
    /**
     * Get all farming minions
     */
    public static List<MinionType> getFarmingMinions() {
        return getByCategory(MinionCategory.FARMING);
    }
    
    /**
     * Get all mining minions
     */
    public static List<MinionType> getMiningMinions() {
        return getByCategory(MinionCategory.MINING);
    }
    
    /**
     * Get all combat minions
     */
    public static List<MinionType> getCombatMinions() {
        return getByCategory(MinionCategory.COMBAT);
    }
    
    /**
     * Get all foraging minions
     */
    public static List<MinionType> getForagingMinions() {
        return getByCategory(MinionCategory.FORAGING);
    }
    
    /**
     * Get all fishing minions
     */
    public static List<MinionType> getFishingMinions() {
        return getByCategory(MinionCategory.FISHING);
    }
    
    /**
     * Get all special minions
     */
    public static List<MinionType> getSpecialMinions() {
        return getByCategory(MinionCategory.SPECIAL);
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
