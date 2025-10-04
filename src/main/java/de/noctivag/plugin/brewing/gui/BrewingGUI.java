package de.noctivag.plugin.brewing.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.brewing.AdvancedBrewingSystem;
import de.noctivag.plugin.brewing.BrewingRecipe;
import de.noctivag.plugin.brewing.PlayerBrewingData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * GUI for the brewing system
 */
public class BrewingGUI {
    
    private final Plugin plugin;
    private final AdvancedBrewingSystem brewingSystem;
    
    public BrewingGUI(Plugin plugin, AdvancedBrewingSystem brewingSystem) {
        this.plugin = plugin;
        this.brewingSystem = brewingSystem;
    }
    
    /**
     * Open the main brewing GUI
     */
    public void openBrewingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBrewing System");
        
        // Fill borders
        fillBorders(gui);
        
        // Add player info
        addPlayerInfo(gui, player);
        
        // Add brewing recipes
        addBrewingRecipes(gui, player);
        
        // Add navigation items
        addNavigationItems(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Fill the borders of the GUI
     */
    private void fillBorders(Inventory gui) {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(" ");
        border.setItemMeta(borderMeta);
        
        // Top and bottom rows
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, border);
            gui.setItem(45 + i, border);
        }
        
        // Left and right columns
        for (int i = 9; i < 45; i += 9) {
            gui.setItem(i, border);
            gui.setItem(i + 8, border);
        }
    }
    
    /**
     * Add player information
     */
    private void addPlayerInfo(Inventory gui, Player player) {
        PlayerBrewingData data = brewingSystem.getPlayerBrewingData(player.getUniqueId());
        
        // Player head
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = playerHead.getItemMeta();
        headMeta.setDisplayName("§6§l" + player.getName() + "'s Brewing Profile");
        headMeta.setLore(Arrays.asList(
            "§7Level: §a" + data.getLevel(),
            "§7Experience: §a" + data.getExperience() + "§7/§a" + (data.getLevel() + 1) * 1000,
            "§7Coins: §a" + data.getCoins(),
            "§7Brewed Potions: §a" + data.getBrewedPotions(),
            "",
            "§7Experience to Next Level: §a" + data.getExperienceToNextLevel(),
            "§7Progress: §a" + String.format("%.1f", data.getExperienceProgress() * 100) + "%"
        ));
        playerHead.setItemMeta(headMeta);
        gui.setItem(4, playerHead);
    }
    
    /**
     * Add brewing recipes
     */
    private void addBrewingRecipes(Inventory gui, Player player) {
        Map<String, BrewingRecipe> recipes = brewingSystem.getAllBrewingRecipes();
        int slot = 10;
        
        for (Map.Entry<String, BrewingRecipe> entry : recipes.entrySet()) {
            if (slot >= 44) break; // Don't go beyond the border
            
            BrewingRecipe recipe = entry.getValue();
            ItemStack recipeItem = createRecipeItem(recipe, player);
            gui.setItem(slot, recipeItem);
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip border slots
        }
    }
    
    /**
     * Create a recipe item
     */
    private ItemStack createRecipeItem(BrewingRecipe recipe, Player player) {
        ItemStack item = new ItemStack(Material.POTION);
        ItemMeta meta = item.getItemMeta();
        
        boolean canBrew = brewingSystem.canBrew(player, recipe.getName().toLowerCase().replace(" ", "_"));
        
        meta.setDisplayName((canBrew ? "§a" : "§c") + recipe.getName());
        
        List<String> lore = new ArrayList<>();
        lore.add("§7Level: §a" + recipe.getLevel());
        lore.add("§7Cost: §a" + recipe.getCost() + " coins");
        lore.add("");
        lore.add("§7Ingredients:");
        for (Material ingredient : recipe.getIngredients()) {
            lore.add("§7- " + ingredient.name().toLowerCase().replace("_", " "));
        }
        lore.add("");
        
        if (canBrew) {
            lore.add("§aClick to brew this potion!");
        } else {
            lore.add("§cCannot brew this potion");
            lore.add("§cMissing ingredients or coins");
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Add navigation items
     */
    private void addNavigationItems(Inventory gui) {
        // Back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§c§l← Back");
        backMeta.setLore(Arrays.asList("§7Return to the main menu"));
        backButton.setItemMeta(backMeta);
        gui.setItem(49, backButton);
        
        // Brewing station button
        ItemStack stationButton = new ItemStack(Material.BREWING_STAND);
        ItemMeta stationMeta = stationButton.getItemMeta();
        stationMeta.setDisplayName("§6§lBrewing Station");
        stationMeta.setLore(Arrays.asList(
            "§7Open your personal brewing station",
            "§7to brew potions automatically"
        ));
        stationButton.setItemMeta(stationMeta);
        gui.setItem(50, stationButton);
        
        // Statistics button
        ItemStack statsButton = new ItemStack(Material.BOOK);
        ItemMeta statsMeta = statsButton.getItemMeta();
        statsMeta.setDisplayName("§e§lBrewing Statistics");
        statsMeta.setLore(Arrays.asList(
            "§7View your brewing statistics",
            "§7and achievements"
        ));
        statsButton.setItemMeta(statsMeta);
        gui.setItem(51, statsButton);
    }
    
    /**
     * Open the brewing station GUI
     */
    public void openBrewingStationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBrewing Station");
        
        // Fill borders
        fillBorders(gui);
        
        // Add brewing station info
        addBrewingStationInfo(gui, player);
        
        // Add active brews
        addActiveBrews(gui, player);
        
        // Add navigation items
        addStationNavigationItems(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Add brewing station information
     */
    private void addBrewingStationInfo(Inventory gui, Player player) {
        PlayerBrewingData data = brewingSystem.getPlayerBrewingData(player.getUniqueId());
        
        // Station info
        ItemStack stationInfo = new ItemStack(Material.BREWING_STAND);
        ItemMeta infoMeta = stationInfo.getItemMeta();
        infoMeta.setDisplayName("§6§lYour Brewing Station");
        infoMeta.setLore(Arrays.asList(
            "§7Status: §aActive",
            "§7Level: §a" + data.getLevel(),
            "§7Coins: §a" + data.getCoins(),
            "",
            "§7This station can brew potions",
            "§7automatically when you have",
            "§7the required ingredients."
        ));
        stationInfo.setItemMeta(infoMeta);
        gui.setItem(4, stationInfo);
    }
    
    /**
     * Add active brews
     */
    private void addActiveBrews(Inventory gui, Player player) {
        // This would show currently brewing potions
        // For now, we'll add placeholder slots
        
        ItemStack emptySlot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = emptySlot.getItemMeta();
        emptyMeta.setDisplayName("§7Empty Brewing Slot");
        emptyMeta.setLore(Arrays.asList(
            "§7Place ingredients here to",
            "§7start brewing a potion"
        ));
        emptySlot.setItemMeta(emptyMeta);
        
        // Add empty brewing slots
        gui.setItem(19, emptySlot);
        gui.setItem(20, emptySlot);
        gui.setItem(21, emptySlot);
    }
    
    /**
     * Add station navigation items
     */
    private void addStationNavigationItems(Inventory gui) {
        // Back to main brewing GUI
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§c§l← Back to Brewing");
        backMeta.setLore(Arrays.asList("§7Return to the brewing menu"));
        backButton.setItemMeta(backMeta);
        gui.setItem(49, backButton);
        
        // Auto-brew toggle
        ItemStack autoBrewButton = new ItemStack(Material.REDSTONE);
        ItemMeta autoBrewMeta = autoBrewButton.getItemMeta();
        autoBrewMeta.setDisplayName("§6§lAuto-Brew: §cDisabled");
        autoBrewMeta.setLore(Arrays.asList(
            "§7Toggle automatic brewing",
            "§7when ingredients are available"
        ));
        autoBrewButton.setItemMeta(autoBrewMeta);
        gui.setItem(50, autoBrewButton);
    }
    
    /**
     * Open the brewing statistics GUI
     */
    public void openBrewingStatisticsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBrewing Statistics");
        
        // Fill borders
        fillBorders(gui);
        
        // Add statistics
        addBrewingStatistics(gui, player);
        
        // Add navigation items
        addStatsNavigationItems(gui);
        
        player.openInventory(gui);
    }
    
    /**
     * Add brewing statistics
     */
    private void addBrewingStatistics(Inventory gui, Player player) {
        Map<String, Integer> stats = brewingSystem.getBrewingStatistics(player.getUniqueId());
        
        // Level statistic
        ItemStack levelStat = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta levelMeta = levelStat.getItemMeta();
        levelMeta.setDisplayName("§a§lBrewing Level");
        levelMeta.setLore(Arrays.asList(
            "§7Current Level: §a" + stats.get("level"),
            "§7Experience: §a" + stats.get("experience"),
            "§7Total Experience: §a" + stats.get("total_experience")
        ));
        levelStat.setItemMeta(levelMeta);
        gui.setItem(10, levelStat);
        
        // Coins statistic
        ItemStack coinsStat = new ItemStack(Material.GOLD_INGOT);
        ItemMeta coinsMeta = coinsStat.getItemMeta();
        coinsMeta.setDisplayName("§6§lBrewing Coins");
        coinsMeta.setLore(Arrays.asList(
            "§7Current Coins: §a" + stats.get("coins"),
            "§7Use coins to brew potions"
        ));
        coinsStat.setItemMeta(coinsMeta);
        gui.setItem(12, coinsStat);
        
        // Brewed potions statistic
        ItemStack potionsStat = new ItemStack(Material.POTION);
        ItemMeta potionsMeta = potionsStat.getItemMeta();
        potionsMeta.setDisplayName("§d§lBrewed Potions");
        potionsMeta.setLore(Arrays.asList(
            "§7Total Brewed: §a" + stats.get("brewed_potions"),
            "§7Keep brewing to increase this!"
        ));
        potionsStat.setItemMeta(potionsMeta);
        gui.setItem(14, potionsStat);
    }
    
    /**
     * Add statistics navigation items
     */
    private void addStatsNavigationItems(Inventory gui) {
        // Back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§c§l← Back to Brewing");
        backMeta.setLore(Arrays.asList("§7Return to the brewing menu"));
        backButton.setItemMeta(backMeta);
        gui.setItem(49, backButton);
    }
}
