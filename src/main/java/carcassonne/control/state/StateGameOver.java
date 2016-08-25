package carcassonne.control.state;

import java.util.List;

import carcassonne.control.MainController;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.GameMessage;
import carcassonne.view.secondary.GameStatisticsGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The specific state where the stats are shown can be placed.
 * @author Timur Saglam
 */
public class StateGameOver extends AbstractControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     * @param mainGUI sets the main GUI.
     * @param rotationGUI sets the rotation GUI.
     * @param placementGUI sets the placement GUI.
     */
    public StateGameOver(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#skip()
     */
    @Override
    public boolean skip() {
        scoreboard.disable();
        changeState(StateIdle.class);
        return true;
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        List<String> winners = round.getWinningPlayers();
        GameMessage.showMessage("The game is over. Winning player(s): " + winners);
        new GameStatisticsGUI(controller, round);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
     */
    @Override
    protected void exit() {
        // No exit functions.
    }

}
