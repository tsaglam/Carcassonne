package carcassonne.model;

import java.util.LinkedList;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * The class for the player objects. It manages the meeples and the points.
 * @author Timur
 */
public class Player {

    private static final int MEEPLE_COUNT = 5;
    private int points;
    private LinkedList<Meeple> usedMeeples;
    private LinkedList<Meeple> unusedMeeples;

    /**
     * Simple constructor.
     * @param playerNumber is the number of the player.
     */
    public Player() {
        points = 0;
        unusedMeeples = new LinkedList<Meeple>();
        usedMeeples = new LinkedList<Meeple>();
        for (int i = 0; i < MEEPLE_COUNT; i++) {
            unusedMeeples.add(new Meeple(this));
        }
    }

    /**
     * Adds points to the players point value.
     * @param points are the points to add.
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Getter for the points of the player.
     * @return the points
     */
    public int getPoints() {
        return points;
    }
    
    /**
     * Checks whether the player can still place Meeples.
     * @return true if he has at least one unused Meeple.
     */
    public boolean hasUnusedMeeples() {
    	return unusedMeeples.size() > 0;
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
        Meeple meeple = unusedMeeples.poll(); // get free meeple.
        meeple.placeOn(tile); // place it.
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
        usedMeeples.add(meeple); // put in unused list if it was previously used.
    }

}
