package carcassonne.control;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import carcassonne.control.state.AbstractGameState;
import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.control.state.StateManning;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.Round;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.ai.RuleBasedAI;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;
import carcassonne.view.secondary.MeepleView;
import carcassonne.view.secondary.TileView;
import carcassonne.view.util.GameMessage;

/**
 * Central class of the game, creates all requires components, receives user input from the user interface in the
 * <code>view package</code>, and controls both the <code>view</code> and the <code>model</code>.
 * @author Timur Saglam
 */
public class MainController implements ControllerFacade { // TODO (HIGH) [DESIGN] separate main controller from state machine?
    private MainView mainView;
    private Map<Class<? extends AbstractGameState>, AbstractGameState> stateMap;
    private AbstractGameState currentState;
    private final GameSettings settings;
    private GlobalKeyBindingManager keyBindings;
    private TileView tileView;
    private MeepleView meepleView;
    private boolean abortOnStateChange;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        settings = new GameSettings();
        createUserInterface();
        createStateMachine();
    }

    /**
     * Shows the main user interface.
     */
    public void startGame() {
        EventQueue.invokeLater(() -> mainView.showUI());
    }

    /**
     * Changes the state of the controller to a new state.
     * @param stateType specifies which state is the new state.
     * @return the new state.
     */
    public AbstractGameState changeState(Class<? extends AbstractGameState> stateType) {
        if (abortOnStateChange && stateType == StatePlacing.class) {
            abortOnStateChange = false;
            return changeState(StateGameOver.class);
        }
        currentState = stateMap.get(stateType);
        if (currentState == null) {
            throw new IllegalStateException("State is not registered: " + stateType);
        }
        return currentState;
    }

    /**
     * Requests to abort the round.
     */
    @Override
    public void requestAbortGame() {
        currentState.abortGame();
        abortOnStateChange = false;
    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     * @param position is the position the user wants to place on.
     */
    @Override
    public void requestMeeplePlacement(GridDirection position) {
        currentState.placeMeeple(position);
    }

    /**
     * Requests to start a new round with a specific amount of players.
     */
    @Override
    public void requestNewRound() {
        currentState.newRound(settings.getNumberOfPlayers());
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     */
    @Override
    public void requestSkip() {
        currentState.skip();
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    @Override
    public void requestTilePlacement(int x, int y) {
        currentState.placeTile(x, y);
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     * @param newRound sets the new round.
     * @param newGrid sets the new grid.
     */
    public void updateStates(Round newRound, TileStack tileStack, Grid newGrid) {
        mainView.getScoreboard().rebuild(newRound.getPlayerCount());
        for (AbstractGameState state : stateMap.values()) {
            state.updateState(newRound, tileStack, newGrid);
        }
    }

    /**
     * Getter for the global key binding manager.
     * @return the global key bindings.
     */
    @Override
    public GlobalKeyBindingManager getKeyBindings() {
        return keyBindings;
    }

    /**
     * Getter for the {@link GameSettings}, which grants access to the games settings.
     * @return the {@link GameSettings} instance.
     */
    @Override
    public GameSettings getSettings() {
        return settings;
    }

    /**
     * Signals that a abort request was scheduled. This request wait too long during AI vs. AI gameplay, thus this method
     * requests the state machine to abort on the next state change. This method should not be queued on the state machine
     * thread.
     */
    void requestAbortOnStateChange() {
        abortOnStateChange = true;
    }

    /**
     * Creates the state machine.
     */
    private void createStateMachine() {
        stateMap = new HashMap<>();
        ArtificialIntelligence playerAI = new RuleBasedAI(settings);
        ViewFacade views = new ViewFacade(mainView, tileView, meepleView);
        currentState = new StateIdle(this, views, playerAI);
        registerState(currentState);
        registerState(new StateManning(this, views, playerAI));
        registerState(new StatePlacing(this, views, playerAI));
        registerState(new StateGameOver(this, views, playerAI));
    }

    /**
     * Creates the views and waits on the completion of the creation.
     */
    private final void createUserInterface() {
        try {
            EventQueue.invokeAndWait(() -> {
                ControllerAdapter adapter = new ControllerAdapter(this);
                mainView = new MainView(adapter);
                tileView = new TileView(adapter, mainView);
                meepleView = new MeepleView(adapter, mainView);
                keyBindings = new GlobalKeyBindingManager(adapter, mainView, tileView);
                mainView.addKeyBindings(keyBindings);
                tileView.addKeyBindings(keyBindings);
                meepleView.addKeyBindings(keyBindings);
                settings.registerNotifiable(mainView.getScoreboard());
                settings.registerNotifiable(mainView);
                settings.registerNotifiable(meepleView);
                settings.registerNotifiable(tileView);
            });
        } catch (InvocationTargetException | InterruptedException exception) {
            GameMessage.showError("Could not create user interface: " + exception.getMessage());
        }
    }

    /**
     * Registers a specific state at the controller.
     * @param state is the specific state.
     */
    private void registerState(AbstractGameState state) {
        if (stateMap.put(state.getClass(), state) != null) {
            throw new IllegalArgumentException("Can't register two states of a kind.");
        }
    }
}
