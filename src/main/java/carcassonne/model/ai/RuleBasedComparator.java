package carcassonne.model.ai;

import java.util.Comparator;

import carcassonne.model.grid.GridSpot;
import carcassonne.util.MinkowskiDistance;

/**
 * Comparator to compare moves with an equal value based on simple heuristic rules.
 * @author Timur Saglam
 */
public class RuleBasedComparator implements Comparator<AbstractCarcassonneMove> {
    private static final int ROUNDING_FACTOR = 100;
    private final GridSpot center;

    private final MinkowskiDistance distanceMeasure;

    /**
     * Creates a rule-based comparator for moves.
     * @param center is the grid spot in the center of the grid, where the foundation tile is placed.
     */
    public RuleBasedComparator(GridSpot center, MinkowskiDistance distanceMeasure) {
        this.center = center;
        this.distanceMeasure = distanceMeasure;
    }

    @Override
    public int compare(AbstractCarcassonneMove firstMove, AbstractCarcassonneMove secondMove) {
        if (firstMove.getGainedMeeples() != secondMove.getGainedMeeples()) {
            // Rule 1: Prefer move with a maximal meeple gain
            return firstMove.getGainedMeeples() - secondMove.getGainedMeeples();
        } else if (firstMove.involvesMeeplePlacement() != secondMove.involvesMeeplePlacement()) {
            // Rule 2: Prefer move without meeple placement
            return preferFalse(firstMove.involvesMeeplePlacement(), secondMove.involvesMeeplePlacement());
        }
        // Rule 3: Choose in the order of castle > monastery > road > fields
        int moveTypeDifference = compareMoveType(firstMove) - compareMoveType(secondMove);
        if (moveTypeDifference != 0) {
            return moveTypeDifference;
        }
        // Rule 4: Finally, choose closest move to the center of the grid
        return (int) (ROUNDING_FACTOR * (distanceToCenter(secondMove) - distanceToCenter(firstMove)));

    }

    private int compareMoveType(AbstractCarcassonneMove move) {
        if (move.involvesMeeplePlacement()) {
            switch (move.getMeepleType()) {
            case CASTLE:
                return 3;
            case MONASTERY:
                return 2;
            case ROAD:
                return 1;
            default:
            }
        }
        return 0; // fields or no meeple placed
    }

    private double distanceToCenter(AbstractCarcassonneMove move) {
        return distanceMeasure.distance(center.getX(), center.getY(), move.getX(), move.getY());
    }

    private int preferFalse(Boolean first, Boolean second) {
        return second.compareTo(first);
    }

}
