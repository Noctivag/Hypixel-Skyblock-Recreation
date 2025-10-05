package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.models.Recipe;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeBookGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private int currentPage = 0;
    private final int recipesPerPage = 28;

    public RecipeBookGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Recipe Book - Seite " + (currentPage + 1);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Title item
        ItemStack titleItem = new ItemStack(Material.BOOK);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lRecipe Book");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Alle freigeschalteten");
        titleLore.add("§7Crafting-Rezepte!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Get all unlocked recipes
        List<Recipe> unlockedRecipes = profile.getUnlockedRecipes();
        int startIndex = currentPage * recipesPerPage;
        int endIndex = Math.min(startIndex + recipesPerPage, unlockedRecipes.size());

        // Display recipes for current page
        int slot = 20;
        for (int i = startIndex; i < endIndex; i++) {
            if (slot >= 45) break; // Don't go beyond the border
            
            Recipe recipe = unlockedRecipes.get(i);
            ItemStack recipeItem = new ItemStack(recipe.getResult().getType());
            ItemMeta recipeMeta = recipeItem.getItemMeta();
            
            recipeMeta.setDisplayName("§a" + recipe.getName());
            
            List<String> recipeLore = new ArrayList<>();
            recipeLore.add("§7Typ: §e" + recipe.getType().getDisplayName());
            recipeLore.add("§7Benötigte Items:");
            
            // Show required items
            for (Map.Entry<Material, Integer> entry : recipe.getRequiredItems().entrySet()) {
                recipeLore.add("§7• " + entry.getKey().name() + " x" + entry.getValue());
            }
            
            if (recipe.getRequiredCollection() != null) {
                recipeLore.add("");
                recipeLore.add("§7Freigeschaltet durch: §b" + recipe.getRequiredCollection().getDisplayName());
            }
            
            recipeLore.add("");
            recipeLore.add("§eKlicke um zu craften!");
            
            recipeMeta.setLore(recipeLore);
            recipeItem.setItemMeta(recipeMeta);
            inventory.setItem(slot, recipeItem);
            
            slot++;
        }

        // Navigation buttons
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName("§e← Vorherige Seite");
            prevPage.setItemMeta(prevMeta);
            inventory.setItem(45, prevPage);
        }

        if (endIndex < unlockedRecipes.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("§eNächste Seite →");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }

        // Recipe statistics
        ItemStack statsItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta statsMeta = statsItem.getItemMeta();
        statsMeta.setDisplayName("§d§lRecipe Statistiken");
        List<String> statsLore = new ArrayList<>();
        statsLore.add("§7Freigeschaltete Rezepte: §a" + unlockedRecipes.size());
        statsLore.add("§7Gesamt verfügbare Rezepte: §e" + getTotalRecipes());
        statsLore.add("§7Fortschritt: §b" + String.format("%.1f", (double) unlockedRecipes.size() / getTotalRecipes() * 100) + "%");
        
        statsMeta.setLore(statsLore);
        statsItem.setItemMeta(statsMeta);
        inventory.setItem(40, statsItem);

        addNavigationButtons();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle page navigation
        if (event.getSlot() == 45 && clickedItem.getType() == Material.ARROW) {
            // Previous page
            if (currentPage > 0) {
                currentPage--;
                setMenuItems();
            }
            return;
        }
        if (event.getSlot() == 53 && clickedItem.getType() == Material.ARROW) {
            // Next page
            PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
            if (profile != null) {
                List<Recipe> unlockedRecipes = profile.getUnlockedRecipes();
                int maxPages = (int) Math.ceil((double) unlockedRecipes.size() / recipesPerPage);
                if (currentPage < maxPages - 1) {
                    currentPage++;
                    setMenuItems();
                }
            }
            return;
        }
        
        // Handle recipe selection
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handleRecipeClick(clickedPlayer, event.getSlot());
            return;
        }
    }

    private void handleRecipeClick(Player player, int slot) {
        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            return;
        }

        List<Recipe> unlockedRecipes = profile.getUnlockedRecipes();
        int startIndex = currentPage * recipesPerPage;
        int recipeIndex = startIndex + (slot - 20);
        
        if (recipeIndex >= 0 && recipeIndex < unlockedRecipes.size()) {
            Recipe recipe = unlockedRecipes.get(recipeIndex);
            
            // Check if player has required items
            if (hasRequiredItems(player, recipe)) {
                // Craft the item
                craftItem(player, recipe);
            } else {
                player.sendMessage("§cDu hast nicht alle benötigten Items!");
            }
        }
    }

    private boolean hasRequiredItems(Player player, Recipe recipe) {
        for (Map.Entry<Material, Integer> entry : recipe.getRequiredItems().entrySet()) {
            if (player.getInventory().contains(entry.getKey(), entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    private void craftItem(Player player, Recipe recipe) {
        // Remove required items
        for (Map.Entry<Material, Integer> entry : recipe.getRequiredItems().entrySet()) {
            player.getInventory().removeItem(new ItemStack(entry.getKey(), entry.getValue()));
        }
        
        // Give result item
        player.getInventory().addItem(recipe.getResult());
        player.sendMessage("§aDu hast " + recipe.getName() + " gecraftet!");
    }

    private int getTotalRecipes() {
        // This would be loaded from a configuration file in a real implementation
        return 100; // Placeholder
    }
}
