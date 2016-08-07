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
    private ImageIcon[] image; // tile image
    private int rotation;
    private Boolean tag;
    private TileType type;
    private Meeple meeple;
    private int x;
    private int y;

    /**
     * Simple constructor.
     * @param top is the top terrain type.
     * @param right is the right terrain type.
     * @param bottom is the bottom terrain. type
     * @param left is the left terrain type.
     * @param middle is the middle terrain type.
     */
    public Tile(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle, String tilePath, String fileType, TileType type) {
        if (!new File(tilePath + rotation + fileType).exists()) {
            throw new IllegalArgumentException("Image path is not valid : " + tilePath);
        } else if (type == null) {
            throw new IllegalArgumentException("Tile can't be null");
        }
        this.type = type;
        tag = false;
        meeple = null;
        buildTerrainMap(top, right, bottom, left, middle);
        loadImages(tilePath, fileType);
        x = -1;
        y = -1;
    }

    /**
     * Getter for the tile image. the image depends on the rotation.
     * @return the image of the tile with the tile specific rotation.
     */
    public ImageIcon getImage() {
        return image[rotation];
    }

    /**
     * Getter for the meeple of the tile.
     * @return
     */
    public Meeple getMeeple() {
        return meeple;
    }

    /**
     * return the terrain type on the tile in the specific direction.
     * @param direction is the specific direction.
     * @return the terrain type, or null if the direction is not mapped.
     */
    public TerrainType getTerrain(GridDirection direction) {
        return terrainMap.get(direction);
    }

    /**
     * Getter for the tile type.
     * @return the type
     */
    public TileType getType() {
        return type;
    }

    /**
     * Getter for the x coordinate.
     * @return the x coordinate
     */
    public int getX() {
        if (x == -1) {
            throw new IllegalStateException("The position of the tile has not been set yet");
        }
        return x;
    }

    /**
     * Getter for the y coordinate.
     * @return the y coordinate
     */
    public int getY() {
        if (y == -1) {
            throw new IllegalStateException("The position of the tile has not been set yet");
        }
        return y;
    }

    /**
     * Checks whether the tile has a meeple.
     * @return true if it has a meeple
     */
    public boolean hasMeeple() {
        return meeple != null;
    }

    /**
     * Checks whether two parts of a tile are connected through same terrain.
     * @param from is the part to check from.
     * @param to is the terrain to check to.
     * @return true if connected, false if not.
     */
    public boolean isConnected(GridDirection fromDirection, GridDirection toDirection) {
        TerrainType middle = getTerrain(GridDirection.MIDDLE);
        TerrainType from = getTerrain(fromDirection);
        TerrainType to = getTerrain(toDirection);
        if (middle == TerrainType.CASTLE_AND_ROAD) { // special case.
            return from.equals(to) && (from.equals(TerrainType.CASTLE) || from.equals(TerrainType.ROAD));
        }
        return from.equals(middle) && (middle.equals(to)); // normal case. basic connection.
    }

    /**
     * Checks whether this tile was already tagged. This is used for the structure checks.
     * @return true if tagged.
     */
    public Boolean isTagged() {
        return tag;
    }

    /**
     * Removes and returns the meeple from the tile. Calls Meeple.removePlacement.
     */
    public void removeMeeple() {
        meeple.removePlacement();
        meeple = null;
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        TerrainType temporary = terrainMap.get(GridDirection.LEFT);
        for (GridDirection direction : GridDirection.directNeighbors()) {
            temporary = terrainMap.put(direction, temporary); // rotate terrain:
        }
        rotation = (rotation >= 3) ? 0 : rotation + 1; // rotation indicator
    }

    /**
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        TerrainType temporary = terrainMap.get(GridDirection.RIGHT);
        GridDirection[] directions = { GridDirection.TOP, GridDirection.LEFT, GridDirection.BOTTOM, GridDirection.RIGHT };
        for (GridDirection direction : directions) { // rotate terrain:
            temporary = terrainMap.put(direction, temporary);
        }
        rotation = (rotation <= 0) ? 3 : rotation - 1; // rotation indicator
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
     * Gives the tile the position where it has been placed.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public void setPosition(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates can't be smaller than zero: " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the tag of the tile.
     * @param value is the value the tag gets set to.
     */
    public void setTag(Boolean value) {
        tag = value;
    }

    private void buildTerrainMap(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle) {
        terrainMap = new HashMap<GridDirection, TerrainType>(5); // create terrain map.
        terrainMap.put(GridDirection.TOP, top); // map the terrain types to the tile position.
        terrainMap.put(GridDirection.RIGHT, right);
        terrainMap.put(GridDirection.BOTTOM, bottom); // TODO (LOW) Update model!
        terrainMap.put(GridDirection.LEFT, left);
        terrainMap.put(GridDirection.MIDDLE, middle);
    }

    private void loadImages(String tilePath, String fileType) {
        image = new ImageIcon[4]; // create image array.
        for (int i = 0; i <= 3; i++) { // for every image:
            image[i] = new ImageIcon(tilePath + i + fileType); // load it from path.
        }
    }
}
