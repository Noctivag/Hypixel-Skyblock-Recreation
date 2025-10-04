package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Recipe Book System - Hypixel SkyBlock Style
 * 
 * Features:
 * - Recipe discovery system
 * - Recipe categories and subcategories
 * - Crafting requirements and materials
 * - Recipe progress tracking
 * - Recipe book GUI
 */
public class RecipeBookSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerRecipeData> playerRecipeData = new ConcurrentHashMap<>();
    private final Map<String, Recipe> recipes = new HashMap<>();
    private final Map<RecipeCategory, List<Recipe>> recipesByCategory = new HashMap<>();
    
    public RecipeBookSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeRecipes();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeRecipes() {
        // Initialize recipe categories
        for (RecipeCategory category : RecipeCategory.values()) {
            recipesByCategory.put(category, new ArrayList<>());
        }
        
        // Weapon Recipes
        addRecipe("aspect_of_the_end", "Aspect of the End", RecipeCategory.WEAPONS, RecipeSubcategory.SWORDS,
            Arrays.asList(
                new RecipeMaterial(Material.ENDER_PEARL, 8),
                new RecipeMaterial(Material.BLAZE_ROD, 1),
                new RecipeMaterial(Material.EMERALD, 2)
            ),
            "§7A powerful sword that can teleport you forward.",
            RecipeRarity.RARE, 15, "combat_12"
        );
        
        addRecipe("aspect_of_the_dragons", "Aspect of the Dragons", RecipeCategory.WEAPONS, RecipeSubcategory.SWORDS,
            Arrays.asList(
                new RecipeMaterial(Material.DRAGON_EGG, 1),
                new RecipeMaterial(Material.ENDER_PEARL, 24),
                new RecipeMaterial(Material.EMERALD, 8)
            ),
            "§7A legendary sword forged from dragon essence.",
            RecipeRarity.LEGENDARY, 25, "combat_18"
        );
        
        addRecipe("hyperion", "Hyperion", RecipeCategory.WEAPONS, RecipeSubcategory.SWORDS,
            Arrays.asList(
                new RecipeMaterial(Material.NETHER_STAR, 1),
                new RecipeMaterial(Material.DIAMOND_BLOCK, 8),
                new RecipeMaterial(Material.EMERALD_BLOCK, 4)
            ),
            "§7The ultimate weapon of destruction.",
            RecipeRarity.MYTHIC, 35, "combat_25"
        );
        
        // Armor Recipes
        addRecipe("dragon_armor", "Dragon Armor", RecipeCategory.ARMOR, RecipeSubcategory.CHESTPLATES,
            Arrays.asList(
                new RecipeMaterial(Material.DRAGON_EGG, 1),
                new RecipeMaterial(Material.DIAMOND, 24),
                new RecipeMaterial(Material.EMERALD, 8)
            ),
            "§7Armor forged from dragon scales.",
            RecipeRarity.LEGENDARY, 20, "combat_16"
        );
        
        // Tool Recipes
        addRecipe("diamond_pickaxe", "Diamond Pickaxe", RecipeCategory.TOOLS, RecipeSubcategory.PICKAXES,
            Arrays.asList(
                new RecipeMaterial(Material.DIAMOND, 3),
                new RecipeMaterial(Material.STICK, 2)
            ),
            "§7A sturdy diamond pickaxe.",
            RecipeRarity.RARE, 5, "mining_8"
        );
        
        addRecipe("drill", "Drill", RecipeCategory.TOOLS, RecipeSubcategory.DRILLS,
            Arrays.asList(
                new RecipeMaterial(Material.IRON_BLOCK, 4),
                new RecipeMaterial(Material.REDSTONE_BLOCK, 2),
                new RecipeMaterial(Material.GOLD_INGOT, 6)
            ),
            "§7An advanced mining tool.",
            RecipeRarity.EPIC, 12, "mining_15"
        );
        
        // Accessory Recipes
        addRecipe("zombie_ring", "Zombie Ring", RecipeCategory.ACCESSORIES, RecipeSubcategory.RINGS,
            Arrays.asList(
                new RecipeMaterial(Material.ROTTEN_FLESH, 32),
                new RecipeMaterial(Material.IRON_INGOT, 8),
                new RecipeMaterial(Material.REDSTONE, 4)
            ),
            "§7A ring that enhances combat abilities.",
            RecipeRarity.UNCOMMON, 8, "combat_10"
        );
        
        addRecipe("mining_talisman", "Mining Talisman", RecipeCategory.ACCESSORIES, RecipeSubcategory.TALISMANS,
            Arrays.asList(
                new RecipeMaterial(Material.COAL, 16),
                new RecipeMaterial(Material.IRON_INGOT, 4),
                new RecipeMaterial(Material.REDSTONE, 2)
            ),
            "§7A talisman that boosts mining speed.",
            RecipeRarity.COMMON, 3, "mining_5"
        );
        
        // Pet Item Recipes
        addRecipe("pet_candy", "Pet Candy", RecipeCategory.PET_ITEMS, RecipeSubcategory.CANDIES,
            Arrays.asList(
                new RecipeMaterial(Material.SUGAR, 8),
                new RecipeMaterial(Material.WHEAT, 4),
                new RecipeMaterial(Material.MILK_BUCKET, 1)
            ),
            "§7Sweet candy that boosts pet XP.",
            RecipeRarity.COMMON, 2, "taming_3"
        );
        
        // Special Item Recipes
        addRecipe("enchanted_book", "Enchanted Book", RecipeCategory.SPECIAL, RecipeSubcategory.BOOKS,
            Arrays.asList(
                new RecipeMaterial(Material.BOOK, 1),
                new RecipeMaterial(Material.LAPIS_LAZULI, 8),
                new RecipeMaterial(Material.EXPERIENCE_BOTTLE, 1)
            ),
            "§7A book containing magical enchantments.",
            RecipeRarity.RARE, 10, "enchanting_12"
        );
        
        // Minion Recipes
        addRecipe("minion_upgrade", "Minion Upgrade", RecipeCategory.MINIONS, RecipeSubcategory.MINION_UPGRADES,
            Arrays.asList(
                new RecipeMaterial(Material.REDSTONE, 16),
                new RecipeMaterial(Material.IRON_INGOT, 8),
                new RecipeMaterial(Material.GOLD_INGOT, 4)
            ),
            "§7Upgrade your minions to be more efficient.",
            RecipeRarity.UNCOMMON, 6, "mining_8"
        );
    }
    
    private void addRecipe(String id, String name, RecipeCategory category, RecipeSubcategory subcategory,
                          List<RecipeMaterial> materials, String description, RecipeRarity rarity, 
                          int level, String requirement) {
        Recipe recipe = new Recipe(id, name, category, subcategory, materials, description, rarity, level, requirement);
        recipes.put(id, recipe);
        recipesByCategory.get(category).add(recipe);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Recipe Book")) {
            event.setCancelled(true);
            openRecipeBook(player);
        }
    }
    
    public void openRecipeBook(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lRecipe Book");
        
        // Category selection
        int slot = 10;
        for (RecipeCategory category : RecipeCategory.values()) {
            List<Recipe> categoryRecipes = recipesByCategory.get(category);
            int discoveredCount = getDiscoveredRecipeCount(player, categoryRecipes);
            int totalCount = categoryRecipes.size();
            
            ItemStack categoryItem = createCategoryItem(category, discoveredCount, totalCount);
            gui.setItem(slot, categoryItem);
            slot++;
            
            if (slot > 16) break; // Limit to first row
        }
        
        // Quick access to recently discovered recipes
        setupRecentRecipes(gui, player);
        
        // Search and filter options
        setupSearchOptions(gui);
        
        // Navigation
        setupNavigation(gui);
        
        player.openInventory(gui);
    }
    
    public void openCategoryGUI(Player player, RecipeCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lRecipe Book - " + category.getDisplayName());
        
        List<Recipe> categoryRecipes = recipesByCategory.get(category);
        int slot = 0;
        
        for (Recipe recipe : categoryRecipes) {
            if (slot >= 45) break; // Limit to inventory size
            
            PlayerRecipeData playerData = getPlayerRecipeData(player.getUniqueId());
            boolean discovered = playerData.isRecipeDiscovered(recipe.getId());
            
            ItemStack recipeItem = createRecipeItem(recipe, discovered);
            gui.setItem(slot, recipeItem);
            slot++;
        }
        
        // Navigation
        setupCategoryNavigation(gui, category);
        
        player.openInventory(gui);
    }
    
    private ItemStack createCategoryItem(RecipeCategory category, int discovered, int total) {
        ItemStack item = new ItemStack(category.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(category.getDisplayName());
            meta.setLore(Arrays.asList(
                "§7Recipes: §e" + discovered + "§7/§e" + total,
                "§7Progress: §e" + String.format("%.1f", (double) discovered / total * 100) + "%",
                "",
                "§eClick to view recipes"
            ));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private ItemStack createRecipeItem(Recipe recipe, boolean discovered) {
        ItemStack item = new ItemStack(discovered ? recipe.getCategory().getMaterial() : Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            String status = discovered ? "§a✓ DISCOVERED" : "§c✗ LOCKED";
            meta.setDisplayName(recipe.getRarity().getColor() + recipe.getName() + " " + status);
            
            List<String> lore = new ArrayList<>();
            lore.add(recipe.getDescription());
            lore.add("");
            lore.add("§7Category: " + recipe.getCategory().getDisplayName());
            lore.add("§7Rarity: " + recipe.getRarity().getDisplayName());
            lore.add("§7Level Required: §e" + recipe.getLevel());
            lore.add("§7Requirement: §e" + recipe.getRequirement());
            lore.add("");
            
            if (discovered) {
                lore.add("§a✓ Recipe discovered!");
                lore.add("§7Materials needed:");
                for (RecipeMaterial material : recipe.getMaterials()) {
                    lore.add("§7• " + material.getAmount() + "x " + material.getMaterial().name());
                }
                lore.add("");
                lore.add("§eClick to view details");
            } else {
                lore.add("§c✗ Recipe not discovered yet");
                lore.add("§7Complete the requirement to unlock");
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void setupRecentRecipes(Inventory gui, Player player) {
        // Recent recipes section
        ItemStack recentItem = new ItemStack(Material.CLOCK);
        ItemMeta meta = recentItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§e§lRecent Recipes");
            meta.setLore(Arrays.asList(
                "§7View your recently discovered recipes",
                "",
                "§eClick to view"
            ));
            recentItem.setItemMeta(meta);
        }
        
        gui.setItem(22, recentItem);
    }
    
    private void setupSearchOptions(Inventory gui) {
        // Search item
        ItemStack searchItem = new ItemStack(Material.COMPASS);
        ItemMeta meta = searchItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§b§lSearch Recipes");
            meta.setLore(Arrays.asList(
                "§7Search for specific recipes",
                "",
                "§eClick to search"
            ));
            searchItem.setItemMeta(meta);
        }
        
        gui.setItem(23, searchItem);
        
        // Filter options
        ItemStack filterItem = new ItemStack(Material.HOPPER);
        ItemMeta filterMeta = filterItem.getItemMeta();
        
        if (filterMeta != null) {
            filterMeta.setDisplayName("§d§lFilter Options");
            filterMeta.setLore(Arrays.asList(
                "§7Filter recipes by rarity, level, etc.",
                "",
                "§eClick to filter"
            ));
            filterItem.setItemMeta(filterMeta);
        }
        
        gui.setItem(24, filterItem);
    }
    
    private void setupNavigation(Inventory gui) {
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = closeItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§c§lClose");
            meta.setLore(Arrays.asList("§7Close the recipe book"));
            closeItem.setItemMeta(meta);
        }
        
        gui.setItem(49, closeItem);
    }
    
    private void setupCategoryNavigation(Inventory gui, RecipeCategory category) {
        // Back button
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta meta = backItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§7§lBack to Categories");
            meta.setLore(Arrays.asList("§7Return to recipe categories"));
            backItem.setItemMeta(meta);
        }
        
        gui.setItem(45, backItem);
        
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        
        if (closeMeta != null) {
            closeMeta.setDisplayName("§c§lClose");
            closeMeta.setLore(Arrays.asList("§7Close the recipe book"));
            closeItem.setItemMeta(closeMeta);
        }
        
        gui.setItem(49, closeItem);
    }
    
    public void discoverRecipe(Player player, String recipeId) {
        PlayerRecipeData playerData = getPlayerRecipeData(player.getUniqueId());
        Recipe recipe = recipes.get(recipeId);
        
        if (recipe == null) return;
        
        if (!playerData.isRecipeDiscovered(recipeId)) {
            playerData.discoverRecipe(recipeId);
            savePlayerRecipeData(player.getUniqueId(), playerData);
            
            player.sendMessage("§a§lNew Recipe Discovered!");
            player.sendMessage("§7You discovered: " + recipe.getRarity().getColor() + recipe.getName());
            player.sendMessage("§7Check your Recipe Book to view it!");
        }
    }
    
    public boolean canCraftRecipe(Player player, String recipeId) {
        Recipe recipe = recipes.get(recipeId);
        if (recipe == null) return false;
        
        PlayerRecipeData playerData = getPlayerRecipeData(player.getUniqueId());
        return playerData.isRecipeDiscovered(recipeId) && 
               getPlayerLevel(player, recipe.getCategory().getSkillType()) >= recipe.getLevel();
    }
    
    private int getDiscoveredRecipeCount(Player player, List<Recipe> recipes) {
        PlayerRecipeData playerData = getPlayerRecipeData(player.getUniqueId());
        int count = 0;
        
        for (Recipe recipe : recipes) {
            if (playerData.isRecipeDiscovered(recipe.getId())) {
                count++;
            }
        }
        
        return count;
    }
    
    private int getPlayerLevel(Player player, String skillType) {
        // For now, return a default level. This should be integrated with the skills system
        return 1;
    }
    
    public PlayerRecipeData getPlayerRecipeData(UUID playerId) {
        return playerRecipeData.computeIfAbsent(playerId, k -> new PlayerRecipeData(playerId));
    }
    
    private void savePlayerRecipeData(UUID playerId, PlayerRecipeData data) {
        // Save to database
        for (String recipeId : data.getDiscoveredRecipes()) {
            databaseManager.executeUpdate(
                "INSERT INTO player_recipe_data (player_uuid, recipe_id, discovered_at) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE discovered_at = VALUES(discovered_at)",
                playerId.toString(), recipeId, System.currentTimeMillis()
            );
        }
    }
    
    public enum RecipeCategory {
        WEAPONS("§cWeapons", Material.DIAMOND_SWORD, "combat"),
        ARMOR("§bArmor", Material.DIAMOND_CHESTPLATE, "combat"),
        TOOLS("§eTools", Material.DIAMOND_PICKAXE, "mining"),
        ACCESSORIES("§dAccessories", Material.GOLD_INGOT, "combat"),
        PET_ITEMS("§6Pet Items", Material.BONE, "taming"),
        SPECIAL("§fSpecial Items", Material.NETHER_STAR, "enchanting"),
        MINIONS("§9Minions", Material.IRON_BLOCK, "mining");
        
        private final String displayName;
        private final Material material;
        private final String skillType;
        
        RecipeCategory(String displayName, Material material, String skillType) {
            this.displayName = displayName;
            this.material = material;
            this.skillType = skillType;
        }
        
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getSkillType() { return skillType; }
    }
    
    public enum RecipeSubcategory {
        SWORDS, AXES, BOWS, HELMETS, CHESTPLATES, LEGGINGS, BOOTS,
        PICKAXES, SHOVELS, HOES, DRILLS, GAUNTLETS,
        TALISMANS, RINGS, ARTIFACTS, RELICS,
        CANDIES, FOODS, PET_UPGRADES,
        BOOKS, SCROLLS, ORBS,
        MINION_UPGRADES, FUEL, STORAGE
    }
    
    public enum RecipeRarity {
        COMMON("§fCommon", "§f"),
        UNCOMMON("§aUncommon", "§a"),
        RARE("§9Rare", "§9"),
        EPIC("§5Epic", "§5"),
        LEGENDARY("§6Legendary", "§6"),
        MYTHIC("§dMythic", "§d");
        
        private final String displayName;
        private final String color;
        
        RecipeRarity(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }
    
    public static class Recipe {
        private final String id;
        private final String name;
        private final RecipeCategory category;
        private final RecipeSubcategory subcategory;
        private final List<RecipeMaterial> materials;
        private final String description;
        private final RecipeRarity rarity;
        private final int level;
        private final String requirement;
        
        public Recipe(String id, String name, RecipeCategory category, RecipeSubcategory subcategory,
                     List<RecipeMaterial> materials, String description, RecipeRarity rarity, 
                     int level, String requirement) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.subcategory = subcategory;
            this.materials = materials;
            this.description = description;
            this.rarity = rarity;
            this.level = level;
            this.requirement = requirement;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public RecipeCategory getCategory() { return category; }
        public RecipeSubcategory getSubcategory() { return subcategory; }
        public List<RecipeMaterial> getMaterials() { return materials; }
        public String getDescription() { return description; }
        public RecipeRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public String getRequirement() { return requirement; }
    }
    
    public static class RecipeMaterial {
        private final Material material;
        private final int amount;
        
        public RecipeMaterial(Material material, int amount) {
            this.material = material;
            this.amount = amount;
        }
        
        public Material getMaterial() { return material; }
        public int getAmount() { return amount; }
    }
    
    public static class PlayerRecipeData {
        private final UUID playerId;
        private final Set<String> discoveredRecipes;
        private final Map<String, Long> discoveryTimes;
        
        public PlayerRecipeData(UUID playerId) {
            this.playerId = playerId;
            this.discoveredRecipes = new HashSet<>();
            this.discoveryTimes = new HashMap<>();
        }
        
        public void discoverRecipe(String recipeId) {
            discoveredRecipes.add(recipeId);
            discoveryTimes.put(recipeId, System.currentTimeMillis());
        }
        
        public boolean isRecipeDiscovered(String recipeId) {
            return discoveredRecipes.contains(recipeId);
        }
        
        public Set<String> getDiscoveredRecipes() { return discoveredRecipes; }
        public Map<String, Long> getDiscoveryTimes() { return discoveryTimes; }
    }
}
