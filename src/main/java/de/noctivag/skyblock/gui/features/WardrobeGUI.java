package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.wardrobe.ArmorSet;
import de.noctivag.skyblock.wardrobe.WardrobeSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;
import java.util.Set;

public class WardrobeGUI extends Menu {
    
    private WardrobeSystem wardrobeSystem;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 28; // 4 rows of 7 items
    
    public WardrobeGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lWardrobe", 54);
        this.wardrobeSystem = plugin.getWardrobeSystem();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Wardrobe info
        setupWardrobeInfo();
        
        // Armor sets
        setupArmorSets();
        
        // Action buttons
        setupActionButtons();
        
        // Navigation buttons
        setupNavigationButtons();
        
        // Close button
        setCloseButton(49);
    }
    
    private void setupWardrobeInfo() {
        if (wardrobeSystem != null) {
            int totalSets = wardrobeSystem.getArmorSetCount(player);
            int maxSets = wardrobeSystem.getMaxArmorSets(player);
            String activeSet = wardrobeSystem.getActiveArmorSet(player);
            
            setItem(4, Material.LEATHER_CHESTPLATE, "§eWardrobe Info", "special",
                "&7Total Sets: &f" + totalSets + "/" + maxSets,
                "&7Active Set: &f" + (activeSet != null ? activeSet : "None"),
                "",
                "&7Store and quickly switch between",
                "&7different armor configurations.");
        }
    }
    
    private void setupArmorSets() {
        if (wardrobeSystem == null) {
            player.sendMessage("§cWardrobe system not available!");
            return;
        }
        
        Map<String, ArmorSet> wardrobe = wardrobeSystem.getPlayerWardrobe(player);
        Set<String> setNames = wardrobe.keySet();
        
        // Calculate pagination
        int totalPages = (int) Math.ceil((double) setNames.size() / ITEMS_PER_PAGE);
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }
        
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, setNames.size());
        
        int slot = 10;
        String[] setNamesArray = setNames.toArray(new String[0]);
        
        for (int i = startIndex; i < endIndex; i++) {
            String setName = setNamesArray[i];
            ArmorSet armorSet = wardrobe.get(setName);
            String activeSet = wardrobeSystem.getActiveArmorSet(player);
            boolean isActive = setName.equals(activeSet);
            
            String rarity = isActive ? "mythic" : armorSet.getRarity();
            String[] lore = {
                armorSet.getDescription(),
                "",
                "&7Defense: &f" + String.format("%.1f", armorSet.getTotalDefense()),
                "&7Pieces: &f" + armorSet.getArmorCount() + "/4",
                "&7Rarity: &f" + armorSet.getRarity().toUpperCase(),
                "",
                isActive ? "&aCurrently Equipped" : "&eClick to equip",
                "&7Right-click for options"
            };
            
            setItem(slot, Material.LEATHER_CHESTPLATE, armorSet.getName(), rarity, lore);
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip border slots
        }
    }
    
    private void setupActionButtons() {
        // Save current armor button
        setItem(45, Material.ANVIL, "§aSave Current Armor", "uncommon",
            "&7Save your currently equipped",
            "&7armor as a new set",
            "",
            "&eClick to save");
        
        // Create new set button
        setItem(46, Material.CRAFTING_TABLE, "§aCreate New Set", "uncommon",
            "&7Create a new armor set",
            "&7from your inventory",
            "",
            "&eClick to create");
        
        // Manage sets button
        setItem(47, Material.WRITABLE_BOOK, "§aManage Sets", "uncommon",
            "&7Delete or rename",
            "&7existing armor sets",
            "",
            "&eClick to manage");
    }
    
    private void setupNavigationButtons() {
        if (wardrobeSystem == null) return;
        
        Map<String, ArmorSet> wardrobe = wardrobeSystem.getPlayerWardrobe(player);
        int totalPages = (int) Math.ceil((double) wardrobe.size() / ITEMS_PER_PAGE);
        
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
                "&7Showing armor sets",
                "&7Total sets: " + wardrobe.size());
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Action buttons
        if (slot == 45) { // Save current armor
            if (wardrobeSystem != null) {
                // For now, save with a default name
                String setName = "set_" + System.currentTimeMillis();
                boolean success = wardrobeSystem.saveCurrentArmorAsSet(player, setName, "Custom Set", "Custom armor set");
                if (success) {
                    setupItems(); // Refresh the GUI
                }
            }
            return;
        }
        
        if (slot == 46) { // Create new set
            player.sendMessage("§aCreating new armor set...");
            player.sendMessage("§7Please equip the armor you want to save, then use the save button.");
            return;
        }
        
        if (slot == 47) { // Manage sets
            player.sendMessage("§aSet management - Coming Soon!");
            return;
        }
        
        // Navigation buttons
        if (slot == 45 && currentPage > 0) { // Previous page
            currentPage--;
            setupItems();
            return;
        }
        
        if (slot == 53) { // Next page
            if (wardrobeSystem != null) {
                Map<String, ArmorSet> wardrobe = wardrobeSystem.getPlayerWardrobe(player);
                int totalPages = (int) Math.ceil((double) wardrobe.size() / ITEMS_PER_PAGE);
                
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    setupItems();
                }
            }
            return;
        }
        
        // Armor set items (slots 10-43)
        if (slot >= 10 && slot <= 43 && slot % 9 != 0 && slot % 9 != 8) {
            if (wardrobeSystem != null) {
                Map<String, ArmorSet> wardrobe = wardrobeSystem.getPlayerWardrobe(player);
                Set<String> setNames = wardrobe.keySet();
                String[] setNamesArray = setNames.toArray(new String[0]);
                
                int startIndex = currentPage * ITEMS_PER_PAGE;
                int setIndex = startIndex + (slot - 10);
                
                if (setIndex < setNamesArray.length) {
                    String setName = setNamesArray[setIndex];
                    ArmorSet armorSet = wardrobe.get(setName);
                    
                    if (event.isRightClick()) {
                        // Show set details
                        player.sendMessage("§aArmor Set: " + armorSet.getDisplayName());
                        player.sendMessage("§7Description: " + armorSet.getDescription());
                        player.sendMessage("§7Defense: " + String.format("%.1f", armorSet.getTotalDefense()));
                        player.sendMessage("§7Pieces: " + armorSet.getArmorCount() + "/4");
                        player.sendMessage("§7Rarity: " + armorSet.getRarity().toUpperCase());
                    } else {
                        // Equip the set
                        boolean success = wardrobeSystem.equipArmorSet(player, setName);
                        if (success) {
                            setupItems(); // Refresh the GUI
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
