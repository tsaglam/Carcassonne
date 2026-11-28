package carcassonne.model.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;

/**
 * Tests for the rotations of {@link Tile}s, in particular the rotations offered by {@link Tile#getPossibleRotations()}
 * which are used to generate the moves of computer-controlled players.
 * @author Timur Saglam
 */
public class TileRotationTest {

    private static final String MISSING_ORIENTATION = "Possible rotations %s of %s do not cover the orientation %s";

    private static Stream<TileType> enabledTiles() {
        return TileType.enabledTiles().stream();
    }

    @ParameterizedTest
    @MethodSource("enabledTiles")
    @DisplayName("Possible rotations match the number of distinct tile images")
    void testPossibleRotationsMatchRotationLimit(TileType type) {
        Tile tile = new Tile(type);
        assertEquals(TileUtil.rotationLimitFor(type), tile.getPossibleRotations().size(), "Wrong possible rotations for " + type);
    }

    @ParameterizedTest
    @MethodSource("enabledTiles")
    @DisplayName("Possible rotations cover every terrain orientation of a tile")
    void testPossibleRotationsCoverAllOrientations(TileType type) {
        Collection<TileRotation> possibleRotations = new Tile(type).getPossibleRotations();
        List<List<TerrainType>> coveredOrientations = new ArrayList<>();
        for (TileRotation rotation : possibleRotations) {
            coveredOrientations.add(terrainOf(type, rotation));
        }
        for (TileRotation rotation : TileRotation.values()) {
            assertTrue(coveredOrientations.contains(terrainOf(type, rotation)), MISSING_ORIENTATION.formatted(possibleRotations, type, rotation));
        }
    }

    private static List<TerrainType> terrainOf(TileType type, TileRotation rotation) {
        Tile tile = new Tile(type);
        tile.rotateTo(rotation);
        return Stream.of(GridDirection.values()).map(tile::getTerrain).toList();
    }
}
