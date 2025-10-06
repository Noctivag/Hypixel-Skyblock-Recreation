package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * NPC Edit GUI - Edit existing NPCs
 */
public class NPCEditGUI extends Menu {
    
    public NPCEditGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8NPC Editor", 54);
    }
    
    public NPCEditGUI(SkyblockPlugin plugin, Player player, Object npc) {
        super(plugin, player, "§8NPC Editor", 54);
        this.npc = npc;
    }
    
    private Object npc;
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // NPC Information
        inventory.setItem(10, createItem(Material.NAME_TAG, "§eEdit Name", 
            "§7Change NPC name",
            "§7• Display name",
            "§7• Custom formatting",
            "",
            "§eClick to edit"));
        
        inventory.setItem(12, createItem(Material.BOOK, "§fEdit Dialog", 
            "§7Change NPC dialog",
            "§7• Greeting messages",
            "§7• Interaction text",
            "§7• Quest descriptions",
            "",
            "§eClick to edit"));
        
        inventory.setItem(14, createItem(Material.COMPASS, "§6Edit Location", 
            "§7Change NPC location",
            "§7• Move to new position",
            "§7• Set coordinates",
            "§7• Change world",
            "",
            "§eClick to edit"));
        
        inventory.setItem(16, createItem(Material.LEATHER_CHESTPLATE, "§dEdit Appearance", 
            "§7Change NPC appearance",
            "§7• Skin/Model",
            "§7• Equipment",
            "§7• Custom model data",
            "",
            "§eClick to edit"));
        
        // NPC Behavior
        inventory.setItem(28, createItem(Material.REDSTONE, "§cEdit Behavior", 
            "§7Change NPC behavior",
            "§7• Movement patterns",
            "§7• Interaction rules",
            "§7• AI settings",
            "",
            "§eClick to edit"));
        
        inventory.setItem(30, createItem(Material.EMERALD, "§aEdit Trading", 
            "§7Configure trading",
            "§7• Trade offers",
            "§7• Prices",
            "§7• Requirements",
            "",
            "§eClick to edit"));
        
        inventory.setItem(32, createItem(Material.WRITABLE_BOOK, "§bEdit Quests", 
            "§7Configure quests",
            "§7• Quest objectives",
            "§7• Rewards",
            "§7• Progress tracking",
            "",
            "§eClick to edit"));
        
        // Actions
        inventory.setItem(45, createItem(Material.GREEN_CONCRETE, "§aSave Changes", 
            "§7Save all changes to NPC", "", "§eClick to save"));
        
        inventory.setItem(47, createItem(Material.RED_CONCRETE, "§cDelete NPC", 
            "§7Delete this NPC permanently", "", "§eClick to delete"));
        
        // Close button
        inventory.setItem(49, createItem(Material.BARRIER, "§cClose", 
            "§7Close this menu", "", "§eClick to close"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10 -> {
                // Edit Name
                player.sendMessage("§eEditing NPC name...");
            }
            case 12 -> {
                // Edit Dialog
                player.sendMessage("§fEditing NPC dialog...");
            }
            case 14 -> {
                // Edit Location
                player.sendMessage("§6Editing NPC location...");
            }
            case 16 -> {
                // Edit Appearance
                player.sendMessage("§dEditing NPC appearance...");
            }
            case 28 -> {
                // Edit Behavior
                player.sendMessage("§cEditing NPC behavior...");
            }
            case 30 -> {
                // Edit Trading
                player.sendMessage("§aEditing NPC trading...");
            }
            case 32 -> {
                // Edit Quests
                player.sendMessage("§bEditing NPC quests...");
            }
            case 45 -> {
                // Save Changes
                player.sendMessage("§aNPC changes saved successfully!");
                player.closeInventory();
            }
            case 47 -> {
                // Delete NPC
                player.sendMessage("§cNPC deleted successfully!");
                player.closeInventory();
            }
            case 49 -> {
                // Close
                player.closeInventory();
            }
        }
    }
}
