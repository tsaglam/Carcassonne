package carcassonne;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import carcassonne.model.Player;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * Tests regarding the hand of tiles of the players.
 * @author Timur Saglam
 */
public class HandOfTilesTest extends CarcassonneTest {

    private static final int STACK_SIZE = 3;

    @Test
    @DisplayName("Dealing hands from a stack that is too small")
    void testDealingFromSmallStack() {
        for (TileType type : TileType.validTiles()) {
            settings.getTileDistribution().setQuantity(type, 0);
        }
        settings.getTileDistribution().setQuantity(TileType.Road, STACK_SIZE);
        settings.setTilesPerPlayer(GameSettings.MAXIMAL_TILES_ON_HAND);

        game.newRound(2, 5, 5);

        for (int i = 0; i < game.getRound().getPlayerCount(); i++) {
            Player player = game.getRound().getPlayer(i);
            assertFalse(player.getHandOfTiles().contains(null), "Hand of player " + i + " contains null tiles: " + player.getHandOfTiles());
        }
    }
}
