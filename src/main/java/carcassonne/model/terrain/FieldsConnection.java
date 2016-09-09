package carcassonne.model.terrain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

public class FieldsConnection {

    Map<GridDirection, FieldsCorner> cornerMap;
    Set<GridDirection> directionSet;

    public FieldsConnection(Tile tile, GridDirection... directions) {
        cornerMap = new HashMap<GridDirection, FieldsCorner>();
        directionSet = new HashSet<GridDirection>();
        for (GridDirection direction : directions) {
            directionSet.add(direction);
            if (Arrays.asList(GridDirection.indirectNeighbors()).contains(direction)) {
                cornerMap.put(direction, new FieldsCorner(direction, tile));
            }
        }
    }

}
