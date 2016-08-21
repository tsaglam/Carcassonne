package carcassonne.model.grid;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.tile.TerrainType;
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
    protected boolean complete;
    protected boolean disbursed;

    /**
     * Basic constructor taking only a tile type.
     * @param patternType is the type of the pattern.
     */
    protected GridPattern(TerrainType patternType) {
        this.patternType = patternType;
        tileList = new LinkedList<Tile>();
        involvedPlayers = new HashMap<Player, Integer>();
        complete = false;
        disbursed = false;
    } // TODO (HIGH) Fix: Needs method that removes the meeples from the tiles and gives information
      // about meeple removal to the GUI

    /**
     * Adds a tile to the pattern, saving the tile, the owner of a potential Meeple on the tile.
     * @param tile is the tile to add.
     */
    protected void add(Tile tile) {
        if (complete) {
            throw new IllegalStateException("Can't add a tile to a completed pattern.");
        }
        tileList.add(tile);
        if (tile.hasMeeple()) {
            addMeepleFrom(tile);
        }
    }

    // adds meeple from tile to involvedPlayers map if the meeple is involved in the pattern.
    private void addMeepleFrom(Tile tile) {
        Meeple meeple = tile.getMeeple(); // Meeple on the tile.
        Player player = meeple.getOwner(); // owner of the meeple.
        GridDirection position = meeple.getPlacementPosition(); // position of meeple on tile.
        if (tile.getTerrain(position) == patternType && tile.isConnectedToTag(position)) {
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
        if (!disbursed) {
            if (complete && involvedPlayers.size() > 0) {
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
            }
            disbursed = true;
        }
    }

    /**
     * Removes the tags of the tile of the pattern. Should be called if the check for this kind of
     * pattern is complete.
     */
    public void removeTileTags() {
        for (Tile tile : tileList) {
            tile.removeTags();
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
        return complete;
    }

    @Override
    public String toString() {
        return "GridPattern of type " + patternType + ", size " + getSize() + " and is complete: " + complete;
    }
}
