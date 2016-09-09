package carcassonne.model.grid;

import java.util.LinkedList;
import java.util.List;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * The playing grid class.
 * @author Timur Saglam
 */
public class Grid {
    private final int width;
    private final int height;
    private GridSpot[][] spots;
    private GridSpot foundation;

    /**
     * Basic constructor
     * @param width is the grid width.
     * @param height is the grid height.
     * @param foundationType is the tile type of the first tile in the middle of the grid.
     */
    public Grid(int width, int height, TileType foundationType) {
        this.width = width;
        this.height = height;
        spots = new GridSpot[width][height];
        placeFoundation(foundationType);
    }

    /**
     * Returns list of all patterns on the grid.
     * @return the list of patterns.
     */
    public List<GridPattern> getAllPatterns() {
        List<GridPattern> patterns = new LinkedList<GridPattern>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (spots[x][y].isOccupied()) {
                    patterns.addAll(spots[x][y].createPatternList());
                }
            }
        }
        for (GridPattern pattern : patterns) {
            pattern.removeTileTags(); // IMPORTANT
        }
        return patterns; // get patterns.
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
        checkParameters(x, y);
        List<Tile> list = new LinkedList<Tile>();
        Tile neighbor;
        for (GridDirection to : GridDirection.directNeighbors()) {
            if (spots[x][y].getTile().isConnected(from, to) && from != to) { // if connected
                neighbor = getNeighbour(x, y, to);
                if (neighbor != null) { // if is on grid
                    list.add(neighbor);
                }
            }
        }
        return list;
    }

    /**
     * Creates a list of direct neighbors.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbors
     */
    public List<Tile> getDirectNeighbors(int x, int y) {
        checkParameters(x, y);
        List<Tile> list = new LinkedList<Tile>();
        Tile neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) {
            neighbor = getNeighbour(x, y, direction);
            if (neighbor != null) {
                list.add(neighbor);
            }
        }
        return list;
    }

    /**
     * Creates a list of direct neighbors.
     * @param tile is the tile the neighbors get created of
     * @return the list of neighbors
     */
    public List<Tile> getDirectNeighbors(Tile tile) {
        return getDirectNeighbors(tile.getX(), tile.getY());
    }

    /**
     * Returns the first tile of round, the foundation tile.
     * @return the tile.
     */
    public Tile getFoundation() {
        return foundation.getTile();
    }

    /**
     * Getter for the grid height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method checks for modified patterns on the grid. As a basis it uses the coordinates of the
     * last placed tile.
     * @param x is the x coordinate of the last placed tile.
     * @param y is the y coordinate of the last placed tile.
     * @return the list of the modified patterns.
     */
    public List<GridPattern> getModifiedPatterns(int x, int y) {
        checkParameters(x, y);
        if (spots[x][y].isFree()) {
            throw new IllegalArgumentException("Can't check for patterns on an free grid space");
        }
        List<GridPattern> modifiedPatterns = spots[x][y].createPatternList();
        for (GridPattern pattern : modifiedPatterns) {
            pattern.removeTileTags(); // VERY IMPORTANT!
        }
        return modifiedPatterns; // get patterns.
    }

    /**
     * Creates a list of neighbors of a tile on specific coordinates.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the list of neighbors
     */
    public List<Tile> getNeighbors(int x, int y) {
        checkParameters(x, y);
        List<Tile> list = new LinkedList<Tile>();
        Tile neighbor;
        for (GridDirection direction : GridDirection.neighbors()) {
            neighbor = getNeighbour(x, y, direction);
            if (neighbor != null) {
                list.add(neighbor);
            }
        }
        return list;
    }

    /**
     * Creates a list of neighbors of a tile.
     * @param ofTile is the tile.
     * @return the list of neighbors
     */
    public List<Tile> getNeighbors(Tile ofTile) {
        return getNeighbors(ofTile.getX(), ofTile.getY());
    }

    /**
     * Returns a specific neighbor of a spot if the neighbor exists.
     * @param spot is the spot.
     * @param direction is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
    public Tile getNeighbour(GridSpot spot, GridDirection direction) {
        return getNeighbour(spot.getX(), spot.getY(), direction);
    }

    /**
     * Returns a specific neighbor of a tile if the neighbor exists.
     * @param x is the tiles x coordinate
     * @param y is the tiles y coordinate
     * @param direction is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
    public Tile getNeighbour(int x, int y, GridDirection direction) {
        int newX = GridDirection.addX(x, direction);
        int newY = GridDirection.addY(y, direction);
        if (isOnGrid(newX, newY)) {
            return spots[newX][newY].getTile(); // return calculated neighbor if valid:
        }
        return null;  // return null if tile not placed or not on grid.
    }

    /**
     * Returns a specific neighbor of a tile if the neighbor exists.
     * @param tile is the tile.
     * @param direction is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
    public Tile getNeighbour(Tile tile, GridDirection direction) {
        return getNeighbour(tile.getX(), tile.getY(), direction);
    }

    /**
     * Safe getter for tiles.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the spot
     * @throws IllegalArgumentException if the requested tile is out of grid.
     */
    public GridSpot getSpot(int x, int y) {
        checkParameters(x, y);
        return spots[x][y];
    }

    /**
     * Getter for the grid width.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    // method checks if a grid space is part of a walled off grid space set TODO comment
    public boolean isClosingFreeSpaceOff(GridSpot spot, GridDirection direction) {
        boolean[][] visitedPositions = new boolean[width][height];
        visitedPositions[spot.getX()][spot.getY()] = true; // mark starting point as visited
        return !findBoundary(spot, direction, visitedPositions); // start recursion
    }

    /**
     * Checks whether the grid is full.
     * @return true if full.
     */
    public boolean isFull() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (spots[x][y].isFree()) {
                    return false; // grid is not full if one position is free
                }
            }
        }
        return true;
    }

    /**
     * Checks whether specific coordinates are on the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if it is on the grid.
     */
    public boolean isOnGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Tries to place a tile on a spot on the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @param tile is the tile to place
     * @return true if it was successful, false if spot is occupied.
     */
    public boolean place(int x, int y, Tile tile) {
        checkParameters(x, y);
        checkParameters(tile);
        return spots[x][y].set(tile);
    }

    /**
     * Error checker method for other methods in this class. It just checks whether specific
     * coordinates are on the grid and throws an error if not.
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    private void checkParameters(int x, int y) {
        if (!isOnGrid(x, y)) {
            throw new IllegalArgumentException("tile coordinates are out of grid: x=" + x + " & y=" + y);
        }
    }

    /**
     * Error checker method for other methods in this class. It just checks whether specific tile is
     * not null.
     * @param tile the tile to check
     */
    private void checkParameters(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile can't be null.");
        } else if (tile.getType() == TileType.Null) {
            throw new IllegalArgumentException("Tile from type TileType.Null can't be placed.");
        }
    }

    // method tries to find a path of free grid spaces to the grid border.
    private boolean findBoundary(GridSpot spot, GridDirection direction, boolean[][] visitedPositions) {
        int newX = GridDirection.addX(spot.getX(), direction); // get coordinates
        int newY = GridDirection.addY(spot.getY(), direction); // of free space
        if (isOnGrid(newX, newY)) { // if on grid
            if (spots[newX][newY].isOccupied()) {
                return false; // is a tile, can't go through tiles
            } else if (!visitedPositions[newX][newY]) { // if not visited
                visitedPositions[newX][newY] = true; // mark as visited
                for (GridDirection newDirection : GridDirection.directNeighbors()) { // recursion
                    if (findBoundary(spots[newX][newY], newDirection, visitedPositions)) {
                        return true; // found boundary
                    }
                }
            }
        } else { // if not on grid
            return true; // found boundary
        }
        return false; // has not found boundary
    }

    /**
     * Places a specific tile in the middle of the grid.
     * @param tileType is the type of that specific tile.
     */
    private void placeFoundation(TileType tileType) {
        int centerX = Math.round((width - 1) / 2);
        int centerY = Math.round((height - 1) / 2);
        foundation = spots[centerX][centerY];
        foundation.forcePlacement(TileFactory.create(tileType));
    }

}