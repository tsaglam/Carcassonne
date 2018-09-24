package carcassonne.model.terrain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TileType;

/**
 * Represents the terrain of a tile. It consists out of nine different terrain types, one for each grid direction.
 * @author Timur Saglam
 */
public class Terrain {
    private List<GridDirection> meepleSpots;
    private final Map<GridDirection, TerrainType> terrainMap;

    /**
     * Creates a terrain instance with nine terrain types.
     * @param terrain is the array of nine terrain types.
     */
    public Terrain(TileType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tile type can't be null");
        } else if (type.getTerrain().length != GridDirection.values().length) {
            throw new IllegalArgumentException("Terrain array is invalid: " + type);
        }
        terrainMap = new HashMap<>(5); // create terrain map.
        for (int i = 0; i < GridDirection.values().length; i++) {
            terrainMap.put(GridDirection.values()[i], type.getTerrain()[i]);
        }
        createMeepleSpots();
    }

    /**
     * return the terrain type on the tile in the specific direction.
     * @param direction is the specific direction.
     * @return the terrain type, or null if the direction is not mapped.
     */
    public TerrainType getAt(GridDirection direction) {
        if (terrainMap.containsKey(direction)) {
            return terrainMap.get(direction);
        }
        throw new IllegalArgumentException("Terrain not defined at " + direction);
    }

    /**
     * Returns a list of grid directions, where meeples can be placed on this terrain.
     * @return the list of meeple spots.
     */
    public List<GridDirection> getMeepleSpots() {
        return meepleSpots;
    }

    /**
     * Checks whether two parts of a tile are connected through same terrain.
     * @param from is the part to check from.
     * @param to is the terrain to check to.
     * @return true if connected, false if not.
     */
    public boolean isConnected(GridDirection from, GridDirection to) {
        if (isDirectConnected(from, to)) {
            return true;
        } else if (from != GridDirection.MIDDLE && to != GridDirection.MIDDLE) {
            return isIndirectConnected(from, to, 1) || isIndirectConnected(from, to, -1);
        }
        return false;
    }

    /**
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        rotate(GridDirection.TOP, GridDirection.LEFT, GridDirection.BOTTOM, GridDirection.RIGHT);
        rotate(GridDirection.TOP_RIGHT, GridDirection.TOP_LEFT, GridDirection.BOTTOM_LEFT, GridDirection.BOTTOM_RIGHT);
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        rotate(GridDirection.directNeighbors());
        rotate(GridDirection.indirectNeighbors());
    }

    private void createMeepleSpots() {
        meepleSpots = new LinkedList<>();
        for (GridDirection direction : GridDirection.values()) {
            meepleSpots.add(direction); // add all possible placements
        }
        for (GridDirection direction : GridDirection.neighbors()) {
            if (getAt(direction) == TerrainType.OTHER || isConnected(direction, GridDirection.MIDDLE)) {
                meepleSpots.remove(direction); // is not a valid meeple spot.
            } else {
                GridDirection left = GridDirection.next(direction, -1);
                GridDirection right = GridDirection.next(direction, 1);
                if (getAt(direction) == getAt(left) && getAt(direction) == getAt(right)) {
                    meepleSpots.remove(left);
                    meepleSpots.remove(right);
                }
            }
        }
    }

    // checks for direct connection through middle:
    private boolean isDirectConnected(GridDirection from, GridDirection to) {
        TerrainType middle = getAt(GridDirection.MIDDLE);
        return getAt(from).equals(middle) && getAt(to).equals(middle);
    }

    // checks for indirect connection through the specified side from a specific start to a specific
    // destination. Side is either 1 (right) or -1 (left.)
    private boolean isIndirectConnected(GridDirection from, GridDirection to, int side) {
        GridDirection current = from;
        GridDirection next;
        while (current != to) { // while not at destination:
            next = GridDirection.next(current, side); // get the next direction
            if (getAt(current) != getAt(next)) {
                return false; // check if still connected
            }
            current = next; // set new current
        }
        return true; // found connection from start to destination.
    }

    /**
     * Rotates the terrain at the specified directions clockwise.
     * @param directions are the specified directions.
     */
    private void rotate(GridDirection... directions) {
        TerrainType temporary = terrainMap.get(directions[directions.length - 1]); // get last one
        for (GridDirection direction : directions) { // rotate terrain through temporary:
            temporary = terrainMap.put(direction, temporary);
        }
        createMeepleSpots();
    }
}
