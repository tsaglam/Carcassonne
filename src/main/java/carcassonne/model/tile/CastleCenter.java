package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;

public class CastleCenter extends Tile {

    public CastleCenter() {
        super(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE);
    }
}
