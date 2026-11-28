package carcassonne.util;

import static carcassonne.settings.GameSettings.TILE_RESOLUTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;

/**
 * JUnit 5 test for {@link TileImageScalingCache}. Tests the bounded eviction and that original images are never
 * evicted.
 * @author Timur Saglam
 */
class TileImageScalingCacheTest {
    private static final Image IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    private List<Tile> tiles;

    @BeforeEach
    void setUp() {
        TileImageScalingCache.clear();
        tiles = TileType.validTiles().stream().map(Tile::new).toList();
    }

    @AfterEach
    void tearDown() {
        TileImageScalingCache.clear();
    }

    @Test
    void testSizeIsBoundedAtMaximum() {
        fillWithScaledImages();
        assertEquals(TileImageScalingCache.MAXIMUM_SIZE, TileImageScalingCache.size());
    }

    @Test
    void testOriginalsAreNeverEvicted() {
        for (Tile tile : tiles) {
            TileImageScalingCache.putScaledImage(IMAGE, tile, TILE_RESOLUTION, false);
        }
        fillWithScaledImages();
        for (Tile tile : tiles) {
            CachedImage original = TileImageScalingCache.getCached(tile, TILE_RESOLUTION);
            assertNotNull(original, "Original image should never be evicted!");
            assertSame(IMAGE, original.image());
            assertFalse(original.isPreview());
        }
        assertEquals(TileImageScalingCache.MAXIMUM_SIZE + tiles.size(), TileImageScalingCache.size());
    }

    @Test
    void testLeastRecentlyUsedIsEvicted() {
        Tile first = tiles.getFirst();
        TileImageScalingCache.putScaledImage(IMAGE, first, 1, false);
        TileImageScalingCache.putScaledImage(IMAGE, first, 2, false);
        int otherTiles = tiles.size() - 1;
        for (int i = 2; i < TileImageScalingCache.MAXIMUM_SIZE; i++) { // fill up with other tiles
            TileImageScalingCache.putScaledImage(IMAGE, tiles.get(1 + i % otherTiles), 1 + i / otherTiles, false);
        }
        assertEquals(TileImageScalingCache.MAXIMUM_SIZE, TileImageScalingCache.size());
        assertNotNull(TileImageScalingCache.getCached(first, 1)); // refreshes size 1, size 2 is now eldest
        TileImageScalingCache.putScaledImage(IMAGE, first, 3, false);
        assertNotNull(TileImageScalingCache.getCached(first, 1), "Recently accessed image should survive!");
        assertNull(TileImageScalingCache.getCached(first, 2), "Least recently used image should be evicted!");
    }

    private void fillWithScaledImages() {
        int inserted = 0;
        for (int size = 1; size < TILE_RESOLUTION && inserted <= 2 * TileImageScalingCache.MAXIMUM_SIZE; size++) {
            for (Tile tile : tiles) {
                TileImageScalingCache.putScaledImage(IMAGE, tile, size, false);
                inserted++;
                assertTrue(TileImageScalingCache.size() <= TileImageScalingCache.MAXIMUM_SIZE + tiles.size());
            }
        }
        assertTrue(inserted > TileImageScalingCache.MAXIMUM_SIZE, "Test should insert more images than the maximum!");
    }
}
