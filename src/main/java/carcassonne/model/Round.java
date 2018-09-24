package carcassonne.model;

import java.util.LinkedList;
import java.util.List;

import carcassonne.control.GameOptions;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;

/**
 * An object of the round class simulates a game round. It does not actively control the game. It represents the round
 * and its information in an object.
 * @author Timur Saglam
 */
public class Round {

    private int activePlayerIndex;
    private Tile currentTile;
    private final Grid grid;
    private Player[] players;
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
        tileStack = new TileStack(playerCount);
        createPlayers();
        currentTile = grid.getFoundation().getTile();
        turnCounter = 1;
    }

    /**
     * Getter for the active players of the round.
     * @return the players whose turn it is.
     */
    public Player getActivePlayer() {
        return players[activePlayerIndex];
    }

    /**
     * Getter for the current tile, that was drawn from the tile stack.
     * @return the current tile of the turn.
     */
    public Tile getCurrentTile() {
        return currentTile;
    }

    /**
     * Getter for a specific players of the round.
     * @param playerNumber is the number of the specific players.
     * @return returns the players.
     */
    public Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    /**
     * Getter for the amount of players in the round.
     * @return the amount of players.
     */
    public int getPlayerCount() {
        return playerCount;
    }

    public int getStackSize() {
        return tileStack.getSize();
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
        List<String> winnerList = new LinkedList<>();
        int maxScore = 0;
        for (Player player : players) {
            if (player.getScore() >= maxScore) {
                if (player.getScore() > maxScore) {
                    winnerList.clear();
                }
                winnerList.add(playerNames[player.getNumber()]);
                maxScore = player.getScore();
            }
        }
        return winnerList;
    }

    /**
     * Checks whether the game round is NOT over. A game round is over if the grid is full or the stack of tiles is empty
     * (no tiles left).
     * @return true if the game is NOT over.
     */
    public boolean isNotOver() {
        return !isOver();
    }

    /**
     * Checks whether the game round is over. A game round is over if the grid is full or the stack of tiles is empty (no
     * tiles left).
     * @return true if the game is over.
     */
    public boolean isOver() {
        return grid.isFull() || tileStack.isEmpty();
    }

    /**
     * Method the starts the turn of the next players a draws a tile from the stack.
     */
    public void nextTurn() {
        activePlayerIndex++;
        if (activePlayerIndex == players.length) {
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
        if (currentTile.getClass() != newTile.getClass()) {
            throw new IllegalArgumentException("type of new tile does not match old tile");
        }
        currentTile = newTile;
    }

    /**
     * creates the players objects and sets the first players as active players.
     * @param playerCount is the number of players in the range of [1, <code>GameOptions.maximalPlayers]</code>.
     */
    private void createPlayers() {
        if (playerCount <= 1 || playerCount > GameOptions.getInstance().maximalPlayers) {
            throw new IllegalArgumentException(playerCount + " is not a valid players count");
        }
        players = new Player[playerCount]; // initialize the players array.
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i); // create the players.
        }
        activePlayerIndex = 0; // set first players as active.
    }
}
