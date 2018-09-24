package carcassonne.model.tile;

/**
 * Factory class for building tile objects. Because of the large amount of different tile objects this factory class
 * enables easy tile creation with the <code>TileType</code> enum.
 * @author Timur Saglam
 */
public final class TileFactory {
    private static final String FOLDER = "src/main/ressources/tiles/";
    private static final String FILE_TYPE = ".jpg"; // TODO (HIGH) move to options.

    /**
     * Factory method of the class. Produces a specific tile for a specific tile type.
     * @param type is the tile type.
     * @return the tile object.
     */
    public static Tile create(TileType type) { // TODO (HIGH) is this class still needed?
        if (type == null) { // null is invalid argument.
            throw new IllegalArgumentException("TileFactory can't create tile from TileType value null.");
        }
        String path = FOLDER + type.name(); // generate path.
        return new Tile(type, path, FILE_TYPE);
    }

    private TileFactory() {
        // PRIVATE CONSTRUCTOR, PREVENTS INSTANTIATION!
    }
}
