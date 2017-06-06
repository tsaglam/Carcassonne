package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.OTHER;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleSidesEdge extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleSidesEdge() {
        super(CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS);
    }
}
