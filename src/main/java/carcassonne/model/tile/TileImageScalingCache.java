package carcassonne.model.tile;

import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Caches scaled images of tiles to improve the performance. When zooming in or out all static images are only rendered
 * ones per zoom level.
 * @author Timur Saglam
 */
public class TileImageScalingCache {
    private static final String KEY_SEPARATOR = "|";
    private static final HashMap<String, ImageIcon> chachedImageIcons = new HashMap<>();

    private TileImageScalingCache() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Checks if there is an existing scaled image in this cache.
     * @param tileType is the type of the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return true if there is an existing image cached with the specified resolution.
     */
    public static boolean containsScaledImage(TileType tileType, int rotation, int resolution) {
        return chachedImageIcons.containsKey(createKey(tileType, rotation, resolution));
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tileType is the type of the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return the scaled image or null if there is none.
     */
    public static ImageIcon getScaledImage(TileType tileType, int rotation, int resolution) {
        return chachedImageIcons.get(createKey(tileType, rotation, resolution));
    }

    /**
     * Places an scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tileType is the type of the tile for which the image was scaled for.
     * @param resolution is the edge with of the scaled image.
     */
    public static void putScaledImage(ImageIcon image, TileType tileType, int rotation, int resolution) {
        chachedImageIcons.put(createKey(tileType, rotation, resolution), image);
    }

    /**
     * Creates a primitive composite key for a tileType type, a resolution, and a orientation.
     */
    private static String createKey(TileType tileType, int rotation, int resolution) {
        return resolution + KEY_SEPARATOR + tileType + KEY_SEPARATOR + rotation;
    }
}
