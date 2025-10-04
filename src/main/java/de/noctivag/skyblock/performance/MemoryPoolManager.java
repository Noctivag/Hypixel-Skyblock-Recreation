package de.noctivag.skyblock.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Level;

/**
 * Memory Pool Manager - Intelligentes Speicher-Management
 * 
 * Verantwortlich für:
 * - Object Pooling
 * - Memory Pre-allocation
 * - Garbage Collection Optimization
 * - Memory Leak Detection
 * - Buffer Management
 * - Cache Management
 */
public class MemoryPoolManager {
    
    private final SkyblockPlugin plugin;
    private final ConcurrentHashMap<Class<?>, ObjectPool<?>> objectPools = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BufferPool> bufferPools = new ConcurrentHashMap<>();
    
    private final LongAdder totalObjectsCreated = new LongAdder();
    private final LongAdder totalObjectsReused = new LongAdder();
    private final LongAdder totalMemoryAllocated = new LongAdder();
    private final LongAdder totalMemoryFreed = new LongAdder();
    
    private final AtomicLong peakMemoryUsage = new AtomicLong(0);
    private final AtomicLong currentMemoryUsage = new AtomicLong(0);
    
    private volatile boolean poolingEnabled = true;
    private volatile long lastGcTime = 0;
    
    public MemoryPoolManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeDefaultPools();
    }
    
    /**
     * Initialisiert Standard-Pools
     */
    private void initializeDefaultPools() {
        // String Pool
        createObjectPool(String.class, 1000, () -> new String());
        
        // StringBuilder Pool
        createObjectPool(StringBuilder.class, 500, () -> new StringBuilder(256));
        
        // ArrayList Pool
        createObjectPool(java.util.ArrayList.class, 200, () -> new java.util.ArrayList<>(16));
        
        // HashMap Pool
        createObjectPool(java.util.HashMap.class, 200, () -> new java.util.HashMap<>(16));
        
        // Byte Array Pool
        createBufferPool("byte-1024", 1024, 100);
        createBufferPool("byte-4096", 4096, 50);
        createBufferPool("byte-16384", 16384, 25);
        
        // Char Array Pool
        createBufferPool("char-1024", 1024, 100);
        createBufferPool("char-4096", 4096, 50);
        
        plugin.getLogger().info("Memory Pool Manager initialized with default pools");
    }
    
    /**
     * Erstellt einen Object Pool
     */
    public <T> void createObjectPool(Class<T> clazz, int maxSize, java.util.function.Supplier<T> factory) {
        ObjectPool<T> pool = new ObjectPool<>(maxSize, factory);
        objectPools.put(clazz, pool);
        plugin.getLogger().info("Created object pool for " + clazz.getSimpleName() + " with max size " + maxSize);
    }
    
    /**
     * Erstellt einen Buffer Pool
     */
    public void createBufferPool(String name, int bufferSize, int maxBuffers) {
        BufferPool pool = new BufferPool(bufferSize, maxBuffers);
        bufferPools.put(name, pool);
        plugin.getLogger().info("Created buffer pool '" + name + "' with size " + bufferSize + " and max buffers " + maxBuffers);
    }
    
    /**
     * Holt ein Objekt aus dem Pool
     */
    @SuppressWarnings("unchecked")
    public <T> T borrowObject(Class<T> clazz) {
        if (!poolingEnabled) {
            return createNewObject(clazz);
        }
        
        ObjectPool<T> pool = (ObjectPool<T>) objectPools.get(clazz);
        if (pool != null) {
            T object = pool.borrow();
            if (object != null) {
                totalObjectsReused.increment();
                return object;
            }
        }
        
        T object = createNewObject(clazz);
        totalObjectsCreated.increment();
        return object;
    }
    
    /**
     * Gibt ein Objekt an den Pool zurück
     */
    @SuppressWarnings("unchecked")
    public <T> void returnObject(Class<T> clazz, T object) {
        if (!poolingEnabled || object == null) {
            return;
        }
        
        ObjectPool<T> pool = (ObjectPool<T>) objectPools.get(clazz);
        if (pool != null) {
            if (pool.returnObject(object)) {
                // Object successfully returned to pool
            } else {
                // Pool is full, object will be garbage collected
            }
        }
    }
    
    /**
     * Holt einen Buffer aus dem Pool
     */
    public byte[] borrowBuffer(String poolName) {
        if (!poolingEnabled) {
            return new byte[getBufferSize(poolName)];
        }
        
        BufferPool pool = bufferPools.get(poolName);
        if (pool != null) {
            byte[] buffer = pool.borrow();
            if (buffer != null) {
                return buffer;
            }
        }
        
        return new byte[getBufferSize(poolName)];
    }
    
    /**
     * Gibt einen Buffer an den Pool zurück
     */
    public void returnBuffer(String poolName, byte[] buffer) {
        if (!poolingEnabled || buffer == null) {
            return;
        }
        
        BufferPool pool = bufferPools.get(poolName);
        if (pool != null) {
            pool.returnBuffer(buffer);
        }
    }
    
    /**
     * Holt einen Char Buffer aus dem Pool
     */
    public char[] borrowCharBuffer(String poolName) {
        if (!poolingEnabled) {
            return new char[getBufferSize(poolName)];
        }
        
        BufferPool pool = bufferPools.get(poolName);
        if (pool != null) {
            char[] buffer = pool.borrowChar();
            if (buffer != null) {
                return buffer;
            }
        }
        
        return new char[getBufferSize(poolName)];
    }
    
    /**
     * Gibt einen Char Buffer an den Pool zurück
     */
    public void returnCharBuffer(String poolName, char[] buffer) {
        if (!poolingEnabled || buffer == null) {
            return;
        }
        
        BufferPool pool = bufferPools.get(poolName);
        if (pool != null) {
            pool.returnCharBuffer(buffer);
        }
    }
    
    /**
     * Erstellt ein neues Objekt
     */
    @SuppressWarnings("unchecked")
    private <T> T createNewObject(Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return (T) new String();
            } else if (clazz == StringBuilder.class) {
                return (T) new StringBuilder(256);
            } else if (clazz == java.util.ArrayList.class) {
                return (T) new java.util.ArrayList<>(16);
            } else if (clazz == java.util.HashMap.class) {
                return (T) new java.util.HashMap<>(16);
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to create new object of type " + clazz.getSimpleName(), e);
            return null;
        }
    }
    
    /**
     * Gibt die Buffer-Größe zurück
     */
    private int getBufferSize(String poolName) {
        if (poolName.contains("1024")) return 1024;
        if (poolName.contains("4096")) return 4096;
        if (poolName.contains("16384")) return 16384;
        return 1024; // Default
    }
    
    /**
     * Führt Garbage Collection aus
     */
    public void performGarbageCollection() {
        long startTime = System.currentTimeMillis();
        long startMemory = getUsedMemory();
        
        System.gc();
        
        long endTime = System.currentTimeMillis();
        long endMemory = getUsedMemory();
        
        long freedMemory = startMemory - endMemory;
        totalMemoryFreed.add(freedMemory);
        lastGcTime = endTime;
        
        plugin.getLogger().info("Garbage collection completed in " + (endTime - startTime) + "ms, freed " + 
            (freedMemory / 1024 / 1024) + "MB");
    }
    
    /**
     * Überwacht Memory Usage
     */
    public void monitorMemoryUsage() {
        long currentMemory = getUsedMemory();
        currentMemoryUsage.set(currentMemory);
        
        if (currentMemory > peakMemoryUsage.get()) {
            peakMemoryUsage.set(currentMemory);
        }
        
        // Trigger GC if memory usage is high
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (currentMemory > maxMemory * 0.8) { // 80% of max memory
            plugin.getLogger().warning("High memory usage detected: " + (currentMemory / 1024 / 1024) + "MB / " + 
                (maxMemory / 1024 / 1024) + "MB");
            performGarbageCollection();
        }
    }
    
    /**
     * Gibt Memory Statistics zurück
     */
    public MemoryStatistics getMemoryStatistics() {
        MemoryStatistics stats = new MemoryStatistics();
        
        stats.setTotalObjectsCreated(totalObjectsCreated.sum());
        stats.setTotalObjectsReused(totalObjectsReused.sum());
        stats.setTotalMemoryAllocated(totalMemoryAllocated.sum());
        stats.setTotalMemoryFreed(totalMemoryFreed.sum());
        stats.setPeakMemoryUsage(peakMemoryUsage.get());
        stats.setCurrentMemoryUsage(currentMemoryUsage.get());
        stats.setLastGcTime(lastGcTime);
        
        // Pool statistics
        stats.setObjectPoolCount(objectPools.size());
        stats.setBufferPoolCount(bufferPools.size());
        
        // Calculate reuse rate
        long totalObjects = totalObjectsCreated.sum() + totalObjectsReused.sum();
        if (totalObjects > 0) {
            stats.setObjectReuseRate((double) totalObjectsReused.sum() / totalObjects);
        }
        
        return stats;
    }
    
    /**
     * Gibt verwendeten Speicher zurück
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    /**
     * Aktiviert/Deaktiviert Pooling
     */
    public void setPoolingEnabled(boolean enabled) {
        this.poolingEnabled = enabled;
        plugin.getLogger().info("Object pooling " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Schließt alle Pools
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down Memory Pool Manager...");
        
        objectPools.clear();
        bufferPools.clear();
        
        // Final garbage collection
        performGarbageCollection();
        
        plugin.getLogger().info("Memory Pool Manager shutdown complete");
    }
    
    /**
     * Object Pool Implementierung
     */
    private static class ObjectPool<T> {
        private final java.util.Queue<T> pool = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final int maxSize;
        private final AtomicLong reused = new AtomicLong(0);
        
        public ObjectPool(int maxSize, java.util.function.Supplier<T> factory) {
            this.maxSize = maxSize;
        }
        
        public T borrow() {
            T object = pool.poll();
            if (object != null) {
                reused.incrementAndGet();
                return object;
            }
            return null;
        }
        
        public boolean returnObject(T object) {
            if (pool.size() < maxSize) {
                pool.offer(object);
                return true;
            }
            return false;
        }
        
    }
    
    /**
     * Buffer Pool Implementierung
     */
    private static class BufferPool {
        private final java.util.Queue<byte[]> bytePool = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final java.util.Queue<char[]> charPool = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final int bufferSize;
        private final int maxBuffers;
        
        public BufferPool(int bufferSize, int maxBuffers) {
            this.bufferSize = bufferSize;
            this.maxBuffers = maxBuffers;
        }
        
        public byte[] borrow() {
            return bytePool.poll();
        }
        
        public char[] borrowChar() {
            return charPool.poll();
        }
        
        public void returnBuffer(byte[] buffer) {
            if (buffer != null && buffer.length == bufferSize && bytePool.size() < maxBuffers) {
                // Clear buffer
                java.util.Arrays.fill(buffer, (byte) 0);
                bytePool.offer(buffer);
            }
        }
        
        public void returnCharBuffer(char[] buffer) {
            if (buffer != null && buffer.length == bufferSize && charPool.size() < maxBuffers) {
                // Clear buffer
                java.util.Arrays.fill(buffer, '\0');
                charPool.offer(buffer);
            }
        }
        
    }
    
    /**
     * Memory Statistics Klasse
     */
    public static class MemoryStatistics {
        private long totalObjectsCreated;
        private long totalObjectsReused;
        private long totalMemoryAllocated;
        private long totalMemoryFreed;
        private long peakMemoryUsage;
        private long currentMemoryUsage;
        private long lastGcTime;
        private int objectPoolCount;
        private int bufferPoolCount;
        private double objectReuseRate;
        
        // Getters und Setters
        public long getTotalObjectsCreated() { return totalObjectsCreated; }
        public void setTotalObjectsCreated(long totalObjectsCreated) { this.totalObjectsCreated = totalObjectsCreated; }
        
        public long getTotalObjectsReused() { return totalObjectsReused; }
        public void setTotalObjectsReused(long totalObjectsReused) { this.totalObjectsReused = totalObjectsReused; }
        
        public long getTotalMemoryAllocated() { return totalMemoryAllocated; }
        public void setTotalMemoryAllocated(long totalMemoryAllocated) { this.totalMemoryAllocated = totalMemoryAllocated; }
        
        public long getTotalMemoryFreed() { return totalMemoryFreed; }
        public void setTotalMemoryFreed(long totalMemoryFreed) { this.totalMemoryFreed = totalMemoryFreed; }
        
        public long getPeakMemoryUsage() { return peakMemoryUsage; }
        public void setPeakMemoryUsage(long peakMemoryUsage) { this.peakMemoryUsage = peakMemoryUsage; }
        
        public long getCurrentMemoryUsage() { return currentMemoryUsage; }
        public void setCurrentMemoryUsage(long currentMemoryUsage) { this.currentMemoryUsage = currentMemoryUsage; }
        
        public long getLastGcTime() { return lastGcTime; }
        public void setLastGcTime(long lastGcTime) { this.lastGcTime = lastGcTime; }
        
        public int getObjectPoolCount() { return objectPoolCount; }
        public void setObjectPoolCount(int objectPoolCount) { this.objectPoolCount = objectPoolCount; }
        
        public int getBufferPoolCount() { return bufferPoolCount; }
        public void setBufferPoolCount(int bufferPoolCount) { this.bufferPoolCount = bufferPoolCount; }
        
        public double getObjectReuseRate() { return objectReuseRate; }
        public void setObjectReuseRate(double objectReuseRate) { this.objectReuseRate = objectReuseRate; }
    }
}
