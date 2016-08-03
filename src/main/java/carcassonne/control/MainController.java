package carcassonne.control;

import java.util.HashMap;

import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;

/**
 * TODO (MEDIUM) comment this class.
 * @author Timur Saglam
 */
public class MainController {

	private MainGUI mainGUI;
	private RotationGUI rotationGUI;
	private PlacementGUI placementGUI;
	private Round round;
	private Grid grid;
    private HashMap<Class<? extends ControllerState>, ControllerState> stateMap;
	private ControllerState currentState;

	/**
	 * Basic constructor. Creates the view and the model of the game.
	 */
	public MainController() {
		mainGUI = new MainGUI(this);
		rotationGUI = new RotationGUI(this);
		placementGUI = new PlacementGUI(this);
		stateMap = new HashMap<Class<? extends ControllerState>, ControllerState>();
		currentState = new StateIdle(this, mainGUI, rotationGUI, placementGUI, round, grid);
		new StateManning(this, mainGUI, rotationGUI, placementGUI, round, grid);
		new StatePlacing(this, mainGUI, rotationGUI, placementGUI, round, grid);
		new StateGameOver(this, mainGUI, rotationGUI, placementGUI, round, grid);
		requestNewGame(2); // TODO (HIGH) make GUI button for the start game function.
	}

	/**
	 * Changes the state of the controller to a new state.
	 * @param stateType specifies which state is the new state.
	 * @return the new state.
	 */
	public ControllerState changeState(Class<? extends ControllerState> stateType) {
		System.out.println("change to " + stateType + " from "+ currentState.getClass()); // TODO (HIGH)
		currentState = stateMap.get(stateType);
		if (currentState == null) {
			throw new IllegalStateException("State is not registered: " + stateType);
		}
		return currentState;
	}

	/**
	 * Registers a specific state at the controller.
	 * @param state is the specific state.
	 * @param stateType is the class type of the specific state.
	 */
	public void register(ControllerState state) {
		if (stateMap.put(state.getClass(), state) != null) {
			throw new IllegalArgumentException("Can't register two states of a kind.");
		}
	}

	/**
	 * Requests to start a new round with a specific amount of players.
	 * @param playerCount sets the amount of players.
	 * @return true if request was granted.
	 */
	public boolean requestNewGame(int playerCount) {
		return currentState.newGame(playerCount);
	}

	/**
	 * Requests to abort the round.
	 * @param playerCount sets the amount of players.
	 * @return true if request was granted.
	 */
	public boolean requestAbortGame() {
		return currentState.abortGame();
	}

	/**
	 * Method for the view to call if a user places a tile.
	 * @param x is the x coordinate.
	 * @param y is the y coordinate.
	 * @return true if request was granted.
	 */
	public boolean requestTilePlacement(int x, int y) {
		return currentState.placeTile(x, y);
	}

	/**
	 * Method for the view to call if the user wants to skip a round.
	 * @return true if request was granted.
	 */
	public boolean requestSkip() {
		return currentState.skip();
	}

	/**
	 * Method for the view to call if a user mans a tile with a meeple.
	 * @return true if request was granted.
	 */
	public boolean requestMeeplePlacement(GridDirection position) {
		return currentState.placeMeeple(position);
	}
}
