package carcassonne.model.tile;

import carcassonne.model.TerrainType;

/**
 * Factory class for building tile objects. Used this because there are so many types of tile with
 * little differences.
 * @author Timur
 */
public class TileFactory {

    /**
     * Factory method of the class. Produces a specific tile for a specific tile type.
     * @param type is the tile type.
     * @return the tile object.
     */
    public static Tile createTile(TileType type) {
        TerrainType standard = TerrainType.OTHER; // the standard terrain type for the default case.
        TerrainType castle = TerrainType.CASTLE;
        TerrainType road = TerrainType.ROAD;
        TerrainType monastry = TerrainType.MONASTRY;
        TerrainType fields = TerrainType.FIELDS;
        String path = "src/main/ressources/tiles/" + type.name() + ".jpg";
        switch (type) {
        case CastleCenter:
            return new Tile(castle, castle, castle, castle, castle, path, type);
        case CastleCenterEntry:
            return new Tile(castle, castle, road, castle, castle, path, type);
        case CastleCenterSide:
            return new Tile(castle, castle, fields, castle, castle, path, type);
        case CastleEdge:
            return new Tile(castle, castle, fields, fields, castle, path, type);
        case CastleEdgeRoad:
            return new Tile(castle, castle, road, road, standard, path, type);
        case CastleSides:
            return new Tile(castle, fields, castle, fields, fields, path, type);
        case CastleSidesEdge:
            return new Tile(castle, fields, fields, castle, fields, path, type);
        case CastleTube:
            return new Tile(fields, castle, fields, castle, fields, path, type);
        case CastleWall:
            return new Tile(castle, fields, fields, fields, fields, path, type);
        case CastleWallCurveLeft:
            return new Tile(castle, fields, road, road, road, path, type);
        case CastleWallCurveRight:
            return new Tile(castle, road, road, fields, road, path, type);
        case CastleWallJunction:
            return new Tile(castle, road, road, road, fields, path, type);
        case CastleWallRoad:
            return new Tile(castle, road, fields, road, road, path, type);
        case Monastry:
            return new Tile(fields, fields, fields, fields, monastry, path, type);
        case MonastryRoad:
            return new Tile(fields, fields, road, fields, monastry, path, type);
        case Road:
            return new Tile(road, fields, road, fields, road, path, type);
        case RoadCurve:
            return new Tile(fields, fields, road, road, road, path, type);
        case RoadJunctionLarge:
            return new Tile(road, road, road, road, fields, path, type);
        case RoadJunctionSmall:
            return new Tile(fields, road, road, road, road, path, type);
        case Null:
        default:
            return new Tile(standard, standard, standard, standard, standard, path, type);
        }

    }
}
