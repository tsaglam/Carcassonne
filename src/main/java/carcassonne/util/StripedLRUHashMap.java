package carcassonne.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe LRU cache using lock striping for improved concurrency. Operations on different stripes can execute in
 * parallel. However, cached images may be evicted between contains and get operations.
 * @param <K> key type
 * @param <V> value type
 */
public class StripedLRUHashMap<K, V> {

    private static final int DEFAULT_STRIPE_COUNT = 32;

    private final int stripeCount;
    private final Stripe<K, V>[] stripes;

    /**
     * Creates a new striped LRU cache with default stripe count.
     * @param maxCapacity maximum total entries across all stripes
     */
    public StripedLRUHashMap(int maxCapacity) {
        this(maxCapacity, DEFAULT_STRIPE_COUNT);
    }

    /**
     * Creates a new striped LRU cache with specified stripe count.
     * @param maxCapacity maximum total entries across all stripes
     * @param stripeCount number of lock stripes (must be power of 2)
     */
    @SuppressWarnings("unchecked")
    public StripedLRUHashMap(int maxCapacity, int stripeCount) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("maxCapacity must be positive");
        }
        if (stripeCount <= 0 || (stripeCount & (stripeCount - 1)) != 0) {
            throw new IllegalArgumentException("stripeCount must be a power of 2");
        }

        this.stripeCount = stripeCount;
        this.stripes = new Stripe[stripeCount];

        int capacityPerStripe = Math.max(1, maxCapacity / stripeCount);
        for (int i = 0; i < stripeCount; i++) {
            stripes[i] = new Stripe<>(capacityPerStripe);
        }
    }

    /**
     * Returns the stripe for the given key.
     */
    private Stripe<K, V> getStripe(K key) {
        int hash = key.hashCode();
        // Spread bits for better distribution
        hash ^= (hash >>> 16);
        int index = hash & (stripeCount - 1);
        return stripes[index];
    }

    /**
     * Associates the specified value with the specified key.
     * @param key the key
     * @param value the value
     * @return the previous value, or null
     */
    public V put(K key, V value) {
        return getStripe(key).put(key, value);
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value, or null if not present
     */
    public V get(K key) {
        return getStripe(key).get(key);
    }

    /**
     * Removes the mapping for the specified key.
     * @param key the key
     * @return the previous value, or null
     */
    public V remove(K key) {
        return getStripe(key).remove(key);
    }

    /**
     * Returns whether this cache contains the specified key.
     * @param key the key
     * @return true if present
     */
    public boolean containsKey(K key) {
        return getStripe(key).containsKey(key);
    }

    /**
     * Returns the approximate current number of entries. Not atomic across stripes.
     * @return approximate size
     */
    public int size() {
        int total = 0;
        for (Stripe<K, V> stripe : stripes) {
            total += stripe.size();
        }
        return total;
    }

    /**
     * Removes all entries from all stripes.
     */
    public void clear() {
        for (Stripe<K, V> stripe : stripes) {
            stripe.clear();
        }
    }

    /**
     * Individual stripe containing a LinkedHashMap and its lock.
     */
    private static class Stripe<K, V> {
        private final Lock lock;
        private final LinkedHashMap<K, V> map;
        private final int maxCapacity;

        Stripe(int maxCapacity) {
            this.maxCapacity = maxCapacity;
            this.lock = new ReentrantLock();
            this.map = new LinkedHashMap<K, V>(maxCapacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > Stripe.this.maxCapacity;
                }
            };
        }

        V put(K key, V value) {
            lock.lock();
            try {
                return map.put(key, value);
            } finally {
                lock.unlock();
            }
        }

        V get(K key) {
            lock.lock();
            try {
                return map.get(key);
            } finally {
                lock.unlock();
            }
        }

        V remove(K key) {
            lock.lock();
            try {
                return map.remove(key);
            } finally {
                lock.unlock();
            }
        }

        boolean containsKey(K key) {
            lock.lock();
            try {
                return map.containsKey(key);
            } finally {
                lock.unlock();
            }
        }

        int size() {
            lock.lock();
            try {
                return map.size();
            } finally {
                lock.unlock();
            }
        }

        void clear() {
            lock.lock();
            try {
                map.clear();
            } finally {
                lock.unlock();
            }
        }
    }
}