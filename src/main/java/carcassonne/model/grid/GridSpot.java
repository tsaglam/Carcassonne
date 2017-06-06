package carcassonne.model.grid;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * The class represents a spot on the grid.
 * @author Timur Saglam
 */
public class GridSpot {

    private final Grid grid;
    private final Map<GridDirection, GridPattern> tagMap; // maps tagged location to the patterns.
    private Tile tile;
    private final int x;
    private final int y;

    /**
     * Creates a grid spot for a specific grid on a specific position.
     * @param grid is the grid.
     * @param x is the x coordinate of the position.
     * @param y is the y coordinate of the position.
     */
    public GridSpot(Grid grid, int x, int y) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        tagMap = new HashMap<GridDirection, GridPattern>();
    }

    /**
     * Creates list of all patterns on the spot.
     * @return the list of patterns.
     */
    public List<GridPattern> createPatternList() {
        checkTile("createPatternList()");
        List<GridPattern> results = new LinkedList<GridPattern>();
        TerrainType terrain;
        // first, check for castle and road patterns:
        for (GridDirection direction : GridDirection.directNeighbors()) {
            terrain = tile.getTerrain(direction); // get terrain type.
            if (terrain != TerrainType.FIELDS && hasNoTagConnectionTo(direction)) {
                results.add(new CastleAndRoadPattern(this, direction, terrain, grid));
            }
        }
        // then check for monastery patterns:
        addPatternIfMonastery(this, results); // the tile itself
        for (GridSpot neighbour : grid.getNeighbors(this)) {
            addPatternIfMonastery(neighbour, results); // neighbors
        }
        return results; // return all patterns.
    }

    /**
     * Forces to place a tile on the grid spot.
     * @param tile is the tile to place.
     */
    public void forcePlacement(Tile tile) {
        this.tile = tile;
        tile.setPosition(this);
    }

    /**
     * Getter for the tile.
     * @return the tile, or null if the grid spot has no tile.
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Getter for the x coordinate of the spot.
     * @return the x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the y coordinate of the spot.
     * @return the y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Method determines if tile recently was tagged by a specific grid pattern on a specific position or a position
     * connected to the specific position.
     * @param tilePosition is the specific position.
     * @param taggedBy is the {@link GridPattern} that tagged this spot.
     * @return true if tagged.
     */
    public Boolean hasTagConnectedTo(GridDirection tilePosition, GridPattern taggedBy) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (tile.isConnected(tilePosition, otherPosition) && tagMap.containsKey(otherPosition) && tagMap.get(otherPosition) == taggedBy) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the grid spot is free.
     * @return true if free
     */
    public boolean isFree() {
        return tile == null;
    }

    /**
     * Method determines if tile recently was tagged by any grid pattern checks on a specific position or a position
     * connected to the specific position.
     * @param tilePosition is the specific position.
     * @return true if not tagged.
     */
    public Boolean hasNoTagConnectionTo(GridDirection tilePosition) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (tile.isConnected(tilePosition, otherPosition) && tagMap.containsKey(otherPosition)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method determines if tile recently was tagged by grid pattern checks on a specific position or not.
     * @param tilePosition is the specific position.
     * @return true if it was not tagged.
     */
    public Boolean isUntagged(GridDirection tilePosition) {
        return !tagMap.containsKey(tilePosition);
    }

    /**
     * Checks whether the grid spot is occupied.
     * @return true if occupied
     */
    public boolean isOccupied() {
        return tile != null;
    }

    /**
     * Removes all the tags from the tile.
     */
    public void removeTags() {
        tagMap.clear();
    }

    /**
     * Set tile on grid spot if possible.
     * @param tile is the tile to set.
     * @return true if the tile could be placed.
     */
    public boolean set(Tile tile) {
        if (isPlaceable(tile, false)) {
            tile.setPosition(this);
            this.tile = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.
    }

    /**
     * tag the tile as recently checked by grid pattern checks for a specific direction.
     * @param direction is the tag direction.
     * @param taggedBy is the {@link GridPattern} that tagged the spot.
     */
    public void setTag(GridDirection direction, GridPattern taggedBy) {
        tagMap.put(direction, taggedBy);
    }

    private void addPatternIfMonastery(GridSpot spot, List<GridPattern> patternList) {
        if (spot.getTile().isMonastery()) {
            if (spot.hasNoTagConnectionTo(GridDirection.MIDDLE)) {
                patternList.add(new MonasteryGridPattern(spot, grid));
            }
        }
    }

    private void checkTile(String methodName) {
        if (isFree()) {
            throw new IllegalStateException("GridSpot is free, can't call " + methodName);
        }
    }

    private boolean isPlaceable(Tile tile, boolean freePlacement) {
        if (isOccupied()) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        GridSpot neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            neighbor = grid.getNeighbor(this, direction);
            if (neighbor == null) { // free space
                if (grid.isClosingFreeSpotsOff(this, direction)) {
                    return false; // you can't close of free spaces
                }
            } else { // if there is a neighbor in the direction.
                neighborCount++;
                if (!tile.hasSameTerrain(direction, neighbor.getTile())) {
                    return false; // if it does not fit to terrain, it can't be placed.
                }
            }
        }
        return neighborCount > 0 || freePlacement; // can be placed beneath another tile.
    }
}
