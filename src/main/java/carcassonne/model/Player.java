package carcassonne.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * The class for the player objects. It manages the meeples and the points.
 * @author Timur Saglam
 */
public class Player {

    private static final int MEEPLE_COUNT = 5;
    private int points;
    private Map<TerrainType, Integer> pointMap;
    private List<Meeple> usedMeeples;
    private List<Meeple> unusedMeeples;
    private final int number;

    /**
     * Simple constructor.
     * @param playerNumber is the number of the player.
     */
    public Player(int number) {
        this.number = number;
        points = 0;
        pointMap = new HashMap<TerrainType, Integer>();
        for (int i = 0; i < TerrainType.values().length - 2; i++) {
            pointMap.put(TerrainType.values()[i], 0);
        }
        unusedMeeples = new LinkedList<Meeple>();
        usedMeeples = new LinkedList<Meeple>();
        for (int i = 0; i < MEEPLE_COUNT; i++) {
            unusedMeeples.add(new Meeple(this));
        }
    }

    /**
     * Adds points to the players point value and keeps track of the type of points.
     * @param points are the points to add.
     */
    public void addPoints(int amount, TerrainType pointType) {
        int pointsToAdd = calculatePoints(amount, pointType);
        pointMap.put(pointType, pointMap.get(pointType) + pointsToAdd);
        points += pointsToAdd;
    }

    /**
     * Multiplies the amount of points by the multiplier of the type of the points.
     * @param amount sets the amount of points.
     * @param pointType is the type of points, which influences the multiplier.
     * @return the multiplied points.
     */
    private int calculatePoints(int amount, TerrainType pointType) {
        if (pointType == TerrainType.CASTLE) {
            return amount * 2;
        } else if (pointType == TerrainType.FIELDS) {
            return amount * 9;
        } else {
            return amount;
        }
    }

    /**
     * Getter for the points of the player.
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Getter for number of the player.
     * @return the player number.
     */
    public int getNumber() {
        return number;
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
     * @return
     */
    public void placeMeepleAt(Tile tile, GridDirection positionOnTile) {
        if (unusedMeeples.isEmpty()) {
            throw new IllegalStateException("No unused meeples are left.");
        }
        Meeple meeple = unusedMeeples.remove(0); // get free meeple.
        meeple.placeOn(tile, positionOnTile); // place it.
        usedMeeples.add(meeple); // and put it to the used ones.
    }

    /**
     * Returns a meeple from the used meeples to the unused meeples.
     * @param meeple is the meeple to return;
     */
    public void returnMeeple(Meeple meeple) {
        if (!usedMeeples.remove(meeple)) { // if it can not find the meeple in the used list.
            throw new IllegalArgumentException("This meeple is not in the used list.");
        }
        unusedMeeples.add(meeple); // put in unused list if it was previously used.
    }

    @Override
    public String toString() {
        return "Player[number: " + number + ", points: " + points + ", used meeples: " + usedMeeples.size() + ", unused meeples: " + unusedMeeples.size() + "]";
    }

}
