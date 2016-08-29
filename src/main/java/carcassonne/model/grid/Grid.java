package carcassonne.model.grid;

import java.util.LinkedList;
import java.util.List;

import carcassonne.model.tile.TerrainType;
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
    private Tile[][] tile;

    /**
     * Basic constructor
     * @param width is the grid width.
     * @param height is the grid height.
     * @param foundationType is the tile type of the first tile in the middle of the grid.
     */
    public Grid(int width, int height, TileType foundationType) {
        this.width = width;
        this.height = height;
        tile = new Tile[width][height];
        placeFoundation(foundationType);
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
            if (tile[x][y].isConnected(from, to) && from != to) { // if connected
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
        int centerX = Math.round((width - 1) / 2);
        int centerY = Math.round((height - 1) / 2);
        return tile[centerX][centerY];
    }

    /**
     * Getter for the grid height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method checks for changed patterns on the grid. As a basis it uses the coordinates of the
     * last placed tile.
     * @param x is the x coordinate of the last placed tile.
     * @param y is the y coordinate of the last placed tile.
     * @return the list of the changed patterns.
     */
    public List<GridPattern> getInfluencedPatterns(int x, int y) {
        checkParameters(x, y);
        if (isFree(x, y)) {
            throw new IllegalArgumentException("Can't check for patterns on an free grid space");
        }
        Tile placedTile = tile[x][y]; // get tile.
        return createPatternList(placedTile); // get patterns.
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
            return tile[newX][newY]; // return calculated neighbor if valid:
        }
        return null;  // return null if tile not placed or not on grid.
    }

    /**
     * Returns a specific neighbor of a tile if the neighbor exists.
     * @param ofTile is the tile.
     * @param direction is the direction where the neighbor should be
     * @return the neighbor, or null if it does not exist.
     */
    public Tile getNeighbour(Tile ofTile, GridDirection direction) {
        return getNeighbour(ofTile.getX(), ofTile.getY(), direction);
    }

    /**
     * Safe getter for tiles.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return the tile
     * @throws IllegalArgumentException if the requested tile is out of grid.
     */
    public Tile getTile(int x, int y) {
        checkParameters(x, y);
        return tile[x][y];
    }

    /**
     * Getter for the grid width.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Checks whether a specific tile of the grid is free.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if free
     */
    public boolean isFree(int x, int y) {
        checkParameters(x, y);
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
     * Checks whether a specific tile of the grid is occupied.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @return true if occupied
     */
    public boolean isOccupied(int x, int y) {
        checkParameters(x, y);
        return tile[x][y] != null;
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
        if (isPlaceable(x, y, tile, false)) {
            tile.setPosition(x, y);
            this.tile[x][y] = tile;
            return true; // tile was successfully placed.
        }
        return false; // tile can't be placed, spot is occupied.
    }

    private void addPatternIfMonastery(Tile startingTile, List<GridPattern> patternList) {
        TileType type = startingTile.getType();
        if (type == TileType.Monastery || type == TileType.MonasteryRoad) {
            patternList.add(new MonasteryGridPattern(startingTile, this));
        }
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

    // creates list of all patterns.
    private List<GridPattern> createPatternList(Tile startingTile) {
        List<GridPattern> results = new LinkedList<GridPattern>();
        TerrainType terrain;
        // first, check for castle and road patterns:
        for (GridDirection direction : GridDirection.directNeighbors()) {
            terrain = startingTile.getTerrain(direction); // get terrain type.
            if ((terrain != TerrainType.FIELDS)) { // exclude fields
                if (startingTile.isNotConnectedToTag(direction)) {
                    results.add(new CastleAndRoadPattern(startingTile, direction, terrain, this));
                }
            }
        }
        // then check for monastery patterns:
        addPatternIfMonastery(startingTile, results); // the tile itself
        for (Tile neighbour : getNeighbors(startingTile)) {
            addPatternIfMonastery(neighbour, results); // neighbors
        }
        for (GridPattern pattern : results) {
            pattern.removeTileTags(); // IMPORTANT
        }
        System.out.println("Found patterns:"); // TODO (HIGHEST) remove debug output
        for (GridPattern pattern : results) {
            System.out.println(pattern); // TODO (HIGHEST) remove debug output
        }
        return results; // return all patterns.
    }

    /**
     * Forces to place a tile on a spot on the grid.
     * @param x is the x coordinate
     * @param y is the y coordinate
     * @param tile is the tile to place
     * @return true if it was successful, false if spot is occupied.
     */
    private boolean forcePlacement(int x, int y, Tile tile) {
        checkParameters(x, y);
        checkParameters(tile);
        if (isPlaceable(x, y, tile, true)) {
            this.tile[x][y] = tile;
            return true; // tile was successfully placed.
        }

        return false; // tile can't be placed, spot is occupied.
    }

    private boolean isClosingFreeSpaceOff(int x, int y, GridDirection direction) {
        int newX = GridDirection.addX(x, direction); // get coordinates
        int newY = GridDirection.addY(y, direction); // of free space
        if (isOnGrid(newX, newY)) {
            return getDirectNeighbors(newX, newY).size() > 2;
        }
        return false;
    }

    /**
     * Checks whether a tile is placeable on a specific position on the grid. First the parameters
     * are checked. Then the method checks whether the terrain on every side of the tile fits to the
     * terrain of the neighboring tile.
     * @param x is the x position on the grid.
     * @param y is the y position on the grid.
     * @param tile is the tile to place.
     * @param freePlacement is the boolean that decides whether a tile has to connect to another
     * tile.
     * @return true if the tile can be placed on the grid position specified through the
     * coordinates.
     */
    private boolean isPlaceable(int x, int y, Tile tile, boolean freePlacement) {
        checkParameters(x, y); // check coordinates.
        checkParameters(tile); // check tile.
        if (isOccupied(x, y)) {
            return false; // can't be placed if spot is occupied.
        }
        int neighborCount = 0;
        Tile neighbor;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for every direction
            neighbor = getNeighbour(x, y, direction);
            if (neighbor == null) { // free space
                if (isClosingFreeSpaceOff(x, y, direction)) {
                    return false; // you can't close of free spaces
                }
            } else { // if there is a neighbor in the direction.
                neighborCount++;
                if (!tile.hasSameTerrain(direction, neighbor)) {
                    return false; // if it does not fit to terrain, it can't be placed.
                }
            }
        }
        return neighborCount > 0 || freePlacement; // can be placed beneath another tile.
    }

    /**
     * Places a specific tile in the middle of the grid.
     * @param tileType is the type of that specific tile.
     */
    private void placeFoundation(TileType tileType) {
        int centerX = Math.round((width - 1) / 2);
        int centerY = Math.round((height - 1) / 2);
        Tile foundation = TileFactory.create(tileType);
        foundation.setPosition(centerX, centerY);
        forcePlacement(centerX, centerY, foundation);
    }

}