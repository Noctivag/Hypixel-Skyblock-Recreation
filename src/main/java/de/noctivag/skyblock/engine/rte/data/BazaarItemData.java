package de.noctivag.skyblock.engine.rte.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Bazaar Item Data - Real-time market data for bazaar items
 */
public class BazaarItemData {
    
    private final String itemId;
    private double instantBuyPrice;
    private double instantSellPrice;
    private double averagePrice;
    private int totalVolume;
    private int orderCount;
    private final List<Double> priceHistory = new ArrayList<>();
    private final List<Integer> volumeHistory = new ArrayList<>();
    private long lastUpdate;
    
    public BazaarItemData(String itemId, double instantBuyPrice, double instantSellPrice, double averagePrice) {
        this.itemId = itemId;
        this.instantBuyPrice = instantBuyPrice;
        this.instantSellPrice = instantSellPrice;
        this.averagePrice = averagePrice;
        this.totalVolume = 0;
        this.orderCount = 0;
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public void updatePrices(PriceData priceData) {
        this.instantBuyPrice = priceData.getInstantBuyPrice();
        this.instantSellPrice = priceData.getInstantSellPrice();
        this.averagePrice = priceData.getAveragePrice();
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public void addTrade(int volume, double price) {
        this.totalVolume += volume;
        this.orderCount++;
        
        // Update price history
        priceHistory.add(price);
        volumeHistory.add(volume);
        
        // Keep only last 100 entries
        if (priceHistory.size() > 100) {
            priceHistory.remove(0);
            volumeHistory.remove(0);
        }
        
        // Update average price
        if (!priceHistory.isEmpty()) {
            double sum = 0.0;
            for (Double p : priceHistory) {
                sum += p;
            }
            this.averagePrice = sum / priceHistory.size();
        }
        
        this.lastUpdate = System.currentTimeMillis();
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static BazaarItemData fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, BazaarItemData.class);
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public double getInstantBuyPrice() { return instantBuyPrice; }
    public double getInstantSellPrice() { return instantSellPrice; }
    public double getAveragePrice() { return averagePrice; }
    public int getTotalVolume() { return totalVolume; }
    public int getOrderCount() { return orderCount; }
    public List<Double> getPriceHistory() { return new ArrayList<>(priceHistory); }
    public List<Integer> getVolumeHistory() { return new ArrayList<>(volumeHistory); }
    public long getLastUpdate() { return lastUpdate; }
}