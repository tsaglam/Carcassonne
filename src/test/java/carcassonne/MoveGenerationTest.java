package carcassonne;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import carcassonne.model.Player;
import carcassonne.model.ai.AbstractCarcassonneMove;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileRotation;
import carcassonne.model.tile.TileType;

/**
 * Tests for the generation of legal moves for computer-controlled players.
 * @author Timur Saglam
 */
public class MoveGenerationTest extends CarcassonneTest {

    @Test
    @DisplayName("Moves are generated for fully symmetric tiles")
    void testMovesForSymmetricTile() {
        game.newRound(2, 3, 3);
        Grid grid = game.getGrid();
        Player player = game.getRound().getActivePlayer();
        Tile tile = new Tile(TileType.CastleCenter);

        // The castle center fits north of the foundation castle:
        assertTrue(grid.getSpot(1, 0).isPlaceable(tile, settings.isAllowingEnclaves()));

        Collection<? extends AbstractCarcassonneMove> moves = grid.getPossibleMoves(tile, player, settings);
        assertFalse(moves.isEmpty(), "No moves generated for " + tile.getType() + " although it can be placed on (1|0)");
    }

    @Test
    @DisplayName("Moves are generated for tiles tilted to the left")
    void testMovesForLeftTiltedTile() {
        game.newRound(2, 3, 3);
        Grid grid = game.getGrid();
        Player player = game.getRound().getActivePlayer();

        // A road curve tilted to the left connects east and south and fits west of the foundation road:
        Tile tiltedTile = new Tile(TileType.RoadCurve);
        tiltedTile.rotateTo(TileRotation.TILTED_LEFT);
        assertTrue(grid.getSpot(0, 1).isPlaceable(tiltedTile, settings.isAllowingEnclaves()));

        Collection<? extends AbstractCarcassonneMove> moves = grid.getPossibleMoves(new Tile(TileType.RoadCurve), player, settings);
        boolean containsMove = moves.stream()
                .anyMatch(it -> it.getX() == 0 && it.getY() == 1 && it.getRequiredTileRotation() == TileRotation.TILTED_LEFT);
        assertTrue(containsMove, "Legal move with rotation " + TileRotation.TILTED_LEFT + " on (0|1) is missing");
    }
}
