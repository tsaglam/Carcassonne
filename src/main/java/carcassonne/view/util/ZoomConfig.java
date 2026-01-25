package carcassonne.view.util;

/**
 * Defines zoom configuration constants for the game view.
 */
public enum ZoomConfig {
    MAX_LEVEL(320),
    MIN_LEVEL(16),
    DEFAULT_LEVEL(128),
    STEP(25),
    STEP_SMALL(5);

    private final int pixels;

    ZoomConfig(int pixels) {
        this.pixels = pixels;
    }

    /*
     * @return the zoom level constant in pixels.
     */
    public int pixels() {
        return pixels;
    }
}