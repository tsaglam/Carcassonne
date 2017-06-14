package carcassonne.model.tile;

import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;

import carcassonne.model.Meeple;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.Terrain;
import carcassonne.model.terrain.TerrainType;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile { // TODO (MEDIUM) build tile grid as graph.
    private static final String FILE_TYPE = ".jpg";
    private static final String FOLDER = "src/main/ressources/tiles/";
    private GridSpot gridSpot;
    private ImageIcon[] image; // tile image
    private Meeple meeple;
    private int rotation;
    private Terrain terrain;

    /**
     * Simple constructor.
     * @param terrain is the array containing the terrain information.
     */
    public Tile(Terrain terrain) {
        String path = FOLDER + getClass().getSimpleName();
        if (terrain == null || path == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else if (!new File(path + rotation + FILE_TYPE).exists()) {
            throw new IllegalArgumentException("Image path is not valid: " + path);
        }
        meeple = null;
        loadImages(path, FILE_TYPE);
    }

    /**
     * Checks whether the tile has same terrain on a specific side to another tile.
     * @param direction is the specific direction.
     * @param other is the other tile.
     * @return true if it has same terrain.
     */
    public boolean canConnectTo(GridDirection direction, Tile other) {
        return getTerrain(direction) == other.getTerrain(GridDirection.opposite(direction));
    }

    /**
     * Getter for spot where the tile is placed
     * @return the grid spot.
     */
    public GridSpot getGridSpot() {
        if (gridSpot == null) {
            throw new IllegalStateException("The position of the tile has not been set yet");
        }
        return gridSpot;
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
     * @return the meeple.
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
        return terrain.getAt(direction);
    }

    /**
     * Checks whether the terrain of the tile connected from a specific grid direction to another specific grid
     * direction.
     * @param from is a specific grid direction.
     * @param to is a specific grid direction.
     * @return true if the terrain connected.
     */
    public boolean hasConnection(GridDirection from, GridDirection to) {
        return terrain.isConnected(from, to);
    }

    /**
     * Checks whether the tile has a meeple.
     * @return true if it has a meeple
     */
    public boolean hasMeeple() {
        return meeple != null;
    }

    /**
     * Checks whether the tile has a meeple on a specific position.
     * @param position is the specific position.
     * @return true if there is a tile on that position.
     */
    public boolean hasMeepleAt(GridDirection position) {
        if (hasMeeple()) {
            return meeple.getPlacementPosition() == position;
        }
        return false;
    }

    /**
     * Checks of tile is a monastery tile, which means it has monastery terrain in the middle of the tile.
     * @return true if is a monastery.
     */
    public boolean isMonastery() {
        return getTerrain(GridDirection.MIDDLE) == TerrainType.MONASTERY;
    }

    /**
     * Removes and returns the meeple from the tile. Calls Meeple.removePlacement.
     */
    public void removeMeeple() {
        if (meeple == null) {
            throw new IllegalStateException("Meeple has already been removed.");
        }
        meeple.removePlacement();
        meeple = null;
    }

    /**
     * Turns a tile 90 degree to the left.
     */
    public void rotateLeft() {
        terrain.rotateLeft();
        rotation = rotation <= 0 ? 3 : rotation - 1; // update rotation indicator
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        terrain.rotateRight();
        rotation = rotation >= 3 ? 0 : rotation + 1; // update rotation indicator
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
     * @param spot is the {@link GridSpot} where the tile was placed.
     */
    public void setPosition(GridSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Position can't be null");
        }
        gridSpot = spot;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "Tile[coordinates: " + gridSpot + ", rotation: " + rotation + ", terrain" + terrain.toString()
                + ", Meeple: " + meeple + "]";
    }

    // uses path to load images for all rotations.
    private void loadImages(String tilePath, String fileType) {
        image = new ImageIcon[4]; // create image array.
        for (int i = 0; i <= 3; i++) { // for every image:
            image[i] = new ImageIcon(tilePath + i + fileType); // load from path.
        }
    }

    protected List<GridDirection> getMeepleSpots() {
        return terrain.getMeepleSpots();
    }
}