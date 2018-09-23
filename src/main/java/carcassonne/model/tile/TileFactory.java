package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;

import carcassonne.model.terrain.Terrain;
import carcassonne.model.terrain.TerrainType;

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
            return produce(type, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE);
        case CastleCenterEntry:
            return produce(type, CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE);
        case CastleCenterSide:
            return produce(type, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE);
        case CastleEdge:
            return produce(type, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS);
        case CastleEdgeRoad:
            return produce(type, CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD);
        case CastleSides:
            return produce(type, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS);
        case CastleSidesEdge:
            return produce(type, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS);
        case CastleTube:
            return produce(type, FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE);
        case CastleWall:
            return produce(type, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS);
        case CastleWallCurveLeft:
            return produce(type, CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case CastleWallCurveRight:
            return produce(type, CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case CastleWallJunction:
            return produce(type, CASTLE, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER);
        case CastleWallRoad:
            return produce(type, CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case Monastery:
            return produce(type, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY);
        case MonasteryRoad:
            return produce(type, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY);
        case Road:
            return produce(type, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case RoadCurve:
            return produce(type, FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case RoadJunctionLarge:
            return produce(type, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER);
        case RoadJunctionSmall:
            return produce(type, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER);
        case RoadCrossLarge:
            return produce(type, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case RoadCrossSmall:
            return produce(type, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);
        case CastleTubeEntry:
            return produce(type, ROAD, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE);
        default:
            return produce(type, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER);
        }
    }

    // fills array, actually creates tile object with type and path.
    private static Tile produce(TileType type, TerrainType... terrain) {
        String path = FOLDER + type.name(); // generate path.
        return new Tile(new Terrain(terrain), type, path, FILE_TYPE);
    }

    private TileFactory() {
        // PRIVATE CONSTRUCTOR, PREVENTS INSTANTIATION!
    }
}
