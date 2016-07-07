package carcassonne.model.tile;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import carcassonne.model.TerrainType;
import carcassonne.model.grid.GridDirection;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile {
    private HashMap<GridDirection, TerrainType> terrainMap;
    private Image image;
    private Boolean tag;
    private TileType type;

    /**
     * Simple constructor.
     * @param top is the top terrain type.
     * @param right is the right terrain type.
     * @param bottom is the bottom terrain. type
     * @param left is the left terrain type.
     * @param middle is the middle terrain type.
     */
    public Tile(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle, String tilePath, TileType type) {
        terrainMap = new HashMap<GridDirection, TerrainType>(5);
        terrainMap.put(GridDirection.TOP, top);
        terrainMap.put(GridDirection.RIGHT, right);
        terrainMap.put(GridDirection.BOTTOM, bottom);
        terrainMap.put(GridDirection.LEFT, left);
        terrainMap.put(GridDirection.MIDDLE, middle);
        this.type = type;
        this.image = new ImageIcon(tilePath).getImage();
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
    public boolean isConnected(GridDirection fromDirection, GridDirection toDirection) {
        TerrainType middle = getTerrainAt(GridDirection.MIDDLE);
        TerrainType from = getTerrainAt(fromDirection);
        TerrainType to = getTerrainAt(toDirection);
        return from.equals(to) && (middle.equals(to) || middle.equals(TerrainType.OTHER));
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
    public Image getImage() {
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
