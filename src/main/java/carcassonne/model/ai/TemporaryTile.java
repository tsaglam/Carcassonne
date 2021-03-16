package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * Tile that is only temporarily placed to analyze possible moves.
 * @author Timur Saglam
 */
public class TemporaryTile extends Tile {

    public TemporaryTile(TileType type) {
        super(type);
    }

    public TemporaryTile(TileType type, TileRotation rotation) {
        this(type);
        while (getRotation() != rotation) {
            rotateRight();
        }
    }

    @Override
    public void placeMeeple(Player player, GridDirection position, GameSettings settings) {
        super.placeMeeple(player, position, new TemporaryMeeple(player), settings);
    }

    @Override
    public void setPosition(GridSpot spot) {
        gridSpot = spot; // no null check, allows removing spot
    }

    @Override
    public void removeMeeple() {
        meeple = null;
    }

}
