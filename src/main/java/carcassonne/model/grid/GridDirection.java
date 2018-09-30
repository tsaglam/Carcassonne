package carcassonne.model.grid;

/**
 * Enumeration for grid directions and tile positions. It is used either to specify a direction on the grid from a
 * specific tile, or to specify a position on a tile.
 * @author Timur Saglam
 */
public enum GridDirection { // TODO (MEDIUM) Naming: Direction/Position, Tile/Grid

    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
    TOP_RIGHT,
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
    TOP_LEFT,
    MIDDLE;

    /**
     * Adds a x coordinate and a <code> GridDirection</code>.
     * @param coordinate is the x coordinate.
     * @return the sum as an x coordinate.
     */
    public int addX(int coordinate) {
        int result = coordinate;
        if (this == TOP_RIGHT || this == RIGHT || this == BOTTOM_RIGHT) {
            result++;
        } else if (this == TOP_LEFT || this == LEFT || this == BOTTOM_LEFT) {
            result--;
        }
        return result;
    }

    /**
     * Adds a y coordinate and a <code> GridDirection</code>.
     * @param coordinate is the y coordinate.
     * @return the sum as an y coordinate.
     */
    public int addY(int coordinate) {
        int result = coordinate;
        if (this == BOTTOM_LEFT || this == BOTTOM || this == BOTTOM_RIGHT) {
            result++;
        } else if (this == TOP_LEFT || this == TOP || this == TOP_RIGHT) {
            result--;
        }
        return result;
    }

    /**
     * Checks whether the this grid direction is directly to the left of another grid direction.
     * @param other is the other grid direction.
     * @return true if it is.
     */
    public boolean isLeftOf(GridDirection other) {
        return next(1) == other;
    }

    /**
     * Checks whether the this grid direction is directly to the right of another grid direction.
     * @param other is the other grid direction.
     * @return true if it is.
     */
    public boolean isRightOf(GridDirection other) {
        return next(-1) == other;
    }

    /**
     * Checks whether the ordinal of a direction is smaller or equal than the ordinal of another direction.
     * @param other is the other direction.
     * @return true if smaller or equal.
     */
    public boolean isSmallerOrEquals(GridDirection other) {
        return ordinal() <= other.ordinal();
    }

    /**
     * Gets the next direction on the specified side of the current direction.
     * @param side sets the side. -1 for left and 1 for right.
     * @return the next direction
     */
    public GridDirection next(int side) {
        if (side != 1 && side != -1) {
            throw new IllegalArgumentException("Parameter side has to be -1 for left or 1 for right.");
        }
        if (this == MIDDLE) {
            return this;
        }
        GridDirection[] cycle = { TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT };
        int position = -2; // error case, sum with parameter side is negative
        for (int i = 0; i < cycle.length; i++) {
            if (cycle[i] == this) { // find in cycle
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
    public GridDirection opposite() {
        if (ordinal() <= 3) { // for TOP, RIGHT, BOTTOM and LEFT:
            return values()[smallOpposite(ordinal())];
        } else if (ordinal() <= 7) { // for TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT and TOP_LEFT:
            return values()[bigOpposite(ordinal())];
        }
        return MIDDLE; // middle is the opposite of itself.
    }

    private int bigOpposite(int ordinal) {
        return 4 + smallOpposite(ordinal - 4);
    }

    private int smallOpposite(int ordinal) {
        return (ordinal + 2) % 4;
    }

    /**
     * Generates an array of the GridDirections for a direct neighbor on the grid.
     * @return an array of TOP, RIGHT, BOTTOM and LEFT.
     */
    public static GridDirection[] directNeighbors() {
        return new GridDirection[] { TOP, RIGHT, BOTTOM, LEFT };
    }

    /**
     * Generates an array of the GridDirections for a indirect neighbor on the grid.
     * @return an array of TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT and TOP_LEFT.
     */
    public static GridDirection[] indirectNeighbors() {
        return new GridDirection[] { TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT };
    }

    /**
     * Generates an array of the GridDirections for a neighbor on the grid.
     * @return an array of all directions except MIDDLE.
     */
    public static GridDirection[] neighbors() {
        return new GridDirection[] { TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT };
    }

    /**
     * Generates an array of the GridDirections for all positions on a tile.
     * @return an array of an array of TOP, RIGHT, BOTTOM, LEFT and MIDDLE.
     */
    public static GridDirection[] tilePositions() {
        return new GridDirection[] { TOP, RIGHT, BOTTOM, LEFT, MIDDLE };
    }

    /**
     * Generates a two dimensional array of the GridDirections for their orientation on a tile.
     * @return a 2D array of an array of TOP_LEFT, LEFT, BOTTOM_LEFT, TOP, MIDDLE, BOTTOM, TOP_RIGHT, RIGHT and
     * BOTTOM_RIGHT.
     */
    public static GridDirection[][] values2D() {
        return new GridDirection[][] { { TOP_LEFT, LEFT, BOTTOM_LEFT }, { TOP, MIDDLE, BOTTOM }, { TOP_RIGHT, RIGHT, BOTTOM_RIGHT } };
    }
}
