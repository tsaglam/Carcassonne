package carcassonne.model.tile;

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
 * This class manages the graphical representation of a {@link Tile}. Additionally, it stores the rotation state of the
 * graphical representation.
 * @author Timur Saglam
 */
public class TileDepiction {
    private static final int IMAGES_PER_TILE = 4;

    private final ArrayList<ImageIcon> images;
    private final PaintShop paintShop;
    private int rotation;

    /**
     * Creates the tile depiction, which includes the representation of the tile in every rotation.
     * @param tileType is the type of the tile, determines the image.
     * @param hasEmblem determines whether the tile representation includes an emblem.
     */
    public TileDepiction(TileType tileType, boolean hasEmblem) {
        images = new ArrayList<>(IMAGES_PER_TILE); // create image array.
        paintShop = new PaintShop();
        for (int index = 0; index < IMAGES_PER_TILE; index++) { // for every image
            String imagePath = GameSettings.TILE_FOLDER_PATH + tileType.name() + index + GameSettings.TILE_FILE_TYPE;
            if (hasEmblem) {
                loadImageAndPaintEmblem(imagePath);
            } else {
                images.add(new ImageIcon(imagePath));
            }
        }
    }

    /**
     * Returns the current depiction according to the rotation.
     * @return the {@link ImageIcon} which is the current depiction.
     */
    public ImageIcon getCurrentDepiction() {
        return images.get(rotation);
    }

    /**
     * Updates the tile depiction to represent the rotated depiction (90 degree to the left).
     */
    public void rotateLeft() {
        rotation = rotation <= 0 ? 3 : rotation - 1; // update rotation indicator
    }

    /**
     * Updates the tile depiction to represent the rotated depiction (90 degree to the right).
     */
    public void rotateRight() {
        rotation = rotation >= 3 ? 0 : rotation + 1; // update rotation indicator
    }

    private void loadImageAndPaintEmblem(String imagePath) {
        File file = new File(imagePath);
        try {
            BufferedImage image = ImageIO.read(file);
            images.add(paintShop.addEmblem(image));
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + imagePath);
        }
    }
}
