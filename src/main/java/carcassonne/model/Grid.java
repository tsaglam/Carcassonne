package carcassonne.model;

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
     * 
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
     * TODO
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if occupied
     */
    public boolean isOccupied(int x, int y) {
        // TODO method has to be implemented!
        return false;
    }

    /**
     * Checks whether the grid is full.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if full.
     */
    public boolean isFull(int x, int y) {
        // TODO method has to be implemented!
        return false;
    }

    /**
     * TODO
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbours
     */
    public List<Tile> getNeighbours(int x, int y) {
        // TODO method has to be implemented!
        return null;
    }

}