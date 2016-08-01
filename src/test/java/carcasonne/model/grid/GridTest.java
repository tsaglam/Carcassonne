package carcasonne.model.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileType;

/**
 * Test case for the grid class.
 * @author Timur
 */
public class GridTest {

    private Grid testGrid;

    @Before
    public void setUp() throws Exception {
        TileType t = TileType.CastleCenterSide;
        testGrid = new Grid(4, 4, t);
    }

    @Test
    public void getDirectNeighborsTest() { // TODO (LOW) more tests for the grid class.
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertEquals(1, testGrid.getDirectNeighbors(x, y).size());
            }
        }
    }

    @Test
    public void getNeighborsTest() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertEquals(1, testGrid.getNeighbors(x, y).size());
            }
        }
    }

}
