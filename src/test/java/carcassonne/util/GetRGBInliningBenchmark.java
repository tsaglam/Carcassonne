package carcassonne.util;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Micro-benchmark to determine whether {@link Color#getRGB()} inside a tight pixel loop is optimized away by the JIT or
 * causes measurable overhead.
 */
class GetRGBInliningBenchmark {
    private static final int SIZE = 500;
    private static final int WARMUP = 30;
    private static final int ITERATIONS = 200;
    private static final long SEED = 42;

    private static int[] imagePixels;
    private static int[] maskPixels;
    private static Color targetColor;

    @BeforeAll
    static void setup() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires headful AWT.");
        Random rng = new Random(SEED);
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        imagePixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        BufferedImage mask = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        maskPixels = ((DataBufferInt) mask.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < imagePixels.length; i++) {
            imagePixels[i] = rng.nextInt() | 0xFF000000;
            maskPixels[i] = rng.nextInt();
        }
        targetColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
    }

    @Test
    void benchmarkInlinedGetRGB() {
        warmupInlined();
        long start = System.nanoTime();
        for (int iter = 0; iter < ITERATIONS; iter++) {
            colorMaskInlined(imagePixels, maskPixels, targetColor, SIZE);
        }
        long elapsed = System.nanoTime() - start;
        report("getRGB() inlined in loop", elapsed);
    }

    @Test
    void benchmarkExtractedGetRGB() {
        warmupExtracted();
        long start = System.nanoTime();
        for (int iter = 0; iter < ITERATIONS; iter++) {
            colorMaskExtracted(imagePixels, maskPixels, targetColor, SIZE);
        }
        long elapsed = System.nanoTime() - start;
        report("getRGB() extracted before loop", elapsed);
    }

    private static void warmupInlined() {
        for (int i = 0; i < WARMUP; i++) {
            Random rng = new Random(SEED + i);
            colorMaskInlined(imagePixels, maskPixels, new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)), SIZE);
        }
    }

    private static void warmupExtracted() {
        for (int i = 0; i < WARMUP; i++) {
            Random rng = new Random(SEED + i);
            colorMaskExtracted(imagePixels, maskPixels, new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256)), SIZE);
        }
    }

    private static void colorMaskInlined(int[] img, int[] mask, Color target, int w) {
        for (int y = 0; y < SIZE; y++) {
            int offset = y * w;
            for (int x = 0; x < SIZE; x++) {
                int index = offset + x;
                int maskAlpha = (mask[index] >> 24) & 0xFF;
                img[index] = blend(img[index], (maskAlpha << 24) | (target.getRGB() & 0x00FFFFFF), true);
            }
        }
    }

    private static void colorMaskExtracted(int[] img, int[] mask, Color target, int w) {
        int targetRGB = target.getRGB();
        for (int y = 0; y < SIZE; y++) {
            int offset = y * w;
            for (int x = 0; x < SIZE; x++) {
                int index = offset + x;
                int maskAlpha = (mask[index] >> 24) & 0xFF;
                img[index] = blend(img[index], (maskAlpha << 24) | (targetRGB & 0x00FFFFFF), true);
            }
        }
    }

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
            if (totalAlpha == 0)
                return 0;
            resultRed = (red1 * alpha1 + red2 * alpha2) / totalAlpha;
            resultGreen = (green1 * alpha1 + green2 * alpha2) / totalAlpha;
            resultBlue = (blue1 * alpha1 + blue2 * alpha2) / totalAlpha;
        } else {
            resultRed = (red1 * (255 - alpha2) + red2 * alpha2) / 255;
            resultGreen = (green1 * (255 - alpha2) + green2 * alpha2) / 255;
            resultBlue = (blue1 * (255 - alpha2) + blue2 * alpha2) / 255;
        }
        return (resultAlpha << 24) | (resultRed << 16) | (resultGreen << 8) | resultBlue;
    }

    private static void report(String label, long elapsedNanos) {
        double avgUs = elapsedNanos / (double) ITERATIONS / 1000.0;
        long totalMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        System.out.printf("  %-35s %8.1f us/op  (%3d iter, %5d ms)%n", label, avgUs, ITERATIONS, totalMs);
    }
}
