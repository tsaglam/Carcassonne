/**
 * 
 */
package carcassonne.model.grid;

import java.util.List;

import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;

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
    public MonasteryGridPattern(List<Tile> tiles) {
        super(TerrainType.MONASTERY);
        if (tiles.size() != 9) {
            throw new IllegalArgumentException("A MonastryGridPattern needs 9 tiles, not " + tiles.size());
        }
        for (Tile tile : tiles) {
            tileList.add(tile);
        }
        complete = true;
    }

}
