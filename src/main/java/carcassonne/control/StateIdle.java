package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;

/**
 * The specific state if no game is running.
 * @author Timur Saglam
 */
public class StateIdle extends ControllerState {

	/**
	 * @param controller
	 * @param mainGUI
	 * @param rotationGUI
	 * @param placementGUI
	 * @param round
	 * @param grid
	 */
	public StateIdle(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Round round, Grid grid) {
		super(controller, mainGUI, rotationGUI, placementGUI, round, grid);
	}

	/**
	 * @see carcassonne.control.ControllerState#entry()
	 */
	@Override
	protected void entry() {
		// No entry functions.
	}

	/**
	 * @see carcassonne.control.ControllerState#exit()
	 */
	@Override
	protected void exit() {
		// No exit functions.
	}
	
	/**
	 * @see carcassonne.control.ControllerState#newGame()
	 */
	@Override
	public boolean newGame(int playerCount) {
		grid = new Grid(options.gridWidth, options.gridHeight, options.foundationType);
		round = new Round(playerCount, grid);
		mainGUI.rebuildLabelGrid();
		mainGUI.set(round.getCurrentTile(), options.gridCenterX, options.gridCenterY);
		placementGUI.setTile(round.getCurrentTile());
		controller.changeState(StateIdle.class);
		return true;
	}

}
