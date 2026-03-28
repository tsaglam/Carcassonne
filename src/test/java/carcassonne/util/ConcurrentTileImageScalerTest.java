package carcassonne.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;
import carcassonne.view.util.ZoomConfig;

/**
 * JUnit 5 test for {@link ConcurrentTileImageScaler}. Tests thread-safety and correctness of concurrent image scaling
 * operations.
 */
class ConcurrentTileImageScalerTest {

    private static final int MIN_SIZE = ZoomConfig.MIN_LEVEL.pixels();
    private static final int MAX_SIZE = ZoomConfig.MAX_LEVEL.pixels();
    private static final int STEP_SIZE = ZoomConfig.STEP_SMALL.pixels();

    private static final int THREAD_COUNT = 200;
    private static final int OPERATIONS_PER_THREAD = 300;
    public static final int TARGET_SIZE = 128;

    private List<Tile> validTiles;

    @BeforeAll
    static void requireHeadfulAwt() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires headful AWT to run.");
    }

    @BeforeEach
    void setUp() {
        validTiles = TileType.validTiles().stream().map(Tile::new).toList();
        assertFalse(validTiles.isEmpty(), "Valid tiles list should not be empty!");
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledImage_BasicFunctionality(boolean fastScaling) {
        Tile tile = validTiles.getFirst();
        Image image = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);
        checkImageProperties(image);
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledMultiResolutionImage_BasicFunctionality(boolean fastScaling) {
        Tile tile = validTiles.getFirst();
        Image image = ConcurrentTileImageScaler.getScaledMultiResolutionImage(tile, TARGET_SIZE, fastScaling);
        checkImageProperties(image);
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testGetScaledImage_Caching(boolean fastScaling) {
        Tile tile = validTiles.getFirst();

        Image image1 = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);
        Image image2 = ConcurrentTileImageScaler.getScaledImage(tile, TARGET_SIZE, fastScaling);

        checkImageProperties(image1);
        checkImageProperties(image2);
        assertSame(image1, image2, "Cached images should be the same instance!");
    }

    private static void checkImageProperties(Image image) {
        assertNotNull(image, "Scaled image should not be null");
        assertEquals(TARGET_SIZE, image.getWidth(null), "Image width should match target size!");
        assertEquals(TARGET_SIZE, image.getHeight(null), "Image height should match target size!");
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testConcurrentScaling(boolean fastScaling) throws InterruptedException {
        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
            AtomicInteger exceptionCount = new AtomicInteger(0);
            List<Tile> tiles = createTestTiles();

            for (int i = 0; i < THREAD_COUNT; i++) {
                executor.submit(() -> {
                    try {
                        Random random = new Random();
                        for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                            Tile tile = tiles.get(random.nextInt(tiles.size()));
                            int targetSize = MIN_SIZE + STEP_SIZE * random.nextInt((MAX_SIZE - MIN_SIZE) / STEP_SIZE + 1);

                            Image image = ConcurrentTileImageScaler.getScaledImage(tile, targetSize, fastScaling);
                            assertNotNull(image, "Scaled image should not be null");
                        }
                    } catch (Exception e) {
                        exceptionCount.incrementAndGet();
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            assertTrue(latch.await(30, TimeUnit.SECONDS), "Test should complete within timeout");
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor should terminate");

            assertEquals(0, exceptionCount.get(), "No exceptions should occur during concurrent scaling");
        }
    }

    @ParameterizedTest(name = "fast scaling = {0}")
    @ValueSource(booleans = {true, false})
    void testConcurrentMultiResolutionScaling(boolean fastScaling) throws InterruptedException {
        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
            AtomicInteger exceptionCount = new AtomicInteger(0);
            List<Tile> tiles = createTestTiles();

            for (int i = 0; i < THREAD_COUNT; i++) {
                executor.submit(() -> {
                    try {
                        Random random = new Random();
                        for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                            Tile tile = tiles.get(random.nextInt(tiles.size()));
                            int targetSize = MIN_SIZE + STEP_SIZE * random.nextInt((MAX_SIZE - MIN_SIZE) / STEP_SIZE + 1);

                            Image image = ConcurrentTileImageScaler.getScaledMultiResolutionImage(tile, targetSize, fastScaling);
                            assertNotNull(image, "Multi-resolution image should not be null");
                        }
                    } catch (Exception e) {
                        exceptionCount.incrementAndGet();
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            assertTrue(latch.await(60, TimeUnit.SECONDS), "Test should complete within timeout");
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor should terminate");
            assertEquals(0, exceptionCount.get(), "No exceptions should occur during concurrent multi-resolution scaling");
        }
    }

    private List<Tile> createTestTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (TileType type : TileType.validTiles()) {
            for (TileRotation rotation : TileRotation.values()) {
                Tile tile = new Tile(type);
                tile.rotateTo(rotation);
                tiles.add(tile);
            }
        }
        return tiles;
    }
}