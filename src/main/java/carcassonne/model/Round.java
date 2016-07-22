package carcassonne.model;

import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileStack;

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
        grid = new Grid(width, height);
        // TODO build starting tile on grid.
        tileStack = new TileStack();
        createPlayers(playerCount);
    }

    private void createPlayers(int playerCount) {
        player = new Player[playerCount]; // initialize the player array.
        for (int i = 0; i < player.length; i++) {
            player[i] = new Player(); // create the players.
        }
        activePlayer = 0; // set first player as active.

    }
    
    /*
     * TODO game logic
     * draw tile from stack
     * show placement gui
     * place tile on grid
     * paint tile.
     * show meeple gui
     * place meeple on grid
     * paint meeple
     * evaluate points
     * add points
     * check if player won or grid is full
     * next player
     */

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
     * Getter for the number of the active player.
     * @return the player number.
     */
    public int getActivePlayerNumber() {
        return activePlayer;
    }

}
