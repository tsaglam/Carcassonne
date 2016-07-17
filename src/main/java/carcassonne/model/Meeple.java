package carcassonne.model;

import carcassonne.model.tile.Tile;

/**
 * Meeples are the token that a player places on the grid.
 * @author Timur
 */
public class Meeple {

    private int owner;
    private Tile placementLocation;

    /**
     * Basic constructor.
     * @param owner is the owner of the meeple.
     */
    public Meeple(int owner) {
        this.owner = owner;
        placementLocation = null;
    }

    /**
     * Places meeple on a specific tile.
     * @param tile is the specific tile.
     */
    public void placeOn(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Job location cannot be null.");
        } else if (isPlaced()) {
            throw new IllegalStateException("Meeple (player " + owner + ") is already in use.");
        }
        placementLocation = tile;
    }

    /**
     * Collects meeple from tile.
     */
    public void collect() {
        placementLocation = null;
    }

    /**
     * Indicates whether the meeple is placed or not.
     * @return true if placed.
     */
    public boolean isPlaced() {
        return placementLocation != null;
    }

    /**
     * Getter for the placement location.
     * @return the tile where the meeple is placed.
     */
    public Tile getJobLocation() {
        return placementLocation;
    }

    /**
     * Getter for the owner number of the meeple.
     * @return the owner number.
     */
    public int getOwner() {
        return owner;
    }

}
