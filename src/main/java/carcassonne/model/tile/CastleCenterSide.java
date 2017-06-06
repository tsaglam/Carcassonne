package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleCenterSide extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleCenterSide() {
        super(CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE);
    }
}
