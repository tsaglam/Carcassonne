package carcassonne.control.state;

import carcassonne.control.MainController;
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
     */
    public StateIdle(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI) {
        super(controller, mainGUI, rotationGUI, placementGUI);
    }

    /**
     * @see carcassonne.control.state.ControllerState#entry()
     */
    @Override
    protected void entry() {
        mainGUI.rebuildLabelGrid();
    }

    /**
     * @see carcassonne.control.state.ControllerState#exit()
     */
    @Override
    protected void exit() {
        // No exit functions.
    }

    /**
     * @see carcassonne.control.state.ControllerState#newGame()
     */
    @Override
    public boolean newGame(int playerCount) {
        Grid newGrid = new Grid(options.gridWidth, options.gridHeight, options.foundationType);
        Round newRound = new Round(playerCount, newGrid);
        controller.updateStates(newRound, newGrid);
        mainGUI.set(round.getCurrentTile(), options.gridCenterX, options.gridCenterY);
        changeState(StateManning.class);
        return true;
    }

}
