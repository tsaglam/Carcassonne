package carcassonne.model.tile;

import java.util.Collection;
import java.util.EnumSet;

import javax.swing.ImageIcon;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.CastleAndRoadPattern;
import carcassonne.model.grid.FieldsPattern;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.RotationDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.terrain.TileTerrain;
import carcassonne.settings.GameSettings;
import carcassonne.util.ConcurrentTileImageScaler;

/**
 * The tile of a grid.
 * @author Timur Saglam
 */
public class Tile {
    private static final int CASTLE_THRESHOLD = 6; // size required for a castle to have an emblem
    protected GridSpot gridSpot;
    protected Meeple meeple;
    private final TileTerrain terrain;
    private final TileType type;
    private TileRotation rotation;
    private final int rotationLimit;

    /**
     * Simple constructor.
     * @param type is the specific {@link TileType} that defines the behavior and state of this tile. It contains the onl
     * hard coded information.
     */
    public Tile(TileType type) {
        if (type == null) {
            throw new IllegalArgumentException("Tile type cannot be null");
        }
        this.type = type;
        terrain = new TileTerrain(type);
        rotation = TileRotation.UP;
        meeple = null;
        rotationLimit = TileUtil.rotationLimitFor(type);
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
     * @return the grid spot, or null if it is not placed yet.
     * @see Tile#isPlaced()
     */
    public GridSpot getGridSpot() {
        return gridSpot;
    }

    /**
     * Getter for the tile image. The image depends on the orientation of the tile.
     * @return the image depicting the tile.
     */
    public ImageIcon getIcon() {
        return getScaledIcon(GameSettings.TILE_RESOLUTION, false);
    }

    /**
     * Getter for the scaled tile image. The image depends on the orientation of the tile.
     * @param edgeLength specifies the edge length of the image in pixels.
     * @return the image depicting the tile.
     */
    public ImageIcon getScaledIcon(int edgeLength) {
        return getScaledIcon(edgeLength, false);
    }

    /**
     * Getter for the scaled tile image with HiDPI support. The image depends on the orientation of the tile.
     * @param edgeLength specifies the edge length of the image in pixels.
     * @param fastScaling specifies whether a fast scaling algorithm should be used.
     * @return the image depicting the tile.
     */
    public ImageIcon getScaledIcon(int edgeLength, boolean fastScaling) {
        return new ImageIcon(ConcurrentTileImageScaler.getScaledMultiResolutionImage(this, edgeLength, fastScaling));
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

    public Collection<TileRotation> getPossibleRotations() {
        return EnumSet.allOf(TileRotation.class).stream().filter(it -> it.ordinal() < rotationLimit - 1).toList();
    }

    /**
     * Getter for the rotation.
     * @return the rotation.
     */
    public TileRotation getRotation() {
        return rotation;
    }

    /**
     * Getter for image index, which is the rotation ordinal but limited to the rotation limit (meaning the number of image
     * files for this tile).
     * @return the image index.
     */
    public int getImageIndex() {
        return rotation.ordinal() % rotationLimit;
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
    public final boolean hasEmblem() {
        int castleSize = 0;
        for (GridDirection direction : GridDirection.values()) {
            if (TerrainType.CASTLE.equals(terrain.at(direction))) {
                castleSize++;
            }
        }
        return castleSize >= CASTLE_THRESHOLD;
    }

    /**
     * Checks of tile is a monastery tile, which means it has monastery terrain in the middle of the tile.
     * @return true if is a monastery.
     */
    public boolean isMonastery() {
        return getTerrain(GridDirection.CENTER) == TerrainType.MONASTERY;
    }

    /**
     * Checks if the tile is already placed.
     * @return true if it is placed.
     */
    public boolean isPlaced() {
        return gridSpot != null;
    }

    /**
     * Checks whether a meeple of a specific player can be placed on a specific position on this tile.
     * @param position is the specific position on the tile.
     * @param player is the player in question.
     * @param settings are the game settings to determine if fortifying is allowed.
     * @return true if a meeple can be placed.
     */
    public boolean allowsPlacingMeeple(GridDirection position, Player player, GameSettings settings) {
        TerrainType terrain = getTerrain(position);
        boolean placeable = false;
        if (isPlaced()) { // placing meeples on tiles that are not placed is not possible
            if (terrain == TerrainType.MONASTERY) {
                placeable = true; // you can always place on a monastery
            } else if (terrain != TerrainType.OTHER) {
                GridPattern pattern;
                if (terrain == TerrainType.FIELDS) {
                    pattern = new FieldsPattern(getGridSpot(), position);
                } else { // castle or road:
                    pattern = new CastleAndRoadPattern(getGridSpot(), position, terrain);
                }
                if (pattern.isNotOccupied() || pattern.isOccupiedBy(player) && settings.isAllowingFortifying()) {
                    placeable = true; // can place meeple
                }
                pattern.removeTileTags();
            }
        }
        return placeable;
    }

    /**
     * Places a meeple on the tile, if the tile has not already one placed.
     * @param player is the player whose meeple is going to be set.
     * @param position is the position of the meeple on the tile.
     */
    public void placeMeeple(Player player, GridDirection position, GameSettings settings) {
        placeMeeple(player, position, player.getMeeple(), settings);
    }

    public void placeMeeple(Player player, GridDirection position, Meeple meeple, GameSettings settings) {
        if (this.meeple != null || !allowsPlacingMeeple(position, player, settings)) {
            throw new IllegalArgumentException("Tile can not have already a meeple placed on it: " + this);
        }
        this.meeple = meeple;
        meeple.setLocation(gridSpot);
        meeple.setPosition(position);
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
        rotation = rotation.rotate(RotationDirection.LEFT);
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateRight() {
        terrain.rotateRight(); // TODO (MEDIUM) [PERFORMANCE] can get fairly expensive when executed often.
        rotation = rotation.rotate(RotationDirection.RIGHT);
    }

    /**
     * Turns a tile 90 degree to the right.
     */
    public void rotateTo(TileRotation targetRotation) {
        while (rotation != targetRotation) {
            rotateRight();
        }
    }

    /**
     * Gives the tile the position where it has been placed.
     * @param spot is the {@link GridSpot} where the tile was placed.
     */
    public void setPosition(GridSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Position can't be null, tile cannot be removed.");
        }
        gridSpot = spot;
    }

    @Override
    public String toString() {
        return type + getClass().getSimpleName() + "[coordinates: " + gridSpot + ", Meeple: " + meeple + "]";
    }
}