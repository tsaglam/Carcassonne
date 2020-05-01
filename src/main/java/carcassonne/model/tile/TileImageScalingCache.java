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
        // private constructor to prevent instantiation!
    }

    /**
     * Checks if there is an existing scaled image in this cache.
     * @param tile is the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return true if there is an existing image cached with the specified resolution.
     */
    public static boolean containsScaledImage(Tile tile, int resolution) {
        return chachedImageIcons.containsKey(createKey(tile, resolution));
    }

    /**
     * Retrieves an existing scaled image in this cache.
     * @param tile is the tile that specifies the base image and its orientation.
     * @param resolution is the edge with of the (quadratic) image.
     * @return the scaled image or null if there is none.
     */
    public static ImageIcon getScaledImage(Tile tile, int resolution) {
        return chachedImageIcons.get(createKey(tile, resolution));
    }

    /**
     * Places an scaled image in this cache to enable its reuse.
     * @param image is the scaled image.
     * @param tile is the tile for which the image was scaled for.
     * @param resolution is the edge with of the scaled image.
     */
    public static void putScaledImage(ImageIcon image, Tile tile, int resolution) {
        chachedImageIcons.put(createKey(tile, resolution), image);
    }

    /**
     * Creates a primitive composite key for a tile type, a resolution, and a orientation.
     */
    private static String createKey(Tile tile, int resolution) {
        return resolution + KEY_SEPARATOR + tile.getType() + KEY_SEPARATOR + tile.getOrientation();
    }
}
