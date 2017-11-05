package carcassonne.model;

import java.util.LinkedList;
import java.util.List;

import carcassonne.control.GameOptions;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;

/**
 * An object of the round class simulates a game round. It does not actively control the game. It
 * represents the round and its information in an object.
 * @author Timur Saglam
 */
public class Round {

    private int activePlayerIndex;
    private Tile currentTile;
    private final Grid grid;
    private Player[] player;
    private final int playerCount;
    private final TileStack tileStack;
    private int turnCounter;

    /**
     * Simple constructor that creates the grid, the tile stack and the players.
     * @param playerCount is the amount of players of the round.
     * @param grid is the grid of the round.
     */
    public Round(int playerCount, Grid grid) {
        this.grid = grid;
        this.playerCount = playerCount;
        tileStack = new TileStack();
        createPlayers();
        currentTile = grid.getFoundation().getTile();
        turnCounter = 1;
    }

    /**
     * Getter for the active player of the round.
     * @return the player whose turn it is.
     */
    public Player getActivePlayer() {
        return player[activePlayerIndex];
    }

    /**
     * Getter for the current tile, that was drawn from the tile stack.
     * @return the current tile of the turn.
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Getter for a specific player of the round.
     * @param playerNumber is the number of the specific player.
     * @return returns the player.
     */
    public Player getPlayer(int playerNumber) {
        return player[playerNumber];
    }

    /**
     * Getter for the amount of players in the round.
     * @return the amount of players.
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Getter for the turn counter.
     * @return the turn counter
     */
    public int getTurnCounter() {
        return turnCounter;
    }

    /**
     * Method determines the winning players by the highest score.
     * @return a list of names of the winning players.
     */
    public List<String> getWinningPlayers() {
        String[] playerNames = GameOptions.getInstance().playerNames;
        List<String> winnerList = new LinkedList<String>();
        int maxScore = 0;
        for (Player player : this.player) {
            if (player.getScore() >= maxScore) {
                if (player.getScore() > maxScore) {
                    winnerList.clear();
                }
                winnerList.add(playerNames[player.getNumber()]);
            }
        }
        return winnerList;
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
     * Checks whether the game round is over. A game round is over if the grid is full or the stack
     * of tiles is empty (no tiles left).
     * @return true if the game is over.
     */
    public boolean isOver() {
        return grid.isFull() || tileStack.isEmpty();
    }

    /**
     * Method the starts the turn of the next player a draws a tile from the stack.
     */
    public void nextTurn() {
        activePlayerIndex++;
        if (activePlayerIndex == player.length) {
            activePlayerIndex = 0;
            turnCounter++;
        }
        currentTile = tileStack.drawTile();
    }

    /**
     * Setter for the current tile.
     * @param newTile is the new Tile to set.
     */
    public void updateCurrentTile(Tile newTile) {
        if (currentTile.getType() != newTile.getType()) {
            throw new IllegalArgumentException("type of new tile does not match old tile");
        }
        currentTile = newTile;
    }

    /**
     * creates the player objects and sets the first player as active player.
     * @param playerCount is the number of players in the range of [1,
     * <code>GameOptions.maximalPlayers]</code>.
     */
    private void createPlayers() {
        if (playerCount <= 1 || playerCount > GameOptions.getInstance().maximalPlayers) {
            throw new IllegalArgumentException(playerCount + " is not a valid player count");
        }
        player = new Player[playerCount]; // initialize the player array.
        for (int i = 0; i < player.length; i++) {
            player[i] = new Player(i); // create the players.
        }
        activePlayerIndex = 0; // set first player as active.

    }

}
