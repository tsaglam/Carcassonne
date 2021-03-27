package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;

/**
 * Represents a single move of a player, consisting of a placement of a tile and optinally a placement of a meeple on
 * that tile.
 * @author Timur Saglam
 */
public interface CarcassonneMove extends Comparable<CarcassonneMove> {

    /*
     * TODO (HIGH) always gets the same tile TODO (HIGH) endless meeples
     */

    /**
     * Getter for the tile placed in this move.
     * @return the tile with the correct rotation.
     */
    public Tile getTile();

    /**
     * Getter for the position of the meeple placement.
     * @return the position or null if no meeple is placed.
     */
    public GridDirection getPosition();

    /**
     * Getter for the potential value of the move.
     * @return the potential value.
     */
    public double getValue();

    /**
     * Getter for the difference in placed meeples.
     * @return how many more meeples where retrieved than placed.
     */
    public int getMeeplesGained();

    /**
     * Getter for the player making the move.
     * @return the player.
     */
    public Player getPlayer();

    /**
     * Determines if a meeple is placed as part of this move.
     * @return true if it does.
     */
    public boolean involvesMeeplePlacement();

    public TileRotation getRotation(); // TODO (HIGH) add comment

    public int getX(); // TODO (HIGH) add comment

    public int getY(); // TODO (HIGH) add comment

    @Override
    public default int compareTo(CarcassonneMove other) {
        return Double.valueOf(getValue()).compareTo(other.getValue());
    }

    /**
     * Checks whether this move involves placing a meeple on a field.
     * @return true if it is a fields move.
     */
    public default boolean isFieldMove() {
        return involvesMeeplePlacement() && getTile().getTerrain(getPosition()) == TerrainType.FIELDS;
    }

}