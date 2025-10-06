package de.noctivag.skyblock.dungeons.classes.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.dungeons.classes.ClassManager;
import de.noctivag.skyblock.dungeons.classes.DungeonClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Class Selection GUI - GUI for selecting dungeon classes
 */
public class ClassSelectionGUI extends CustomGUI {
    
    private final ClassManager classManager;
    
    public ClassSelectionGUI(Player player, ClassManager classManager) {
        super("§cSelect Dungeon Class", 54);
        this.classManager = classManager;
    }
    
    @Override
    public void setupItems() {
        // Get available classes
        Map<String, DungeonClass> availableClasses = classManager.getAvailableClasses();
        
        // Place classes in inventory
        int slot = 10;
        for (DungeonClass dungeonClass : availableClasses.values()) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack classItem = dungeonClass.createItemStack();
            inventory.setItem(slot, classItem);
            
            slot += 2; // Skip every other slot for better layout
        }
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupNavigation() {
        // Close button
        ItemStack closeItem = new ItemStack(org.bukkit.Material.BARRIER);
        org.bukkit.inventory.meta.ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§cClose");
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
}

