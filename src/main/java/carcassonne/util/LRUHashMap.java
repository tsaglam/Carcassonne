package carcassonne.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * least recently used cache based on a {@link LinkedHashMap}.
 * @author Timur Saglam
 * @param <K> the key.
 * @param <V> the value.
 */
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_SIZE = 100;
    private static final int MAXIMUM_SIZE = 2000;
    private static final long serialVersionUID = -7078586519346306608L;
    private K lastRemovedKey;

    /**
     * Creates the hash map.
     * @param maximumSize is the maximal size of the hash map.
     */
    public LRUHashMap() {
        super(INITIAL_SIZE, LOAD_FACTOR, true);
        lastRemovedKey = null;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        boolean full = size() > MAXIMUM_SIZE;
        if (full) { // remembers last removed key
            lastRemovedKey = eldest.getKey();
        }
        return full;
    }

    /**
     * Returns the key of the last removed map entry.
     * @return the key of the now deleted entry.
     */
    public K getLastRemovedKey() {
        return lastRemovedKey;
    }
}
