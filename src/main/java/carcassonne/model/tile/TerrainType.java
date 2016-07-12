package carcassonne.model.tile;

/**
 * enum for the terrain type.
 * @author Timur Saglam
 */
public enum TerrainType {
    CASTLE, ROAD, MONASTRY, FIELDS, OTHER, CASTLE_AND_ROAD;

    /**
     * Generates an array of the basic terrain types.
     * @return an array of an array of CASTLE, ROAD, MONASTRY, FIELDS, OTHER.
     */
    public static TerrainType[] basicTerrain() {
        TerrainType[] directions = { CASTLE, ROAD, MONASTRY, FIELDS, OTHER };
        return directions;
    }
}