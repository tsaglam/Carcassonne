package carcassonne.model; // maybe move to controller? or not?

import carcassonne.control.GameOptions;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;
import carcassonne.model.tile.TileType;

/**
 * An object of the round class simulates a game round. It does not actively control the game. It
 * represents the round and its information in an object.
 * @author Timur
 */
public class Round {

    private Grid grid;
    private Player[] player;
    private int activePlayer;
    private TileStack tileStack;
    private Tile currentTile;

    /**
     * Simple constructor that creates the grid, the tile stack and the players.
     * @param playerCount is the amount of players of the round.
     * @param height is the maximal grid height
     * @param width is the maximal grid width
     */
    public Round(int playerCount, int width, int height) {
        grid = new Grid(width, height, TileType.CastleWallRoad);
        tileStack = new TileStack();
        createPlayers(playerCount);
        currentTile = getFirstTile();
    }

    /**
     * Getter for the number of the active player.
     * @return the player number.
     */
    public int getActivePlayerNumber() {
        return activePlayer;
    }

    /**
     * Getter for foundation tile of a round.
     * @return the the foundation tile.
     */
    public Tile getFirstTile() {
        return grid.getTile(Math.round((grid.getWidth() - 1) / 2), Math.round((grid.getHeight() - 1) / 2));
    }

    /**
     * Getter for the current tile.
     * @return the currentTile.
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Checks whether the game round is over. A game round is over if the grid is full or the stack
     * of tiles is empty (no tiles left).
     * @return true if the game is over.
     */
    public boolean isOver() {
        return grid.isFull() || tileStack.isEmpty();
    }

    /**
     * Checks whether the game round is NOT over. A game round is over if the grid is full or the
     * stack of tiles is empty (no tiles left).
     * @return true if the game is NOT over.
     */
    public boolean isNotOver() {
        return !isOver();
    }

    /**
     * Method the starts the turn of the next player.
     */
    public void nextTurn() {
        activePlayer++;
        if (activePlayer == player.length) {
            activePlayer = 0;
        }
        currentTile = tileStack.drawTile();
    }

    public boolean makeGridPlacement(int x, int y, Tile tile) {
        currentTile = tile;
        return grid.place(x, y, tile);
    }

    public boolean makeMeeplePlacement(GridDirection direction) {
        player[activePlayer].placeMeepleAt(currentTile);
        return true; // TODO (HIGH) make check for amount of meeples
    }

    /**
     * comment game logic method. draw tile from stack show placement gui place tile on grid paint
     * tile. show meeple gui place meeple on grid paint meeple evaluate points add points check if
     * player won or grid is full next player
     */
    public void startGame() {
        // TODO (MEDIUM) implement game logic as stated in the jdoc comment.
    }

    /**
     * creates the player objects and sets the first player as active player.
     * @param playerCount is the number of players in the range of [1,
     * <code>GameOptions.maximalPlayers]</code>.
     */
    private void createPlayers(int playerCount) {
        if (playerCount <= 1 || playerCount >= GameOptions.getInstance().maximalPlayers) {
            throw new IllegalArgumentException(playerCount + " is not a valid player count");
        }
        player = new Player[playerCount]; // initialize the player array.
        for (int i = 0; i < player.length; i++) {
            player[i] = new Player(); // create the players.
        }
        activePlayer = 0; // set first player as active.

    }

}
