/**
 * 
 */
package carcassonne.model.grid;

import java.util.List;

import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;

/**
 * This class represents a specific kind of grid pattern, the grid patterns for the terrain type
 * MONASTERY.
 * @author Timur Saglam
 */
public class MonasteryGridPattern extends GridPattern {

    /**
     * Simple constructor. MonastryGridPatterns are created instantly with all tiles, because they
     * are easy to detect. Also they are complete from object construction on.
     * @param tiles the tiles to add to the pattern.
     */
    public MonasteryGridPattern(Tile startingTile, Grid grid) {
        super(TerrainType.MONASTERY);
        TileType tileType = startingTile.getType();
        if (tileType != TileType.Monastery && tileType != TileType.MonasteryRoad) {
            throw new IllegalArgumentException("Can't create monastery pattern from non monastery tile");
        }
        buildPattern(startingTile, grid);
    }

    private void buildPattern(Tile monasteryTile, Grid grid) {
        List<Tile> neighbors = grid.getNeighbors(monasteryTile);
        add(monasteryTile); // add monastery
        for (Tile neighbor : neighbors) {
            tileList.add(neighbor);
        }
        if (neighbors.size() == 8) {
            complete = true;
        }
    }
}
