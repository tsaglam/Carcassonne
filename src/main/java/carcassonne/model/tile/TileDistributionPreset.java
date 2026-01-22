package carcassonne.model.tile;

import carcassonne.model.terrain.TerrainType;

/**
 * Defines preset configurations for tile distributions.
 * @author Timur Saglam
 */
public enum TileDistributionPreset {

    AUTHORS_CHOICE("Timur's Homebrew Version", it -> {
        // No change required from the default
    }),

    ORIGINAL("Carcassonne Original", it -> {
        // No homebrew/expansion tiles:
        it.setQuantity(TileType.CastleMini, 0);
        it.setQuantity(TileType.MonasteryRoad, 0);
        it.setQuantity(TileType.RoadCrossSmall, 0);
        it.setQuantity(TileType.RoadEnd, 0);
        it.setQuantity(TileType.CastleSidesQuad, 0);
        it.setQuantity(TileType.CastleWallEntry, 0);
        it.setQuantity(TileType.CastleWallEntryLeft, 0);
        it.setQuantity(TileType.CastleWallEntryRight, 0);
        it.setQuantity(TileType.CastleCenterSides, 0);
        it.setQuantity(TileType.CastleTubeEntries, 0);
        it.setQuantity(TileType.CastleTubeEntry, 0);
        it.setQuantity(TileType.CastleSidesEdgeRoad, 0);
        it.setQuantity(TileType.CastleSidesRoad, 0);
        it.setQuantity(TileType.MonasteryJunction, 0);

        // Set normal amounts for original tiles:
        it.setQuantity(TileType.CastleTube, 3);
        it.setQuantity(TileType.Road, 8);
        it.setQuantity(TileType.RoadCurve, 9);
        it.setQuantity(TileType.RoadJunctionSmall, 4);
    }),

    CASTLE_HEAVY("Castle Heavy", it -> {
        for (TileType type : TileType.enabledTiles()) {
            if (type.containsTerrain(TerrainType.CASTLE)) {
                it.setQuantity(type, type.getAmount() * 2); // double all castle tiles
            }
        }
    }),

    LAND_OF_CATHEDRALS("Land of Cathedrals", it -> {
        // first, increase castle tiles by one:
        for (TileType type : TileType.enabledTiles()) {
            if (type.containsTerrain(TerrainType.CASTLE)) {
                it.setQuantity(type, type.getAmount() + 1);
            }
        }

        // ensure many cathedrals but no other monasteries:
        it.setQuantity(TileType.MonasteryCastle, 11);
        it.setQuantity(TileType.Monastery, 0);
        it.setQuantity(TileType.MonasteryRoad, 0);
        it.setQuantity(TileType.MonasteryJunction, 0);

        // to make up for the lack of field connecting tiles, ensure many mini castles:
        it.setQuantity(TileType.CastleMini, 5);
    }),

    NO_ROADS("No Roads", it -> {
        for (TileType type : TileType.enabledTiles()) {
            if (type.containsTerrain(TerrainType.ROAD)) {
                it.setQuantity(type, 0);
            }
        }
    }),

    NO_MONASTERIES("No Monasteries", it -> {
        it.setQuantity(TileType.Monastery, 0);
        it.setQuantity(TileType.MonasteryRoad, 0);
        it.setQuantity(TileType.MonasteryCastle, 0);
        it.setQuantity(TileType.MonasteryJunction, 0);
    });

    private final String name;
    private final PresetApplier applier;

    /**
     * Creates a preset for a tile distribution.
     * @param name is the name of the distribution.
     * @param applier is the function that adapts tile quantities of a given default distribution.
     * @see TileDistribution
     */
    TileDistributionPreset(String name, PresetApplier applier) {
        this.name = name;
        this.applier = applier;
    }

    /**
     * Applies this preset to the given distribution.
     * @param distribution the distribution to modify.
     */
    public void applyTo(TileDistribution distribution) {
        distribution.reset();
        applier.apply(distribution);
    }

    /**
     * @return the name of the distribution.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @FunctionalInterface
    private interface PresetApplier {
        void apply(TileDistribution distribution);
    }
}