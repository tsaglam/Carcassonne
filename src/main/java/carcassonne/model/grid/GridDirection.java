package carcassonne.model.grid;

/**
 * @author Timur enum for the grid directions.
 */
public enum GridDirection {

    TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, MIDDLE;

    /**
     * Generates an array of the GridDirections for a neighbor on the grid.
     * @return an array of all directions except MIDDLE.
     */
    public static GridDirection[] neighbors() {
        GridDirection[] directions = { TOP, RIGHT, BOTTOM, LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT };
        return directions;
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
}
