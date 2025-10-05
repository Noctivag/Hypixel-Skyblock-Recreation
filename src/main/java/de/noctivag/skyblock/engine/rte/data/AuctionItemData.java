package de.noctivag.skyblock.engine.rte.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Auction Item Data - Real-time market data for auction house items
 */
public class AuctionItemData {
    
    private final String itemId;
    private double averagePrice;
    private int totalAuctions;
    private int activeAuctions;
    private double totalVolume;
    private final List<Double> priceHistory = new ArrayList<>();
    private final List<Integer> volumeHistory = new ArrayList<>();
    private long lastUpdate;
    
    public AuctionItemData(String itemId, double averagePrice) {
        this.itemId = itemId;
        this.averagePrice = averagePrice;
        this.totalAuctions = 0;
        this.activeAuctions = 0;
        this.totalVolume = 0.0;
        this.lastUpdate = java.lang.System.currentTimeMillis();
    }
    
    public void addAuction(double price, int volume) {
        this.totalAuctions++;
        this.activeAuctions++;
        this.totalVolume += volume;
        
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
        
        this.lastUpdate = java.lang.System.currentTimeMillis();
    }
    
    public void completeAuction() {
        this.activeAuctions = Math.max(0, this.activeAuctions - 1);
        this.lastUpdate = java.lang.System.currentTimeMillis();
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
    
    public static AuctionItemData fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, AuctionItemData.class);
    }
    
    // Getters
    public String getItemId() { return itemId; }
    public double getAveragePrice() { return averagePrice; }
    public int getTotalAuctions() { return totalAuctions; }
    public int getActiveAuctions() { return activeAuctions; }
    public double getTotalVolume() { return totalVolume; }
    public List<Double> getPriceHistory() { return new ArrayList<>(priceHistory); }
    public List<Integer> getVolumeHistory() { return new ArrayList<>(volumeHistory); }
    public long getLastUpdate() { return lastUpdate; }
}
