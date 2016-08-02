package carcassonne.model.grid;

/**
 * @author Timur enum for the grid directions.
 */
public enum GridDirection {

    TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, MIDDLE;

    /**
     * Generates an array of the GridDirections for a direct neighbor on the grid.
     * @return an array of TOP, RIGHT, BOTTOM and LEFT.
     */
    public static GridDirection[] directNeighbors() {
        GridDirection[] directions = { TOP, RIGHT, BOTTOM, LEFT };
        return directions;
    }

    /**
     * Generates an array of the GridDirections for a indirect neighbor on the grid.
     * @return an array of TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT and TOP_LEFT.
     */
    public static GridDirection[] indirectNeighbors() {
        GridDirection[] directions = { TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT };
        return directions;
    }

    /**
     * Generates an array of the GridDirections for a neighbor on the grid.
     * @return an array of all directions except MIDDLE.
     */
    public static GridDirection[] neighbors() {
        GridDirection[] directions = { TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT };
        return directions;
    }

    /**
     * Generates an array of the GridDirections for all positions on a tile.
     * @return an array of an array of TOP, RIGHT, BOTTOM, LEFT and MIDDLE.
     */
    public static GridDirection[] tilePositions() {
        GridDirection[] directions = { TOP, RIGHT, BOTTOM, LEFT, MIDDLE };
        return directions;
    }

    /**
     * Generates a two dimensional array of the GridDirections for their orientation on a tile.
     * @return a 2D array of an array of TOP_LEFT, LEFT, BOTTOM_LEFT, TOP, MIDDLE, BOTTOM,
     * TOP_RIGHT, RIGHT and BOTTOM_RIGHT.
     */
    public static GridDirection[][] values2D() {
        GridDirection[][] directions = { { TOP_LEFT, LEFT, BOTTOM_LEFT }, { TOP, MIDDLE, BOTTOM }, { TOP_RIGHT, RIGHT, BOTTOM_RIGHT } };
        return directions;
    }

    /**
     * Calculates the opposite <code>GridDirection</code> for a specific <code>GridDirection</code>.
     * @param from is the <code>GridDirection</code> the opposite gets calculated from.
     * @return the opposite <code>GridDirection</code>.
     */
    public static GridDirection opposite(GridDirection from) {
        int ordinal = from.ordinal(); // get number of enum value
        if (ordinal <= 3) { // for TOP, RIGHT, BOTTOM and LEFT:
            return values()[smallOpposite(ordinal)];
        } else if (ordinal <= 7) { // for TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT and TOP_LEFT:
            return values()[bigOpposite(ordinal)];
        }
        return MIDDLE; // middle is the opposite of itself.
    }

    private static int smallOpposite(int ordinal) {
        return (ordinal + 2) % 4;
    }

    private static int bigOpposite(int ordinal) {
        return 4 + smallOpposite(ordinal - 4);
    }
}
