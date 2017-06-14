package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;

import carcassonne.model.terrain.Terrain;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class RoadJunctionLarge extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public RoadJunctionLarge() {
        super(new Terrain(ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER));
    }
}
