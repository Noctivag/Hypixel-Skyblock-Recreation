package de.noctivag.skyblock.enums;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum für verschiedene Minion-Typen
 */
public enum MinionType {
    
    WHEAT_MINION("§aWheat Minion", Material.WHEAT, 10000, "Generiert Wheat"),
    CARROT_MINION("§6Carrot Minion", Material.CARROT, 10000, "Generiert Carrots"),
    POTATO_MINION("§ePotato Minion", Material.POTATO, 10000, "Generiert Potatoes"),
    PUMPKIN_MINION("§6Pumpkin Minion", Material.PUMPKIN, 12000, "Generiert Pumpkins"),
    MELON_MINION("§aMelon Minion", Material.MELON, 12000, "Generiert Melons"),
    SUGAR_CANE_MINION("§fSugar Cane Minion", Material.SUGAR_CANE, 10000, "Generiert Sugar Cane"),
    CACTUS_MINION("§aCactus Minion", Material.CACTUS, 10000, "Generiert Cactus"),
    COCOA_MINION("§6Cocoa Minion", Material.COCOA_BEANS, 10000, "Generiert Cocoa Beans"),
    
    COAL_MINION("§8Coal Minion", Material.COAL, 15000, "Generiert Coal"),
    IRON_MINION("§7Iron Minion", Material.IRON_INGOT, 20000, "Generiert Iron Ingots"),
    GOLD_MINION("§6Gold Minion", Material.GOLD_INGOT, 25000, "Generiert Gold Ingots"),
    DIAMOND_MINION("§bDiamond Minion", Material.DIAMOND, 30000, "Generiert Diamonds"),
    EMERALD_MINION("§aEmerald Minion", Material.EMERALD, 35000, "Generiert Emeralds"),
    REDSTONE_MINION("§cRedstone Minion", Material.REDSTONE, 15000, "Generiert Redstone"),
    LAPIS_MINION("§9Lapis Minion", Material.LAPIS_LAZULI, 15000, "Generiert Lapis Lazuli"),
    QUARTZ_MINION("§fQuartz Minion", Material.QUARTZ, 20000, "Generiert Quartz"),
    
    COBBLESTONE_MINION("§7Cobblestone Minion", Material.COBBLESTONE, 8000, "Generiert Cobblestone"),
    OBSIDIAN_MINION("§5Obsidian Minion", Material.OBSIDIAN, 40000, "Generiert Obsidian"),
    SAND_MINION("§eSand Minion", Material.SAND, 10000, "Generiert Sand"),
    GRAVEL_MINION("§7Gravel Minion", Material.GRAVEL, 10000, "Generiert Gravel"),
    CLAY_MINION("§9Clay Minion", Material.CLAY, 12000, "Generiert Clay"),
    ICE_MINION("§bIce Minion", Material.ICE, 15000, "Generiert Ice"),
    SNOW_MINION("§fSnow Minion", Material.SNOW_BLOCK, 10000, "Generiert Snow Blocks"),
    END_STONE_MINION("§eEnd Stone Minion", Material.END_STONE, 20000, "Generiert End Stone"),
    
    OAK_MINION("§6Oak Minion", Material.OAK_LOG, 10000, "Generiert Oak Logs"),
    BIRCH_MINION("§fBirch Minion", Material.BIRCH_LOG, 10000, "Generiert Birch Logs"),
    SPRUCE_MINION("§8Spruce Minion", Material.SPRUCE_LOG, 10000, "Generiert Spruce Logs"),
    JUNGLE_MINION("§aJungle Minion", Material.JUNGLE_LOG, 10000, "Generiert Jungle Logs"),
    ACACIA_MINION("§cAcacia Minion", Material.ACACIA_LOG, 10000, "Generiert Acacia Logs"),
    DARK_OAK_MINION("§8Dark Oak Minion", Material.DARK_OAK_LOG, 10000, "Generiert Dark Oak Logs"),
    
    COW_MINION("§7Cow Minion", Material.LEATHER, 12000, "Generiert Leather"),
    PIG_MINION("§dPig Minion", Material.PORKCHOP, 12000, "Generiert Porkchops"),
    CHICKEN_MINION("§fChicken Minion", Material.CHICKEN, 10000, "Generiert Chicken"),
    SHEEP_MINION("§fSheep Minion", Material.WHITE_WOOL, 12000, "Generiert Wool"),
    RABBIT_MINION("§eRabbit Minion", Material.RABBIT_HIDE, 10000, "Generiert Rabbit Hide"),
    MUSHROOM_MINION("§cMushroom Minion", Material.RED_MUSHROOM, 15000, "Generiert Mushrooms");
    
    private final String displayName;
    private final Material material;
    private final long baseActionTime;
    private final String description;
    
    MinionType(String displayName, Material material, long baseActionTime, String description) {
        this.displayName = displayName;
        this.material = material;
        this.baseActionTime = baseActionTime;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public long getBaseActionTime() {
        return baseActionTime;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Generiert Items basierend auf dem Minion-Typ und Level
     */
    public List<org.bukkit.inventory.ItemStack> generateItems(int level) {
        List<org.bukkit.inventory.ItemStack> items = new ArrayList<>();
        
        // Basis-Items pro Aktion
        int baseAmount = 1;
        
        // Level-Bonus
        int levelBonus = (level - 1) / 2; // Jedes 2. Level gibt +1 Item
        
        // Zufällige Chance auf zusätzliche Items
        int randomBonus = Math.random() < 0.1 ? 1 : 0; // 10% Chance
        
        int totalAmount = baseAmount + levelBonus + randomBonus;
        
        // Erstelle Items
        for (int i = 0; i < totalAmount; i++) {
            items.add(new org.bukkit.inventory.ItemStack(material));
        }
        
        return items;
    }
    
    /**
     * Gibt alle Minion-Typen einer Kategorie zurück
     */
    public static List<MinionType> getMinionsByCategory(String category) {
        List<MinionType> minions = new ArrayList<>();
        
        switch (category.toLowerCase()) {
            case "farming":
                for (MinionType minion : values()) {
                    if (minion.name().contains("WHEAT") || minion.name().contains("CARROT") || 
                        minion.name().contains("POTATO") || minion.name().contains("PUMPKIN") ||
                        minion.name().contains("MELON") || minion.name().contains("SUGAR") ||
                        minion.name().contains("CACTUS") || minion.name().contains("COCOA")) {
                        minions.add(minion);
                    }
                }
                break;
            case "mining":
                for (MinionType minion : values()) {
                    if (minion.name().contains("COAL") || minion.name().contains("IRON") || 
                        minion.name().contains("GOLD") || minion.name().contains("DIAMOND") ||
                        minion.name().contains("EMERALD") || minion.name().contains("REDSTONE") ||
                        minion.name().contains("LAPIS") || minion.name().contains("QUARTZ") ||
                        minion.name().contains("COBBLESTONE") || minion.name().contains("OBSIDIAN") ||
                        minion.name().contains("SAND") || minion.name().contains("GRAVEL") ||
                        minion.name().contains("CLAY") || minion.name().contains("ICE") ||
                        minion.name().contains("SNOW") || minion.name().contains("END_STONE")) {
                        minions.add(minion);
                    }
                }
                break;
            case "foraging":
                for (MinionType minion : values()) {
                    if (minion.name().contains("OAK") || minion.name().contains("BIRCH") || 
                        minion.name().contains("SPRUCE") || minion.name().contains("JUNGLE") ||
                        minion.name().contains("ACACIA") || minion.name().contains("DARK_OAK")) {
                        minions.add(minion);
                    }
                }
                break;
            case "combat":
                for (MinionType minion : values()) {
                    if (minion.name().contains("COW") || minion.name().contains("PIG") || 
                        minion.name().contains("CHICKEN") || minion.name().contains("SHEEP") ||
                        minion.name().contains("RABBIT") || minion.name().contains("MUSHROOM")) {
                        minions.add(minion);
                    }
                }
                break;
        }
        
        return minions;
    }
}
