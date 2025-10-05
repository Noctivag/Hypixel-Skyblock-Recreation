package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Listener for brewing modifier GUI events
 */
public class BrewingModifierGUIListener implements Listener {
    
    private final SkyblockPlugin plugin;
    private final BrewingModifierManager modifierManager;
    
    public BrewingModifierGUIListener(SkyblockPlugin plugin, BrewingModifierManager modifierManager) {
        this.plugin = plugin;
        this.modifierManager = modifierManager;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        
        if (!(event.getInventory().getHolder() instanceof BrewingModifierGUI)) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        // Handle close button
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        // Handle modifier items
        if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
            handleModifierClick(player, clickedItem);
        }
    }
    
    private void handleModifierClick(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getDisplayName() == null) {
            return;
        }
        
        String displayName = meta.getDisplayName();
        
        // Extract modifier type from display name
        String typeStr = displayName.replace("§a", "").replace("§7", "").replace("§8", "").split(" ")[0];
        
        try {
            BrewingModifierType type = BrewingModifierType.valueOf(typeStr);
            
            if (modifierManager.hasModifier(player.getUniqueId(), type)) {
                // Remove modifier
                modifierManager.removeModifier(player.getUniqueId(), type);
                player.sendMessage("§aRemoved brewing modifier: " + type.name());
            } else {
                // Add modifier (with default multiplier)
                BrewingModifier modifier = BrewingModifierFactory.createCustomModifier(
                    type.name(), 1.5, "Custom modifier"
                );
                modifierManager.addModifier(player.getUniqueId(), type, modifier);
                player.sendMessage("§aAdded brewing modifier: " + type.name() + " (1.5x)");
            }
            
            // Refresh GUI
            BrewingModifierGUI gui = new BrewingModifierGUI(plugin, player, modifierManager);
            gui.open(player);
            
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid modifier type!");
        }
    }
}