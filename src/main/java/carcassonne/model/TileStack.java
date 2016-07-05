package carcassonne.model;

import java.util.ArrayList;

import carcassonne.model.tile.Tile;

/**
 * The stack of tiles for a game.
 * @author Timur
 */
public class TileStack {
    ArrayList<Tile> tileList;

    public TileStack() {
        tileList = new ArrayList<Tile>();
        //TODO implement filling of the stack.
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tileList.isEmpty()) {
            return null;
        }
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
