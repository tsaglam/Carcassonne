package carcassonne.util;

import static carcassonne.settings.GameSettings.TILE_RESOLUTION;

import java.awt.Image;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import carcassonne.model.tile.Tile;

/**
 * Caches scaled images of tiles to improve the performance. Scaled images are kept in a bounded least recently used
 * cache. The original images (at {@link carcassonne.settings.GameSettings#TILE_RESOLUTION}) from which all scaled
 * images are derived are kept separately and are never evicted.
 * @author Timur Saglam
 */
public final class TileImageScalingCache {
    static final int MAXIMUM_SIZE = 2000;
    private static final Map<Integer, CachedImage> scaledImages = Collections.synchronizedMap(new LRUHashMap<>(MAXIMUM_SIZE));
    private static final Map<Integer, CachedImage> originalImages = new ConcurrentHashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the (quadratic) image.
     * @return the cached image record or null if there is none.
     */
    public static CachedImage getCached(Tile tile, int size) {
        return selectMap(size).get(createKey(tile, size));
    }

    /**
     * Places a scaled image in this cache to enable its reuse. Images with the original tile resolution are never evicted.
     * @param image is the scaled image.
     * @param tile is the tile whose scaled image is requested.
     * @param size is the edge with of the scaled image.
     * @param preview determines if the image is a preview render or final render.
     */
    public static void putScaledImage(Image image, Tile tile, int size, boolean preview) {
        selectMap(size).put(createKey(tile, size), new CachedImage(image, preview));
    }

    /**
     * Clears the cache, removing all stored tile images, including the original images.
     */
    public static void clear() {
        scaledImages.clear();
        originalImages.clear();
    }

    /**
     * Returns the number of cached elements in this cache, including the original images.
     * @return the number of cached elements.
     */
    public static int size() {
        return scaledImages.size() + originalImages.size();
    }

    /**
     * Creates a primitive composite key for a tile type, a size, and an orientation using bit packing.
     */
    static int createKey(Tile tile, int size) {
        return size | (tile.getType().ordinal() << 9) | (tile.getImageIndex() << 15);
    }

    private static Map<Integer, CachedImage> selectMap(int size) {
        return size == TILE_RESOLUTION ? originalImages : scaledImages;
    }
}
