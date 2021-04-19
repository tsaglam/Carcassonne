package carcassonne.util;

/**
 * Utility class for the calculation of the Minkowski distance, including Manhattan and Euclidean distance.
 * @author Timur Saglam
 */
public enum MinkowskiDistance {
    COMPASS(Math.pow(2, -0.5), "Compass"),
    MANHATTAN(1, "Diamond"),
    EUCLIDEAN(2, "Round"),
    ROUNDED_SQUARE(Math.pow(2, 1.5), "Rounded Square");

    private final double order;
    private final String description;

    /**
     * Minkowski distance of a certain order.
     * @param order specifies the order.
     */
    private MinkowskiDistance(double order, String description) {
        this.order = order;
        this.description = description;
    }

    /**
     * Calculates the distance between two points P(x, y) and Q(x, y).
     * @param x1 is the X-coordinate of P.
     * @param y1 is the Y-coordinate of P.
     * @param x2 is the X-coordinate of Q.
     * @param y2 is the Y-coordinate of Q.
     * @return the distance.
     */
    public double distance(int x1, int y1, int x2, int y2) {
        return calculate(x1, y1, x2, y2, order);
    }

    /**
     * Returns a textual description of the geometric properties of the distance measure.
     * @return the textual description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Calculates the Minkowski distance of a certain order between two points P(x, y) and Q(x, y).
     * @param x1 is the X-coordinate of P.
     * @param y1 is the Y-coordinate of P.
     * @param x2 is the X-coordinate of Q.
     * @param y2 is the Y-coordinate of Q.
     * @param order is the Minkowski distance order.
     * @return the distance.
     */
    public static double calculate(int x1, int y1, int x2, int y2, double order) {
        return root(Math.pow(Math.abs(x1 - x2), order) + Math.pow(Math.abs(y1 - y2), order), order);
    }

    /**
     * Calculates the root of a certain degree of a number.
     */
    private static double root(double number, double degree) {
        return Math.round(Math.pow(number, 1 / degree));
    }

}
