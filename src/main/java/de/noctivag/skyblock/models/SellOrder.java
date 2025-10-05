package de.noctivag.skyblock.models;

import java.util.UUID;

/**
 * Datenstruktur für Verkaufs-Orders im Bazaar
 * Repräsentiert eine Verkaufs-Anfrage eines Spielers
 */
public class SellOrder {
    
    private final String orderId;
    private final UUID playerId;
    private final String itemName;
    private final double pricePerUnit;
    private final int amount;
    private final long timestamp;
    private int soldAmount;
    private boolean isActive;
    
    public SellOrder(UUID playerId, String itemName, double pricePerUnit, int amount) {
        this.orderId = generateOrderId();
        this.playerId = playerId;
        this.itemName = itemName;
        this.pricePerUnit = pricePerUnit;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
        this.soldAmount = 0;
        this.isActive = true;
    }
    
    /**
     * Generiert eine eindeutige Order-ID
     * @return Order-ID
     */
    private String generateOrderId() {
        return "SELL_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000);
    }
    
    /**
     * Verkauft einen Teil der Order
     * @param amount Die zu verkaufende Menge
     * @return true wenn Order vollständig verkauft wurde
     */
    public boolean sellOrder(int amount) {
        this.soldAmount += amount;
        if (this.soldAmount >= this.amount) {
            this.isActive = false;
            return true; // Order vollständig verkauft
        }
        return false; // Order noch nicht vollständig verkauft
    }
    
    /**
     * Gibt die verbleibende Menge zurück
     * @return Verbleibende Menge
     */
    public int getRemainingAmount() {
        return Math.max(0, amount - soldAmount);
    }
    
    /**
     * Gibt den Gesamtwert der Order zurück
     * @return Gesamtwert
     */
    public double getTotalValue() {
        return pricePerUnit * amount;
    }
    
    /**
     * Gibt den Wert der verkauften Menge zurück
     * @return Wert der verkauften Menge
     */
    public double getSoldValue() {
        return pricePerUnit * soldAmount;
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
    
    public int getSoldAmount() {
        return soldAmount;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "SellOrder{" +
                "orderId='" + orderId + '\'' +
                ", playerId=" + playerId +
                ", itemName='" + itemName + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", amount=" + amount +
                ", soldAmount=" + soldAmount +
                ", isActive=" + isActive +
                '}';
    }
}
