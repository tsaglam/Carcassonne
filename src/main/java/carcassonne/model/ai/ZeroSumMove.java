package carcassonne.model.ai;

import java.util.Collection;
import java.util.stream.Stream;

import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridPattern;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.settings.GameSettings;

/**
 * Represents a single move of a player which is valued mostly according to the immediate value that moves brings in
 * addition to some very simple rules.
 * @author Timur Saglam
 */
public class ZeroSumMove implements CarcassonneMove { // TODO (HIGH) separate move from zero sum move
    private final GridSpot gridSpot;
    private int meeplesGained;
    private final Player player;
    private final GridDirection position;
    private final GameSettings settings;
    private final TemporaryTile temporaryTile;
    private final double value;

    /**
     * Creates the move. Does not check if the move is legal.
     * @param temporaryTile is the temporaryTile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param position is the position on which the meeple is placed on the temporaryTile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the temporaryTile is placed on.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile temporaryTile, GridDirection position, Player player, GameSettings settings) {
        this.temporaryTile = temporaryTile;
        this.position = position;
        this.player = player;
        this.gridSpot = temporaryTile.getGridSpot();
        this.settings = settings;
        value = calculateValue();
    }

    /**
     * Creates the move without a meeple placement. Does not check if the move is legal.
     * @param temporaryTile is the temporaryTile placed in the move. Needs to be assigned to a {@link GridSpot}.
     * @param position is the position on which the meeple is placed on the temporaryTile.
     * @param player is the responsible player.
     * @param gridSpot is the spot the temporaryTile is placed on.
     * @param settings are the game settings.
     */
    public ZeroSumMove(TemporaryTile tile, Player player, GameSettings settings) {
        this(tile, null, player, settings);
    }

    @Override
    public int getMeeplesGained() {
        return meeplesGained;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public GridDirection getPosition() {
        return position;
    }

    @Override
    public TileRotation getRotation() { // TODO (VERY HIGH) own class, separate zero sum from base calculations
        return temporaryTile.getRotation();
    }

    @Override
    public TerrainType getTerrainType() {
        if (position == null) {
            return null;
        }
        return temporaryTile.getTerrain(position);
    }

    @Override
    public Tile getTile() {
        return temporaryTile.getOriginal();
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int getX() {
        return gridSpot.getX();
    }

    @Override
    public int getY() {
        return gridSpot.getY();
    }

    @Override
    public boolean involvesMeeplePlacement() {
        return position != null;
    }

    @Override
    public boolean isFieldMove() {
        return involvesMeeplePlacement() && temporaryTile.getTerrain(position) == TerrainType.FIELDS;
    }

    @Override
    public String toString() {
        String meeple = involvesMeeplePlacement() ? temporaryTile.getTerrain(position) + " on " + position : "without meeple";
        return "Move for " + player.getName() + " with value " + value + ": " + temporaryTile.getType() + " " + meeple + " " + gridSpot;
    }

    private int calculateEmployedMeeples(Collection<GridPattern> localPatterns) {
        Stream<Meeple> localMeeples = localPatterns.stream().flatMap(it -> it.getMeepleList().stream());
        return Math.toIntExact(localMeeples.filter(it -> it.getOwner() == player).count());
    }

    private double calculateScoreSnapshot(Collection<GridPattern> localPatterns) {
        return localPatterns.stream().mapToInt(this::zeroSumScore).sum();
    }

    private double calculateValue() {
        gridSpot.removeTile();
        Collection<GridPattern> localPatterns = gridSpot.getGrid().getLocalPatterns(gridSpot);
        double scoreBefore = calculateScoreSnapshot(localPatterns);
        meeplesGained = calculateEmployedMeeples(localPatterns);
        gridSpot.place(temporaryTile);
        if (involvesMeeplePlacement()) {
            temporaryTile.placeMeeple(player, position, new TemporaryMeeple(player), settings);
        }
        localPatterns = gridSpot.getGrid().getLocalPatterns(gridSpot);
        double scoreAfter = calculateScoreSnapshot(localPatterns);
        int meeplesAfter = calculateEmployedMeeples(localPatterns);
        temporaryTile.removeMeeple();
        meeplesGained -= meeplesAfter;
        double zeroSumScore = scoreAfter - scoreBefore;
        return zeroSumScore + 0.5 * meeplesGained; // TODO (HIGH) Implement variable meeple value
    }

    private int zeroSumScore(GridPattern pattern) {
        int score = 2 * pattern.getScoreFor(player);
        for (Player dominantPlayer : pattern.getDominantPlayers()) {
            score -= pattern.getScoreFor(dominantPlayer);
        }
        return score;
    }
}
