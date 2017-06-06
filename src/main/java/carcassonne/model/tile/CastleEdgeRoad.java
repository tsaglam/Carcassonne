package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleEdgeRoad extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleEdgeRoad() {
        super(CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD);
    }
}
