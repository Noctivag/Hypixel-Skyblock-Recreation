package de.noctivag.skyblock.engine.rte;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents price history data for an item
 */
public class PriceHistory {
    private final String itemId;
    private final List<PricePoint> pricePoints;
    private final Map<String, Object> metadata;

    public PriceHistory(String itemId) {
        this.itemId = itemId;
        this.pricePoints = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public String getItemId() {
        return itemId;
    }

    public List<PricePoint> getPricePoints() {
        return new ArrayList<>(pricePoints);
    }

    public void addPricePoint(PricePoint pricePoint) {
        pricePoints.add(pricePoint);
        // Keep only last 1000 points to prevent memory issues
        if (pricePoints.size() > 1000) {
            pricePoints.remove(0);
        }
    }

    public void addPricePoint(LocalDateTime timestamp, double sellPrice, double buyPrice, long volume) {
        addPricePoint(new PricePoint(timestamp, sellPrice, buyPrice, volume));
    }

    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public PricePoint getLatestPrice() {
        if (pricePoints.isEmpty()) return null;
        return pricePoints.get(pricePoints.size() - 1);
    }

    public PricePoint getOldestPrice() {
        if (pricePoints.isEmpty()) return null;
        return pricePoints.get(0);
    }

    public List<PricePoint> getPricesInRange(LocalDateTime start, LocalDateTime end) {
        return pricePoints.stream()
                .filter(p -> !p.getTimestamp().isBefore(start) && !p.getTimestamp().isAfter(end))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public double getAveragePrice(LocalDateTime start, LocalDateTime end) {
        List<PricePoint> range = getPricesInRange(start, end);
        if (range.isEmpty()) return 0.0;
        
        return range.stream()
                .mapToDouble(p -> (p.getSellPrice() + p.getBuyPrice()) / 2.0)
                .average()
                .orElse(0.0);
    }

    public double getPriceChange(LocalDateTime start, LocalDateTime end) {
        List<PricePoint> range = getPricesInRange(start, end);
        if (range.size() < 2) return 0.0;
        
        PricePoint first = range.get(0);
        PricePoint last = range.get(range.size() - 1);
        
        double firstAvg = (first.getSellPrice() + first.getBuyPrice()) / 2.0;
        double lastAvg = (last.getSellPrice() + last.getBuyPrice()) / 2.0;
        
        return lastAvg - firstAvg;
    }

    public double getPriceVolatility(LocalDateTime start, LocalDateTime end) {
        List<PricePoint> range = getPricesInRange(start, end);
        if (range.size() < 2) return 0.0;
        
        double mean = range.stream()
                .mapToDouble(p -> (p.getSellPrice() + p.getBuyPrice()) / 2.0)
                .average()
                .orElse(0.0);
        
        double variance = range.stream()
                .mapToDouble(p -> Math.pow((p.getSellPrice() + p.getBuyPrice()) / 2.0 - mean, 2))
                .average()
                .orElse(0.0);
        
        return Math.sqrt(variance);
    }

    public long getTotalVolume(LocalDateTime start, LocalDateTime end) {
        return getPricesInRange(start, end).stream()
                .mapToLong(PricePoint::getVolume)
                .sum();
    }

    /**
     * Represents a single price point in time
     */
    public static class PricePoint {
        private final LocalDateTime timestamp;
        private final double sellPrice;
        private final double buyPrice;
        private final long volume;

        public PricePoint(LocalDateTime timestamp, double sellPrice, double buyPrice, long volume) {
            this.timestamp = timestamp;
            this.sellPrice = sellPrice;
            this.buyPrice = buyPrice;
            this.volume = volume;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getSellPrice() {
            return sellPrice;
        }

        public double getBuyPrice() {
            return buyPrice;
        }

        public long getVolume() {
            return volume;
        }

        public double getAveragePrice() {
            return (sellPrice + buyPrice) / 2.0;
        }

        public double getSpread() {
            return buyPrice - sellPrice;
        }

        public double getSpreadPercentage() {
            return (getSpread() / getAveragePrice()) * 100.0;
        }

        @Override
        public String toString() {
            return String.format("PricePoint{timestamp=%s, sell=%.2f, buy=%.2f, volume=%d}", 
                               timestamp, sellPrice, buyPrice, volume);
        }
    }
}
