package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class RoadJunctionSmall extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public RoadJunctionSmall() {
        super(FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER);
    }
}
