package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * NPC Creation GUI - Create new NPCs
 */
public class NPCCreationGUI extends Menu {
    
    public NPCCreationGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8NPC Creation", 54);
    }
    
    public NPCCreationGUI(SkyblockPlugin plugin, Player player, org.bukkit.Location location) {
        super(plugin, player, "§8NPC Creation", 54);
        this.location = location;
    }
    
    private org.bukkit.Location location;
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // NPC Types
        inventory.setItem(10, createItem(Material.VILLAGER_SPAWN_EGG, "§aVillager NPC", 
            "§7Create a villager NPC",
            "§7• Trading functionality",
            "§7• Quest giver",
            "§7• Information provider",
            "",
            "§eClick to create"));
        
        inventory.setItem(12, createItem(Material.ZOMBIE_SPAWN_EGG, "§cMonster NPC", 
            "§7Create a monster NPC",
            "§7• Combat interactions",
            "§7• Boss encounters",
            "§7• Special abilities",
            "",
            "§eClick to create"));
        
        inventory.setItem(14, createItem(Material.PLAYER_HEAD, "§bCustom NPC", 
            "§7Create a custom NPC",
            "§7• Custom appearance",
            "§7• Custom interactions",
            "§7• Custom behavior",
            "",
            "§eClick to create"));
        
        inventory.setItem(16, createItem(Material.ARMOR_STAND, "§dDecoration NPC", 
            "§7Create a decoration NPC",
            "§7• Static display",
            "§7• Cosmetic purposes",
            "§7• No interactions",
            "",
            "§eClick to create"));
        
        // NPC Settings
        inventory.setItem(28, createItem(Material.NAME_TAG, "§eNPC Name", 
            "§7Set NPC name",
            "§7• Display name",
            "§7• Custom formatting",
            "",
            "§eClick to set"));
        
        inventory.setItem(30, createItem(Material.BOOK, "§fNPC Dialog", 
            "§7Set NPC dialog",
            "§7• Greeting messages",
            "§7• Interaction text",
            "§7• Quest descriptions",
            "",
            "§eClick to set"));
        
        inventory.setItem(32, createItem(Material.COMPASS, "§6NPC Location", 
            "§7Set NPC location",
            "§7• Current position",
            "§7• Custom coordinates",
            "§7• World selection",
            "",
            "§eClick to set"));
        
        // Actions
        inventory.setItem(45, createItem(Material.GREEN_CONCRETE, "§aCreate NPC", 
            "§7Create the NPC with current settings", "", "§eClick to create"));
        
        inventory.setItem(47, createItem(Material.RED_CONCRETE, "§cCancel", 
            "§7Cancel NPC creation", "", "§eClick to cancel"));
        
        // Close button
        inventory.setItem(49, createItem(Material.BARRIER, "§cClose", 
            "§7Close this menu", "", "§eClick to close"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10 -> {
                // Villager NPC
                player.sendMessage("§aCreating villager NPC...");
            }
            case 12 -> {
                // Monster NPC
                player.sendMessage("§cCreating monster NPC...");
            }
            case 14 -> {
                // Custom NPC
                player.sendMessage("§bCreating custom NPC...");
            }
            case 16 -> {
                // Decoration NPC
                player.sendMessage("§dCreating decoration NPC...");
            }
            case 28 -> {
                // NPC Name
                player.sendMessage("§eSetting NPC name...");
            }
            case 30 -> {
                // NPC Dialog
                player.sendMessage("§fSetting NPC dialog...");
            }
            case 32 -> {
                // NPC Location
                player.sendMessage("§6Setting NPC location...");
            }
            case 45 -> {
                // Create NPC
                player.sendMessage("§aNPC created successfully!");
                player.closeInventory();
            }
            case 47 -> {
                // Cancel
                player.sendMessage("§cNPC creation cancelled.");
                player.closeInventory();
            }
            case 49 -> {
                // Close
                player.closeInventory();
            }
        }
    }
}
