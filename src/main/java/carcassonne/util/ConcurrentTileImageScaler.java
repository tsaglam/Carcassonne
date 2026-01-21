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
 * Tile scaling utility class that is optimized for concurrent use. It uses a sophisticated locking mechanism and
 * leverages the image caching capabilities of the {@link TileImageScalingCache} and the image scaling capabilities of
 * the {@link FastImageScaler}.
 * @author Timur Saglam
 */
public final class ConcurrentTileImageScaler {
    private static final ConcurrentMap<Integer, Semaphore> semaphores = new ConcurrentHashMap<>();
    private static final int SHIFT_VALUE = 1000;
    private static final int SINGLE_PERMIT = 1;

    private ConcurrentTileImageScaler() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Returns the scaled image of a tile. This method is thread safe and leverages caching.
     * @param tile is the tile whose image is required.
     * @param targetSize is the edge length of the (quadratic) image in pixels.
     * @param fastScaling specifies whether a fast scaling algorithm should be used.
     * @return the scaled {@link Image}.
     */
    public static Image getScaledImage(Tile tile, int targetSize, boolean fastScaling) {
        int lockKey = createKey(tile, targetSize);
        semaphores.putIfAbsent(lockKey, new Semaphore(SINGLE_PERMIT));
        Semaphore lock = semaphores.get(lockKey);
        try {
            lock.acquire();
            return getScaledImageUnsafe(tile, targetSize, fastScaling);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            lock.release();
        }
        return null;
    }

    /**
     * Returns a scaled multi-resolution image of a tile supporting HighDPI displays (e.g., Retina). This method is
     * thread-safe and leverages caching.
     * @param tile the tile whose image is required
     * @param targetSize the edge length of the quadratic image in pixels
     * @param fastScaling whether to use a fast scaling algorithm
     * @return the scaled multi-resolution {@link Image}
     */
    public static Image getScaledMultiResolutionImage(Tile tile, int targetSize, boolean fastScaling) {
        double[] factors = GameSettings.HIGH_DPI_FACTORS;
        Image[] images = new Image[factors.length + 1];

        // Base resolution image
        images[0] = getScaledImage(tile, targetSize, fastScaling);

        // Higher resolution variants
        for (int i = 0; i < factors.length; i++) {
            int scaledSize = (int) Math.round(targetSize * factors[i]);
            images[i + 1] = getScaledImage(tile, Math.min(scaledSize, GameSettings.TILE_RESOLUTION), fastScaling);
        }

        return new BaseMultiResolutionImage(images);
    }

    /**
     * Either scales the full resolution image to the required size or retrieves the cached scaled image. This method is not
     * thread safe.
     */
    private static Image getScaledImageUnsafe(Tile tile, int targetSize, boolean fastScaling) {
        if (TileImageScalingCache.containsScaledImage(tile, targetSize, fastScaling)) {
            return TileImageScalingCache.getScaledImage(tile, targetSize);
        }
        Image largerImage = getOriginalImage(tile, targetSize);
        Image scaledImage = scaleImage(largerImage, targetSize, fastScaling);
        TileImageScalingCache.putScaledImage(scaledImage, tile, targetSize, fastScaling);
        return scaledImage;
    }

    /**
     * Gets a full-size image for a specific tile. Uses caching to reuse image icons.
     */
    private static Image getOriginalImage(Tile tile, int targetSize) {
        int lockKey = createKey(tile, TILE_RESOLUTION);
        semaphores.putIfAbsent(lockKey, new Semaphore(SINGLE_PERMIT));
        Semaphore lock = semaphores.get(lockKey);
        try {
            if (targetSize != TILE_RESOLUTION) {
                lock.acquire();
            }
            return getOriginalImageUnsafe(tile);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            if (targetSize != TILE_RESOLUTION) {
                lock.release();
            }
        }
        return null;
    }

    /**
     * Loads an image for a specific tile. Uses caching to reuse image icons. This method is not thread safe.
     */
    private static Image getOriginalImageUnsafe(Tile tile) {
        String imagePath = GameSettings.TILE_FOLDER_PATH + tile.getType().name() + tile.getImageIndex() + GameSettings.TILE_FILE_TYPE;
        if (TileImageScalingCache.containsScaledImage(tile, TILE_RESOLUTION, false)) {
            return TileImageScalingCache.getScaledImage(tile, TILE_RESOLUTION);
        }
        if (tile.hasEmblem()) {
            return loadImageAndPaintEmblem(tile, imagePath);
        }
        Image image = ImageLoadingUtil.createBufferedImage(imagePath);
        TileImageScalingCache.putScaledImage(image, tile, TILE_RESOLUTION, false);
        return image;
    }

    /**
     * Loads a tile image for a tile with a certain rotation index and paints its emblem.
     */
    private static Image loadImageAndPaintEmblem(Tile tile, String imagePath) {
        BufferedImage image = ImageLoadingUtil.createBufferedImage(imagePath);
        Image paintedImage = PaintShop.addEmblem(image);
        TileImageScalingCache.putScaledImage(paintedImage, tile, TILE_RESOLUTION, false);
        return paintedImage;
    }

    /**
     * Scales the full resolution image to the required size with either the fast or the smooth scaling algorithm. This
     * method is not thread safe.
     */
    private static Image scaleImage(Image image, int size, boolean fastScaling) {
        if (fastScaling) {
            return FastImageScaler.scaleDown(image, size);
        }
        return image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }

    /**
     * Creates a primitive composite key for the tile depiction with a specific edge length.
     */
    private static int createKey(Tile tile, int size) {
        return size + tile.getType().ordinal() * SHIFT_VALUE + tile.getImageIndex() * SHIFT_VALUE * SHIFT_VALUE;
    }
}
