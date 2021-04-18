package carcassonne.model.ai;

import java.util.Collection;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player which is valued according to the immediate value of the single move when
 * modeling the Carcassonne move as a zero-sum game.
 * @author Timur Saglam
 */
public class ZeroSumMove extends AbstractCarcassonneMove {

    /**
     * Creates the move. Does not check if the move is legal.
     * @param tile is the tile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param meeplePosition is the position on which the meeple is placed on the tile.
     * @param actingPlayer is the player that is executing the move.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile tile, GridDirection meeplePosition, Player actingPlayer, GameSettings settings) {
        super(tile, meeplePosition, actingPlayer, settings);
    }

    /**
     * Creates the move without a meeple placement. Does not check if the move is legal.
     * @param tile is the tile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param actingPlayer is the player that is executing the move.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile tile, Player actingPlayer, GameSettings settings) {
        this(tile, null, actingPlayer, settings);
    }

    @Override
    protected double calculateValue() {
        gridSpot.removeTile();
        Collection<GridPattern> localPatterns = gridSpot.getGrid().getLocalPatterns(gridSpot);
        double scoreBefore = localPatterns.stream().mapToInt(this::zeroSumScore).sum();
        gainedMeeples = calculateEmployedMeeples(localPatterns);
        gridSpot.place(tile);
        if (involvesMeeplePlacement()) {
            tile.placeMeeple(actingPlayer, meeplePosition, new TemporaryMeeple(actingPlayer), settings);
        }
        localPatterns = gridSpot.getGrid().getLocalPatterns(gridSpot);
        double scoreAfter = localPatterns.stream().mapToInt(this::zeroSumScore).sum();
        gainedMeeples -= calculateEmployedMeeples(localPatterns);
        tile.removeMeeple();
        return scoreAfter - scoreBefore;
    }

    private int zeroSumScore(GridPattern pattern) {
        int score = pattern.getScoreFor(actingPlayer); // acting players gain
        for (Player dominantPlayer : pattern.getDominantPlayers()) {
            if (dominantPlayer != actingPlayer) {
                score -= pattern.getScoreFor(dominantPlayer); // other players gain = acting players loss
            }
        }
        return score;
    }
}
