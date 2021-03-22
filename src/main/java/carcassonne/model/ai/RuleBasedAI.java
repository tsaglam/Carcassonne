package carcassonne.model.ai;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        possibleMoves.forEach(System.err::println); // TODO (HIGH remove debug output)
        List<CarcassonneMove> consideredMoves = possibleMoves.stream().filter(it -> it.getValue() >= 0).collect(toList());
        if (consideredMoves.size() == 0) {
            currentMove = Optional.empty();
        } else {
            double maximumValue = consideredMoves.stream().mapToDouble(it -> it.getValue()).max().getAsDouble();
            // TODO (HIGH) Count retrieved meeples in move
            // TODO (HIGH) Maybe include using and retrieving meeples in score
            // TODO (HIGH) Think about where to consider something like: don't place field when meeples <= 1
            // TODO (HIGH) If there are multiple best options pick randomly (after monastery > castle > field > road)
            currentMove = consideredMoves.stream().filter(it -> it.getValue() == maximumValue).findAny();
        }
        System.out.println("Best: " + currentMove); // TODO (HIGH remove debug output)
        return currentMove;
    }

    @Override
    public Tile chooseTileToDrop(Collection<Tile> tiles) { // TODO (HIGH) find better heuristic:
        Optional<Tile> optionalTile = tiles.stream().skip(new Random().nextInt(tiles.size())).findFirst();
        return optionalTile.orElseThrow(() -> new IllegalArgumentException("Cannot choose from empty collection!"));
    }

}
