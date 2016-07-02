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
        for (GridDirection direction : GridDirection.values()) {
            if (direction == GridDirection.TOP_RIGHT) {
                break;
            }
            if (getNeighbour(x, y, direction) != null) {
                list.add(getNeighbour(x, y, direction));
            }
        }
        return list;
    }

    /**
     * Creates a list of neighbors.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbors
     */
    public List<Tile> getNeighbors(int x, int y) {
        List<Tile> list = new LinkedList<Tile>();
        for (GridDirection direction : GridDirection.values()) {
            if (getNeighbour(x, y, direction) != null) {
                list.add(getNeighbour(x, y, direction));
            }
        }
        return list;
    }
    
    public Tile getNeighbour(int x, int y, GridDirection dir) {
        // changes the x coordinate according to direction:
        if (dir == GridDirection.TOP_RIGHT || dir == GridDirection.RIGHT || dir == GridDirection.BOTTOM_RIGHT) {
            x++;
        } else if (dir == GridDirection.TOP_LEFT || dir == GridDirection.LEFT || dir == GridDirection.BOTTOM_LEFT) {
            x--;
        }
        // changes the y coordinate according to direction:
        if (dir == GridDirection.BOTTOM_LEFT || dir == GridDirection.BOTTOM || dir == GridDirection.BOTTOM_RIGHT) {
            y++;
        } else if (dir == GridDirection.TOP_LEFT || dir == GridDirection.TOP || dir == GridDirection.TOP_RIGHT) {
            y--;
        }
        // return calculated neighbor if valid:
        if (isOnGrid(x, y)) {
            return tile[x][y];
        }
        return null;  // return null if tile not placed or not on grid.
    }

    /**
     * Creates a list of tiles that are connected to a specific tile with the terrain 
     * in a specific direction on the tile.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of connected tiles.
     */
    public List<Tile> getConnectedTiles(int x, int y, GridDirection connectedFrom) {
        List<Tile> list = new LinkedList<Tile>();
        //TODO implement me
        return null;
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