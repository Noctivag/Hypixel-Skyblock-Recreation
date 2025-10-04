package de.noctivag.skyblock.brewing;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Brewing System for Hypixel Skyblock-style potions
 * Includes custom recipes, brewing stations, and advanced effects
 */
public class AdvancedBrewingSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerBrewingData> playerBrewingData = new ConcurrentHashMap<>();
    private final Map<String, BrewingRecipe> brewingRecipes = new HashMap<>();
    private final Map<String, PotionEffect> potionEffects = new HashMap<>();
    private final Map<UUID, BrewingStation> activeBrewingStations = new ConcurrentHashMap<>();
    
    public AdvancedBrewingSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeBrewingRecipes();
        initializePotionEffects();
        startBrewingTask();
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Initialize all brewing recipes
     */
    private void initializeBrewingRecipes() {
        // Health Potions
        brewingRecipes.put("health_potion_1", new BrewingRecipe(
            "Health Potion I", 
            Arrays.asList(Material.GHAST_TEAR, Material.NETHER_WART), 
            PotionType.HEALING, 
            1, 
            1000
        ));
        
        brewingRecipes.put("health_potion_2", new BrewingRecipe(
            "Health Potion II", 
            Arrays.asList(Material.GHAST_TEAR, Material.GLOWSTONE_DUST), 
            PotionType.HEALING, 
            2, 
            2000
        ));
        
        brewingRecipes.put("health_potion_3", new BrewingRecipe(
            "Health Potion III", 
            Arrays.asList(Material.GHAST_TEAR, Material.GLOWSTONE), 
            PotionType.HEALING, 
            3, 
            5000
        ));
        
        // Mana Potions
        brewingRecipes.put("mana_potion_1", new BrewingRecipe(
            "Mana Potion I", 
            Arrays.asList(Material.LAPIS_LAZULI, Material.NETHER_WART), 
            PotionType.HEALING, 
            1, 
            1000
        ));
        
        brewingRecipes.put("mana_potion_2", new BrewingRecipe(
            "Mana Potion II", 
            Arrays.asList(Material.LAPIS_BLOCK, Material.GLOWSTONE_DUST), 
            PotionType.HEALING, 
            2, 
            2000
        ));
        
        brewingRecipes.put("mana_potion_3", new BrewingRecipe(
            "Mana Potion III", 
            Arrays.asList(Material.LAPIS_BLOCK, Material.GLOWSTONE), 
            PotionType.HEALING, 
            3, 
            5000
        ));
        
        // Speed Potions
        brewingRecipes.put("speed_potion_1", new BrewingRecipe(
            "Speed Potion I", 
            Arrays.asList(Material.SUGAR, Material.NETHER_WART), 
            PotionType.SWIFTNESS, 
            1, 
            1000
        ));
        
        brewingRecipes.put("speed_potion_2", new BrewingRecipe(
            "Speed Potion II", 
            Arrays.asList(Material.SUGAR, Material.REDSTONE), 
            PotionType.SWIFTNESS, 
            2, 
            2000
        ));
        
        brewingRecipes.put("speed_potion_3", new BrewingRecipe(
            "Speed Potion III", 
            Arrays.asList(Material.SUGAR, Material.REDSTONE_BLOCK), 
            PotionType.SWIFTNESS, 
            3, 
            5000
        ));
        
        // Strength Potions
        brewingRecipes.put("strength_potion_1", new BrewingRecipe(
            "Strength Potion I", 
            Arrays.asList(Material.BLAZE_POWDER, Material.NETHER_WART), 
            PotionType.STRENGTH, 
            1, 
            1000
        ));
        
        brewingRecipes.put("strength_potion_2", new BrewingRecipe(
            "Strength Potion II", 
            Arrays.asList(Material.BLAZE_POWDER, Material.REDSTONE), 
            PotionType.STRENGTH, 
            2, 
            2000
        ));
        
        brewingRecipes.put("strength_potion_3", new BrewingRecipe(
            "Strength Potion III", 
            Arrays.asList(Material.BLAZE_POWDER, Material.REDSTONE_BLOCK), 
            PotionType.STRENGTH, 
            3, 
            5000
        ));
        
        // Critical Potions
        brewingRecipes.put("critical_potion_1", new BrewingRecipe(
            "Critical Potion I", 
            Arrays.asList(Material.EMERALD, Material.NETHER_WART), 
            PotionType.HEALING, 
            1, 
            2000
        ));
        
        brewingRecipes.put("critical_potion_2", new BrewingRecipe(
            "Critical Potion II", 
            Arrays.asList(Material.EMERALD, Material.GLOWSTONE_DUST), 
            PotionType.HEALING, 
            2, 
            4000
        ));
        
        brewingRecipes.put("critical_potion_3", new BrewingRecipe(
            "Critical Potion III", 
            Arrays.asList(Material.EMERALD, Material.GLOWSTONE), 
            PotionType.HEALING, 
            3, 
            8000
        ));
        
        // Magic Find Potions
        brewingRecipes.put("magic_find_potion_1", new BrewingRecipe(
            "Magic Find Potion I", 
            Arrays.asList(Material.DIAMOND, Material.NETHER_WART), 
            PotionType.LUCK, 
            1, 
            3000
        ));
        
        brewingRecipes.put("magic_find_potion_2", new BrewingRecipe(
            "Magic Find Potion II", 
            Arrays.asList(Material.DIAMOND, Material.REDSTONE), 
            PotionType.LUCK, 
            2, 
            6000
        ));
        
        brewingRecipes.put("magic_find_potion_3", new BrewingRecipe(
            "Magic Find Potion III", 
            Arrays.asList(Material.DIAMOND, Material.REDSTONE_BLOCK), 
            PotionType.LUCK, 
            3, 
            12000
        ));
        
        // Pet Luck Potions
        brewingRecipes.put("pet_luck_potion_1", new BrewingRecipe(
            "Pet Luck Potion I", 
            Arrays.asList(Material.GOLD_INGOT, Material.NETHER_WART), 
            PotionType.LUCK, 
            1, 
            2000
        ));
        
        brewingRecipes.put("pet_luck_potion_2", new BrewingRecipe(
            "Pet Luck Potion II", 
            Arrays.asList(Material.GOLD_BLOCK, Material.GLOWSTONE_DUST), 
            PotionType.LUCK, 
            2, 
            4000
        ));
        
        brewingRecipes.put("pet_luck_potion_3", new BrewingRecipe(
            "Pet Luck Potion III", 
            Arrays.asList(Material.GOLD_BLOCK, Material.GLOWSTONE), 
            PotionType.LUCK, 
            3, 
            8000
        ));
        
        // Mining Speed Potions
        brewingRecipes.put("mining_speed_potion_1", new BrewingRecipe(
            "Mining Speed Potion I", 
            Arrays.asList(Material.IRON_INGOT, Material.NETHER_WART), 
            PotionType.SWIFTNESS, 
            1, 
            1500
        ));
        
        brewingRecipes.put("mining_speed_potion_2", new BrewingRecipe(
            "Mining Speed Potion II", 
            Arrays.asList(Material.IRON_BLOCK, Material.REDSTONE), 
            PotionType.SWIFTNESS, 
            2, 
            3000
        ));
        
        brewingRecipes.put("mining_speed_potion_3", new BrewingRecipe(
            "Mining Speed Potion III", 
            Arrays.asList(Material.IRON_BLOCK, Material.REDSTONE_BLOCK), 
            PotionType.SWIFTNESS, 
            3, 
            6000
        ));
        
        // Farming Fortune Potions
        brewingRecipes.put("farming_fortune_potion_1", new BrewingRecipe(
            "Farming Fortune Potion I", 
            Arrays.asList(Material.WHEAT, Material.NETHER_WART), 
            PotionType.LUCK, 
            1, 
            1500
        ));
        
        brewingRecipes.put("farming_fortune_potion_2", new BrewingRecipe(
            "Farming Fortune Potion II", 
            Arrays.asList(Material.HAY_BLOCK, Material.GLOWSTONE_DUST), 
            PotionType.LUCK, 
            2, 
            3000
        ));
        
        brewingRecipes.put("farming_fortune_potion_3", new BrewingRecipe(
            "Farming Fortune Potion III", 
            Arrays.asList(Material.HAY_BLOCK, Material.GLOWSTONE), 
            PotionType.LUCK, 
            3, 
            6000
        ));
    }
    
    /**
     * Initialize all potion effects
     */
    private void initializePotionEffects() {
        // Health Effects
        potionEffects.put("health_boost_1", new PotionEffect("Health Boost I", 20, 300, 1.0));
        potionEffects.put("health_boost_2", new PotionEffect("Health Boost II", 40, 300, 2.0));
        potionEffects.put("health_boost_3", new PotionEffect("Health Boost III", 60, 300, 3.0));
        
        // Mana Effects
        potionEffects.put("mana_boost_1", new PotionEffect("Mana Boost I", 50, 300, 1.0));
        potionEffects.put("mana_boost_2", new PotionEffect("Mana Boost II", 100, 300, 2.0));
        potionEffects.put("mana_boost_3", new PotionEffect("Mana Boost III", 150, 300, 3.0));
        
        // Speed Effects
        potionEffects.put("speed_boost_1", new PotionEffect("Speed Boost I", 20, 300, 1.0));
        potionEffects.put("speed_boost_2", new PotionEffect("Speed Boost II", 40, 300, 2.0));
        potionEffects.put("speed_boost_3", new PotionEffect("Speed Boost III", 60, 300, 3.0));
        
        // Strength Effects
        potionEffects.put("strength_boost_1", new PotionEffect("Strength Boost I", 20, 300, 1.0));
        potionEffects.put("strength_boost_2", new PotionEffect("Strength Boost II", 40, 300, 2.0));
        potionEffects.put("strength_boost_3", new PotionEffect("Strength Boost III", 60, 300, 3.0));
        
        // Critical Effects
        potionEffects.put("critical_boost_1", new PotionEffect("Critical Boost I", 10, 300, 1.0));
        potionEffects.put("critical_boost_2", new PotionEffect("Critical Boost II", 20, 300, 2.0));
        potionEffects.put("critical_boost_3", new PotionEffect("Critical Boost III", 30, 300, 3.0));
        
        // Magic Find Effects
        potionEffects.put("magic_find_boost_1", new PotionEffect("Magic Find Boost I", 10, 300, 1.0));
        potionEffects.put("magic_find_boost_2", new PotionEffect("Magic Find Boost II", 20, 300, 2.0));
        potionEffects.put("magic_find_boost_3", new PotionEffect("Magic Find Boost III", 30, 300, 3.0));
        
        // Pet Luck Effects
        potionEffects.put("pet_luck_boost_1", new PotionEffect("Pet Luck Boost I", 10, 300, 1.0));
        potionEffects.put("pet_luck_boost_2", new PotionEffect("Pet Luck Boost II", 20, 300, 2.0));
        potionEffects.put("pet_luck_boost_3", new PotionEffect("Pet Luck Boost III", 30, 300, 3.0));
        
        // Mining Speed Effects
        potionEffects.put("mining_speed_boost_1", new PotionEffect("Mining Speed Boost I", 20, 300, 1.0));
        potionEffects.put("mining_speed_boost_2", new PotionEffect("Mining Speed Boost II", 40, 300, 2.0));
        potionEffects.put("mining_speed_boost_3", new PotionEffect("Mining Speed Boost III", 60, 300, 3.0));
        
        // Farming Fortune Effects
        potionEffects.put("farming_fortune_boost_1", new PotionEffect("Farming Fortune Boost I", 10, 300, 1.0));
        potionEffects.put("farming_fortune_boost_2", new PotionEffect("Farming Fortune Boost II", 20, 300, 2.0));
        potionEffects.put("farming_fortune_boost_3", new PotionEffect("Farming Fortune Boost III", 30, 300, 3.0));
    }
    
    /**
     * Start the brewing task
     */
    private void startBrewingTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateBrewingStations();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active brewing stations
     */
    private void updateBrewingStations() {
        for (BrewingStation station : activeBrewingStations.values()) {
            station.update();
        }
    }
    
    /**
     * Get player brewing data
     */
    public PlayerBrewingData getPlayerBrewingData(UUID playerId) {
        return playerBrewingData.computeIfAbsent(playerId, k -> new PlayerBrewingData());
    }
    
    /**
     * Create a brewing station for a player
     */
    public BrewingStation createBrewingStation(Player player) {
        BrewingStation station = new BrewingStation(player, this);
        activeBrewingStations.put(player.getUniqueId(), station);
        return station;
    }
    
    /**
     * Remove a brewing station
     */
    public void removeBrewingStation(UUID playerId) {
        activeBrewingStations.remove(playerId);
    }
    
    /**
     * Get brewing recipe by ID
     */
    public BrewingRecipe getBrewingRecipe(String recipeId) {
        return brewingRecipes.get(recipeId);
    }
    
    /**
     * Get potion effect by ID
     */
    public PotionEffect getPotionEffect(String effectId) {
        return potionEffects.get(effectId);
    }
    
    /**
     * Get all brewing recipes
     */
    public Map<String, BrewingRecipe> getAllBrewingRecipes() {
        return new HashMap<>(brewingRecipes);
    }
    
    /**
     * Get all potion effects
     */
    public Map<String, PotionEffect> getAllPotionEffects() {
        return new HashMap<>(potionEffects);
    }
    
    /**
     * Check if player can brew a recipe
     */
    public boolean canBrew(Player player, String recipeId) {
        PlayerBrewingData data = getPlayerBrewingData(player.getUniqueId());
        BrewingRecipe recipe = getBrewingRecipe(recipeId);
        
        if (recipe == null) return false;
        
        // Check if player has required ingredients
        for (Material ingredient : recipe.getIngredients()) {
            if (!player.getInventory().contains(ingredient)) {
                return false;
            }
        }
        
        // Check if player has enough coins
        if (data.getCoins() < recipe.getCost()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Brew a potion
     */
    public boolean brewPotion(Player player, String recipeId) {
        if (!canBrew(player, recipeId)) {
            return false;
        }
        
        BrewingRecipe recipe = getBrewingRecipe(recipeId);
        PlayerBrewingData data = getPlayerBrewingData(player.getUniqueId());
        
        // Remove ingredients
        for (Material ingredient : recipe.getIngredients()) {
            player.getInventory().removeItem(new ItemStack(ingredient, 1));
        }
        
        // Remove coins
        data.removeCoins(recipe.getCost());
        
        // Create potion
        ItemStack potion = createPotion(recipe);
        player.getInventory().addItem(potion);
        
        // Update statistics
        data.incrementBrewedPotions();
        data.addExperience(recipe.getCost() / 10);
        
        return true;
    }
    
    /**
     * Create a potion from a recipe
     */
    private ItemStack createPotion(BrewingRecipe recipe) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6" + recipe.getName());
            meta.setLore(Arrays.asList(
                "§7A powerful potion that grants",
                "§7" + recipe.getName() + " effect",
                "",
                "§7Duration: §a" + (recipe.getLevel() * 60) + " seconds",
                "§7Level: §a" + recipe.getLevel(),
                "",
                "§8Brewed with advanced alchemy"
            ));
            
            // Set potion type based on recipe
            PotionData potionData = new PotionData(recipe.getPotionType(), false, false);
            meta.setBasePotionData(potionData);
            
            potion.setItemMeta(meta);
        }
        
        return potion;
    }
    
    /**
     * Get player's brewing level
     */
    public int getBrewingLevel(UUID playerId) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's brewing experience
     */
    public int getBrewingExperience(UUID playerId) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add brewing experience to player
     */
    public void addBrewingExperience(UUID playerId, int experience) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's brewing coins
     */
    public int getBrewingCoins(UUID playerId) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add brewing coins to player
     */
    public void addBrewingCoins(UUID playerId, int coins) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove brewing coins from player
     */
    public void removeBrewingCoins(UUID playerId, int coins) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's brewing statistics
     */
    public Map<String, Integer> getBrewingStatistics(UUID playerId) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("brewed_potions", data.getBrewedPotions());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's brewing data
     */
    public void resetBrewingData(UUID playerId) {
        playerBrewingData.remove(playerId);
    }
    
    /**
     * Save player's brewing data
     */
    public void saveBrewingData(UUID playerId) {
        PlayerBrewingData data = getPlayerBrewingData(playerId);
        // Save to database
        databaseManager.savePlayerBrewingData(playerId, data);
    }
    
    /**
     * Load player's brewing data
     */
    public void loadBrewingData(UUID playerId) {
        databaseManager.loadPlayerBrewingData(playerId).thenAccept(data -> {
            if (data instanceof PlayerBrewingData brewingData) {
                playerBrewingData.put(playerId, brewingData);
            }
        });
    }
    
    /**
     * Get brewing station for player
     */
    public BrewingStation getBrewingStation(UUID playerId) {
        return activeBrewingStations.get(playerId);
    }
    
    /**
     * Get all active brewing stations
     */
    public Map<UUID, BrewingStation> getActiveBrewingStations() {
        return new HashMap<>(activeBrewingStations);
    }
    
    /**
     * Shutdown the brewing system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerBrewingData.keySet()) {
            saveBrewingData(playerId);
        }
        
        // Clear data
        playerBrewingData.clear();
        activeBrewingStations.clear();
    }
}
