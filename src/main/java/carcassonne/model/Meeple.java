package carcassonne.model;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.TerrainType;

/**
 * Meeples are the token that a player places on the grid.
 * @author Timur Saglam
 */
public class Meeple {
    private GridSpot location;
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
    public GridSpot getLocation() {
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
     * Returns the type of the meeple, meaning on which terrain the meeple is placed on.
     * @return the terrain or OTHER if it is not placed.
     */
    public TerrainType getType() {
        if (isPlaced()) {
            return location.getTile().getTerrain(position);
        }
        return TerrainType.OTHER;
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
            owner.returnMeeple(this); // return me.
            location = null; // mark as unplaced.
        }
    }

    /**
     * Sets the placement location, which is the tile where the meeple is placed.
     * @param placementLocation is the placement location.
     */
    public void setLocation(GridSpot placementLocation) {
        this.location = placementLocation;
    }

    /**
     * Sets the placement position, which is the position on the tile where the meeple is placed.
     * @param placementPosition is the placement position.
     */
    public void setPosition(GridDirection placementPosition) {
        this.position = placementPosition;
    }

    @Override
    public String toString() {
        String placement = "";
        String type = "Unplaced ";
        if (isPlaced()) {
            type = location.getTile().getTerrain(position).toReadableString();
            placement = "placed on (" + location.getX() + "|" + location.getY() + ")" + " at " + position + " ";
        }
        return type + getClass().getSimpleName() + " by Player " + owner.getNumber() + " " + placement;
    }
}
