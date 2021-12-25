package carcassonne.util;

import java.awt.Image;
import java.util.HashMap;

import carcassonne.model.tile.Tile;

/**
 * Caches scaled images of tiles to improve the performance. When zooming in or out all static images are only rendered
 * ones per zoom level.
 * @author Timur Saglam
 */
public final class TileImageScalingCache {
    private static final int SHIFT_VALUE = 1000;
    private static final HashMap<Integer, CachedImage> cachedImages = new LRUHashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Checks if there is an existing scaled tile image in this cache.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @param previewAllowed determines if the cached image may be a preview render or should be a final render.
     * @return true if there is an existing image cached with the specified size.
     */
    public static boolean containsScaledImage(Tile tile, int size, boolean previewAllowed) {
        int key = createKey(tile, size);
        if (previewAllowed) {
            return cachedImages.containsKey(key);
        }
        return cachedImages.containsKey(key) && !cachedImages.get(key).isPreview();
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @return the scaled image or null if there is none.
     */
    public static Image getScaledImage(Tile tile, int size) {
        return cachedImages.get(createKey(tile, size)).getImage();
    }

    /**
     * Places an scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the scaled image.
     * @param preview determines if the image is a preview render or final render.
     */
    public static void putScaledImage(Image image, Tile tile, int size, boolean preview) {
        cachedImages.put(createKey(tile, size), new CachedImage(image, preview));
    }

    /**
     * Clears the cache, removing all stored tile images. This call might be unsafe with concurrent calls, as there are no
     * guarantees for clearing while putting.
     */
    public static void clear() {
        cachedImages.clear();
    }

    /**
     * Returns the number of cached elements in this cache.
     * @return the number of cached elements.
     */
    public static int size() {
        return cachedImages.size();
    }

    /**
     * Creates a primitive composite key for a tileType type, a size, and a orientation.
     */
    private static int createKey(Tile tile, int size) {
        return size + tile.getType().ordinal() * SHIFT_VALUE + tile.getRotation().ordinal() * SHIFT_VALUE * SHIFT_VALUE;
    }
}
