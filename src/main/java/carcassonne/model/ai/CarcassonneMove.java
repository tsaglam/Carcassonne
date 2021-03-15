package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * Represents a single move of a player, consisting of a placement of a tile and optinally a placement of a meeple on
 * that tile.
 * @author Timur Saglam
 */
public interface CarcassonneMove extends Comparable<CarcassonneMove> {

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
     * Getter for the player making the move.
     * @return the player.
     */
    public Player getPlayer();

    @Override
    public default int compareTo(CarcassonneMove other) {
        return Double.valueOf(getValue()).compareTo(other.getValue());
    }

}