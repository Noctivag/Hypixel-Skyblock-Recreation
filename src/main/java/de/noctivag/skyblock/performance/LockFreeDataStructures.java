package de.noctivag.skyblock.performance;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

/**
 * Lock-Free Data Structures - Hochperformante, lock-freie Datenstrukturen
 * 
 * Verantwortlich für:
 * - Lock-Free Collections
 * - Atomic Operations
 * - High-Performance Caching
 * - Memory-Efficient Data Structures
 * - Thread-Safe Operations ohne Locks
 */
public class LockFreeDataStructures {
    
    /**
     * Lock-Free Cache mit LRU-Eviction
     */
    public static class LockFreeCache<K, V> {
        private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
        private final ConcurrentLinkedQueue<K> accessOrder = new ConcurrentLinkedQueue<>();
        private final AtomicInteger size = new AtomicInteger(0);
        private final AtomicLong hits = new AtomicLong(0);
        private final AtomicLong misses = new AtomicLong(0);
        private final int maxSize;
        
        public LockFreeCache(int maxSize) {
            this.maxSize = maxSize;
        }
        
        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null) {
                hits.incrementAndGet();
                // Update access order (lock-free)
                accessOrder.offer(key); // Add to end
                return entry.getValue();
            }
            misses.incrementAndGet();
            return null;
        }
        
        public V put(K key, V value) {
            CacheEntry<V> newEntry = new CacheEntry<>(value, java.lang.System.currentTimeMillis());
            CacheEntry<V> oldEntry = cache.put(key, newEntry);
            
            if (oldEntry == null) {
                int currentSize = size.incrementAndGet();
                if (currentSize > maxSize) {
                    evictLRU();
                }
            }
            
            accessOrder.offer(key);
            return oldEntry != null ? oldEntry.getValue() : null;
        }
        
        public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null) {
                hits.incrementAndGet();
                accessOrder.offer(key);
                return entry.getValue();
            }
            
            V value = mappingFunction.apply(key);
            if (value != null) {
                put(key, value);
            }
            return value;
        }
        
        private void evictLRU() {
            K keyToEvict = accessOrder.poll();
            if (keyToEvict != null) {
                cache.remove(keyToEvict);
                size.decrementAndGet();
            }
        }
        
        public void clear() {
            cache.clear();
            accessOrder.clear();
            size.set(0);
        }
        
        public int size() {
            return size.get();
        }
        
        public long getHits() {
            return hits.get();
        }
        
        public long getMisses() {
            return misses.get();
        }
        
        public double getHitRate() {
            long total = hits.get() + misses.get();
            return total > 0 ? (double) hits.get() / total : 0.0;
        }
        
        private static class CacheEntry<V> {
            private final V value;
            
            public CacheEntry(V value, long timestamp) {
                this.value = value;
            }
            
            public V getValue() {
                return value;
            }
            
        }
    }
    
    /**
     * Lock-Free Ring Buffer
     */
    public static class LockFreeRingBuffer<T> {
        private final Object[] buffer;
        private final int capacity;
        private final AtomicInteger writeIndex = new AtomicInteger(0);
        private final AtomicInteger readIndex = new AtomicInteger(0);
        private final AtomicInteger size = new AtomicInteger(0);
        
        public LockFreeRingBuffer(int capacity) {
            this.capacity = capacity;
            this.buffer = new Object[capacity];
        }
        
        public boolean offer(T item) {
            if (size.get() >= capacity) {
                return false; // Buffer full
            }
            
            int currentWriteIndex = writeIndex.get();
            int nextWriteIndex = (currentWriteIndex + 1) % capacity;
            
            if (writeIndex.compareAndSet(currentWriteIndex, nextWriteIndex)) {
                buffer[currentWriteIndex] = item;
                size.incrementAndGet();
                return true;
            }
            
            return false; // CAS failed, retry
        }
        
        @SuppressWarnings("unchecked")
        public T poll() {
            if (size.get() <= 0) {
                return null; // Buffer empty
            }
            
            int currentReadIndex = readIndex.get();
            int nextReadIndex = (currentReadIndex + 1) % capacity;
            
            if (readIndex.compareAndSet(currentReadIndex, nextReadIndex)) {
                T item = (T) buffer[currentReadIndex];
                buffer[currentReadIndex] = null; // Help GC
                size.decrementAndGet();
                return item;
            }
            
            return null; // CAS failed, retry
        }
        
        public int size() {
            return size.get();
        }
        
        public boolean isEmpty() {
            return size.get() == 0;
        }
        
        public boolean isFull() {
            return size.get() >= capacity;
        }
        
        public int capacity() {
            return capacity;
        }
    }
    
    /**
     * Lock-Free Counter mit verschiedenen Aggregationen
     */
    public static class LockFreeCounter {
        private final LongAdder count = new LongAdder();
        private final LongAdder sum = new LongAdder();
        private final AtomicLong min = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong max = new AtomicLong(Long.MIN_VALUE);
        private final AtomicLong lastValue = new AtomicLong(0);
        
        public void add(long value) {
            count.increment();
            sum.add(value);
            lastValue.set(value);
            
            // Update min (lock-free)
            long currentMin = min.get();
            while (value < currentMin && !min.compareAndSet(currentMin, value)) {
                currentMin = min.get();
            }
            
            // Update max (lock-free)
            long currentMax = max.get();
            while (value > currentMax && !max.compareAndSet(currentMax, value)) {
                currentMax = max.get();
            }
        }
        
        public long getCount() {
            return count.sum();
        }
        
        public long getSum() {
            return sum.sum();
        }
        
        public double getAverage() {
            long countValue = count.sum();
            return countValue > 0 ? (double) sum.sum() / countValue : 0.0;
        }
        
        public long getMin() {
            long minValue = min.get();
            return minValue == Long.MAX_VALUE ? 0 : minValue;
        }
        
        public long getMax() {
            long maxValue = max.get();
            return maxValue == Long.MIN_VALUE ? 0 : maxValue;
        }
        
        public long getLastValue() {
            return lastValue.get();
        }
        
        public void reset() {
            count.reset();
            sum.reset();
            min.set(Long.MAX_VALUE);
            max.set(Long.MIN_VALUE);
            lastValue.set(0);
        }
    }
    
    /**
     * Lock-Free HashMap mit Atomic Operations
     */
    public static class LockFreeHashMap<K, V> {
        private final ConcurrentHashMap<K, AtomicReference<V>> map = new ConcurrentHashMap<>();
        
        public V get(K key) {
            AtomicReference<V> ref = map.get(key);
            return ref != null ? ref.get() : null;
        }
        
        public V put(K key, V value) {
            AtomicReference<V> ref = map.computeIfAbsent(key, k -> new AtomicReference<>());
            return ref.getAndSet(value);
        }
        
        public V putIfAbsent(K key, V value) {
            AtomicReference<V> ref = map.computeIfAbsent(key, k -> new AtomicReference<>());
            if (ref.compareAndSet(null, value)) {
                return null;
            }
            return ref.get();
        }
        
        public boolean replace(K key, V expectedValue, V newValue) {
            AtomicReference<V> ref = map.get(key);
            return ref != null && ref.compareAndSet(expectedValue, newValue);
        }
        
        public V remove(K key) {
            AtomicReference<V> ref = map.remove(key);
            return ref != null ? ref.get() : null;
        }
        
        public boolean remove(K key, V expectedValue) {
            AtomicReference<V> ref = map.get(key);
            if (ref != null && ref.compareAndSet(expectedValue, null)) {
                map.remove(key, ref);
                return true;
            }
            return false;
        }
        
        public int size() {
            return map.size();
        }
        
        public boolean isEmpty() {
            return map.isEmpty();
        }
        
        public void clear() {
            map.clear();
        }
        
        public boolean containsKey(K key) {
            return map.containsKey(key);
        }
    }
    
    /**
     * Lock-Free Stack (LIFO)
     */
    public static class LockFreeStack<T> {
        private final AtomicReference<Node<T>> head = new AtomicReference<>();
        private final AtomicInteger size = new AtomicInteger(0);
        
        public void push(T item) {
            Node<T> newNode = new Node<>(item);
            Node<T> currentHead;
            
            do {
                currentHead = head.get();
                newNode.next = currentHead;
            } while (!head.compareAndSet(currentHead, newNode));
            
            size.incrementAndGet();
        }
        
        public T pop() {
            Node<T> currentHead;
            Node<T> newHead;
            
            do {
                currentHead = head.get();
                if (currentHead == null) {
                    return null; // Stack is empty
                }
                newHead = currentHead.next;
            } while (!head.compareAndSet(currentHead, newHead));
            
            size.decrementAndGet();
            return currentHead.item;
        }
        
        public T peek() {
            Node<T> currentHead = head.get();
            return currentHead != null ? currentHead.item : null;
        }
        
        public boolean isEmpty() {
            return head.get() == null;
        }
        
        public int size() {
            return size.get();
        }
        
        private static class Node<T> {
            final T item;
            Node<T> next;
            
            Node(T item) {
                this.item = item;
            }
        }
    }
    
    /**
     * Lock-Free Queue (FIFO)
     */
    public static class LockFreeQueue<T> {
        private final AtomicReference<Node<T>> head = new AtomicReference<>();
        private final AtomicReference<Node<T>> tail = new AtomicReference<>();
        private final AtomicInteger size = new AtomicInteger(0);
        
        public LockFreeQueue() {
            Node<T> dummy = new Node<>(null);
            head.set(dummy);
            tail.set(dummy);
        }
        
        public void enqueue(T item) {
            Node<T> newNode = new Node<>(item);
            Node<T> currentTail;
            Node<T> currentTailNext;
            
            while (true) {
                currentTail = tail.get();
                currentTailNext = currentTail.next.get();
                
                if (currentTail == tail.get()) {
                    if (currentTailNext == null) {
                        if (currentTail.next.compareAndSet(null, newNode)) {
                            break;
                        }
                    } else {
                        tail.compareAndSet(currentTail, currentTailNext);
                    }
                }
            }
            
            tail.compareAndSet(currentTail, newNode);
            size.incrementAndGet();
        }
        
        public T dequeue() {
            Node<T> currentHead;
            Node<T> currentTail;
            Node<T> currentHeadNext;
            T item;
            
            while (true) {
                currentHead = head.get();
                currentTail = tail.get();
                currentHeadNext = currentHead.next.get();
                
                if (currentHead == head.get()) {
                    if (currentHead == currentTail) {
                        if (currentHeadNext == null) {
                            return null; // Queue is empty
                        }
                        tail.compareAndSet(currentTail, currentHeadNext);
                    } else {
                        if (currentHeadNext == null) {
                            continue;
                        }
                        item = currentHeadNext.item;
                        if (head.compareAndSet(currentHead, currentHeadNext)) {
                            break;
                        }
                    }
                }
            }
            
            size.decrementAndGet();
            return item;
        }
        
        public T peek() {
            Node<T> currentHead = head.get();
            Node<T> currentHeadNext = currentHead.next.get();
            return currentHeadNext != null ? currentHeadNext.item : null;
        }
        
        public boolean isEmpty() {
            Node<T> currentHead = head.get();
            Node<T> currentHeadNext = currentHead.next.get();
            return currentHeadNext == null;
        }
        
        public int size() {
            return size.get();
        }
        
        private static class Node<T> {
            final T item;
            final AtomicReference<Node<T>> next = new AtomicReference<>();
            
            Node(T item) {
                this.item = item;
            }
        }
    }
    
    /**
     * Lock-Free BitSet für schnelle Set-Operationen
     */
    public static class LockFreeBitSet {
        private final AtomicLong[] bits;
        private final int size;
        
        public LockFreeBitSet(int size) {
            this.size = size;
            int arraySize = (size + 63) / 64; // Round up to nearest multiple of 64
            this.bits = new AtomicLong[arraySize];
            for (int i = 0; i < arraySize; i++) {
                bits[i] = new AtomicLong(0);
            }
        }
        
        public void set(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            
            int arrayIndex = index / 64;
            int bitIndex = index % 64;
            long mask = 1L << bitIndex;
            
            AtomicLong atomicLong = bits[arrayIndex];
            long currentValue;
            do {
                currentValue = atomicLong.get();
            } while (!atomicLong.compareAndSet(currentValue, currentValue | mask));
        }
        
        public void clear(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            
            int arrayIndex = index / 64;
            int bitIndex = index % 64;
            long mask = ~(1L << bitIndex);
            
            AtomicLong atomicLong = bits[arrayIndex];
            long currentValue;
            do {
                currentValue = atomicLong.get();
            } while (!atomicLong.compareAndSet(currentValue, currentValue & mask));
        }
        
        public boolean get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            
            int arrayIndex = index / 64;
            int bitIndex = index % 64;
            long mask = 1L << bitIndex;
            
            return (bits[arrayIndex].get() & mask) != 0;
        }
        
        public int size() {
            return size;
        }
        
        public int cardinality() {
            int count = 0;
            for (AtomicLong atomicLong : bits) {
                count += Long.bitCount(atomicLong.get());
            }
            return count;
        }
        
        public void clear() {
            for (AtomicLong atomicLong : bits) {
                atomicLong.set(0);
            }
        }
    }
}
