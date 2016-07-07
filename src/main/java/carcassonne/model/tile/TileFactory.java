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
        switch (type) { // TODO implement real tile creation.
        case CastleCenter:
            return new Tile(castle, castle, castle, castle, castle);
        case CastleCenterEntry:
            return new Tile(castle, castle, road, castle, castle);
        case CastleCenterSide:
            return new Tile(castle, castle, fields, castle, castle);
        case CastleEdge:
            return new Tile(castle, castle, fields, fields, castle);
        case CastleEdgeRoad:
            return new Tile(castle, castle, road, road, standard);
        case CastleSides:
            return new Tile(castle, fields, castle, fields, fields);
        case CastleSidesEdge:
            return new Tile(castle, fields, fields, castle, fields);
        case CastleTube:
            return new Tile(fields, castle, fields, castle, fields);
        case CastleWall:
            return new Tile(castle, fields, fields, fields, fields);
        case CastleWallCurveLeft:
            return new Tile(castle, fields, road, road, road);
        case CastleWallCurveRight:
            return new Tile(castle, road, road, fields, road);
        case CastleWallJunction:
            return new Tile(castle, road, road, road, fields);
        case CastleWallRoad:
            return new Tile(castle, road, fields, road, road);
        case Monastry:
            return new Tile(fields, fields, fields, fields, monastry);
        case MonastryRoad:
            return new Tile(fields, fields, road, fields, monastry);
        case Road:
            return new Tile(road, fields, road, fields, road);
        case RoadCurve:
            return new Tile(fields, fields, road, road, road);
        case RoadJunctionLarge:
            return new Tile(road, road, road, road, fields);
        case RoadJunctionSmall:
            return new Tile(fields, road, road, road, road);
        case Null:
        default:
            return new Tile(standard, standard, standard, standard, standard);
        }

    }
}
