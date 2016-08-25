package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The specific state if no game is running.
 * @author Timur Saglam
 */
public class StateIdle extends AbstractControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the Controller
     * @param mainGUI sets the MainGUI
     * @param rotationGUI sets the RotationGUI
     * @param placementGUI sets the PlacementGUI
     * @param scoreboard sets the Scoreboard
     */
    public StateIdle(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#newGame()
     */
    @Override
    public void newGame(int playerCount) {
        Grid newGrid = new Grid(options.gridWidth, options.gridHeight, options.foundationType);
        Round newRound = new Round(playerCount, newGrid);
        controller.updateStates(newRound, newGrid);
        updateScores();
        mainGUI.set(round.getCurrentTile(), options.gridCenterX, options.gridCenterY);
        changeState(StateManning.class);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        mainGUI.rebuildGrids();
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
     */
    @Override
    protected void exit() {
        // No exit functions.
    }

}
