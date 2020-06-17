package carcassonne.model.tile;

import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import carcassonne.util.LRUHashMap;

/**
 * Caches scaled images of tiles to improve the performance. When zooming in or out all static images are only rendered
 * ones per zoom level.
 * @author Timur Saglam
 */
public final class TileImageScalingCache {
    private static final int SHIFT_VALUE = 1000;
    private static final LRUHashMap<Integer, ImageIcon> cachedImageIcons = new LRUHashMap<>();
    private static final ConcurrentHashMap<Integer, Boolean> cachedScalingInformation = new ConcurrentHashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Checks if there is an existing scaled tile image in this cache.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @param preview determines if the image should be a preview render or final render.
     * @return true if there is an existing image cached with the specified size.
     */
    public static boolean containsScaledImage(Tile tile, int size, boolean preview) {
        int key = createKey(tile, size);
        if (preview) {
            return cachedImageIcons.containsKey(key);
        }
        return cachedImageIcons.containsKey(key) && !cachedScalingInformation.get(key);
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @return the scaled image or null if there is none.
     */
    public static ImageIcon getScaledImage(Tile tile, int size) {
        return cachedImageIcons.get(createKey(tile, size));
    }

    /**
     * Places an scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the scaled image.
     * @param preview determines if the image is a preview render or final render.
     */
    public static void putScaledImage(ImageIcon image, Tile tile, int size, boolean preview) {
        int key = createKey(tile, size);
        cachedImageIcons.put(key, image);
        cachedScalingInformation.put(key, preview);
        restoreConsistency();
    }

    /**
     * Clears the cache, removing all stored tile images.
     */
    public static void clear() {
        cachedImageIcons.clear();
        cachedScalingInformation.clear();
    }

    /**
     * Returns the number of cached elements in this cache.
     * @return the number of cached elements.
     */
    public static int size() {
        return cachedImageIcons.size();
    }

    /**
     * Restores consistency between the cached images and the cached scaling information.
     */
    private static void restoreConsistency() {
        Integer lastRemovedKey = cachedImageIcons.getLastRemovedKey();
        if (lastRemovedKey != null && cachedScalingInformation.contains(lastRemovedKey)) {
            cachedScalingInformation.remove(lastRemovedKey);
        }
    }

    /**
     * Creates a primitive composite key for a tileType type, a size, and a orientation.
     */
    private static int createKey(Tile tile, int size) {
        return size + tile.getType().ordinal() * SHIFT_VALUE + tile.getRotation() * SHIFT_VALUE * SHIFT_VALUE;
    }
}
