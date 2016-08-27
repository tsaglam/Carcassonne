package carcassonne.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * The class for the player objects. It manages the meeples and the score.
 * @author Timur Saglam
 */
public class Player {

    private static final int MEEPLE_COUNT = 5;
    private int overallScore;
    private Map<TerrainType, Integer> scoreMap;
    private List<Meeple> usedMeeples;
    private List<Meeple> unusedMeeples;
    private final int number;

    /**
     * Simple constructor.
     * @param number is the number of the player.
     */
    public Player(int number) {
        this.number = number;
        overallScore = 0;
        scoreMap = new HashMap<TerrainType, Integer>();
        for (int i = 0; i < TerrainType.values().length - 1; i++) {
            scoreMap.put(TerrainType.values()[i], 0);
        }
        unusedMeeples = new LinkedList<Meeple>();
        usedMeeples = new LinkedList<Meeple>();
        for (int i = 0; i < MEEPLE_COUNT; i++) {
            unusedMeeples.add(new Meeple(this));
        }
    }

    /**
     * Adds score to the players score value and keeps track of the type of score.
     * @param amount is the amount of points the player gets.
     * @param scoreType determines the score multiplier.
     */
    public void addScore(int amount, TerrainType scoreType) {
        int scoreToAdd = calculateScore(amount, scoreType);
        scoreMap.put(scoreType, scoreMap.get(scoreType) + scoreToAdd);
        overallScore += scoreToAdd;
    }

    /**
     * Getter for number of the player.
     * @return the player number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Getter for the score of the player.
     * @return the score
     */
    public int getScore() {
        return overallScore;
    }

    /**
     * Getter for a specific terrain score.
     * @return the specific score.
     */
    public int getTerrainScore(TerrainType scoreType) {
        if (scoreMap.containsKey(scoreType)) {
            return scoreMap.get(scoreType);
        }
        return -1; // error
    }

    /**
     * Getter for the amount of unused meeples.
     * @return the amount of unused meeples.
     */
    public int getUnusedMeepleCount() {
        return unusedMeeples.size();
    }

    /**
     * Checks whether the player can still place Meeples.
     * @return true if he has at least one unused Meeple.
     */
    public boolean hasUnusedMeeples() {
        return !unusedMeeples.isEmpty();
    }

    /**
     * Places one of its meeples on a tile. Tells the meeple it was placed.
     * @param tile is the tile to place a meeple on.
     * @param position is the position the meeple gets placed on.
     */
    public void placeMeepleAt(Tile tile, GridDirection position) {
        if (unusedMeeples.isEmpty()) {
            throw new IllegalStateException("No unused meeples are left.");
        }
        Meeple meeple = unusedMeeples.remove(0); // get free meeple.
        meeple.placeOn(tile, position); // place it.
        usedMeeples.add(meeple); // and put it to the used ones.
    }

    /**
     * Returns a meeple from the used meeples to the unused meeples.
     * @param meeple is the meeple to return;
     */
    public void returnMeeple(Meeple meeple) {
        if (!usedMeeples.remove(meeple)) { // if it can not find the meeple in the used list.
            throw new IllegalArgumentException("This meeple is not in the used list: " + meeple);
        }
        unusedMeeples.add(meeple); // put in unused list if it was previously used.
    }

    @Override
    public String toString() {
        return "Player[number: " + number + ", score: " + overallScore + ", used meeples: " + usedMeeples.size() + ", unused meeples: " + unusedMeeples.size() + "]";
    }

    /**
     * Multiplies the amount of score by the multiplier of the type of the score.
     * @param amount sets the amount of score.
     * @param scoreType is the type of score, which influences the multiplier.
     * @return the multiplied score.
     */
    private int calculateScore(int amount, TerrainType scoreType) {
        if (scoreType == TerrainType.CASTLE) {
            return amount * 2;
        } else if (scoreType == TerrainType.FIELDS) {
            return amount * 3;
        } else {
            return amount;
        }
    }

}
