package carcassonne;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.testutils.TestGame;

/**
 * Test for the computer-controlled player.
 */
public class AiPlayerTest extends CarcassonneTest {

    @BeforeEach
    @Override
    void setUp(TestInfo testInfo) {
        System.out.println("Starting Test: " + testInfo.getDisplayName());
        game = new TestGame(true);
        settings = game.getSettings();
        settings.setPlayerComputerControlled(true, 0);
    }

    @ParameterizedTest
    @MethodSource("gameScenarios")
    @DisplayName("AI completes various game scenarios")
    void testVariousGameScenarios(GameScenario scenario) {
        game.newRound(scenario.players, scenario.width, scenario.height);
        assertState(StateGameOver.class);

        game.skipPostGameStatistics();
        assertState(StateIdle.class);

        verifyAllPlayersScored(scenario.players);
    }

    private static Stream<Arguments> gameScenarios() {
        return Stream.of(Arguments.of(new GameScenario("tiny grid", 2, 3, 3)), //
                Arguments.of(new GameScenario("wide grid", 2, 25, 3)), //
                Arguments.of(new GameScenario("many players", 5, 28, 5)));
    }

    private void verifyAllPlayersScored(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            int playerIndex = i;
            assertTrue(game.getRound().getPlayer(playerIndex).getScore() > 0, () -> "Player " + playerIndex + " should have scored points");
        }
    }

    record GameScenario(String name, int players, int width, int height) {
        @Override
        public String toString() {
            return name + " (" + players + " players, " + width + "x" + height + ")";
        }
    }
}
