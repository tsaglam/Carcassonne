package carcassonne.model.tile;

/**
 * enum for the terrain type.
 * @author Timur Saglam
 */
public enum TerrainType {
    CASTLE, ROAD, MONASTERY, FIELDS, OTHER;

    /**
     * Generates an array of the basic terrain types.
     * @return an array of an array of CASTLE, ROAD, MONASTERY, FIELDS, OTHER.
     */
    public static TerrainType[] basicTerrain() {
        TerrainType[] directions = { CASTLE, ROAD, MONASTERY, FIELDS, OTHER };
        return directions;
    }
}