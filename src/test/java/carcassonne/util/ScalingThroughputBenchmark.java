package carcassonne.util;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;
import carcassonne.view.util.ZoomConfig;

/**
 * Concurrent throughput and latency benchmark for {@link ConcurrentTileImageScaler}.
 */
class ScalingThroughputBenchmark {
    private static final int MIN_SIZE = ZoomConfig.MIN_LEVEL.pixels();
    private static final int MAX_SIZE = ZoomConfig.MAX_LEVEL.pixels();
    private static final int STEP_SIZE = ZoomConfig.STEP_SMALL.pixels();
    private static final Random SEED_RNG = new Random(1337);

    private static List<Tile> tiles;
    private static int[] sizes;

    @BeforeAll
    static void setup() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires headful AWT.");
        tiles = new ArrayList<>();
        for (TileType type : TileType.validTiles()) {
            for (TileRotation rotation : TileRotation.values()) {
                Tile tile = new Tile(type);
                tile.rotateTo(rotation);
                tiles.add(tile);
            }
        }
        int range = (MAX_SIZE - MIN_SIZE) / STEP_SIZE + 1;
        sizes = new int[range];
        for (int i = 0; i < range; i++) {
            sizes[i] = MIN_SIZE + i * STEP_SIZE;
        }
    }

    @Test
    @DisplayName("Cold start: scale all tile-variant-size combos once")
    void benchmarkColdStart() {
        TileImageScalingCache.clear();
        int totalOps = tiles.size() * sizes.length;
        long start = System.nanoTime();
        for (Tile tile : tiles) {
            for (int size : sizes) {
                ConcurrentTileImageScaler.getScaledImage(tile, size, true);
            }
        }
        long elapsed = System.nanoTime() - start;
        report("Cold start (" + totalOps + " ops)", elapsed, totalOps);
    }

    @Test
    @DisplayName("Hot cache: scale all combos after warmup")
    void benchmarkHotCache() {
        TileImageScalingCache.clear();
        int totalOps = tiles.size() * sizes.length;
        warmup(totalOps);
        long start = System.nanoTime();
        for (Tile tile : tiles) {
            for (int size : sizes) {
                ConcurrentTileImageScaler.getScaledImage(tile, size, true);
            }
        }
        long elapsed = System.nanoTime() - start;
        report("Hot cache (" + totalOps + " ops)", elapsed, totalOps);
    }

    @Test
    @DisplayName("Parallel: N threads scaling random tiles concurrently")
    void benchmarkParallelScaling() throws InterruptedException {
        TileImageScalingCache.clear();
        int threads = Runtime.getRuntime().availableProcessors();
        int opsPerThread = 500;
        warmup(threads * opsPerThread / 4);
        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
            CountDownLatch latch = new CountDownLatch(threads);
            long start = System.nanoTime();
            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    Random rng = new Random(SEED_RNG.nextLong());
                    for (int j = 0; j < opsPerThread; j++) {
                        Tile tile = tiles.get(rng.nextInt(tiles.size()));
                        int size = sizes[rng.nextInt(sizes.length)];
                        ConcurrentTileImageScaler.getScaledImage(tile, size, true);
                    }
                    latch.countDown();
                });
            }
            latch.await(120, TimeUnit.SECONDS);
            long elapsed = System.nanoTime() - start;
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            report("Parallel " + threads + " threads (" + (threads * opsPerThread) + " ops)", elapsed, threads * opsPerThread);
        }
    }

    private static void warmup(int opsToCover) {
        for (Tile tile : tiles) {
            for (int size : sizes) {
                ConcurrentTileImageScaler.getScaledImage(tile, size, true);
                if (--opsToCover <= 0)
                    return;
            }
        }
    }

    private static void report(String label, long elapsedNanos, int ops) {
        double opsPerSec = ops * 1_000_000_000.0 / elapsedNanos;
        double avgUs = elapsedNanos / (double) ops / 1000.0;
        long totalMs = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        System.out.printf("  %-45s %8.1f ops/s  %7.1f us/op  (%5d ms)%n", label, opsPerSec, avgUs, totalMs);
    }
}
