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
     * Constructor of the state.
     * @param controller sets the controller.
     * @param mainGUI sets the main GUI.
     * @param rotationGUI sets the rotation GUI.
     * @param placementGUI sets the placement GUI.
     * @param round sets the round.
     * @param grid sets the grid.
     */
    public StateIdle(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Round round, Grid grid) {
        super(controller, mainGUI, rotationGUI, placementGUI, round, grid);
    }

    /**
     * @see carcassonne.control.ControllerState#entry()
     */
    @Override
    protected void entry() {
        mainGUI.rebuildLabelGrid();
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
        mainGUI.set(round.getCurrentTile(), options.gridCenterX, options.gridCenterY);
        System.out.println(round);
        System.out.println(placementGUI);
        changeState(StateManning.class); // TODO (HIGHEST) inconsistent object (round objects)
        return true;
    }

}
