package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;

/**
 * Is the abstract state of the state machine.
 * @author Timur Saglam
 */
public abstract class ControllerState {

	protected MainController controller;
	protected MainGUI mainGUI;
	protected RotationGUI rotationGUI;
	protected PlacementGUI placementGUI;
	protected Round round;
	protected Grid grid;

	/**
	 * Constructor of the abstract state, sets the class variables from the
	 * parameters and calls the <code>entry()</code> method.
	 */
	public ControllerState(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Round round,
			Grid grid) {
		this.controller = controller;
		this.mainGUI = mainGUI;
		this.rotationGUI = rotationGUI;
		this.placementGUI = placementGUI;
		this.round = round;
		this.grid = grid;
		entry();
	}

	protected abstract void entry();

	protected abstract void exit();

	/**
	 * Starts new round with a specific amount of players.
	 * @param playerCount sets the amount of players.
	 * @return true if a new game was started.
	 */
	public boolean newGame(int playerCount) {
		System.err.println("You can't start a new game right now.");
		return false;
	}

	/**
	 * Method for the view to call if a user places a tile.
	 * @param x is the x coordinate.
	 * @param y is the y coordinate.
	 * @return true if tile was placed.
	 */
	public boolean placeTile(int x, int y) {
		System.err.println("You can't place a tile right now.");
		return false;
	}

	/**
	 * Method for the view to call if the user wants to skip a round.
	 * @return true if turn was skipped.
	 */
	public boolean skip() {
		System.err.println("You can't place a tile right now.");
		return false;
	}

	/**
	 * Method for the view to call if a user mans a tile with a Meeple.
	 * @return true if Meeple was placed.
	 */
	public boolean placeMeeple(GridDirection position) {
		System.err.println("You can't place meeple tile right now.");
		return false;
	}
}