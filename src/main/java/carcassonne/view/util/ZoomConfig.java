package carcassonne.view.util;

/**
 * Defines zoom configuration constants for the game view.
 */
public enum ZoomConfig {
    MAX_LEVEL(300),
    MIN_LEVEL(25),
    DEFAULT_LEVEL(125),
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