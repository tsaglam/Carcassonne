package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;

import carcassonne.model.terrain.TerrainType;

/**
 * Enum for the specific type of tiles. this is needed for the tile factory. It allows easy construction of tile
 * objects. Values use lower case for easy image importing. TODO (HIGH) rename enum values and tile image resources.
 * @author Timur
 */
public enum TileType {
    CastleCenter(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE),
    CastleCenterEntry(3, CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleCenterSide(4, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleEdge(5, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleEdgeRoad(5, CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD),
    CastleSides(3, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleSidesEdge(2, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS),
    CastleTube(3, FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleWall(5, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleWallCurveLeft(3, CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallCurveRight(3, CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallJunction(3, CASTLE, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    CastleWallRoad(4, CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    Monastery(4, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    MonasteryRoad(2, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    Null(0, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER),
    Road(7, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCurve(8, FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadJunctionLarge(2, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadJunctionSmall(4, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadCrossLarge(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCrossSmall(2, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleTubeEntry(2, ROAD, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    MonasteryCastle(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, MONASTERY);

    private final TerrainType[] terrain;
    private final int amount;

    private TileType(int amount, TerrainType... terrain) {
        this.amount = amount;
        this.terrain = terrain;
    }

    /**
     * Getter for the terrain types on the tile of this tile type.
     * @return the terrain types of the tile.
     */
    public TerrainType[] getTerrain() {
        return terrain;
    }

    /**
     * Getter for the standard amount of tiles of this type in a stack.
     * @return the amount of tiles.
     */
    public int getAmount() {
        return amount;
    }

}
