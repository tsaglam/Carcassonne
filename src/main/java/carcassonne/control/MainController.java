package carcassonne.control;

import java.util.HashMap;
import java.util.Map;

import carcassonne.control.state.AbstractControllerState;
import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.control.state.StateManning;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.settings.GameSettings;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The MainController is the central class of the game. The game is started with the instantiation of this class. The
 * class gets the user input from the <code>MouseAdapter</code> in the <code>view package</code>, and controls both the
 * <code>view</code> and the <code>model</code>. The <code>controller</code> package also contains the state machine,
 * which consists out of the <code>MainController</code> class and the state classes. This system implements the
 * model/view/controller architecture, which is not 100% formally implemented. The reason for this is that in the user
 * input is made in Swing through the <code>MouseAdapters</code>, which belong to the <code>view</code> package.
 * @author Timur Saglam
 */
public class MainController {
    private final MainGUI mainGUI;
    private final RotationGUI rotationGUI;
    private final PlacementGUI placementGUI;
    private final Map<Class<? extends AbstractControllerState>, AbstractControllerState> stateMap;
    private AbstractControllerState currentState;
    private final GameSettings settings;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        settings = new GameSettings();

        mainGUI = new MainGUI(this);
        rotationGUI = new RotationGUI(this, mainGUI);
        placementGUI = new PlacementGUI(this, mainGUI);
        GlobalKeyBindingManager keyBindings = new GlobalKeyBindingManager(this, mainGUI, rotationGUI);
        mainGUI.addKeyBindings(keyBindings);
        rotationGUI.addKeyBindings(keyBindings);
        placementGUI.addKeyBindings(keyBindings);
        settings.registerNotifiable(mainGUI.getScoreboard());
        settings.registerNotifiable(mainGUI);
        settings.registerNotifiable(placementGUI);
        settings.registerNotifiable(rotationGUI);
        stateMap = new HashMap<>();
        currentState = new StateIdle(this, mainGUI, rotationGUI, placementGUI);
        registerState(currentState);
        registerState(new StateManning(this, mainGUI, rotationGUI, placementGUI));
        registerState(new StatePlacing(this, mainGUI, rotationGUI, placementGUI));
        registerState(new StateGameOver(this, mainGUI, rotationGUI, placementGUI));
    }

    /**
     * Shows the main GUI.
     */
    public void startGame() {
        mainGUI.showUI();
    }

    /**
     * Changes the state of the controller to a new state.
     * @param stateType specifies which state is the new state.
     * @return the new state.
     */
    public AbstractControllerState changeState(Class<? extends AbstractControllerState> stateType) {
        currentState = stateMap.get(stateType);
        if (currentState == null) {
            throw new IllegalStateException("State is not registered: " + stateType);
        }
        return currentState;
    }

    /**
     * Requests to abort the round.
     */
    public void requestAbortGame() {
        currentState.abortGame();
    }

    /**
     * Method for the view to see whether a meeple is placeable on a specific tile.
     * @param position is the specific position on the tile.
     * @return true if a meeple can be placed on the position on the current tile.
     */
    public boolean requestPlacementStatus(GridDirection position) {
        return currentState.isPlaceable(position);
    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     * @param position is the position the user wants to place on.
     */
    public void requestMeeplePlacement(GridDirection position) {
        currentState.placeMeeple(position);
    }

    /**
     * Requests to start a new round with a specific amount of players.
     */
    public void requestNewRound() {
        currentState.newRound(settings.getAmountOfPlayers());
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     */
    public void requestSkip() {
        currentState.skip();
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void requestTilePlacement(int x, int y) {
        currentState.placeTile(x, y);
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     * @param newRound sets the new round.
     * @param newGrid sets the new grid.
     */
    public void updateStates(Round newRound, Grid newGrid) {
        mainGUI.getScoreboard().rebuild(newRound.getPlayerCount());
        for (AbstractControllerState state : stateMap.values()) {
            state.updateState(newRound, newGrid);
        }
    }

    /**
     * Getter for the {@link GameSettings}, which grants access to the games settings.
     * @return the {@link GameSettings} instance.
     */
    public GameSettings getSettings() {
        return settings;
    }

    /**
     * Registers a specific state at the controller.
     * @param state is the specific state.
     */
    private void registerState(AbstractControllerState state) {
        if (stateMap.put(state.getClass(), state) != null) {
            throw new IllegalArgumentException("Can't register two states of a kind.");
        }
    }
}
