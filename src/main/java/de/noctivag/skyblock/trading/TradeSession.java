package de.noctivag.skyblock.trading;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trade session between two players
 */
public class TradeSession {
    
    private final Player player1;
    private final Player player2;
    private final TradingSystem tradingSystem;
    private final long startTime;
    
    // Trade items
    private final List<ItemStack> player1Items = new ArrayList<>();
    private final List<ItemStack> player2Items = new ArrayList<>();
    
    // Trade status
    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private boolean isCompleted = false;
    private boolean isCancelled = false;
    
    public TradeSession(Player player1, Player player2, TradingSystem tradingSystem) {
        this.player1 = player1;
        this.player2 = player2;
        this.tradingSystem = tradingSystem;
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Open the trade GUI for both players
     */
    public void openTradeGUI() {
        new TradeGUI(this, player1).open();
        new TradeGUI(this, player2).open();
    }
    
    /**
     * Add an item to a player's trade
     */
    public boolean addItem(Player player, ItemStack item) {
        if (isCompleted || isCancelled) {
            return false;
        }
        
        if (player.equals(player1)) {
            player1Items.add(item);
        } else if (player.equals(player2)) {
            player2Items.add(item);
        } else {
            return false;
        }
        
        // Reset ready status when items change
        player1Ready = false;
        player2Ready = false;
        
        // Update both GUIs
        updateGUIs();
        
        return true;
    }
    
    /**
     * Remove an item from a player's trade
     */
    public boolean removeItem(Player player, int index) {
        if (isCompleted || isCancelled) {
            return false;
        }
        
        List<ItemStack> items = player.equals(player1) ? player1Items : player2Items;
        
        if (index < 0 || index >= items.size()) {
            return false;
        }
        
        items.remove(index);
        
        // Reset ready status when items change
        player1Ready = false;
        player2Ready = false;
        
        // Update both GUIs
        updateGUIs();
        
        return true;
    }
    
    /**
     * Set a player's ready status
     */
    public boolean setReady(Player player, boolean ready) {
        if (isCompleted || isCancelled) {
            return false;
        }
        
        if (player.equals(player1)) {
            player1Ready = ready;
        } else if (player.equals(player2)) {
            player2Ready = ready;
        } else {
            return false;
        }
        
        // Update both GUIs
        updateGUIs();
        
        // Check if both players are ready
        if (player1Ready && player2Ready) {
            executeTrade();
        }
        
        return true;
    }
    
    /**
     * Execute the trade
     */
    private void executeTrade() {
        if (isCompleted || isCancelled) {
            return;
        }
        
        // Check if both players have inventory space
        if (!hasInventorySpace(player1, player2Items) || !hasInventorySpace(player2, player1Items)) {
            player1.sendMessage("§cNot enough inventory space to complete the trade!");
            player2.sendMessage("§cNot enough inventory space to complete the trade!");
            return;
        }
        
        // Execute the trade
        for (ItemStack item : player2Items) {
            if (item != null) {
                player1.getInventory().addItem(item);
            }
        }
        
        for (ItemStack item : player1Items) {
            if (item != null) {
                player2.getInventory().addItem(item);
            }
        }
        
        isCompleted = true;
        
        // Send success messages
        player1.sendMessage("§aTrade completed successfully!");
        player2.sendMessage("§aTrade completed successfully!");
        
        // Close GUIs
        player1.closeInventory();
        player2.closeInventory();
        
        // Remove from trading system
        tradingSystem.removeTradeSession(this);
    }
    
    /**
     * Cancel the trade
     */
    public void cancel(String reason) {
        if (isCompleted || isCancelled) {
            return;
        }
        
        isCancelled = true;
        
        // Return items to players
        for (ItemStack item : player1Items) {
            if (item != null) {
                player1.getInventory().addItem(item);
            }
        }
        
        for (ItemStack item : player2Items) {
            if (item != null) {
                player2.getInventory().addItem(item);
            }
        }
        
        // Send cancellation messages
        player1.sendMessage("§cTrade cancelled: " + reason);
        player2.sendMessage("§cTrade cancelled: " + reason);
        
        // Close GUIs
        player1.closeInventory();
        player2.closeInventory();
        
        // Remove from trading system
        tradingSystem.removeTradeSession(this);
    }
    
    /**
     * Check if a player has enough inventory space
     */
    private boolean hasInventorySpace(Player player, List<ItemStack> items) {
        int emptySlots = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                emptySlots++;
            }
        }
        
        return emptySlots >= items.size();
    }
    
    /**
     * Update both trade GUIs
     */
    private void updateGUIs() {
        // This would update the GUI in a real implementation
        // For now, we'll just send a message
        player1.sendMessage("§7Trade updated. Items: " + player1Items.size() + " vs " + player2Items.size());
        player2.sendMessage("§7Trade updated. Items: " + player2Items.size() + " vs " + player1Items.size());
    }
    
    /**
     * Check if the trade session has expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > 300000; // 5 minutes
    }
    
    // Getters
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public List<ItemStack> getPlayer1Items() { return new ArrayList<>(player1Items); }
    public List<ItemStack> getPlayer2Items() { return new ArrayList<>(player2Items); }
    public boolean isPlayer1Ready() { return player1Ready; }
    public boolean isPlayer2Ready() { return player2Ready; }
    public boolean isCompleted() { return isCompleted; }
    public boolean isCancelled() { return isCancelled; }
    public long getStartTime() { return startTime; }
    
    /**
     * Get the other player in the trade
     */
    public Player getOtherPlayer(Player player) {
        if (player.equals(player1)) {
            return player2;
        } else if (player.equals(player2)) {
            return player1;
        }
        return null;
    }
    
    /**
     * Get a player's items
     */
    public List<ItemStack> getPlayerItems(Player player) {
        if (player.equals(player1)) {
            return getPlayer1Items();
        } else if (player.equals(player2)) {
            return getPlayer2Items();
        }
        return new ArrayList<>();
    }
    
    /**
     * Get a player's ready status
     */
    public boolean isPlayerReady(Player player) {
        if (player.equals(player1)) {
            return player1Ready;
        } else if (player.equals(player2)) {
            return player2Ready;
        }
        return false;
    }
}
