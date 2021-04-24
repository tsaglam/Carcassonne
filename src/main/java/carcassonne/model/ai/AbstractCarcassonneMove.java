package carcassonne.model.ai;

import java.util.Collection;
import java.util.stream.Stream;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player, consisting of a placement of a tile and optionally a placement of a meeple on
 * that tile.
 * @author Timur Saglam
 */
public abstract class AbstractCarcassonneMove implements Comparable<AbstractCarcassonneMove> {

    protected final Player actingPlayer;
    protected int gainedMeeples;
    protected final GridSpot gridSpot;
    protected final GridDirection meeplePosition;
    protected final GameSettings settings;
    protected final TemporaryTile tile;
    protected final double value;
    protected double fieldValue;

    /**
     * Creates the move. Does not check if the move is legal.
     * @param tile is the tile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param meeplePosition is the position on which the meeple is placed on the tile.
     * @param actingPlayer is the player that is executing the move.
     * @param settings are the game settings.
     */
    public AbstractCarcassonneMove(TemporaryTile tile, GridDirection meeplePosition, Player actingPlayer, GameSettings settings) {
        this.tile = tile;
        this.meeplePosition = meeplePosition;
        this.actingPlayer = actingPlayer;
        this.settings = settings;
        if (!tile.isPlaced()) {
            throw new IllegalStateException("Tile needs to be placed: " + tile);
        }
        fieldValue = Double.NaN; // field score not yet set
        gridSpot = tile.getGridSpot();
        value = calculateValue();
    }

    @Override
    public int compareTo(AbstractCarcassonneMove other) {
        return Double.valueOf(getValue()).compareTo(other.getValue());
    }

    /**
     * Getter for the actingPlayer making the move.
     * @return the actingPlayer.
     */
    public Player getActingPlayer() {
        return actingPlayer;
    }

    /**
     * Getter for the difference in placed meeples.
     * @return how many more meeples where retrieved than placed.
     */
    public int getGainedMeeples() {
        return gainedMeeples;
    }

    /**
     * Getter for the meeplePosition of the meeple placement.
     * @return the meeplePosition or null if no meeple is placed.
     */
    public GridDirection getMeeplePosition() {
        return meeplePosition;
    }

    /**
     * Getter for the terrain type where the meeple is placed.
     * @return the terrain type or null if no meeple is placed.
     */
    public TerrainType getMeepleType() {
        if (meeplePosition == null) {
            return null;
        }
        return tile.getTerrain(meeplePosition);
    }

    /**
     * Getter for the tile placed in this move.
     * @return the tile with the correct rotation.
     */
    public Tile getOriginalTile() {
        return tile.getOriginal();
    }

    /**
     * Specifies which rotation needs to be applied to the original tile in order to correctly place it.
     * @return the rotation rotation for this move.
     */
    public TileRotation getRequiredTileRotation() {
        return tile.getRotation();
    }

    /**
     * Getter for the combined value of the move for all grid patterns.
     * @return the combined value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Getter for the value of the move regarding field patterns.
     * @return the field value.
     */
    public double getFieldValue() {
        return fieldValue;
    }

    /**
     * Getter for the x-coordinate of the tile placement.
     * @return the x-coordinate on the grid.
     */
    public int getX() {
        return gridSpot.getX();
    }

    /**
     * Getter for the y-coordinate of the tile placement.
     * @return the y-coordinate on the grid.
     */
    public int getY() {
        return gridSpot.getY();
    }

    /**
     * Determines if a meeple is placed as part of this move.
     * @return true if it does.
     */
    public boolean involvesMeeplePlacement() {
        return meeplePosition != null;
    }

    /**
     * Checks whether this move involves placing a meeple on a field.
     * @return true if it is a fields move.
     */
    public boolean isFieldMove() {
        return involvesMeeplePlacement() && tile.getTerrain(meeplePosition) == TerrainType.FIELDS;
    }

    @Override
    public String toString() {
        String meeple = involvesMeeplePlacement() ? tile.getTerrain(meeplePosition) + " on " + meeplePosition : "without meeple";
        return getClass().getSimpleName() + " for " + actingPlayer.getName() + " with value " + value + " (field value: " + fieldValue + "): "
                + tile.getType() + " " + meeple + " " + gridSpot;
    }

    /**
     * Calculates how many meeples the acting player employs on a set of grid patterns.
     * @param patterns are the grid patterns to check.
     * @return the number of employed meeples.
     */
    protected int calculateEmployedMeeples(Collection<GridPattern> patterns) {
        Stream<Meeple> localMeeples = patterns.stream().flatMap(it -> it.getMeepleList().stream());
        return Math.toIntExact(localMeeples.filter(it -> it.getOwner() == actingPlayer).count());
    }

    /**
     * Calculates the value of the move as well as the pure field value.
     * @return the value of the move.
     * @see AbstractCarcassonneMove#getValue()
     * @see AbstractCarcassonneMove#getFieldValue()
     */
    protected abstract double calculateValue();

}