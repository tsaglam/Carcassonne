package carcassonne.model.tile;

import java.io.File;
import java.util.HashMap;

import javax.swing.ImageIcon;

import carcassonne.model.Meeple;
import carcassonne.model.grid.GridDirection;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile {
    private HashMap<GridDirection, TerrainType> terrainMap;
    private ImageIcon image;
    private Boolean tag;
    private TileType type;
    private Meeple meeple;

    /**
     * Simple constructor.
     * @param top is the top terrain type.
     * @param right is the right terrain type.
     * @param bottom is the bottom terrain. type
     * @param left is the left terrain type.
     * @param middle is the middle terrain type.
     */
    public Tile(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle, String tilePath, TileType type) {
        if (!new File(tilePath).exists()) {
            throw new IllegalArgumentException("Image path is not valid : " + tilePath);
        } else if (type == null) {
            throw new IllegalArgumentException("Tile can't be null");
        }
        terrainMap = new HashMap<GridDirection, TerrainType>(5);
        terrainMap.put(GridDirection.TOP, top);
        terrainMap.put(GridDirection.RIGHT, right);
        terrainMap.put(GridDirection.BOTTOM, bottom);
        terrainMap.put(GridDirection.LEFT, left);
        terrainMap.put(GridDirection.MIDDLE, middle);
        this.type = type;
        this.image = new ImageIcon(tilePath);
        tag = false;
        meeple = null;

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
    public boolean isConnected(GridDirection fromDirection, GridDirection toDirection) {
        TerrainType middle = getTerrainAt(GridDirection.MIDDLE);
        TerrainType from = getTerrainAt(fromDirection);
        TerrainType to = getTerrainAt(toDirection);
        if (middle == TerrainType.CASTLE_AND_ROAD) { // special case.
            return from.equals(to) && (from.equals(TerrainType.CASTLE) || from.equals(TerrainType.ROAD));
        }
        return from.equals(middle) && (middle.equals(to)); // normal case. basic connection.
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotate() {
        TerrainType temporary = terrainMap.get(GridDirection.LEFT);
        for (GridDirection direction : GridDirection.directNeighbors()) {
            temporary = terrainMap.put(direction, temporary);
        }
        // TODO rotate picture itself.
    }

    /**
     * Places a meeple on the tile, if the tile has not already one placed.
     * @param meeple the meeple to place on the tile.
     */
    public void setMeeple(Meeple meeple) {
        if (this.meeple != null) {
            throw new IllegalArgumentException("Tile can not have already a meeple placed on it.");
        }
        this.meeple = meeple;
    }

    /**
     * Removes and returns the meeple from the tile. Calls Meeple.removePlacement.
     */
    public void removeMeeple() {
        meeple.removePlacement();
        meeple = null;
    }

    /**
     * Checks whether the tile has a meeple.
     * @return true if it has a meeple
     */
    public boolean hasMeeple() {
        return meeple != null;
    }

    /**
     * Getter for the meeple of the tile.
     * @return
     */
    public Meeple getMeeple() {
        return meeple;
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

    /**
     * Getter for the tile image.
     * @return the image of the tile.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * Getter for the tile type.
     * @return the type
     */
    public TileType getType() {
        return type;
    }
}
