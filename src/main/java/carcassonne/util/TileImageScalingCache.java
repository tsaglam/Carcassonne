package carcassonne.util;

import java.awt.Image;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import carcassonne.model.tile.Tile;

/**
 * Caches scaled images of tiles to improve the performance. Uses {@link ConcurrentHashMap#compute} to avoid duplicate
 * computation of the same tile-size combination under concurrent access.
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
     * Retrieves or computes a scaled image for the given tile and size. Prevents duplicate computation under concurrent
     * access: if multiple threads request the same key simultaneously, only one computes; others receive the result.
     * @param tile the tile
     * @param size the edge length
     * @param preview whether the image is a preview (fast) or final render
     * @param supplier the computation to run if miss
     * @return the scaled image
     */
    public static Image computeIfAbsent(Tile tile, int size, boolean preview, Supplier<Image> supplier) {
        int key = createKey(tile, size);
        CachedImage result = cachedImages.compute(key, (existingKey, existing) -> {
            if (existing != null && (preview || !existing.isPreview())) {
                return existing;
            }
            return new CachedImage(supplier.get(), preview);
        });
        return result.image();
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
