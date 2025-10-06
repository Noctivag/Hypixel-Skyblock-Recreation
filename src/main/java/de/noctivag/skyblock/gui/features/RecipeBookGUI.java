package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.recipe.RecipeBookSystem;
import de.noctivag.skyblock.recipe.RecipeCategory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RecipeBookGUI extends Menu {
    
    private RecipeBookSystem recipeBookSystem;
    private RecipeCategory currentCategory = RecipeCategory.TOOLS;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 28; // 4 rows of 7 items
    
    public RecipeBookGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lRecipe Book", 54);
        this.recipeBookSystem = plugin.getRecipeBookSystem();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Category buttons (top row)
        setupCategoryButtons();
        
        // Recipe items
        setupRecipeItems();
        
        // Navigation buttons
        setupNavigationButtons();
        
        // Close button
        setCloseButton(49);
    }
    
    private void setupCategoryButtons() {
        RecipeCategory[] categories = RecipeCategory.values();
        int startSlot = 1;
        
        for (int i = 0; i < Math.min(categories.length, 7); i++) {
            RecipeCategory category = categories[i];
            boolean isSelected = category == currentCategory;
            
            setItem(startSlot + i, category.getIcon(), category.getDisplayName(), 
                isSelected ? "special" : "uncommon",
                category.getDescription(),
                "",
                isSelected ? "&aCurrently selected" : "&eClick to view");
        }
    }
    
    private void setupRecipeItems() {
        if (recipeBookSystem == null) {
            player.sendMessage("§cRecipe Book system not available!");
            return;
        }
        
        var recipes = recipeBookSystem.getRecipesByCategory(currentCategory);
        int startSlot = 10; // Start after the category row
        int endSlot = 43; // End before navigation row
        
        // Clear recipe area
        for (int i = startSlot; i <= endSlot; i++) {
            if (i % 9 != 0 && i % 9 != 8) { // Not border slots
                inventory.setItem(i, null);
            }
        }
        
        // Calculate pagination
        int totalPages = (int) Math.ceil((double) recipes.size() / ITEMS_PER_PAGE);
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }
        
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, recipes.size());
        
        int slot = startSlot;
        for (int i = startIndex; i < endIndex; i++) {
            var recipe = recipes.get(i);
            boolean isUnlocked = recipeBookSystem.hasUnlockedRecipe(player, recipe.getId());
            
            String rarity = isUnlocked ? "uncommon" : "common";
            String[] lore = {
                recipe.getDescription(),
                "",
                recipe.getUnlockDescription(),
                "",
                isUnlocked ? "&aUnlocked" : "&cLocked"
            };
            
            setItem(slot, recipe.getResult(), recipe.getName(), rarity, lore);
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip border slots
        }
    }
    
    private void setupNavigationButtons() {
        if (recipeBookSystem == null) return;
        
        var recipes = recipeBookSystem.getRecipesByCategory(currentCategory);
        int totalPages = (int) Math.ceil((double) recipes.size() / ITEMS_PER_PAGE);
        
        // Previous page button
        if (currentPage > 0) {
            setBackButton(45);
        }
        
        // Next page button
        if (currentPage < totalPages - 1) {
            setNextButton(53);
        }
        
        // Page info
        if (totalPages > 1) {
            setItem(49, Material.BOOK, "§ePage " + (currentPage + 1) + "/" + totalPages, "uncommon",
                "&7Showing " + currentCategory.getDisplayName() + " recipes",
                "&7Total recipes: " + recipes.size());
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Category buttons (slots 1-7)
        if (slot >= 1 && slot <= 7) {
            RecipeCategory[] categories = RecipeCategory.values();
            int categoryIndex = slot - 1;
            
            if (categoryIndex < categories.length) {
                currentCategory = categories[categoryIndex];
                currentPage = 0;
                setupItems();
            }
            return;
        }
        
        // Navigation buttons
        if (slot == 45) { // Previous page
            if (currentPage > 0) {
                currentPage--;
                setupItems();
            }
            return;
        }
        
        if (slot == 53) { // Next page
            if (recipeBookSystem != null) {
                var recipes = recipeBookSystem.getRecipesByCategory(currentCategory);
                int totalPages = (int) Math.ceil((double) recipes.size() / ITEMS_PER_PAGE);
                
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    setupItems();
                }
            }
            return;
        }
        
        // Recipe items (slots 10-43)
        if (slot >= 10 && slot <= 43 && slot % 9 != 0 && slot % 9 != 8) {
            if (recipeBookSystem != null) {
                var recipes = recipeBookSystem.getRecipesByCategory(currentCategory);
                int startIndex = currentPage * ITEMS_PER_PAGE;
                int recipeIndex = startIndex + (slot - 10);
                
                if (recipeIndex < recipes.size()) {
                    var recipe = recipes.get(recipeIndex);
                    
                    if (recipeBookSystem.hasUnlockedRecipe(player, recipe.getId())) {
                        // Show recipe details
                        player.sendMessage("§aRecipe: " + recipe.getDisplayName());
                        player.sendMessage("§7Category: " + recipe.getCategory().getDisplayName());
                        player.sendMessage("§7Result: " + recipe.getAmount() + "x " + recipe.getResult().name());
                    } else {
                        player.sendMessage("§cThis recipe is locked!");
                        player.sendMessage("§7" + recipe.getUnlockDescription());
                    }
                }
            }
            return;
        }
        
        // Close button
        if (slot == 49) {
            close();
        }
    }
}
