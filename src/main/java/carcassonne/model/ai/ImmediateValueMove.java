package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player whch is valued according to the immediate value that moves brings.
 * @author Timur Saglam
 */
public class ImmediateValueMove implements CarcassonneMove {
    private final GridSpot gridSpot;
    private final Player player;
    private final GridDirection position;
    private final GameSettings settings;
    private final TemporaryTile tile;
    private final double value;

    /**
     * Creates the move. Does not check if the move is legal.
     * @param tile is the tile placed in the move.
     * @param position is the position on which the meeple is placed on the tile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the tile is placed on.
     * @param settings are the game settings.
     */
    public ImmediateValueMove(TemporaryTile tile, GridDirection position, Player player, GridSpot gridSpot, GameSettings settings) {
        this.tile = tile;
        this.position = position;
        this.player = player;
        this.gridSpot = gridSpot;
        this.settings = settings;
        value = calculateValue();
    }

    /**
     * Creates the move without a meeple placement. Does not check if the move is legal.
     * @param tile is the tile placed in the move.
     * @param position is the position on which the meeple is placed on the tile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the tile is placed on.
     * @param settings are the game settings.
     */
    public ImmediateValueMove(TemporaryTile tile, Player player, GridSpot gridSpot, GameSettings settings) {
        this(tile, null, player, gridSpot, settings);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public GridDirection getPosition() {
        return position;
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Move for " + player.getName() + " with value " + value + ": " + tile.getType() + " " + position + " " + gridSpot;
    }

    private int calculateScoreSnapshot() {
        return gridSpot.getGrid().getLocalPatterns(gridSpot).stream().mapToInt(it -> it.getScoreFor(player)).sum();
    }

    private double calculateValue() {
        int scoreBeforeMove = calculateScoreSnapshot();
        gridSpot.place(tile);
        tile.placeMeeple(player, position, new TemporaryMeeple(player), settings);
        int scoreAfterMove = calculateScoreSnapshot();
        tile.removeMeeple();
        gridSpot.removeTile();
        return scoreAfterMove - scoreBeforeMove;
    }
}
