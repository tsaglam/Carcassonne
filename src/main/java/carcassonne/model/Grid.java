/**
 * 
 */
package carcassonne.model;

import java.util.List;

/**
 * @author Timur Saglam 
 * The playing grid class.
 */
public class Grid {
    private int width;
    private int height;
    private Tile tile[][];

    /**
     * Basic constructor
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        tile = new Tile[width][height];
    }

    /**
     * Safe getter for tiles.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the tile.
     * @throws IllegalArgumentException if the requested tile is out of grid.
     */
    public Tile getTile(int x, int y) {
        if (x > 0 && x < width && y > 0 && y < height) {
            return tile[x][x];
        }
        throw new IllegalArgumentException("tile coordinates are out of grid");
    }

    public boolean isOccupied(int x, int y) {
        // TODO method has to be implemented!
        return false;
    }

    public boolean isFull(int x, int y) {
        // TODO method has to be implemented!
        return false;
    }

    public List<Tile> getNeighbours(int x, int y) {
        // TODO method has to be implemented!
        return null;
    }

}