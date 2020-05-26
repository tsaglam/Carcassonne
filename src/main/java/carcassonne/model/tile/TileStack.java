package carcassonne.model.tile;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final Stack<Tile> tiles;
    private final Queue<Tile> returnedTiles;
    private Stack<Integer> randomAmounts;
    private final double multiplicator;
    private final boolean useFixedAmounts;

    /**
     * Simple constructor, creates the default fixed-amount stack.
     * @param players is the amount of player for which this tile stack is intended.
     */
    public TileStack(int players) {
        this(players, false);
    }

    /**
     * Constructor which allows specifying the stack type.
     * @param players is the amount of player for which this tile stack is intended.
     * @param useFixedAmounts specifies whether the amount of the different tile types should be fixed or random.
     */
    public TileStack(int players, boolean useFixedAmounts) {
        this.useFixedAmounts = useFixedAmounts;
        multiplicator = 0.4 + players * 0.3;
        tiles = new Stack<>();
        returnedTiles = new LinkedList<>();
        fillStack();
        rotateRandomly();
        Collections.shuffle(tiles);
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            if (returnedTiles.isEmpty()) {
                return null;
            } else {
                return returnedTiles.poll();
            }
        }
        return tiles.pop();
    }

    /**
     * Returns a tile that is not placed under the stack.
     * @param tile is the tile to put back under the stack.
     */
    public void putBack(Tile tile) {
        if (tile.isPlaced()) {
            throw new IllegalArgumentException("Cannot return a placed tile!");
        }
        if (!tiles.isEmpty()) {
            returnedTiles.add(tile); // tiles can only be returned once!
        }
    }

    /**
     * Getter for the size of the stack.
     * @return the amount of tiled on the stack.
     */
    public int getSize() {
        return tiles.size() + returnedTiles.size();
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tiles.isEmpty() && returnedTiles.isEmpty();
    }

    private void fillStack() {
        for (TileType tileType : TileType.validTiles()) {
            int amount = getAmount(tileType); // call once per tile type
            for (int i = 0; i < amount; i++) {
                tiles.add(new Tile(tileType));
            }
        }
    }

    private int getAmount(TileType tileType) {
        double amount;
        if (useFixedAmounts) {
            amount = Math.round(tileType.getAmount());
        } else {
            amount = getRandomAmount(tileType, 5);
        }
        return (int) (amount * multiplicator); // scale stack to player size
    }

    // assigns a somewhat random amount of tiles
    private double getRandomAmount(TileType tileType, int shuffles) {
        ensureInitialization();
        int amount = randomAmounts.pop();
        if (amount == tileType.getAmount()) { // if amount is the normal one
            randomAmounts.push(amount); // put amount back
            return getPseudoRandomAmount(tileType, shuffles - 1); // get pseudo random amount
        }
        return amount; // return random amount
    }

    // re-shuffles the stack and tries again. Use random number between 1 and 8 after a certain amount of tries.
    private double getPseudoRandomAmount(TileType tileType, int shuffles) {
        if (shuffles > 0) {
            Collections.shuffle(randomAmounts);
            return getRandomAmount(tileType, shuffles - 1);
        } else {
            return (int) (1 + Math.random() * 7);
        }
    }

    // makes sure the random amounts are initialized.
    private void ensureInitialization() {
        if (randomAmounts == null) { // if random amounts where not generated
            randomAmounts = new Stack<>(); // get all standard amounts in a stack
            randomAmounts.addAll(TileType.validTiles().stream().map(it -> it.getAmount()).collect(toList()));
            Collections.shuffle(randomAmounts);
        }
    }

    private void rotateRandomly() {
        for (Tile tile : tiles) {
            for (int i = 0; i < Math.round(Math.random() * 4 - 0.5); i++) {
                tile.rotateRight(); // Random orientation with equal chance for each orientation.
            }
        }
    }
}
