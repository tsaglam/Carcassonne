package carcassonne.model.tile;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.settings.GameSettings;
import carcassonne.util.FastImageScaler;
import carcassonne.view.GameMessage;
import carcassonne.view.PaintShop;

/**
 * This class manages the graphical representation of a {@link Tile}. Additionally, it stores the orientation state of
 * the graphical representation.
 * @author Timur Saglam
 */
public class TileDepiction {
    private static final int SHIFT_VALUE = 1000;
    private static final int SINGLE_PERMIT = 1;
    private static final int FULL_RESOLUTION = 300;
    private static final int IMAGES_PER_TILE = 4;
    private static final ConcurrentMap<Integer, Semaphore> semaphores = new ConcurrentHashMap<>();
    private final ArrayList<ImageIcon> images;
    private final TileType tileType;
    private int rotation;

    /**
     * Creates the tile depiction, which includes the representation of the tile in every orientation.
     * @param tileType is the type of the tile, determines the image.
     * @param hasEmblem determines whether the tile representation includes an emblem.
     */
    public TileDepiction(TileType tileType, boolean hasEmblem) {
        this.tileType = tileType;
        images = new ArrayList<>(IMAGES_PER_TILE); // create image array.
        for (int index = 0; index < IMAGES_PER_TILE; index++) { // for every image
            String imagePath = GameSettings.TILE_FOLDER_PATH + tileType.name() + index + GameSettings.TILE_FILE_TYPE;
            loadImage(imagePath, index, hasEmblem);
        }
    }

    /**
     * Returns the current depiction according to the orientation.
     * @return the {@link ImageIcon} which is the current depiction.
     */
    public ImageIcon getCurrentDepiction() {
        return images.get(rotation);
    }

    /**
     * Returns the current depiction according to the orientation. This method is thread safe!
     * @param edgeLength is the edge length of the (quadratic) image in pixels.
     * @param fastScaling specifies whether a fast scaling algorithm should be used.
     * @return the {@link ImageIcon} which is the current depiction.
     */
    public ImageIcon getCurrentScaledDepiction(int edgeLength, boolean fastScaling) {
        int lockKey = createKey(edgeLength);
        semaphores.putIfAbsent(lockKey, new Semaphore(SINGLE_PERMIT));
        Semaphore lock = semaphores.get(lockKey);
        try {
            lock.acquire();
            return getScaledImage(edgeLength, fastScaling);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            lock.release();
        }
        return new ImageIcon();
    }

    /**
     * Updates the tile depiction to represent the rotated depiction (90 degree to the left).
     */
    public void rotateLeft() {
        rotation = rotation <= 0 ? 3 : rotation - 1; // update orientation indicator
    }

    /**
     * Updates the tile depiction to represent the rotated depiction (90 degree to the right).
     */
    public void rotateRight() {
        rotation = rotation >= 3 ? 0 : rotation + 1; // update orientation indicator
    }

    /*
     * Either scales the full resolution image to the required size or retrieves the cached scaled image.
     */
    private ImageIcon getScaledImage(int edgeLength, boolean fastScaling) {
        if (TileImageScalingCache.containsScaledImage(tileType, rotation, edgeLength, fastScaling)) {
            return TileImageScalingCache.getScaledImage(tileType, rotation, edgeLength);
        }
        ImageIcon scaledImage = new ImageIcon(scaleImage(edgeLength, fastScaling));
        TileImageScalingCache.putScaledImage(scaledImage, tileType, rotation, edgeLength, fastScaling);
        return scaledImage;
    }

    /*
     * Scales the full resolution image to the required size with either the fast or the smooth scaling algorithm.
     */
    private Image scaleImage(int edgeLength, boolean fastScaling) {
        if (fastScaling) {
            return FastImageScaler.scaleImage(images.get(rotation).getImage(), edgeLength);
        }
        return images.get(rotation).getImage().getScaledInstance(edgeLength, edgeLength, Image.SCALE_SMOOTH);
    }

    /*
     * Loads a tile image for this tile depiction with a certain rotation index. Uses caching to reuse image icons.
     */
    private void loadImage(String imagePath, int index, boolean hasEmblem) {
        if (TileImageScalingCache.containsScaledImage(tileType, index, FULL_RESOLUTION, false)) {
            images.add(TileImageScalingCache.getScaledImage(tileType, index, FULL_RESOLUTION));
        } else if (hasEmblem) {
            loadImageAndPaintEmblem(imagePath, index);
        } else {
            ImageIcon image = new ImageIcon(imagePath);
            TileImageScalingCache.putScaledImage(image, tileType, index, FULL_RESOLUTION, false);
            images.add(image);
        }
    }

    /**
     * Loads a tile image for this tile depiction with a certain rotation index and paints its emblem.
     */
    private void loadImageAndPaintEmblem(String imagePath, int index) {
        File file = new File(imagePath);
        try {
            BufferedImage image = ImageIO.read(file);
            ImageIcon paintedImage = PaintShop.addEmblem(image);
            images.add(paintedImage);
            TileImageScalingCache.putScaledImage(paintedImage, tileType, index, FULL_RESOLUTION, false);
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + imagePath);
        }
    }

    /*
     * Creates a primitive composite key for the tile depiction with a specific edge length.
     */
    private int createKey(int edgeLength) {
        return edgeLength + tileType.ordinal() * SHIFT_VALUE + rotation * SHIFT_VALUE * SHIFT_VALUE;
    }
}
