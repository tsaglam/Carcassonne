/**
 * 
 */
package carcassonne.model.tile;

/**
 * Enum for the specific type of tiles. this is needed for the tile factory.
 * It allows easy construction of tile objects.
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
