package carcassonne.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileStack;
import carcassonne.settings.GameSettings;

/**
 * An object of the round class simulates a game round. It does not actively control the game. It represents the round
 * and its information in an object.
 * @author Timur Saglam
 */
public class Round {

    private static final String NOTHING = "";
    private static final String SQUARE_BRACKETS = "[\\[\\]]";
    
    private int activePlayerIndex;
    private final Grid grid;
    private Player[] players;
    private final int playerCount;
    private final TileStack tileStack;

    /**
     * Simple constructor that creates the grid, the tile stack and the players.
     * @param playerCount is the amount of players of the round.
     * @param tileStack is the stack of tiles.
     * @param grid is the grid of the round.
     * @param settings are the {@link GameSettings}.
     */
    public Round(int playerCount, TileStack tileStack, Grid grid, GameSettings settings) {
        this.grid = grid;
        this.playerCount = playerCount;
        this.tileStack = tileStack;
        createPlayers(settings);
    }

    /**
     * Getter for the active players of the round.
     * @return the players whose turn it is.
     */
    public Player getActivePlayer() {
        return players[activePlayerIndex];
    }

    /**
     * Checks if there are any human players in a match.
     * @return true if at least human player is taking part.
     */
    public boolean hasHumanPlayers() {
        return Arrays.stream(players).anyMatch(it -> !it.isComputerControlled());
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

    /**
     * Method determines the winning players by the highest score.
     * @return a list of names of the winning players.
     */
    public String winningPlayers() {
        List<String> winnerList = new LinkedList<>();
        int maxScore = 0;
        for (Player player : players) {
            if (player.getScore() >= maxScore) {
                if (player.getScore() > maxScore) {
                    winnerList.clear();
                }
                winnerList.add(player.getName());
                maxScore = player.getScore();
            }
        }
        return winnerList.toString().replaceAll(SQUARE_BRACKETS, NOTHING);
    }

    /**
     * Checks whether the game round is over. A game round is over if the grid is full or the stack of tiles is empty (no
     * tiles left).
     * @return true if the game is over.
     */
    public boolean isOver() {
        return grid.isFull() || tileStack.isEmpty() && Arrays.stream(players).allMatch(Player::hasEmptyHand);
    }

    /**
     * Method the starts the turn of the next players a draws a tile from the stack.
     */
    public void nextTurn() {
        activePlayerIndex = ++activePlayerIndex % players.length;
    }

    /**
     * creates the players objects and sets the first players as active players.
     * @param playerCount is the number of players in the range of [1, <code>GameOptions.MAXIMAL_PLAYERS]</code>.
     */
    private void createPlayers(GameSettings settings) {
        if (playerCount <= 1 || playerCount > GameSettings.MAXIMAL_PLAYERS) {
            throw new IllegalArgumentException(playerCount + " is not a valid players count");
        }
        players = new Player[playerCount]; // initialize the players array.
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i, settings); // create the players.
        }
    }
}
