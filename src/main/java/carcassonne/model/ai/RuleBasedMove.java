package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player which is valued mostly according to the immediate value that moves brings in
 * addition to some very simple rules.
 * @author Timur Saglam
 */
public class RuleBasedMove implements CarcassonneMove {
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
    public RuleBasedMove(TemporaryTile tile, GridDirection position, Player player, GridSpot gridSpot, GameSettings settings) {
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
    public RuleBasedMove(TemporaryTile tile, Player player, GridSpot gridSpot, GameSettings settings) {
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
        return "Move for " + player.getName() + " with value " + value + ": " + tile.getType() + " " + tile.getTerrain(position) + " on " + position
                + " " + gridSpot;
    }

    private int calculateScoreSnapshot() {
        return gridSpot.getGrid().getLocalPatterns(gridSpot).stream().mapToInt(it -> it.getScoreFor(player)).sum();
    }

    private double calculateValue() {
        int scoreBeforeMove = calculateScoreSnapshot();
        if (!tile.isPlaced()) {
            gridSpot.place(tile);
        }
        tile.placeMeeple(player, position, new TemporaryMeeple(player), settings);
        int scoreAfterMove = calculateScoreSnapshot();
        tile.removeMeeple();
        // System.err.println("score: " + scoreBeforeMove + " --> " + scoreAfterMove);
        return scoreAfterMove - scoreBeforeMove;
    }
}
