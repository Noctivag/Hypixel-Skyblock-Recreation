package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.travel.FastTravelSystem;
import de.noctivag.skyblock.travel.TravelCategory;
import de.noctivag.skyblock.travel.TravelLocation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.stream.Collectors;

public class FastTravelGUI extends Menu {
    
    private FastTravelSystem fastTravelSystem;
    private TravelCategory currentCategory = null; // null = all locations
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 28; // 4 rows of 7 items
    
    public FastTravelGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lFast Travel", 54);
        this.fastTravelSystem = plugin.getFastTravelSystem();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Travel info
        setupTravelInfo();
        
        // Category buttons
        setupCategoryButtons();
        
        // Location items
        setupLocationItems();
        
        // Navigation buttons
        setupNavigationButtons();
        
        // Close button
        setCloseButton(49);
    }
    
    private void setupTravelInfo() {
        if (fastTravelSystem != null) {
            boolean isOnCooldown = fastTravelSystem.isOnCooldown(player);
            long remainingCooldown = fastTravelSystem.getRemainingCooldown(player);
            
            String[] lore = {
                "&7Quickly travel between",
                "&7different locations",
                "",
                isOnCooldown ? "&cCooldown: " + (remainingCooldown / 1000) + "s" : "&aReady to travel"
            };
            
            setItem(4, Material.ENDER_PEARL, "§eFast Travel", "special", lore);
        }
    }
    
    private void setupCategoryButtons() {
        TravelCategory[] categories = TravelCategory.values();
        int startSlot = 1;
        
        // All locations button
        setItem(startSlot, Material.BOOK, "All Locations", 
            currentCategory == null ? "special" : "uncommon",
            "&7Show all travel locations",
            "",
            currentCategory == null ? "&aCurrently selected" : "&eClick to view");
        
        // Category buttons
        for (int i = 0; i < Math.min(categories.length, 6); i++) {
            TravelCategory category = categories[i];
            boolean isSelected = category == currentCategory;
            
            setItem(startSlot + 1 + i, category.getIcon(), category.getDisplayName(), 
                isSelected ? "special" : "uncommon",
                category.getDescription(),
                "",
                isSelected ? "&aCurrently selected" : "&eClick to filter");
        }
    }
    
    private void setupLocationItems() {
        if (fastTravelSystem == null) {
            player.sendMessage("§cFast Travel system not available!");
            return;
        }
        
        List<TravelLocation> locations = getFilteredLocations();
        int startSlot = 10; // Start after the category row
        int endSlot = 43; // End before navigation row
        
        // Clear location area
        for (int i = startSlot; i <= endSlot; i++) {
            if (i % 9 != 0 && i % 9 != 8) { // Not border slots
                inventory.setItem(i, null);
            }
        }
        
        // Calculate pagination
        int totalPages = (int) Math.ceil((double) locations.size() / ITEMS_PER_PAGE);
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }
        
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, locations.size());
        
        int slot = startSlot;
        for (int i = startIndex; i < endIndex; i++) {
            TravelLocation location = locations.get(i);
            boolean isUnlocked = fastTravelSystem.hasUnlockedLocation(player, location.getId());
            boolean isOnCooldown = fastTravelSystem.isOnCooldown(player);
            
            String rarity = isUnlocked ? "uncommon" : "common";
            String[] lore = {
                location.getDescription(),
                "",
                "&7Category: " + location.getCategory().getDisplayName(),
                "&7Cost: " + location.getFormattedCost(),
                "",
                isUnlocked ? "&aUnlocked" : "&cLocked",
                isOnCooldown ? "&cOn cooldown" : "&eClick to travel"
            };
            
            setItem(slot, location.getCategory().getIcon(), location.getName(), rarity, lore);
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip border slots
        }
    }
    
    private void setupNavigationButtons() {
        if (fastTravelSystem == null) return;
        
        List<TravelLocation> locations = getFilteredLocations();
        int totalPages = (int) Math.ceil((double) locations.size() / ITEMS_PER_PAGE);
        
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
                "&7Showing " + (currentCategory != null ? currentCategory.getDisplayName() : "All") + " locations",
                "&7Total locations: " + locations.size());
        }
    }
    
    private List<TravelLocation> getFilteredLocations() {
        if (fastTravelSystem == null) return List.of();
        
        if (currentCategory == null) {
            return fastTravelSystem.getUnlockedLocations(player);
        } else {
            return fastTravelSystem.getLocationsByCategory(currentCategory).stream()
                    .filter(location -> fastTravelSystem.hasUnlockedLocation(player, location.getId()))
                    .collect(Collectors.toList());
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Category buttons (slots 1-7)
        if (slot >= 1 && slot <= 7) {
            if (slot == 1) {
                currentCategory = null; // All locations
            } else {
                TravelCategory[] categories = TravelCategory.values();
                int categoryIndex = slot - 2;
                
                if (categoryIndex < categories.length) {
                    currentCategory = categories[categoryIndex];
                }
            }
            currentPage = 0;
            setupItems();
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
            List<TravelLocation> locations = getFilteredLocations();
            int totalPages = (int) Math.ceil((double) locations.size() / ITEMS_PER_PAGE);
            
            if (currentPage < totalPages - 1) {
                currentPage++;
                setupItems();
            }
            return;
        }
        
        // Location items (slots 10-43)
        if (slot >= 10 && slot <= 43 && slot % 9 != 0 && slot % 9 != 8) {
            if (fastTravelSystem != null) {
                List<TravelLocation> locations = getFilteredLocations();
                int startIndex = currentPage * ITEMS_PER_PAGE;
                int locationIndex = startIndex + (slot - 10);
                
                if (locationIndex < locations.size()) {
                    TravelLocation location = locations.get(locationIndex);
                    
                    if (event.isRightClick()) {
                        // Show location details
                        player.sendMessage("§aLocation: " + location.getDisplayName());
                        player.sendMessage("§7Category: " + location.getCategory().getDisplayName());
                        player.sendMessage("§7Description: " + location.getDescription());
                        player.sendMessage("§7Cost: " + location.getFormattedCost());
                        player.sendMessage("§7Status: " + (fastTravelSystem.hasUnlockedLocation(player, location.getId()) ? "&aUnlocked" : "&cLocked"));
                    } else {
                        // Travel to location
                        boolean success = fastTravelSystem.travelToLocation(player, location.getId());
                        if (success) {
                            close(); // Close the GUI after successful travel
                        }
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
