package carcassonne.model;

/**
 * @author Timur Saglam
 * The tile of a grid.
 */
public class Tile {
    TerrainType top;
    TerrainType right;
    TerrainType bottom;
    TerrainType left;
    TerrainType middle;
    
    /**
     * Simple constructor.
     * @param top is the top terrain type.
     * @param right is the right terrain type.
     * @param bottom is the bottom terrain. type
     * @param left is the left terrain type.
     * @param middle is the middle terrain type.
     */
    public Tile(TerrainType top, TerrainType right, TerrainType bottom, TerrainType left, TerrainType middle) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.middle = middle;
    }
    
    /**
     * return the terrain type on the tile i  the specific direction.
     * @param direction is the specific direction.
     * @return the terrain type.
     */
    public TerrainType getTerrainType(GridDirection direction) {
        switch (direction) { //TODO use terrain map?
            case UP: return top;
            case RIGHT: return right;
            case DOWN: return bottom;
            case LEFT: return left;
            case MIDDLE: return middle;
            default: throw new IllegalArgumentException("Unknown grid direction.");
        }
    }
    
    /**
     * Checks whether two parts of a tile are connected through same terrain.
     * @param from is the part to check from.
     * @param to is the terrain to check to.
     * @return true if connected, false if not.
     */
    public boolean isConnected(GridDirection from, GridDirection to) {
        return getTerrainType(from).equals(middle) && getTerrainType(to).equals(middle);
    }
}
