package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleCenter extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleCenter() {
        super(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE);
    }
}
