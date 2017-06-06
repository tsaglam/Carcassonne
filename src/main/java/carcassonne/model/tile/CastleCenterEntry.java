package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleCenterEntry extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleCenterEntry() {
        super(CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE);
    }
}
