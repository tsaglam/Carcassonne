package carcassonne.control;

import carcassonne.model.Round;
import carcassonne.model.grid.Grid;

/**
 * The specific state if no game is running.
 * @author Timur Saglam
 */
public class StateIdle extends ControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     */
    public StateIdle(MainController controller) {
        super(controller);
    }

    /**
     * @see carcassonne.control.ControllerState#entry()
     */
    @Override
    protected void entry() {
        controller.getMainGUI().rebuildLabelGrid();
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
        controller.setGrid(new Grid(options.gridWidth, options.gridHeight, options.foundationType));
        controller.setRound(new Round(playerCount, controller.getGrid()));
        controller.getMainGUI().set(controller.getRound().getCurrentTile(), options.gridCenterX, options.gridCenterY);
        changeState(StateManning.class); // TODO (HIGHEST) inconsistent object (round objects)
        return true;
    }

}
