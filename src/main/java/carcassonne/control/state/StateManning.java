package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.GameMessage;
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
    public StateManning(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#abortGame()
     */
    @Override
    public boolean abortGame() {
        scoreboard.disable();
        changeState(StateGameOver.class);
        return true;
    }

    @Override
    public boolean placeMeeple(GridDirection position) {
        Tile tile = round.getCurrentTile();
        placeAndShowMeeple(tile, position);
        processGridPatterns(tile);
        startNextTurn();
        return true;
    }

    @Override
    public boolean skip() {
        startNextTurn();
        return true;
    }

    // places meeple on grid an shows meeple on the GUI.
    private void placeAndShowMeeple(Tile tile, GridDirection position) {
        Player player = round.getActivePlayer();
        player.placeMeepleAt(tile, position);
        mainGUI.setMeeple(tile, position, player);
    }

    // gives the players the points they earned.
    private void processGridPatterns(Tile tile) {
        for (GridPattern pattern : grid.getInfluencedPatterns(tile.getX(), tile.getY())) {
            if (pattern.isComplete()) {
                for (Meeple meeple : pattern.getMeepleList()) {
                    mainGUI.removeMeeple(meeple);
                }
                pattern.disburse();
                updateScores();
            }
        }
    }

    // starts the next turn and changes the state to state placing.
    private void startNextTurn() {
        round.nextTurn();
        changeState(StatePlacing.class);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        if (round.getActivePlayer().hasUnusedMeeples()) {
            placementGUI.setTile(round.getCurrentTile(), round.getActivePlayer().getNumber());
        } else {
            GameMessage.showMessage("You have no Meeples left. Regain meeples by Completion to place Meepels again. ");
            startNextTurn();
        }
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
     */
    @Override
    protected void exit() {
        placementGUI.disableFrame();
    }

}
