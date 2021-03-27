package carcassonne.model.ai;

import java.util.ArrayList;
import java.util.List;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.settings.GameSettings;

/**
 * Tile that is only temporarily placed to analyze possible moves.
 * @author Timur Saglam
 */
public class TemporaryTile extends Tile {

    final Tile original;

    public TemporaryTile(Tile original) {
        super(original.getType());
        this.original = original;
    }

    public TemporaryTile(Tile original, TileRotation rotation) {
        this(original);
        rotateTo(rotation);
    }

    /**
     * Returns the original tile from which this tile is copied from.
     * @return the original tile.
     */
    public Tile getOriginal() {
        return original;
    }

    @Override
    public void placeMeeple(Player player, GridDirection position, GameSettings settings) {
        super.placeMeeple(player, position, new TemporaryMeeple(player), settings);
    }

    @Override
    public void removeMeeple() {
        meeple = null;
    }

    @Override
    public void setPosition(GridSpot spot) {
        gridSpot = spot; // no null check, allows removing spot
    }

    public static List<TemporaryTile> possibleRotationsOf(Tile tile) {
        List<TemporaryTile> rotatedTiles = new ArrayList<>();
        for (TileRotation rotation : tile.getPossibleRotations()) {
            rotatedTiles.add(new TemporaryTile(tile, rotation));
        }
        return rotatedTiles;
    }

}
