package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.CENTER;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * The class represents a spot on the grid.
 * @author Timur Saglam
 */
public class GridSpot {

    private final Grid grid;
    private final Map<GridDirection, Set<GridPattern>> tagMap; // maps tagged location to the patterns.
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
        tagMap = new EnumMap<>(GridDirection.class);
        for (GridDirection direction : GridDirection.values()) {
            tagMap.put(direction, new HashSet<>());
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
            if ((terrain == TerrainType.CASTLE || terrain == TerrainType.ROAD) && !isIndirectlyTagged(direction)) {
                results.add(new CastleAndRoadPattern(this, direction, terrain));
            }
        }
        // then, check fields:
        for (GridDirection direction : GridDirection.values()) {
            TerrainType terrain = tile.getTerrain(direction); // get terrain type.
            if (terrain == TerrainType.FIELDS && !isIndirectlyTagged(direction)) {
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
     * Determines if this grid spot was recently tagged by any grid pattern on a specified position or a position connected
     * to the specified position.
     * @param tilePosition is the specific position.
     * @return true if not directly or indirectly tagged.
     */
    public boolean isIndirectlyTagged(GridDirection tilePosition) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (isTagged(otherPosition) && tile.hasConnection(tilePosition, otherPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if this grid spot was recently tagged by a specific grid pattern on a specified position or a position
     * connected to the specified position.
     * @param tilePosition is the specific position.
     * @param tagger is the specific grid pattern.
     * @return true if not directly or indirectly tagged by the grid pattern.
     */
    public boolean isIndirectlyTaggedBy(GridDirection tilePosition, GridPattern tagger) {
        for (GridDirection otherPosition : GridDirection.values()) {
            if (tile.hasConnection(tilePosition, otherPosition) && tagMap.get(otherPosition).contains(tagger)) {
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
     * Checks whether a tile can be placed on this spot. This requires the spot to be free, the tile to be compatible with
     * the neighboring tiles, and being compatible with the enclave game rule if present.
     * @param tile is the tile to place.
     * @param allowEnclaves determines if it is legal to enclose free spots.
     * @return true if the tile can be placed.
     */
    public boolean isPlaceable(Tile tile, boolean allowEnclaves) {
        if (isOccupied()) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            GridSpot neighbor = grid.getNeighbor(this, direction);
            if (neighbor == null) { // free space
                if (!allowEnclaves && grid.isClosingFreeSpotsOff(this, direction)) {
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
        tagMap.values().forEach(Set::clear);
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
     * @param allowEnclaves determines if it is legal to enclose free spots.
     * @return true if the tile could be placed.
     */
    public boolean place(Tile tile, boolean allowEnclaves) {
        if (isPlaceable(tile, allowEnclaves)) {
            tile.setPosition(this);
            this.tile = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.
    }

    /**
     * Removes any placed tile from the grid spot, updates the position of the tile.
     */
    public void removeTile() { // TODO (HIGH) [AI] this should be only allowed for temporary tiles.
        if (tile != null) {
            tile.setPosition(null);
            tile = null;
        }
    }

    /**
     * tag the tile as recently checked by grid pattern checks for a specific direction.
     * @param direction is the tag direction.
     * @param tagger is the {@link GridPattern} that tagged the spot.
     */
    public void setTag(GridDirection direction, GridPattern tagger) {
        tagMap.get(direction).add(tagger);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[on: (" + x + "|" + y + "), Occupied:" + isOccupied() + "]";
    }

    private void addPatternIfMonastery(GridSpot spot, List<GridPattern> patternList) {
        if (spot.getTile().getTerrain(CENTER) == TerrainType.MONASTERY && !spot.isIndirectlyTagged(CENTER)) {
            patternList.add(new MonasteryPattern(spot));
        }
    }

    /**
     * Method determines if tile recently was tagged by grid pattern checks on a specific position or not.
     * @param tilePosition is the specific position.
     * @return true if it was tagged.
     */
    private Boolean isTagged(GridDirection direction) {
        return !tagMap.get(direction).isEmpty();
    }
}
