package carcasonne.model.tile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.tile.TileStack;

/**
 * Test case for the tile stack class.
 * @author Timur
 */
public class TileStackTest {

    TileStack stack;

    @Before
    public void setUp() throws Exception {
        stack = new TileStack();
    }

    @Test
    public void test() {
        assertFalse(stack.isEmpty());
        while (!stack.isEmpty()) {
            System.out.println("Took " + stack.drawTile().getType() + " from stack.");
        }
        assertNull(stack.drawTile());
    }

}
