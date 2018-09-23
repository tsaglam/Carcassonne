package carcassonne.model.tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final List<Tile> tileList;
    private final int multiplicator;

    /**
     * Simple constructor.
     */
    public TileStack(int players) {
        multiplicator = players / 2;
        tileList = new ArrayList<Tile>();
        fillStack();
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tileList.isEmpty()) {
            return null;
        }
        return tileList.remove((int) Math.round(Math.random() * (tileList.size() - 1)));
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tileList.isEmpty();
    }

    private void fillStack() {
        int[] tileAmount = { 1, 3, 4, 5, 5, 3, 2, 3, 5, 3, 3, 3, 4, 4, 2, 0, 7, 8, 2, 4, 1, 2, 2, 1};
        for (TileType tileType : TileType.values()) {
            for (int i = 0; i < tileAmount[tileType.ordinal()] * multiplicator; i++) {
                tileList.add(TileFactory.create(tileType));
            }
        }
    }
}
