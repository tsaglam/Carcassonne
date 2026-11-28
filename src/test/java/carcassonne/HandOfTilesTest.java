package carcassonne;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.Player;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * Tests regarding the hand of tiles of the players.
 * @author Timur Saglam
 */
public class HandOfTilesTest extends CarcassonneTest {

    private static final int STACK_SIZE = 3;
    private static final int MAXIMAL_TURNS = 20;
    private static final int HUMAN_PLAYERS = 3;

    @Test
    @DisplayName("Dealing hands from a stack that is too small")
    void testDealingFromSmallStack() {
        startRoundWithSmallStack(2);

        int dealtTiles = 0;
        for (int i = 0; i < game.getRound().getPlayerCount(); i++) {
            Player player = game.getRound().getPlayer(i);
            assertFalse(player.getHandOfTiles().contains(null), "Hand of player " + i + " contains null tiles: " + player.getHandOfTiles());
            dealtTiles += player.getHandOfTiles().size();
        }
        assertEquals(STACK_SIZE, dealtTiles);
    }

    @Test
    @DisplayName("Playing out a round with players without tiles")
    void testPlayingOutSmallStack() {
        for (int player = 0; player < HUMAN_PLAYERS; player++) {
            settings.setPlayerComputerControlled(false, player); // the stall only affects human players
        }
        startRoundWithSmallStack(HUMAN_PLAYERS);

        for (int turn = 0; turn < MAXIMAL_TURNS && game.getState() == StatePlacing.class; turn++) {
            game.skipTilePlacement();
        }
        assertState(StateGameOver.class);
    }

    private void startRoundWithSmallStack(int players) {
        for (TileType type : TileType.validTiles()) {
            settings.getTileDistribution().setQuantity(type, 0);
        }
        settings.getTileDistribution().setQuantity(TileType.Road, STACK_SIZE);
        settings.setTilesPerPlayer(GameSettings.MAXIMAL_TILES_ON_HAND);
        game.newRound(players, 5, 5);
    }
}
