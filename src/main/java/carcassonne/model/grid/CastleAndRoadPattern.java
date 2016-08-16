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
        if (patternType != TerrainType.CASTLE && patternType != TerrainType.ROAD) {
            throw new IllegalArgumentException("Can only create CastleAndRoadPatterns from type castle or road");
        } else if(startingTile == null || startingDirection == null || grid == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        } else if (!patternCheckRecursion(startingTile, startingDirection, grid)) {
            complete = true;
        }
    }

    private boolean patternCheckRecursion(Tile startingTile, GridDirection startingPoint, Grid grid) {
        boolean hasOpenEnd = false;
        Tile neighbor;
        GridDirection oppositeDirection;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for direction
            if (startingTile.isConnected(startingPoint, direction)) { // if is connected side
                neighbor = grid.getNeighbour(startingTile, direction); // get the neighbor
                if (neighbor != null) { // if the neighbor exists
                    oppositeDirection = GridDirection.opposite(direction);
                    if (!neighbor.isTagged(oppositeDirection)) { // if neighbor not visited yet
                        neighbor.setTag(startingPoint); // mark as visited
                        add(neighbor); // add to pattern
                        hasOpenEnd = patternCheckRecursion(neighbor, oppositeDirection, grid);
                    }
                } else {
                    hasOpenEnd = true; // open connection, can't be finished pattern.
                }
            }
        }
        return hasOpenEnd;
    }

}
