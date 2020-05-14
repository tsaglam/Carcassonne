package carcassonne.model.tile;

import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import carcassonne.util.LRUHashMap;

/**
 * Caches scaled images of tiles to improve the performance. When zooming in or out all static images are only rendered
 * ones per zoom level.
 * @author Timur Saglam
 */
public class TileImageScalingCache {
    private static final int SHIFT_VALUE = 1000;
    private static final LRUHashMap<Integer, ImageIcon> cachedImageIcons = new LRUHashMap<>();
    private static final ConcurrentHashMap<Integer, Boolean> cachedScalingInformation = new ConcurrentHashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Checks if there is an existing scaled image in this cache.
     * @param tileType is the type of the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return true if there is an existing image cached with the specified resolution.
     */
    public static boolean containsScaledImage(TileType tileType, int rotation, int resolution, boolean preview) {
        int key = createKey(tileType, rotation, resolution);
        if (preview) {
            return cachedImageIcons.containsKey(key);
        }
        return cachedImageIcons.containsKey(key) && !cachedScalingInformation.get(key);
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tileType is the type of the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return the scaled image or null if there is none.
     */
    public static ImageIcon getScaledImage(TileType tileType, int rotation, int resolution) {
        return cachedImageIcons.get(createKey(tileType, rotation, resolution));
    }

    /**
     * Places an scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tileType is the type of the tile for which the image was scaled for.
     * @param resolution is the edge with of the scaled image.
     */
    public static void putScaledImage(ImageIcon image, TileType tileType, int rotation, int resolution, boolean preview) {
        int key = createKey(tileType, rotation, resolution);
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
     * Creates a primitive composite key for a tileType type, a resolution, and a orientation.
     */
    private static int createKey(TileType tileType, int rotation, int resolution) {
        return resolution + tileType.ordinal() * SHIFT_VALUE + rotation * SHIFT_VALUE * SHIFT_VALUE;
    }
}
