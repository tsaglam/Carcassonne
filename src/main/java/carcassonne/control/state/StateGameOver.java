package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.view.ViewContainer;
import carcassonne.view.util.GameMessage;

/**
 * The specific state where the statistics are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends AbstractGameState {

    private static final String GAME_OVER_MESSAGE = "The game is over. Winning player(s): ";

    /**
     * Constructor of the state.
     * @param controller sets the ControllerFacade
     * @param mainGUI sets the MainGUI
     * @param previewGUI sets the PreviewGUI
     * @param placementGUI sets the PlacementGUI
     */
    public StateGameOver(MainController controller, ViewContainer views, ArtificialIntelligence playerAI) {
        super(controller, views, playerAI);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#abortGame()
     */
    @Override
    public void abortGame() {
        GameMessage.showWarning("You already aborted the current game. Close the game statistics to start a new game.");
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
        views.onScoreboard(it -> it.disable());
        exit();
        changeState(StateIdle.class);
    }

    /**
     * @see carcassonne.control.state.AbstractGameState#entry()
     */
    @Override
    protected void entry() {
        System.out.println("FINAL PATTERNS:"); // TODO (LOW) remove debug output
        for (GridPattern pattern : grid.getAllPatterns()) {
            System.out.println(pattern); // TODO (LOW) remove debug output
            pattern.forceDisburse();
        }
        updateScores();
        updateStackSize();
        views.onMainView(it -> it.resetMenuState());
        GameMessage.showMessage(GAME_OVER_MESSAGE + round.getWinningPlayers());
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