package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.LEFT;
import static carcassonne.model.grid.GridDirection.MIDDLE;
import static carcassonne.model.grid.GridDirection.TOP_LEFT;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.CASTLE;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
        startingSpot.setTag(startingDirection, this); // initial tag
        add(startingSpot); // initial tile
        buildPattern(startingSpot.getTile(), startingDirection);
    }

    @Override
    public int getSize() {
        return adjacentCastles.size(); // the amount of adjacentCastles is the size of this pattern
    }

    @Override
    public void removeTileTags() {
        super.removeTileTags();
        for (CastleAndRoadPattern castle : adjacentCastles) {
            castle.removeTileTags(); // also remove the tile tags of the marked adjacentCastles
        }
    }

    // adds a grid direction to a list if it has not castle terain at that diection on the tile.
    private void addIfNotCastle(List<GridDirection> results, Tile tile, GridDirection next) {
        if (tile.getTerrain(next) != TerrainType.CASTLE) {
            results.add(next);
        }
    }

    private List<GridDirection> adjacentPositions(GridDirection position) {
        List<GridDirection> neighbors = new LinkedList<>();
        if (position.isSmallerOrEquals(LEFT)) {
            neighbors.add(MIDDLE);
        }
        if (position.isSmallerOrEquals(TOP_LEFT)) {
            neighbors.add(position.next(-1));
            neighbors.add(position.next(1));
        } else {
            neighbors.addAll(Arrays.asList(GridDirection.directNeighbors()));
        }
        return neighbors;

    }

    private void buildPattern(Tile tile, GridDirection startingPoint) {
        List<GridDirection> fieldPositions = getFieldPositions(tile, startingPoint);
        for (GridDirection position : fieldPositions) {
            countAdjacentCastles(tile, position);
            tile.getGridSpot().setTag(position, this); // mark as visited
        }
        for (GridDirection position : fieldPositions) {
            checkNeighbors(tile, position);
        }
    }

    /**
     * @param tile
     * @param position
     */
    private void checkNeighbors(Tile tile, GridDirection position) {
        for (GridDirection connectionDirection : fieldConnections(position, tile)) {
            GridSpot neighbor = grid.getNeighbor(tile.getGridSpot(), connectionDirection); // get the neighbor
            GridDirection oppositeDirection = getFieldOpposite(position, connectionDirection);
            if (neighbor != null && !neighbor.hasTagConnectedTo(oppositeDirection, this)) {
                neighbor.setTag(oppositeDirection, this); // mark as visited
                add(neighbor); // add to pattern
                buildPattern(neighbor.getTile(), oppositeDirection);
            }
        }
    }

    // Counts neighboring adjacent castles for a position on at tile. Finds all castle patterns on the tile that are
    // directly
    // adjacent to the field position and saves the complete ones.
    private void countAdjacentCastles(Tile tile, GridDirection position) {
        for (GridDirection neighbor : adjacentPositions(position)) {
            if (tile.getTerrain(neighbor) == CASTLE) {
                CastleAndRoadPattern castle = new CastleAndRoadPattern(tile.getGridSpot(), neighbor, CASTLE, grid);
                if (castle.complete) {
                    adjacentCastles.add(castle); // remember if complete
                } else {
                    castle.removeTileTags(); // discard if not
                }
            }
        }
    }

    /**
     * Gives for a specific tile and a specific position on that tile the directions in which the field connects to. If the
     * tile has not the terrain field on this position the result list is empty.
     */
    private List<GridDirection> fieldConnections(GridDirection position, Tile tile) {
        List<GridDirection> results = new LinkedList<>();
        if (tile.getTerrain(position) == TerrainType.FIELDS) {
            if (position.isSmallerOrEquals(LEFT)) {
                results.add(position); // for simple directions just return themselves.
            } else if (position.isSmallerOrEquals(TOP_LEFT)) {
                addIfNotCastle(results, tile, position.next(-1)); // for edges it depends whether the neighboring
                addIfNotCastle(results, tile, position.next(1)); // directions have castle terrain or not
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
                return position.opposite().next(-1).next(-1); // return opposite and two to the right
            } else { // neighbor to the right of the corner
                return position.opposite().next(1).next(1); // return opposite and two to the left
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

}
