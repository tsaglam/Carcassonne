package carcassonne.model.tile;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;
import carcassonne.view.PaintShop;

/**
 * This class manages the graphical representation of a {@link Tile}. Additionally, it stores the orientation state of
 * the graphical representation.
 * @author Timur Saglam
 */
public class TileDepiction {
    private static final int IMAGES_PER_TILE = 4;

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
     * Returns the current depiction according to the orientation.
     * @param edgeLength is the edge length of the (quadratic) image in pixels.
     * @return the {@link ImageIcon} which is the current depiction.
     */
    public ImageIcon getCurrentScaledDepiction(int edgeLength) {
        if (TileImageScalingCache.containsScaledImage(tileType, rotation, edgeLength)) {
            return TileImageScalingCache.getScaledImage(tileType, rotation, edgeLength);
        }
        ImageIcon scaledImage = new ImageIcon(images.get(rotation).getImage().getScaledInstance(edgeLength, edgeLength, Image.SCALE_SMOOTH));
        TileImageScalingCache.putScaledImage(scaledImage, tileType, rotation, edgeLength);
        return scaledImage;
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

    private void loadImage(String imagePath, int index, boolean hasEmblem) {
        if (TileImageScalingCache.containsScaledImage(tileType, index, 300)) { // TODO (HIGH) use constant for full res
            images.add(TileImageScalingCache.getScaledImage(tileType, index, 300));
        } else if (hasEmblem) {
            loadImageAndPaintEmblem(imagePath, index);
        } else {
            ImageIcon image = new ImageIcon(imagePath);
            TileImageScalingCache.putScaledImage(image, tileType, index, 300);
            images.add(image);
        }
    }

    private void loadImageAndPaintEmblem(String imagePath, int index) {
        File file = new File(imagePath);
        try {
            BufferedImage image = ImageIO.read(file);
            ImageIcon paintedImage = PaintShop.addEmblem(image);
            images.add(paintedImage);
            TileImageScalingCache.putScaledImage(paintedImage, tileType, index, 300);
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + imagePath);
        }
    }
}
