package carcassonne.model;

import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileStack;
import carcassonne.model.tile.TileType;

/**
 * An object of the round class simulates a game round.
 * @author Timur
 */
public class Round {

    private Grid grid;
    private Player[] player;
    private int activePlayer;
    private TileStack tileStack;

    /**
     * Simple constructor that creates the grid, the tile stack and the players.
     * @param playerCount is the amount of players of the round.
     * @param height is the maximal grid height
     * @param width is the maximal grid width
     */
    public Round(int playerCount, int width, int height) {
        grid = new Grid(width, height, TileType.CastleWallRoad);
        // TODO get the control or gui to draw the tile.
        tileStack = new TileStack();
        createPlayers(playerCount);
    }

    /**
     * Getter for the number of the active player.
     * @return the player number.
     */
    public int getActivePlayerNumber() {
        return activePlayer;
    }

    /**
     * Method the starts the turn of the next player.
     */
    public void nextTurn() {
        activePlayer++;
        if (activePlayer == player.length) {
            activePlayer = 0;
        }
    }

    /**
     * TODO comment game logic method.  
     * draw tile from stack show placement gui place tile on grid paint tile. show
     * meeple gui place meeple on grid paint meeple evaluate points add points check if player won
     * or grid is full next player
     */
    public void startGame() {
        // TODO implement game logic as stated in the jdoc comment.
    }

    /**
     * creates the player objects and sets the first player as active player.
     * @param playerCount is the number of players.
     */
    private void createPlayers(int playerCount) {
        player = new Player[playerCount]; // initialize the player array.
        for (int i = 0; i < player.length; i++) {
            player[i] = new Player(); // create the players.
        }
        activePlayer = 0; // set first player as active.

    }

    /**
     * Checks whether the game round is over. A game round is over if the grid is full or the stack
     * of tiles is empty (no tiles left).
     * @return true if the game is over.
     */
    private boolean isOver() {
        return grid.isFull() || tileStack.isEmpty();
    }

}
