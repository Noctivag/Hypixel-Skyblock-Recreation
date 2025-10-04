package de.noctivag.skyblock.engine.rte.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Price Update Message - Real-time price update broadcast
 */
public class PriceUpdateMessage {
    
    private final String itemId;
    private final double instantBuyPrice;
    private final double instantSellPrice;
    private final double averagePrice;
    private final long timestamp;
    
    public PriceUpdateMessage(String itemId, double instantBuyPrice, double instantSellPrice, double averagePrice, long timestamp) {
        this.itemId = itemId;
        this.instantBuyPrice = instantBuyPrice;
        this.instantSellPrice = instantSellPrice;
        this.averagePrice = averagePrice;
        this.timestamp = timestamp;
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static PriceUpdateMessage fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, PriceUpdateMessage.class);
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public double getInstantBuyPrice() { return instantBuyPrice; }
    public double getInstantSellPrice() { return instantSellPrice; }
    public double getAveragePrice() { return averagePrice; }
    public long getTimestamp() { return timestamp; }
}