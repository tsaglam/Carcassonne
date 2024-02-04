package carcassonne.model.tile;

import static carcassonne.model.terrain.TerrainType.CASTLE;
import static carcassonne.model.terrain.TerrainType.FIELDS;
import static carcassonne.model.terrain.TerrainType.MONASTERY;
import static carcassonne.model.terrain.TerrainType.OTHER;
import static carcassonne.model.terrain.TerrainType.ROAD;
import static java.util.Arrays.stream;

import java.util.List;
import java.util.Locale;

import carcassonne.model.terrain.TerrainType;

/**
 * Enumeration for the specific type of tiles. It allows easy construction of tile objects. This enum show the entirety
 * of hard-coded data in this game. Everything else is algorithmically calculated from this data (e.g. tile placement,
 * emblems, pattern completion). Values use lower case for easy image importing.
 * @author Timur
 */
public enum TileType { // TODO (MEDIUM) [STYLE] rename enum values and tile image resources.
    Null(0, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER, OTHER),
    CastleCenter(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE),
    CastleCenterEntry(3, CASTLE, CASTLE, ROAD, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleCenterSide(4, CASTLE, CASTLE, FIELDS, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, CASTLE),
    CastleEdge(5, CASTLE, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleEdgeRoad(4, CASTLE, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, ROAD),
    CastleSides(3, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleSidesEdge(2, CASTLE, FIELDS, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, FIELDS),
    CastleTube(2, FIELDS, CASTLE, FIELDS, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleWall(5, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS),
    CastleWallCurveLeft(3, CASTLE, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallCurveRight(3, CASTLE, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallJunction(3, CASTLE, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    CastleWallRoad(4, CASTLE, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    Monastery(4, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    MonasteryRoad(2, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    Road(7, ROAD, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCurve(8, FIELDS, FIELDS, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadJunctionLarge(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadJunctionSmall(3, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    RoadCrossLarge(0, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    RoadCrossSmall(2, FIELDS, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleTubeEntries(1, ROAD, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleTubeEntry(1, FIELDS, CASTLE, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    MonasteryCastle(1, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, CASTLE, MONASTERY),
    MonasteryJunction(1, ROAD, ROAD, ROAD, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, MONASTERY),
    CastleSidesRoad(2, CASTLE, ROAD, CASTLE, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleSidesEdgeRoad(1, CASTLE, ROAD, ROAD, CASTLE, FIELDS, FIELDS, FIELDS, OTHER, ROAD),
    CastleSidesQuad(1, CASTLE, CASTLE, CASTLE, CASTLE, OTHER, OTHER, OTHER, OTHER, FIELDS),
    RoadEnd(1, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, OTHER),
    CastleCenterSides(1, FIELDS, FIELDS, CASTLE, FIELDS, OTHER, OTHER, OTHER, OTHER, CASTLE),
    CastleMini(1, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, CASTLE),
    CastleWallEntryLeft(1, CASTLE, FIELDS, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallEntryRight(1, CASTLE, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD),
    CastleWallEntry(2, CASTLE, FIELDS, ROAD, FIELDS, FIELDS, FIELDS, FIELDS, FIELDS, ROAD);

    private final TerrainType[] terrain;
    private final int amount;

    /**
     * Basic constructor for the terrain type, sets the terrain and the amount.
     * @param amount is the standard amount of tiles of this type in a stack.
     * @param terrain are the terrain types on the tiles of this tile type
     */
    TileType(int amount, TerrainType... terrain) {
        this.amount = amount;
        this.terrain = terrain;
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
     * Returns the tile type name with spaces between names in lower case.
     * @return the readable representation, such as "castle wall".
     */
    public String readableRepresentation() {
        return toString().replaceAll("([^_])([A-Z])", "$1 $2").toLowerCase(Locale.UK);
    }

    /**
     * Returns a list of all valid tile types.
     * @return all tile types except {@link TileType#Null}.
     */
    public static List<TileType> validTiles() {
        return stream(values()).filter(it -> it != Null).toList();
    }

    /**
     * Returns a list of all valid tiles that are available in game.
     * @return the list of enabled tiles.
     */
    public static List<TileType> enabledTiles() {
        return stream(values()).filter(it -> it != Null && it.getAmount() > 0).toList();
    }

}
