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

import java.util.Arrays;
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
     * @param type is the tile type of the terrain.
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
    public TerrainType at(GridDirection direction) {
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
        } else if (terrainMap.get(from) == TerrainType.FIELDS && terrainMap.get(to) == TerrainType.FIELDS) {
            return isImplicitlyConnected(from, to);
        }
        return false;
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

    private void createMeepleSpots() { // TODO (HIGH) Improve code quality.
        meepleSpots = new LinkedList<>();
        meepleSpots.addAll(Arrays.asList(GridDirection.values()));
        for (GridDirection spot : GridDirection.values()) { // for every spot
            if (terrainMap.get(spot) != TerrainType.OTHER && meepleSpots.contains(spot)) { // if not checked
                LinkedList<GridDirection> removalList = new LinkedList<>();
                int x = 0;
                int y = 0;
                for (GridDirection other : GridDirection.values()) {
                    if (isConnected(spot, other)) { // for every connected spot
                        removalList.add(other); // mark as part of pattern
                        x = other.addX(x);
                        y = other.addY(y);
                    }
                }
                x = (int) Math.round(x / 3.0); // calculate center of pattern
                y = (int) Math.round(y / 3.0);
                GridDirection center = GridDirection.values2D()[x + 1][y + 1];
                if (isConnected(center, spot)) {
                    removalList.remove(center); // keep result spot
                    meepleSpots.removeAll(removalList); // remove the rest
                }
            }
        }
        removeRedundantSpots(GridDirection.directNeighbors(), false); // merge to top, right, bottom, and left
        removeRedundantSpots(GridDirection.indirectNeighbors(), true); // merge to the corners and add already removed anchors
        removeRedundantSpots(GridDirection.directNeighbors(), true); // merge one more time
    }

    // checks for direct connection through middle:
    private boolean isDirectConnected(GridDirection from, GridDirection to) {
        TerrainType middle = terrainMap.get(MIDDLE);
        return terrainMap.get(from) == middle && terrainMap.get(to) == middle;
    }

    private boolean isImplicitlyConnected(GridDirection from, GridDirection to) {
        boolean connected = false;
        for (GridDirection corner : GridDirection.indirectNeighbors()) { // for every connected corner:
            if (isDirectConnected(from, corner) || isIndirectConnected(from, corner)) {
                for (RotationDirection side : RotationDirection.values()) { // to the left and right
                    connected |= isImplicitlyConnected(corner, to, side); // check corner to corner connection
                }
            }
        } // TODO (HIGH) reduce code duplication.
        for (GridDirection corner : GridDirection.indirectNeighbors()) {
            if (isDirectConnected(to, corner) || isIndirectConnected(to, corner)) {
                for (RotationDirection side : RotationDirection.values()) { // for left and right
                    connected |= isImplicitlyConnected(from, corner, side);
                }
            }
        }
        return connected;
    }

    private boolean isImplicitlyConnected(GridDirection from, GridDirection to, RotationDirection side) {
        if (from == to) {
            return true; // is connected
        }
        GridDirection between = from.next(side); // between this and next corner
        GridDirection next = between.next(side); // next corner
        if (terrainMap.get(between) == TerrainType.CASTLE && terrainMap.get(MIDDLE) != TerrainType.CASTLE) {
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
            if (terrainMap.get(current) != terrainMap.get(next)) {
                return false; // check if still connected
            }
            current = next; // set new current
        }
        return true; // found connection from start to destination.
    }

    // removes redundant meeple spots and optionally adds anchor spots.
    private void removeRedundantSpots(GridDirection[] anchorDirections, boolean addAnchor) {
        List<GridDirection> removalList = new LinkedList<>();
        for (GridDirection anchor : anchorDirections) {
            GridDirection left = anchor.next(RotationDirection.LEFT);
            GridDirection right = anchor.next(RotationDirection.RIGHT);
            if (terrainMap.get(anchor) == terrainMap.get(left) && terrainMap.get(anchor) == terrainMap.get(right) && meepleSpots.contains(left)
                    && meepleSpots.contains(right)) {
                removalList.add(left);
                removalList.add(right);
                if (addAnchor && !isConnected(anchor, MIDDLE)) {
                    meepleSpots.add(anchor);
                }
            }
        }
        meepleSpots.removeAll(removalList);
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
