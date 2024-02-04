package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.CENTER;
import static carcassonne.model.grid.GridDirection.NORTH_WEST;
import static carcassonne.model.grid.GridDirection.WEST;
import static carcassonne.model.terrain.RotationDirection.LEFT;
import static carcassonne.model.terrain.RotationDirection.RIGHT;
import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;

import java.util.LinkedList;
import java.util.List;

import carcassonne.model.tile.Tile;

/**
 * Grid pattern for fields.
 * @author Timur Saglam
 */
public class FieldsPattern extends GridPattern {
    private static final int POINTS_PER_CASTLE = 3;
    private final List<CastleAndRoadPattern> adjacentCastles;
    private final Grid grid;

    /**
     * Creates a new field pattern.
     * @param startingSpot is the {@link GridSpot} where the pattern starts.
     * @param startingDirection is the position on the spot where the pattern starts.
     * @param grid is the correlating {@link Grid}.
     */
    public FieldsPattern(GridSpot startingSpot, GridDirection startingDirection) {
        super(FIELDS, POINTS_PER_CASTLE);
        checkArgs(startingSpot, startingDirection);
        grid = startingSpot.getGrid();
        adjacentCastles = new LinkedList<>();
        checkArgs(startingSpot, startingDirection);
        startingSpot.setTag(startingDirection, this); // initial tag, is needed for adding meeples!
        add(startingSpot); // initial tile
        buildPattern(startingSpot, startingDirection);
        adjacentCastles.forEach(CastleAndRoadPattern::removeOwnTags); // also remove the tile tags of the marked adjacentCastles
    }

    @Override
    public int getPatternScore() {
        return adjacentCastles.size() * scoreMultiplier;
    }

    // adds a grid direction to a list if it has not castle terrain at that direction on the tile.
    private void addIfNotCastle(List<GridDirection> results, Tile tile, GridDirection next) {
        if (tile.getTerrain(next) != CASTLE) {
            results.add(next);
        }
    }

    private void buildPattern(GridSpot spot, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = getFieldPositions(spot.getTile(), startingPoint);
        for (GridDirection position : fieldPositions) { // for every positions of this field on this tile
            countAdjacentCastles(spot, position); // count castles to determine pattern size
            spot.setTag(position, this); // mark as visited
        }
        fieldPositions.forEach(it -> checkNeighbors(spot, it)); // check every possible neighbor
    }

    private void checkNeighbors(GridSpot spot, GridDirection position) {
        for (GridDirection connectionDirection : getFieldConnections(position, spot.getTile())) { // ∀ connection points
            GridSpot neighbor = grid.getNeighbor(spot, connectionDirection); // get the neighbor
            GridDirection oppositeDirection = getFieldOpposite(position, connectionDirection); // get the connecting position on neighbor
            if (neighbor != null && !neighbor.isIndirectlyTagged(oppositeDirection)) { // if not visited
                neighbor.setTag(oppositeDirection, this); // mark as visited
                add(neighbor); // add to pattern
                buildPattern(neighbor, oppositeDirection); // continue building recursively
            }
        }
    }

    // Counts neighboring adjacent castles for a position on at tile. Finds all castle patterns on the tile that are
    // directly adjacent to the field position and saves the complete ones.
    private void countAdjacentCastles(GridSpot spot, GridDirection position) {
        for (GridDirection neighbor : getAdjacentPositions(position)) {
            if (spot.getTile().getTerrain(neighbor) == CASTLE && isUntagged(spot, neighbor)) { // if is unvisited castle
                CastleAndRoadPattern castle = new CastleAndRoadPattern(spot, neighbor, CASTLE);
                if (castle.isComplete()) { // if castle is closed (pattern check)
                    adjacentCastles.add(castle); // remember pattern to count points
                } else {
                    castle.removeOwnTags(); // IMPORTANT, remove tags if not used any further!
                }
            }
        }
    }

    /**
     * Returns every adjacent position on a tile for a specific initial position.
     */
    private List<GridDirection> getAdjacentPositions(GridDirection position) {
        List<GridDirection> neighbors = new LinkedList<>();
        if (position.isSmallerOrEquals(WEST)) {
            neighbors.add(CENTER); // the classic direction are adjacent to the middle
        }
        if (position.isSmallerOrEquals(NORTH_WEST)) { // everything except the middle has these two neighbors:
            neighbors.add(position.nextDirectionTo(LEFT)); // counterclockwise adjacent position
            neighbors.add(position.nextDirectionTo(RIGHT)); // clockwise adjacent position
        } else {
            neighbors.addAll(GridDirection.directNeighbors()); // the middle has the classic directions as neighbors
        }
        return neighbors;

    }

    /**
     * Gives for a specific tile and a specific position on that tile the directions in which the field connects to. If the
     * tile has not the terrain field on this position the result list is empty.
     */
    private List<GridDirection> getFieldConnections(GridDirection position, Tile tile) {
        List<GridDirection> results = new LinkedList<>();
        if (tile.getTerrain(position) == FIELDS) {
            if (position.isSmallerOrEquals(WEST)) {
                results.add(position); // for simple directions just return themselves.
            } else if (position.isSmallerOrEquals(NORTH_WEST)) {
                addIfNotCastle(results, tile, position.nextDirectionTo(LEFT)); // for edges it depends whether the neighboring
                addIfNotCastle(results, tile, position.nextDirectionTo(RIGHT)); // directions have castle terrain or not
            }
        }
        return results;
    }

    // Returns the position on the grid of a neighboring tile on a direction which is directly in contact with a specific
    // position of the first tile.
    private GridDirection getFieldOpposite(GridDirection position, GridDirection neighborDirection) {
        if (position.isSmallerOrEquals(WEST)) {
            return position.opposite(); // top, right, bottom, left are simply inverted
        }
        if (position.isSmallerOrEquals(NORTH_WEST)) {
            if (neighborDirection.isLeftOf(position)) { // neighbor to the left of the corner
                return position.opposite().nextDirectionTo(LEFT).nextDirectionTo(LEFT); // return opposite and two to the right
            }
            return position.opposite().nextDirectionTo(RIGHT).nextDirectionTo(RIGHT); // return opposite and two to the left
        }
        return position; // middle stays middle
    }

    private List<GridDirection> getFieldPositions(Tile tile, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = new LinkedList<>();
        for (GridDirection position : GridDirection.values()) { // for every position on tile
            if (tile.hasConnection(startingPoint, position)) {
                fieldPositions.add(position);
            }
        }
        return fieldPositions;
    }

    private boolean isUntagged(GridSpot spot, GridDirection position) {
        boolean tagged = false;
        for (CastleAndRoadPattern castle : adjacentCastles) {
            tagged |= spot.isIndirectlyTaggedBy(position, castle);
        }
        return !tagged;
    }

}
