package carcasonne.model.tile;

import static carcassonne.model.tile.TerrainType.CASTLE;
import static carcassonne.model.tile.TerrainType.FIELDS;
import static carcassonne.model.tile.TerrainType.MONASTRY;
import static carcassonne.model.tile.TerrainType.OTHER;
import static carcassonne.model.tile.TerrainType.ROAD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * Test class for the tile class.
 * @author Timur
 */
public class TileTest {
    private Tile tile;
    private ImageIcon image;
    private String stdPath;
    private String stdFileType;
    private TileType stdTileType;

    @Before
    public void setUp() throws Exception {
        tile = null;
        image = null;
        stdPath = "src/main/ressources/tiles/Null";
        stdFileType = ".jpg";
        stdTileType = TileType.Null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidImageTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, "not a file!", stdFileType, stdTileType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidTypeTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, stdPath, stdFileType, null);
    }

    @Test
    public void basicTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, stdPath, stdFileType, stdTileType);
        // TEST TAG:
        assertEquals(stdTileType, tile.getType());
        assertFalse(tile.isTagged()); // Initially not tagged.
        tile.setTag(true); // tag it manually
        assertTrue(tile.isTagged()); // is now tagged
        // TEST TERRAIN:
        for (GridDirection position : GridDirection.tilePositions()) { // for every tile position
            assertNotNull(tile.getTerrain(position)); // has a terrain type
        }
    }

    @Test
    public void rotateTest() {
        tile = new Tile(CASTLE, FIELDS, ROAD, MONASTRY, OTHER, stdPath, stdFileType, stdTileType);
        tile.rotateRight();
        TerrainType[] expected = { MONASTRY, CASTLE, FIELDS, ROAD, OTHER };
        int i = 0;
        for (GridDirection direction : GridDirection.tilePositions()) { // for every position
            assertEquals(expected[i++], tile.getTerrain(direction)); // check if rotated
        }
        for (int j = 0; j < 10; j++) {
            tile.rotateRight(); // should not crash.
        }
    }

    @Test
    public void rotateTest2() {
        tile = new Tile(CASTLE, FIELDS, ROAD, MONASTRY, OTHER, stdPath, stdFileType, stdTileType);
        tile.rotateLeft();
        TerrainType[] expected = { FIELDS, ROAD, MONASTRY, CASTLE, OTHER };
        int i = 0;
        for (GridDirection direction : GridDirection.tilePositions()) { // for every position
            assertEquals(expected[i++], tile.getTerrain(direction)); // check if rotated
        }
        for (int j = 0; j < 10; j++) {
            tile.rotateLeft(); // should not crash.
        }
    }

    @Test
    public void isConnectedTest() {
        int expected;
        for (TerrainType top : TerrainType.basicTerrain()) { // for every top terrain
            for (TerrainType right : TerrainType.basicTerrain()) { // for every right terrain
                for (TerrainType bottom : TerrainType.basicTerrain()) { // for bottom top terrain
                    for (TerrainType left : TerrainType.basicTerrain()) { // for every left terrain
                        for (TerrainType middle : TerrainType.basicTerrain()) { // for every middle
                                                                                // terrain
                            for (GridDirection direction : GridDirection.directNeighbors()) { // from
                                                                                              // every
                                                                                              // direction
                                tile = new Tile(top, right, bottom, left, middle, stdPath, stdFileType, stdTileType); // create
                                                                                                                      // that
                                                                                                                      // tile
                                TerrainType atDirection = tile.getTerrain(direction); // get
                                                                                      // terrain
                                                                                      // from
                                                                                      // direction
                                expected = 0; // generate expected connections:
                                if (atDirection == middle) {
                                    expected = (middle == top) ? ++expected : expected;
                                    expected = (middle == right) ? ++expected : expected;
                                    expected = (middle == bottom) ? ++expected : expected;
                                    expected = (middle == left) ? ++expected : expected;
                                }
                                checkConnections(tile, direction, expected); // count real
                                                                             // connections
                            }
                        }
                    }
                }
            }
        }
        // special case which uses the CASTLE_AND_ROAD type:
        checkConnections(TileType.CastleEdgeRoad, GridDirection.TOP, 2);
        checkConnections(TileType.CastleEdgeRoad, GridDirection.LEFT, 2);
        checkConnections(TileType.CastleEdgeRoad, GridDirection.BOTTOM, 2);
        checkConnections(TileType.CastleEdgeRoad, GridDirection.RIGHT, 2);
    }

    private void checkConnections(Tile tile, GridDirection fromDirection, int expectedConnections) {
        int connectionCounter = 0;
        for (GridDirection position : GridDirection.directNeighbors()) { // for every tile side
            if (tile.isConnected(fromDirection, position)) { // if side connected to fromDirection
                connectionCounter++; // increase connection counter.
            }
        }
        assertEquals(expectedConnections, connectionCounter); // connections have to be tile
                                                              // connections.
    }

    private void checkConnections(TileType tileType, GridDirection fromDirection, int expectedConnections) {
        tile = TileFactory.create(tileType); // create tile from type
        int connectionCounter = 0;
        for (GridDirection position : GridDirection.directNeighbors()) { // for every tile side
            if (tile.isConnected(fromDirection, position)) { // if side connected to fromDirection
                connectionCounter++; // increase connection counter.
            }
        }
        assertEquals(expectedConnections, connectionCounter); // connections have to be tile
                                                              // connections.
    }

    @Test
    public void imageTest() {
        for (TileType type : TileType.values()) { // for every tile type
            tile = TileFactory.create(type); // create tile
            image = tile.getImage(); // get image
            assertEquals(image.getIconHeight(), 100); // check height
            assertEquals(image.getIconWidth(), 100); // check width
        }
    }

    @Test
    public void nullTileTest() {
        tile = TileFactory.create(TileType.Null);
        tile.rotateRight(); // just testing the useless tile for exceptions & co
        assertEquals(TerrainType.OTHER, tile.getTerrain(GridDirection.TOP));
        assertEquals(TileType.Null, tile.getType());
        assertEquals(true, tile.isConnected(GridDirection.TOP, GridDirection.LEFT));
    }

    @Test
    public void meepleTest() {
        Player p = new Player();
        tile = TileFactory.create(TileType.Null);
        assertFalse(tile.hasMeeple());
        p.placeMeepleAt(tile, GridDirection.TOP);
        assertTrue(tile.hasMeeple());
        assertNotNull(tile.getMeeple());
        tile.removeMeeple();
        assertFalse(tile.hasMeeple());
    }

}
