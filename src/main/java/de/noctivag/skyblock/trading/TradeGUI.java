package de.noctivag.skyblock.trading;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TradeGUI extends Menu {
    
    private final TradeSession tradeSession;
    private final Player currentPlayer;
    private final Player otherPlayer;
    
    public TradeGUI(TradeSession tradeSession, Player player) {
        super(SkyblockPlugin.getInstance(), player, "§8§lTrading with " + tradeSession.getOtherPlayer(player).getName(), 54);
        this.tradeSession = tradeSession;
        this.currentPlayer = player;
        this.otherPlayer = tradeSession.getOtherPlayer(player);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Trade info
        setupTradeInfo();
        
        // Player's items (left side)
        setupPlayerItems();
        
        // Other player's items (right side)
        setupOtherPlayerItems();
        
        // Action buttons
        setupActionButtons();
        
        // Close button
        setCloseButton(49);
    }
    
    private void setupTradeInfo() {
        setItem(4, Material.ANVIL, "§eTrade Session", "special",
            "&7Trading with: &f" + otherPlayer.getName(),
            "&7Your items: &f" + tradeSession.getPlayerItems(currentPlayer).size(),
            "&7Their items: &f" + tradeSession.getPlayerItems(otherPlayer).size(),
            "",
            "&7Click items to add/remove them",
            "&7from the trade.");
    }
    
    private void setupPlayerItems() {
        // Player's inventory slots (left side)
        int startSlot = 10;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int slot = startSlot + (i * 9) + j;
                int itemIndex = i * 3 + j;
                
                if (itemIndex < tradeSession.getPlayerItems(currentPlayer).size()) {
                    ItemStack item = tradeSession.getPlayerItems(currentPlayer).get(itemIndex);
                    String displayName = item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
                        ? item.getItemMeta().getDisplayName() 
                        : item.getType().name().toLowerCase().replace("_", " ");
                    setItem(slot, item.getType(), displayName, "uncommon",
                        "&7Click to remove from trade");
                } else {
                    setItem(slot, Material.GRAY_STAINED_GLASS_PANE, "§7Empty Slot", "common",
                        "&7Click to add item from inventory");
                }
            }
        }
    }
    
    private void setupOtherPlayerItems() {
        // Other player's items (right side)
        int startSlot = 14;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int slot = startSlot + (i * 9) + j;
                int itemIndex = i * 3 + j;
                
                if (itemIndex < tradeSession.getPlayerItems(otherPlayer).size()) {
                    ItemStack item = tradeSession.getPlayerItems(otherPlayer).get(itemIndex);
                    String displayName = item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
                        ? item.getItemMeta().getDisplayName() 
                        : item.getType().name().toLowerCase().replace("_", " ");
                    setItem(slot, item.getType(), displayName, "uncommon",
                        "&7" + otherPlayer.getName() + "'s item",
                        "&7Cannot be modified");
                } else {
                    setItem(slot, Material.GRAY_STAINED_GLASS_PANE, "§7Empty Slot", "common",
                        "&7" + otherPlayer.getName() + "'s slot");
                }
            }
        }
    }
    
    private void setupActionButtons() {
        // Ready button
        boolean isReady = tradeSession.isPlayerReady(currentPlayer);
        setItem(45, isReady ? Material.LIME_WOOL : Material.RED_WOOL, 
            isReady ? "§aReady!" : "§cNot Ready", "uncommon",
            "&7Click to toggle ready status",
            "",
            isReady ? "&aYou are ready to trade" : "&cYou are not ready to trade");
        
        // Cancel button
        setItem(47, Material.BARRIER, "§cCancel Trade", "uncommon",
            "&7Click to cancel the trade",
            "",
            "&cThis will return all items");
        
        // Status display
        boolean otherReady = tradeSession.isPlayerReady(otherPlayer);
        setItem(46, Material.BOOK, "§eTrade Status", "uncommon",
            "&7Your status: " + (isReady ? "&aReady" : "&cNot Ready"),
            "&7" + otherPlayer.getName() + "'s status: " + (otherReady ? "&aReady" : "&cNot Ready"),
            "",
            "&7Both players must be ready to trade");
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Player's items (left side, slots 10-41)
        if (slot >= 10 && slot <= 41 && slot % 9 >= 1 && slot % 9 <= 3) {
            int itemIndex = ((slot - 10) / 9) * 3 + ((slot - 10) % 9);
            
            if (itemIndex < tradeSession.getPlayerItems(currentPlayer).size()) {
                // Remove item from trade
                tradeSession.removeItem(currentPlayer, itemIndex);
                setupItems(); // Refresh GUI
            } else {
                // Add item from inventory
                ItemStack cursorItem = event.getCursor();
                if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                    tradeSession.addItem(currentPlayer, cursorItem.clone());
                    event.setCursor(null);
                    setupItems(); // Refresh GUI
                }
            }
            return;
        }
        
        // Action buttons
        if (slot == 45) { // Ready button
            boolean newReady = !tradeSession.isPlayerReady(currentPlayer);
            tradeSession.setReady(currentPlayer, newReady);
            setupItems(); // Refresh GUI
            return;
        }
        
        if (slot == 47) { // Cancel button
            tradeSession.cancel(currentPlayer.getName() + " cancelled the trade");
            return;
        }
        
        // Close button
        if (slot == 49) {
            tradeSession.cancel(currentPlayer.getName() + " closed the trade GUI");
        }
    }
}
