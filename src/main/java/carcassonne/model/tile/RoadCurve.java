package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class RoadCurve extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public RoadCurve() {
        super(FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
    }
}
