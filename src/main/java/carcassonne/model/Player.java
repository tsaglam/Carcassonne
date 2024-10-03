package carcassonne.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.settings.PlayerColor;

/**
 * The class for the player objects. It manages the meeples and the score.
 * @author Timur Saglam
 */
public class Player {
    private final int maximalTiles;
    private int freeMeeples;
    private final int number;
    private int overallScore;
    private Map<TerrainType, Integer> terrainSpecificScores;
    private final GameSettings settings;
    private final List<Tile> handOfTiles;
    private final boolean computerControlled;
    private final List<Meeple> placedMeeples;

    /**
     * Simple constructor.
     * @param number is the number of the player.
     * @param settings are the {@link GameSettings}.
     */
    public Player(int number, GameSettings settings) {
        this.number = number;
        this.settings = settings;
        freeMeeples = GameSettings.MAXIMAL_MEEPLES;
        maximalTiles = settings.getTilesPerPlayer();
        handOfTiles = new ArrayList<>();
        placedMeeples = new ArrayList<>();
        computerControlled = settings.isPlayerComputerControlled(number);
        initializeScores();
    }

    /**
     * Adds points to the players score value and keeps track of the type of score.
     * @param amount is the amount of points the player gets.
     * @param scoreType is the pattern type responsible for the points.
     */
    public void addPoints(int amount, TerrainType scoreType) {
        terrainSpecificScores.put(scoreType, terrainSpecificScores.get(scoreType) + amount);
        overallScore += amount;
    }

    /**
     * Adds a tile to the hand of the player if there is space.
     * @param tile the new tile.
     * @return true if successfully added, false if the hand is full.
     */
    public boolean addTile(Tile tile) {
        if (handOfTiles.size() < maximalTiles) {
            return handOfTiles.add(tile);
        }
        return false;
    }

    /**
     * Drops a tile from the hand of the player.
     * @param tile is the tile to drop.
     * @return true if it was dropped, false if it is not in the hand of the player.
     */
    public boolean dropTile(Tile tile) {
        return handOfTiles.remove(tile);
    }

    /**
     * Convenience method for {@link GameSettings#getPlayerColor(int)}.
     * @return the {@link PlayerColor} of this player.
     */
    public PlayerColor getColor() {
        return settings.getPlayerColor(number);
    }

    /**
     * Getter for the amount of free meeples.
     * @return the amount of free meeples.
     */
    public int getFreeMeeples() {
        return freeMeeples;
    }

    /**
     * Returns the number of placed meeples that cannot be retrieved, meaning placed on a field.
     * @return the number of placed fields meeples.
     */
    public int getUnretrievableMeeples() {
        return (int) placedMeeples.stream().filter(it -> it.getType() == TerrainType.FIELDS).count();
    }

    /**
     * Gives read access to the hand of tiles.
     * @return the hand of tiles.
     */
    public Collection<Tile> getHandOfTiles() {
        return new ArrayList<>(handOfTiles);
    }

    /**
     * Grants access to a meeple.
     * @return the meeple.
     */
    public Meeple getMeeple() {
        if (hasFreeMeeples()) {
            freeMeeples--;
            Meeple meeple = new Meeple(this);
            placedMeeples.add(meeple);
            assert placedMeeples.size() <= GameSettings.MAXIMAL_MEEPLES;
            return meeple;
        }
        throw new IllegalStateException("No unused meeples are left.");
    }

    /**
     * Convenience method for {@link GameSettings#getPlayerName(int)}.
     * @return the name of this player.
     */
    public String getName() {
        return settings.getPlayerName(number);
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
        if (terrainSpecificScores.containsKey(scoreType)) {
            return terrainSpecificScores.get(scoreType);
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
     * Checks if the hand of the player is full.
     * @return true if full.
     */
    public boolean hasFullHand() {
        return handOfTiles.size() == maximalTiles;
    }

    /**
     * Checks if the hand of the player is full.
     * @return true if full.
     */
    public boolean hasEmptyHand() {
        return handOfTiles.isEmpty();
    }

    /**
     * Returns a meeple after its job is down. Allows the player to place another meeple.
     */
    public void returnMeeple(Meeple meeple) {
        boolean isRemoved = placedMeeples.remove(meeple);
        assert isRemoved;
        freeMeeples++;
    }

    @Override
    public String toString() {
        return "Player[number: " + number + ", score: " + overallScore + ", free meeples: " + freeMeeples + "]";
    }

    private void initializeScores() {
        overallScore = 0;
        terrainSpecificScores = new EnumMap<>(TerrainType.class);
        for (int i = 0; i < TerrainType.values().length - 1; i++) {
            terrainSpecificScores.put(TerrainType.values()[i], 0); // initial scores are zero
        }
    }

    /**
     * Checks whether the player is a human player or an AI player.
     * @return true if it is an AI player.
     */
    public boolean isComputerControlled() {
        return computerControlled;
    }
}
