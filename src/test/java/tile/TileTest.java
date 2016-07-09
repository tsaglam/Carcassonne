package tile;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * Test class for the tile class.
 * @author Timur
 */
public class TileTest {
    private Tile tile;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tile = TileFactory.createTile(TileType.CastleEdgeRoad);
    }

    @Test
    public void basicTileTest() {
        fail("Not yet implemented");
    }
    
    @Test
    public void nullTileTest() {
        tile = TileFactory.createTile(TileType.Null);
        fail("Not yet implemented");
    }

}
