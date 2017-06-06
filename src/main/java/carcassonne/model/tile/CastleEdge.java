package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleEdge extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleEdge() {
        super(CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS);
    }
}
