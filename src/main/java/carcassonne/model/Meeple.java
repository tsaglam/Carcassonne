package carcassonne.model;

import carcassonne.model.tile.Tile;

/**
 * Meeples are the token that a player places on the grid.
 * @author Timur
 */
public class Meeple {
    private Player owner;
    private Tile placementLocation;

    /**
     * Basic constructor.
     * @param owner is the owner of the meeple.
     */
    public Meeple(Player owner) {
        this.owner = owner;
    }

    /**
     * Getter for the placement location.
     * @return the tile where the meeple is placed.
     */
    public Tile getJobLocation() {
        return placementLocation;
    }

    /**
     * Getter for the player that owns the meeple.
     * @return the owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Indicates whether the meeple is placed or not.
     * @return true if placed.
     */
    public boolean isPlaced() {
        return placementLocation != null;
    }

    /**
     * Places meeple on a specific tile. This method calls Tile.setMeeple().
     * @param tile is the specific tile.
     */
    public void placeOn(Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Job location cannot be null.");
        } else if (isPlaced()) {
            throw new IllegalStateException("Meeple (player " + owner + ") is already in use.");
        }
        placementLocation = tile;
        tile.setMeeple(this);
    }

    /**
     * Collects meeple from tile.
     */
    public void removePlacement() {
        placementLocation = null; // mark as unplaced.
        owner.returnMeeple(this); // return me.
    }

}
