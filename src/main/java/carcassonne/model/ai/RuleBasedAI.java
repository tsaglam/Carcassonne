package carcassonne.model.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import carcassonne.model.Player;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;

public class RuleBasedAI implements ArtificialIntelligence {
    private final GameSettings settings;
    private Optional<CarcassonneMove> currentMove;

    public RuleBasedAI(GameSettings settings) {
        this.settings = settings;
    }

    @Override
    public Optional<CarcassonneMove> getCurrentMove() {
        return currentMove;
    }

    @Override
    public Optional<CarcassonneMove> calculateBestMoveFor(Collection<Tile> tiles, Player player, Grid grid) {
        Collection<CarcassonneMove> possibleMoves = new ArrayList<>();
        for (Tile tile : tiles) {
            possibleMoves.addAll(grid.getPossibleMoves(tile, player, settings));
        }
        Stream<CarcassonneMove> consideredMoves = possibleMoves.stream().filter(it -> it.getValue() >= 0);
        if (consideredMoves.count() == 0) {
            currentMove = Optional.empty();
        } else {
            double maximumValue = consideredMoves.mapToDouble(it -> it.getValue()).max().getAsDouble();
            // TODO (HIGH) Count retrieved meeples in move
            // TODO (HIGH) Maybe include using and retrieving meeples in score
            // TODO (HIGH) Think about where to consider something like: don't place field when meeples <= 1
            currentMove = consideredMoves.filter(it -> it.getValue() == maximumValue).findAny();
        }
        System.out.println("Best: " + currentMove); // TODO (HIGH remove debug output)
        return currentMove;
    }

}
