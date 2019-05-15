package carcassonne.model.grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;

/**
 * The playing grid class.
 * @author Timur Saglam
 */
public class Grid {
    private final int width;
    private final int height;
    private final GridSpot[][] spots;
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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                spots[x][y] = new GridSpot(this, x, y);
            }
        }
        placeFoundation(foundationType);
    }

    /**
     * Returns list of all patterns on the grid.
     * @return the list of patterns.
     */
    public List<GridPattern> getAllPatterns() {
        List<GridPattern> patterns = new LinkedList<>();
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
     * Creates a list of spots that are connected to a specific spot with the terrain in a specific direction on the
     * spot.
     * @param spot is the spot on the grid where the tile is.
     * @param from is the direction the tile is connected from
     * @return the list of connected tiles.
     */
    public List<GridSpot> getConnectedTiles(GridSpot spot, GridDirection from) {
        checkParameters(spot);
        List<GridSpot> list = new LinkedList<>();
        for (GridDirection to : GridDirection.directNeighbors()) {
            if (spot.getTile().hasConnection(from, to) && from != to) { // if connected
                list.addAll(getNeighbors(spot, false, to));
            }
        }
        return list;
    }

    /**
     * Returns the spot of the first tile of round, the foundation tile.
     * @return the grid spot.
     */
    public GridSpot getFoundation() {
        return foundation;
    }

    /**
     * Getter for the grid height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method checks for modified patterns on the grid. As a basis it uses the coordinates of the last placed tile.
     * @param spot is the spot of the last placed tile.
     * @return the list of the modified patterns.
     */
    public List<GridPattern> getModifiedPatterns(GridSpot spot) {
        checkParameters(spot);
        if (spot.isFree()) {
            throw new IllegalArgumentException("Can't check for patterns on an free grid space");
        }
        List<GridPattern> modifiedPatterns = spot.createPatternList();
        for (GridPattern pattern : modifiedPatterns) {
            pattern.removeTileTags(); // VERY IMPORTANT!
        }
        return modifiedPatterns; // get patterns.
    }

    public GridSpot getNeighbor(GridSpot spot, GridDirection direction) {
        List<GridSpot> neighbors = getNeighbors(spot, false, direction);
        if (neighbors.isEmpty()) {
            return null; // return null if tile not placed or not on grid.
        } else {
            return neighbors.get(0);
        }
    }

    /**
     * Creates a list of neighbors of a tile on a specific spot.
     * @param spot is the spot.
     * @return the list of neighbors
     */
    public List<GridSpot> getNeighbors(GridSpot spot, boolean allowEmptySpots) {
        return getNeighbors(spot, allowEmptySpots, GridDirection.neighbors());
    }

    /**
     * Returns a specific neighbor of a grid spot if the neighbor exists and has a tile.
     * @param spot is the spot of the tile.
     * @param direction is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
    public List<GridSpot> getNeighbors(GridSpot spot, boolean allowEmptySpots, GridDirection... directions) {
        checkParameters(spot);
        ArrayList<GridSpot> neighbors = new ArrayList<>(directions.length);
        for (GridDirection direction : directions) {
            int newX = direction.addX(spot.getX());
            int newY = direction.addY(spot.getY());
            if (isOnGrid(newX, newY) && (allowEmptySpots || spots[newX][newY].isOccupied())) {
                neighbors.add(spots[newX][newY]); // return calculated neighbor if valid:
            }
        }
        return neighbors;
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

    /**
     * Checks whether a spot on the grid would close free spots off in a direction if a tile would be placed there.
     * @param spot is the spot.
     * @param direction is the direction.
     * @return true if it does.
     */
    public boolean isClosingFreeSpotsOff(GridSpot spot, GridDirection direction) {
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
     * Checks whether a specific spot on the grid is valid.
     * @param spot is the spot
     * @return true if it is on the grid.
     */
    public boolean isOnGrid(GridSpot spot) {
        return spot != null && spot.equals(spots[spot.getX()][spot.getY()]);
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

    private void checkParameters(GridSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Spot can't be null!");
        } else if (!spots[spot.getX()][spot.getY()].equals(spot)) {
            throw new IllegalArgumentException("Spot is not on the grid!");
        }
    }

    /**
     * Error checker method for other methods in this class. It just checks whether specific coordinates are on the grid
     * and throws an error if not.
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    private void checkParameters(int x, int y) {
        if (!isOnGrid(x, y)) {
            throw new IllegalArgumentException("tile coordinates are out of grid: x=" + x + " & y=" + y);
        }
    }

    /**
     * Error checker method for other methods in this class. It just checks whether specific tile is not null.
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
        int newX = direction.addX(spot.getX()); // get coordinates
        int newY = direction.addY(spot.getY()); // of free space
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
        foundation.forcePlacement(new Tile(tileType));
    }

}