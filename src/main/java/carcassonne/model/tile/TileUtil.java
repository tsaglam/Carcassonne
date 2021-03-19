package carcassonne.model.tile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import carcassonne.settings.GameSettings;

/**
 * Tile utility class. TODO (HIGH) Maybe move this somewhere else.
 * @author Timur Saglam
 */
public final class TileUtil {
    private final static Map<TileType, Integer> rotations = new HashMap<>();

    private TileUtil() {
        throw new IllegalStateException(); // private constructor for non-instantiability
    }

    /**
     * Determines how often a tile of a specific {@link TileType} can be rotated before it returns to the first rotation.
     * @param type is the specific {@link TileType}.
     * @return the number of possible rotations (between 1 and 3).
     */
    public static int rotationLimitFor(TileType type) {
        return rotations.computeIfAbsent(type, key -> determineRotationLimit(key));
    }

    /**
     * Determines rotation limit for a tile type based on the available image resources.
     */
    private static int determineRotationLimit(TileType type) {
        int rotations = 0;
        for (int rotation = 0; rotation < TileRotation.values().length; rotation++) {
            String path = GameSettings.TILE_FOLDER_PATH + type.name() + rotation + GameSettings.TILE_FILE_TYPE;
            InputStream file = TileUtil.class.getClassLoader().getResourceAsStream(path);
            if (file != null) {
                rotations++;
            }
        }
        if (rotations == 0) {
            throw new IllegalStateException(type + " tile needs at least one image file!");
        }
        return rotations;
    }

}
