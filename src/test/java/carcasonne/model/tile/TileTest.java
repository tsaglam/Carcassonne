package carcasonne.model.tile;

import static carcassonne.model.TerrainType.CASTLE;
import static carcassonne.model.TerrainType.FIELDS;
import static carcassonne.model.TerrainType.MONASTRY;
import static carcassonne.model.TerrainType.OTHER;
import static carcassonne.model.TerrainType.ROAD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Image;

import org.junit.Before;
import org.junit.Test;

import carcassonne.model.TerrainType;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * Test class for the tile class.
 * @author Timur
 */
public class TileTest {
    private Tile tile;
    private Image image;
    private String stdPath;
    private TileType stdTileType;

    @Before
    public void setUp() throws Exception {
        tile = null;
        image = null;
        stdPath = "src/main/ressources/tiles/Null.jpg";
        stdTileType = TileType.Null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidImageTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, "not a file!", stdTileType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidTypeTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, stdPath, null);
    }

    @Test
    public void basicTest() {
        tile = new Tile(OTHER, OTHER, OTHER, OTHER, OTHER, stdPath, stdTileType);
        // TEST TAG:
        assertEquals(stdTileType, tile.getType());
        assertFalse(tile.isTagged()); // Initially not tagged.
        tile.setTag(true); // tag it manually
        assertTrue(tile.isTagged()); // is now tagged
        // TEST TERRAIN:
        for (GridDirection position : GridDirection.tilePositions()) { // for every tile position
            assertNotNull(tile.getTerrainAt(position)); // has a terrain type
        }
    }

    @Test
    public void rotateTest() {
        tile = new Tile(CASTLE, FIELDS, ROAD, MONASTRY, OTHER, stdPath, stdTileType);
        tile.rotate();
        TerrainType[] expected = { MONASTRY, CASTLE, FIELDS, ROAD, OTHER };
        int i = 0;
        for (GridDirection direction : GridDirection.tilePositions()) { // for every position
            assertEquals(expected[i++], tile.getTerrainAt(direction)); // check i rotated successfully.
        }
        // TODO maybe test if image is really rotated
    }

    @Test
    public void isConnectedTest() {
        int expected;
        for (TerrainType top : TerrainType.basicTerrain()) { // for every top terrain
            for (TerrainType right : TerrainType.basicTerrain()) { // for every right terrain
                for (TerrainType bottom : TerrainType.basicTerrain()) { // for bottom top terrain
                    for (TerrainType left : TerrainType.basicTerrain()) { // for every left terrain
                        for (TerrainType middle : TerrainType.basicTerrain()) { // for every middle terrain
                            for (GridDirection direction : GridDirection.directNeighbors()) { // from every direction
                                tile = new Tile(top, right, bottom, left, middle, stdPath, stdTileType); // create that tile
                                TerrainType atDirection = tile.getTerrainAt(direction); // get terrain from direction
                                expected = 0; // generate expected connections:
                                if (atDirection == middle) {
                                    expected = (middle == top) ? ++expected : expected;
                                    expected = (middle == right) ? ++expected : expected;
                                    expected = (middle == bottom) ? ++expected : expected;
                                    expected = (middle == left) ? ++expected : expected;
                                }
                                checkConnections(tile, direction, expected); // count real connections
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
        tile = TileFactory.createTile(tileType); // create tile from type
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
            tile = TileFactory.createTile(type); // create tile
            image = tile.getImage(); // get image
            assertEquals(image.getHeight(null), 100); // check height
            assertEquals(image.getWidth(null), 100); // check width
        }
    }

    @Test
    public void nullTileTest() {
        tile = TileFactory.createTile(TileType.Null); 
        tile.rotate(); // just testing the useless tile for exceptions & co
        assertEquals(TerrainType.OTHER, tile.getTerrainAt(GridDirection.TOP));
        assertEquals(TileType.Null, tile.getType());
        assertEquals(true, tile.isConnected(GridDirection.TOP, GridDirection.LEFT));
    }

}
