package carcasonne.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.Grid;

/**
 * Test case for the grid class.
 * @author Timur
 */
public class GridTest {

    private Grid testGrid;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        testGrid = new Grid(4, 4);
    }

    @Test
    public void getDirectNeighborsTest() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertEquals(testGrid.getDirectNeighbors(x, y).size(), 0);
            }
        }
    }

    @Test
    public void getNeighborsTest() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                assertEquals(testGrid.getNeighbors(x, y).size(), 0);
            }
        }
    }

}
