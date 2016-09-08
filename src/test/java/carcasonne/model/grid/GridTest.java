package carcasonne.model.grid;

import static carcassonne.model.tile.TileType.CastleCenter;
import static carcassonne.model.tile.TileType.CastleEdgeRoad;
import static carcassonne.model.tile.TileType.CastleSides;
import static carcassonne.model.tile.TileType.CastleWallCurveRight;
import static carcassonne.model.tile.TileType.Monastery;
import static carcassonne.model.tile.TileType.Road;
import static carcassonne.model.tile.TileType.RoadCurve;
import static carcassonne.model.tile.TileType.RoadJunctionLarge;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        tile = TileFactory.create(Monastery);
        testGrid = new Grid(7, 7, Monastery);
    }

    @Test
    public void tilePlacementTest() {
        System.out.println("Tile placement test:");
        Tile tmp = TileFactory.create(Monastery);
        tmp.rotateRight();
        assertTrue(testGrid.place(2, 3, tmp));
        tmp = TileFactory.create(RoadCurve);
        tmp.rotateRight();
        assertTrue(testGrid.place(2, 2, tmp));
        assertTrue(testGrid.place(2, 1, TileFactory.create(RoadJunctionLarge)));
        tmp = TileFactory.create(CastleEdgeRoad);
        tmp.rotateLeft();
        assertTrue(testGrid.place(2, 0, tmp));
        assertTrue(testGrid.place(1, 0, TileFactory.create(CastleCenter)));
        tmp = TileFactory.create(CastleSides);
        tmp.rotateRight();
        assertTrue(testGrid.place(0, 0, tmp));
        assertTrue(testGrid.place(0, 1, TileFactory.create(Monastery)));
        tmp = TileFactory.create(Road);
        tmp.rotateRight();
        assertTrue(testGrid.place(0, 2, tmp));
        assertTrue(testGrid.place(1, 2, TileFactory.create(RoadJunctionLarge)));
        assertTrue(testGrid.place(1, 1, TileFactory.create(CastleWallCurveRight)));
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
