package de.noctivag.skyblock.recipe;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Recipe Book System - Manages all crafting recipes and player recipe unlocks
 */
public class RecipeBookSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Recipe storage
    private final Map<String, SkyblockRecipe> recipes = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> playerUnlockedRecipes = new ConcurrentHashMap<>();
    
    // Recipe categories
    private final Map<RecipeCategory, List<String>> categoryRecipes = new HashMap<>();
    
    public RecipeBookSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing RecipeBookSystem...");
        
        try {
            // Initialize recipe categories
            initializeCategories();
            
            // Load all recipes
            loadRecipes();
            
            // Load player data
            loadPlayerData();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("RecipeBookSystem initialized with " + recipes.size() + " recipes.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize RecipeBookSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down RecipeBookSystem...");
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("RecipeBookSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "RecipeBookSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Initialize recipe categories
     */
    private void initializeCategories() {
        for (RecipeCategory category : RecipeCategory.values()) {
            categoryRecipes.put(category, new ArrayList<>());
        }
    }
    
    /**
     * Load all recipes
     */
    private void loadRecipes() {
        // Basic Tools
        addRecipe(new SkyblockRecipe("wooden_pickaxe", "Wooden Pickaxe", RecipeCategory.TOOLS, 
            Material.WOODEN_PICKAXE, 1, Arrays.asList(
                "WWW",
                " S ",
                " S "
            ), Map.of('W', Material.OAK_PLANKS, 'S', Material.STICK)));
        
        addRecipe(new SkyblockRecipe("stone_pickaxe", "Stone Pickaxe", RecipeCategory.TOOLS,
            Material.STONE_PICKAXE, 1, Arrays.asList(
                "SSS",
                " S ",
                " S "
            ), Map.of('S', Material.COBBLESTONE)));
        
        addRecipe(new SkyblockRecipe("iron_pickaxe", "Iron Pickaxe", RecipeCategory.TOOLS,
            Material.IRON_PICKAXE, 1, Arrays.asList(
                "III",
                " S ",
                " S "
            ), Map.of('I', Material.IRON_INGOT)));
        
        addRecipe(new SkyblockRecipe("diamond_pickaxe", "Diamond Pickaxe", RecipeCategory.TOOLS,
            Material.DIAMOND_PICKAXE, 1, Arrays.asList(
                "DDD",
                " S ",
                " S "
            ), Map.of('D', Material.DIAMOND)));
        
        // Basic Weapons
        addRecipe(new SkyblockRecipe("wooden_sword", "Wooden Sword", RecipeCategory.WEAPONS,
            Material.WOODEN_SWORD, 1, Arrays.asList(
                " W ",
                " W ",
                " S "
            ), Map.of('W', Material.OAK_PLANKS, 'S', Material.STICK)));
        
        addRecipe(new SkyblockRecipe("stone_sword", "Stone Sword", RecipeCategory.WEAPONS,
            Material.STONE_SWORD, 1, Arrays.asList(
                " S ",
                " S ",
                " S "
            ), Map.of('S', Material.COBBLESTONE)));
        
        addRecipe(new SkyblockRecipe("iron_sword", "Iron Sword", RecipeCategory.WEAPONS,
            Material.IRON_SWORD, 1, Arrays.asList(
                " I ",
                " I ",
                " S "
            ), Map.of('I', Material.IRON_INGOT)));
        
        addRecipe(new SkyblockRecipe("diamond_sword", "Diamond Sword", RecipeCategory.WEAPONS,
            Material.DIAMOND_SWORD, 1, Arrays.asList(
                " D ",
                " D ",
                " S "
            ), Map.of('D', Material.DIAMOND)));
        
        // Basic Armor
        addRecipe(new SkyblockRecipe("leather_helmet", "Leather Helmet", RecipeCategory.ARMOR,
            Material.LEATHER_HELMET, 1, Arrays.asList(
                "LLL",
                "L L",
                "   "
            ), Map.of('L', Material.LEATHER)));
        
        addRecipe(new SkyblockRecipe("leather_chestplate", "Leather Chestplate", RecipeCategory.ARMOR,
            Material.LEATHER_CHESTPLATE, 1, Arrays.asList(
                "L L",
                "LLL",
                "LLL"
            ), Map.of('L', Material.LEATHER)));
        
        addRecipe(new SkyblockRecipe("leather_leggings", "Leather Leggings", RecipeCategory.ARMOR,
            Material.LEATHER_LEGGINGS, 1, Arrays.asList(
                "LLL",
                "L L",
                "L L"
            ), Map.of('L', Material.LEATHER)));
        
        addRecipe(new SkyblockRecipe("leather_boots", "Leather Boots", RecipeCategory.ARMOR,
            Material.LEATHER_BOOTS, 1, Arrays.asList(
                "   ",
                "L L",
                "L L"
            ), Map.of('L', Material.LEATHER)));
        
        // Iron Armor
        addRecipe(new SkyblockRecipe("iron_helmet", "Iron Helmet", RecipeCategory.ARMOR,
            Material.IRON_HELMET, 1, Arrays.asList(
                "III",
                "I I",
                "   "
            ), Map.of('I', Material.IRON_INGOT)));
        
        addRecipe(new SkyblockRecipe("iron_chestplate", "Iron Chestplate", RecipeCategory.ARMOR,
            Material.IRON_CHESTPLATE, 1, Arrays.asList(
                "I I",
                "III",
                "III"
            ), Map.of('I', Material.IRON_INGOT)));
        
        addRecipe(new SkyblockRecipe("iron_leggings", "Iron Leggings", RecipeCategory.ARMOR,
            Material.IRON_LEGGINGS, 1, Arrays.asList(
                "III",
                "I I",
                "I I"
            ), Map.of('I', Material.IRON_INGOT)));
        
        addRecipe(new SkyblockRecipe("iron_boots", "Iron Boots", RecipeCategory.ARMOR,
            Material.IRON_BOOTS, 1, Arrays.asList(
                "   ",
                "I I",
                "I I"
            ), Map.of('I', Material.IRON_INGOT)));
        
        // Diamond Armor
        addRecipe(new SkyblockRecipe("diamond_helmet", "Diamond Helmet", RecipeCategory.ARMOR,
            Material.DIAMOND_HELMET, 1, Arrays.asList(
                "DDD",
                "D D",
                "   "
            ), Map.of('D', Material.DIAMOND)));
        
        addRecipe(new SkyblockRecipe("diamond_chestplate", "Diamond Chestplate", RecipeCategory.ARMOR,
            Material.DIAMOND_CHESTPLATE, 1, Arrays.asList(
                "D D",
                "DDD",
                "DDD"
            ), Map.of('D', Material.DIAMOND)));
        
        addRecipe(new SkyblockRecipe("diamond_leggings", "Diamond Leggings", RecipeCategory.ARMOR,
            Material.DIAMOND_LEGGINGS, 1, Arrays.asList(
                "DDD",
                "D D",
                "D D"
            ), Map.of('D', Material.DIAMOND)));
        
        addRecipe(new SkyblockRecipe("diamond_boots", "Diamond Boots", RecipeCategory.ARMOR,
            Material.DIAMOND_BOOTS, 1, Arrays.asList(
                "   ",
                "D D",
                "D D"
            ), Map.of('D', Material.DIAMOND)));
        
        // Basic Blocks
        addRecipe(new SkyblockRecipe("crafting_table", "Crafting Table", RecipeCategory.BLOCKS,
            Material.CRAFTING_TABLE, 1, Arrays.asList(
                "WW",
                "WW"
            ), Map.of('W', Material.OAK_PLANKS)));
        
        addRecipe(new SkyblockRecipe("furnace", "Furnace", RecipeCategory.BLOCKS,
            Material.FURNACE, 1, Arrays.asList(
                "CCC",
                "C C",
                "CCC"
            ), Map.of('C', Material.COBBLESTONE)));
        
        addRecipe(new SkyblockRecipe("chest", "Chest", RecipeCategory.BLOCKS,
            Material.CHEST, 1, Arrays.asList(
                "WWW",
                "W W",
                "WWW"
            ), Map.of('W', Material.OAK_PLANKS)));
        
        // Special Items
        addRecipe(new SkyblockRecipe("enchanting_table", "Enchanting Table", RecipeCategory.SPECIAL,
            Material.ENCHANTING_TABLE, 1, Arrays.asList(
                " B ",
                "DOD",
                "CCC"
            ), Map.of('B', Material.BOOK, 'D', Material.DIAMOND, 'O', Material.OBSIDIAN, 'C', Material.BOOKSHELF)));
        
        addRecipe(new SkyblockRecipe("anvil", "Anvil", RecipeCategory.SPECIAL,
            Material.ANVIL, 1, Arrays.asList(
                "III",
                " I ",
                "III"
            ), Map.of('I', Material.IRON_BLOCK)));
        
        addRecipe(new SkyblockRecipe("brewing_stand", "Brewing Stand", RecipeCategory.SPECIAL,
            Material.BREWING_STAND, 1, Arrays.asList(
                " B ",
                "CCC"
            ), Map.of('B', Material.BLAZE_ROD, 'C', Material.COBBLESTONE)));
        
        plugin.getLogger().info("Loaded " + recipes.size() + " recipes across " + RecipeCategory.values().length + " categories.");
    }
    
    /**
     * Add a recipe to the system
     */
    private void addRecipe(SkyblockRecipe recipe) {
        recipes.put(recipe.getId(), recipe);
        categoryRecipes.get(recipe.getCategory()).add(recipe.getId());
    }
    
    /**
     * Load player data
     */
    private void loadPlayerData() {
        // This would load from database in a real implementation
        // For now, we'll initialize empty sets
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // This would save to database in a real implementation
    }
    
    /**
     * Get all recipes in a category
     */
    public List<SkyblockRecipe> getRecipesByCategory(RecipeCategory category) {
        List<String> recipeIds = categoryRecipes.get(category);
        List<SkyblockRecipe> categoryRecipes = new ArrayList<>();
        
        for (String recipeId : recipeIds) {
            SkyblockRecipe recipe = recipes.get(recipeId);
            if (recipe != null) {
                categoryRecipes.add(recipe);
            }
        }
        
        return categoryRecipes;
    }
    
    /**
     * Get all recipes
     */
    public Collection<SkyblockRecipe> getAllRecipes() {
        return recipes.values();
    }
    
    /**
     * Get a recipe by ID
     */
    public SkyblockRecipe getRecipe(String id) {
        return recipes.get(id);
    }
    
    /**
     * Check if a player has unlocked a recipe
     */
    public boolean hasUnlockedRecipe(Player player, String recipeId) {
        Set<String> unlockedRecipes = playerUnlockedRecipes.get(player.getUniqueId());
        return unlockedRecipes != null && unlockedRecipes.contains(recipeId);
    }
    
    /**
     * Unlock a recipe for a player
     */
    public void unlockRecipe(Player player, String recipeId) {
        playerUnlockedRecipes.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(recipeId);
        player.sendMessage("§aRecipe unlocked: §f" + recipes.get(recipeId).getName());
    }
    
    /**
     * Get unlocked recipes for a player
     */
    public Set<String> getUnlockedRecipes(Player player) {
        return playerUnlockedRecipes.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
    
    /**
     * Get all recipe categories
     */
    public RecipeCategory[] getCategories() {
        return RecipeCategory.values();
    }
}
