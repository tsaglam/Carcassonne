package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class Monastery extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public Monastery() {
        super(FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY);
    }
}
