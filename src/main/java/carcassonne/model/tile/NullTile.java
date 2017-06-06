package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.OTHER;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class NullTile extends Tile {

    /**
     * Simple constructor that sets the null terrain.
     */
    public NullTile() {
        super(OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER);
    }
}
