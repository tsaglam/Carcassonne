package carcassonne.model.terrain;

import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

/**
 * Enumeration for the terrain type. Is used to specify the terrain of a tile on its different positions.
 * @author Timur Saglam
 */
public enum TerrainType {
    CASTLE,
    ROAD,
    MONASTERY,
    FIELDS,
    OTHER;

    /**
     * Generates a list of the basic terrain types, which is every terrain except {@link Other}.
     * @return a list of CASTLE, ROAD, MONASTERY, FIELDS.
     */
    public static List<TerrainType> basicTerrain() {
        return List.of(CASTLE, ROAD, MONASTERY, FIELDS);
    }
}