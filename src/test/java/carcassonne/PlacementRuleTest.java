package carcassonne;

import static carcassonne.model.tile.TileRotation.TILTED_LEFT;
import static carcassonne.model.tile.TileRotation.TILTED_RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import carcassonne.control.state.StateManning;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;

public class PlacementRuleTest extends CarcassonneTest { // TODO add tests for meeple placement

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("Setting rule on fortifying own patterns")
    void patternFortificationTest(boolean allowFortifying) {
        // Setup:
        settings.setAllowFortifying(allowFortifying);
        assertEquals(allowFortifying, settings.isAllowingFortifying());

        game.newRound(2, 4, 2);
        game.placeTileWithoutMeeple(TileType.Road, TILTED_RIGHT, 0, 0);
        game.placeTileWithoutMeeple(TileType.Road, TILTED_RIGHT, 2, 0);

        // Player 1 places left castle:
        game.placeTileAndMeeple(TileType.CastleWall, TILTED_RIGHT, 0, 1, GridDirection.EAST);

        // Player 2 places right castle:
        game.placeTileAndMeeple(TileType.CastleWall, TILTED_LEFT, 2, 1, GridDirection.WEST);

        // Player 1 connects (place without meeple)
        game.placeTile(TileType.CastleTube, 1, 1);
        assertState(StateManning.class);

        // Retrieve the actual placed tile to test allowsPlacingMeeple(...)
        Tile connectingTile = game.getGrid().getSpot(1, 1).getTile();

        // Check if fortifying your own pattern is allowed:
        assertEquals(allowFortifying, connectingTile.allowsPlacingMeeple(GridDirection.CENTER, game.getRound().getPlayer(0), game.getSettings()));
        assertEquals(allowFortifying, connectingTile.allowsPlacingMeeple(GridDirection.CENTER, game.getRound().getPlayer(1), game.getSettings()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("Setting rule on enclosing free spots.")
    void enclaveBuildingTest(boolean allowEnclaves) {
        // Setup:
        settings.setAllowEnclaves(allowEnclaves);
        assertEquals(allowEnclaves, settings.isAllowingEnclaves());
        game.newRound(2, 3, 4);

        // Setting reflected in grid:
        Grid grid = game.getGrid();
        assertEquals(allowEnclaves, grid.isAllowingEnclaves());

        // Prepare enclosing a free spot at (1|2):
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.TILTED_LEFT, 0, 1);
        game.placeTileWithoutMeeple(TileType.RoadCurve, 2, 1);
        game.placeTileWithoutMeeple(TileType.Road, 0, 2);
        game.placeTileWithoutMeeple(TileType.Road, 2, 2);
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.UPSIDE_DOWN, 0, 3);
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.TILTED_RIGHT, 2, 3);

        assertTrue(grid.getSpot(1, 3).isFree());
        assertEquals(allowEnclaves, grid.place(1, 3, new Tile(TileType.RoadCrossLarge)));
    }

    @Test
    @DisplayName("Placing a tile without any neighboring tiles")
    void testPlacingWithoutNeighborsIsNotAllowed() {
        game.newRound(2, 2, 2);
        Grid grid = game.getGrid();
        assertFalse(grid.place(1, 1, new Tile(TileType.Monastery)));
        assertTrue(grid.getSpot(1, 1).isFree());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1})
    @DisplayName("Placing a tile outside the grid")
    void testPlacingOutsideGridIsNotAllowed(int invalid) {
        game.newRound(2, 1, 1);
        Grid grid = game.getGrid();
        Tile tile = new Tile(TileType.Road);
        assertThrows(IllegalArgumentException.class, () -> grid.place(invalid, 0, tile));
        assertThrows(IllegalArgumentException.class, () -> grid.place(0, invalid, tile));
    }

    @Test
    @DisplayName("Placing a tile with mismatching terrain")
    void testTerrainMismatchIsNotAllowed() {
        game.newRound(2, 3, 3);
        Grid grid = game.getGrid();
        assertFalse(grid.place(1, 0, new Tile(TileType.Monastery)));
        assertFalse(grid.place(0, 1, new Tile(TileType.CastleCenter)));
        assertFalse(grid.place(0, 2, new Tile(TileType.CastleCenter)));
        assertFalse(grid.place(2, 0, new Tile(TileType.CastleCenter)));
    }

    @Test
    @DisplayName("Placing a tile on an occupied spot")
    void testPlacingOnOccupiedSpotIsNotAllowed() {
        game.newRound(2, 1, 1);
        Grid grid = game.getGrid();
        assertFalse(grid.place(0, 0, new Tile(TileType.RoadCurve)));
        assertEquals(TileType.CastleWallRoad, grid.getSpot(0, 0).getTile().getType());
    }
}
