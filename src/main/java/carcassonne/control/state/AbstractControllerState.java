package carcassonne.control.state;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.GameMessage;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * Is the abstract state of the state machine.
 * @author Timur Saglam
 */
public abstract class AbstractControllerState {

    protected MainController controller;
    protected MainGUI mainGUI;
    protected RotationGUI rotationGUI;
    protected PlacementGUI placementGUI;
    protected Round round;
    protected Grid grid;
    protected GameOptions options;

    /**
     * Constructor of the abstract state, sets the controller from the parameter, registers the
     * state at the controller and calls the <code>entry()</code> method.
     * @param controller sets the controller.
     */
    public AbstractControllerState(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI) {
        this.controller = controller;
        this.mainGUI = mainGUI;
        this.rotationGUI = rotationGUI;
        this.placementGUI = placementGUI;
        options = GameOptions.getInstance();
        controller.registerState(this);
    }

    protected void changeState(Class<? extends AbstractControllerState> stateType) {
        exit();
        AbstractControllerState newState = controller.changeState(stateType);
        newState.entry();
    }

    /**
     * Entry method of the state.
     */
    protected abstract void entry();

    /**
     * Exit method of the state.
     */
    protected abstract void exit();

    /**
     * Updates the round and the grid object after a new round was started.
     * @param round sets the new round.
     * @param grid sets the new grid.
     */
    public void updateState(Round round, Grid grid) {
        this.round = round;
        this.grid = grid;
    }

    /**
     * Starts new round with a specific amount of players.
     * @param playerCount sets the amount of players.
     * @return true if a new game was started.
     */
    public boolean newGame(int playerCount) {
        GameMessage.showWarning("You can't start a new game right now.");
        return false;
    }

    /**
     * Starts new round with a specific amount of players.
     * @return true if the game was aborted.
     */
    public boolean abortGame() {
        GameMessage.showWarning("You can't abort a game right now.");
        return false;
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     * @return true if tile was placed.
     */
    public boolean placeTile(int x, int y) {
        GameMessage.showWarning("You can't place a tile right now.");
        return false;
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     * @return true if turn was skipped.
     */
    public boolean skip() {
        GameMessage.showWarning("You can't place a tile right now.");
        return false;
    }

    /**
     * Method for the view to call if a user mans a tile with a Meeple.
     * @return true if Meeple was placed.
     */
    public boolean placeMeeple(GridDirection position) {
        GameMessage.showWarning("You can't place meeple tile right now.");
        return false;
    }
}
