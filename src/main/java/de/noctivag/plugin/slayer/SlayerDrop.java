package de.noctivag.plugin.slayer;

import org.bukkit.Material;

/**
 * Slayer Drop - Represents a rare drop from slayer bosses
 */
public class SlayerDrop {
    
    private final String name;
    private final String description;
    private final Material material;
    private final SlayerDropRarity rarity;
    private final SlayerSystem.SlayerType type;
    private final SlayerSystem.SlayerTier tier;
    private final double baseChance;
    
    public SlayerDrop(String name, String description, Material material, SlayerDropRarity rarity,
                     SlayerSystem.SlayerType type, SlayerSystem.SlayerTier tier, double baseChance) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.rarity = rarity;
        this.type = type;
        this.tier = tier;
        this.baseChance = baseChance;
    }
    
    /**
     * Get drops for a specific slayer type
     */
    public static SlayerDrop[] getDropsForType(SlayerSystem.SlayerType type) {
        return switch (type) {
            case ZOMBIE -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§2Rotten Flesh", "§7Basic zombie drop", Material.ROTTEN_FLESH, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§aUndead Catalyst", "§7Increases undead damage", Material.ROTTEN_FLESH, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Revenant Viscera", "§7Rare zombie material", Material.ROTTEN_FLESH, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Revenant Falchion", "§7Powerful zombie sword", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dRevenant Armor", "§7Legendary zombie armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
            
            case SPIDER -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§8String", "§7Basic spider drop", Material.STRING, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§7Spider Eye", "§7Spider vision material", Material.SPIDER_EYE, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Tarantula Web", "§7Rare spider silk", Material.STRING, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Tarantula Blade", "§7Deadly spider weapon", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dTarantula Armor", "§7Legendary spider armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
            
            case WOLF -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§fBone", "§7Basic wolf drop", Material.BONE, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§7Wolf Tooth", "§7Sharp canine tooth", Material.BONE, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Sven Fur", "§7Rare wolf pelt", Material.WHITE_WOOL, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Pooch Sword", "§7Powerful wolf blade", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dSven Armor", "§7Legendary wolf armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
            
            case ENDERMAN -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§5Ender Pearl", "§7Basic enderman drop", Material.ENDER_PEARL, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§7Null Sphere", "§7Void energy sphere", Material.ENDER_PEARL, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Void Fragment", "§7Rare void material", Material.ENDER_PEARL, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Voidedge Katana", "§7Void-cutting blade", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dVoidgloom Armor", "§7Legendary void armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
            
            case BLAZE -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§6Blaze Rod", "§7Basic blaze drop", Material.BLAZE_ROD, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§7Inferno Fragment", "§7Fire essence", Material.BLAZE_ROD, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Demonlord Fragment", "§7Rare demon material", Material.BLAZE_ROD, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Inferno Sword", "§7Burning blade of power", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dInferno Armor", "§7Legendary fire armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
            
            case VAMPIRE -> new SlayerDrop[]{
                // Tier I drops
                new SlayerDrop("§4Blood", "§7Basic vampire essence", Material.REDSTONE, 
                    SlayerDropRarity.COMMON, type, SlayerSystem.SlayerTier.TIER_I, 0.5),
                
                // Tier II drops
                new SlayerDrop("§7Blood Chalice", "§7Vampire ritual item", Material.GOLD_INGOT, 
                    SlayerDropRarity.UNCOMMON, type, SlayerSystem.SlayerTier.TIER_II, 0.1),
                
                // Tier III drops
                new SlayerDrop("§6Riftstalker Fragment", "§7Rare vampire material", Material.REDSTONE, 
                    SlayerDropRarity.RARE, type, SlayerSystem.SlayerTier.TIER_III, 0.05),
                
                // Tier IV drops
                new SlayerDrop("§5Vampire Sword", "§7Blood-drinking blade", Material.IRON_SWORD, 
                    SlayerDropRarity.EPIC, type, SlayerSystem.SlayerTier.TIER_IV, 0.02),
                
                // Tier V drops
                new SlayerDrop("§dVampire Armor", "§7Legendary vampire armor", Material.DIAMOND_CHESTPLATE, 
                    SlayerDropRarity.LEGENDARY, type, SlayerSystem.SlayerTier.TIER_V, 0.01)
            };
        };
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Material getMaterial() { return material; }
    public SlayerDropRarity getRarity() { return rarity; }
    public SlayerSystem.SlayerType getType() { return type; }
    public SlayerSystem.SlayerTier getTier() { return tier; }
    public double getBaseChance() { return baseChance; }
    
    /**
     * Slayer Drop Rarity enum
     */
    public enum SlayerDropRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        SlayerDropRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
}
