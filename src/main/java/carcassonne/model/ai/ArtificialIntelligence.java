package carcassonne.model.ai;

import java.util.Collection;
import java.util.Optional;

import carcassonne.model.Player;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileStack;

public interface ArtificialIntelligence {
    /**
     * Returns the last calculated best move without recomputing it.
     * @return
     */
    public Optional<CarcassonneMove> getCurrentMove();

    /**
     * Calculates a new best move for a player who places a tile on a grid. This move can be retrieved without recomputing
     * it with {@link ArtificialIntelligence#getCurentMove()}
     * @param tile is the set of tiles to choose from.
     * @param player is the player who places the tile.
     * @param grid is the grid on which the tile should be placed.
     */
    public Optional<CarcassonneMove> calculateBestMoveFor(Collection<Tile> tiles, Player player, Grid grid, TileStack stack);

    /**
     * Determines which tile to drop when the AI is skipping a turn.
     * @param tiles is a list of tiles to drop.
     * @return the best tile to drop.
     */
    public Tile chooseTileToDrop(Collection<Tile> tiles);

}
