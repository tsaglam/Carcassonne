package carcassonne.model.grid;

import static carcassonne.model.grid.GridDirection.BOTTOM;
import static carcassonne.model.grid.GridDirection.BOTTOM_LEFT;
import static carcassonne.model.grid.GridDirection.BOTTOM_RIGHT;
import static carcassonne.model.grid.GridDirection.LEFT;
import static carcassonne.model.grid.GridDirection.RIGHT;
import static carcassonne.model.grid.GridDirection.TOP;
import static carcassonne.model.grid.GridDirection.TOP_LEFT;
import static carcassonne.model.grid.GridDirection.TOP_RIGHT;
import static carcassonne.model.grid.GridDirection.directNeighbors;
import static carcassonne.model.grid.GridDirection.neighbors;

import java.util.LinkedList;
import java.util.List;

import carcassonne.model.tile.Tile;

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
     * Tries to place a tile on a spot on the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @param tile is the tile to place
     * @return true if it was successful, false if spot is occupied.
     */
    public boolean place(int x, int y, Tile tile) {
        checkCoordinates(x, y);
        if (isOccupied(x, y)) {
            return false; // tile cant be placed, spot is occupied.
        } else {
            this.tile[x][y] = tile;
            return true; // tile was successfully placed.
        }
    }

    /**
     * Checks whether a specific tile of the grid is occupied.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if occupied
     */
    public boolean isOccupied(int x, int y) {
        checkCoordinates(x, y);
        return tile[x][y] != null;
    }

    /**
     * Checks whether a specific tile of the grid is free.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if free
     */
    public boolean isFree(int x, int y) {
        checkCoordinates(x, y);
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
        checkCoordinates(x, y);
        List<Tile> list = new LinkedList<Tile>();
        for (GridDirection direction : directNeighbors()) {
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
        checkCoordinates(x, y);
        List<Tile> list = new LinkedList<Tile>();
        for (GridDirection direction : neighbors()) {
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
        int newX = x;
        int newY = y;
        // changes the x coordinate according to direction:
        if (dir == TOP_RIGHT || dir == RIGHT || dir == BOTTOM_RIGHT) {
            newX++;
        } else if (dir == TOP_LEFT || dir == LEFT || dir == BOTTOM_LEFT) {
            newX--;
        }
        // changes the y coordinate according to direction:
        if (dir == BOTTOM_LEFT || dir == BOTTOM || dir == BOTTOM_RIGHT) {
            newY++;
        } else if (dir == TOP_LEFT || dir == TOP || dir == TOP_RIGHT) {
            newY--;
        }
        // return calculated neighbor if valid:
        if (isOnGrid(newX, newY)) {
            return tile[newX][newY];
        }
        return null;  // return null if tile not placed or not on grid.
    }

    /**
     * Creates a list of tiles that are connected to a specific tile with the terrain in a specific
     * direction on the tile.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @param from is the direction the tile is connected from
     * @return the list of connected tiles.
     */
    public List<Tile> getConnectedTiles(int x, int y, GridDirection from) {
        checkCoordinates(x, y);
        List<Tile> list = new LinkedList<Tile>();
        for (GridDirection to : directNeighbors()) {
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
     * Error checker method for other methods in this class. It just checks whether specific
     * coordinates are on the grid and throws an error if not.
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    private void checkCoordinates(int x, int y) {
        if (!isOnGrid(x, y)) {
            throw new IllegalArgumentException("tile coordinates are out of grid");
        }
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
     * Safe getter for tiles.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the tile
     * @throws IllegalArgumentException if the requested tile is out of grid.
     */
    public Tile getTile(int x, int y) {
        checkCoordinates(x, y);
        return tile[x][y];
    }

}