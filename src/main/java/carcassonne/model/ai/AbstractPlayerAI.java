package carcassonne.model.ai;

import java.util.List;

/**
 * This is the abstract super class for the artificial intelligence classes. The artificial intelligence classes contain
 * the algorithm that play the game independently.
 * @author Timur Saglam
 */
public abstract class AbstractPlayerAI {

    /**
     * Basic constructor, should be called by sub classes.
     */
    public AbstractPlayerAI() {
    }

    public abstract CarcassonneMove getBestMove();

    public List<CarcassonneMove> getPossibleMoves() {
        // TODO (MEDIUM) implement method getPossibleMoves().
        return null;
    }

}
