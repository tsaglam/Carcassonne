package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.CENTER;

import java.util.Collection;
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
    private final Map<GridDirection, List<GridPattern>> tagMap; // maps tagged location to the patterns.
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
        tagMap = new HashMap<>();
        for (GridDirection direction : GridDirection.values()) {
            tagMap.put(direction, new LinkedList<>());
        }
    }

    /**
     * Creates list of all patterns that are affected by this spot.
     * @return the list of patterns.
     */
    public Collection<GridPattern> createPatternList() {
        if (isFree()) {
            throw new IllegalStateException("GridSpot is free, cannot create patterns");
        }
        List<GridPattern> results = new LinkedList<>();
        // first, check for castle and road patterns:
        for (GridDirection direction : GridDirection.tilePositions()) {
            TerrainType terrain = tile.getTerrain(direction); // get terrain type.
            if ((terrain == TerrainType.CASTLE || terrain == TerrainType.ROAD) && hasNoTagConnectedTo(direction)) {
                results.add(new CastleAndRoadPattern(this, direction, terrain));
            }
        }
        // then, check fields:
        for (GridDirection direction : GridDirection.values()) {
            TerrainType terrain = tile.getTerrain(direction); // get terrain type.
            if (terrain == TerrainType.FIELDS && hasNoTagConnectedTo(direction)) {
                results.add(new FieldsPattern(this, direction));
            }
        }
        // then check for monastery patterns:
        addPatternIfMonastery(this, results); // the tile itself
        grid.getNeighbors(this, false, GridDirection.neighbors()).forEach(it -> addPatternIfMonastery(it, results));
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
     * Getter for the grid of which this grid spot is part of.
     * @return the containing grid.
     */
    public Grid getGrid() {
        return grid;
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
     * Method determines if tile recently was tagged by any grid pattern checks on a specific position or a position
     * connected to the specific position.
     * @param tilePosition is the specific position.
     * @return true if not tagged.
     */
    public Boolean hasNoTagConnectedTo(GridDirection tilePosition) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (tile.hasConnection(tilePosition, otherPosition) && containsKey(otherPosition)) {
                return false;
            }
        }
        return true;
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
            if (tile.hasConnection(tilePosition, otherPosition) && tagMap.get(otherPosition).contains(taggedBy)) {
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
     * Checks whether the grid spot is occupied.
     * @return true if occupied
     */
    public boolean isOccupied() {
        return tile != null;
    }

    /**
     * Method determines if tile recently was tagged by grid pattern checks on a specific position or not.
     * @param tilePosition is the specific position.
     * @return true if it was not tagged.
     */
    public Boolean isUntagged(GridDirection tilePosition) {
        return !containsKey(tilePosition);
    }

    public boolean isPlaceable(Tile tile) {
        if (isOccupied()) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            GridSpot neighbor = grid.getNeighbor(this, direction);
            if (neighbor == null) { // free space
                if (grid.isClosingFreeSpotsOff(this, direction)) {
                    return false; // you can't close off free spaces
                }
            } else { // if there is a neighbor in the direction.
                neighborCount++;
                if (!tile.canConnectTo(direction, neighbor.getTile())) {
                    return false; // if it does not fit to terrain, it can't be placed.
                }
            }
        }
        return neighborCount > 0; // can be placed beneath another tile.
    }
    

    /**
     * Removes all the tags from the tile.
     */
    public void removeTags() {
        tagMap.values().forEach(it -> it.clear());
    }

    /**
     * Removes all the tags of a specific pattern from the tile.
     * @param pattern is the specific grid pattern.
     */
    public void removeTagsFrom(GridPattern pattern) {
        tagMap.values().forEach(it -> it.remove(pattern));
    }

    /**
     * Set tile on grid spot if possible.
     * @param tile is the tile to set.
     * @return true if the tile could be placed.
     */
    public boolean set(Tile tile) {
        if (isPlaceable(tile)) {
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
        tagMap.get(direction).add(taggedBy);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[on: (" + x + "|" + y + "), Occupied:" + isOccupied() + "]";
    }

    private void addPatternIfMonastery(GridSpot spot, List<GridPattern> patternList) {
        if (spot.getTile().getTerrain(CENTER) == TerrainType.MONASTERY && spot.hasNoTagConnectedTo(CENTER)) {
            patternList.add(new MonasteryGridPattern(spot));
        }
    }

    private boolean containsKey(GridDirection direction) {
        return !tagMap.get(direction).isEmpty();
    }
}
