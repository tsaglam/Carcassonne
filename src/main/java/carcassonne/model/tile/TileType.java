package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

import carcassonne.model.terrain.TerrainType;

/**
 * Enum for the specific type of tiles. this is needed for the tile factory. It allows easy construction of tile
 * objects. Values use lower case for easy image importing. TODO (HIGH) rename enum values and tile image resources.
 * @author Timur
 */
public enum TileType {
    Null(0, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER),
    CastleCenter(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE),
    CastleCenterEntry(3, CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleCenterSide(4, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleEdge(5, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleEdgeRoad(5, CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD),
    CastleSides(3, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleSidesEdge(2, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS),
    CastleTube(2, FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleWall(5, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleWallCurveLeft(3, CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallCurveRight(3, CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallJunction(3, CASTLE, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    CastleWallRoad(4, CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    Monastery(3, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    MonasteryRoad(3, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    Road(7, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCurve(8, FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadJunctionLarge(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadJunctionSmall(3, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadCrossLarge(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCrossSmall(2, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleTubeEntries(1, ROAD, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleTubeEntry(1, FIELDS, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    MonasteryCastle(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, MONASTERY),
    MonasteryJunction(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    CastleSidesRoad(2, CASTLE, ROAD, CASTLE, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleSidesEdgeRoad(1, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, ROAD),
    CastleSidesQuad(1, CASTLE, CASTLE, CASTLE, CASTLE, OTHER, OTHER, OTHER, OTHER, FIELDS),
    RoadEnd(2, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    CastleCenterSides(1, FIELDS, FIELDS, CASTLE, FIELDS, OTHER, OTHER, OTHER, OTHER, CASTLE),
    CastleMini(1, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleWallEntryLeft(1, CASTLE, FIELDS, FIELDS, ROAD, OTHER, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallEntryRight(1, CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, OTHER, ROAD),
    CastleWallEntry(2, CASTLE, FIELDS, ROAD, FIELDS, OTHER, FIELDS, FIELDS, OTHER, ROAD);

    private final TerrainType[] terrain;
    private final int amount;

    /**
     * Basic constructor for the terrain type, sets the terrain and the amount.
     * @param amount is the standard amount of tiles of this type in a stack.
     * @param terrain are the terrain types on the tiles of this tile type
     */
    private TileType(int amount, TerrainType... terrain) {
        this.amount = amount;
        this.terrain = terrain;
    }

    public boolean contains(TerrainType terrainType) {
        for (TerrainType tileTerrain : terrain) {
            if (tileTerrain.equals(terrainType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter for the standard amount of tiles of this type in a stack.
     * @return the amount of tiles.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Getter for the terrain types on the tiles of this tile type.
     * @return the terrain types of the tile.
     */
    public TerrainType[] getTerrain() {
        return terrain;
    }

    /**
     * Returns a list of all valid tile types.
     * @return all tile types except {@link TileType#Null}.
     */
    public static List<TileType> validTiles() {
        return stream(values()).filter(it -> it != Null).collect(toList());
    }

}
