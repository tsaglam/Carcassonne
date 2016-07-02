package carcassonne.model;

import java.util.HashMap;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile {
    private HashMap<GridDirection, TerrainType> terrainMap;
    private Boolean tag;

    /**
     * Simple constructor.
     * @param top is the top terrain type.
     * @param right is the right terrain type.
     * @param bottom is the bottom terrain. type
     * @param left is the left terrain type.
     * @param middle is the middle terrain type.
     */
    public Tile(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle) {
        terrainMap = new HashMap<GridDirection, TerrainType>(5);
        terrainMap.put(GridDirection.TOP, top);
        terrainMap.put(GridDirection.RIGHT, right);
        terrainMap.put(GridDirection.BOTTOM, bottom);
        terrainMap.put(GridDirection.LEFT, left);
        terrainMap.put(GridDirection.MIDDLE, middle);
        tag = false;
    }

    /**
     * return the terrain type on the tile in the specific direction.
     * @param direction is the specific direction.
     * @return the terrain type.
     */
    public TerrainType getTerrainAt(GridDirection direction) {
        return terrainMap.get(direction);
    }

    /**
     * Checks whether two parts of a tile are connected through same terrain.
     * @param from is the part to check from.
     * @param to is the terrain to check to.
     * @return true if connected, false if not.
     */
    public boolean isConnected(GridDirection from, GridDirection to) {
        TerrainType middle = getTerrainAt(GridDirection.MIDDLE);
        return getTerrainAt(from).equals(middle) && getTerrainAt(to).equals(middle);
    }

    /**
     * Checks whether this tile was already tagged. This is used for the structure checks.
     * @return true if tagged.
     */
    public Boolean isTagged() {
        return tag;
    }

    /**
     * Sets the tag of the tile.
     * @param value is the value the tag gets set to.
     */
    public void setTag(Boolean value) {
        tag = value;
    }
}
