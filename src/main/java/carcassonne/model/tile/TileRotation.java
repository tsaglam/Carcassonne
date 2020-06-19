package carcassonne.model.tile;

import carcassonne.model.terrain.RotationDirection;

/**
 * Enumeration for the tile rotation.
 * @author Timur Saglam
 */
public enum TileRotation {
    UP, // rotate 0 degree
    TILTED_RIGHT, // rotate 90 degree clockwise
    UPSIDE_DOWN, // rotate 180 degree
    TILTED_LEFT; // rotate 90 degree counter-clockwise

    /**
     * Returns the next {@link TileRotation} relative from this one.
     * @param direction determines if it should rotate clockwise or counterclockwise.
     * @return the rotated {@link TileRotation}.
     */
    public TileRotation rotate(RotationDirection direction) {
        int newOrdinal = Math.floorMod(this.ordinal() + direction.getValue(), values().length);
        return values()[newOrdinal];
    }
}
