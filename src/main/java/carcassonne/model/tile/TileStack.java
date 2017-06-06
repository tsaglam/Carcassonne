package carcassonne.model.tile;

import java.util.ArrayList;
import java.util.List;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final List<Tile> tileList;

    /**
     * Simple constructor.
     */
    public TileStack() {
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

    private void fillStack() { // TODO (HIGHEST) replace temporary method
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleCenter());
        }
        for (int i = 0; i < 4; i++) {
            tileList.add(new CastleCenterEntry());
        }
        for (int i = 0; i < 5; i++) {
            tileList.add(new CastleCenterSide());
        }
        for (int i = 0; i < 5; i++) {
            tileList.add(new CastleEdge());
        }
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleEdgeRoad());
        }
        for (int i = 0; i < 2; i++) {
            tileList.add(new CastleSides());
        }
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleSidesEdge());
        }
        for (int i = 0; i < 5; i++) {
            tileList.add(new CastleTube());
        }
        for (int i = 0; i < 1; i++) {
            tileList.add(new CastleWall());
        }
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleWallCurveLeft());
        }
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleWallCurveRight());
        }
        for (int i = 0; i < 3; i++) {
            tileList.add(new CastleWallJunction());
        }
        for (int i = 0; i < 4; i++) {
            tileList.add(new CastleWallRoad());
        }
        for (int i = 0; i < 4; i++) {
            tileList.add(new Monastery());
        }
        for (int i = 0; i < 2; i++) {
            tileList.add(new MonasteryRoad());
        }
        for (int i = 0; i < 8; i++) {
            tileList.add(new Road());
        }
        for (int i = 0; i < 9; i++) {
            tileList.add(new RoadCurve());
        }
        for (int i = 0; i < 1; i++) {
            tileList.add(new RoadJunctionLarge());
        }
        for (int i = 0; i < 4; i++) {
            tileList.add(new RoadJunctionSmall());
        }
    }
}
