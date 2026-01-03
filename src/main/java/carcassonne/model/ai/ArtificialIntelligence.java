package carcassonne.model.ai;

import java.util.Collection;
import java.util.Optional;

import carcassonne.model.Player;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;

public interface ArtificialIntelligence {
    /**
     * @return returns the last calculated best move without recomputing it.
     */
    Optional<AbstractCarcassonneMove> getCurrentMove();

    /**
     * Calculates a new best move for a player who places a tile on a grid. This move can be retrieved without recomputing
     * it with {@link ArtificialIntelligence#getCurrentMove()}
     * @param tiles is the set of tiles to choose from.
     * @param player is the player who places the tile.
     * @param grid is the grid on which the tile should be placed.
     */
    Optional<AbstractCarcassonneMove> calculateBestMoveFor(Collection<Tile> tiles, Player player, Grid grid, TileStack stack);

    /**
     * Determines which tile to drop when the AI is skipping a turn.
     * @param tiles is a list of tiles to drop.
     * @return the best tile to drop.
     */
    Tile chooseTileToDrop(Collection<Tile> tiles);

}
