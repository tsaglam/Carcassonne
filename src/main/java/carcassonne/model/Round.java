package carcassonne.model;

import carcassonne.control.GameOptions;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;

/**
 * An object of the round class simulates a game round. It does not actively control the game. It
 * represents the round and its information in an object.
 * @author Timur
 */
public class Round {

	private Grid grid;
	private Player[] player;
    private int activePlayer;
    private int playerCount;
    private int turnCounter;
    private TileStack tileStack;
    private Tile currentTile;

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
        currentTile = grid.getFoundation();
        turnCounter = 1;
    }

    /**
     * creates the player objects and sets the first player as active player.
     * @param playerCount is the number of players in the range of [1,
     * <code>GameOptions.maximalPlayers]</code>.
     */
    private void createPlayers() {
        if (playerCount <= 1 || playerCount >= GameOptions.getInstance().maximalPlayers) {
            throw new IllegalArgumentException(playerCount + " is not a valid player count");
        }
        player = new Player[playerCount]; // initialize the player array.
        for (int i = 0; i < player.length; i++) {
            player[i] = new Player(i); // create the players.
        }
        activePlayer = 0; // set first player as active.

    }

    public Player getActivePlayer() {
  		return player[activePlayer];
  	}

    public Tile getCurrentTile() {
  		return currentTile;
  	}
    
    /**
     * Setter for the current tile.
     * @param newTile is the new Tile to set.
     */
    public void updateCurrentTile(Tile newTile) {
        currentTile = newTile;
    }

    public Player getPlayer(int playerNumber) {
        return player[playerNumber];
    }
    
    public int getPlayerCount() {
  		return playerCount;
  	}

  	public TileStack getTileStack() {
  		return tileStack;
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
        activePlayer++;
        if (activePlayer == player.length) {
            activePlayer = 0;
            turnCounter++;
        }
        currentTile = tileStack.drawTile();
    }

  	public void setCurrentTile(Tile currentTile) {
  		this.currentTile = currentTile;
  	}

}
