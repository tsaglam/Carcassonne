package carcassonne.model.terrain;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * This class stores the information in which direction a corner with the terrain <code>TerrainType.FIELDS</code> is
 * connected to.
 * @author Timur Saglam
 */
public class FieldsCorner {
    private static final int LEFT = 4;
    private static final int RIGHT = 3;
    private final List<GridDirection> connectedDirections;
    private final GridDirection cornerDirection;

    /**
     * Creates a fields corner of a tile.
     * @param cornerDirection is the direction of the corner.
     * @param tile is the tile where the corner is on.
     */
    public FieldsCorner(GridDirection cornerDirection, Tile tile) {
        checkArguments(cornerDirection, tile);
        this.cornerDirection = cornerDirection;
        connectedDirections = new LinkedList<GridDirection>();
        addIfConnected(LEFT, tile);
        addIfConnected(RIGHT, tile);
    }

    /**
     * Getter for the list of connected directions.
     * @return the connected directions.
     */
    public List<GridDirection> getConnectedDirections() {
        return connectedDirections;
    }

    /**
     * getter for the direction of the corner.
     * @return the corner direction
     */
    public GridDirection getCornerDirection() {
        return cornerDirection;
    }

    @Override
    public String toString() {
        return "FieldsCorner[direction: " + cornerDirection + ", connected to: " + connectedDirections + "]";
    }

    // adds a side to the connection list if its connected. Use the class variables LEFT and RIGHT!
    private void addIfConnected(int side, Tile tile) {
        GridDirection sideDirection = getSide(side);
        if (tile.getTerrain(sideDirection) != TerrainType.CASTLE) {
            connectedDirections.add(sideDirection);
        }
    }

    // checks arguments, throw exception if something is wrong.
    private void checkArguments(GridDirection cornerDirection, Tile tile) {
        if (cornerDirection == null || tile == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        } else if (!Arrays.asList(GridDirection.indirectNeighbors()).contains(cornerDirection)) {
            throw new IllegalArgumentException("Corner direction " + cornerDirection + " is not a corner.");
        } else if (tile.getTerrain(cornerDirection) != TerrainType.FIELDS) {
            throw new IllegalArgumentException(
                    "The corner " + cornerDirection + " has not FIELDS terrain on tile " + tile + ". It can't be a FieldsCorner.");
        }
    }

    // calculates sides of a corner. Use the class variables LEFT and RIGHT!
    private GridDirection getSide(int side) {
        return GridDirection.values()[(cornerDirection.ordinal() - side) % 4];
    }
}
