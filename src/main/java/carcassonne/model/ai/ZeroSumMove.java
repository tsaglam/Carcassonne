package carcassonne.model.ai;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player which is valued mostly according to the immediate value that moves brings in
 * addition to some very simple rules.
 * @author Timur Saglam
 */
public class ZeroSumMove implements CarcassonneMove { // TODO (HIGH) separate move from zero sum move
    private final GridSpot gridSpot;
    private final Player player;
    private final GridDirection position;
    private final GameSettings settings;
    private final TemporaryTile temporaryTile;
    private final double value;

    /**
     * Creates the move. Does not check if the move is legal.
     * @param temporaryTile is the temporaryTile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param position is the position on which the meeple is placed on the temporaryTile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the temporaryTile is placed on.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile temporaryTile, GridDirection position, Player player, GameSettings settings) {
        this.temporaryTile = temporaryTile;
        this.position = position;
        this.player = player;
        this.gridSpot = temporaryTile.getGridSpot();
        this.settings = settings;
        value = calculateValue();
    }

    /**
     * Creates the move without a meeple placement. Does not check if the move is legal.
     * @param temporaryTile is the temporaryTile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param position is the position on which the meeple is placed on the temporaryTile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the temporaryTile is placed on.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile tile, Player player, GameSettings settings) {
        this(tile, null, player, settings);
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
        return temporaryTile.getOriginal();
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        String meeple = involvesMeeplePlacement() ? temporaryTile.getTerrain(position) + " on " + position : "without meeple";
        return "Move for " + player.getName() + " with value " + value + ": " + temporaryTile.getType() + " " + meeple + " " + gridSpot;
    }

    @Override
    public boolean involvesMeeplePlacement() {
        return position != null;
    }

    private int calculateScoreSnapshot() {
        return gridSpot.getGrid().getLocalPatterns(gridSpot).stream().mapToInt(this::zeroSumScore).sum();
    }

    private int zeroSumScore(GridPattern pattern) {
        int score = 2 * pattern.getScoreFor(player);
        for (Player dominantPlayer : pattern.getDominantPlayers()) {
            score -= pattern.getScoreFor(dominantPlayer);
        }
        return score;
    }

    private double calculateValue() {
        gridSpot.removeTile();
        int scoreBeforeMove = calculateScoreSnapshot();
        gridSpot.place(temporaryTile);
        if (involvesMeeplePlacement()) {
            temporaryTile.placeMeeple(player, position, new TemporaryMeeple(player), settings);
        }
        int scoreAfterMove = calculateScoreSnapshot();
        temporaryTile.removeMeeple();
        return scoreAfterMove - scoreBeforeMove;
    }

    @Override
    public int getX() {
        return gridSpot.getX();
    }

    @Override
    public int getY() {
        return gridSpot.getY();
    }

    @Override
    public TileRotation getRotation() {
        return temporaryTile.getRotation();
    }
}
