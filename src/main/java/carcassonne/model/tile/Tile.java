package carcassonne.model.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.Terrain;
import carcassonne.model.terrain.TerrainType;
import carcassonne.view.GameMessage;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile {
    private static final int IMAGES_PER_TILE = 4;
    private static final int CASTLE_TRESHOLD = 6;
    private static final String FOLDER = "src/main/ressources/tiles/"; // TODO (HIGH) move to options.
    private static final String FILE_TYPE = ".png"; // TODO (HIGH) move to options.
    private GridSpot gridSpot;
    private BufferedImage[] images; // tile image
    private Meeple meeple;
    private int rotation;
    private final Terrain terrain;
    private final TileType type;

    /**
     * Simple constructor.
     * @param terrain is the array containing the terrain information.
     * @param type is the tile type enum value of the tile.
     * @param tilePath is the path to the tiles.
     * @param fileType is the file type of the tiles.
     */
    public Tile(TileType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tile type cannot be null");
        }
        this.type = type;
        terrain = new Terrain(type);
        meeple = null;
        loadImages(FOLDER + type.name(), FILE_TYPE);
    }

    /**
     * Checks whether the tile has same terrain on a specific side to another tile.
     * @param direction is the specific direction.
     * @param other is the other tile.
     * @return true if it has same terrain.
     */
    public boolean canConnectTo(GridDirection direction, Tile other) {
        return getTerrain(direction) == other.getTerrain(direction.opposite());
    }

    /**
     * Getter for spot where the tile is placed
     * @return the grid spot, or null if it not placed yet.
     * @see isPlaced
     */
    public GridSpot getGridSpot() {
        return gridSpot;
    }

    /**
     * Getter for the tile image. the image depends on the rotation.
     * @return the image of the tile with the tile specific rotation.
     */
    public BufferedImage getImage() {
        return images[rotation];
    }

    /**
     * Getter for the tile image. the image depends on the rotation.
     * @return the image of the tile with the tile specific rotation.
     */
    public ImageIcon getIcon() {
        return new ImageIcon(images[rotation]);
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
        return terrain.at(direction);
    }

    /**
     * Getter for the tile type.
     * @return the type
     */
    public TileType getType() {
        return type;
    }

    /**
     * Checks whether the terrain of the tile connected from a specific grid direction to another specific grid direction.
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
            return meeple.getPosition() == position;
        }
        return false;
    }

    /**
     * Checks whether a meeple can be potentially placed on a specific position by its terrain.
     * @param direction is the specific position on the tile.
     * @return if it can be potentially placed. Does not check whether enemy players sit on the pattern.
     */
    public boolean hasMeepleSpot(GridDirection direction) {
        return terrain.getMeepleSpots().contains(direction);
    }

    /**
     * Determines whether this tile has an emblem. Only large castle tiles can have emblems.
     * @return true if it has an emblem, which doubles the points of this tile.
     */
    public boolean hasEmblem() {
        int castleSize = 0;
        for (GridDirection direction : GridDirection.values()) {
            if (terrain.at(direction).equals(TerrainType.CASTLE)) {
                castleSize++;
            }

        }
        return castleSize >= CASTLE_TRESHOLD;
    }

    /**
     * Checks of tile is a monastery tile, which means it has monastery terrain in the middle of the tile.
     * @return true if is a monastery.
     */
    public boolean isMonastery() {
        return getTerrain(GridDirection.MIDDLE) == TerrainType.MONASTERY;
    }

    /**
     * Checks if the tile is already placed.
     * @return true if it is placed.
     */
    public boolean isPlaced() {
        return gridSpot != null;
    }

    /**
     * Places a meeple on the tile, if the tile has not already one placed.
     * @param player is the player whose meeple is going to be set.
     * @param position is the position of the meeple on the tile.
     */
    public void placeMeeple(Player player, GridDirection position) {
        if (meeple == null) {
            meeple = player.getMeeple();
            meeple.setLocation(this);
            meeple.setPosition(position);
        } else {
            throw new IllegalArgumentException("Tile can not have already a meeple placed on it: " + toString());
        }
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
        return type + getClass().getSimpleName() + "[coordinates: " + gridSpot + ", rotation: " + rotation + ", terrain" + terrain + ", Meeple: "
                + meeple + "]";
    }

    // uses path to load images for all rotations.
    private void loadImages(String tilePath, String fileType) {
        images = new BufferedImage[4]; // create image array.
        for (int i = 0; i < IMAGES_PER_TILE; i++) { // for every image:
            File file = new File(tilePath + i + fileType);
            try {
                images[i] = ImageIO.read(file);
            } catch (IOException exception) {
                exception.printStackTrace();
                GameMessage.showError("ERROR: Could not load image loacted at " + tilePath + i + fileType);
            }
        }
    }

    /**
     * Getter for the meeple spots.
     * @return the positions on the grid where placing a meeple is possible.
     */
    protected List<GridDirection> getMeepleSpots() {
        return terrain.getMeepleSpots();
    }
}