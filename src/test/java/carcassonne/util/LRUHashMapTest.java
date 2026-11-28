package carcassonne.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 test for {@link LRUHashMap}.
 * @author Timur Saglam
 */
class LRUHashMapTest {
    private static final int MAXIMUM_SIZE = 3;

    private LRUHashMap<Integer, String> map;

    @BeforeEach
    void setUp() {
        map = new LRUHashMap<>(MAXIMUM_SIZE);
    }

    @Test
    void testSizeIsBounded() {
        for (int i = 0; i < MAXIMUM_SIZE * 10; i++) {
            map.put(i, Integer.toString(i));
            assertTrue(map.size() <= MAXIMUM_SIZE, "Size should never exceed the maximum!");
        }
        assertEquals(MAXIMUM_SIZE, map.size());
    }

    @Test
    void testEvictsLeastRecentlyInserted() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.put(4, "4");
        assertFalse(map.containsKey(1), "Eldest entry should be evicted!");
        assertTrue(map.containsKey(2));
        assertTrue(map.containsKey(3));
        assertTrue(map.containsKey(4));
    }

    @Test
    void testAccessRefreshesEntry() {
        map.put(1, "1");
        map.put(2, "2");
        map.put(3, "3");
        map.get(1); // 2 is now the least recently used entry
        map.put(4, "4");
        assertTrue(map.containsKey(1), "Recently accessed entry should survive!");
        assertFalse(map.containsKey(2), "Least recently used entry should be evicted!");
        assertTrue(map.containsKey(3));
        assertTrue(map.containsKey(4));
    }
}
