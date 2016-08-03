package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;

/**
 * The specific state where the stats are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends ControllerState {

	/**
	 * Constructor of the state.
	 * @param controller sets the controller.
	 * @param mainGUI sets the main GUI.
	 * @param rotationGUI sets the rotation GUI.
	 * @param placementGUI sets the placement GUI.
	 * @param round sets the round.
	 * @param grid sets the grid.
	 */
	public StateGameOver(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Round round,
			Grid grid) {
		super(controller, mainGUI, rotationGUI, placementGUI, round, grid);
	}

	/**
	 * @see carcassonne.control.ControllerState#entry()
	 */
	@Override
	protected void entry() {
		// TODO (MEDIUM) show stats in StateGameOver instead of jump to next state
		changeState(StateIdle.class);

	}

	/**
	 * @see carcassonne.control.ControllerState#exit()
	 */
	@Override
	protected void exit() {
		// TODO (MEDIUM) hide stats in StateGameOver

	}

}
