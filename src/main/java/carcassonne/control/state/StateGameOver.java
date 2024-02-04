package carcassonne.control.state;

import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.settings.GameSettings;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.util.GameMessage;

/**
 * The specific state where the statistics are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends AbstractGameState {

    private static final String GAME_OVER_MESSAGE = "The game is over. Winning player(s): ";

    /**
     * Constructor of the state.
     * @param stateMachine is the state machine managing this state.
     * @param settings are the game settings.
     * @param views contains the user interfaces.
     * @param playerAI is the current AI strategy.
     */
    public StateGameOver(StateMachine stateMachine, GameSettings settings, ViewFacade views, ArtificialIntelligence playerAI) {
        super(stateMachine, settings, views, playerAI);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#abortGame()
     */
    @Override
    public void abortGame() {
        // Do nothing, round is already aborted.
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#newRound()
     */
    @Override
    public void newRound(int playerCount) {
        exit();
        changeState(StateIdle.class);
        startNewRound(playerCount);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeMeeple()
     */
    @Override
    public void placeMeeple(GridDirection position) {
        throw new IllegalStateException("Placing meeples in StateGameOver is not allowed.");
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#placeTile()
     */
    @Override
    public void placeTile(int x, int y) {
        // do nothing.
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#skip()
     */
    @Override
    public void skip() {
        views.onScoreboard(Scoreboard::disable);
        exit();
        changeState(StateIdle.class);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#entry()
     */
    @Override
    protected void entry() {
        System.out.println("FINAL PATTERNS:"); // TODO (LOW) [PRINT] remove debug output
        for (GridPattern pattern : grid.getAllPatterns()) {
            System.out.println(pattern); // TODO (LOW) [PRINT] remove debug output
            pattern.forceDisburse(settings.getSplitPatternScore());
        }
        updateScores();
        updateStackSize();
        views.onMainView(MainView::resetMenuState);
        GameMessage.showMessage(GAME_OVER_MESSAGE + round.winningPlayers());
        views.showGameStatistics(round);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#exit()
     */
    @Override
    protected void exit() {
        views.closeGameStatistics();
    }
}