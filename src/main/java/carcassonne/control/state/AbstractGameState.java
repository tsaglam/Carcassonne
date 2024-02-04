package carcassonne.control.state;

import carcassonne.model.Player;
import carcassonne.model.Round;
import carcassonne.model.ai.ArtificialIntelligence;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;
import carcassonne.view.ViewFacade;
import carcassonne.view.main.MainView;

/**
 * Is the abstract state of the state machine.
 * @author Timur Saglam
 */
public abstract class AbstractGameState { // TODO (HIGH) [AI] separate human move states from AI moves?

    private final StateMachine stateMachine;
    protected GameSettings settings;
    protected ViewFacade views;
    protected Round round;
    protected TileStack tileStack;
    protected Grid grid;
    protected ArtificialIntelligence playerAI;
    protected static final String NO_MOVE = "No AI move is available!";

    /**
     * Constructor of the abstract state, sets the controller from the parameter, registers the state at the controller and
     * calls the <code>entry()</code> method.
     * @param stateMachine is the state machine managing this state.
     * @param settings are the game settings.
     * @param views contains the user interfaces.
     * @param playerAI is the current AI strategy.
     */
    protected AbstractGameState(StateMachine stateMachine, GameSettings settings, ViewFacade views, ArtificialIntelligence playerAI) {
        this.stateMachine = stateMachine;
        this.settings = settings;
        this.playerAI = playerAI;
        this.views = views;
    }

    /**
     * Starts new round with a specific amount of players.
     */
    public abstract void abortGame();

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
     * @param tileStack sets the tile stack.
     * @param grid sets the new grid.
     */
    public void updateState(Round round, TileStack tileStack, Grid grid) {
        this.round = round;
        this.grid = grid;
        this.tileStack = tileStack;
    }

    /**
     * Changes the state of the state machine to a new state.
     * @param stateType is the class of the target state.
     */
    protected void changeState(Class<? extends AbstractGameState> stateType) {
        stateMachine.changeState(stateType); // Encapsulated in a method, as concrete state do not know the state machine
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
        Grid newGrid = new Grid(settings.getGridWidth(), settings.getGridHeight(), settings.isAllowingEnclaves());
        TileStack tileStack = new TileStack(settings.getTileDistribution(), settings.getStackSizeMultiplier());
        Round newRound = new Round(playerCount, tileStack, newGrid, settings);
        stateMachine.updateStates(newRound, tileStack, newGrid);
        updateScores();
        updateStackSize();
        if (settings.isGridSizeChanged()) {
            settings.setGridSizeChanged(false);
            views.onMainView(MainView::rebuildGrid);
        }
        GridSpot spot = grid.getFoundation(); // starting spot.
        views.onMainView(it -> it.setTile(spot.getTile(), spot.getX(), spot.getY()));
        highlightSurroundings(spot);
        for (int i = 0; i < round.getPlayerCount(); i++) {
            Player player = round.getPlayer(i);
            while (!player.hasFullHand()) {
                player.addTile(tileStack.drawTile());
            }
        }
        views.onMainView(it -> it.setCurrentPlayer(round.getActivePlayer()));
        changeState(StatePlacing.class);
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     */
    protected void updateScores() {
        for (int playerNumber = 0; playerNumber < round.getPlayerCount(); playerNumber++) {
            Player player = round.getPlayer(playerNumber);
            views.onScoreboard(it -> it.update(player));
        }
    }

    /**
     * Returns the selected tile of the player. It does not matter if the player is a computer player or not.
     * @return the selected tile, either by a human player or the AI.
     */
    protected Tile getSelectedTile() {
        if (round.getActivePlayer().isComputerControlled()) {
            return playerAI.getCurrentMove().orElseThrow(() -> new IllegalStateException(NO_MOVE)).getOriginalTile();
        }
        return views.getSelectedTile();
    }

    /**
     * Updates the label which displays the current stack size.
     */
    protected void updateStackSize() {
        views.onScoreboard(it -> it.updateStackSize(tileStack.getSize()));
    }

    /**
     * Highlights the surroundings of a {@link GridSpot} on the main view.
     * @param spot is the {@link GridSpot} that determines where to highlight.
     */
    protected void highlightSurroundings(GridSpot spot) {
        for (GridSpot neighbor : grid.getNeighbors(spot, true, GridDirection.directNeighbors())) {
            if (neighbor != null && neighbor.isFree()) {
                views.onMainView(it -> it.setSelectionHighlight(neighbor.getX(), neighbor.getY()));
            }
        }
    }
}
