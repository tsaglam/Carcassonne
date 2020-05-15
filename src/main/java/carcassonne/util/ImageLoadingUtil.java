package carcassonne.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.view.GameMessage;
import carcassonne.view.PaintShop;

/**
 * Utility class for loading image files and creating image icons and buffered images.
 * @author Timur Saglam
 */
public enum ImageLoadingUtil {
    EMBLEM("emblem.png"),
    HIGHLIGHT("highlight.png"),
    LEFT("icons/left.png"),
    NULL_TILE("tiles/Null0.png"),
    RIGHT("icons/right.png"),
    SKIP("icons/skip.png");

    private final String path;

    ImageLoadingUtil(String path) {
        this.path = path;
    }

    /**
     * Convenience method that creates a buffered image for the image enumeral.
     * @return the buffered image.
     */
    public BufferedImage createBufferedImage() {
        return createBufferedImage(path);
    }

    /**
     * Convenience method that creates an image icon for the image enumeral.
     * @return the image icon.
     */
    public ImageIcon createImageIcon() {
        return createImageIcon(path);
    }

    /**
     * Loads an image from a path and creates a buffered image.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the buffered image.
     */
    public static BufferedImage createBufferedImage(String path) {
        try {
            return ImageIO.read(PaintShop.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + path);
            return null;
        }
    }

    /**
     * Loads an image from a path and creates an image icon.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the image icon.
     */
    public static ImageIcon createImageIcon(String path) {
        return new ImageIcon(ImageLoadingUtil.class.getClassLoader().getResource(path));
    }
}
