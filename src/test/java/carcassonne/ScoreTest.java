package carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;

/**
 * Tests related to the score calculation of grid patterns.
 */
public class ScoreTest extends CarcassonneTest {

    @Test
    @DisplayName("Completion of a monastery")
    void testMonasteryScoring() {
        game.newRound(2, 5, 4);

        game.placeTileAndMeeple(TileType.Monastery, 2, 2, GridDirection.CENTER);
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.TILTED_LEFT, 1, 1);
        game.placeTileWithoutMeeple(TileType.RoadCurve, 3, 1);
        game.placeTileWithoutMeeple(TileType.Road, 1, 2);
        game.placeTileWithoutMeeple(TileType.Road, 3, 2);
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.UPSIDE_DOWN, 1, 3);
        game.placeTileWithoutMeeple(TileType.Road, TileRotation.TILTED_LEFT, 2, 3);
        game.placeTileWithoutMeeple(TileType.RoadCurve, TileRotation.TILTED_RIGHT, 3, 3);
        assertState(StatePlacing.class);

        assertEquals(9, game.getRound().getPlayer(0).getScore());
    }

    @Test
    @DisplayName("Completion of starting tile castle")
    void testCastleCompletion() {
        // Setup:
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);

        // Complete castle with placed and starting tile:
        game.placeTileAndMeeple(TileType.CastleWall, TileRotation.UPSIDE_DOWN, 1, 0, GridDirection.SOUTH);
        assertState(StatePlacing.class);  // after meeple placement, state returns to Placing

        // Check score:
        assertEquals(0, game.getRound().getActivePlayer().getScore());
        assertEquals(4, game.getRound().getPlayer(0).getScore());

    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("2 players complete castle together")
    void testSharedCastleScoring(boolean splitScore) {
        int expectedScore = splitScore ? 3 : 6;
        settings.setSplitPatternScore(splitScore);

        // Setup:
        game.newRound(2, 3, 2);
        game.placeTileWithoutMeeple(TileType.Road, TileRotation.TILTED_RIGHT, 0, 0);
        game.placeTileWithoutMeeple(TileType.Road, TileRotation.TILTED_RIGHT, 2, 0);

        // Player 1 places left castle:
        game.placeTileAndMeeple(TileType.CastleWall, TileRotation.TILTED_RIGHT, 0, 1, GridDirection.EAST);

        // Player 2 places right castle:
        game.placeTileAndMeeple(TileType.CastleWall, TileRotation.TILTED_LEFT, 2, 1, GridDirection.WEST);

        // Player 1 connects:
        game.placeTileWithoutMeeple(TileType.CastleTube, 1, 1);
        assertState(StateGameOver.class);

        assertEquals(expectedScore, game.getRound().getPlayer(0).getScore());
        assertEquals(expectedScore, game.getRound().getPlayer(1).getScore());
    }

}
