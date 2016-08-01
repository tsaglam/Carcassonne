package carcasonne.model;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.Player;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * Test case for the players, the meeples and the meeple placement on tiles.
 * @author Timur
 */
public class PlayerTest {
    private Player player;
    private Grid grid;
    Tile tile;

    @Before
    public void setUp() throws Exception {
        player = new Player();
        grid = new Grid(3, 3, TileType.Null);
        tile = TileFactory.createTile(TileType.CastleWallRoad);
        grid.place(0, 0, tile);
    }

    @Test
    public void test() {
        player.placeMeepleAt(grid.getTile(0, 0));
        // TODO (LOW) implement tests
        fail("Not yet implemented");
    }

}
