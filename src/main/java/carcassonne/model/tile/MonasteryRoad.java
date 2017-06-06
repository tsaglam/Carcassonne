package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class MonasteryRoad extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public MonasteryRoad() {
        super(FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY);
    }
}
