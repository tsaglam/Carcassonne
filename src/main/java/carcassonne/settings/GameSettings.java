package carcassonne.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.TileDistribution;
import carcassonne.view.NotifiableUI;
import carcassonne.view.PaintShop;

/**
 * Class for the management of the Carcassonne game settings.
 * @author Timur Saglam
 */
public class GameSettings {
    // THRESHOLD CONSTANTS:
    public static final int MAXIMAL_PLAYERS = 5;
    public static final int MAXIMAL_TILES_ON_HAND = 5;
    public static final int TILE_SIZE = 100;
    public static final int TILE_RESOLUTION = 600;
    public static final int HIGH_DPI_FACTOR = 2; // maximum supported DPI factor.

    // STRING CONSTANTS
    public static final String TILE_FILE_TYPE = ".png";
    public static final String TILE_FOLDER_PATH = "tiles/";
    private static final String EMPTY = "";
    private static final String MEEPLE_PATH = "meeple/meeple_";
    private static final String PNG = ".png";
    private static final String TEMPLATE = "_template";
    private static final String[] DEFAULT_NAMES = { "ONE", "TWO", "THREE", "FOUR", "FIVE" };

    // COLOR CONSTANTS:
    public static final Color GUI_COLOR = new Color(190, 190, 190);
    private static final PlayerColor[] DEFAULT_COLORS = { new PlayerColor(30, 26, 197), new PlayerColor(151, 4, 12), new PlayerColor(14, 119, 25),
            new PlayerColor(216, 124, 0), new PlayerColor(96, 0, 147) };

    // COSMETIC:
    private final List<PlayerColor> colors;
    private final List<String> names;

    // GAME SETUP:
    private int gridWidth;
    private int gridHeight;
    private int amountOfPlayers;
    private int stackSizeMultiplier;
    private final TileDistribution tileDistribution;

    // GAME RULES:
    private boolean allowFortifying;
    private int tilesPerPlayer;
    private final Map<TerrainType, Boolean> meepleRules;

    // OTHER/INTERNAL
    private boolean gridSizeChanged;
    private final List<NotifiableUI> changeListeners;

    /**
     * Creates a settings instance. Instances hold different setting values when one is changed.
     */
    public GameSettings() {
        colors = new ArrayList<>(Arrays.asList(DEFAULT_COLORS));
        names = new ArrayList<>(Arrays.asList(DEFAULT_NAMES));
        meepleRules = new HashMap<TerrainType, Boolean>();
        TerrainType.basicTerrain().forEach(it -> meepleRules.put(it, true));
        tileDistribution = new TileDistribution();
        amountOfPlayers = 2;
        tilesPerPlayer = 1;
        stackSizeMultiplier = 1;
        gridWidth = 29;
        gridHeight = 19;
        gridSizeChanged = false;
        changeListeners = new ArrayList<NotifiableUI>();
    }

    /**
     * Returns how many player are playing in the next round.
     * @return the amount of players.
     */
    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    /**
     * Getter for the height of the grid.
     * @return the gridHeight the grid height in tiles.
     */
    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Getter for the width of the grid.
     * @return the gridWidth the grid width in tiles.
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Returns for a specific meeple type if meeple placement is enabled or disabled.
     * @param type it the specific meeple type to query.
     * @return true if placement is enabled.
     */
    public boolean getMeepleRule(TerrainType type) {
        return meepleRules.getOrDefault(type, false);
    }

    /**
     * Returns the {@link PlayerColor} of a specific {@link Player}.
     * @param playerNumber is the number of the {@link Player}.
     * @return the {@link PlayerColor}.
     */
    public PlayerColor getPlayerColor(int playerNumber) {
        return colors.get(playerNumber);
    }

    /**
     * Returns the name of a specific {@link Player}.
     * @param playerNumber is the number of the {@link Player}.
     * @return the name.
     */
    public String getPlayerName(int playerNumber) {
        return names.get(playerNumber);
    }

    /**
     * Returns the multiplier for the tile amounts in a tile stack. When a tile amount is 2 and the stack multiplier is 2
     * the tile stack contains for tiles of this type.
     * @return the stack size multiplier.
     */
    public int getStackSizeMultiplier() {
        return stackSizeMultiplier;
    }

    /**
     * Getter for the current tile distribution.
     * @return the tile distribution.
     */
    public TileDistribution getTileDistribution() {
        return tileDistribution;
    }

    /**
     * Specifies the tiles that each player can hold on his hand.
     * @return the tiles per player.
     */
    public int getTilesPerPlayer() {
        return tilesPerPlayer;
    }

    /**
     * Determines if players are allowed to directly place meeples on patterns they already own.
     * @return true if it is allowed.
     */
    public boolean isAllowingFortifying() {
        return allowFortifying;
    }

    /**
     * Gives information whether the user or the game changed the grid size settings.
     * @return the true if the size was changed.
     */
    public boolean isGridSizeChanged() {
        return gridSizeChanged;
    }

    /**
     * Registers a UI element that wants to listen to changes.
     * @param notifiable is the UI element.
     */
    public void registerNotifiable(NotifiableUI notifiable) {
        changeListeners.add(notifiable);
    }

    /**
     * Changes whether players are allowed to directly place meeples on patterns they already own.
     * @param allowFortifying forbids or allows fortifying.
     */
    public void setAllowFortifying(boolean allowFortifying) {
        this.allowFortifying = allowFortifying;
    }

    /**
     * Specifies how many player are playing in the next round.
     * @param amountOfPlayers is the amount of players.
     */
    public void setAmountOfPlayers(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }

    /**
     * Setter for the height of grid.
     * @param gridHeight the grid height in tiles.
     */
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        gridSizeChanged = true;
    }

    /**
     * Sets the indicator of grid size change.
     * @param gridSizeChanged the value to set the indicator to.
     */
    public void setGridSizeChanged(boolean gridSizeChanged) {
        this.gridSizeChanged = gridSizeChanged;
    }

    /**
     * Setter for the width of grid.
     * @param gridWidth the grid width in tiles.
     */
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        gridSizeChanged = true;
    }

    /**
     * Changes the {@link PlayerColor} of a specific {@link Player}.
     * @param color is the new base {@link Color}.
     * @param playerNumber is the number of the {@link Player}.
     */
    public void setPlayerColor(Color color, int playerNumber) {
        colors.set(playerNumber, new PlayerColor(color));
        notifyListeners();
    }

    /**
     * Changes the name of a specific {@link Player}.
     * @param name is the new name.
     * @param playerNumber is the number of the {@link Player}.
     */
    public void setPlayerName(String name, int playerNumber) {
        names.set(playerNumber, name);
        notifyListeners();
    }

    /**
     * Sets the multiplier for the tile amounts in a tile stack. When a tile amount is 2 and the stack multiplier is 2 the
     * tile stack contains for tiles of this type.
     * @param stackSizeMultiplier is the new stack size multiplier.
     */
    public void setStackSizeMultiplier(int stackSizeMultiplier) {
        this.stackSizeMultiplier = stackSizeMultiplier;
    }

    /**
     * Changes how many tiles each player can hold on his hand.
     * @param tilesPerPlayer is the new amount of tiles per player.
     */
    public void setTilesPerPlayer(int tilesPerPlayer) {
        this.tilesPerPlayer = tilesPerPlayer;
    }

    /**
     * Toggles whether for a specific meeple type if meeple placement is enabled or disabled.
     * @param type it the specific meeple type.
     */
    public void toggleMeepleRule(TerrainType type) {
        meepleRules.computeIfPresent(type, (key, enabled) -> !enabled);
    }

    private void notifyListeners() {
        PaintShop.clearCachedImages();
        for (NotifiableUI notifiable : changeListeners) {
            notifiable.notifyChange();
        }
    }

    /**
     * Builds the path to the image of a specific meeple type.
     * @param type is the type of terrain the meeple occupies.
     * @param isTemplate specifies whether the template image should be loaded.
     * @return the path as a String.
     */
    public static String getMeeplePath(TerrainType type, boolean isTemplate) { // TODO (MEDIUM) move to image loading utility class?
        return MEEPLE_PATH + type.toString().toLowerCase(Locale.UK) + (isTemplate ? TEMPLATE : EMPTY) + PNG;
    }
}