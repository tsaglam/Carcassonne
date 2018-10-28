package carcassonne.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.model.terrain.TerrainType;

/**
 * This is the Carcassonne paint shop! It paints the meeple images!
 * @author Timur Saglam
 */
public class PaintShop {
    private final Map<TerrainType, BufferedImage> imageMap;
    private final GameOptions options;
    private final Map<TerrainType, BufferedImage> templateMap;

    /**
     * Basic constructor, creates base images and templates.
     */
    public PaintShop() {
        options = GameOptions.getInstance();
        imageMap = buildImageMap(false);
        templateMap = buildImageMap(true);
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
    public ImageIcon getColoredMeeple(TerrainType meepleType, int playerNumber) {
        return getColoredMeeple(meepleType, options.getPlayerColor(playerNumber));
    }

    // prepares the base images and templates
    private Map<TerrainType, BufferedImage> buildImageMap(boolean isTemplate) {
        Map<TerrainType, BufferedImage> map = new HashMap<>();
        for (TerrainType terrainType : TerrainType.basicTerrain()) {
            File file = new File(options.getMeeplePath(terrainType, isTemplate));
            try {
                map.put(terrainType, ImageIO.read(file));
            } catch (IOException exception) {
                System.err.println(exception.getMessage());
            }
        }
        return map;
    }

    // Colors a meeple with RGB color.
    private ImageIcon paintMeeple(TerrainType meepleType, int color) {
        BufferedImage image = imageMap.get(meepleType);
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
}