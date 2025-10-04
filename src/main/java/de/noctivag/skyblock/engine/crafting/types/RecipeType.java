package de.noctivag.skyblock.engine.crafting.types;

import java.util.Arrays;

/**
 * Recipe Types in Hypixel Skyblock
 * 
 * Defines the different types of recipes that can be crafted.
 * Each type has different characteristics and requirements.
 */
public enum RecipeType {
    MINION("Minion", "🤖", "Minion recipes for automated resource gathering"),
    ENCHANTED("Enchanted", "✨", "Enchanted item recipes for advanced crafting"),
    SPECIAL("Special", "⭐", "Special recipes with unique requirements"),
    TOOL("Tool", "🔨", "Tool recipes for improved efficiency"),
    ARMOR("Armor", "🛡️", "Armor recipes for protection and bonuses"),
    WEAPON("Weapon", "⚔️", "Weapon recipes for combat effectiveness"),
    ACCESSORY("Accessory", "💍", "Accessory recipes for stat bonuses"),
    BLOCK("Block", "🧱", "Block recipes for construction"),
    FOOD("Food", "🍖", "Food recipes for health and hunger"),
    POTION("Potion", "🧪", "Potion recipes for temporary effects"),
    MISC("Misc", "📦", "Miscellaneous recipes");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    RecipeType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get recipe type by name (case insensitive)
     */
    public static RecipeType getByName(String name) {
        if (name == null) return null;
        
        return Arrays.stream(values())
            .filter(type -> type.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Check if this recipe type is a minion type
     */
    public boolean isMinionType() {
        return this == MINION;
    }
    
    /**
     * Check if this recipe type is an enchanted type
     */
    public boolean isEnchantedType() {
        return this == ENCHANTED;
    }
    
    /**
     * Check if this recipe type is a special type
     */
    public boolean isSpecialType() {
        return this == SPECIAL;
    }
    
    /**
     * Check if this recipe type is a tool type
     */
    public boolean isToolType() {
        return this == TOOL;
    }
    
    /**
     * Check if this recipe type is an armor type
     */
    public boolean isArmorType() {
        return this == ARMOR;
    }
    
    /**
     * Check if this recipe type is a weapon type
     */
    public boolean isWeaponType() {
        return this == WEAPON;
    }
    
    /**
     * Check if this recipe type is an accessory type
     */
    public boolean isAccessoryType() {
        return this == ACCESSORY;
    }
    
    /**
     * Check if this recipe type is a block type
     */
    public boolean isBlockType() {
        return this == BLOCK;
    }
    
    /**
     * Check if this recipe type is a food type
     */
    public boolean isFoodType() {
        return this == FOOD;
    }
    
    /**
     * Check if this recipe type is a potion type
     */
    public boolean isPotionType() {
        return this == POTION;
    }
    
    /**
     * Check if this recipe type is a misc type
     */
    public boolean isMiscType() {
        return this == MISC;
    }
    
    /**
     * Get recipe type weight for recipe power calculation
     */
    public double getWeight() {
        return switch (this) {
            case MINION -> 2.0; // Most valuable
            case ENCHANTED -> 1.8; // Very valuable
            case SPECIAL -> 1.5; // Valuable
            case WEAPON -> 1.3; // Important
            case ARMOR -> 1.2; // Important
            case TOOL -> 1.1; // Moderately important
            case ACCESSORY -> 1.0; // Standard
            case BLOCK -> 0.9; // Less important
            case FOOD -> 0.8; // Less important
            case POTION -> 0.7; // Less important
            case MISC -> 0.5; // Least important
        };
    }
    
    /**
     * Get recipe type difficulty (affects unlock requirements)
     */
    public double getDifficulty() {
        return switch (this) {
            case MINION -> 1.0; // Standard difficulty
            case ENCHANTED -> 1.2; // Higher difficulty
            case SPECIAL -> 1.5; // High difficulty
            case WEAPON -> 1.1; // Slightly higher difficulty
            case ARMOR -> 1.1; // Slightly higher difficulty
            case TOOL -> 1.0; // Standard difficulty
            case ACCESSORY -> 0.9; // Slightly lower difficulty
            case BLOCK -> 0.8; // Lower difficulty
            case FOOD -> 0.7; // Lower difficulty
            case POTION -> 0.8; // Lower difficulty
            case MISC -> 0.6; // Lowest difficulty
        };
    }
    
    /**
     * Get recipe type category
     */
    public RecipeTypeCategory getCategory() {
        return switch (this) {
            case MINION -> RecipeTypeCategory.AUTOMATION;
            case ENCHANTED -> RecipeTypeCategory.ENHANCEMENT;
            case SPECIAL -> RecipeTypeCategory.SPECIAL;
            case TOOL, ARMOR, WEAPON, ACCESSORY -> RecipeTypeCategory.EQUIPMENT;
            case BLOCK -> RecipeTypeCategory.CONSTRUCTION;
            case FOOD, POTION -> RecipeTypeCategory.CONSUMABLE;
            case MISC -> RecipeTypeCategory.MISC;
        };
    }
    
    /**
     * Get recipes by category
     */
    public static RecipeType[] getByCategory(RecipeTypeCategory category) {
        return Arrays.stream(values())
            .filter(type -> type.getCategory() == category)
            .toArray(RecipeType[]::new);
    }
    
    /**
     * Get all automation recipe types
     */
    public static RecipeType[] getAutomationTypes() {
        return getByCategory(RecipeTypeCategory.AUTOMATION);
    }
    
    /**
     * Get all enhancement recipe types
     */
    public static RecipeType[] getEnhancementTypes() {
        return getByCategory(RecipeTypeCategory.ENHANCEMENT);
    }
    
    /**
     * Get all special recipe types
     */
    public static RecipeType[] getSpecialTypes() {
        return getByCategory(RecipeTypeCategory.SPECIAL);
    }
    
    /**
     * Get all equipment recipe types
     */
    public static RecipeType[] getEquipmentTypes() {
        return getByCategory(RecipeTypeCategory.EQUIPMENT);
    }
    
    /**
     * Get all construction recipe types
     */
    public static RecipeType[] getConstructionTypes() {
        return getByCategory(RecipeTypeCategory.CONSTRUCTION);
    }
    
    /**
     * Get all consumable recipe types
     */
    public static RecipeType[] getConsumableTypes() {
        return getByCategory(RecipeTypeCategory.CONSUMABLE);
    }
    
    /**
     * Get all misc recipe types
     */
    public static RecipeType[] getMiscTypes() {
        return getByCategory(RecipeTypeCategory.MISC);
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
    
    /**
     * Recipe type categories for organization
     */
    public enum RecipeTypeCategory {
        AUTOMATION("Automation"),
        ENHANCEMENT("Enhancement"),
        SPECIAL("Special"),
        EQUIPMENT("Equipment"),
        CONSTRUCTION("Construction"),
        CONSUMABLE("Consumable"),
        MISC("Misc");
        
        private final String displayName;
        
        RecipeTypeCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
