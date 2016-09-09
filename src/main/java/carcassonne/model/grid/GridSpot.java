package carcassonne.model.grid;

import java.util.LinkedList;
import java.util.List;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;

/**
 * The class represents a spot on the grid.
 * @author Timur Saglam
 */
public class GridSpot {

    private Tile tile;
    private final Grid grid;
    private final int x;
    private final int y;

    public GridSpot(Grid grid, int x, int y) {
        this.grid = grid;
        this.x = x;
        this.y = y;
    }

    public boolean set(Tile tile) {
        if (isPlaceable(tile, false)) {
            tile.setPosition(x, y);
            this.tile = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.   
    }
    
    public boolean forcePlacement(Tile tile) {
        if (isPlaceable(tile, true)) {
            this.tile = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.
    }

    public Tile getTile() {
        return tile;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
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
    
 // creates list of all patterns.
    public List<GridPattern> createPatternList() {
        List<GridPattern> results = new LinkedList<GridPattern>();
        TerrainType terrain;
        // first, check for castle and road patterns:
        for (GridDirection direction : GridDirection.directNeighbors()) {
            terrain = tile.getTerrain(direction); // get terrain type.
            if (terrain != TerrainType.FIELDS && tile.isNotConnectedToAnyTag(direction)) {
                results.add(new CastleAndRoadPattern(this, direction, terrain, grid));
            }
        }
        // then check for monastery patterns:
        addPatternIfMonastery(tile, results); // the tile itself
        for (Tile neighbour : grid.getNeighbors(tile)) {
            addPatternIfMonastery(neighbour, results); // neighbors
        }
        return results; // return all patterns.
    }
    
    private void addPatternIfMonastery(Tile startingTile, List<GridPattern> patternList) {
        TileType type = startingTile.getType();
        if (type == TileType.Monastery || type == TileType.MonasteryRoad) {
            if (startingTile.isNotConnectedToAnyTag(GridDirection.MIDDLE)) {
                patternList.add(new MonasteryGridPattern(this, grid));
            }
        }
    }

    private boolean isPlaceable(Tile tile, boolean freePlacement) {
        if (isOccupied()) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        Tile neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            neighbor = grid.getNeighbour(this, direction);
            if (neighbor == null) { // free space
                if (grid.isClosingFreeSpaceOff(this, direction)) {
                    return false; // you can't close of free spaces
                }
            } else { // if there is a neighbor in the direction.
                neighborCount++;
                if (!tile.hasSameTerrain(direction, neighbor)) {
                    return false; // if it does not fit to terrain, it can't be placed.
                }
            }
        }
        return neighborCount > 0 || freePlacement; // can be placed beneath another tile.
    }
}
