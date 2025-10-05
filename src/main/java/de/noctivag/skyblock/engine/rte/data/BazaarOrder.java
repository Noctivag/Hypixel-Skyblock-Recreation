package de.noctivag.skyblock.engine.rte.data;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

/**
 * Bazaar Order - Individual order in the bazaar system
 */
public class BazaarOrder {
    
    private final UUID id;
    private final UUID playerId;
    private final String itemId;
    private int amount;
    private final double price;
    private final OrderType type;
    private final long createdAt;
    
    public BazaarOrder(UUID id, UUID playerId, String itemId, int amount, double price, OrderType type, long createdAt) {
        this.id = id;
        this.playerId = playerId;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.createdAt = createdAt;
    }
    
    public static BazaarOrder fromRedisData(java.util.Map<String, String> data) {
        UUID id = UUID.fromString(data.get("id"));
        UUID playerId = UUID.fromString(data.get("player_id"));
        String itemId = data.get("item_id");
        int amount = Integer.parseInt(data.get("amount"));
        double price = Double.parseDouble(data.get("price"));
        OrderType type = OrderType.valueOf(data.get("type"));
        long createdAt = Long.parseLong(data.get("created_at"));
        
        return new BazaarOrder(id, playerId, itemId, amount, price, type, createdAt);
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public UUID getPlayerId() { return playerId; }
    public String getItemId() { return itemId; }
    public int getAmount() { return amount; }
    public double getPrice() { return price; }
    public OrderType getType() { return type; }
    public long getCreatedAt() { return createdAt; }
    
    public void setAmount(int amount) { this.amount = amount; }
    
    public enum OrderType {
        BUY, SELL
    }
}
