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

    @Override
    public default int compareTo(CarcassonneMove other) {
        return Double.valueOf(getValue()).compareTo(other.getValue());
    }

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
     * Getter for the position of the meeple placement.
     * @return the position or null if no meeple is placed.
     */
    public GridDirection getPosition();

    public TileRotation getRotation(); // TODO (HIGH) add comment

    /**
     * Getter for the terrain type where the meeple is placed.
     * @return the terrain type.
     */
    public TerrainType getTerrainType();

    /**
     * Getter for the tile placed in this move.
     * @return the tile with the correct rotation.
     */
    public Tile getTile();

    /**
     * Getter for the potential value of the move.
     * @return the potential value.
     */
    public double getValue();

    public int getX(); // TODO (HIGH) add comment

    public int getY(); // TODO (HIGH) add comment

    /**
     * Determines if a meeple is placed as part of this move.
     * @return true if it does.
     */
    public boolean involvesMeeplePlacement();

    /**
     * Checks whether this move involves placing a meeple on a field.
     * @return true if it is a fields move.
     */
    public boolean isFieldMove();

}