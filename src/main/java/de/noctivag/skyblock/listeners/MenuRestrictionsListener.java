package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

/**
 * Menu Restrictions Listener
 * 
 * Prevents players from:
 * - Dropping menu items in the last slot (slot 8)
 * - Taking or moving items in any menu
 * - Dragging items in menus
 */
public class MenuRestrictionsListener implements Listener {
    private final SkyblockPlugin SkyblockPlugin;

    public MenuRestrictionsListener(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        
        // Check if the item is a menu item
        if (isMenuItem(item)) {
            // Check if player is dropping from the last slot (slot 8)
            if (player.getInventory().getHeldItemSlot() == 8) {
                event.setCancelled(true);
                player.sendMessage(Component.text("§cDu kannst Menü-Items nicht aus dem letzten Slot droppen!"));
                return;
            }
            
            // Also prevent dropping menu items from any slot
            event.setCancelled(true);
            player.sendMessage(Component.text("§cDu kannst Menü-Items nicht droppen!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        // Check if the inventory is a menu (not player inventory)
        if (event.getInventory().getType() == InventoryType.PLAYER) {
            // Only restrict if clicking on menu items in player inventory
            ItemStack clicked = event.getCurrentItem();
            if (clicked != null && isMenuItem(clicked)) {
                // Prevent taking menu items from player inventory
                if (event.getClick().isShiftClick() || event.getClick().isLeftClick() || event.getClick().isRightClick()) {
                    event.setCancelled(true);
                    player.sendMessage(Component.text("§cDu kannst Menü-Items nicht aus deinem Inventar nehmen!"));
                }
            }
            return;
        }
        
        // For all other inventories (menus), prevent any item manipulation
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        
        // Check if this is a menu by title patterns
        if (isMenuTitle(title)) {
            // Allow only specific actions in menus - let other listeners handle the actual menu functionality
            // This listener only prevents item manipulation, not menu interactions
            if (event.getClick().isShiftClick() || 
                event.getClick().isLeftClick() || 
                event.getClick().isRightClick()) {
                
                // Check if clicking on a valid menu item
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null && clicked.getType() != Material.AIR) {
                    // Allow the click for menu functionality - other listeners will handle it
                    return;
                }
                
                // Prevent taking empty slots or invalid items
                event.setCancelled(true);
                player.sendMessage(Component.text("§cDu kannst keine Items aus dem Menü nehmen oder verschieben!"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        // Check if dragging in a menu
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        
        if (isMenuTitle(title)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("§cDu kannst keine Items im Menü verschieben!"));
        }
        
        // Also prevent dragging menu items from player inventory
        for (int slot : event.getRawSlots()) {
            if (slot < 36) { // Player inventory slots
                ItemStack item = event.getInventory().getItem(slot);
                if (item != null && isMenuItem(item)) {
                    event.setCancelled(true);
                    player.sendMessage(Component.text("§cDu kannst Menü-Items nicht verschieben!"));
                    break;
                }
            }
        }
    }

    /**
     * Check if an item is a menu item
     */
    private boolean isMenuItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;
        
        Component displayName = meta.displayName();
        if (displayName == null) return false;
        
        String name = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(displayName);
        
        // Check for common menu item patterns
        return name.contains("§6✧ Hauptmenü ✧") ||
               name.contains("Hauptmenü") ||
               name.contains("Island") ||
               name.contains("Islands") ||
               name.contains("Menu") ||
               name.contains("Menü") ||
               name.contains("GUI") ||
               name.contains("§a§l") ||
               name.contains("§c§l") ||
               name.contains("§e§l") ||
               name.contains("§b§l") ||
               name.contains("§d§l") ||
               name.contains("§5§l") ||
               name.contains("§6§l") ||
               name.contains("§f§l") ||
               name.contains("§7§l") ||
               name.contains("§8§l") ||
               name.contains("§9§l") ||
               name.contains("§0§l") ||
               name.contains("§1§l") ||
               name.contains("§2§l") ||
               name.contains("§3§l") ||
               name.contains("§4§l");
    }

    /**
     * Check if a title indicates a menu
     */
    private boolean isMenuTitle(String title) {
        if (title == null) return false;
        
        return title.contains("§a§lIslands") ||
               title.contains("§c§lEvent Menü") ||
               title.contains("§6✧ Hauptmenü ✧") ||
               title.contains("Hauptmenü") ||
               title.contains("Warp-Menü") ||
               title.contains("Warps:") ||
               title.contains("SkyBlock Menu") ||
               title.contains("Skills") ||
               title.contains("Collections") ||
               title.contains("Profile") ||
               title.contains("Fast Travel") ||
               title.contains("ULTIMATE MENU") ||
               title.contains("ULTIMATE EVENTS") ||
               title.contains("REFORGE SYSTEM") ||
               title.contains("ENCHANTING SYSTEM") ||
               title.contains("PET MANAGEMENT") ||
               title.contains("ACCESSORY SYSTEM") ||
               title.contains("INTEGRATED MENU") ||
               title.contains("Cosmetics") ||
               title.contains("Messages") ||
               title.contains("Settings") ||
               title.contains("Friends") ||
               title.contains("Party") ||
               title.contains("Admin") ||
               title.contains("Gadgets") ||
               title.contains("Basic Commands") ||
               title.contains("Join Message") ||
               title.contains("Menu") ||
               title.contains("Menü") ||
               title.contains("GUI");
    }
}
