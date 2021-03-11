package carcassonne.model.grid;

import carcassonne.model.tile.Tile;

public class CarcassonneMove { // TODO (HIGH) Implement Carcassonne move!
    private Tile tile;
    private GridDirection position;

    public CarcassonneMove(Tile tile, GridDirection position) {
        super();
        this.tile = tile;
        this.position = position;
    }

    public Tile getTile() {
        return tile;
    }

    public GridDirection getPosition() {
        return position;
    }
    
    public double getPotentialValue() {
        return 0; // TODO (HIGH) generate rule-based evaluation.
    }

}
