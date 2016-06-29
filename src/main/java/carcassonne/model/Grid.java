package carcassonne.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The playing grid class.
 * @author Timur Saglam
 */
public class Grid {
    private int width;
    private int height;
    private Tile[][] tile;

    /**
     * Basic constructor
     * @param width is the grid width.
     * @param height is the grid height.
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        tile = new Tile[width][height];
    }

    /**
     * Safe getter for tiles.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the tile
     * @throws IllegalArgumentException if the requested tile is out of grid.
     */
    public Tile getTile(int x, int y) {
        if (x > 0 && x < width && y > 0 && y < height) {
            return tile[x][x];
        }
        throw new IllegalArgumentException("tile coordinates are out of grid");
    }

    /**
     * Checks whether a specific tile of the grid is occupied.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if occupied
     */
    public boolean isOccupied(int x, int y) {
        return tile[x][y] != null;
    }

    /**
     * Checks whether a specific tile of the grid is free.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if free
     */
    public boolean isFree(int x, int y) {
        return tile[x][y] == null;
    }

    /**
     * Checks whether the grid is full.
     * @return true if full.
     */
    public boolean isFull() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (isFree(x, y)) {
                    return false; // grid is not full if one position is free
                }
            }
        }
        return true;
    }

    /**
     * Creates a list of direct neighbors.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbors
     */
    public List<Tile> getDirectNeighbors(int x, int y) {
        List<Tile> list = new LinkedList<Tile>();
        addIfValid(x, y - 1, list); // add UP
        addIfValid(x + 1, y, list); // add RIGHT
        addIfValid(x, y + 1, list); // add DOWN
        addIfValid(x - 1, y, list); // add LEFT
        return list;
    }

    private void addIfValid(int x, int y, List<Tile> list) {
        if (isOnGrid(x, y)) { // if the tile is valid.
            list.add(tile[x][y]); // add it to a specific list.
        }
    }

    /**
     * Creates a list of neighbors.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbors
     */
    public List<Tile> getNeighbors(int x, int y) {
        List<Tile> list = getDirectNeighbors(x, y); // list with direct neighbors.
        addIfValid(x + 1, y - 1, list); // add UP RIGHT
        addIfValid(x + 1, y + 1, list); // add DOWN RIGHT
        addIfValid(x - 1, y + 1, list); // add DOWN LEFT
        addIfValid(x - 1, y - 1, list); // add UP LEFT
        return list;
    }

    /**
     * Checks whether specific coordinates are on the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if it is on the grid.
     */
    public boolean isOnGrid(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return true;
        }
        return false;
    }

}