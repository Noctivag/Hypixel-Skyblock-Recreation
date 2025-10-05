package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * NPC Management GUI
 */
public class NPCManagementGUI extends CustomGUI {
    
    public NPCManagementGUI() {
        super("§eNPC Management", 54);
        setupItems();
    }
    
    public NPCManagementGUI(SkyblockPlugin plugin, Player player) {
        super("§eNPC Management", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add NPC management items
        ItemStack npc = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta npcMeta = npc.getItemMeta();
        npcMeta.setDisplayName("§eCreate NPC");
        npcMeta.setLore(Arrays.asList("§7Create a new NPC", "§7Click to create!"));
        npc.setItemMeta(npcMeta);
        inventory.setItem(22, npc);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the NPC management GUI for a player
     */
    public static void openForPlayer(Player player) {
        NPCManagementGUI gui = new NPCManagementGUI();
        gui.open(player);
    }
}

