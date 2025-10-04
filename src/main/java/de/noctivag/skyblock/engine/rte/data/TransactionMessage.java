package de.noctivag.skyblock.engine.rte.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

/**
 * Transaction Message - Real-time transaction broadcast
 */
public class TransactionMessage {
    
    private final UUID buyerId;
    private final UUID sellerId;
    private final String itemId;
    private final int amount;
    private final double price;
    private final long timestamp;
    
    public TransactionMessage(UUID buyerId, UUID sellerId, String itemId, int amount, double price, long timestamp) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.timestamp = timestamp;
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static TransactionMessage fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, TransactionMessage.class);
    }
    
    // Getters
    public UUID getBuyerId() { return buyerId; }
    public UUID getSellerId() { return sellerId; }
    public String getItemId() { return itemId; }
    public int getAmount() { return amount; }
    public double getPrice() { return price; }
    public long getTimestamp() { return timestamp; }
}