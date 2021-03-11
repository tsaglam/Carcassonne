package carcassonne.model.grid;

import carcassonne.model.terrain.TerrainType;

/**
 * @author Timur Saglam
 */
public class CastleAndRoadPattern extends GridPattern { // TODO (MEDIUM) use subclasses to make constructors generic (factory?)

    /**
     * Public constructor for creating road and monastery patterns.
     * @param startingSpot is the starting spot of the pattern.
     * @param startingDirection is the starting direction of the pattern.
     * @param patternType is the type of the pattern.
     * @param grid is the grid the pattern is created on.
     */
    public CastleAndRoadPattern(GridSpot startingSpot, GridDirection startingDirection, TerrainType patternType) {
        super(patternType, (patternType == TerrainType.CASTLE) ? 2 : 1);
        checkArgs(startingSpot, startingDirection, patternType);
        startingSpot.setTag(startingDirection, this); // initial tag
        add(startingSpot); // initial tile
        complete = buildPattern(startingSpot, startingDirection); // recursive algorithm.
    }

    @Override
    public void forceDisburse() {
        scoreMultiplier = 1; // reduce score multiplier
        super.forceDisburse();
    }

    private boolean buildPattern(GridSpot spot, GridDirection startingPoint) {
        boolean isClosed = true;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every side
            if (spot.getTile().hasConnection(startingPoint, direction)) { // if is connected side
                GridSpot neighbor = spot.getGrid().getNeighbor(spot, direction); // get the neighbor
                if (neighbor == null) { // if it has no neighbor
                    isClosed = false; // open side, can't be finished pattern.
                } else { // continue on neighbors
                    isClosed &= checkNeighbor(spot, neighbor, direction);
                }
            }
        }
        return isClosed;
    }

    private void checkArgs(GridSpot spot, GridDirection direction, TerrainType terrain) {
        if (terrain != TerrainType.CASTLE && terrain != TerrainType.ROAD) {
            throw new IllegalArgumentException("Can only create CastleAndRoadPatterns from type castle or road");
        }
        checkArgs(spot, direction);
    }

    private boolean checkNeighbor(GridSpot startingTile, GridSpot neighbor, GridDirection direction) {
        GridDirection oppositeDirection = direction.opposite();
        if (!neighbor.hasTagConnectedTo(oppositeDirection, this)) { // if neighbor not visited yet
            startingTile.setTag(direction, this);
            neighbor.setTag(oppositeDirection, this); // mark as visited
            add(neighbor); // add to pattern
            return buildPattern(neighbor, oppositeDirection);
        }
        return true;
    }
}
