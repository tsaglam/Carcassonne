package carcassonne.model.tile;

/**
 * Enum for the specific type of tiles. this is needed for the tile factory. It allows easy
 * construction of tile objects. Values use lower case for easy image importing. TODO (HIGH) rename
 * enum values and tile image resources.
 * @author Timur
 */
public enum TileType {
    CastleCenter, CastleCenterEntry, CastleCenterSide, CastleEdge, CastleEdgeRoad, CastleSides, CastleSidesEdge, CastleTube, CastleWall, CastleWallCurveLeft, CastleWallCurveRight, CastleWallJunction, CastleWallRoad, Monastery, MonasteryRoad, Null, Road, RoadCurve, RoadJunctionLarge, RoadJunctionSmall, RoadCrossLarge, RoadCrossSmall, CastleTubeEntry
}
