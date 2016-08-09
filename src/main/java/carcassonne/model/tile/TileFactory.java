package carcassonne.model.tile;

import static carcassonne.model.tile.TerrainType.CASTLE;
import static carcassonne.model.tile.TerrainType.FIELDS;
import static carcassonne.model.tile.TerrainType.MONASTRY;
import static carcassonne.model.tile.TerrainType.OTHER;
import static carcassonne.model.tile.TerrainType.ROAD;

/**
 * Factory class for building tile objects. Used this because there are so many types of tile with
 * little differences.
 * @author Timur
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
        String path = FOLDER + type.name(); // generate path.
        switch (type) {
        case CastleCenter:
            return new Tile(CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, path, FILE_TYPE, type);
        case CastleCenterEntry:
            return new Tile(CASTLE, CASTLE, ROAD, CASTLE, CASTLE, path, FILE_TYPE, type);
        case CastleCenterSide:
            return new Tile(CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, path, FILE_TYPE, type);
        case CastleEdge:
            return new Tile(CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, path, FILE_TYPE, type);
        case CastleEdgeRoad:
            return new Tile(CASTLE, CASTLE, ROAD, ROAD, ROAD, path, FILE_TYPE, type);
        case CastleSides:
            return new Tile(CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, path, FILE_TYPE, type);
        case CastleSidesEdge:
            return new Tile(CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, path, FILE_TYPE, type);
        case CastleTube:
            return new Tile(FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, path, FILE_TYPE, type);
        case CastleWall:
            return new Tile(CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, path, FILE_TYPE, type);
        case CastleWallCurveLeft:
            return new Tile(CASTLE, FIELDS, ROAD, ROAD, ROAD, path, FILE_TYPE, type);
        case CastleWallCurveRight:
            return new Tile(CASTLE, ROAD, ROAD, FIELDS, ROAD, path, FILE_TYPE, type);
        case CastleWallJunction:
            return new Tile(CASTLE, ROAD, ROAD, ROAD, FIELDS, path, FILE_TYPE, type);
        case CastleWallRoad:
            return new Tile(CASTLE, ROAD, FIELDS, ROAD, ROAD, path, FILE_TYPE, type);
        case Monastry:
            return new Tile(FIELDS, FIELDS, FIELDS, FIELDS, MONASTRY, path, FILE_TYPE, type);
        case MonastryRoad:
            return new Tile(FIELDS, FIELDS, ROAD, FIELDS, MONASTRY, path, FILE_TYPE, type);
        case Road:
            return new Tile(ROAD, FIELDS, ROAD, FIELDS, ROAD, path, FILE_TYPE, type);
        case RoadCurve:
            return new Tile(FIELDS, FIELDS, ROAD, ROAD, ROAD, path, FILE_TYPE, type);
        case RoadJunctionLarge:
            return new Tile(ROAD, ROAD, ROAD, ROAD, FIELDS, path, FILE_TYPE, type);
        case RoadJunctionSmall:
            return new Tile(FIELDS, ROAD, ROAD, ROAD, ROAD, path, FILE_TYPE, type);
        default:
            return new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, path, FILE_TYPE, type);
        } 
    }
    
    private TileFactory() {
        // PRIVATE CONSTRUCTOR, PREVENTS INSTANTIATION!
    }
}
