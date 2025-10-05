package de.noctivag.skyblock.utils;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import de.noctivag.skyblock.SkyblockPlugin;

/**
 * TPS (Ticks Per Second) Utility - Server performance monitoring
 * 
 * Features:
 * - Real-time TPS calculation
 * - Performance monitoring
 * - Server health indicators
 * - Lag detection
 */
public class TPSUtil {
    
    private static final long TICK_TIME = 50L; // 50ms per tick
    private static final long LAST_TICK = java.lang.System.currentTimeMillis();
    private static final long[] TICK_TIMES = new long[600]; // 30 seconds of data
    private static int tickCount = 0;
    
    /**
     * Get current TPS
     */
    public static double getTPS() {
        return getTPS(100); // Last 5 seconds
    }
    
    /**
     * Get TPS for specific time period
     */
    public static double getTPS(int ticks) {
        if (tickCount < ticks) {
            return 20.0; // Not enough data
        }
        
        long totalTime = 0;
        int startIndex = (tickCount - ticks) % TICK_TIMES.length;
        
        for (int i = 0; i < ticks; i++) {
            int index = (startIndex + i) % TICK_TIMES.length;
            totalTime += TICK_TIMES[index];
        }
        
        double averageTickTime = (double) totalTime / ticks;
        return Math.min(20.0, 1000.0 / averageTickTime);
    }
    
    /**
     * Get TPS color based on performance
     */
    public static String getTPSColor(double tps) {
        if (tps >= 18.0) {
            return "&a"; // Green - Good
        } else if (tps >= 15.0) {
            return "&e"; // Yellow - Moderate
        } else if (tps >= 10.0) {
            return "&6"; // Gold - Poor
        } else {
            return "&c"; // Red - Critical
        }
    }
    
    /**
     * Get TPS status text
     */
    public static String getTPSStatus(double tps) {
        if (tps >= 18.0) {
            return "Excellent";
        } else if (tps >= 15.0) {
            return "Good";
        } else if (tps >= 10.0) {
            return "Poor";
        } else {
            return "Critical";
        }
    }
    
    /**
     * Get colored TPS string (alias for getFormattedTPS)
     *
     * @return Colored TPS string
     */
    public static String getColoredTPS() {
        return getFormattedTPS();
    }
    
    /**
     * Check if server is lagging
     */
    public static boolean isLagging() {
        return getTPS() < 15.0;
    }
    
    /**
     * Check if server is critically lagging
     */
    public static boolean isCriticallyLagging() {
        return getTPS() < 10.0;
    }
    
    /**
     * Get server performance level
     */
    public static PerformanceLevel getPerformanceLevel() {
        double tps = getTPS();
        
        if (tps >= 19.0) {
            return PerformanceLevel.EXCELLENT;
        } else if (tps >= 17.0) {
            return PerformanceLevel.GOOD;
        } else if (tps >= 15.0) {
            return PerformanceLevel.MODERATE;
        } else if (tps >= 10.0) {
            return PerformanceLevel.POOR;
        } else {
            return PerformanceLevel.CRITICAL;
        }
    }
    
    /**
     * Get formatted TPS string
     */
    public static String getFormattedTPS() {
        double tps = getTPS();
        String color = getTPSColor(tps);
        return color + String.format("%.2f", tps) + " TPS";
    }
    
    /**
     * Get detailed performance info
     */
    public static String getPerformanceInfo() {
        double tps = getTPS();
        String color = getTPSColor(tps);
        String status = getTPSStatus(tps);
        
        return color + String.format("%.2f", tps) + " TPS &7(" + status + ")";
    }
    
    /**
     * Update tick time (called by scheduler)
     */
    public static void updateTickTime() {
        long currentTime = java.lang.System.currentTimeMillis();
        long tickTime = currentTime - LAST_TICK;
        
        TICK_TIMES[tickCount % TICK_TIMES.length] = tickTime;
        tickCount++;
    }
    
    /**
     * Tick method for scheduler
     */
    public static void tick() {
        updateTickTime();
    }
    
    /**
     * Get memory usage percentage
     */
    public static double getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return (double) usedMemory / maxMemory * 100;
    }
    
    /**
     * Get formatted memory usage
     */
    public static String getFormattedMemoryUsage() {
        double usage = getMemoryUsage();
        String color = usage > 90 ? "&c" : usage > 75 ? "&e" : "&a";
        
        return color + String.format("%.1f%%", usage) + " Memory";
    }
    
    /**
     * Performance level enum
     */
    public enum PerformanceLevel {
        EXCELLENT("&aExcellent", 19.0),
        GOOD("&aGood", 17.0),
        MODERATE("&eModerate", 15.0),
        POOR("&6Poor", 10.0),
        CRITICAL("&cCritical", 0.0);
        
        private final String displayName;
        private final double minTPS;
        
        PerformanceLevel(String displayName, double minTPS) {
            this.displayName = displayName;
            this.minTPS = minTPS;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMinTPS() { return minTPS; }
    }
}
