package carcassonne.model.terrain;

import static carcassonne.model.grid.GridDirection.CENTER;
import static carcassonne.model.grid.GridDirection.EAST;
import static carcassonne.model.grid.GridDirection.NORTH;
import static carcassonne.model.grid.GridDirection.NORTH_EAST;
import static carcassonne.model.grid.GridDirection.NORTH_WEST;
import static carcassonne.model.grid.GridDirection.SOUTH;
import static carcassonne.model.grid.GridDirection.SOUTH_EAST;
import static carcassonne.model.grid.GridDirection.SOUTH_WEST;
import static carcassonne.model.grid.GridDirection.WEST;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TileType;

/**
 * Represents the terrain information of a single tile. It consists out of nine different terrain types, one for each
 * grid direction. Every other property, such as the meeple spots and connections between positions, is computed from
 * that information.
 * @author Timur Saglam
 */
public class TileTerrain {
    private Set<GridDirection> meepleSpots;
    private final Map<GridDirection, TerrainType> terrain;

    /**
     * Creates a terrain instance with nine terrain types.
     * @param type is the tile type of the terrain.
     */
    public TileTerrain(TileType type) {
        terrain = new HashMap<>();
        for (int i = 0; i < GridDirection.values().length; i++) {
            terrain.put(GridDirection.values()[i], type.getTerrain()[i]);
        }
        createMeepleSpots();
    }

    /**
     * return the terrain type on the tile in the specific direction.
     * @param direction is the specific direction.
     * @return the terrain type, or null if the direction is not mapped.
     */
    public TerrainType at(GridDirection direction) {
        if (terrain.containsKey(direction)) {
            return terrain.get(direction);
        }
        throw new IllegalArgumentException("TileTerrain not defined at " + direction);
    }

    /**
     * Returns a set of grid directions, where meeples can be placed on this terrain.
     * @return the list of meeple spots.
     */
    public Set<GridDirection> getMeepleSpots() {
        return meepleSpots;
    }

    /**
     * Checks whether two parts of a tile are connected through same terrain.
     * @param from is the part to check from.
     * @param towards is the terrain to check to.
     * @return true if connected, false if not.
     */
    public final boolean isConnected(GridDirection from, GridDirection towards) {
        if (isDirectConnected(from, towards)) {
            return true; // directly connected through the middle of the tile
        } else if (from != CENTER && towards != CENTER && isIndirectConnected(from, towards)) {
            return true; // is not from or to middle but indirectly connected (counter)clockwise
        } else if (terrain.get(from) == TerrainType.FIELDS && terrain.get(towards) == TerrainType.FIELDS) {
            return isImplicitlyConnected(from, towards); // is connected through implicit terrain information
        }
        return false;
    }

    /**
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        rotate(List.of(NORTH, WEST, SOUTH, EAST));
        rotate(List.of(NORTH_EAST, NORTH_WEST, SOUTH_WEST, SOUTH_EAST));
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        rotate(GridDirection.directNeighbors());
        rotate(GridDirection.indirectNeighbors());
    }

    /**
     * Creates the list of positions on the tile where a meeple can be placed.
     */
    private void createMeepleSpots() {
        meepleSpots = new HashSet<>();
        for (GridDirection position : GridDirection.values()) { // for every spot
            if (terrain.get(position) != TerrainType.OTHER) { // if not checked
                createMeepleSpot(position);
            }
        }
        removeRedundantSpots(GridDirection.directNeighbors(), false); // merge to top, right, bottom, and left
        removeRedundantSpots(GridDirection.indirectNeighbors(), true); // merge to the corners and add already removed anchors
        removeRedundantSpots(GridDirection.directNeighbors(), true); // merge one more time
    }

    /**
     * Creates a single meeple spot.
     */
    private void createMeepleSpot(GridDirection position) {
        List<GridDirection> connectedPositions = Stream.of(GridDirection.values()).filter(it -> isConnected(position, it)).toList();
        Point sum = new Point();
        for (GridDirection connectedPosition : connectedPositions) {
            sum.x += connectedPosition.getX(); // sum up coordinate weights to calculate the center
            sum.y += connectedPosition.getY();
        }
        GridDirection center = GridDirection.values2D()[(int) Math.round(sum.x / 3.0) + 1][(int) Math.round(sum.y / 3.0) + 1];
        if (isConnected(center, position)) {
            meepleSpots.add(center); // add the geometrical pattern center
        } else {
            meepleSpots.add(position); // just add the original position
        }
    }

    /**
     * Checks if the directions are directly connected through the middle
     */
    private boolean isDirectConnected(GridDirection from, GridDirection towards) {
        TerrainType middle = terrain.get(CENTER);
        return terrain.get(from) == middle && terrain.get(towards) == middle;
    }

    /**
     * Checks if the directions are connected clockwise or counter-clockwise around the tile.
     */
    private boolean isIndirectConnected(GridDirection from, GridDirection towards) {
        boolean connected = false;
        for (RotationDirection side : RotationDirection.values()) { // for left and right
            connected |= isIndirectConnected(from, towards, side);
        }
        return connected;
    }

    /**
     * Checks for indirect connection through the specified side from a specific start to a specific destination.
     */
    private boolean isIndirectConnected(GridDirection from, GridDirection towards, RotationDirection side) {
        GridDirection current = from;
        GridDirection next;
        while (current != towards) { // while not at destination:
            next = current.nextDirectionTo(side); // get the next direction
            if (terrain.get(current) != terrain.get(next)) {
                return false; // check if still connected
            }
            current = next; // set new current
        }
        return true; // found connection from start to destination.
    }

    /**
     * Checks for implicit connection, which means connection that is only implicitly represented through the terrain, e.g.
     * because a road does not end in a castle because it passes through the tile.
     */
    private boolean isImplicitlyConnected(GridDirection from, GridDirection towards) {
        boolean connected = false;
        for (GridDirection direction : Arrays.asList(from, towards)) { // for both directions
            GridDirection other = (from == direction) ? towards : from;
            for (GridDirection corner : GridDirection.indirectNeighbors()) { // for every connected corner:
                if (isDirectConnected(direction, corner) || isIndirectConnected(direction, corner)) { // if connected to corner
                    for (RotationDirection side : RotationDirection.values()) { // to the left and right
                        connected |= isImplicitlyConnected(corner, other, side); // check corner to corner connection
                    }
                }
            }
        }
        return connected;
    }

    /**
     * Checks for implicit connection in a specific direction.
     */
    private boolean isImplicitlyConnected(GridDirection from, GridDirection towards, RotationDirection side) {
        if (from == towards) {
            return true; // is connected
        }
        GridDirection inbetween = from.nextDirectionTo(side); // between this and next corner
        GridDirection nextCorner = inbetween.nextDirectionTo(side); // next corner
        if (hasNoCastleEntry(inbetween)) {
            return isImplicitlyConnected(nextCorner, towards, side);
        }
        return false;
    }

    /**
     * removes redundant meeple spots and optionally adds anchor spots.
     */
    private void removeRedundantSpots(List<GridDirection> anchorDirections, boolean addAnchor) {
        List<GridDirection> removalList = new LinkedList<>();
        for (GridDirection anchor : anchorDirections) {
            GridDirection left = anchor.nextDirectionTo(RotationDirection.LEFT);
            GridDirection right = anchor.nextDirectionTo(RotationDirection.RIGHT);
            if (terrain.get(anchor) == terrain.get(left) && terrain.get(anchor) == terrain.get(right) && meepleSpots.contains(left)
                    && meepleSpots.contains(right)) {
                removalList.add(left);
                removalList.add(right);
                if (addAnchor && !isConnected(anchor, CENTER)) {
                    meepleSpots.add(anchor);
                }
            }
        }
        meepleSpots.removeAll(removalList);
    }

    /**
     * Checks whether this tile terrain has a street passing through the center of the tile. This means the middle is of
     * terrain street and is connected to at least two other sides.
     */
    private boolean hasPassingStreet() {
        return terrain.get(CENTER) == TerrainType.ROAD
                && GridDirection.tilePositions().stream().filter(it -> isDirectConnected(CENTER, it)).count() > 2;
    }

    /**
     * Checks whether the tile terrain has a castle entry towards a specified castle position. This means no street ending
     * towards it.
     */
    private boolean hasNoCastleEntry(GridDirection castlePosition) {
        return terrain.get(castlePosition) == TerrainType.CASTLE && (terrain.get(CENTER) == TerrainType.OTHER || hasPassingStreet());
    }

    /**
     * Rotates the terrain at the specified directions clockwise.
     * @param directions are the specified directions.
     */
    private void rotate(List<GridDirection> directions) {
        TerrainType temporary = terrain.get(directions.get(directions.size() - 1)); // get last one
        for (GridDirection direction : directions) { // rotate terrain through temporary:
            temporary = terrain.put(direction, temporary);
        }
        createMeepleSpots();
    }
}
