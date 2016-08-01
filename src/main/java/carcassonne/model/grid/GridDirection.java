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
}
