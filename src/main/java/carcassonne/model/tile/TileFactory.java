package carcassonne.model.tile;

import static carcassonne.model.TerrainType.CASTLE;
import static carcassonne.model.TerrainType.CASTLE_AND_ROAD;
import static carcassonne.model.TerrainType.FIELDS;
import static carcassonne.model.TerrainType.MONASTRY;
import static carcassonne.model.TerrainType.OTHER;
import static carcassonne.model.TerrainType.ROAD;

/**
 * Factory class for building tile objects. Used this because there are so many types of tile with
 * little differences.
 * @author Timur
 */
public final class TileFactory {

    /**
     * Factory method of the class. Produces a specific tile for a specific tile type.
     * @param type is the tile type.
     * @return the tile object.
     */
    public static Tile createTile(TileType type) {
        String path = "src/main/ressources/tiles/" + type.name() + ".jpg";
        switch (type) {
        case CastleCenter:
            return new Tile(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, path, type);
        case CastleCenterEntry:
            return new Tile(CASTLE, CASTLE, ROAD, CASTLE, CASTLE, path, type);
        case CastleCenterSide:
            return new Tile(CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, path, type);
        case CastleEdge:
            return new Tile(CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, path, type);
        case CastleEdgeRoad:
            return new Tile(CASTLE, CASTLE, ROAD, ROAD, CASTLE_AND_ROAD, path, type);
        case CastleSides:
            return new Tile(CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, path, type);
        case CastleSidesEdge:
            return new Tile(CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, path, type);
        case CastleTube:
            return new Tile(FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, path, type);
        case CastleWall:
            return new Tile(CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, path, type);
        case CastleWallCurveLeft:
            return new Tile(CASTLE, FIELDS, ROAD, ROAD, ROAD, path, type);
        case CastleWallCurveRight:
            return new Tile(CASTLE, ROAD, ROAD, FIELDS, ROAD, path, type);
        case CastleWallJunction:
            return new Tile(CASTLE, ROAD, ROAD, ROAD, FIELDS, path, type);
        case CastleWallRoad:
            return new Tile(CASTLE, ROAD, FIELDS, ROAD, ROAD, path, type);
        case Monastry:
            return new Tile(FIELDS, FIELDS, FIELDS, FIELDS, MONASTRY, path, type);
        case MonastryRoad:
            return new Tile(FIELDS, FIELDS, ROAD, FIELDS, MONASTRY, path, type);
        case Road:
            return new Tile(ROAD, FIELDS, ROAD, FIELDS, ROAD, path, type);
        case RoadCurve:
            return new Tile(FIELDS, FIELDS, ROAD, ROAD, ROAD, path, type);
        case RoadJunctionLarge:
            return new Tile(ROAD, ROAD, ROAD, ROAD, FIELDS, path, type);
        case RoadJunctionSmall:
            return new Tile(FIELDS, ROAD, ROAD, ROAD, ROAD, path, type);
        case Null:
        default:
            return new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, path, type);
        }

    }
}
