package carcassonne.control;

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
     */
    public StateGameOver(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI) {
        super(controller, mainGUI, rotationGUI, placementGUI);
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
