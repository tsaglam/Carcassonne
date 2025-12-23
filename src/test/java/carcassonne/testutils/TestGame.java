package carcassonne.testutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import carcassonne.control.state.AbstractGameState;
import carcassonne.control.state.StateExposingStateMachine;
import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.control.state.StateManning;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileStack;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * Simple helper for constructing a testable Carcassonne game environment. Provides access to the game states, settings,
 * and internal game model objects.
 */
public class TestGame {

    private final StateExposingStateMachine stateMachine;
    private final GameSettings settings;
    private final TestViewFacade views;

    /**
     * Creates a new test game with default settings and a mock view facade.
     */
    public TestGame(boolean printGridOnStateTransitions) {
        views = new TestViewFacade(false);
        settings = new GameSettings();
        stateMachine = new StateExposingStateMachine(views, settings, printGridOnStateTransitions);
    }

    /**
     * Starts a new round assuming the game is idle.
     * @param numberOfPlayers is the number of players.
     * @param gridWidth is the width of the grid.
     * @param gridHeight is the height of the grid.
     */
    public void newRound(int numberOfPlayers, int gridWidth, int gridHeight) {
        assumeTrue(state() instanceof StateIdle || state() instanceof StateGameOver);
        settings.setGridWidth(gridWidth);
        settings.setGridHeight(gridHeight);
        state().newRound(numberOfPlayers);
    }

    public void abort() {
        assertNotEquals(StateIdle.class, state().getClass());
        state().abortGame();
    }

    public void skipPostGameStatistics() {
        assertEquals(StateGameOver.class, state().getClass());
        state().skip();
    }

    public void skipTilePlacement() {
        assertEquals(StatePlacing.class, state().getClass());
        Tile tile = getRound().getActivePlayer().getHandOfTiles().iterator().next();
        views.setNextTile(tile);
        state().skip();
    }

    public void skipMeeplePlacement() {
        assertEquals(StateManning.class, state().getClass());
        state().skip();
    }

    /**
     * Convenience method to set the next tile (circumventing the stack) and place it.
     * @param type is the type of tile to place.
     * @param rotation is the rotation for the tile to place.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void placeTile(TileType type, TileRotation rotation, int x, int y) {
        placeTile(createTile(type, rotation), x, y);
    }

    public void placeTile(TileType type, int x, int y) {
        placeTile(type, TileRotation.UP, x, y);
    }

    /**
     * Convenience method to set and place the next tile (circumventing the stack) and also place a meeple.
     * @param type is the type of tile to place.
     * @param rotation is the rotation for the tile to place.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     * @param meeplePosition is the direction that indicates the position on the tile to place the meeple.
     */
    public void placeTileAndMeeple(TileType type, TileRotation rotation, int x, int y, GridDirection meeplePosition) {
        Tile tile = createTile(type, rotation);
        placeTile(tile, x, y);
        assertTrue(tile.allowsPlacingMeeple(meeplePosition, getRound().getActivePlayer(), settings));
        state().placeMeeple(meeplePosition);
    }

    public void placeTileAndMeeple(TileType type, int x, int y, GridDirection meeplePosition) {
        placeTileAndMeeple(type, TileRotation.UP, x, y, meeplePosition);
    }

    /**
     * Convenience method to set and place the next tile (circumventing the stack) and also skips meeple placement.
     * @param type is the type of tile to place.
     * @param rotation is the rotation for the tile to place.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void placeTileWithoutMeeple(TileType type, TileRotation rotation, int x, int y) {
        Tile tile = createTile(type, rotation);
        placeTile(tile, x, y);
        state().skip();
    }

    public void placeTileWithoutMeeple(TileType type, int x, int y) {
        placeTileWithoutMeeple(type, TileRotation.UP, x, y);
    }

    /**
     * Convenience method to set the next tile (circumventing the stack) and place it.
     * @param tile is the tile to place.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    private void placeTile(Tile tile, int x, int y) {
        views.setNextTile(tile);
        boolean placeable = getGrid().getSpot(x, y).isPlaceable(tile, settings.isAllowingEnclaves());
        assertTrue(placeable, "Cannot place tile " + tile.getType() + " at position (" + x + ", " + y + ")." + GridPrinter.printTile(tile));
        state().placeTile(x, y);
        assertTrue(getGrid().getSpot(x, y).isOccupied(), "Could not place " + tile.getType() + " at position (" + x + ", " + y + ").");
    }

    public Class<? extends AbstractGameState> getState() {
        return state().getClass();
    }

    public GameSettings getSettings() {
        return settings;
    }

    /**
     * Returns the current {@link Round}.
     * @return the current round, or {@code null} if not yet initialized
     */
    public Round getRound() {
        return stateMachine.getRound();
    }

    /**
     * Returns the current {@link TileStack}.
     * @return the current tile stack, or {@code null} if not yet initialized
     */
    public TileStack getTileStack() {
        return stateMachine.getTileStack();
    }

    /**
     * Returns the current {@link Grid}.
     * @return the current grid, or {@code null} if not yet initialized
     */
    public Grid getGrid() {
        return stateMachine.getGrid();
    }

    /**
     * Returns direct access to the current state.
     * @return the current state, which is only valid until the next state change.
     */
    public AbstractGameState state() {
        return stateMachine.getCurrentState();
    }

    private Tile createTile(TileType type, TileRotation rotation) {
        Tile tile = new Tile(type);
        tile.rotateTo(rotation);
        return tile;
    }

}
