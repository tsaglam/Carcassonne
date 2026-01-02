package carcassonne.model.tile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Encapsulates a {@link TileType} distribution for a {@link TileStack}. Does not contain any tiles.
 * @author Timur Saglam
 */
public class TileDistribution {
    private static final int MAXIMAL_ATTEMPTS = 5;
    private final Map<TileType, Integer> distribution;
    private final Map<TileType, Integer> restorationPoint;

    /**
     * Creates a default distribution.
     * @see TileType
     */
    public TileDistribution() {
        distribution = new HashMap<>();
        restorationPoint = new HashMap<>();
    }

    /**
     * Gives information about the quantity of a tile type in this distribution.
     * @param tileType is the specific tile type.
     * @return number of tiles for this type.
     */
    public int getQuantity(TileType tileType) {
        if (distribution.containsKey(tileType)) {
            return distribution.get(tileType);
        }
        return tileType.getAmount(); // default amount if not present
    }

    /**
     * Sets the quantity for a tile type.
     * @param tileType is the tile type whose quantity is changed.
     * @param quantity is the number of tiles for the tile type.
     */
    public void setQuantity(TileType tileType, int quantity) {
        distribution.put(tileType, quantity);
    }

    /**
     * Creates a restoration point, allowing to reset the distribution to its current state.
     */
    public void createBackup() {
        restorationPoint.clear();
        restorationPoint.putAll(distribution);
    }

    /**
     * Restores the distribution to the state at the last restoration point. Restores default distribution if no restoration
     * point has been created.
     */
    public void restoreLastBackup() {
        distribution.clear();
        distribution.putAll(restorationPoint);
    }

    /**
     * Resets the distribution to the default distribution.
     */
    public void reset() {
        distribution.clear();
    }

    /**
     * Shuffles the tile amounts. The shuffle is not completely random as it tries to avoid giving a tile type its original
     * amount.
     */
    public void shuffle() {
        TileType.enabledTiles().forEach(it -> distribution.putIfAbsent(it, it.getAmount()));
        Stack<Integer> tileAmounts = new Stack<>();
        tileAmounts.addAll(distribution.values());
        Collections.shuffle(tileAmounts);
        TileType.enabledTiles().forEach(it -> distribution.put(it, getPseudoRandomAmount(it, tileAmounts)));
    }

    /**
     * Chooses a pseudo-random amount from a stack of amounts for a certain tile type.
     */
    private int getPseudoRandomAmount(TileType tileType, Stack<Integer> randomAmounts) {
        int amount = randomAmounts.peek();
        int attempts = 0;
        while (amount == tileType.getAmount() && attempts < MAXIMAL_ATTEMPTS) {
            Collections.shuffle(randomAmounts);
            amount = randomAmounts.peek();
            attempts++;
        }
        return randomAmounts.pop();
    }
}
