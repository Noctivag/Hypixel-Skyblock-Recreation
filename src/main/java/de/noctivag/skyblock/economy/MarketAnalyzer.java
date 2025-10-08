package de.noctivag.skyblock.economy;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Market Analyzer - Marktanalyse und Preisforschung
 * 
 * Verantwortlich für:
 * - Marktanalyse
 * - Preisvorhersagen
 * - Trend-Erkennung
 * - Volatilitäts-Messung
 * - Marktberichte
 * - Anomalie-Erkennung
 */
public class MarketAnalyzer {
    private final EconomySystem economySystem;
    private final Map<String, MarketData> marketData = new ConcurrentHashMap<>();
    private final Map<String, List<Double>> priceHistory = new ConcurrentHashMap<>();
    private final Map<String, List<Integer>> volumeHistory = new ConcurrentHashMap<>();
    private final Map<String, MarketTrend> trendAnalysis = new ConcurrentHashMap<>();
    
    // Analysis Configuration
    private final int HISTORY_LENGTH = 100; // Keep last 100 data points
    private final double VOLATILITY_THRESHOLD = 0.2; // 20% volatility threshold
    private final double TREND_THRESHOLD = 0.1; // 10% trend threshold
    
    public MarketAnalyzer(EconomySystem economySystem) {
        this.economySystem = economySystem;
        initializeMarketData();
    }
    
    private void initializeMarketData() {
        // Initialize market data for all items
        for (String itemId : economySystem.getAllCurrentPrices().keySet()) {
            marketData.put(itemId, new MarketData(itemId));
            priceHistory.put(itemId, new ArrayList<>());
            volumeHistory.put(itemId, new ArrayList<>());
            trendAnalysis.put(itemId, new MarketTrend());
        }
    }
    
    public void analyzeMarket() {
        // Update market data
        updateMarketData();
        
        // Analyze trends
        analyzeTrends();
        
        // Detect anomalies
        detectAnomalies();
        
        // Generate market report
        generateMarketReport();
    }
    
    private void updateMarketData() {
        for (String itemId : economySystem.getAllCurrentPrices().keySet()) {
            double currentPrice = economySystem.getCurrentPrice(itemId);
            double basePrice = economySystem.getBasePrice(itemId);
            
            // Update price history
            List<Double> prices = priceHistory.get(itemId);
            prices.add(currentPrice);
            if (prices.size() > HISTORY_LENGTH) {
                prices.remove(0);
            }
            
            // Update volume history (simulated for now)
            List<Integer> volumes = volumeHistory.get(itemId);
            int volume = generateSimulatedVolume(itemId, currentPrice);
            volumes.add(volume);
            if (volumes.size() > HISTORY_LENGTH) {
                volumes.remove(0);
            }
            
            // Update market data
            MarketData data = marketData.get(itemId);
            data.updatePrice(currentPrice);
            data.updateVolume(volume);
            data.setBasePrice(basePrice);
        }
    }
    
    private int generateSimulatedVolume(String itemId, double price) {
        // Simulate trading volume based on price and item type
        Random random = new Random();
        int baseVolume = 100;
        
        // Adjust volume based on item type
        if (itemId.contains("DIAMOND") || itemId.contains("EMERALD")) {
            baseVolume = 50; // Rare items have lower volume
        } else if (itemId.contains("COBBLESTONE") || itemId.contains("STONE")) {
            baseVolume = 500; // Common items have higher volume
        }
        
        // Adjust volume based on price
        if (price > 100) {
            baseVolume = (int) (baseVolume * 0.5); // Expensive items have lower volume
        } else if (price < 1) {
            baseVolume = (int) (baseVolume * 2); // Cheap items have higher volume
        }
        
        // Add randomness
        return baseVolume + random.nextInt(baseVolume / 2);
    }
    
    private void analyzeTrends() {
        for (String itemId : priceHistory.keySet()) {
            List<Double> prices = priceHistory.get(itemId);
            if (prices.size() < 10) continue; // Need at least 10 data points
            
            MarketTrend trend = trendAnalysis.get(itemId);
            
            // Calculate short-term trend (last 10 points)
            double shortTermTrend = calculateTrend(prices.subList(Math.max(0, prices.size() - 10), prices.size()));
            
            // Calculate long-term trend (last 50 points)
            double longTermTrend = 0.0;
            if (prices.size() >= 50) {
                longTermTrend = calculateTrend(prices.subList(prices.size() - 50, prices.size()));
            }
            
            // Calculate volatility
            double volatility = calculateVolatility(prices);
            
            // Update trend analysis
            trend.setShortTermTrend(shortTermTrend);
            trend.setLongTermTrend(longTermTrend);
            trend.setVolatility(volatility);
            trend.setTrendStrength(calculateTrendStrength(shortTermTrend, longTermTrend));
        }
    }
    
    private double calculateTrend(List<Double> prices) {
        if (prices.size() < 2) return 0.0;
        
        double firstPrice = prices.get(0);
        double lastPrice = prices.get(prices.size() - 1);
        
        return (lastPrice - firstPrice) / firstPrice;
    }
    
    private double calculateVolatility(List<Double> prices) {
        if (prices.size() < 2) return 0.0;
        
        double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = prices.stream().mapToDouble(price -> Math.pow(price - mean, 2)).average().orElse(0.0);
        
        return Math.sqrt(variance) / mean;
    }
    
    private double calculateTrendStrength(double shortTerm, double longTerm) {
        // Trend strength is based on consistency between short and long term trends
        if (Math.abs(shortTerm) < 0.01 && Math.abs(longTerm) < 0.01) {
            return 0.0; // No trend
        }
        
        double consistency = 1.0 - Math.abs(shortTerm - longTerm);
        double magnitude = Math.max(Math.abs(shortTerm), Math.abs(longTerm));
        
        return consistency * magnitude;
    }
    
    private void detectAnomalies() {
        for (String itemId : priceHistory.keySet()) {
            List<Double> prices = priceHistory.get(itemId);
            if (prices.size() < 20) continue; // Need at least 20 data points
            
            double currentPrice = prices.get(prices.size() - 1);
            double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double standardDeviation = calculateStandardDeviation(prices);
            
            // Detect price anomalies (more than 2 standard deviations from mean)
            if (Math.abs(currentPrice - averagePrice) > 2 * standardDeviation) {
                MarketData data = marketData.get(itemId);
                data.setAnomalyDetected(true);
                data.setAnomalyType(currentPrice > averagePrice ? "PRICE_SPIKE" : "PRICE_DROP");
                data.setAnomalySeverity(Math.abs(currentPrice - averagePrice) / standardDeviation);
                
                // Broadcast anomaly to admins
                broadcastAnomaly(itemId, currentPrice, averagePrice, data.getAnomalyType());
            } else {
                MarketData data = marketData.get(itemId);
                data.setAnomalyDetected(false);
            }
        }
    }
    
    private double calculateStandardDeviation(List<Double> prices) {
        double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = prices.stream().mapToDouble(price -> Math.pow(price - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
    
    private void broadcastAnomaly(String itemId, double currentPrice, double averagePrice, String anomalyType) {
        String message = "§c§lMARKET ANOMALY DETECTED!";
        message += "\n§7Item: §e" + itemId;
        message += "\n§7Current Price: §6" + currentPrice;
        message += "\n§7Average Price: §6" + averagePrice;
        message += "\n§7Type: §e" + anomalyType;
        
        // Broadcast to all online players (you might want to limit this to admins)
        Bukkit.broadcastMessage(message);
    }
    
    private void generateMarketReport() {
        // Generate market report every hour
        new BukkitRunnable() {
            @Override
            public void run() {
                generateHourlyReport();
            }
        }.runTaskTimer(economySystem.SkyblockPlugin, 0L, 20L * 60L * 60L); // Every hour
    }
    
    private void generateHourlyReport() {
        // Find top movers
        List<MarketData> topGainers = findTopGainers(5);
        List<MarketData> topLosers = findTopLosers(5);
        List<MarketData> mostVolatile = findMostVolatile(5);
        
        // Generate report
        String report = "§6§lMARKET REPORT - " + new Date().toString();
        report += "\n§7Top Gainers:";
        for (MarketData data : topGainers) {
            report += "\n§7• " + data.getItemId() + ": §a+" + String.format("%.2f", data.getPriceChange() * 100) + "%";
        }
        
        report += "\n§7Top Losers:";
        for (MarketData data : topLosers) {
            report += "\n§7• " + data.getItemId() + ": §c" + String.format("%.2f", data.getPriceChange() * 100) + "%";
        }
        
        report += "\n§7Most Volatile:";
        for (MarketData data : mostVolatile) {
            report += "\n§7• " + data.getItemId() + ": §e" + String.format("%.2f", data.getVolatility() * 100) + "%";
        }
        
        // Broadcast report
        Bukkit.broadcastMessage(report);
    }
    
    private List<MarketData> findTopGainers(int count) {
        return marketData.values().stream()
            .sorted((a, b) -> Double.compare(b.getPriceChange(), a.getPriceChange()))
            .limit(count)
            .toList();
    }
    
    private List<MarketData> findTopLosers(int count) {
        return marketData.values().stream()
            .sorted((a, b) -> Double.compare(a.getPriceChange(), b.getPriceChange()))
            .limit(count)
            .toList();
    }
    
    private List<MarketData> findMostVolatile(int count) {
        return marketData.values().stream()
            .sorted((a, b) -> Double.compare(b.getVolatility(), a.getVolatility()))
            .limit(count)
            .toList();
    }
    
    public MarketData getMarketData(String itemId) {
        return marketData.get(itemId);
    }
    
    public Map<String, MarketData> getAllMarketData() {
        return new HashMap<>(marketData);
    }
    
    public MarketTrend getTrendAnalysis(String itemId) {
        return trendAnalysis.get(itemId);
    }
    
    public List<Double> getPriceHistory(String itemId) {
        return new ArrayList<>(priceHistory.getOrDefault(itemId, new ArrayList<>()));
    }
    
    public List<Integer> getVolumeHistory(String itemId) {
        return new ArrayList<>(volumeHistory.getOrDefault(itemId, new ArrayList<>()));
    }
    
    // Market Data Class
    public static class MarketData {
        private double currentPrice;
        private double basePrice;
        private double previousPrice;
        private double priceChange;
        private double volatility;
        private boolean anomalyDetected;
        private String anomalyType;
        private double anomalySeverity;
        private final long lastUpdated;
        private int currentVolume;

        public MarketData(String itemId) {
            super(itemId, 0.0, 0);
            this.currentPrice = 0.0;
            this.basePrice = 0.0;
            this.previousPrice = 0.0;
            this.priceChange = 0.0;
            this.currentVolume = 0;
            this.volatility = 0.0;
            this.anomalyDetected = false;
            this.anomalyType = null;
            this.anomalySeverity = 0.0;
            this.lastUpdated = java.lang.System.currentTimeMillis();
        }
        
        public void updatePrice(double newPrice) {
            this.previousPrice = this.currentPrice;
            this.currentPrice = newPrice;
            this.priceChange = this.previousPrice > 0 ? (newPrice - this.previousPrice) / this.previousPrice : 0.0;
        }
        
        public void updateVolume(int newVolume) {
            this.currentVolume = newVolume;
        }
        
        public void setBasePrice(double basePrice) {
            this.basePrice = basePrice;
        }
        
        public void setVolatility(double volatility) {
            this.volatility = volatility;
        }
        
        public void setAnomalyDetected(boolean anomalyDetected) {
            this.anomalyDetected = anomalyDetected;
        }
        
        public void setAnomalyType(String anomalyType) {
            this.anomalyType = anomalyType;
        }
        
        public void setAnomalySeverity(double anomalySeverity) {
            this.anomalySeverity = anomalySeverity;
        }
        
        // Getters
        public String getItemId() { return itemId; }
        public double getCurrentPrice() { return currentPrice; }
        public double getBasePrice() { return basePrice; }
        public double getPreviousPrice() { return previousPrice; }
        public double getPriceChange() { return priceChange; }
        public int getCurrentVolume() { return currentVolume; }
        public double getVolatility() { return volatility; }
        public boolean isAnomalyDetected() { return anomalyDetected; }
        public String getAnomalyType() { return anomalyType; }
        public double getAnomalySeverity() { return anomalySeverity; }
        public long getLastUpdated() { return lastUpdated; }
    }
    
    // Market Trend Class
    public static class MarketTrend {
        private double shortTermTrend;
        private double longTermTrend;
        private double volatility;
        private double trendStrength;
        private String trendDirection;
        
        public MarketTrend() {
            this.shortTermTrend = 0.0;
            this.longTermTrend = 0.0;
            this.volatility = 0.0;
            this.trendStrength = 0.0;
            this.trendDirection = "NEUTRAL";
        }
        
        public void setShortTermTrend(double shortTermTrend) {
            this.shortTermTrend = shortTermTrend;
            updateTrendDirection();
        }
        
        public void setLongTermTrend(double longTermTrend) {
            this.longTermTrend = longTermTrend;
            updateTrendDirection();
        }
        
        public void setVolatility(double volatility) {
            this.volatility = volatility;
        }
        
        public void setTrendStrength(double trendStrength) {
            this.trendStrength = trendStrength;
        }
        
        private void updateTrendDirection() {
            if (Math.abs(shortTermTrend) < 0.01) {
                this.trendDirection = "NEUTRAL";
            } else if (shortTermTrend > 0) {
                this.trendDirection = "BULLISH";
            } else {
                this.trendDirection = "BEARISH";
            }
        }
        
        // Getters
        public double getShortTermTrend() { return shortTermTrend; }
        public double getLongTermTrend() { return longTermTrend; }
        public double getVolatility() { return volatility; }
        public double getTrendStrength() { return trendStrength; }
        public String getTrendDirection() { return trendDirection; }
    }
}
