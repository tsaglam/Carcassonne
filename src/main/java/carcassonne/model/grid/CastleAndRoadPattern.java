/**
 * 
 */
package carcassonne.model.grid;

import carcassonne.model.tile.TerrainType;
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
     */
    public CastleAndRoadPattern(Tile startingTile, GridDirection startingDirection, TerrainType patternType, Grid grid) {
        super(patternType);
        checkArgs(startingTile, startingDirection, patternType, grid);
        startingTile.setTag(startingDirection); // initial tag
        add(startingTile); // initial tile
        complete = buildPattern(startingTile, startingDirection, grid); // recusive algorithm.
        removeTileTags();
    }

    private boolean buildPattern(Tile startingTile, GridDirection startingPoint, Grid grid) {
        boolean isClosed = true;
        Tile neighbor;
        GridDirection oppositeDirection;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for direction
            if (startingTile.isConnected(startingPoint, direction)) { // if is connected side
                neighbor = grid.getNeighbour(startingTile, direction); // get the neighbor
                if (neighbor == null) { // if it has no neighbor
                    isClosed = false; // open connection, can't be finished pattern.
                } else {
                    oppositeDirection = GridDirection.opposite(direction);
                    if (neighbor.isNotTagged(oppositeDirection)) { // if neighbor not visited yet
                        startingTile.setTag(direction);
                        neighbor.setTag(oppositeDirection); // mark as visited
                        add(neighbor); // add to pattern
                        isClosed &= buildPattern(neighbor, oppositeDirection, grid);
                    }
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
}
