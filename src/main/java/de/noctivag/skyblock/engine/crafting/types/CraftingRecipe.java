package de.noctivag.skyblock.engine.crafting.types;
import java.util.UUID;

import java.util.*;

/**
 * Crafting Recipe
 * 
 * Represents a crafting recipe with requirements and crafting logic.
 * Recipes are unlocked through collection progression and have dependencies.
 */
public class CraftingRecipe {
    
    private final String recipeId;
    private final String displayName;
    private final RecipeType type;
    private final List<RecipeRequirement> requirements;
    private final String description;
    private final Map<String, Integer> materials;
    private final Map<String, Integer> products;
    private final int craftingTime; // in seconds
    
    public CraftingRecipe(String recipeId, String displayName, RecipeType type, 
                         List<RecipeRequirement> requirements, String description) {
        this.recipeId = recipeId;
        this.displayName = displayName;
        this.type = type;
        this.requirements = new ArrayList<>(requirements);
        this.description = description;
        this.materials = new HashMap<>();
        this.products = new HashMap<>();
        this.craftingTime = 1; // Default 1 second
    }
    
    public CraftingRecipe(String recipeId, String displayName, RecipeType type, 
                         List<RecipeRequirement> requirements, String description,
                         Map<String, Integer> materials, Map<String, Integer> products, int craftingTime) {
        this.recipeId = recipeId;
        this.displayName = displayName;
        this.type = type;
        this.requirements = new ArrayList<>(requirements);
        this.description = description;
        this.materials = new HashMap<>(materials);
        this.products = new HashMap<>(products);
        this.craftingTime = craftingTime;
    }
    
    public String getRecipeId() {
        return recipeId;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public RecipeType getType() {
        return type;
    }
    
    public List<RecipeRequirement> getRequirements() {
        return new ArrayList<>(requirements);
    }
    
    public String getDescription() {
        return description;
    }
    
    public Map<String, Integer> getMaterials() {
        return new HashMap<>(materials);
    }
    
    public Map<String, Integer> getProducts() {
        return new HashMap<>(products);
    }
    
    public int getCraftingTime() {
        return craftingTime;
    }
    
    /**
     * Check if a player meets all requirements for this recipe
     */
    public boolean meetsRequirements(UUID playerId) {
        for (RecipeRequirement requirement : requirements) {
            if (!requirement.isMet(playerId)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check if a player can craft this recipe (has materials)
     */
    public boolean canCraft(UUID playerId) {
        // TODO: Implement material checking logic
        // This would check if the player has all required materials in their inventory
        return true; // Placeholder
    }
    
    /**
     * Get recipe difficulty based on requirements
     */
    public RecipeDifficulty getDifficulty() {
        int totalRequirements = requirements.size();
        int collectionRequirements = (int) requirements.stream()
            .filter(req -> req.getType() == RecipeRequirement.RequirementType.COLLECTION)
            .count();
        
        if (totalRequirements >= 5 || collectionRequirements >= 3) {
            return RecipeDifficulty.EXPERT;
        } else if (totalRequirements >= 3 || collectionRequirements >= 2) {
            return RecipeDifficulty.ADVANCED;
        } else if (totalRequirements >= 2 || collectionRequirements >= 1) {
            return RecipeDifficulty.INTERMEDIATE;
        } else {
            return RecipeDifficulty.BASIC;
        }
    }
    
    /**
     * Get recipe category
     */
    public RecipeCategory getCategory() {
        return switch (type) {
            case MINION -> RecipeCategory.MINION;
            case ENCHANTED -> RecipeCategory.ENCHANTED;
            case SPECIAL -> RecipeCategory.SPECIAL;
            case TOOL -> RecipeCategory.TOOL;
            case ARMOR -> RecipeCategory.ARMOR;
            case WEAPON -> RecipeCategory.WEAPON;
            case ACCESSORY -> RecipeCategory.ACCESSORY;
            case BLOCK -> RecipeCategory.BLOCK;
            case FOOD -> RecipeCategory.FOOD;
            case POTION -> RecipeCategory.POTION;
            case MISC -> RecipeCategory.MISC;
        };
    }
    
    /**
     * Get recipe rarity based on requirements
     */
    public RecipeRarity getRarity() {
        RecipeDifficulty difficulty = getDifficulty();
        
        return switch (difficulty) {
            case BASIC -> RecipeRarity.COMMON;
            case INTERMEDIATE -> RecipeRarity.UNCOMMON;
            case ADVANCED -> RecipeRarity.RARE;
            case EXPERT -> RecipeRarity.EPIC;
        };
    }
    
    /**
     * Get recipe color based on rarity
     */
    public String getRarityColor() {
        return switch (getRarity()) {
            case COMMON -> "§7"; // Gray
            case UNCOMMON -> "§a"; // Green
            case RARE -> "§9"; // Blue
            case EPIC -> "§5"; // Purple
            case LEGENDARY -> "§6"; // Gold
        };
    }
    
    /**
     * Get formatted recipe name with rarity color
     */
    public String getFormattedName() {
        return getRarityColor() + displayName;
    }
    
    /**
     * Get recipe icon based on type
     */
    public String getIcon() {
        return switch (type) {
            case MINION -> "🤖";
            case ENCHANTED -> "✨";
            case SPECIAL -> "⭐";
            case TOOL -> "🔨";
            case ARMOR -> "🛡️";
            case WEAPON -> "⚔️";
            case ACCESSORY -> "💍";
            case BLOCK -> "🧱";
            case FOOD -> "🍖";
            case POTION -> "🧪";
            case MISC -> "📦";
        };
    }
    
    /**
     * Get formatted recipe with icon and rarity color
     */
    public String getFormattedRecipe() {
        return getIcon() + " " + getFormattedName();
    }
    
    /**
     * Get recipe value (for sorting/prioritizing)
     */
    public int getRecipeValue() {
        return switch (getRarity()) {
            case COMMON -> 10;
            case UNCOMMON -> 25;
            case RARE -> 50;
            case EPIC -> 100;
            case LEGENDARY -> 250;
        };
    }
    
    /**
     * Check if this recipe is a minion recipe
     */
    public boolean isMinionRecipe() {
        return type == RecipeType.MINION;
    }
    
    /**
     * Check if this recipe is an enchanted recipe
     */
    public boolean isEnchantedRecipe() {
        return type == RecipeType.ENCHANTED;
    }
    
    /**
     * Check if this recipe is a special recipe
     */
    public boolean isSpecialRecipe() {
        return type == RecipeType.SPECIAL;
    }
    
    /**
     * Get collection requirements
     */
    public List<RecipeRequirement> getCollectionRequirements() {
        return requirements.stream()
            .filter(req -> req.getType() == RecipeRequirement.RequirementType.COLLECTION)
            .toList();
    }
    
    /**
     * Get skill requirements
     */
    public List<RecipeRequirement> getSkillRequirements() {
        return requirements.stream()
            .filter(req -> req.getType() == RecipeRequirement.RequirementType.SKILL)
            .toList();
    }
    
    /**
     * Get level requirements
     */
    public List<RecipeRequirement> getLevelRequirements() {
        return requirements.stream()
            .filter(req -> req.getType() == RecipeRequirement.RequirementType.LEVEL)
            .toList();
    }
    
    /**
     * Get quest requirements
     */
    public List<RecipeRequirement> getQuestRequirements() {
        return requirements.stream()
            .filter(req -> req.getType() == RecipeRequirement.RequirementType.QUEST)
            .toList();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CraftingRecipe that = (CraftingRecipe) obj;
        return recipeId.equals(that.recipeId);
    }
    
    @Override
    public int hashCode() {
        return recipeId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("CraftingRecipe{recipeId='%s', displayName='%s', type=%s, requirements=%d}", 
            recipeId, displayName, type, requirements.size());
    }
    
    /**
     * Recipe difficulty levels
     */
    public enum RecipeDifficulty {
        BASIC("Basic"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced"),
        EXPERT("Expert");
        
        private final String displayName;
        
        RecipeDifficulty(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Recipe categories
     */
    public enum RecipeCategory {
        MINION("Minion"),
        ENCHANTED("Enchanted"),
        SPECIAL("Special"),
        TOOL("Tool"),
        ARMOR("Armor"),
        WEAPON("Weapon"),
        ACCESSORY("Accessory"),
        BLOCK("Block"),
        FOOD("Food"),
        POTION("Potion"),
        MISC("Misc");
        
        private final String displayName;
        
        RecipeCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Recipe rarity levels
     */
    public enum RecipeRarity {
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare"),
        EPIC("Epic"),
        LEGENDARY("Legendary");
        
        private final String displayName;
        
        RecipeRarity(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
