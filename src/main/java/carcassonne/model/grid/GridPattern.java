package carcassonne.model.grid;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;

/**
 * A pattern of connected terrain on tiles of the grid. A grid pattern contains information about the tiles of the
 * pattern and the players involved in the pattern. Also it counts the amount of meeples per player on the tiles of the
 * pattern.
 * @author Timur Saglam
 */
public class GridPattern {

    private boolean disbursed;
    protected boolean complete;
    private Map<Player, Integer> involvedPlayers;
    private List<Meeple> meepleList;
    protected final TerrainType patternType;
    protected int scoreMultiplier;
    protected List<GridSpot> containedSpots;

    /**
     * Basic constructor taking only a tile type.
     * @param patternType is the type of the pattern.
     * @param scoreMultiplier is the score multiplier of the pattern.
     */
    protected GridPattern(TerrainType patternType, int scoreMultiplier) {
        this.patternType = patternType;
        this.scoreMultiplier = scoreMultiplier;
        containedSpots = new LinkedList<>();
        meepleList = new LinkedList<>();
        involvedPlayers = new HashMap<>();
    }

    /**
     * Disburses complete patterns. Distributes the score among all dominant players on the pattern. Removes the meeple
     * placement and returns them to the players. Can only be called once in the lifetime of a pattern.
     * @param splitScore determines if shared patterns are scored by splitting the score or awarding full score.
     */
    public void disburse(boolean splitScore) {
        if (complete) {
            distributePatternScore(splitScore);
            meepleList.forEach(it -> it.getLocation().getTile().removeMeeple()); // remove meeples from tiles.
            involvedPlayers.clear();
        }
    }

    /**
     * Disburses incomplete patterns. Distributes the score among all dominant players on the pattern. This should be used
     * at the end of the round.
     * @param splitScore determines if shared patterns are scored by splitting the score or awarding full score.
     */
    public void forceDisburse(boolean splitScore) {
        if (!complete) {
            distributePatternScore(splitScore);
        }
    }

    /**
     * Determines the dominant players, which are the involved players with maximum amount of meeples on this pattern.
     */
    public List<Player> getDominantPlayers() {
        if (involvedPlayers.isEmpty()) {
            return Collections.emptyList();
        }
        int maximum = Collections.max(involvedPlayers.values()); // most meeples on pattern
        return involvedPlayers.keySet().stream().filter(player -> involvedPlayers.get(player) == maximum).collect(toList());
    }

    /**
     * Getter for the meeple list.
     * @return the meeple list.
     */
    public List<Meeple> getMeepleList() {
        return meepleList;
    }

    /**
     * Returns the score of the pattern, independent of which player is dominant.
     * @return the full score.
     */
    public int getPatternScore() {
        return containedSpots.size() * scoreMultiplier;
    }

    /**
     * Returns the type of the pattern in form of a {@link TerrainType}.
     * @return the pattern type.
     */
    public TerrainType getType() {
        return patternType;
    }

    public int getScoreFor(Player player) {
        List<Player> dominantPlayers = getDominantPlayers();
        if (dominantPlayers.contains(player)) {
            return divideScore(getPatternScore(), dominantPlayers);
        }
        return 0;
    }

    /**
     * Returns the current size of the pattern, which equals the amount of added tiles.
     * @return the size.
     */
    public int getSize() {
        return containedSpots.size();
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
     * Checks whether a specific player is involved in the occupation of the pattern. That means he has at least one meeple
     * on the pattern.
     * @param player is the specific player.
     * @return true if he is involved in the occupation of the pattern, false if not.
     */
    public boolean isOccupiedBy(Player player) {
        return involvedPlayers.containsKey(player);
    }

    /**
     * Removes all OWN tags of all tiles of the pattern.
     */
    public void removeOwnTags() {
        containedSpots.forEach(it -> it.removeTagsFrom(this));
    }

    /**
     * Removes all tags of all tiles of the pattern. Needs to be called after ALL patterns of a tile have been created.
     */
    public void removeTileTags() {
        containedSpots.forEach(it -> it.removeTags());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("GridPattern[type: ");
        builder.append(patternType).append(", size: ").append(getSize()).append(", complete: ").append(complete);
        builder.append(", disbursed: ").append(disbursed).append(", meeples: ").append(meepleList).append(", on: ");
        builder.append(containedSpots.stream().map(it -> "(" + it.getX() + "|" + it.getY() + ")").collect(toList()));
        return builder.toString();
    }

    private void distributePatternScore(boolean splitScore) {
        if (!disbursed && !involvedPlayers.isEmpty()) {
            List<Player> dominantPlayers = getDominantPlayers();
            int stake = splitScore ? divideScore(getPatternScore(), dominantPlayers) : getPatternScore();
            for (Player player : dominantPlayers) { // dominant players split the pot
                player.addPoints(stake, patternType);
            }
            disbursed = true;
        }
    }

    // adds meeple from tile to involvedPlayers map if the meeple is involved in the pattern.
    private void addMeepleFrom(GridSpot spot) {
        assert !disbursed;
        Meeple meeple = spot.getTile().getMeeple(); // Meeple on the tile.
        if (!meepleList.contains(meeple) && isPartOfPattern(spot, meeple.getPosition())) {
            Player player = meeple.getOwner(); // owner of the meeple.
            if (involvedPlayers.containsKey(player)) {
                involvedPlayers.put(player, involvedPlayers.get(player) + 1);
            } else {
                involvedPlayers.put(player, 1);
            }
            meepleList.add(meeple);
        }
    }

    private int divideScore(int score, List<Player> dominantPlayers) {
        return (int) Math.ceil(score / (double) dominantPlayers.size());
    }

    private boolean isPartOfPattern(GridSpot spot, GridDirection position) {
        boolean onCorrectTerrain = spot.getTile().getTerrain(position) == patternType;
        boolean onPattern = spot.isIndirectlyTaggedBy(position, this) || patternType == TerrainType.MONASTERY;
        return onCorrectTerrain && onPattern;
    }

    /**
     * Adds a spot to the pattern, saving the tile on the spot, the owner of a potential Meeple on the tile.
     * @param spot is the spot to add.
     */
    protected void add(GridSpot spot) {
        containedSpots.add(spot);
        if (spot.getTile().hasMeeple()) {
            addMeepleFrom(spot);
        }
    }

    /**
     * Checks the usual inputs on being null.
     * @param spot is any grid spot.
     * @param direction is any grid direction.
     * @param grid is any grid.
     */
    protected void checkArgs(GridSpot spot, GridDirection direction) {
        if (spot == null || direction == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
    }
}
