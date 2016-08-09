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
     * @param terrain is the array containing the terrain information.
     * @param type is the tile type enum value of the tile.
     * @param tilePath is the path to the tiles.
     * @param fileType is the file type of the tiles.
     */
    public Tile(TerrainType[] terrain, TileType type, String tilePath, String fileType) {
        if (type == null || terrain == null || fileType == null || tilePath == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else if (!new File(tilePath + rotation + fileType).exists()) {
            throw new IllegalArgumentException("Image path is not valid: " + tilePath);
        } else if (terrain.length != 9) {
            throw new IllegalArgumentException("Terrain array is invalid: " + terrain.toString());
        }
        this.type = type;
        tag = false;
        meeple = null;
        buildTerrainMap(terrain);
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
    public boolean isConnected(GridDirection from, GridDirection to) {
        if (isDirectConnected(from, to)) {
            return true;
        } else if (from != GridDirection.MIDDLE && to != GridDirection.MIDDLE) {
            return isindirectConnected(from, to, 1) || isindirectConnected(from, to, -1);
        }
        return false;
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
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        GridDirection[] directions = { GridDirection.TOP, GridDirection.LEFT, GridDirection.BOTTOM, GridDirection.RIGHT };
        rotateTerrain(directions);
        GridDirection[] directions2 = { GridDirection.TOP_RIGHT, GridDirection.TOP_LEFT, GridDirection.BOTTOM_LEFT, GridDirection.BOTTOM_RIGHT };
        rotateTerrain(directions2);
        rotation = (rotation <= 0) ? 3 : rotation - 1; // rotation indicator
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        rotateTerrain(GridDirection.directNeighbors());
        rotateTerrain(GridDirection.indirectNeighbors());
        rotation = (rotation >= 3) ? 0 : rotation + 1; // rotation indicator
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

    // maps TerrainType from terrain array to GridDirection with same index:
    private void buildTerrainMap(TerrainType[] terrain) {
        terrainMap = new HashMap<GridDirection, TerrainType>(5); // create terrain map.
        GridDirection[] tilePosition = GridDirection.values();
        for (int i = 0; i < terrain.length; i++) {
            terrainMap.put(tilePosition[i], terrain[i]);
        }
    }

    // checks for direct connection through middle:
    private boolean isDirectConnected(GridDirection from, GridDirection to) {
        TerrainType middle = getTerrain(GridDirection.MIDDLE);
        return (getTerrain(from).equals(middle) && getTerrain(to).equals(middle));
    }

    // checks for indirect connection through the specified side from a specific start to a specific
    // destination. Side is either 1 (right) or -1 (left.)
    private boolean isindirectConnected(GridDirection from, GridDirection to, int side) {
        GridDirection current = from;
        GridDirection next;
        while (current != to) { // while not at destination:
            next = GridDirection.next(current, side); // get the next direction
            if (getTerrain(current) != getTerrain(next)) { // check if still connected
                return false;

            }
            current = next; // set new current
        }
        return true; // found connection from start to destination.
    }

    // uses path to load images for all rotations.
    private void loadImages(String tilePath, String fileType) {
        image = new ImageIcon[4]; // create image array.
        for (int i = 0; i <= 3; i++) { // for every image:
            image[i] = new ImageIcon(tilePath + i + fileType); // load it from path.
        }
    }

    private void rotateTerrain(GridDirection[] directions) {
        TerrainType temporary = terrainMap.get(directions[directions.length - 1]); // get last one
        for (GridDirection direction : directions) { // rotate terrain through temporary:
            temporary = terrainMap.put(direction, temporary);
        }
    }
}
