package de.noctivag.skyblock.models;

import java.util.UUID;

/**
 * Datenstruktur für Kauf-Orders im Bazaar
 * Repräsentiert eine Kauf-Anfrage eines Spielers
 */
public class BuyOrder {
    
    private final String orderId;
    private final UUID playerId;
    private final String itemName;
    private final double pricePerUnit;
    private final int amount;
    private final long timestamp;
    private int filledAmount;
    private boolean isActive;
    
    public BuyOrder(UUID playerId, String itemName, double pricePerUnit, int amount) {
        this.orderId = generateOrderId();
        this.playerId = playerId;
        this.itemName = itemName;
        this.pricePerUnit = pricePerUnit;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.filledAmount = 0;
        this.isActive = true;
    }
    
    /**
     * Generiert eine eindeutige Order-ID
     * @return Order-ID
     */
    private String generateOrderId() {
        return "BUY_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }
    
    /**
     * Füllt einen Teil der Order
     * @param amount Die zu füllende Menge
     * @return true wenn Order vollständig gefüllt wurde
     */
    public boolean fillOrder(int amount) {
        this.filledAmount += amount;
        if (this.filledAmount >= this.amount) {
            this.isActive = false;
            return true; // Order vollständig gefüllt
        }
        return false; // Order noch nicht vollständig
    }
    
    /**
     * Gibt die verbleibende Menge zurück
     * @return Verbleibende Menge
     */
    public int getRemainingAmount() {
        return Math.max(0, amount - filledAmount);
    }
    
    /**
     * Gibt den Gesamtwert der Order zurück
     * @return Gesamtwert
     */
    public double getTotalValue() {
        return pricePerUnit * amount;
    }
    
    /**
     * Gibt den Wert der gefüllten Menge zurück
     * @return Wert der gefüllten Menge
     */
    public double getFilledValue() {
        return pricePerUnit * filledAmount;
    }
    
    /**
     * Gibt den Wert der verbleibenden Menge zurück
     * @return Wert der verbleibenden Menge
     */
    public double getRemainingValue() {
        return pricePerUnit * getRemainingAmount();
    }
    
    // Getter-Methoden
    public String getOrderId() {
        return orderId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public int getFilledAmount() {
        return filledAmount;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "BuyOrder{" +
                "orderId='" + orderId + '\'' +
                ", playerId=" + playerId +
                ", itemName='" + itemName + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", amount=" + amount +
                ", filledAmount=" + filledAmount +
                ", isActive=" + isActive +
                '}';
    }
}
