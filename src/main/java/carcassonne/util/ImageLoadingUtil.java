package carcassonne.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.view.PaintShop;
import carcassonne.view.util.GameMessage;

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
    SKIP("icons/skip.png"),
    SPLASH("splash.png");

    private static final int PLACEHOLDER_IMAGE_SIZE = 10;
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
     * Convenience method that creates a high-DPI image for the image enumeral.
     * @return the image, which has half of the width and height of the original file.
     */
    public Image createHighDpiImage() {
        return createHighDpiImage(path);
    }

    /**
     * Convenience method that creates a high-DPI image icon for the image enumeral.
     * @return the image icon, which has half of the width and height of the original file.
     */
    public ImageIcon createHighDpiImageIcon() {
        return new ImageIcon(createHighDpiImage(path));
    }

    /**
     * Loads an image from a path and creates a buffered image. Does some performance optimizations.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the buffered image.
     */
    public static BufferedImage createBufferedImage(String path) {
        BufferedImage image = loadBufferedImage(path);
        return makeCompatible(image);
    }

    /**
     * Loads an image from a path and creates a high-DPI image icon.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the image icon, which has half of the width and height of the original file.
     */
    public static ImageIcon createHighDpiImageIcon(String path) {
        return new ImageIcon(createHighDpiImage(path));
    }

    /**
     * Loads an image from a path and creates a high-DPI image.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the image, which has half of the width and height of the original file.
     */
    public static Image createHighDpiImage(String path) {
        BufferedImage fullSize = createBufferedImage(path);
        Image base = makeCompatible(fullSize.getScaledInstance(fullSize.getWidth() / 2, fullSize.getHeight() / 2, Image.SCALE_SMOOTH),
                fullSize.getTransparency());
        return new BaseMultiResolutionImage(base, fullSize);
    }

    /**
     * Creates a high-DPI image from a high-res image.
     * @param image is the high resolution image used as the version with the highest resolution.
     * @param transparency is the {@link Transparency} of the image.
     * @return the image, which has half of the width and height of the original file.
     */
    public static Image createHighDpiImage(Image image) {
        Image base = image.getScaledInstance(image.getWidth(null) / 2, image.getHeight(null) / 2, Image.SCALE_SMOOTH);
        return new BaseMultiResolutionImage(base, image);
    }

    /**
     * Loads an image from a path and creates an image icon.
     * @param path is the relative file path, omitting the resource folder path.
     * @return the image icon.
     */
    public static ImageIcon createImageIcon(String path) {
        return new ImageIcon(ImageLoadingUtil.class.getClassLoader().getResource(path));
    }

    /**
     * Converts a buffered image to a compatible image.
     * @param image is the image to convert.
     * @return the compatible image.
     * @see GraphicsConfiguration#createCompatibleImage(int, int, int)
     */
    public static BufferedImage makeCompatible(BufferedImage image) {
        return makeCompatibleImage(image, image.getWidth(), image.getHeight(), image.getTransparency());
    }

    /**
     * Converts an image to a compatible image.
     * @param image is the image to convert.
     * @param is the transparency of the image, can be received from buffered images with
     * {@link Transparency#getTransparency()}.
     * @return the compatible image.
     * @see GraphicsConfiguration#createCompatibleImage(int, int, int)
     */
    public static Image makeCompatible(Image image, int transparency) {
        return makeCompatibleImage(image, image.getWidth(null), image.getHeight(null), transparency);
    }

    private static BufferedImage makeCompatibleImage(Image image, int width, int height, int transparency) {
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage convertedImage = configuration.createCompatibleImage(width, height, transparency);
        Graphics2D graphics2D = convertedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return convertedImage;
    }

    private static BufferedImage loadBufferedImage(String path) {
        try {
            return ImageIO.read(PaintShop.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + path);
            return new BufferedImage(PLACEHOLDER_IMAGE_SIZE, PLACEHOLDER_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        }
    }
}
