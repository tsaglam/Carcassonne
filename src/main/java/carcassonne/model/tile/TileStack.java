package carcassonne.model.tile;

import java.util.ArrayList;

/**
 * The stack of tiles for a game.
 * @author Timur
 */
public class TileStack {
    ArrayList<Tile> tileList;

    public TileStack() {
        tileList = new ArrayList<Tile>();
        fillStack();
    }

    private void fillStack() {
        int[] tileAmount = { 1, 3, 4, 5, 5, 3, 2, 3, 5, 3, 3, 3, 4, 4, 2, 0, 8, 9, 1, 4 };
        for (TileType tileType : TileType.values()) {
            for (int i = 0; i < tileAmount[tileType.ordinal()]; i++) {
                tileList.add(TileFactory.createTile(tileType));
            }
        }
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
}
