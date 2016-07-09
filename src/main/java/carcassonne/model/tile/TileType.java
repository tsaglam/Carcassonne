/**
 * 
 */
package carcassonne.model.tile;

/**
 * Enum for the specific type of tiles. this is needed for the tile factory.
 * It allows easy construction of tile objects. 
 * Values use lower case for easy image importing. TODO maybe rename enum values and tile image resources.
 * @author Timur
 */
public enum TileType {
    CastleCenter,
    CastleCenterEntry,
    CastleCenterSide,
    CastleEdge,
    CastleEdgeRoad,
    CastleSides,
    CastleSidesEdge,
    CastleTube,
    CastleWall,
    CastleWallCurveLeft,
    CastleWallCurveRight,
    CastleWallJunction,
    CastleWallRoad,
    Monastry,
    MonastryRoad,
    Null,
    Road,
    RoadCurve,
    RoadJunctionLarge,
    RoadJunctionSmall 
}
