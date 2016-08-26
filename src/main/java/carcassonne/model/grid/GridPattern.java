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
    protected List<Meeple> meepleList;
    protected boolean complete;
    protected boolean disbursed;

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
     * meeples on the pattern. Removes the meeple placement and returns them to the players. Can
     * only be called once in the lifetime of a GridPttern object.
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
                    player.addScore((int) Math.ceil(getSize() / divider), patternType);
                }
                for (Meeple meeple : meepleList) {
                    meeple.removePlacement();
                }
                disbursed = true;
            }
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
        return "GridPattern of type " + patternType + ", size " + getSize() + " and is complete: " + complete;
    }

    // adds meeple from tile to involvedPlayers map if the meeple is involved in the pattern.
    private void addMeepleFrom(Tile tile) {
        Meeple meeple = tile.getMeeple(); // Meeple on the tile.
        if (!meepleList.contains(meeple)) {
            GridDirection position = meeple.getPlacementPosition(); // position of meeple on tile.
            if (isPartOfPattern(tile, position)) {
                Player player = meeple.getOwner(); // owner of the meeple.
                if (involvedPlayers.containsKey(player)) {
                    involvedPlayers.put(player, involvedPlayers.get(player) + 1);
                } else {
                    involvedPlayers.put(player, 1);
                }
                meepleList.add(meeple);
            }
        }
    }

    private boolean isPartOfPattern(Tile tile, GridDirection position) {
        boolean isOnCorrectTerrain = tile.getTerrain(position) == patternType;
        boolean isOnPattern = tile.isConnectedToTag(position) || patternType == TerrainType.MONASTERY;
        return isOnCorrectTerrain && isOnPattern;
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
