package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The specific state when a Meeple can be placed.
 * @author Timur Saglam
 */
public class StateManning extends AbstractControllerState {

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
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        placementGUI.setTile(round.getCurrentTile(), round.getActivePlayer().getNumber());
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
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
            for (GridPattern pattern : grid.getInfluencedPatterns(tile.getX(), tile.getY())) {
                System.out.println(pattern); // TODO (MEDIUM) remove pattern debug output:
                pattern.disburse();
                controller.updateScores(round.getScores());
            }
            placementGUI.disableFrame();
            round.nextTurn();
            changeState(StatePlacing.class);
            return true;
        }
        return false;
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#abortGame()
     */
    @Override
    public boolean abortGame() {
        changeState(StateGameOver.class);
        return true;
    }

}
