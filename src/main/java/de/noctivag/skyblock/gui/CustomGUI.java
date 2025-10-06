package de.noctivag.skyblock.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Base class for custom GUIs
 */
public abstract class CustomGUI implements InventoryHolder {
    
    protected Inventory inventory;
    protected String title;
    protected int size;
    
    public CustomGUI(String title, int size) {
        this.title = title;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, title);
    }
    
    /**
     * Get the inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Get the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Get the size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Open the GUI for a player
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }
    
    /**
     * Open the GUI (for compatibility with Menu framework)
     */
    public void open() {
        // This method should be overridden by subclasses that have access to the player
        throw new UnsupportedOperationException("open() method must be overridden by subclasses");
    }
    
    /**
     * Setup the GUI items
     * Override this method to add items to the GUI
     */
    public abstract void setupItems();
}
