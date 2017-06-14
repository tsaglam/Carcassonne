package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;

import carcassonne.model.terrain.Terrain;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleCenter extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleCenter() {
        super(new Terrain(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE));
    }
}
