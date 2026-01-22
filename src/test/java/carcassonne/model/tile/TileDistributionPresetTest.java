package carcassonne.model.tile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the {@link TileDistributionPreset}.
 */
public class TileDistributionPresetTest {

    private TileDistribution distribution;

    @BeforeEach
    void setUp() {
        distribution = new TileDistribution();
    }

    @Test
    @DisplayName("Resetting a preset-applied distribution does not preserve preset.")
    void testResettingPreset() {
        int roadEndBefore = distribution.getQuantity(TileType.RoadEnd);
        int castleTubeBefore = distribution.getQuantity(TileType.CastleTube);
        TileDistributionPreset.ORIGINAL.applyTo(distribution);
        distribution.reset();
        int roadEndAfter = distribution.getQuantity(TileType.RoadEnd);
        int castleTubeAfter = distribution.getQuantity(TileType.CastleTube);
        assertEquals(roadEndBefore, roadEndAfter);
        assertEquals(castleTubeAfter, castleTubeBefore);
    }

    @Test
    @DisplayName("Applying different presets produces different distributions.")
    void testDifferentPresetsProduceDifferentDistributions() {
        TileDistribution distribution1 = new TileDistribution();
        TileDistribution distribution2 = new TileDistribution();

        TileDistributionPreset.NO_ROADS.applyTo(distribution1);
        TileDistributionPreset.CASTLE_HEAVY.applyTo(distribution2);

        boolean foundDifference = false;
        for (TileType type : TileType.enabledTiles()) {
            if (distribution1.getQuantity(type) != distribution2.getQuantity(type)) {
                foundDifference = true;
                break;
            }
        }

        assertTrue(foundDifference, "Different presets should produce different distributions!");
    }

    @Test
    @DisplayName("Preset names are not null or empty.")
    void testPresetNamesAreValid() {
        for (TileDistributionPreset preset : TileDistributionPreset.values()) {
            assertNotEquals(null, preset.getName(), "Preset name should not be null!");
            assertNotEquals("", preset.getName(), "Preset name should not be empty!");
        }
    }

    @Test
    @DisplayName("toString returns the preset name.")
    void testToStringReturnsName() {
        for (TileDistributionPreset preset : TileDistributionPreset.values()) {
            assertEquals(preset.getName(), preset.toString(), "toString should return the preset name!");
        }
    }

    @Test
    @DisplayName("Applying preset multiple times is idempotent.")
    void testApplyingPresetIsIdempotent() {
        TileDistributionPreset.CASTLE_HEAVY.applyTo(distribution);
        int castleWallAfterFirst = distribution.getQuantity(TileType.CastleWall);

        TileDistributionPreset.CASTLE_HEAVY.applyTo(distribution);
        int castleWallAfterSecond = distribution.getQuantity(TileType.CastleWall);

        assertEquals(castleWallAfterFirst, castleWallAfterSecond,
                "Applying CASTLE_HEAVY twice should double the tiles again (not idempotent by design)!");
    }

    @Test
    @DisplayName("AUTHORS_CHOICE preset keeps default distribution unchanged.")
    void testAuthorsChoicePreset() {
        int monasteryBefore = distribution.getQuantity(TileType.Monastery);
        int roadBefore = distribution.getQuantity(TileType.Road);

        TileDistributionPreset.AUTHORS_CHOICE.applyTo(distribution);

        assertEquals(monasteryBefore, distribution.getQuantity(TileType.Monastery), "AUTHORS_CHOICE should not change Monastery quantity!");
        assertEquals(roadBefore, distribution.getQuantity(TileType.Road), "AUTHORS_CHOICE should not change Road quantity!");
    }

    @Test
    @DisplayName("ORIGINAL preset removes homebrew tiles.")
    void testOriginalPresetRemovesHomebrewTiles() {
        TileDistributionPreset.ORIGINAL.applyTo(distribution);

        assertEquals(0, distribution.getQuantity(TileType.CastleMini), "ORIGINAL should remove CastleMini!");
        assertEquals(0, distribution.getQuantity(TileType.RoadEnd), "ORIGINAL should remove RoadEnd!");
    }

    @Test
    @DisplayName("CASTLE_HEAVY preset does not affect non-castle tiles.")
    void testCastleHeavyPresetDoesNotAffectNonCastleTiles() {
        int roadBefore = distribution.getQuantity(TileType.Road);
        int monasteryBefore = distribution.getQuantity(TileType.Monastery);

        TileDistributionPreset.CASTLE_HEAVY.applyTo(distribution);

        assertEquals(roadBefore, distribution.getQuantity(TileType.Road), "CASTLE_HEAVY should not change Road!");
        assertEquals(monasteryBefore, distribution.getQuantity(TileType.Monastery), "CASTLE_HEAVY should not change Monastery!");
    }

    @Test
    @DisplayName("NO_ROADS preset removes road tiles.")
    void testNoRoadsPresetRemovesAllRoads() {
        TileDistributionPreset.NO_ROADS.applyTo(distribution);
        assertEquals(0, distribution.getQuantity(TileType.Road), "NO_ROADS should remove all road tiles.");
    }
}