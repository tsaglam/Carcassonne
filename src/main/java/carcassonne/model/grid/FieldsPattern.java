package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.LEFT;
import static carcassonne.model.grid.GridDirection.MIDDLE;
import static carcassonne.model.grid.GridDirection.TOP_LEFT;
import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import carcassonne.model.terrain.RotationDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

public class FieldsPattern extends GridPattern {
    private final List<CastleAndRoadPattern> adjacentCastles;
    private final Grid grid;

    public FieldsPattern(GridSpot startingSpot, GridDirection startingDirection, Grid grid) {
        super(FIELDS);
        this.grid = grid;
        adjacentCastles = new LinkedList<>();
        checkArgs(startingSpot, startingDirection, grid);
        startingSpot.setTag(startingDirection, this); // initial tag, is needed for adding meeples!
        add(startingSpot); // initial tile
        buildPattern(startingSpot, startingDirection);
        for (CastleAndRoadPattern castle : adjacentCastles) {
            castle.removeOwnTags(); // also remove the tile tags of the marked adjacentCastles
        }
    }

    @Override
    public int getSize() {
        return adjacentCastles.size(); // the amount of adjacentCastles is the size of this pattern
    }

    // adds a grid direction to a list if it has not castle terain at that diection on the tile.
    private void addIfNotCastle(List<GridDirection> results, Tile tile, GridDirection next) {
        if (tile.getTerrain(next) != TerrainType.CASTLE) {
            results.add(next);
        }
    }

    private void buildPattern(GridSpot spot, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = getFieldPositions(spot.getTile(), startingPoint);
        for (GridDirection position : fieldPositions) { // for every positions of this field on this tile
            countAdjacentCastles(spot, position); // count castles to determine pattern size
            spot.setTag(position, this); // mark as visited
        }
        for (GridDirection position : fieldPositions) {
            checkNeighbors(spot, position); // check every possible neighbor
        }
    }

    private void checkNeighbors(GridSpot spot, GridDirection position) {
        for (GridDirection connectionDirection : getFieldConnections(position, spot.getTile())) { // ∀ connection points
            GridSpot neighbor = grid.getNeighbor(spot, connectionDirection); // get the neighbor
            GridDirection oppositeDirection = getFieldOpposite(position, connectionDirection); // get the connecting position on neighbor
            if (neighbor != null && neighbor.hasNoTagConnectedTo(oppositeDirection)) { // if not visited
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
                CastleAndRoadPattern castle = new CastleAndRoadPattern(spot, neighbor, CASTLE, grid);
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
        if (position.isSmallerOrEquals(LEFT)) {
            neighbors.add(MIDDLE); // the classic direction are adjacent to the middle
        }
        if (position.isSmallerOrEquals(TOP_LEFT)) { // everything except the middle has these two neighbors:
            neighbors.add(position.next(RotationDirection.LEFT)); // counterclockwise adjacent position
            neighbors.add(position.next(RotationDirection.RIGHT)); // clockwise adjacent position
        } else {
            neighbors.addAll(Arrays.asList(GridDirection.directNeighbors())); // the middle has the classic directions as neighbors
        }
        return neighbors;

    }

    /**
     * Gives for a specific tile and a specific position on that tile the directions in which the field connects to. If the
     * tile has not the terrain field on this position the result list is empty.
     */
    private List<GridDirection> getFieldConnections(GridDirection position, Tile tile) {
        List<GridDirection> results = new LinkedList<>();
        if (tile.getTerrain(position) == TerrainType.FIELDS) {
            if (position.isSmallerOrEquals(LEFT)) {
                results.add(position); // for simple directions just return themselves.
            } else if (position.isSmallerOrEquals(TOP_LEFT)) {
                addIfNotCastle(results, tile, position.next(RotationDirection.LEFT)); // for edges it depends whether the neighboring
                addIfNotCastle(results, tile, position.next(RotationDirection.RIGHT)); // directions have castle terrain or not
            }
        }
        return results;
    }

    // Returns the position on the grid of a neighboring tile on a direction which is directly in contact with a specific
    // position of the first tile.
    private GridDirection getFieldOpposite(GridDirection position, GridDirection neighborDirection) {
        if (position.isSmallerOrEquals(LEFT)) {
            return position.opposite(); // top, right, botton left are simply inverted
        } else if (position.isSmallerOrEquals(TOP_LEFT)) {
            if (neighborDirection.isLeftOf(position)) { // neighbor to the left of the corner
                return position.opposite().next(RotationDirection.LEFT).next(RotationDirection.LEFT); // return opposite and two to the right
            } else { // neighbor to the right of the corner
                return position.opposite().next(RotationDirection.RIGHT).next(RotationDirection.RIGHT); // return opposite and two to the left
            }
        }
        return position; // middle stays middle
    }

    private List<GridDirection> getFieldPositions(Tile tile, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = new LinkedList<>();
        for (GridDirection position : GridDirection.values()) { // for every position on tile
            if (tile.hasConnection(startingPoint, position)) { // TODO (HIGH) Add exceptions!
                fieldPositions.add(position);
            }
        }
        return fieldPositions;
    }

    private boolean isUntagged(GridSpot spot, GridDirection position) {
        boolean tagged = false;
        for (CastleAndRoadPattern castle : adjacentCastles) {
            tagged |= spot.hasTagConnectedTo(position, castle);
        }
        return !tagged;
    }

}
