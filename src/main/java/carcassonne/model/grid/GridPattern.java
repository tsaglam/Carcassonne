package carcassonne.model.grid;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.Player;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * A pattern of connected terrain on tiles of the grid.
 * @author Timur Saglam
 */
public class GridPattern {

    private TerrainType patternType;
    private List<Tile> tileList;
    private Map<Player, Integer> involvedPlayers;
    private boolean isComplete;
    private boolean isDisbursed;

    /**
     * Basic constructor.
     */
    public GridPattern(TerrainType patternType) {
        this.patternType = patternType;
        tileList = new LinkedList<Tile>();
        involvedPlayers = new HashMap<Player, Integer>();
        isComplete = false;
        isDisbursed = false;
    }

    /**
     * Adds a tile to the pattern, saving the tile, the owner of a potential Meeple on the tile.
     * @param tile is the tile to add.
     */
    public void add(Tile tile) {
        tileList.add(tile);
        if (tile.hasMeeple()) {
            Player player = tile.getMeeple().getOwner();
            if (involvedPlayers.containsKey(player)) {
                involvedPlayers.put(player, involvedPlayers.get(player) + 1);
            } else {
                involvedPlayers.put(player, 1);
            }
        }
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
     * Gives every involved player points if he is one of the players with the maximal amount of
     * meeples on the pattern. Can only be called once in the lifetime of a GridPttern object.
     */
    public void disburse() {
        if (isComplete && !isDisbursed) {
            int maximum = Collections.max(involvedPlayers.values());
            double divider = 0;
            for (Player player : involvedPlayers.keySet()) {
                if (involvedPlayers.get(player) == maximum) {
                    divider++;
                } else {
                    involvedPlayers.remove(player);
                }
            }
            for (Player player : involvedPlayers.keySet()) {
                player.addPoints((int) Math.ceil(getSize() / divider), patternType);
            }
            isDisbursed = true;
        }
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
        return isComplete;
    }
}
