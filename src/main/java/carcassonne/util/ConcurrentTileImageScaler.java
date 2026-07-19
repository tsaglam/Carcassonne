package carcassonne.util;

import static carcassonne.settings.GameSettings.TILE_RESOLUTION;

import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.view.PaintShop;

/**
 * Thread-safe tile image scaling with a lock-free cache-hit fast path. Cache misses acquire a per-key semaphore to
 * prevent duplicate work when many grid positions request the same tile at the same size.
 * @author Timur Saglam
 */
public final class ConcurrentTileImageScaler {
    private static final ConcurrentMap<Integer, Semaphore> scalingMutexes = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, Semaphore> loadingMutexes = new ConcurrentHashMap<>();
    private static final int SINGLE_PERMIT = 1;

    private ConcurrentTileImageScaler() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Returns the scaled image of a tile. Lock-free for cache hits.
     * @param tile is the tile whose image is required.
     * @param targetSize is the edge length of the (quadratic) image in pixels.
     * @param fastScaling specifies whether a fast scaling algorithm should be used.
     * @return the scaled {@link Image}.
     */
    public static Image getScaledImage(Tile tile, int targetSize, boolean fastScaling) {
        CachedImage cached = TileImageScalingCache.getCached(tile, targetSize);
        if (cached != null && (fastScaling || !cached.isPreview())) {
            return cached.image();
        }
        return getScaledImageLocked(tile, targetSize, fastScaling);
    }

    /**
     * Returns a scaled multi-resolution image of a tile supporting HighDPI displays (e.g., Retina).
     * @param tile the tile whose image is required
     * @param targetSize the edge length of the quadratic image in pixels
     * @param fastScaling whether to use a fast scaling algorithm
     * @return the scaled multi-resolution {@link Image}
     */
    public static Image getScaledMultiResolutionImage(Tile tile, int targetSize, boolean fastScaling) {
        double[] factors = GameSettings.HIGH_DPI_FACTORS;
        Image[] images = new Image[factors.length + 1];

        images[0] = getScaledImage(tile, targetSize, fastScaling);

        for (int i = 0; i < factors.length; i++) {
            int scaledSize = (int) Math.round(targetSize * factors[i]);
            images[i + 1] = getScaledImage(tile, Math.min(scaledSize, GameSettings.TILE_RESOLUTION), fastScaling);
        }

        return new BaseMultiResolutionImage(images);
    }

    private static Image getScaledImageLocked(Tile tile, int targetSize, boolean fastScaling) {
        int lockKey = TileImageScalingCache.createKey(tile, targetSize);
        Semaphore lock = scalingMutexes.computeIfAbsent(lockKey, k -> new Semaphore(SINGLE_PERMIT));
        lock.acquireUninterruptibly();
        try {
            return getScaledImageUnsafe(tile, targetSize, fastScaling);
        } finally {
            lock.release();
        }
    }

    private static Image getScaledImageUnsafe(Tile tile, int targetSize, boolean fastScaling) {
        CachedImage cached = TileImageScalingCache.getCached(tile, targetSize);
        if (cached != null && (fastScaling || !cached.isPreview())) {
            return cached.image();
        }
        Image largerImage = getOriginalImage(tile);
        Image scaledImage = FastImageScaler.scale(largerImage, targetSize, fastScaling);
        TileImageScalingCache.putScaledImage(scaledImage, tile, targetSize, fastScaling);
        return scaledImage;
    }

    private static Image getOriginalImage(Tile tile) {
        CachedImage cached = TileImageScalingCache.getCached(tile, TILE_RESOLUTION);
        if (cached != null && !cached.isPreview()) {
            return cached.image();
        }
        int lockKey = TileImageScalingCache.createKey(tile, TILE_RESOLUTION);
        Semaphore lock = loadingMutexes.computeIfAbsent(lockKey, k -> new Semaphore(SINGLE_PERMIT));
        lock.acquireUninterruptibly();
        try {
            cached = TileImageScalingCache.getCached(tile, TILE_RESOLUTION);
            if (cached != null && !cached.isPreview()) {
                return cached.image();
            }
            String imagePath = GameSettings.TILE_FOLDER_PATH + tile.getType().name() + tile.getImageIndex() + GameSettings.TILE_FILE_TYPE;
            Image image;
            if (tile.hasEmblem()) {
                BufferedImage loaded = ImageLoadingUtil.createBufferedImage(imagePath);
                image = PaintShop.addEmblem(loaded);
            } else {
                image = ImageLoadingUtil.createBufferedImage(imagePath);
            }
            TileImageScalingCache.putScaledImage(image, tile, TILE_RESOLUTION, false);
            return image;
        } finally {
            lock.release();
        }
    }
}
