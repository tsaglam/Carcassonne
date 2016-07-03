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
        for (GridDirection direction : GridDirection.directNeighbors()) {
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
        for (GridDirection direction : GridDirection.neighbors()) {
            if (getNeighbour(x, y, direction) != null) {
                list.add(getNeighbour(x, y, direction));
            }
        }
        return list;
    }

    /**
     * Returns a specific neighbor of a tile if the neighbor exists.
     * @param x is the tiles x coordinate
     * @param y is the tiles y coordinate
     * @param dir is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
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
     * Creates a list of tiles that are connected to a specific tile with the terrain in a specific
     * direction on the tile.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of connected tiles.
     */
    public List<Tile> getConnectedTiles(int x, int y, GridDirection from) {
        List<Tile> list = new LinkedList<Tile>();
        for (GridDirection to : GridDirection.directNeighbors()) {
            if (tile[x][y].isConnected(from, to) && from != to) { // if connected
                if (getNeighbour(x, y, to) != null) { // if is on grid
                    list.add(getNeighbour(x, y, to));
                }
            }
        }
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

    /**
     * Getter for the grid width.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for the grid height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Access to a tile of the grid itself.
     * @return the tile
     */
    public Tile at(int x, int y) {
        return tile[x][y];
    }

}