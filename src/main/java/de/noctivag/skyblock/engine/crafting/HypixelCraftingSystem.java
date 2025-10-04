package de.noctivag.skyblock.engine.crafting;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.crafting.types.CraftingRecipe;
import de.noctivag.skyblock.engine.crafting.types.RecipeType;
import de.noctivag.skyblock.engine.crafting.types.RecipeRequirement;
import de.noctivag.skyblock.engine.collections.types.CollectionType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Crafting System
 * 
 * Implements the crafting and recipe system as specified in the requirements:
 * - Crafting recipes are unlocked through collection progression
 * - Recipe tree system with dependencies
 * - Integration with collections system for recipe unlocks
 * - Support for various recipe types (crafting, minion, special)
 */
public class HypixelCraftingSystem implements Service {
    
    private final Map<UUID, Set<String>> unlockedRecipes = new ConcurrentHashMap<>();
    private final Map<String, CraftingRecipe> recipes = new ConcurrentHashMap<>();
    private final Map<RecipeType, List<CraftingRecipe>> recipesByType = new ConcurrentHashMap<>();
    private final Map<CollectionType, List<CraftingRecipe>> recipesByCollection = new ConcurrentHashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public HypixelCraftingSystem() {
        initializeRecipes();
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all crafting recipes
            initializeRecipes();
            
            // Load player recipe unlocks from database
            loadPlayerRecipeUnlocks();
            
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            
            // Save player recipe unlocks to database
            savePlayerRecipeUnlocks();
            
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "HypixelCraftingSystem";
    }
    
    /**
     * Get unlocked recipes for a player
     */
    public Set<String> getUnlockedRecipes(UUID playerId) {
        return new HashSet<>(unlockedRecipes.getOrDefault(playerId, new HashSet<>()));
    }
    
    /**
     * Check if a player has unlocked a specific recipe
     */
    public boolean hasRecipeUnlocked(UUID playerId, String recipeId) {
        return unlockedRecipes.getOrDefault(playerId, new HashSet<>()).contains(recipeId);
    }
    
    /**
     * Unlock a recipe for a player
     */
    public CompletableFuture<Boolean> unlockRecipe(UUID playerId, String recipeId) {
        return CompletableFuture.supplyAsync(() -> {
            CraftingRecipe recipe = recipes.get(recipeId);
            if (recipe == null) return false;
            
            Set<String> playerRecipes = unlockedRecipes.computeIfAbsent(playerId, k -> new HashSet<>());
            playerRecipes.add(recipeId);
            
            return true;
        });
    }
    
    /**
     * Get recipe by ID
     */
    public CraftingRecipe getRecipe(String recipeId) {
        return recipes.get(recipeId);
    }
    
    /**
     * Get all recipes
     */
    public Map<String, CraftingRecipe> getAllRecipes() {
        return new ConcurrentHashMap<>(recipes);
    }
    
    /**
     * Get recipes by type
     */
    public List<CraftingRecipe> getRecipesByType(RecipeType type) {
        return new ArrayList<>(recipesByType.getOrDefault(type, new ArrayList<>()));
    }
    
    /**
     * Get recipes by collection type
     */
    public List<CraftingRecipe> getRecipesByCollection(CollectionType collectionType) {
        return new ArrayList<>(recipesByCollection.getOrDefault(collectionType, new ArrayList<>()));
    }
    
    /**
     * Get available recipes for a player (unlocked and meet requirements)
     */
    public List<CraftingRecipe> getAvailableRecipes(UUID playerId) {
        List<CraftingRecipe> available = new ArrayList<>();
        
        for (CraftingRecipe recipe : recipes.values()) {
            if (hasRecipeUnlocked(playerId, recipe.getRecipeId()) && 
                recipe.meetsRequirements(playerId)) {
                available.add(recipe);
            }
        }
        
        return available;
    }
    
    /**
     * Get craftable recipes for a player (has materials)
     */
    public List<CraftingRecipe> getCraftableRecipes(UUID playerId) {
        List<CraftingRecipe> craftable = new ArrayList<>();
        
        for (CraftingRecipe recipe : getAvailableRecipes(playerId)) {
            if (recipe.canCraft(playerId)) {
                craftable.add(recipe);
            }
        }
        
        return craftable;
    }
    
    /**
     * Craft an item
     */
    public CompletableFuture<Boolean> craftItem(UUID playerId, String recipeId, int amount) {
        return CompletableFuture.supplyAsync(() -> {
            CraftingRecipe recipe = recipes.get(recipeId);
            if (recipe == null) return false;
            
            if (!hasRecipeUnlocked(playerId, recipeId)) return false;
            if (!recipe.canCraft(playerId)) return false;
            
            // TODO: Implement actual crafting logic
            // This would involve:
            // 1. Checking if player has required materials
            // 2. Removing materials from player inventory
            // 3. Adding crafted items to player inventory
            // 4. Logging the craft for statistics
            
            return true;
        });
    }
    
    /**
     * Get recipe unlock requirements
     */
    public List<RecipeRequirement> getRecipeRequirements(String recipeId) {
        CraftingRecipe recipe = recipes.get(recipeId);
        return recipe != null ? recipe.getRequirements() : new ArrayList<>();
    }
    
    /**
     * Check if a player can unlock a recipe
     */
    public boolean canUnlockRecipe(UUID playerId, String recipeId) {
        CraftingRecipe recipe = recipes.get(recipeId);
        if (recipe == null) return false;
        
        return recipe.meetsRequirements(playerId);
    }
    
    /**
     * Get recipe tree for a player
     */
    public RecipeTree getRecipeTree(UUID playerId) {
        return new RecipeTree(playerId, this);
    }
    
    /**
     * Initialize all crafting recipes
     */
    private void initializeRecipes() {
        // Initialize recipes by type
        for (RecipeType type : RecipeType.values()) {
            recipesByType.put(type, new ArrayList<>());
        }
        
        // Initialize recipes by collection
        for (CollectionType collectionType : CollectionType.values()) {
            recipesByCollection.put(collectionType, new ArrayList<>());
        }
        
        // Create all recipes
        createMiningRecipes();
        createFarmingRecipes();
        createCombatRecipes();
        createForagingRecipes();
        createFishingRecipes();
        createSpecialRecipes();
    }
    
    /**
     * Create mining-related recipes
     */
    private void createMiningRecipes() {
        // Cobblestone recipes
        addRecipe(new CraftingRecipe(
            "cobblestone_minion_1", "Cobblestone Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.COBBLESTONE, 1000)),
            "Basic cobblestone minion"
        ));
        
        addRecipe(new CraftingRecipe(
            "cobblestone_minion_2", "Cobblestone Minion II", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.COBBLESTONE, 5000)),
            "Improved cobblestone minion"
        ));
        
        // Coal recipes
        addRecipe(new CraftingRecipe(
            "coal_minion_1", "Coal Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.COAL, 1000)),
            "Basic coal minion"
        ));
        
        // Iron recipes
        addRecipe(new CraftingRecipe(
            "iron_minion_1", "Iron Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.IRON, 1000)),
            "Basic iron minion"
        ));
        
        // Gold recipes
        addRecipe(new CraftingRecipe(
            "gold_minion_1", "Gold Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.GOLD, 1000)),
            "Basic gold minion"
        ));
        
        // Diamond recipes
        addRecipe(new CraftingRecipe(
            "diamond_minion_1", "Diamond Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.DIAMOND, 1000)),
            "Basic diamond minion"
        ));
        
        // Lapis recipes
        addRecipe(new CraftingRecipe(
            "lapis_minion_1", "Lapis Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.LAPIS, 1000)),
            "Basic lapis minion"
        ));
        
        // Emerald recipes
        addRecipe(new CraftingRecipe(
            "emerald_minion_1", "Emerald Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.EMERALD, 1000)),
            "Basic emerald minion"
        ));
        
        // Redstone recipes
        addRecipe(new CraftingRecipe(
            "redstone_minion_1", "Redstone Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.REDSTONE, 1000)),
            "Basic redstone minion"
        ));
        
        // Quartz recipes
        addRecipe(new CraftingRecipe(
            "quartz_minion_1", "Quartz Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.QUARTZ, 1000)),
            "Basic quartz minion"
        ));
        
        // Obsidian recipes
        addRecipe(new CraftingRecipe(
            "obsidian_minion_1", "Obsidian Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.OBSIDIAN, 1000)),
            "Basic obsidian minion"
        ));
        
        // Glowstone recipes
        addRecipe(new CraftingRecipe(
            "glowstone_minion_1", "Glowstone Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.GLOWSTONE, 1000)),
            "Basic glowstone minion"
        ));
        
        // Gravel recipes
        addRecipe(new CraftingRecipe(
            "gravel_minion_1", "Gravel Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.GRAVEL, 1000)),
            "Basic gravel minion"
        ));
        
        // Ice recipes
        addRecipe(new CraftingRecipe(
            "ice_minion_1", "Ice Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.ICE, 1000)),
            "Basic ice minion"
        ));
        
        // Sand recipes
        addRecipe(new CraftingRecipe(
            "sand_minion_1", "Sand Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SAND, 1000)),
            "Basic sand minion"
        ));
        
        // End Stone recipes
        addRecipe(new CraftingRecipe(
            "end_stone_minion_1", "End Stone Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.END_STONE, 1000)),
            "Basic end stone minion"
        ));
    }
    
    /**
     * Create farming-related recipes
     */
    private void createFarmingRecipes() {
        // Wheat recipes
        addRecipe(new CraftingRecipe(
            "wheat_minion_1", "Wheat Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.WHEAT, 1000)),
            "Basic wheat minion"
        ));
        
        // Carrot recipes
        addRecipe(new CraftingRecipe(
            "carrot_minion_1", "Carrot Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.CARROT, 1000)),
            "Basic carrot minion"
        ));
        
        // Potato recipes
        addRecipe(new CraftingRecipe(
            "potato_minion_1", "Potato Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.POTATO, 1000)),
            "Basic potato minion"
        ));
        
        // Pumpkin recipes
        addRecipe(new CraftingRecipe(
            "pumpkin_minion_1", "Pumpkin Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.PUMPKIN, 1000)),
            "Basic pumpkin minion"
        ));
        
        // Melon recipes
        addRecipe(new CraftingRecipe(
            "melon_minion_1", "Melon Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.MELON, 1000)),
            "Basic melon minion"
        ));
        
        // Mushroom recipes
        addRecipe(new CraftingRecipe(
            "mushroom_minion_1", "Mushroom Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.MUSHROOM, 1000)),
            "Basic mushroom minion"
        ));
        
        // Cactus recipes
        addRecipe(new CraftingRecipe(
            "cactus_minion_1", "Cactus Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.CACTUS, 1000)),
            "Basic cactus minion"
        ));
        
        // Sugar Cane recipes
        addRecipe(new CraftingRecipe(
            "sugar_cane_minion_1", "Sugar Cane Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SUGAR_CANE, 1000)),
            "Basic sugar cane minion"
        ));
        
        // Nether Wart recipes
        addRecipe(new CraftingRecipe(
            "nether_wart_minion_1", "Nether Wart Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.NETHER_WART, 1000)),
            "Basic nether wart minion"
        ));
        
        // Cocoa Beans recipes
        addRecipe(new CraftingRecipe(
            "cocoa_beans_minion_1", "Cocoa Beans Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.COCOA_BEANS, 1000)),
            "Basic cocoa beans minion"
        ));
        
        // Seeds recipes
        addRecipe(new CraftingRecipe(
            "seeds_minion_1", "Seeds Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SEEDS, 1000)),
            "Basic seeds minion"
        ));
    }
    
    /**
     * Create combat-related recipes
     */
    private void createCombatRecipes() {
        // Rotten Flesh recipes
        addRecipe(new CraftingRecipe(
            "zombie_minion_1", "Zombie Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.ROTTEN_FLESH, 1000)),
            "Basic zombie minion"
        ));
        
        // Bone recipes
        addRecipe(new CraftingRecipe(
            "skeleton_minion_1", "Skeleton Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.BONE, 1000)),
            "Basic skeleton minion"
        ));
        
        // String recipes
        addRecipe(new CraftingRecipe(
            "spider_minion_1", "Spider Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.STRING, 1000)),
            "Basic spider minion"
        ));
        
        // Spider Eye recipes
        addRecipe(new CraftingRecipe(
            "cave_spider_minion_1", "Cave Spider Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SPIDER_EYE, 1000)),
            "Basic cave spider minion"
        ));
        
        // Sulphur recipes
        addRecipe(new CraftingRecipe(
            "creeper_minion_1", "Creeper Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SULPHUR, 1000)),
            "Basic creeper minion"
        ));
        
        // Ender Pearl recipes
        addRecipe(new CraftingRecipe(
            "enderman_minion_1", "Enderman Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.ENDER_PEARL, 1000)),
            "Basic enderman minion"
        ));
        
        // Ghast Tear recipes
        addRecipe(new CraftingRecipe(
            "ghast_minion_1", "Ghast Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.GHAST_TEAR, 1000)),
            "Basic ghast minion"
        ));
        
        // Slime Ball recipes
        addRecipe(new CraftingRecipe(
            "slime_minion_1", "Slime Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SLIME_BALL, 1000)),
            "Basic slime minion"
        ));
        
        // Blaze Rod recipes
        addRecipe(new CraftingRecipe(
            "blaze_minion_1", "Blaze Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.BLAZE_ROD, 1000)),
            "Basic blaze minion"
        ));
        
        // Magma Cream recipes
        addRecipe(new CraftingRecipe(
            "magma_cube_minion_1", "Magma Cube Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.MAGMA_CREAM, 1000)),
            "Basic magma cube minion"
        ));
    }
    
    /**
     * Create foraging-related recipes
     */
    private void createForagingRecipes() {
        // Oak Log recipes
        addRecipe(new CraftingRecipe(
            "oak_minion_1", "Oak Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.OAK_LOG, 1000)),
            "Basic oak minion"
        ));
        
        // Spruce Log recipes
        addRecipe(new CraftingRecipe(
            "spruce_minion_1", "Spruce Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SPRUCE_LOG, 1000)),
            "Basic spruce minion"
        ));
        
        // Birch Log recipes
        addRecipe(new CraftingRecipe(
            "birch_minion_1", "Birch Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.BIRCH_LOG, 1000)),
            "Basic birch minion"
        ));
        
        // Dark Oak Log recipes
        addRecipe(new CraftingRecipe(
            "dark_oak_minion_1", "Dark Oak Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.DARK_OAK_LOG, 1000)),
            "Basic dark oak minion"
        ));
        
        // Acacia Log recipes
        addRecipe(new CraftingRecipe(
            "acacia_minion_1", "Acacia Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.ACACIA_LOG, 1000)),
            "Basic acacia minion"
        ));
        
        // Jungle Log recipes
        addRecipe(new CraftingRecipe(
            "jungle_minion_1", "Jungle Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.JUNGLE_LOG, 1000)),
            "Basic jungle minion"
        ));
    }
    
    /**
     * Create fishing-related recipes
     */
    private void createFishingRecipes() {
        // Raw Fish recipes
        addRecipe(new CraftingRecipe(
            "fishing_minion_1", "Fishing Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.RAW_FISH, 1000)),
            "Basic fishing minion"
        ));
        
        // Prismarine Shard recipes
        addRecipe(new CraftingRecipe(
            "prismarine_shard_minion_1", "Prismarine Shard Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.PRISMARINE_SHARD, 1000)),
            "Basic prismarine shard minion"
        ));
        
        // Prismarine Crystals recipes
        addRecipe(new CraftingRecipe(
            "prismarine_crystals_minion_1", "Prismarine Crystals Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.PRISMARINE_CRYSTALS, 1000)),
            "Basic prismarine crystals minion"
        ));
        
        // Clay Ball recipes
        addRecipe(new CraftingRecipe(
            "clay_ball_minion_1", "Clay Ball Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.CLAY_BALL, 1000)),
            "Basic clay ball minion"
        ));
        
        // Lily Pad recipes
        addRecipe(new CraftingRecipe(
            "lily_pad_minion_1", "Lily Pad Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.LILY_PAD, 1000)),
            "Basic lily pad minion"
        ));
        
        // Ink Sack recipes
        addRecipe(new CraftingRecipe(
            "ink_sack_minion_1", "Ink Sack Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.INK_SACK, 1000)),
            "Basic ink sack minion"
        ));
        
        // Sponge recipes
        addRecipe(new CraftingRecipe(
            "sponge_minion_1", "Sponge Minion I", RecipeType.MINION,
            Arrays.asList(new RecipeRequirement(CollectionType.SPONGE, 1000)),
            "Basic sponge minion"
        ));
    }
    
    /**
     * Create special recipes
     */
    private void createSpecialRecipes() {
        // Special crafting recipes that don't fit into other categories
        addRecipe(new CraftingRecipe(
            "enchanted_cobblestone", "Enchanted Cobblestone", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.COBBLESTONE, 10000)),
            "Enchanted cobblestone for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_coal", "Enchanted Coal", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.COAL, 10000)),
            "Enchanted coal for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_iron", "Enchanted Iron", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.IRON, 10000)),
            "Enchanted iron for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_gold", "Enchanted Gold", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.GOLD, 10000)),
            "Enchanted gold for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_diamond", "Enchanted Diamond", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.DIAMOND, 10000)),
            "Enchanted diamond for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_lapis", "Enchanted Lapis", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.LAPIS, 10000)),
            "Enchanted lapis for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_emerald", "Enchanted Emerald", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.EMERALD, 10000)),
            "Enchanted emerald for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_redstone", "Enchanted Redstone", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.REDSTONE, 10000)),
            "Enchanted redstone for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_quartz", "Enchanted Quartz", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.QUARTZ, 10000)),
            "Enchanted quartz for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_obsidian", "Enchanted Obsidian", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.OBSIDIAN, 10000)),
            "Enchanted obsidian for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_glowstone", "Enchanted Glowstone", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.GLOWSTONE, 10000)),
            "Enchanted glowstone for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_gravel", "Enchanted Gravel", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.GRAVEL, 10000)),
            "Enchanted gravel for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_ice", "Enchanted Ice", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.ICE, 10000)),
            "Enchanted ice for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_sand", "Enchanted Sand", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SAND, 10000)),
            "Enchanted sand for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_end_stone", "Enchanted End Stone", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.END_STONE, 10000)),
            "Enchanted end stone for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_wheat", "Enchanted Wheat", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.WHEAT, 10000)),
            "Enchanted wheat for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_carrot", "Enchanted Carrot", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.CARROT, 10000)),
            "Enchanted carrot for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_potato", "Enchanted Potato", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.POTATO, 10000)),
            "Enchanted potato for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_pumpkin", "Enchanted Pumpkin", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.PUMPKIN, 10000)),
            "Enchanted pumpkin for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_melon", "Enchanted Melon", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.MELON, 10000)),
            "Enchanted melon for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_mushroom", "Enchanted Mushroom", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.MUSHROOM, 10000)),
            "Enchanted mushroom for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_cactus", "Enchanted Cactus", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.CACTUS, 10000)),
            "Enchanted cactus for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_sugar_cane", "Enchanted Sugar Cane", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SUGAR_CANE, 10000)),
            "Enchanted sugar cane for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_nether_wart", "Enchanted Nether Wart", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.NETHER_WART, 10000)),
            "Enchanted nether wart for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_cocoa_beans", "Enchanted Cocoa Beans", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.COCOA_BEANS, 10000)),
            "Enchanted cocoa beans for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_seeds", "Enchanted Seeds", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SEEDS, 10000)),
            "Enchanted seeds for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_rotten_flesh", "Enchanted Rotten Flesh", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.ROTTEN_FLESH, 10000)),
            "Enchanted rotten flesh for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_bone", "Enchanted Bone", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.BONE, 10000)),
            "Enchanted bone for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_string", "Enchanted String", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.STRING, 10000)),
            "Enchanted string for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_spider_eye", "Enchanted Spider Eye", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SPIDER_EYE, 10000)),
            "Enchanted spider eye for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_sulphur", "Enchanted Sulphur", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SULPHUR, 10000)),
            "Enchanted sulphur for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_ender_pearl", "Enchanted Ender Pearl", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.ENDER_PEARL, 10000)),
            "Enchanted ender pearl for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_ghast_tear", "Enchanted Ghast Tear", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.GHAST_TEAR, 10000)),
            "Enchanted ghast tear for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_slime_ball", "Enchanted Slime Ball", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SLIME_BALL, 10000)),
            "Enchanted slime ball for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_blaze_rod", "Enchanted Blaze Rod", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.BLAZE_ROD, 10000)),
            "Enchanted blaze rod for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_magma_cream", "Enchanted Magma Cream", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.MAGMA_CREAM, 10000)),
            "Enchanted magma cream for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_oak_log", "Enchanted Oak Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.OAK_LOG, 10000)),
            "Enchanted oak log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_spruce_log", "Enchanted Spruce Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SPRUCE_LOG, 10000)),
            "Enchanted spruce log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_birch_log", "Enchanted Birch Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.BIRCH_LOG, 10000)),
            "Enchanted birch log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_dark_oak_log", "Enchanted Dark Oak Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.DARK_OAK_LOG, 10000)),
            "Enchanted dark oak log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_acacia_log", "Enchanted Acacia Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.ACACIA_LOG, 10000)),
            "Enchanted acacia log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_jungle_log", "Enchanted Jungle Log", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.JUNGLE_LOG, 10000)),
            "Enchanted jungle log for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_raw_fish", "Enchanted Raw Fish", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.RAW_FISH, 10000)),
            "Enchanted raw fish for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_prismarine_shard", "Enchanted Prismarine Shard", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.PRISMARINE_SHARD, 10000)),
            "Enchanted prismarine shard for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_prismarine_crystals", "Enchanted Prismarine Crystals", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.PRISMARINE_CRYSTALS, 10000)),
            "Enchanted prismarine crystals for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_clay_ball", "Enchanted Clay Ball", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.CLAY_BALL, 10000)),
            "Enchanted clay ball for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_lily_pad", "Enchanted Lily Pad", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.LILY_PAD, 10000)),
            "Enchanted lily pad for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_ink_sack", "Enchanted Ink Sack", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.INK_SACK, 10000)),
            "Enchanted ink sack for advanced crafting"
        ));
        
        addRecipe(new CraftingRecipe(
            "enchanted_sponge", "Enchanted Sponge", RecipeType.ENCHANTED,
            Arrays.asList(new RecipeRequirement(CollectionType.SPONGE, 10000)),
            "Enchanted sponge for advanced crafting"
        ));
    }
    
    /**
     * Add a recipe to the system
     */
    private void addRecipe(CraftingRecipe recipe) {
        recipes.put(recipe.getRecipeId(), recipe);
        recipesByType.computeIfAbsent(recipe.getType(), k -> new ArrayList<>()).add(recipe);
        
        // Add to collection-based recipes if it has collection requirements
        for (RecipeRequirement requirement : recipe.getRequirements()) {
            if (requirement.getType() == RecipeRequirement.RequirementType.COLLECTION) {
                recipesByCollection.computeIfAbsent(requirement.getCollectionType(), k -> new ArrayList<>()).add(recipe);
            }
        }
    }
    
    /**
     * Load player recipe unlocks from database
     */
    private void loadPlayerRecipeUnlocks() {
        // TODO: Implement database loading
        // This will integrate with the existing database system
    }
    
    /**
     * Save player recipe unlocks to database
     */
    private void savePlayerRecipeUnlocks() {
        // TODO: Implement database saving
        // This will integrate with the existing database system
    }
}
