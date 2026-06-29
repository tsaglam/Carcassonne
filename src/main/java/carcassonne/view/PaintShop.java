package carcassonne.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.util.ConcurrentTileImageScaler;
import carcassonne.util.FastImageScaler;
import carcassonne.util.ImageLoadingUtil;
import carcassonne.view.util.ThreadingUtil;

/**
 * This is the Carcassonne paint shop! It paints meeple images and tile highlights! It is implemented as a utility class
 * with static methods to increase performance through avoiding loading images more often that needed.
 * @author Timur Saglam
 */
public final class PaintShop {
    private static final int HIGH_DPI_FACTOR = 2;
    private static final BufferedImage emblemImage = ensureIntArgb(ImageLoadingUtil.EMBLEM.createBufferedImage());
    private static final BufferedImage highlightBaseImage = ImageLoadingUtil.NULL_TILE.createBufferedImage();
    private static final BufferedImage highlightImage = ensureIntArgb(ImageLoadingUtil.HIGHLIGHT.createBufferedImage());
    private static final int MAXIMAL_ALPHA = 255;
    private static final int MAXIMUM_MEEPLE_CACHE_SIZE = 500;
    private static final ConcurrentHashMap<String, ImageIcon> cachedMeepleImages = new ConcurrentHashMap<>();
    private static final Map<TerrainType, BufferedImage> templateMap = buildImageMap(true);
    private static final Map<TerrainType, BufferedImage> imageMap = buildImageMap(false);
    private static final String KEY_SEPARATOR = "|";

    private PaintShop() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Adds the emblem image to the top right of any tile image.
     * @param originalTile is the original tile image without the emblem.
     * @return a copy of the image with an emblem.
     */
    public static Image addEmblem(BufferedImage originalTile) {
        BufferedImage copy = deepCopy(originalTile);
        int[] copyPixels = ((DataBufferInt) copy.getRaster().getDataBuffer()).getData();
        int[] emblemPixels = ((DataBufferInt) emblemImage.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < emblemImage.getHeight(); y++) {
            int copyOffset = y * copy.getWidth();
            int emblemOffset = y * emblemImage.getWidth();
            for (int x = 0; x < emblemImage.getWidth(); x++) {
                int index = copyOffset + x;
                copyPixels[index] = blend(copyPixels[index], emblemPixels[emblemOffset + x], false);
            }
        }
        return copy;
    }

    /**
     * Clears the meeple image cache. Should be cleared when player colors change.
     */
    public static void clearCachedImages() {
        cachedMeepleImages.clear();
    }

    /**
     * Pre-warms the tile image cache by loading and scaling all tile types at full resolution.
     * Should be called once at startup to avoid first-render lag.
     */
    public static void prewarm() {
        ThreadingUtil.runInBackground(() -> TileType.validTiles().parallelStream().forEach(type -> {
            Tile tile = new Tile(type);
            for (TileRotation rotation : TileRotation.values()) {
                tile.rotateTo(rotation);
                ConcurrentTileImageScaler.getScaledMultiResolutionImage(tile, GameSettings.TILE_RESOLUTION, false);
            }
        }));
    }

    /**
     * Returns a custom colored highlight image.
     * @param player determines the color of the highlight.
     * @param size is the edge length in pixels of the image.
     * @return the highlighted tile.
     */
    public static ImageIcon getColoredHighlight(Player player, int size, boolean fastScaling) {
        return createPlayerColoredIcon(highlightBaseImage, player, size, fastScaling);
    }

    /**
     * Returns a colored tile image icon.
     * @param tile is the tile to be colored.
     * @param player is the player whose color is used.
     * @param size is the desired tile size.
     * @param fastScaling determines the rendering quality.
     * @return the colored tile image wrapped in an image icon.
     */
    public static ImageIcon getColoredTile(Tile tile, Player player, int size, boolean fastScaling) {
        Image baseImage = ConcurrentTileImageScaler.getScaledImage(tile, GameSettings.TILE_RESOLUTION, fastScaling);
        BufferedImage bufferedBaseImage = bufferedImageOf(baseImage);
        return createPlayerColoredIcon(bufferedBaseImage, player, size, fastScaling);
    }

    /**
     * Returns a custom colored meeple.
     * @param meepleType is the type of the meeple.
     * @param color is the custom color.
     * @param size is the edge length in pixels of the image.
     * @return the colored meeple.
     */
    public static ImageIcon getColoredMeeple(TerrainType meepleType, Color color, int size) {
        String key = createKey(color, meepleType, size);
        if (cachedMeepleImages.containsKey(key)) {
            return cachedMeepleImages.get(key);
        }
        Image paintedMeeple = paintMeeple(meepleType, color.getRGB(), size * HIGH_DPI_FACTOR);
        ImageIcon icon = new ImageIcon(ImageLoadingUtil.createHighDpiImage(paintedMeeple));
        putMeepleInCache(key, icon);
        return icon;
    }

    /**
     * Returns a custom colored meeple.
     * @param meepleType is the type of the meeple.
     * @param player is the {@link Player} whose color is used.
     * @param size is the edge length in pixels of the image.
     * @return the colored meeple.
     */
    public static ImageIcon getColoredMeeple(TerrainType meepleType, Player player, int size) {
        return getColoredMeeple(meepleType, player.getColor(), size);
    }

    /**
     * Creates a non-colored meeple image icon.
     * @param meepleType is the type of the meeple.
     * @param size is the edge length in pixels of the image.
     * @return non-colored meeple image.
     */
    public static ImageIcon getPreviewMeeple(TerrainType meepleType, int size) {
        String key = createKey(meepleType, size);
        if (cachedMeepleImages.containsKey(key)) {
            return cachedMeepleImages.get(key);
        }
        Image preview = imageMap.get(meepleType).getScaledInstance(size * HIGH_DPI_FACTOR, size * HIGH_DPI_FACTOR, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(ImageLoadingUtil.createHighDpiImage(preview));
        putMeepleInCache(key, icon);
        return icon;
    }

    /**
     * Linearly interpolates between two colors in RGB space.
     * @param startColor the color at fraction {@code 0.0}
     * @param endColor the color at fraction {@code 1.0}
     * @param fraction the interpolation factor, typically in the range {@code [0.0, 1.0]}
     * @return the interpolated color depending on the fraction.
     * @throws IllegalArgumentException if the fraction is outside the range {@code [0.0, 1.0]}
     */
    public static Color interpolateColor(Color startColor, Color endColor, float fraction) {
        int red = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * fraction);
        int green = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * fraction);
        int blue = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * fraction);
        return new Color(red, green, blue);
    }

    /**
     * Blends to colors correctly based on alpha composition. Either blends both colors or applies the second on the first
     * one.
     * @param first is the first color to be applied.
     * @param second is the second color to be applied.
     * @param blendEqually applies the second on the first one of true, blends on alpha values if false.
     * @return the blended color.
     */
    private static int blend(int first, int second, boolean blendEqually) {
        int alpha1 = (first >> 24) & 0xFF;
        int red1 = (first >> 16) & 0xFF;
        int green1 = (first >> 8) & 0xFF;
        int blue1 = first & 0xFF;
        int alpha2 = (second >> 24) & 0xFF;
        int red2 = (second >> 16) & 0xFF;
        int green2 = (second >> 8) & 0xFF;
        int blue2 = second & 0xFF;
        int resultAlpha = Math.max(alpha1, alpha2);
        int resultRed, resultGreen, resultBlue;
        if (blendEqually) {
            int totalAlpha = alpha1 + alpha2;
            if (totalAlpha == 0) {
                return 0;
            }
            resultRed = (red1 * alpha1 + red2 * alpha2) / totalAlpha;
            resultGreen = (green1 * alpha1 + green2 * alpha2) / totalAlpha;
            resultBlue = (blue1 * alpha1 + blue2 * alpha2) / totalAlpha;
        } else {
            resultRed = (red1 * (MAXIMAL_ALPHA - alpha2) + red2 * alpha2) / MAXIMAL_ALPHA;
            resultGreen = (green1 * (MAXIMAL_ALPHA - alpha2) + green2 * alpha2) / MAXIMAL_ALPHA;
            resultBlue = (blue1 * (MAXIMAL_ALPHA - alpha2) + blue2 * alpha2) / MAXIMAL_ALPHA;
        }
        return (resultAlpha << 24) | (resultRed << 16) | (resultGreen << 8) | resultBlue;
    }

    /**
     * Converts a given Image into a BufferedImage. This can be costly and should only be done when really required.
     * @param image is the {@link Image} to be converted.
     * @return a {@link BufferedImage}, either the cast original image or a copy.
     */
    private static BufferedImage bufferedImageOf(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage bufferedImage = ImageLoadingUtil
                .makeCompatible(new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB));
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }

    // prepares the base images and templates
    private static Map<TerrainType, BufferedImage> buildImageMap(boolean isTemplate) {
        Map<TerrainType, BufferedImage> map = new HashMap<>();
        for (TerrainType terrainType : TerrainType.values()) {
            BufferedImage meepleImage = ImageLoadingUtil.createBufferedImage(GameSettings.getMeeplePath(terrainType, isTemplate));
            map.put(terrainType, ensureIntArgb(meepleImage));
        }
        return map;
    }

    private static ImageIcon createPlayerColoredIcon(BufferedImage baseImage, Player player, int size, boolean fastScaling) {
        BufferedImage coloredImage = colorMaskBased(baseImage, highlightImage, player.getColor());
        Image smallImage = scaleDown(coloredImage, size, fastScaling, coloredImage.getTransparency());
        int largeSize = Math.min(size * HIGH_DPI_FACTOR, GameSettings.TILE_RESOLUTION);
        Image largeImage = scaleDown(coloredImage, largeSize, fastScaling, coloredImage.getTransparency());
        return new ImageIcon(new BaseMultiResolutionImage(smallImage, largeImage));
    }

    private static BufferedImage colorMaskBased(BufferedImage imageToColor, BufferedImage maskImage, Color targetColor) {
        BufferedImage image = deepCopy(imageToColor);
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int[] maskPixels = ((DataBufferInt) maskImage.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < maskImage.getHeight(); y++) {
            int offset = y * maskImage.getWidth();
            for (int x = 0; x < maskImage.getWidth(); x++) {
                int index = offset + x;
                int maskAlpha = (maskPixels[index] >> 24) & 0xFF;
                imagePixels[index] = blend(imagePixels[index], (maskAlpha << 24) | (targetColor.getRGB() & 0x00FFFFFF), true);
            }
        }
        return image;
    }

    private static String createKey(Color color, TerrainType meepleType, int size) {
        return createKey(meepleType, size) + color.getRGB();
    }

    private static String createKey(TerrainType meepleType, int size) {
        return meepleType + KEY_SEPARATOR + size + KEY_SEPARATOR;
    }

    private static void putMeepleInCache(String key, ImageIcon icon) {
        cachedMeepleImages.put(key, icon);
        if (cachedMeepleImages.size() > MAXIMUM_MEEPLE_CACHE_SIZE) {
            cachedMeepleImages.remove(cachedMeepleImages.keySet().iterator().next());
        }
    }

    // copies an image to avoid side effects.
    private static BufferedImage deepCopy(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
            int[] src = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            int[] dst = ((DataBufferInt) copy.getRaster().getDataBuffer()).getData();
            System.arraycopy(src, 0, dst, 0, src.length);
        } else {
            Graphics2D graphics = copy.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
        }
        return copy;
    }

    // ensures a BufferedImage is TYPE_INT_ARGB for direct pixel array access.
    private static BufferedImage ensureIntArgb(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
            return image;
        }
        BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = converted.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return converted;
    }

    // Colors a meeple with RGB color.
    private static Image paintMeeple(TerrainType meepleType, int color, int size) {
        BufferedImage image = deepCopy(imageMap.get(meepleType));
        int[] imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        BufferedImage template = templateMap.get(meepleType);
        int[] templatePixels = ((DataBufferInt) template.getRaster().getDataBuffer()).getData();
        int blackRGB = Color.BLACK.getRGB();
        for (int y = 0; y < template.getHeight(); y++) {
            int offset = y * template.getWidth();
            for (int x = 0; x < template.getWidth(); x++) {
                int index = offset + x;
                if (templatePixels[index] == blackRGB) {
                    imagePixels[index] = color;
                }
            }
        }
        return image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }

    private static Image scaleDown(Image image, int size, boolean fastScaling, int transparency) {
        return ImageLoadingUtil.makeCompatible(scaleDown(image, size, fastScaling), transparency);
    }

    private static Image scaleDown(Image image, int size, boolean fastScaling) {
        if (fastScaling) {
            return FastImageScaler.scaleDown(image, size);
        }
        return image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }
}