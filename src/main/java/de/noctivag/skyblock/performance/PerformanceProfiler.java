package de.noctivag.skyblock.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Performance Profiler - Detaillierte Performance-Analyse und Profiling
 * 
 * Verantwortlich für:
 * - Method-Level Performance Tracking
 * - Memory Usage Monitoring
 * - CPU Usage Analysis
 * - Garbage Collection Monitoring
 * - Thread Performance Analysis
 * - System Resource Monitoring
 */
public class PerformanceProfiler {
    
    private final SkyblockPlugin plugin;
    private final ConcurrentHashMap<String, MethodProfile> methodProfiles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SystemProfile> systemProfiles = new ConcurrentHashMap<>();
    
    private final LongAdder totalMethodCalls = new LongAdder();
    private final LongAdder totalExecutionTime = new LongAdder();
    private final AtomicLong peakMemoryUsage = new AtomicLong(0);
    private final AtomicLong peakThreadCount = new AtomicLong(0);
    
    private volatile boolean profilingEnabled = false;
    private volatile long profilingStartTime = 0;
    
    public PerformanceProfiler(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Startet das Performance-Profiling
     */
    public void startProfiling() {
        profilingEnabled = true;
        profilingStartTime = System.currentTimeMillis();
        methodProfiles.clear();
        systemProfiles.clear();
        totalMethodCalls.reset();
        totalExecutionTime.reset();
        
        plugin.getLogger().info("Performance profiling started");
    }
    
    /**
     * Stoppt das Performance-Profiling
     */
    public void stopProfiling() {
        profilingEnabled = false;
        long profilingDuration = System.currentTimeMillis() - profilingStartTime;
        
        plugin.getLogger().info("Performance profiling stopped after " + profilingDuration + "ms");
        generateProfilingReport();
    }
    
    /**
     * Profiliert eine Methode
     */
    public <T> T profileMethod(String methodName, java.util.function.Supplier<T> method) {
        if (!profilingEnabled) {
            return method.get();
        }
        
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();
        int startThreads = Thread.activeCount();
        
        try {
            T result = method.get();
            return result;
        } finally {
            long endTime = System.nanoTime();
            long endMemory = getUsedMemory();
            int endThreads = Thread.activeCount();
            
            recordMethodExecution(methodName, endTime - startTime, endMemory - startMemory, 
                                Math.max(endThreads - startThreads, 0));
        }
    }
    
    /**
     * Profiliert eine Methode ohne Rückgabewert
     */
    public void profileMethod(String methodName, Runnable method) {
        if (!profilingEnabled) {
            method.run();
            return;
        }
        
        long startTime = System.nanoTime();
        long startMemory = getUsedMemory();
        int startThreads = Thread.activeCount();
        
        try {
            method.run();
        } finally {
            long endTime = System.nanoTime();
            long endMemory = getUsedMemory();
            int endThreads = Thread.activeCount();
            
            recordMethodExecution(methodName, endTime - startTime, endMemory - startMemory, 
                                Math.max(endThreads - startThreads, 0));
        }
    }
    
    /**
     * Zeichnet Methoden-Ausführung auf
     */
    private void recordMethodExecution(String methodName, long executionTime, long memoryDelta, int threadDelta) {
        MethodProfile profile = methodProfiles.computeIfAbsent(methodName, k -> new MethodProfile());
        
        profile.incrementCalls();
        profile.addExecutionTime(executionTime);
        profile.addMemoryUsage(memoryDelta);
        profile.addThreadUsage(threadDelta);
        
        totalMethodCalls.increment();
        totalExecutionTime.add(executionTime);
        
        // Update peaks
        long currentMemory = getUsedMemory();
        if (currentMemory > peakMemoryUsage.get()) {
            peakMemoryUsage.set(currentMemory);
        }
        
        int currentThreads = Thread.activeCount();
        if (currentThreads > peakThreadCount.get()) {
            peakThreadCount.set(currentThreads);
        }
    }
    
    /**
     * Profiliert System-Ressourcen
     */
    public void profileSystemResources() {
        if (!profilingEnabled) {
            return;
        }
        
        SystemProfile profile = new SystemProfile();
        
        // Memory profiling
        Runtime runtime = Runtime.getRuntime();
        profile.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        profile.setMaxMemory(runtime.maxMemory());
        profile.setFreeMemory(runtime.freeMemory());
        
        // Thread profiling
        profile.setActiveThreads(Thread.activeCount());
        profile.setPeakThreads(peakThreadCount.get());
        
        // CPU profiling (approximation)
        profile.setCpuUsage(getCpuUsage());
        
        // GC profiling
        profile.setGcCount(getGcCount());
        profile.setGcTime(getGcTime());
        
        systemProfiles.put(String.valueOf(System.currentTimeMillis()), profile);
    }
    
    /**
     * Generiert einen Profiling-Report
     */
    private void generateProfilingReport() {
        plugin.getLogger().info("=== Performance Profiling Report ===");
        
        // Method-level statistics
        plugin.getLogger().info("Method Performance:");
        methodProfiles.entrySet().stream()
            .sorted((e1, e2) -> Long.compare(e2.getValue().getTotalExecutionTime(), e1.getValue().getTotalExecutionTime()))
            .limit(10)
            .forEach(entry -> {
                MethodProfile profile = entry.getValue();
                plugin.getLogger().info(String.format("  %s: %d calls, %.2fms avg, %.2fms total, %d bytes memory",
                    entry.getKey(),
                    profile.getCallCount(),
                    profile.getAverageExecutionTime() / 1_000_000.0,
                    profile.getTotalExecutionTime() / 1_000_000.0,
                    profile.getTotalMemoryUsage()));
            });
        
        // System statistics
        plugin.getLogger().info("System Performance:");
        plugin.getLogger().info("  Total method calls: " + totalMethodCalls.sum());
        plugin.getLogger().info("  Total execution time: " + (totalExecutionTime.sum() / 1_000_000.0) + "ms");
        plugin.getLogger().info("  Peak memory usage: " + (peakMemoryUsage.get() / 1024 / 1024) + "MB");
        plugin.getLogger().info("  Peak thread count: " + peakThreadCount.get());
        
        // Current system state
        Runtime runtime = Runtime.getRuntime();
        plugin.getLogger().info("  Current memory usage: " + ((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024) + "MB");
        plugin.getLogger().info("  Current thread count: " + Thread.activeCount());
        plugin.getLogger().info("  Available processors: " + runtime.availableProcessors());
    }
    
    /**
     * Gibt detaillierte Performance-Metriken zurück
     */
    public PerformanceMetrics getDetailedMetrics() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        
        // Method metrics
        metrics.setTotalMethodCalls(totalMethodCalls.sum());
        metrics.setTotalExecutionTime(totalExecutionTime.sum());
        metrics.setAverageExecutionTime(totalMethodCalls.sum() > 0 ? 
            totalExecutionTime.sum() / totalMethodCalls.sum() : 0);
        
        // System metrics
        Runtime runtime = Runtime.getRuntime();
        metrics.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
        metrics.setMaxMemory(runtime.maxMemory());
        metrics.setPeakMemoryUsage(peakMemoryUsage.get());
        metrics.setActiveThreads(Thread.activeCount());
        metrics.setPeakThreadCount(peakThreadCount.get());
        metrics.setAvailableProcessors(runtime.availableProcessors());
        
        // Method profiles
        metrics.setMethodProfiles(new ConcurrentHashMap<>(methodProfiles));
        
        return metrics;
    }
    
    /**
     * Gibt Method-Profile zurück
     */
    public ConcurrentHashMap<String, MethodProfile> getMethodProfiles() {
        return new ConcurrentHashMap<>(methodProfiles);
    }
    
    /**
     * Gibt System-Profile zurück
     */
    public ConcurrentHashMap<String, SystemProfile> getSystemProfiles() {
        return new ConcurrentHashMap<>(systemProfiles);
    }
    
    /**
     * Prüft ob Profiling aktiv ist
     */
    public boolean isProfilingEnabled() {
        return profilingEnabled;
    }
    
    /**
     * Gibt verwendeten Speicher zurück
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Gibt CPU-Usage zurück (approximation)
     */
    private double getCpuUsage() {
        // Vereinfachte CPU-Usage-Berechnung
        // In einer echten Implementierung würde man JMX oder andere APIs verwenden
        return 0.0; // Placeholder
    }
    
    /**
     * Gibt GC-Count zurück
     */
    private long getGcCount() {
        // Vereinfachte GC-Count-Berechnung
        // In einer echten Implementierung würde man JMX verwenden
        return 0; // Placeholder
    }
    
    /**
     * Gibt GC-Time zurück
     */
    private long getGcTime() {
        // Vereinfachte GC-Time-Berechnung
        // In einer echten Implementierung würde man JMX verwenden
        return 0; // Placeholder
    }
    
    /**
     * Method Profile Klasse
     */
    public static class MethodProfile {
        private final LongAdder callCount = new LongAdder();
        private final LongAdder totalExecutionTime = new LongAdder();
        private final LongAdder totalMemoryUsage = new LongAdder();
        private final LongAdder totalThreadUsage = new LongAdder();
        private final AtomicLong minExecutionTime = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong maxExecutionTime = new AtomicLong(Long.MIN_VALUE);
        
        public void incrementCalls() {
            callCount.increment();
        }
        
        public void addExecutionTime(long executionTime) {
            totalExecutionTime.add(executionTime);
            
            // Update min/max
            long currentMin = minExecutionTime.get();
            while (executionTime < currentMin && !minExecutionTime.compareAndSet(currentMin, executionTime)) {
                currentMin = minExecutionTime.get();
            }
            
            long currentMax = maxExecutionTime.get();
            while (executionTime > currentMax && !maxExecutionTime.compareAndSet(currentMax, executionTime)) {
                currentMax = maxExecutionTime.get();
            }
        }
        
        public void addMemoryUsage(long memoryUsage) {
            totalMemoryUsage.add(Math.abs(memoryUsage));
        }
        
        public void addThreadUsage(int threadUsage) {
            totalThreadUsage.add(threadUsage);
        }
        
        public long getCallCount() {
            return callCount.sum();
        }
        
        public long getTotalExecutionTime() {
            return totalExecutionTime.sum();
        }
        
        public double getAverageExecutionTime() {
            long calls = callCount.sum();
            return calls > 0 ? (double) totalExecutionTime.sum() / calls : 0.0;
        }
        
        public long getMinExecutionTime() {
            long min = minExecutionTime.get();
            return min == Long.MAX_VALUE ? 0 : min;
        }
        
        public long getMaxExecutionTime() {
            long max = maxExecutionTime.get();
            return max == Long.MIN_VALUE ? 0 : max;
        }
        
        public long getTotalMemoryUsage() {
            return totalMemoryUsage.sum();
        }
        
        public long getTotalThreadUsage() {
            return totalThreadUsage.sum();
        }
    }
    
    /**
     * System Profile Klasse
     */
    public static class SystemProfile {
        private long usedMemory;
        private long maxMemory;
        private long freeMemory;
        private int activeThreads;
        private long peakThreads;
        private double cpuUsage;
        private long gcCount;
        private long gcTime;
        private long timestamp;
        
        public SystemProfile() {
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters und Setters
        public long getUsedMemory() { return usedMemory; }
        public void setUsedMemory(long usedMemory) { this.usedMemory = usedMemory; }
        
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        
        public long getFreeMemory() { return freeMemory; }
        public void setFreeMemory(long freeMemory) { this.freeMemory = freeMemory; }
        
        public int getActiveThreads() { return activeThreads; }
        public void setActiveThreads(int activeThreads) { this.activeThreads = activeThreads; }
        
        public long getPeakThreads() { return peakThreads; }
        public void setPeakThreads(long peakThreads) { this.peakThreads = peakThreads; }
        
        public double getCpuUsage() { return cpuUsage; }
        public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
        
        public long getGcCount() { return gcCount; }
        public void setGcCount(long gcCount) { this.gcCount = gcCount; }
        
        public long getGcTime() { return gcTime; }
        public void setGcTime(long gcTime) { this.gcTime = gcTime; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
    
    /**
     * Performance Metrics Klasse
     */
    public static class PerformanceMetrics {
        private long totalMethodCalls;
        private long totalExecutionTime;
        private long averageExecutionTime;
        private long usedMemory;
        private long maxMemory;
        private long peakMemoryUsage;
        private int activeThreads;
        private long peakThreadCount;
        private int availableProcessors;
        private ConcurrentHashMap<String, MethodProfile> methodProfiles;
        
        // Getters und Setters
        public long getTotalMethodCalls() { return totalMethodCalls; }
        public void setTotalMethodCalls(long totalMethodCalls) { this.totalMethodCalls = totalMethodCalls; }
        
        public long getTotalExecutionTime() { return totalExecutionTime; }
        public void setTotalExecutionTime(long totalExecutionTime) { this.totalExecutionTime = totalExecutionTime; }
        
        public long getAverageExecutionTime() { return averageExecutionTime; }
        public void setAverageExecutionTime(long averageExecutionTime) { this.averageExecutionTime = averageExecutionTime; }
        
        public long getUsedMemory() { return usedMemory; }
        public void setUsedMemory(long usedMemory) { this.usedMemory = usedMemory; }
        
        public long getMaxMemory() { return maxMemory; }
        public void setMaxMemory(long maxMemory) { this.maxMemory = maxMemory; }
        
        public long getPeakMemoryUsage() { return peakMemoryUsage; }
        public void setPeakMemoryUsage(long peakMemoryUsage) { this.peakMemoryUsage = peakMemoryUsage; }
        
        public int getActiveThreads() { return activeThreads; }
        public void setActiveThreads(int activeThreads) { this.activeThreads = activeThreads; }
        
        public long getPeakThreadCount() { return peakThreadCount; }
        public void setPeakThreadCount(long peakThreadCount) { this.peakThreadCount = peakThreadCount; }
        
        public int getAvailableProcessors() { return availableProcessors; }
        public void setAvailableProcessors(int availableProcessors) { this.availableProcessors = availableProcessors; }
        
        public ConcurrentHashMap<String, MethodProfile> getMethodProfiles() { return methodProfiles; }
        public void setMethodProfiles(ConcurrentHashMap<String, MethodProfile> methodProfiles) { this.methodProfiles = methodProfiles; }
    }
}
