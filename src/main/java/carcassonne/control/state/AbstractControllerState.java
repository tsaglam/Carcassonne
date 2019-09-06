package carcassonne.control.state;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.settings.GameSettings;
import carcassonne.settings.SystemProperties;
import carcassonne.view.main.MainGUI;
import carcassonne.view.menubar.Scoreboard;
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
    protected Scoreboard scoreboard;

    /**
     * Constructor of the abstract state, sets the controller from the parameter, registers the state at the controller and
     * calls the <code>entry()</code> method.
     * @param controller sets the Controller
     * @param mainGUI sets the MainGUI
     * @param rotationGUI sets the RotationGUI
     * @param placementGUI sets the PlacementGUI
     * @param scoreboard sets the Scoreboard
     */
    public AbstractControllerState(MainController controller, MainGUI mainGUI, RotationGUI rotationGUI, PlacementGUI placementGUI) {
        this.controller = controller;
        this.mainGUI = mainGUI;
        this.rotationGUI = rotationGUI;
        this.placementGUI = placementGUI;
        scoreboard = mainGUI.getScoreboard();
    }

    /**
     * Starts new round with a specific amount of players.
     */
    public abstract void abortGame();

    /**
     * Method for the view to see whether a meeple is placeable on a specific tile.
     * @param position is the specific position on the tile.
     * @return true if a meeple can be placed on the position on the current tile.
     */
    public abstract boolean isPlaceable(GridDirection position);

    /**
     * Starts new round with a specific amount of players.
     * @param playerCount sets the amount of players.
     */
    public abstract void newRound(int playerCount);

    /**
     * Method for the view to call if a user mans a tile with a Meeple.
     * @param position is the placement position.
     */
    public abstract void placeMeeple(GridDirection position);

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public abstract void placeTile(int x, int y);

    /**
     * Method for the view to call if the user wants to skip a round.
     */
    public abstract void skip();

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
     * Changes the state to a new state.
     * @param stateType is the type of the new state.
     */
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
     * Starts a new round for a specific number of players.
     * @param playerCount is the specific number of players.
     */
    protected void startNewRound(int playerCount) {
        SystemProperties systemProperties = new SystemProperties();
        int gridWidth = systemProperties.getResolutionWidth() / GameSettings.TILE_SIZE;
        int gridHeight = systemProperties.getResolutionHeight() / GameSettings.TILE_SIZE;
        Grid newGrid = new Grid(gridWidth, gridHeight);
        Round newRound = new Round(playerCount, newGrid, controller.getProperties());
        controller.updateStates(newRound, newGrid);
        updateScores();
        updateStackSize();
        GridSpot spot = round.getCurrentTile().getGridSpot(); // starting spot.
        mainGUI.setTile(spot.getTile(), spot.getX(), spot.getY());
        highlightSurroundings(spot);
        round.nextTurn(); // first tile is drawn, player one is active.
        mainGUI.setCurrentPlayer(round.getActivePlayer());
        changeState(StatePlacing.class);
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     */
    protected void updateScores() {
        Player player;
        for (int playerNumber = 0; playerNumber < round.getPlayerCount(); playerNumber++) {
            player = round.getPlayer(playerNumber);
            scoreboard.update(player);
        }
    }

    /**
     * Updates the label which displays the current stack size.
     */
    protected void updateStackSize() {
        scoreboard.updateStackSize(round.getStackSize());
    }

    /**
     * Highlights the surroundings of a {@link GridSpot} on the main UI.
     * @param spot is the {@link GridSpot} that determines where to highlight.
     */
    protected void highlightSurroundings(GridSpot spot) {
        for (GridSpot neighbor : grid.getNeighbors(spot, true, GridDirection.directNeighbors())) {
            if (neighbor != null && neighbor.isFree()) {
                mainGUI.setHighlight(neighbor.getX(), neighbor.getY());
            }
        }
    }
}
