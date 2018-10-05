package carcassonne.model.terrain;

/**
 * Rotation direction enumeration. Left for anti-clockwise and right for clockwise.
 * @author Timur Saglam
 */
public enum RotationDirection {
    LEFT,
    RIGHT;

    /**
     * Returns an integer for the rotation direction.
     * @return 1 for right and -1 for left.
     */
    public int toInt() {
        if (this == RIGHT) {
            return 1;
        }
        return -1;
    }
}
