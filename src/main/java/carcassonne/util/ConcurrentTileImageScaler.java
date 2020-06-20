package carcassonne.util;

import static carcassonne.settings.GameSettings.TILE_RESOLUTION;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;
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
     * Returns the scaled image icon of a tile. This method is thread safe and leverages caching.
     * @param tile is the tile whose image is required.
     * @param size is the edge length of the (quadratic) image in pixels.
     * @param fastScaling specifies whether a fast scaling algorithm should be used.
     * @return the scaled {@link ImageIcon}.
     */
    public static ImageIcon getScaledImage(Tile tile, int size, boolean fastScaling) {
        int lockKey = createKey(tile, size);
        semaphores.putIfAbsent(lockKey, new Semaphore(SINGLE_PERMIT));
        Semaphore lock = semaphores.get(lockKey);
        try {
            lock.acquire();
            return getScaledImageUnsafe(tile, size, fastScaling);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            lock.release();
        }
        return new ImageIcon();
    }

    /**
     * Either scales the full resolution image to the required size or retrieves the cached scaled image. This method is not
     * thread safe.
     */
    private static ImageIcon getScaledImageUnsafe(Tile tile, int size, boolean fastScaling) {
        if (TileImageScalingCache.containsScaledImage(tile, size, fastScaling)) {
            return TileImageScalingCache.getScaledImage(tile, size);
        }
        ImageIcon original = getOriginalImage(tile, size);
        ImageIcon scaledImage = new ImageIcon(scaleImage(original, size, fastScaling));
        TileImageScalingCache.putScaledImage(scaledImage, tile, size, fastScaling);
        return scaledImage;
    }

    /**
     * Gets a full-size image for a specific tile. Uses caching to reuse image icons.
     */
    private static ImageIcon getOriginalImage(Tile tile, int size) {
        int lockKey = createKey(tile, TILE_RESOLUTION);
        semaphores.putIfAbsent(lockKey, new Semaphore(SINGLE_PERMIT));
        Semaphore lock = semaphores.get(lockKey);
        try {
            if (size != TILE_RESOLUTION) {
                lock.acquire();
            }
            return getOriginalImageUnsafe(tile);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            if (size != TILE_RESOLUTION) {
                lock.release();
            }
        }
        return new ImageIcon();
    }

    /**
     * Loads an image for a specific tile. Uses caching to reuse image icons. This method is not thread safe.
     */
    private static ImageIcon getOriginalImageUnsafe(Tile tile) {
        String imagePath = GameSettings.TILE_FOLDER_PATH + tile.getType().name() + tile.getRotation().ordinal() + GameSettings.TILE_FILE_TYPE;
        if (TileImageScalingCache.containsScaledImage(tile, TILE_RESOLUTION, false)) {
            return TileImageScalingCache.getScaledImage(tile, TILE_RESOLUTION);
        } else if (tile.hasEmblem()) {
            return loadImageAndPaintEmblem(tile, imagePath);
        } else {
            ImageIcon image = ImageLoadingUtil.createImageIcon(imagePath);
            TileImageScalingCache.putScaledImage(image, tile, TILE_RESOLUTION, false);
            return image;
        }
    }

    /**
     * Loads a tile image for a tile with a certain rotation index and paints its emblem.
     */
    private static ImageIcon loadImageAndPaintEmblem(Tile tile, String imagePath) {
        try {
            BufferedImage image = ImageIO.read(ConcurrentTileImageScaler.class.getClassLoader().getResourceAsStream(imagePath));
            ImageIcon paintedImage = PaintShop.addEmblem(image);
            TileImageScalingCache.putScaledImage(paintedImage, tile, TILE_RESOLUTION, false);
            return paintedImage;
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + imagePath);
        }
        return new ImageIcon();
    }

    /**
     * Scales the full resolution image to the required size with either the fast or the smooth scaling algorithm. This
     * method is not thread safe.
     */
    private static Image scaleImage(ImageIcon image, int size, boolean fastScaling) {
        if (fastScaling) {
            return FastImageScaler.scaleDown(image.getImage(), size);
        }
        return image.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }

    /**
     * Creates a primitive composite key for the tile depiction with a specific edge length.
     */
    private static int createKey(Tile tile, int size) {
        return size + tile.getType().ordinal() * SHIFT_VALUE + tile.getRotation().ordinal() * SHIFT_VALUE * SHIFT_VALUE;
    }
}
