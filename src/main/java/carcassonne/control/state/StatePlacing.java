package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The specific state when a Tile can be placed.
 * @author Timur Saglam
 */
public class StatePlacing extends AbstractControllerState {

    /**
     * Constructor of the state.
     * @param controller sets the controller.
     * @param mainGUI sets the MainGUI
     * @param rotationGUI sets the RotationGUI
     * @param placementGUI sets the PlacementGUI
     * @param scoreboard sets the Scoreboard
     */
    public StatePlacing(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI, Scoreboard scoreboard) {
        super(controller, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#abortGame()
     */
    @Override
    public void abortGame() {
        changeState(StateGameOver.class);
    }

    @Override
    public void placeTile(int x, int y) {
        Tile tile = rotationGUI.getTile();
        if (grid.place(x, y, tile)) {
            round.updateCurrentTile(tile);
            mainGUI.set(tile, x, y);
            changeState(StateManning.class);
        }
    }

    @Override
    public void skip() {
        round.nextTurn();
        entry();
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#entry()
     */
    @Override
    protected void entry() {
        if (round.isOver()) {
            changeState(StateGameOver.class);
        } else {
            rotationGUI.setTile(round.getCurrentTile(), round.getActivePlayer().getNumber());
        }
    }

    /**
     * @see carcassonne.control.state.AbstractControllerState#exit()
     */
    @Override
    protected void exit() {
        rotationGUI.disableFrame();
    }

}
