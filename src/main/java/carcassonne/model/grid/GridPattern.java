package carcassonne.model.grid;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * A pattern of connected terrain on tiles of the grid. A grid pattern contains information about
 * the tiles of the pattern and the players involved in the pattern. Also it counts the amount of
 * meeples per player on the tiles of the pattern.
 * @author Timur Saglam
 */
public class GridPattern {

    protected final TerrainType patternType;
    protected List<Tile> tileList;
    protected Map<Player, Integer> involvedPlayers;
    protected List<Meeple> meepleList;
    protected boolean complete;
    private boolean disbursed;
    private boolean reducedPoints;

    /**
     * Basic constructor taking only a tile type.
     * @param patternType is the type of the pattern.
     */
    protected GridPattern(TerrainType patternType) {
        this.patternType = patternType;
        tileList = new LinkedList<Tile>();
        meepleList = new LinkedList<Meeple>();
        involvedPlayers = new HashMap<Player, Integer>();
        complete = false;
        disbursed = false;
        reducedPoints = false;
    }

    /**
     * Checks whether the pattern already contains a tile.
     * @param tile is the tile to check.
     * @return true if the pattern already contains the tile.
     */
    public boolean contains(Tile tile) {
        return tileList.contains(tile);
    }

    /**
     * Disburses complete patterns. Gives every involved player points if he is one of the players
     * with the maximal amount of meeples on the pattern. Removes the meeple placement and returns
     * them to the players. Can only be called once in the lifetime of a GridPttern object.
     */
    public void disburse() {
        if (!disbursed && complete && !involvedPlayers.isEmpty()) {
            List<Player> removalList = new LinkedList<Player>();
            int maximum = Collections.max(involvedPlayers.values()); // most meeples on pattern
            for (Player player : involvedPlayers.keySet()) { // for all involved players
                if (involvedPlayers.get(player) != maximum) { // if has not enough meeples
                    removalList.add(player); // add to removal list (remove later)
                }
            }
            for (Player player : removalList) {
                involvedPlayers.remove(player); // remove players who don't get points
            }
            for (Player player : involvedPlayers.keySet()) { // other players split the pot
                player.addScore((int) Math.ceil(getSize() / involvedPlayers.size()), patternType, reducedPoints);
            }
            for (Meeple meeple : meepleList) {
                meeple.removePlacement(); // remove meeples from tiles.
            }
            disbursed = true;
        }
    }

    /**
     * Disburses pattern if it is incomplete. This should be used at the end of the round and does
     * not disburse complete patterns.
     */
    public void forceDisburse() {
        if (!complete) {
            complete = true;
            reducedPoints = true;
            disburse();
        }
    }

    /**
     * Getter for the meeple list.
     * @return the meeple list.
     */
    public List<Meeple> getMeepleList() {
        return meepleList;
    }

    /**
     * Returns the current size of the pattern, which equals the amount of added tiles.
     * @return the size.
     */
    public int getSize() {
        return tileList.size();
    }

    /**
     * Checks whether the pattern is complete or not. That means there cannot be
     * @return true if complete.
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Checks whether no player has set a meeple on the pattern.
     * @return true if the pattern is not occupied, false if not.
     */
    public boolean isNotOccupied() {
        return involvedPlayers.isEmpty();
    }

    /**
     * Checks whether a specific player is involved in the occupation of the pattern. That means he
     * has at least one meeple on the pattern.
     * @param player is the specific player.
     * @return true if he is involved in the occupation of the pattern, false if not.
     */
    public boolean isOccupiedBy(Player player) {
        return involvedPlayers.containsKey(player);
    }

    /**
     * Removes all tags of all tiles of the pattern. Needs to be called after ALL patterns of a tile
     * have been created.
     */
    public void removeTileTags() {
        for (Tile tile : tileList) {
            tile.removeTags();
        }
    }

    @Override
    public String toString() {
        return "GridPattern[type: " + patternType + ", size: " + getSize() + ", complete: " + complete + ", meeples: " + meepleList;
    }

    // adds meeple from tile to involvedPlayers map if the meeple is involved in the pattern.
    private void addMeepleFrom(Tile tile) {
        Meeple meeple = tile.getMeeple(); // Meeple on the tile.
        if (!meepleList.contains(meeple) && isPartOfPattern(tile, meeple.getPlacementPosition())) {
            Player player = meeple.getOwner(); // owner of the meeple.
            if (involvedPlayers.containsKey(player)) {
                involvedPlayers.put(player, involvedPlayers.get(player) + 1);
            } else {
                involvedPlayers.put(player, 1);
            }
            meepleList.add(meeple);
        }
    }

    private boolean isPartOfPattern(Tile tile, GridDirection position) {
        boolean onCorrectTerrain = tile.getTerrain(position) == patternType;
        boolean onPattern = tile.isConnectedToTag(position, this) || patternType == TerrainType.MONASTERY;
        return onCorrectTerrain && onPattern;
    }

    /**
     * Adds a tile to the pattern, saving the tile, the owner of a potential Meeple on the tile.
     * @param tile is the tile to add.
     */
    protected void add(Tile tile) {
        tileList.add(tile);
        if (tile.hasMeeple()) {
            addMeepleFrom(tile);
        }
    }
}
