package carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import carcassonne.control.state.AbstractGameState;
import carcassonne.settings.GameSettings;
import carcassonne.testutils.TestGame;

abstract class CarcassonneTest {

    protected TestGame game;
    protected GameSettings settings;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println("Starting Test: " + testInfo.getDisplayName());
        game = new TestGame(true);
        settings = game.getSettings();
        settings.setPlayerComputerControlled(false, 1);
    }

    protected void assertState(Class<? extends AbstractGameState> expectedState) {
        Class<? extends AbstractGameState> actualState = game.getState();
        assertEquals(expectedState, actualState, "Expected state " + expectedState.getSimpleName() + " but got " + actualState.getSimpleName());
    }
}
