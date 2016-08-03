package carcassonne.control;

import carcassonne.model.Player;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.view.MainGUI;
import carcassonne.view.PlacementGUI;
import carcassonne.view.RotationGUI;

/**
 * The specific state when a Meeple can be placed.
 * @author Timur Saglam
 */
public class StateManning extends ControllerState {

	/**
	 * Constructor of the state.
	 * @param controller sets the controller.
	 * @param mainGUI sets the main GUI.
	 * @param rotationGUI sets the rotation GUI.
	 * @param placementGUI sets the placement GUI.
	 * @param round sets the round.
	 * @param grid sets the grid.
	 */
	public StateManning(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Round round,
			Grid grid) {
		super(controller, mainGUI, rotationGUI, placementGUI, round, grid);
	}

	/**
	 * @see carcassonne.control.ControllerState#entry()
	 */
	@Override
	protected void entry() {
		System.out.println(round);
		System.out.println(placementGUI);
		placementGUI.setTile(round.getCurrentTile());
	}

	/**
	 * @see carcassonne.control.ControllerState#exit()
	 */
	@Override
	protected void exit() {
		placementGUI.disableFrame();
	}

	@Override
	public boolean placeMeeple(GridDirection position) {
		Player player = round.getActivePlayer();
		if (player.hasUnusedMeeples()) {
			player.placeMeepleAt(round.getCurrentTile(), position);
			mainGUI.set(null, 0, 0, position); // TODO (HIGH) get real information from method above
			placementGUI.disableFrame();
			round.nextTurn();
			changeState(StatePlacing.class);
			return true;
		}
		return false;
	}

	/**
	 * @see carcassonne.control.ControllerState#abortGame()
	 */
	@Override
	public boolean abortGame() {
		changeState(StateGameOver.class);
		return true;
	}

}
