package carcassonne.model.tile;

import static carcassonne.model.tile.TerrainType.CASTLE;
import static carcassonne.model.tile.TerrainType.FIELDS;
import static carcassonne.model.tile.TerrainType.MONASTERY;
import static carcassonne.model.tile.TerrainType.OTHER;
import static carcassonne.model.tile.TerrainType.ROAD;

/**
 * Factory class for building tile objects. Because of the large amount of different tile objects
 * this factory class enables easy tile creation with the <code>TileType</code> enum.
 * @author Timur Saglam
 */
public final class TileFactory {
    private static final String FOLDER = "src/main/ressources/tiles/";
    private static final String FILE_TYPE = ".jpg";

    /**
     * Factory method of the class. Produces a specific tile for a specific tile type.
     * @param type is the tile type.
     * @return the tile object.
     */
    public static Tile create(TileType type) {
        if (type == null) { // null is invalid argument.
            throw new IllegalArgumentException("TileFactory can't create tile from TileType value null.");
        }
        switch (type) {
        case CastleCenter:
            return produce(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, type);
        case CastleCenterEntry:
            return produce(CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE, type);
        case CastleCenterSide:
            return produce(CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE, type);
        case CastleEdge:
            return produce(CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, type);
        case CastleEdgeRoad:
            return produce(CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD, type);
        case CastleSides:
            return produce(CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, type);
        case CastleSidesEdge:
            return produce(CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS, type);
        case CastleTube:
            return produce(FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE, type);
        case CastleWall:
            return produce(CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, type);
        case CastleWallCurveLeft:
            return produce(CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD, type);
        case CastleWallCurveRight:
            return produce(CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD, type);
        case CastleWallJunction:
            return produce(CASTLE, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER, type);
        case CastleWallRoad:
            return produce(CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD, type);
        case Monastery:
            return produce(FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY, type);
        case MonasteryRoad:
            return produce(FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY, type);
        case Road:
            return produce(ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD, type);
        case RoadCurve:
            return produce(FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD, type);
        case RoadJunctionLarge:
            return produce(ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER, type);
        case RoadJunctionSmall:
            return produce(FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER, type);
        default:
            return produce(OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, type);
        }
    }

    // fills array, actually creates tile object with type and path.
    private static Tile produce(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType topRight, TerrainType bottomRight, TerrainType bottomLeft, TerrainType topLeft,
            TerrainType middle, TileType type) {
        TerrainType[] terrain = { top, right, bottom, left, topRight, bottomRight, bottomLeft, topLeft, middle };
        String path = FOLDER + type.name(); // generate path.
        return new Tile(terrain, type, path, FILE_TYPE);
    }
    
    // TODO (HIGHEST) use this!!!
    private static Tile produce(TileType type, TerrainType... terrain ) {
        String path = FOLDER + type.name(); // generate path.
        return new Tile(terrain, type, path, FILE_TYPE);
    }

    private TileFactory() {
        // PRIVATE CONSTRUCTOR, PREVENTS INSTANTIATION!
    }
}
