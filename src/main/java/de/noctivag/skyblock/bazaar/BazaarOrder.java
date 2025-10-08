package de.noctivag.skyblock.bazaar;

import java.util.UUID;

/**
 * Bazaar Order - Represents a bazaar order
 */
public class BazaarOrder {
    
    private final UUID orderId;
    private final UUID playerId;
    private final String itemId;
    private final int amount;
    private final double pricePerUnit;
    private final BazaarOrderType orderType;
    private final long creationTime;
    private boolean active;
    
    public BazaarOrder(UUID orderId, UUID playerId, String itemId, int amount, 
                       double pricePerUnit, BazaarOrderType orderType) {
        this.orderId = orderId;
        this.playerId = playerId;
        this.itemId = itemId;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.orderType = orderType;
        this.creationTime = System.currentTimeMillis();
        this.active = true;
    }
    
    /**
     * Get the order ID
     */
    public UUID getOrderId() {
        return orderId;
    }
    
    /**
     * Get the player ID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Get the item ID
     */
    public String getItemId() {
        return itemId;
    }
    
    /**
     * Get the amount
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * Get the price per unit
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Get the total price (amount * pricePerUnit)
     */
    public double getPrice() {
        return getTotalPrice();
    }
    
    /**
     * Get the order type
     */
    public BazaarOrderType getOrderType() {
        return orderType;
    }
    
    /**
     * Get the creation time
     */
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Check if the order is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Set the order as active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Get the total price
     */
    public double getTotalPrice() {
        return amount * pricePerUnit;
    }
}
