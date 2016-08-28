package carcassonne.model;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * Meeples are the token that a player places on the grid.
 * @author Timur Saglam
 */
public class Meeple {
    private final Player owner;
    private Tile placementLocation;
    private GridDirection placementPosition;

    /**
     * Basic constructor.
     * @param owner is the owner of the meeple.
     */
    public Meeple(Player owner) {
        this.owner = owner;
    }

    /**
     * Getter for the player that owns the meeple.
     * @return the owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Getter for the placement location.
     * @return the tile where the meeple is placed.
     */
    public Tile getPlacementLocation() {
        return placementLocation;
    }

    /**
     * Getter for the placement position.
     * @return the position on the tile where the meeple is placed.
     */
    public GridDirection getPlacementPosition() {
        return placementPosition;
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
     * @param position is the position of the meeple on the tile.
     */
    public void placeOn(Tile tile, GridDirection position) {
        if (tile == null) {
            throw new IllegalArgumentException("Job location cannot be null.");
        } else if (isPlaced()) {
            throw new IllegalStateException("Meeple (player " + owner + ") is already in use.");
        }
        placementLocation = tile;
        placementPosition = position;
        tile.setMeeple(this);
    }

    /**
     * Collects meeple from tile.
     */
    public void removePlacement() {
        owner.returnMeeple(this); // return me.
        placementLocation = null; // mark as unplaced.
    }

    @Override
    public String toString() {
        return "Meeple[placed: " + isPlaced() + ", location: (" + placementLocation.getX() + "|" + placementLocation.getY() + "), position: "
                + placementPosition + ", owner: " + owner.getNumber() + "]";
    }
}
