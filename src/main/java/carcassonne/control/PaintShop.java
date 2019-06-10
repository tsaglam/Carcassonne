package carcassonne.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;

/**
 * This is the Carcassonne paint shop! It paints the meeple images!
 * @author Timur Saglam
 */
public class PaintShop {
    private static final String HIGHLIGHT = "src/main/ressources/highlight.png"; // TODO (HIGH) move to options.
    private final Map<TerrainType, BufferedImage> imageMap;
    private final Map<TerrainType, BufferedImage> templateMap;
    private BufferedImage highlightImage;

    /**
     * Basic constructor, creates base images and templates.
     */
    public PaintShop() {
        imageMap = buildImageMap(false);
        templateMap = buildImageMap(true);
        File file = new File(HIGHLIGHT);
        try {
            highlightImage = ImageIO.read(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Returns a custom colored meeple.
     * @param meepleType is the type of the meeple.
     * @param color is the custom color.
     * @return the colored meeple.
     */
    public ImageIcon getColoredMeeple(TerrainType meepleType, Color color) {
        return paintMeeple(meepleType, color.getRGB());
    }

    /**
     * Returns a custom colored meeple.
     * @param meepleType is the type of the meeple.
     * @param playerNumber is the number of the player whose color is used.
     * @return the colored meeple.
     */
    public ImageIcon getColoredMeeple(TerrainType meepleType, Player player) {
        return getColoredMeeple(meepleType, player.getColor());
    }

    /**
     * Returns a custom colored highlight image.
     * @param player determines the color of the highlight.
     * @param tileIcon is the tile to highlight.
     * @return the highlighted tile.
     */
    public ImageIcon getColoredHighlight(Player player) {
        BufferedImage tileImage = loadImage("src/main/ressources/tiles/Null0.png"); // TODO (HIGH) move to properties
        return colorMaskBased(tileImage, highlightImage, player.getColor());
    }

    // prepares the base images and templates
    private Map<TerrainType, BufferedImage> buildImageMap(boolean isTemplate) {
        Map<TerrainType, BufferedImage> map = new HashMap<>();
        for (TerrainType terrainType : TerrainType.basicTerrain()) {
            BufferedImage meepleImage = loadImage(GameSettings.getMeeplePath(terrainType, isTemplate));
            map.put(terrainType, meepleImage);
        }
        return map;
    }

    // Colors a meeple with RGB color.
    private ImageIcon paintMeeple(TerrainType meepleType, int color) {
        BufferedImage image = deepCopy(imageMap.get(meepleType));
        BufferedImage template = templateMap.get(meepleType);
        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                if (template.getRGB(x, y) == Color.BLACK.getRGB()) {
                    image.setRGB(x, y, color);
                }
            }
        }
        return new ImageIcon(image);
    }

    private ImageIcon colorMaskBased(BufferedImage imageToColor, BufferedImage maskImage, Color targetColor) {
        BufferedImage image = deepCopy(imageToColor);
        for (int x = 0; x < maskImage.getWidth(); x++) {
            for (int y = 0; y < maskImage.getHeight(); y++) {
                Color maskPixel = new Color(maskImage.getRGB(x, y), true);
                Color targetPixel = new Color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), maskPixel.getAlpha());
                Color imagePixel = new Color(image.getRGB(x, y), true);
                Color blendedColor = blend(imagePixel, targetPixel);
                image.setRGB(x, y, blendedColor.getRGB());
            }
        }
        return new ImageIcon(image);
    }

    // Blends to colors correctly based on alpha composition.
    private Color blend(Color first, Color second) {
        double totalAlpha = first.getAlpha() + second.getAlpha();
        double firstWeight = first.getAlpha() / totalAlpha;
        double secondWeight = second.getAlpha() / totalAlpha;
        double red = firstWeight * first.getRed() + secondWeight * second.getRed();
        double green = firstWeight * first.getGreen() + secondWeight * second.getGreen();
        double blue = firstWeight * first.getBlue() + secondWeight * second.getBlue();
        int alpha = Math.max(first.getAlpha(), second.getAlpha());
        return new Color((int) red, (int) green, (int) blue, alpha);
    }

    // copies a image to avoid side effects.
    private BufferedImage deepCopy(BufferedImage image) {
        ColorModel model = image.getColorModel();
        boolean isAlphaPremultiplied = model.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(model, raster, isAlphaPremultiplied, null);
    }

    private BufferedImage loadImage(String path) {
        File file = new File(path);
        try {
            return ImageIO.read(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            GameMessage.showError("ERROR: Could not load image loacted at " + path);
            return null;
        }
    }
}