package carcassonne.model.grid;

/**
 * @author Timur enum for the grid directions.
 */
public enum GridDirection { // TODO (MEDIUM) Naming: Direction/Position, Tile/Grid

    TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, MIDDLE;

    /**
     * Adds a x coordinate and a <code> GridDirection</code>.
     * @param coordinate is the x coordinate.
     * @param dir is the <code> GridDirection</code>.
     * @return the sum as an x coordinate.
     */
    public static int addX(int coordinate, GridDirection dir) {
        int result = coordinate;
        if (dir == TOP_RIGHT || dir == RIGHT || dir == BOTTOM_RIGHT) {
            result++;
        } else if (dir == TOP_LEFT || dir == LEFT || dir == BOTTOM_LEFT) {
            result--;
        }
        return result;
    }

    /**
     * Adds a y coordinate and a <code> GridDirection</code>.
     * @param coordinate is the y coordinate.
     * @param dir is the <code> GridDirection</code>.
     * @return the sum as an y coordinate.
     */
    public static int addY(int coordinate, GridDirection dir) {
        int result = coordinate;
        if (dir == BOTTOM_LEFT || dir == BOTTOM || dir == BOTTOM_RIGHT) {
            result++;
        } else if (dir == TOP_LEFT || dir == TOP || dir == TOP_RIGHT) {
            result--;
        }
        return result;
    }

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
     * Gets the next direction on the specified side of the current direction.
     * @param direction is the current direction.
     * @param side sets the side. -1 for left and 1 for right.
     * @return the next direction
     */
    public static GridDirection next(GridDirection direction, int side) {
        if (side != 1 && side != -1) {
            throw new IllegalArgumentException("Parameter side has to be -1 for left or 1 for right.");
        }
        if (direction == MIDDLE) {
            return direction;
        }
        GridDirection[] cycle = { TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT };
        int position = -2; // error case, sum with parameter side is negative
        for (int i = 0; i < cycle.length; i++) {
            if (cycle[i] == direction) { // find in cycle
                position = i; // save cycle position
            }
        }
        return cycle[(8 + position + side) % 8];
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

    private static int bigOpposite(int ordinal) {
        return 4 + smallOpposite(ordinal - 4);
    }

    private static int smallOpposite(int ordinal) {
        return (ordinal + 2) % 4;
    }
}
