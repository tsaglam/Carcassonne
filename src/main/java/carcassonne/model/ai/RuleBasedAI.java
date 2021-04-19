package carcassonne.model.ai;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import carcassonne.model.Player;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;

public class RuleBasedAI implements ArtificialIntelligence {
    private static final double REQUIRED_FIELD_VALUE = 12;
    private static final int UPPER_BOUND = 75;
    private static final int LOWER_BOUND = 25;
    private static final double MEEPLE_VALUE_FACTOR = 0.5;
    private static final double LAST_MEEPLE_INCENTIVE = 2.5;
    private static final String EMPTY_COLLECTION = "Cannot choose random element from empty collection!";
    private final GameSettings settings;
    private Optional<AbstractCarcassonneMove> currentMove;

    public RuleBasedAI(GameSettings settings) {
        this.settings = settings;
    }

    @Override
    public Optional<AbstractCarcassonneMove> calculateBestMoveFor(Collection<Tile> tiles, Player player, Grid grid, TileStack stack) {
        currentMove = Optional.empty();
        Collection<AbstractCarcassonneMove> possibleMoves = new ArrayList<>();
        for (Tile tile : tiles) {
            possibleMoves.addAll(grid.getPossibleMoves(tile, player, settings));
        }
        // RULE 1: Only consider move with a positive value:
        List<AbstractCarcassonneMove> consideredMoves = possibleMoves.stream().filter(it -> it.getValue() >= 0).collect(toList());
        // RULE 2: Do not place last meeple on fields (except at the end):
        if (player.getFreeMeeples() == 1 && stack.getSize() > settings.getNumberOfPlayers()) {
            consideredMoves = consideredMoves.stream().filter(it -> !it.isFieldMove()).collect(toList());
        }
        // RULE 3: Avoid placing low value fields early in the game:
        consideredMoves = filterEarlyFieldMoves(consideredMoves, stack);
        // RULE 4: Find best move based on score value and meeple value
        if (!consideredMoves.isEmpty()) {
            double maximumValue = consideredMoves.stream().mapToDouble(it -> combinedValue(it, stack)).max().getAsDouble();
            Stream<AbstractCarcassonneMove> bestMoves = consideredMoves.stream().filter(it -> combinedValue(it, stack) == maximumValue);
            currentMove = chooseAmongBestMoves(bestMoves.collect(toList()), grid);
        }
        System.out.println(currentMove); // TODO (HIGH) [AI] remove debug output
        return currentMove;
    }

    @Override
    public Tile chooseTileToDrop(Collection<Tile> tiles) {
        return chooseRandom(tiles); // TODO (HIGH) [AI] find a meaningful heuristic
    }

    @Override
    public Optional<AbstractCarcassonneMove> getCurrentMove() {
        return currentMove;
    }

    private Optional<AbstractCarcassonneMove> chooseAmongBestMoves(List<AbstractCarcassonneMove> listOfMoves, Grid grid) {
        RuleBasedComparator comparator = new RuleBasedComparator(grid.getFoundation(), settings.getDistanceMeasure());
        AbstractCarcassonneMove maximum = Collections.max(listOfMoves, comparator);
        List<AbstractCarcassonneMove> bestMoves = listOfMoves.stream().filter(it -> comparator.compare(it, maximum) == 0).collect(toList());
        return Optional.of(chooseRandom(bestMoves));
    }

    private <T> T chooseRandom(Collection<T> elements) {
        Optional<T> randomElement = elements.stream().skip(new Random().nextInt(elements.size())).findFirst();
        return randomElement.orElseThrow(() -> new IllegalArgumentException(EMPTY_COLLECTION));
    }

    /**
     * Filters field moves if their value is too low. The required value decreases with a shrinking tile stack.
     */
    private List<AbstractCarcassonneMove> filterEarlyFieldMoves(Collection<AbstractCarcassonneMove> moves, TileStack stack) {
        double tiles = Math.max(LOWER_BOUND, Math.min(stack.getSize(), UPPER_BOUND));
        double requiredValue = REQUIRED_FIELD_VALUE * (tiles / 50 - 0.5);
        return moves.stream().filter(move -> !move.isFieldMove() || move.getValue() > requiredValue).collect(toList());
    }

    private double combinedValue(AbstractCarcassonneMove move, TileStack stack) {
        return move.getValue() + variableMeepleValue(move, stack);
    }

    /**
     * Calculates the value of the spend and retrieved meeples. Depends on the fill level of the tile stack.
     */
    private double variableMeepleValue(AbstractCarcassonneMove move, TileStack stack) {
        int meepleDifference = move.getGainedMeeples();
        int freeMeeples = move.getActingPlayer().getFreeMeeples();
        if (endIsNear(stack, move.getActingPlayer()) && meepleDifference < 0) {
            return 0;
        }
        double value = 0;
        if (freeMeeples == 0 && meepleDifference > 0 || freeMeeples == 1 && meepleDifference < 0) {
            value += LAST_MEEPLE_INCENTIVE;
        }
        for (int i = 0; i < Math.abs(meepleDifference); i++) {
            value += (GameSettings.MAXIMAL_MEEPLES - freeMeeples) * MEEPLE_VALUE_FACTOR;
        }
        return value * Math.signum(meepleDifference);
    }

    /**
     * The end is near if a player has equal or less moves left than meeples.
     */
    private boolean endIsNear(TileStack stack, Player player) {
        return stack.getSize() / (double) settings.getNumberOfPlayers() <= player.getFreeMeeples();
    }

}
