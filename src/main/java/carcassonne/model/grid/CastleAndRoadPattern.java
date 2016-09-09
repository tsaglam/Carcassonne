/**
 * 
 */
package carcassonne.model.grid;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * @author Timur Saglam
 */
public class CastleAndRoadPattern extends GridPattern {

    /**
     * Public constructor for creating road and monastery patterns.
     * @param startingTile is the starting tile of the pattern.
     * @param startingDirection is the starting direction of the pattern.
     * @param patternType is the type of the pattern.
     * @param grid is the grid the pattern is created on.
     */
    public CastleAndRoadPattern(Tile startingTile, GridDirection startingDirection, TerrainType patternType, Grid grid) {
        super(patternType);
        checkArgs(startingTile, startingDirection, patternType, grid);
        startingTile.setTag(startingDirection, this); // initial tag
        add(startingTile); // initial tile
        complete = buildPattern(startingTile, startingDirection, grid); // recursive algorithm.
    }

    private boolean buildPattern(Tile startingTile, GridDirection startingPoint, Grid grid) {
        boolean isClosed = true;
        Tile neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every side
            if (startingTile.isConnected(startingPoint, direction)) { // if is connected side
                neighbor = grid.getNeighbour(startingTile, direction); // get the neighbor
                if (neighbor == null) { // if it has no neighbor
                    isClosed = false; // open side, can't be finished pattern.
                } else { // continue on neighbors
                    isClosed &= checkNeighbor(startingTile, neighbor, direction, grid);
                }
            }
        }
        return isClosed;
    }

    private void checkArgs(Tile tile, GridDirection direction, TerrainType terrain, Grid grid) {
        if (terrain != TerrainType.CASTLE && terrain != TerrainType.ROAD) {
            throw new IllegalArgumentException("Can only create CastleAndRoadPatterns from type castle or road");
        } else if (tile == null || direction == null || grid == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
    }

    private boolean checkNeighbor(Tile startingTile, Tile neighbor, GridDirection direction, Grid grid) {
        GridDirection oppositeDirection = GridDirection.opposite(direction);
        if (!neighbor.isConnectedToTag(oppositeDirection, this)) { // if neighbor not visited yet
            startingTile.setTag(direction, this);
            neighbor.setTag(oppositeDirection, this); // mark as visited
            add(neighbor); // add to pattern
            return buildPattern(neighbor, oppositeDirection, grid);
        }
        return true;
    }
}
