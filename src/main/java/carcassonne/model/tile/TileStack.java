package carcassonne.model.tile;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.Stack;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final Stack<Tile> tiles;
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
        fillStack();

    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null;
        }
        return tiles.pop();
    }

    /**
     * Getter for the size of the stack.
     * @return the amount of tiled on the stack.
     */
    public int getSize() {
        return tiles.size();
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    private void fillStack() {
        for (TileType tileType : TileType.validTiles()) {
            int amount = getAmount(tileType); // call once per tile type
            for (int i = 0; i < amount; i++) {
                tiles.add(new Tile(tileType));
            }
        }
        Collections.shuffle(tiles);
    }

    private int getAmount(TileType tileType) {
        double amount;
        if (useFixedAmounts) {
            amount = Math.round(tileType.getAmount());
        } else {
            amount = getRandomAmount(tileType);
        }
        return (int) (amount * multiplicator); // scale stack to player size
    }

    private double getRandomAmount(TileType tileType) {
        if (randomAmounts == null) { // if random amounts where not generated
            randomAmounts = new Stack<>(); // get all standard amounts in a stack
            randomAmounts.addAll(TileType.validTiles().stream().map(it -> it.getAmount()).collect(toList()));
            Collections.shuffle(randomAmounts);
        }
        return randomAmounts.pop(); // return random amount
    }
}
