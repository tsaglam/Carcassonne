package carcassonne.util;

import static carcassonne.settings.GameSettings.TILE_RESOLUTION;

import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;

import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.view.PaintShop;

/**
 * Tile scaling utility class that is optimized for concurrent use. It leverages the image caching capabilities of the
 * {@link TileImageScalingCache} and the image scaling capabilities of the {@link FastImageScaler}. Duplicate scaling of
 * the same tile-size combination is prevented by {@link TileImageScalingCache#computeIfAbsent}.
 * @author Timur Saglam
 */
public final class ConcurrentTileImageScaler {

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
        CachedImage cached = TileImageScalingCache.getCached(tile, targetSize);
        if (cached != null && (fastScaling || !cached.isPreview())) {
            return cached.image();
        }
        Image largerImage = getOriginalImage(tile);
        return TileImageScalingCache.computeIfAbsent(tile, targetSize, fastScaling, () -> scaleImage(largerImage, targetSize, fastScaling));
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

        images[0] = getScaledImage(tile, targetSize, fastScaling);

        for (int i = 0; i < factors.length; i++) {
            int scaledSize = (int) Math.round(targetSize * factors[i]);
            images[i + 1] = getScaledImage(tile, Math.min(scaledSize, GameSettings.TILE_RESOLUTION), fastScaling);
        }

        return new BaseMultiResolutionImage(images);
    }

    /**
     * Gets a full-size image for a specific tile. Uses caching to reuse image icons.
     */
    private static Image getOriginalImage(Tile tile) {
        CachedImage cached = TileImageScalingCache.getCached(tile, TILE_RESOLUTION);
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
    }

    /**
     * Scales the full resolution image to the required size with either the fast or the smooth scaling algorithm.
     */
    private static Image scaleImage(Image image, int size, boolean fastScaling) {
        if (fastScaling) {
            return FastImageScaler.scaleDown(image, size);
        }
        return image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }
}
