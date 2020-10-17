package carcassonne.model.grid;

import java.util.List;
import java.util.Locale;

import carcassonne.model.terrain.RotationDirection;

/**
 * Enumeration for grid directions and tile positions. It is used either to specify a direction on the grid from a
 * specific tile, or to specify a position on a tile.
 * @author Timur Saglam
 */
public enum GridDirection {
    NORTH,
    EAST,
    SHOUTH,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH_WEST,
    CENTER;

    /**
     * Returns the X coordinate of a <code>GridDirection</code>.
     * @return either -1, 0, or 1.
     */
    public int getX() {
        if (this == NORTH_EAST || this == EAST || this == SOUTH_EAST) {
            return 1;
        } else if (this == NORTH_WEST || this == WEST || this == SOUTH_WEST) {
            return -1;
        }
        return 0;
    }

    /**
     * Returns the Y coordinate of a <code>GridDirection</code>.
     * @return either -1, 0, or 1.
     */
    public int getY() {
        if (this == SOUTH_WEST || this == SHOUTH || this == SOUTH_EAST) {
            return 1;
        } else if (this == NORTH_WEST || this == NORTH || this == NORTH_EAST) {
            return -1;
        }
        return 0;
    }

    /**
     * Checks whether the this grid direction is directly to the left of another grid direction.
     * @param other is the other grid direction.
     * @return true if it is.
     */
    public boolean isLeftOf(GridDirection other) {
        return nextDirectionTo(RotationDirection.RIGHT) == other;
    }

    /**
     * Checks whether the this grid direction is directly to the right of another grid direction.
     * @param other is the other grid direction.
     * @return true if it is.
     */
    public boolean isRightOf(GridDirection other) {
        return nextDirectionTo(RotationDirection.LEFT) == other;
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
     * @param side sets the side.
     * @return the next direction
     */
    public GridDirection nextDirectionTo(RotationDirection side) {
        if (this == CENTER) {
            return this;
        }
        GridDirection[] cycle = { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SHOUTH, SOUTH_WEST, WEST, NORTH_WEST };
        int position = -2; // error case, sum with parameter side is negative
        for (int i = 0; i < cycle.length; i++) {
            if (cycle[i] == this) { // find in cycle
                position = i; // save cycle position
            }
        }
        return cycle[(cycle.length + position + side.getValue()) % cycle.length];
    }

    /**
     * Calculates the opposite <code>GridDirection</code> for a specific <code>GridDirection</code>.
     * @return the opposite <code>GridDirection</code>.
     */
    public GridDirection opposite() {
        if (ordinal() <= 3) { // for NORTH, EAST, SHOUTH and WEST:
            return values()[smallOpposite(ordinal())];
        } else if (ordinal() <= 7) { // for NORTH_EAST, SOUTH_EAST, SOUTH_WEST and NORTH_WEST:
            return values()[bigOpposite(ordinal())];
        }
        return CENTER; // middle is the opposite of itself.
    }

    /**
     * Returns a lower case version of the grid direction with spaces instead of underscores.
     * @return the readable version.
     */
    public String toReadableString() {
        return toString().toLowerCase(Locale.UK).replace('_', ' ');
    }

    private int bigOpposite(int ordinal) {
        return 4 + smallOpposite(ordinal - 4);
    }

    private int smallOpposite(int ordinal) {
        return (ordinal + 2) % 4;
    }

    /**
     * Generates a list of the GridDirections for a direct neighbor on the grid.
     * @return a list of NORTH, EAST, SHOUTH and WEST.
     */
    public static List<GridDirection> directNeighbors() {
        return List.of(NORTH, EAST, SHOUTH, WEST);
    }

    /**
     * Generates a list of the GridDirections for a indirect neighbor on the grid.
     * @return a list of NORTH_EAST, SOUTH_EAST, SOUTH_WEST and NORTH_WEST.
     */
    public static List<GridDirection> indirectNeighbors() {
        return List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    /**
     * Generates a list of the GridDirections for a neighbor on the grid.
     * @return a list of all directions except CENTER.
     */
    public static List<GridDirection> neighbors() {
        return List.of(NORTH, EAST, SHOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    /**
     * Generates a list of the GridDirections for all positions on a tile.
     * @return a list of NORTH, EAST, SHOUTH, WEST and CENTER.
     */
    public static List<GridDirection> tilePositions() {
        return List.of(NORTH, EAST, SHOUTH, WEST, CENTER);
    }

    /**
     * Generates a list of the GridDirections by row.
     * @return a list of NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SHOUTH, SOUTH_EAST in that order.
     */
    public static List<GridDirection> byRow() {
        return List.of(NORTH_WEST, NORTH, NORTH_EAST, WEST, CENTER, EAST, SOUTH_WEST, SHOUTH, SOUTH_EAST);
    }

    /**
     * Generates a two dimensional list of the GridDirections for their orientation on a tile.
     * @return a 2D list of of NORTH_WEST, WEST, SOUTH_WEST, NORTH, CENTER, SHOUTH, NORTH_EAST, EAST and SOUTH_EAST.
     */
    public static GridDirection[][] values2D() {
        return new GridDirection[][] { { NORTH_WEST, WEST, SOUTH_WEST }, { NORTH, CENTER, SHOUTH }, { NORTH_EAST, EAST, SOUTH_EAST } };
    }
}
