package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.ROAD;

/**
 * Specific subclass of the class {@link Tile}.
 * @author Timur Saglam
 */
public class CastleWallCurveRight extends Tile {

    /**
     * Simple constructor that sets the terrain.
     */
    public CastleWallCurveRight() {
        super(CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
    }
}
