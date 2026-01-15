package carcassonne.control.state;

import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.GridDirection;
import carcassonne.settings.GameSettings;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.util.GameMessage;

/**
 * The specific state if no game is running.
 * @author Timur Saglam
 */
public class StateIdle extends AbstractGameState {

    /**
     * Constructor of the state.
     * @param stateMachine is the state machine managing this state.
     * @param settings are the game settings.
     * @param views contains the user interfaces.
     * @param playerAI is the current AI strategy.
     */
    public StateIdle(StateMachine stateMachine, GameSettings settings, ViewFacade views, ArtificialIntelligence playerAI) {
        super(stateMachine, settings, views, playerAI);
    }

    @Override
    public void abortGame() {
        views.reroute(() -> GameMessage.showMessage("There is currently no game running."));
    }

    @Override
    public void newRound(int playerCount) {
        startNewRound(playerCount);
    }

    @Override
    public void placeMeeple(GridDirection position) {
        throw new IllegalStateException("Placing meeples in StateIdle is not allowed.");
    }

    @Override
    public void placeTile(int x, int y) {
        // do nothing.
    }

    @Override
    public void skip() {
        // do nothing.
    }

    @Override
    protected void entry() {
        views.onMainView(MainView::resetGrid);
    }

    @Override
    protected void exit() {
        // No exit functions.
    }

}
