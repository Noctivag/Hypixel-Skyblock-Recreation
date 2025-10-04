package de.noctivag.skyblock.reforges;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Reforge Listener - Handles reforge GUI interactions and effects
 */
public class ReforgeListener implements Listener {
    
    private final SkyblockPlugin plugin;
    private final ReforgeSystem reforgeSystem;
    private final ReforgeGUI reforgeGUI;
    private final Map<Player, ItemStack> reforgeHistory;
    
    public ReforgeListener(SkyblockPlugin plugin, ReforgeSystem reforgeSystem, ReforgeGUI reforgeGUI) {
        this.plugin = plugin;
        this.reforgeSystem = reforgeSystem;
        this.reforgeGUI = reforgeGUI;
        this.reforgeHistory = new HashMap<>();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        Inventory inventory = event.getInventory();
        // String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(inventory.getView().title());
        String title = "Reforge Table"; // Placeholder
        
        if (title != null && title.contains("Reforge Table")) {
            handleReforgeGUIClick(player, event);
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        Inventory inventory = event.getInventory();
        // String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(inventory.getView().title());
        String title = "Reforge Table"; // Placeholder
        
        if (title != null && title.contains("Reforge Table")) {
            event.setCancelled(true);
        }
    }
    
    private void handleReforgeGUIClick(Player player, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null) return;
        
        // Check for category buttons
        NamespacedKey categoryKey = new NamespacedKey(plugin, "reforge_category");
        if (meta.getPersistentDataContainer().has(categoryKey, PersistentDataType.STRING)) {
            String category = meta.getPersistentDataContainer().get(categoryKey, PersistentDataType.STRING);
            ItemStack itemInSlot = event.getInventory().getItem(13);
            reforgeGUI.openReforgeCategory(player, category, itemInSlot);
            return;
        }
        
        // Check for reforge selection
        NamespacedKey reforgeKey = new NamespacedKey(plugin, "reforge_select");
        if (meta.getPersistentDataContainer().has(reforgeKey, PersistentDataType.STRING)) {
            String reforgeName = meta.getPersistentDataContainer().get(reforgeKey, PersistentDataType.STRING);
            ReforgeSystem.Reforge reforge = reforgeSystem.getReforge(reforgeName);
            
            if (reforge != null) {
                ItemStack itemInSlot = event.getInventory().getItem(13);
                if (itemInSlot != null && !itemInSlot.getType().isAir()) {
                    applyReforgeToItem(player, itemInSlot, reforge);
                } else {
                    player.sendMessage("§cPlease place an item in the reforge slot first!");
                }
            }
            return;
        }
        
        // Check for action buttons
        NamespacedKey actionKey = new NamespacedKey(plugin, "reforge_action");
        if (meta.getPersistentDataContainer().has(actionKey, PersistentDataType.STRING)) {
            String action = meta.getPersistentDataContainer().get(actionKey, PersistentDataType.STRING);
            handleReforgeAction(player, action, event);
            return;
        }
        
        // Handle item placement in reforge slot
        if (event.getSlot() == 13) {
            handleItemPlacement(player, event);
        }
    }
    
    private void applyReforgeToItem(Player player, ItemStack item, ReforgeSystem.Reforge reforge) {
        if (!reforgeSystem.canReforge(item, reforge)) {
            player.sendMessage("§cThis reforge cannot be applied to this item type!");
            return;
        }
        
        double cost = reforgeSystem.calculateReforgeCost(reforge, item);
        
        // Check if player has enough coins (simplified)
        if (hasEnoughCoins(player, cost)) {
            // Store previous state for undo
            ItemStack previousItem = item.clone();
            reforgeHistory.put(player, previousItem);
            
            // Remove current reforge if any
            ReforgeSystem.Reforge currentReforge = reforgeSystem.getItemReforge(item);
            if (currentReforge != null) {
                player.sendMessage("§eRemoved old reforge: " + currentReforge.getName());
            }
            
            // Apply new reforge
            reforgeSystem.applyReforge(item, reforge);
            
            // Deduct coins (simplified)
            player.sendMessage("§aApplied " + reforge.getName() + " reforge to your item for " + 
                             String.format("%.0f", cost) + " coins!");
            
            // Play sound effect
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            
            // Show stat changes
            showReforgeStats(player, reforge, item);
            
            // Update GUI
            updateReforgeGUI(player, item);
        } else {
            player.sendMessage("§cYou don't have enough coins! Cost: " + 
                             String.format("%.0f", cost) + " coins");
        }
    }
    
    private void handleReforgeAction(Player player, String action, InventoryClickEvent event) {
        switch (action) {
            case "back" -> reforgeGUI.openReforgeTable(player);
            case "remove" -> {
                ItemStack item = event.getInventory().getItem(13);
                if (item != null && !item.getType().isAir()) {
                    removeReforgeFromItem(player, item);
                } else {
                    player.sendMessage("§cNo item in reforge slot!");
                }
            }
            case "undo" -> {
                ItemStack previousItem = reforgeHistory.get(player);
                if (previousItem != null) {
                    ItemStack currentItem = event.getInventory().getItem(13);
                    if (currentItem != null && !currentItem.getType().isAir()) {
                        // Restore previous state
                        event.getInventory().setItem(13, previousItem.clone());
                        reforgeHistory.remove(player);
                        player.sendMessage("§aUndone last reforge!");
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        player.sendMessage("§cNo item to undo reforge on!");
                    }
                } else {
                    player.sendMessage("§cNo reforge history to undo!");
                }
            }
            case "apply" -> {
                player.sendMessage("§eSelect a reforge from the categories above!");
            }
        }
    }
    
    private void handleItemPlacement(Player player, InventoryClickEvent event) {
        ItemStack item = event.getCursor();
        if (item != null && !item.getType().isAir()) {
            // Check if item is compatible with any reforge
            boolean compatible = false;
            for (ReforgeSystem.Reforge reforge : reforgeSystem.getAllReforges()) {
                if (reforgeSystem.canReforge(item, reforge)) {
                    compatible = true;
                    break;
                }
            }
            
            if (compatible) {
                player.sendMessage("§aItem placed! Select a reforge category to view available reforges.");
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            } else {
                player.sendMessage("§cThis item cannot be reforged!");
                event.setCancelled(true);
            }
        }
    }
    
    private void removeReforgeFromItem(Player player, ItemStack item) {
        ReforgeSystem.Reforge currentReforge = reforgeSystem.getItemReforge(item);
        if (currentReforge == null) {
            player.sendMessage("§cThis item doesn't have a reforge to remove!");
            return;
        }
        
        reforgeSystem.removeReforge(item);
        player.sendMessage("§aRemoved " + currentReforge.getName() + " reforge from your item!");
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
        
        // Update GUI
        updateReforgeGUI(player, item);
    }
    
    private void updateReforgeGUI(Player player, ItemStack item) {
        // Close and reopen GUI to update display
        new BukkitRunnable() {
            @Override
            public void run() {
                player.closeInventory();
                reforgeGUI.openReforgeTable(player);
            }
        }.runTaskLater(plugin, 1L);
    }
    
    private void showReforgeStats(Player player, ReforgeSystem.Reforge reforge, ItemStack item) {
        player.sendMessage("§7Stats added:");
        
        java.util.Map<String, Double> stats = reforgeSystem.calculateReforgeStats(reforge, item);
        for (java.util.Map.Entry<String, Double> stat : stats.entrySet()) {
            player.sendMessage("§7- " + stat.getKey() + ": §a+" + String.format("%.1f", stat.getValue()));
        }
    }
    
    private boolean hasEnoughCoins(Player player, double amount) {
        // Simplified coin check - in real implementation, check player's balance
        return true; // For demo purposes
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Handle reforge stone usage
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENCHANTED_BOOK) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return;
        
        // Check if this is a reforge stone
        if (meta.hasLore() && meta.getLore().stream().anyMatch(line -> 
            line.toString().contains("§7Right-click to use this reforge stone!"))) {
            String displayName = meta.displayName() != null ? LegacyComponentSerializer.legacySection().serialize(meta.displayName()) : "";
            String reforgeName = displayName.replace("§d", "").replace(" Reforge Stone", "");
            ReforgeSystem.Reforge reforge = reforgeSystem.getReforge(reforgeName);
            
            if (reforge != null) {
                event.getPlayer().sendMessage("§eDrag this reforge stone onto an item to apply " + reforgeName + " reforge!");
            }
        }
    }
    
    public void clearPlayerHistory(Player player) {
        reforgeHistory.remove(player);
    }
    
    public boolean hasReforgeHistory(Player player) {
        return reforgeHistory.containsKey(player);
    }
}
