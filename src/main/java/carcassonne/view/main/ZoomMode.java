package carcassonne.view.main;

/**
 * Enumeration for the three zoom modes. Determines the image scaling quality and what images are scaled.
 * @author Timur Saglam
 */
public enum ZoomMode {
    /**
     * Smooth image scaling, takes longer.
     */
    SMOOTH,

    /**
     * Fast image scaling, but with highlight scaling.
     */
    FAST,
}
