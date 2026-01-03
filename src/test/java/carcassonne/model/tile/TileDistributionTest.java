package carcassonne.model.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the {@link TileDistribution}.
 */
public class TileDistributionTest {

    public static final int CUSTOM_QUANTITY = 777;
    public static final int CUSTOM_QUANTITY_2 = 150;

    private TileDistribution distribution;
    private int originalMonasteryQuantity;

    @BeforeEach
    void setUp() {
        distribution = new TileDistribution();
        originalMonasteryQuantity = distribution.getQuantity(TileType.Monastery);
    }

    @Test
    @DisplayName("Setting a custom quantity updates the distribution.")
    void testSetQuantity() {
        distribution.setQuantity(TileType.Monastery, CUSTOM_QUANTITY);
        assertEquals(CUSTOM_QUANTITY, distribution.getQuantity(TileType.Monastery), "Quantity should be updated to custom value!");
    }

    @Test
    @DisplayName("Resetting restores default quantities.")
    void testResetDistribution() {
        distribution.setQuantity(TileType.Monastery, CUSTOM_QUANTITY);
        distribution.reset();
        assertEquals(originalMonasteryQuantity, distribution.getQuantity(TileType.Monastery), "Reset should restore original default quantity!");
    }

    @Test
    @DisplayName("Creating and restoring a backup recovers the distribution state.")
    void testRestoreDistribution() {
        distribution.setQuantity(TileType.Monastery, CUSTOM_QUANTITY);

        // create backup and change quantity:
        distribution.createBackup();
        distribution.setQuantity(TileType.Monastery, CUSTOM_QUANTITY_2);
        assertEquals(CUSTOM_QUANTITY_2, distribution.getQuantity(TileType.Monastery), "Quantity should be updated to second custom value!");

        // restore:
        distribution.restoreLastBackup();
        assertEquals(CUSTOM_QUANTITY, distribution.getQuantity(TileType.Monastery), "Restore should recover quantity from backup point!");
    }

    @Test
    @DisplayName("Shuffling preserves the total number of tiles.")
    void testShufflePreservesTotalQuantity() {
        int totalBefore = totalQuantity(distribution);
        distribution.shuffle();
        int totalAfter = totalQuantity(distribution);
        assertEquals(totalBefore, totalAfter, "Shuffle should preserve total tile count!");
    }

    @Test
    @DisplayName("Shuffling with custom quantities preserves the total count.")
    void testShuffleRespectsCustomQuantities() {
        distribution.setQuantity(TileType.Monastery, CUSTOM_QUANTITY);

        int totalBefore = totalQuantity(distribution);
        distribution.shuffle();
        int totalAfter = totalQuantity(distribution);
        assertEquals(totalBefore, totalAfter, "Shuffle should preserve total tile count!");
    }

    private static int totalQuantity(TileDistribution distribution) {
        int total = 0;
        for (TileType type : TileType.enabledTiles()) {
            total += distribution.getQuantity(type);
        }
        return total;
    }
}