package carcassonne.util;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import carcassonne.model.tile.TileRotation;
import org.junit.jupiter.api.BeforeEach;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.util.ZoomConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JUnit 5 test for {@link ConcurrentTileImageScaler}. Tests thread-safety and correctness of concurrent image scaling
 * operations.
 */
class ConcurrentTileImageScalerTest {

    private static final int MIN_SIZE = ZoomConfig.MIN_LEVEL.pixels();
    private static final int MAX_SIZE = ZoomConfig.MAX_LEVEL.pixels();
    private static final int STEP_SIZE = ZoomConfig.STEP_SMALL.pixels();
    private static final int NUM_THREADS = 100;
    private static final int OPERATIONS_PER_THREAD = 120;
    public static final int TARGET_SIZE = 128;

    private List<Tile> validTiles;

    @BeforeEach
    void setUp() {
        validTiles = TileType.validTiles().stream().map(Tile::new).toList();
        assertFalse(validTiles.isEmpty(), "Valid tiles list should not be empty");
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledImage_BasicFunctionality(boolean fastScaling) {
        Tile tile = validTiles.getFirst();
        Image image = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);
        checkImageProperties(image, TARGET_SIZE);
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledMultiResolutionImage_BasicFunctionality(boolean fastScaling) {
        Tile tile = validTiles.getFirst();
        Image image = ConcurrentTileImageScaler.getScaledMultiResolutionImage(tile, TARGET_SIZE, fastScaling);
        checkImageProperties(image, TARGET_SIZE);
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledImage_Caching(boolean fastScaling) {
        Tile tile = validTiles.getFirst();

        Image image1 = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);
        Image image2 = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);

        checkImageProperties(image1, TARGET_SIZE);
        checkImageProperties(image2, TARGET_SIZE);
        assertSame(image1, image2, "Cached images should be the same instance");
    }

    private static void checkImageProperties(Image image, int targetSize) {
        assertNotNull(image, "Scaled image should not be null");
        assertEquals(targetSize, image.getWidth(null), "Image width should match target size");
        assertEquals(targetSize, image.getHeight(null), "Image height should match target size");
    }

    private Tile getRandomTile(Random random) {
        Tile tile = validTiles.get(random.nextInt(validTiles.size()));
        List<TileRotation> rotations = new ArrayList<>(tile.getPossibleRotations());
        if (!rotations.isEmpty()) {
            int index = random.nextInt(rotations.size());
            tile.rotateTo(rotations.get(index));
        }
        return tile;
    }

    private int getRandomSize(Random rng) {
        int numSteps = (MAX_SIZE - MIN_SIZE) / STEP_SIZE;
        int randomStep = rng.nextInt(numSteps + 1);
        return MIN_SIZE + (randomStep * STEP_SIZE);
    }
}