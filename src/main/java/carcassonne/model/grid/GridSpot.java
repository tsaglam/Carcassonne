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

    private final Grid grid;
    private Tile tile;
    private final int x;
    private final int y;

    public GridSpot(Grid grid, int x, int y) {
        this.grid = grid;
        this.x = x;
        this.y = y;
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
        addPatternIfMonastery(this, results); // the tile itself
        for (GridSpot neighbour : grid.getNeighbors(this)) {
            addPatternIfMonastery(neighbour, results); // neighbors
        }
        return results; // return all patterns.
    }

    public boolean forcePlacement(Tile tile) {
        if (isPlaceable(tile, true)) {
            this.tile = tile;
            tile.setPosition(this);
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

    public boolean set(Tile tile) {
        if (isPlaceable(tile, false)) {
            tile.setPosition(this);
            this.tile = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.
    }

    private void addPatternIfMonastery(GridSpot spot, List<GridPattern> patternList) {
        Tile monasteryTile = spot.getTile();
        TileType type = monasteryTile.getType();
        if (type == TileType.Monastery || type == TileType.MonasteryRoad) {
            if (monasteryTile.isNotConnectedToAnyTag(GridDirection.MIDDLE)) {
                patternList.add(new MonasteryGridPattern(spot, grid));
            }
        }
    }

    private boolean isPlaceable(Tile tile, boolean freePlacement) {
        if (isOccupied()) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        GridSpot neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            neighbor = grid.getNeighbour(this, direction);
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
