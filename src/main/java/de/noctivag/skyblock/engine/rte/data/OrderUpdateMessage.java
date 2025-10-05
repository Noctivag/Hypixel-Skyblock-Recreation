package de.noctivag.skyblock.engine.rte.data;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

/**
 * Order Update Message - Real-time order update broadcast
 */
public class OrderUpdateMessage {
    
    private final UUID orderId;
    private final UUID playerId;
    private final String itemId;
    private final int amount;
    private final double price;
    private final BazaarOrder.OrderType type;
    private final long timestamp;
    
    public OrderUpdateMessage(UUID orderId, UUID playerId, String itemId, int amount, double price, BazaarOrder.OrderType type, long timestamp) {
        this.orderId = orderId;
        this.playerId = playerId;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.timestamp = timestamp;
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static OrderUpdateMessage fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, OrderUpdateMessage.class);
    }
    
    // Getters
    public UUID getOrderId() { return orderId; }
    public UUID getPlayerId() { return playerId; }
    public String getItemId() { return itemId; }
    public int getAmount() { return amount; }
    public double getPrice() { return price; }
    public BazaarOrder.OrderType getType() { return type; }
    public long getTimestamp() { return timestamp; }
}
