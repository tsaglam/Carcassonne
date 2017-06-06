package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleWallCurveLeft extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleWallCurveLeft() {
        super(CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
    }
}
