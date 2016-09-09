/**
 * 
 */
package carcassonne.model.grid;

import java.util.List;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;

/**
 * This class represents a specific kind of grid pattern, the grid patterns for the terrain type
 * MONASTERY.
 * @author Timur Saglam
 */
public class MonasteryGridPattern extends GridPattern {

    /**
     * Simple constructor that creates the pattern.
     * @param startingTile is the starting tile of the pattern, containing a monastery.
     * @param grid is the grid the pattern is created from.
     */
    public MonasteryGridPattern(GridSpot spot, Grid grid) {
        super(TerrainType.MONASTERY);
        Tile startingTile = spot.getTile();
        TileType tileType = startingTile.getType();
        if (tileType != TileType.Monastery && tileType != TileType.MonasteryRoad) {
            throw new IllegalArgumentException("Can't create monastery pattern from non monastery tile");
        }
        buildPattern(startingTile, grid);
    }

    private void buildPattern(Tile monasteryTile, Grid grid) {
        List<Tile> neighbors = grid.getNeighbors(monasteryTile);
        add(monasteryTile); // add monastery
        monasteryTile.setTag(GridDirection.MIDDLE, this);
        for (Tile neighbor : neighbors) {
            tileList.add(neighbor);
        }
        if (neighbors.size() == GridDirection.neighbors().length) {
            complete = true;
        }
    }
}
