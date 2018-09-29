package carcassonne.model;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * Meeples are the token that a player places on the grid.
 * @author Timur Saglam
 */
public class Meeple {
    private Tile location;
    private final Player owner;
    private GridDirection position;

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
    public Tile getLocation() {
        return location;
    }

    /**
     * Getter for the player that owns the meeple.
     * @return the owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Getter for the placement position.
     * @return the position on the tile where the meeple is placed.
     */
    public GridDirection getPosition() {
        return position;
    }

    /**
     * Indicates whether the meeple is placed or not.
     * @return true if placed.
     */
    public boolean isPlaced() {
        return location != null;
    }

    /**
     * Collects meeple from tile.
     */
    public void removePlacement() {
        if (location != null) {
            owner.returnMeeple(); // return me.
            location = null; // mark as unplaced.
        }
    }

    /**
     * Sets the placement location, which is the tile where the meeple is placed.
     * @param location is the placement location.
     */
    public void setLocation(Tile placementLocation) {
        this.location = placementLocation;
    }

    /**
     * Sets the placement position, which is the position on the tile where the meeple is placed.
     * @param position is the placement position.
     */
    public void setPosition(GridDirection placementPosition) {
        this.position = placementPosition;
    }

    @Override
    public String toString() {
        String locationString = "null";
        if (isPlaced()) {
            locationString = location.getGridSpot().toString();
        }
        return "Meeple[placed: " + isPlaced() + ", location: " + locationString + ", position: " + position + ", owner: " + owner + "]";
    }
}
