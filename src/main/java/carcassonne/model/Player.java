package carcassonne.model;

import java.util.HashMap;
import java.util.Map;

import carcassonne.model.grid.CastleAndRoadPattern;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * The class for the player objects. It manages the meeples and the score.
 * @author Timur Saglam
 */
public class Player {
    private static final int MAX_MEEPLES = 5;
    private int freeMeeples;
    private Map<TerrainType, Integer> multiplierMap;
    private final int number;
    private int overallScore;
    private Map<TerrainType, Integer> scoreMap;

    /**
     * Simple constructor.
     * @param number is the number of the player.
     */
    public Player(int number) {
        this.number = number;
        freeMeeples = MAX_MEEPLES;
        initializeMultiplierMap();
        initializeScores();
    }

    /**
     * Adds score to the players score value and keeps track of the type of score.
     * @param amount is the amount of points the player gets.
     * @param scoreType determines the score multiplier.
     * @param gameOver determines if the game is running or not. Changes score multipliers.
     */
    public void addScore(int amount, TerrainType scoreType, boolean gameOver) {
        int scoreToAdd = calculateScore(amount, scoreType, gameOver);
        scoreMap.put(scoreType, scoreMap.get(scoreType) + scoreToAdd);
        overallScore += scoreToAdd;
    }

    /**
     * Getter for the amount of free meeples.
     * @return the amount of free meeples.
     */
    public int getFreeMeeples() {
        return freeMeeples;
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
     * @param scoreType is the type of the specific terrain score.
     * @return the specific score.
     */
    public int getTerrainScore(TerrainType scoreType) {
        if (scoreMap.containsKey(scoreType)) {
            return scoreMap.get(scoreType);
        }
        return -1; // error
    }

    /**
     * Checks whether the player can still place Meeples.
     * @return true if he has at least one free Meeple.
     */
    public boolean hasFreeMeeples() {
        return freeMeeples > 0;
    }

    /**
     * Places, if possible, one of the players meeples on a specific tile on the grid. Tells the
     * meeple it was placed.
     * @param tile is the tile to place a meeple on.
     * @param position is the position the meeple gets placed on.
     * @param grid is the grid where the tile is placed.
     * @return true if the meeple was placed, false if not.
     */
    public boolean placeMeepleAt(Tile tile, GridDirection position, Grid grid) {
        if (freeMeeples == 0) {
            throw new IllegalStateException("No unused meeples are left.");
        }
        if (canPlaceMeepleAt(tile, position, grid)) { // can place meeple:
            Meeple meeple = new Meeple(this); // create meeple
            freeMeeples--;
            meeple.placeOn(tile, position); // place it.
            return true;
        }
        return false; // Can't place meeple.
    }

    /**
     * Returns a meeple after its job is down. Allows the player to place another meeple.
     */
    public void returnMeeple() {
        freeMeeples++;
    }

    @Override
    public String toString() {
        return "Player[number: " + number + ", score: " + overallScore + ", free meeples: " + freeMeeples + "]";
    }

    /**
     * Multiplies the amount of score by the multiplier of the type of the score.
     * @param amount sets the amount of score.
     * @param scoreType is the type of score, which influences the multiplier.
     * @param gameOver determines if the game is running or not. Changes score multipliers.
     * @return the multiplied score.
     */
    private int calculateScore(int amount, TerrainType scoreType, boolean gameOver) {
        if (scoreType == TerrainType.CASTLE && gameOver) {
            return amount * 2;
        }
        return amount * multiplierMap.get(scoreType);
    }

    private boolean canPlaceMeepleAt(Tile tile, GridDirection position, Grid grid) {
        TerrainType terrain = tile.getTerrain(position);
        boolean placeable = false;
        if (terrain == TerrainType.MONASTERY) {
            placeable = true; // you can place on monastery
        } else { // castle or road
            CastleAndRoadPattern pattern = new CastleAndRoadPattern(tile, position, terrain, grid);
            if (pattern.isNotOccupied() || pattern.isOccupiedBy(this)) {
                placeable = true; // can place meeple
            }
            pattern.removeTileTags();
        }
        return placeable;
    }

    private void initializeMultiplierMap() {
        multiplierMap = new HashMap<TerrainType, Integer>();
        multiplierMap.put(TerrainType.CASTLE, 2);
        multiplierMap.put(TerrainType.ROAD, 1);
        multiplierMap.put(TerrainType.MONASTERY, 1);
        multiplierMap.put(TerrainType.FIELDS, 3);
    }

    private void initializeScores() {
        overallScore = 0;
        scoreMap = new HashMap<TerrainType, Integer>();
        for (int i = 0; i < TerrainType.values().length - 1; i++) {
            scoreMap.put(TerrainType.values()[i], 0); // initial scores are zero
        }
    }

}
