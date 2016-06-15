package carcassonne.model;

import java.util.ArrayList;

/**
 * The stack of tiles for a game.
 * @author Timur
 */
public class TileStack {
    ArrayList<Tile> tileList;

    public TileStack() {
        tileList = new ArrayList<Tile>();
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile
     */
    public Tile drawTile() {
        return tileList.remove((int) Math.round(Math.random() * tileList.size()));
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tileList.isEmpty();
    }
}
