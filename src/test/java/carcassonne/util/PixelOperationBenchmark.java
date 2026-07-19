package carcassonne.util;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.PaintShop;

/**
 * Benchmarks the pixel-level image operations in {@link PaintShop}. Run:
 * {@code mvn test -Dtest=PixelOperationBenchmark}
 */
class PixelOperationBenchmark {
    private static final int TILE_SIZE = 500;
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASURE_ITERATIONS = 20;
    private static final long SEED = 42;

    private static BufferedImage testTileImage;
    private static Player[] players;

    @BeforeAll
    static void setup() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires headful AWT.");
        Random random = new Random(SEED);
        testTileImage = createRandomImage(TILE_SIZE, TILE_SIZE, random);
        players = new Player[] {new Player(1, new GameSettings())};
    }

    @Test
    void benchmarkAddEmblem() {
        System.out.println("\n--- addEmblem (" + TILE_SIZE + "x" + TILE_SIZE + " tile, emblem region blending) ---");
        warmup(() -> PaintShop.addEmblem(testTileImage));
        long elapsed = measure(() -> PaintShop.addEmblem(testTileImage));
        report("addEmblem", elapsed);
    }

    @Test
    void benchmarkColoredHighlight() {
        System.out.println("\n--- getColoredHighlight (" + TILE_SIZE + "x" + TILE_SIZE + " colorMaskBased + scale) ---");
        Player player = players[0];
        warmup(() -> PaintShop.getColoredHighlight(player, TILE_SIZE, false));
        long elapsed = measure(() -> PaintShop.getColoredHighlight(player, TILE_SIZE, false));
        report("getColoredHighlight", elapsed);
    }

    @Test
    void benchmarkColoredMeeple() {
        System.out.println("\n--- getColoredMeeple (paintMeeple + scale + HiDPI, " + TILE_SIZE + "px template) ---");
        Player player = players[0];
        PaintShop.clearCachedImages();
        warmup(() -> {
            PaintShop.clearCachedImages();
            PaintShop.getColoredMeeple(TerrainType.ROAD, player, TILE_SIZE / 2);
        });
        long elapsed = 0;
        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            PaintShop.clearCachedImages();
            long start = System.nanoTime();
            PaintShop.getColoredMeeple(TerrainType.ROAD, player, TILE_SIZE / 2);
            elapsed += System.nanoTime() - start;
        }
        report("getColoredMeeple", elapsed);
    }

    @Test
    void benchmarkCombinedWorkload() {
        System.out.println("\n=== COMBINED WORKLOAD (" + TILE_SIZE + "x" + TILE_SIZE + " images) ===");
        System.out.println("Each iteration: addEmblem + getColoredHighlight + getColoredMeeple");
        Player player = players[0];

        warmup(() -> {
            PaintShop.addEmblem(testTileImage);
            PaintShop.getColoredHighlight(player, TILE_SIZE, false);
            PaintShop.clearCachedImages();
            PaintShop.getColoredMeeple(TerrainType.ROAD, player, TILE_SIZE / 2);
        });

        long elapsed = 0;
        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            PaintShop.clearCachedImages();
            long start = System.nanoTime();
            PaintShop.addEmblem(testTileImage);
            PaintShop.getColoredHighlight(player, TILE_SIZE, false);
            PaintShop.getColoredMeeple(TerrainType.ROAD, player, TILE_SIZE / 2);
            elapsed += System.nanoTime() - start;
        }
        report("combined", elapsed);
    }

    private static void warmup(Runnable task) {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            task.run();
        }
    }

    private static long measure(Runnable task) {
        long start = System.nanoTime();
        for (int i = 0; i < MEASURE_ITERATIONS; i++) {
            task.run();
        }
        return System.nanoTime() - start;
    }

    private static void report(String name, long elapsedNanos) {
        double avgUs = elapsedNanos / (double) MEASURE_ITERATIONS / 1000.0;
        long totalMs = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        System.out.printf("  %-30s %8.1f us avg  (%3d iter, %5d ms total)%n", name, avgUs, MEASURE_ITERATIONS, totalMs);
    }

    private static BufferedImage createRandomImage(int width, int height, Random random) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((java.awt.image.DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = random.nextInt() | 0xFF000000;
        }
        return image;
    }
}
