package carcassonne.model.tile;

/**
 * Enumeration for the terrain type. Is used to specify the terrain of a tile on its different
 * positions.
 * @author Timur Saglam
 */
public enum TerrainType {
    CASTLE, ROAD, MONASTERY, FIELDS, OTHER;

    /**
     * Generates an array of the basic terrain types.
     * @return an array of an array of CASTLE, ROAD, MONASTERY, FIELDS, OTHER.
     */
    public static TerrainType[] basicTerrain() {
        return new TerrainType[] { CASTLE, ROAD, MONASTERY, FIELDS, OTHER };
    }
}