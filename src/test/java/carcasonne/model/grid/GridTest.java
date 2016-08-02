package carcasonne.model.grid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static carcassonne.model.tile.TileType.*;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.grid.Grid;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;

/**
 * Test case for the grid class.
 * @author Timur
 */
public class GridTest {

    private Grid testGrid;
    private Tile tile;

    @Before
    public void setUp() throws Exception {
        tile = TileFactory.create(Monastry);
        testGrid = new Grid(7, 7, Monastry);
    }

    @Test
    public void tilePlacementTest() {
        System.out.println("Tile placement test:");
        assertTrue(testGrid.place(1, 0, TileFactory.create(CastleCenter)));
        assertTrue(testGrid.place(0, 1, TileFactory.create(Monastry)));
        assertTrue(testGrid.place(2, 1, TileFactory.create(RoadJunctionLarge)));
        assertTrue(testGrid.place(1, 2, TileFactory.create(RoadJunctionLarge)));
        assertTrue(testGrid.place(1, 1, TileFactory.create(CastleWallCurveRight)));
        Tile tmp = TileFactory.create(RoadCurve);
        tmp.rotateRight();
        assertTrue(testGrid.place(2, 2, tmp));
        tmp = TileFactory.create(CastleSides);
        tmp.rotateRight();
        assertTrue(testGrid.place(0, 0, tmp));
        tmp = TileFactory.create(CastleEdgeRoad);
        tmp.rotateLeft();
        assertTrue(testGrid.place(2, 0, tmp));
        tmp = TileFactory.create(Road);
        tmp.rotateRight();
        assertTrue(testGrid.place(0, 2, tmp));
    }

    @Test
    public void getDirectNeighborsTest() { // TODO (LOW) more tests for the grid class.
        System.out.println("getDirectNeighbors test:");
        assertEquals(0, testGrid.getDirectNeighbors(3, 3).size());
        testGrid.place(2, 2, tile);
        assertEquals(0, testGrid.getDirectNeighbors(3, 3).size());
        testGrid.place(2, 3, tile);
        assertEquals(1, testGrid.getDirectNeighbors(3, 3).size());
        testGrid.place(3, 2, tile);
        assertEquals(2, testGrid.getDirectNeighbors(3, 3).size());
    }

    @Test
    public void getNeighborsTest() {
        System.out.println("getNeighbors test:");
        assertEquals(0, testGrid.getNeighbors(3, 3).size());
        testGrid.place(2, 2, tile);
        assertEquals(1, testGrid.getNeighbors(3, 3).size());
        testGrid.place(2, 3, tile);
        assertEquals(2, testGrid.getNeighbors(3, 3).size());
        testGrid.place(3, 2, tile);
        assertEquals(3, testGrid.getNeighbors(3, 3).size());
    }

}
