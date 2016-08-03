package carcassonne.control;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;

/**
 * The specific state when a Meeple can be placed.
 * @author Timur Saglam
 */
public class StateManning extends ControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     */
    public StateManning(MainController controller) {
        super(controller);
    }

    /**
     * @see carcassonne.control.ControllerState#entry()
     */
    @Override
    protected void entry() {
        controller.getPlacementGUI().setTile(controller.getRound().getCurrentTile());
    }

    /**
     * @see carcassonne.control.ControllerState#exit()
     */
    @Override
    protected void exit() {
        controller.getPlacementGUI().disableFrame();
    }

    @Override
    public boolean placeMeeple(GridDirection position) {
        Player player = controller.getRound().getActivePlayer();
        if (player.hasUnusedMeeples()) {
            player.placeMeepleAt(controller.getRound().getCurrentTile(), position);
            controller.getMainGUI().set(null, 0, 0, position); // TODO (HIGH) get real information from method above
            controller.getPlacementGUI().disableFrame();
            controller.getRound().nextTurn();
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
