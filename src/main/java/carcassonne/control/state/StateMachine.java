package carcassonne.control.state;

import java.util.HashMap;
import java.util.Map;

import carcassonne.model.Round;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;
import carcassonne.view.ViewFacade;

/**
 * State machine for the Carcassonne game state.
 * @author Timur Saglam
 */
public class StateMachine {
    private boolean abortRequested;
    private AbstractGameState currentState;
    private final Map<Class<? extends AbstractGameState>, AbstractGameState> stateMap;
    private final ViewFacade views;

    /**
     * Creates a Carcassonne state machine.
     * @param views is the facade to all user interfaces.
     * @param playerAI is the employed AI for computer-controlled players.
     * @param settings are the game settings.
     */
    public StateMachine(ViewFacade views, ArtificialIntelligence playerAI, GameSettings settings) {
        this.views = views;
        stateMap = new HashMap<>();
        currentState = new StateIdle(this, settings, views, playerAI);
        registerState(currentState);
        registerState(new StateManning(this, settings, views, playerAI));
        registerState(new StatePlacing(this, settings, views, playerAI));
        registerState(new StateGameOver(this, settings, views, playerAI));
    }

    /**
     * Returns the the current game state.
     * @return that state.
     */
    public AbstractGameState getCurrentState() {
        return currentState;
    }

    /**
     * Schedules an asynchronous abort request, meaning the state machine aborts on the next state change. This method
     * should not be called on the state machine thread or executor service, as during AI vs. AI gameplay that request will
     * starve.
     * @param abortOnStateChange set true to schedule an abort request, set to false to cancel it.
     */
    public void setAbortRequested(boolean abortOnStateChange) {
        abortRequested = abortOnStateChange;
    }

    /**
     * Registers a specific state with the state machine.
     * @param state is that state.
     */
    private void registerState(AbstractGameState state) {
        if (stateMap.put(state.getClass(), state) != null) {
            throw new IllegalArgumentException("Can't register two states of a kind.");
        }
    }

    /**
     * Changes the state of the state machine.
     * @param stateType specifies the target state to change to.
     */
    /* package-private */ void changeState(Class<? extends AbstractGameState> stateType) {
        currentState.exit();
        if (abortRequested && stateType == StatePlacing.class) {
            abortRequested = false;
            changeState(StateGameOver.class);
        } else {
            currentState = stateMap.get(stateType); // set new state
            if (currentState == null) {
                throw new IllegalStateException("State is not registered: " + stateType);
            }
            currentState.entry();
        }
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     * @param newRound is the new round.
     * @param tileStack is the new (and full) stack.
     * @param newGrid is the new (and empty) grid.
     */
    /* package-private */ void updateStates(Round newRound, TileStack tileStack, Grid newGrid) {
        views.onScoreboard(it -> it.rebuild(newRound.getPlayerCount()));
        for (AbstractGameState state : stateMap.values()) {
            state.updateState(newRound, tileStack, newGrid);
        }
    }
}
