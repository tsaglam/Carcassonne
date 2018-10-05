package carcassonne.model.terrain;

import static carcassonne.model.grid.GridDirection.BOTTOM;
import static carcassonne.model.grid.GridDirection.BOTTOM_LEFT;
import static carcassonne.model.grid.GridDirection.BOTTOM_RIGHT;
import static carcassonne.model.grid.GridDirection.LEFT;
import static carcassonne.model.grid.GridDirection.MIDDLE;
import static carcassonne.model.grid.GridDirection.RIGHT;
import static carcassonne.model.grid.GridDirection.TOP;
import static carcassonne.model.grid.GridDirection.TOP_LEFT;
import static carcassonne.model.grid.GridDirection.TOP_RIGHT;

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
            return true; // directly connected through the middle of the tile
        } else if (from != MIDDLE && to != MIDDLE && (isIndirectConnected(from, to))) {
            return true; // is not from or to middle but indirectly connected (counter)clockwise
        } else if (getAt(from) == TerrainType.FIELDS && getAt(to) == TerrainType.FIELDS && isEdge(to)) {
            return isImplicitlyConnected(from, to);
        }
        return false;
    }

    public boolean isEdge(GridDirection direction) {
        return TOP_RIGHT.isSmallerOrEquals(direction) && direction.isSmallerOrEquals(TOP_LEFT);
    }

    /**
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        rotate(TOP, LEFT, BOTTOM, RIGHT);
        rotate(TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT);
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        rotate(GridDirection.directNeighbors());
        rotate(GridDirection.indirectNeighbors());
    }

    private void createMeepleSpots() { // TODO (HIGH) use meeple spots
        meepleSpots = new LinkedList<>();
        for (GridDirection direction : GridDirection.values()) {
            meepleSpots.add(direction); // add all possible placements
        }
        for (GridDirection direction : GridDirection.neighbors()) {
            if (getAt(direction) == TerrainType.OTHER || isConnected(direction, GridDirection.MIDDLE)) {
                meepleSpots.remove(direction); // is not a valid meeple spot.
            } else {
                GridDirection left = direction.next(RotationDirection.LEFT);
                GridDirection right = direction.next(RotationDirection.RIGHT);
                if (getAt(direction) == getAt(left) && getAt(direction) == getAt(right)) {
                    meepleSpots.remove(left);
                    meepleSpots.remove(right);
                }
            }
        }
    }

    // checks for direct connection through middle:
    private boolean isDirectConnected(GridDirection from, GridDirection to) {
        TerrainType middle = getAt(MIDDLE);
        return getAt(from) == middle && getAt(to) == middle;
    }

    private boolean isImplicitlyConnected(GridDirection from, GridDirection to) {
        boolean connected = false;
        for (GridDirection corner : GridDirection.indirectNeighbors()) {
            if (isDirectConnected(from, corner) || isIndirectConnected(from, corner)) {
                for (RotationDirection side : RotationDirection.values()) { // for left and right
                    connected |= isImplicitlyConnected(corner, to, side);
                }
            }
        }
        return connected;
    }

    private boolean isImplicitlyConnected(GridDirection from, GridDirection to, RotationDirection side) {
        if (from == to) {
            return true; // is connected
        }
        GridDirection between = from.next(side);
        GridDirection next = between.next(side);
        if (getAt(between) == TerrainType.CASTLE && getAt(MIDDLE) != TerrainType.CASTLE) {
            return isImplicitlyConnected(next, to, side);
        }
        return false;
    }

    private boolean isIndirectConnected(GridDirection from, GridDirection to) {
        boolean connected = false;
        for (RotationDirection side : RotationDirection.values()) { // for left and right
            connected |= isIndirectConnected(from, to, side);
        }
        return connected;
    }

    // checks for indirect connection through the specified side from a specific start to a specific
    // destination. Side is either 1 (right) or -1 (left.)
    private boolean isIndirectConnected(GridDirection from, GridDirection to, RotationDirection side) {
        GridDirection current = from;
        GridDirection next;
        while (current != to) { // while not at destination:
            next = current.next(side); // get the next direction
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
