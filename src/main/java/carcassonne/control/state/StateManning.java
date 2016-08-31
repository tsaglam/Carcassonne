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
     * @param mainGUI sets the MainGUI
     * @param rotationGUI sets the RotationGUI
     * @param placementGUI sets the PlacementGUI
     * @param scoreboard sets the Scoreboard
     */
    public StateManning(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#abortGame()
     */
    @Override
    public void abortGame() {
        changeState(StateGameOver.class);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#newGame()
     */
    @Override
    public void newGame(int playerCount) {
        GameMessage.showWarning("Abort the current game before starting a new one.");
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#placeMeeple()
     */
    @Override
    public void placeMeeple(GridDirection position) {
        if (placeAndShowMeeple(position)) {
            processGridPatterns();
            startNextTurn();
        }
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#placeTile()
     */
    @Override
    public void placeTile(int x, int y) {
        // do nothing.
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#skip()
     */
    @Override
    public void skip() {
        processGridPatterns();
        startNextTurn();
    }

    // places meeple on grid an shows meeple on the GUI, if its possible.
    private boolean placeAndShowMeeple(GridDirection position) {
        Tile tile = round.getCurrentTile();
        Player player = round.getActivePlayer();
        Boolean couldPlaceMeeple = false;
        if (player.placeMeepleAt(tile, position, grid)) {
            System.out.println("CAN PLACE MEEPLE!"); // TODO
            mainGUI.setMeeple(tile, position, player);
            couldPlaceMeeple = true;
            updateScores();
        } else {
            System.out.println("CAN'T PLACE MEEPLE!"); // TODO
            GameMessage.showWarning("You can't place meeple directly on an occupied Castle or Road!");
        }
        return couldPlaceMeeple;
    }

    // gives the players the points they earned.
    private void processGridPatterns() {
        Tile tile = round.getCurrentTile();
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
            processGridPatterns();
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
