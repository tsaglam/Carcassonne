package carcassonne.model.tile;

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
    private final double multiplicator;

    /**
     * Simple constructor, creates the default fixed-amount stack.
     * @param players is the amount of player for which this tile stack is intended.
     * @param distribution is the tile distribution according which the stack is filled.
     */
    public TileStack(int players, TileDistribution distribution) {
        multiplicator = 0.4 + players * 0.3;
        tiles = new Stack<>();
        returnedTiles = new LinkedList<>();
        fillStack(distribution);
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

    private void fillStack(TileDistribution distribution) {
        for (TileType tileType : TileType.validTiles()) {
            int amount = (int) (distribution.getQuantity(tileType) * multiplicator);
            for (int i = 0; i < amount; i++) {
                tiles.add(new Tile(tileType));
            }
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
