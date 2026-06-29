package carcassonne.util;

import java.awt.Image;
import java.util.concurrent.ConcurrentHashMap;

import carcassonne.model.tile.Tile;

/**
 * Caches scaled images of tiles to improve the performance. Backed by {@link ConcurrentHashMap} for lock-free reads.
 * @author Timur Saglam
 */
public final class TileImageScalingCache {
    private static final ConcurrentHashMap<Integer, CachedImage> cachedImages = new ConcurrentHashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Retrieves an existing scaled image in this cache. Lock-free read.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @return the cached image record or null if there is none.
     */
    public static CachedImage getCached(Tile tile, int size) {
        return cachedImages.get(createKey(tile, size));
    }

    /**
     * Places a scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the scaled image.
     * @param preview determines if the image is a preview render or final render.
     */
    public static void putScaledImage(Image image, Tile tile, int size, boolean preview) {
        cachedImages.put(createKey(tile, size), new CachedImage(image, preview));
    }

    /**
     * Clears the cache, removing all stored tile images.
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
     * Creates a primitive composite key for a tile type, a size, and an orientation using bit packing.
     */
    private static int createKey(Tile tile, int size) {
        return size | (tile.getType().ordinal() << 9) | (tile.getRotation().ordinal() << 15);
    }
}
