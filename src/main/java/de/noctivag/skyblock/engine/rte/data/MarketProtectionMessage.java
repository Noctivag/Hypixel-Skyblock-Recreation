package de.noctivag.skyblock.engine.rte.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Market Protection Message - Market protection activation broadcast
 */
public class MarketProtectionMessage {
    
    private final String itemId;
    private final ProtectionType protectionType;
    private final long timestamp;
    
    public MarketProtectionMessage(String itemId, ProtectionType protectionType, long timestamp) {
        this.itemId = itemId;
        this.protectionType = protectionType;
        this.timestamp = timestamp;
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static MarketProtectionMessage fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, MarketProtectionMessage.class);
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public ProtectionType getProtectionType() { return protectionType; }
    public long getTimestamp() { return timestamp; }
    
    public enum ProtectionType {
        MANIPULATION_DETECTED,
        HIGH_VOLUME,
        PRICE_SPIKE,
        PRICE_CRASH,
        EMERGENCY_STOP
    }
}
