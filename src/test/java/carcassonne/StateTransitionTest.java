package carcassonne;

import static carcassonne.model.tile.TileRotation.TILTED_RIGHT;
import static carcassonne.model.tile.TileRotation.UPSIDE_DOWN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.control.state.StateManning;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TileType;

/**
 * Tests regarding the transitions between game states.
 */
public class StateTransitionTest extends CarcassonneTest {

    @Test
    @DisplayName("Idle to Placing via start new round")
    void testIdleToPlacing() {
        assertState(StateIdle.class);
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Placing to Manning via place tile")
    void testPlacingToManning() {
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
    }

    @Test
    @DisplayName("Manning to Placing via place meeple")
    void testManningToPlacingViaMeeple() {
        game.newRound(2, 3, 3);
        game.placeTile(TileType.Road, TILTED_RIGHT, 2, 1);
        assertState(StateManning.class);
        game.state().placeMeeple(GridDirection.CENTER);
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Manning to Placing via skip meeple")
    void testManningToPlacingViaSkip() {
        game.newRound(2, 3, 3);
        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
        game.skipMeeplePlacement();
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Placing to Placing via skip tile")
    void testPlacingToPlacingViaSkip() {
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        game.skipTilePlacement();
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Placing to GameOver via abort round")
    void testPlacingToGameOverViaAbort() {
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        game.abort();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }

    @Test
    @DisplayName("Manning to GameOver via abort round")
    void testManningToGameOverViaAbort() {
        game.newRound(2, 3, 3);
        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
        game.abort();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }

    @Test
    @DisplayName("Manning to GameOver via round over")
    void testManningToGameOverViaRoundOver() {
        game.newRound(2, 1, 2);
        game.placeTile(TileType.Monastery, 0, 1);
        assertState(StateManning.class);
        game.skipMeeplePlacement();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }

    @Test
    @DisplayName("GameOver to Idle via exit statistics")
    void testGameOverToIdle() {
        game.newRound(2, 3, 3);
        game.abort();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }

    @Test
    @DisplayName("GameOver to Placing via start new round")
    void testGameOverToPlacing() {
        game.newRound(2, 3, 3);
        game.abort();
        assertState(StateGameOver.class);
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Multiple transitions: complete game flow")
    void testCompleteGameFlow() {
        assertState(StateIdle.class);
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);

        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
        game.skipMeeplePlacement();
        assertState(StatePlacing.class);
        game.placeTileAndMeeple(TileType.CastleWall, UPSIDE_DOWN, 1, 0, GridDirection.SOUTH);
        assertState(StatePlacing.class);

        game.abort();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }

    @Test
    @DisplayName("Round completion with castle")
    void testRoundCompletionWithCastle() {
        game.newRound(2, 1, 3);
        game.placeTileWithoutMeeple(TileType.Monastery, 0, 2);
        assertState(StatePlacing.class);
        game.placeTileAndMeeple(TileType.CastleWall, UPSIDE_DOWN, 0, 0, GridDirection.SOUTH);
        assertState(StateGameOver.class);
    }

    @Test
    @DisplayName("Placing self-loop via invalid skip")
    void testPlacingSelfLoop() {
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        game.skipTilePlacement();
        assertState(StatePlacing.class);
        game.skipTilePlacement();
        assertState(StatePlacing.class);
    }

    @Test
    @DisplayName("Abort from different states")
    void testAbortFromDifferentStates() {
        // From Placing
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        game.abort();
        assertState(StateGameOver.class);
        game.skipPostGameStatistics();

        // From Manning
        game.newRound(2, 3, 3);
        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
        game.abort();
        assertState(StateGameOver.class);
    }

    @Test
    @DisplayName("Multiple rounds in sequence")
    void testMultipleRounds() {
        for (int i = 0; i < 3; i++) {
            game.newRound(2, 3, 3);
            assertState(StatePlacing.class);
            game.placeTileWithoutMeeple(TileType.CastleWall, UPSIDE_DOWN, 1, 0);
            assertState(StatePlacing.class);
            game.abort();
            assertState(StateGameOver.class);
            game.skipPostGameStatistics();
            assertState(StateIdle.class);
        }
    }

    @Test
    @DisplayName("Invalid transitions: exceptions and no-ops")
    void testInvalidTransitions() {
        // State idle:
        assertState(StateIdle.class);
        game.state().skip();
        assertState(StateIdle.class);
        assertThrows(IllegalStateException.class, () -> game.state().placeMeeple(null));
        assertState(StateIdle.class);
        assertDoesNotThrow(() -> game.state().placeTile(0, 0));
        assertState(StateIdle.class);
        assertDoesNotThrow(() -> game.state().abortGame());
        assertState(StateIdle.class);

        // State placing
        game.newRound(2, 3, 3);
        assertState(StatePlacing.class);
        assertThrows(IllegalStateException.class, () -> game.state().placeMeeple(null));
        assertState(StatePlacing.class);
        assertDoesNotThrow(() -> game.state().newRound(2));
        assertState(StatePlacing.class);

        // State manning:
        game.placeTile(TileType.Road, TILTED_RIGHT, 1, 2);
        assertState(StateManning.class);
        assertDoesNotThrow(() -> game.state().placeTile(0, 0));
        assertState(StateManning.class);
        assertDoesNotThrow(() -> game.state().newRound(2));
        assertState(StateManning.class);

        // State game over:
        game.abort();
        assertState(StateGameOver.class);
        assertThrows(IllegalStateException.class, () -> game.state().placeMeeple(null));
        assertState(StateGameOver.class);
        assertDoesNotThrow(() -> game.state().placeTile(0, 0));
        assertState(StateGameOver.class);
        assertDoesNotThrow(() -> game.abort());
        assertState(StateGameOver.class);

        // Back to idle:
        game.skipPostGameStatistics();
        assertState(StateIdle.class);
    }
}
