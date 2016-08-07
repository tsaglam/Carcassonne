package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
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
     */
    public StateManning(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI) {
        super(controller, mainGUI, rotationGUI, placementGUI);
    }

    /**
     * @see carcassonne.control.state.ControllerState#entry()
     */
    @Override
    protected void entry() {
        placementGUI.setTile(round.getCurrentTile());
    }

    /**
     * @see carcassonne.control.state.ControllerState#exit()
     */
    @Override
    protected void exit() {
        placementGUI.disableFrame();
    }

    @Override
    public boolean placeMeeple(GridDirection position) {
        Player player = round.getActivePlayer();
        if (player.hasUnusedMeeples()) {
            Tile tile = round.getCurrentTile();
            player.placeMeepleAt(tile, position);
            mainGUI.setMeeple(tile, position, player);
            placementGUI.disableFrame();
            round.nextTurn();
            changeState(StatePlacing.class);
            return true;
        }
        return false;
    }

    /**
     * @see carcassonne.control.state.ControllerState#abortGame()
     */
    @Override
    public boolean abortGame() {
        changeState(StateGameOver.class);
        return true;
    }

}
