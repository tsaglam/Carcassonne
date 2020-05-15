package carcassonne.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Utility class for scaling quadratic images extremely fast. This class enable 5x faster scaling than
 * {@link Image#getScaledInstance(int, int, int)} with {@link Image#SCALE_FAST} and 10x faster scaling than
 * {@link Image#getScaledInstance(int, int, int)} with {@link Image#SCALE_SMOOTH}. Additionally, the resulting images
 * look better as images generated with {@link Image#SCALE_FAST}. This class is heavily based on an article by Dr. Franz
 * Graf.
 * @see <a href=" https://www.locked.de/fast-image-scaling-in-java/">locked.de/fast-image-scaling-in-java</a>
 * @author Timur Saglam
 */
public final class FastImageScaler {
    
    private FastImageScaler() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Scales a quadratic images to a image with a given resolution.
     * @param image is the original image.
     * @param resolution is the desired edge length of the scaled image.
     * @return the scaled image.
     */
    public static Image scaleImage(Image image, int resolution) {
        Image result = scaleByHalf(image, resolution);
        result = scaleExact(result, resolution);
        return result;
    }

    /*
     * While the image is larger than 2x the target size: Scale image with factor 0.5 and nearest neighbor interpolation.
     */
    private static BufferedImage scaleByHalf(Image image, int resolution) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        float factor = getBinFactor(width, height, resolution);
        width *= factor;
        height *= factor;
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = scaled.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.drawImage(image, 0, 0, width, height, null);
        graphics.dispose();
        return scaled;
    }

    /*
     * Scale to final target size with bilinear interpolation.
     */
    private static BufferedImage scaleExact(Image image, int resolution) {
        float factor = resolution / (float) image.getWidth(null);
        int width = (int) (image.getWidth(null) * factor);
        int height = (int) (image.getHeight(null) * factor);
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return scaled;
    }

    private static float getBinFactor(int width, int height, int resolution) {
        float factor = 1;
        float target = resolution / (float) width;
        if (target <= 1) {
            while (factor / 2 > target) {
                factor /= 2;
            }
        } else {
            while (factor * 2 < target) {
                factor *= 2;
            }
        }
        return factor;
    }
}
