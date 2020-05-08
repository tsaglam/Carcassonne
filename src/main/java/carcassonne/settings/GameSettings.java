package carcassonne.settings;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.view.PaintShop;

/**
 * Class for the management of the Carcassonne game settings.
 * @author Timur Saglam
 */
public class GameSettings {
    public static final int MAXIMAL_PLAYERS = 5;
    public static final int TILE_SIZE = 100;
    public static final String TILE_FILE_TYPE = ".png";
    public static final String HIGHLIGHT_PATH = "src/main/ressources/highlight.png";
    public static final String NULL_TILE_PATH = "src/main/ressources/tiles/Null0.png";
    public static final String EMBLEM_PATH = "src/main/ressources/emblem.png";
    public static final String TILE_FOLDER_PATH = "src/main/ressources/tiles/";

    private static final PlayerColor[] DEFAULT_COLORS = { new PlayerColor(30, 26, 197), new PlayerColor(151, 4, 12), new PlayerColor(14, 119, 25),
            new PlayerColor(216, 124, 0), new PlayerColor(96, 0, 147) };
    private static final String[] DEFAULT_NAMES = { "ONE", "TWO", "THREE", "FOUR", "FIVE" };
    private static final String EMPTY = "";
    private static final String MEEPLE_PATH = "src/main/ressources/meeple/meeple_";
    private static final String PNG = ".png";
    private static final String TEMPLATE = "_template";
    private final List<Notifiable> changeListeners;
    private boolean chaosMode;
    private int amountOfPlayers;
    private int gridWidth;
    private int gridHeight;

    private final ArrayList<PlayerColor> colors;

    private final ArrayList<String> names;

    /**
     * Creates a settings instance. Instances hold different setting values when one is changed.
     */
    public GameSettings() {
        colors = new ArrayList<>(Arrays.asList(DEFAULT_COLORS));
        names = new ArrayList<>(Arrays.asList(DEFAULT_NAMES));
        amountOfPlayers = 2;
        gridWidth = 29;
        gridHeight = 19;
                
        changeListeners = new ArrayList<Notifiable>();
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
     * Checks whether chaos mode is enabled.
     * @return true if it is enabled.
     */
    public boolean isChaosMode() {
        return chaosMode;
    }

    /**
     * Registers a UI element that wants to listen to changes.
     * @param notifiable is the UI element.
     */
    public void registerNotifiable(Notifiable notifiable) {
        changeListeners.add(notifiable);
    }

    /**
     * Specifies how many player are playing in the next round.
     * @param amountOfPlayers is the amount of players.
     */
    public void setAmountOfPlayers(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
    }

    /**
     * Sets the chaos mode setting.
     * @param chaosMode specifies whether chaos mode is active or not.
     */
    public void setChaosMode(boolean chaosMode) {
        this.chaosMode = chaosMode;
    }

    /**
     * Setter for the height of grid.
     * @param gridHeight the grid height in tiles.
     */
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    /**
     * Setter for the width of grid.
     * @param gridWidth the grid width in tiles.
     */
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
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
     * Builds the path to the image of a specific meeple type.
     * @param type is the type of terrain the meeple occupies.
     * @param isTemplate specifies whether the template image should be loaded.
     * @return the path as a String.
     */
    public static String getMeeplePath(TerrainType type, boolean isTemplate) {
        return MEEPLE_PATH + type.toString().toLowerCase() + (isTemplate ? TEMPLATE : EMPTY) + PNG;
    }

    private void notifyListeners() {
        PaintShop.clearCachedImages();
        for (Notifiable notifiable : changeListeners) {
            notifiable.notifyChange();
        }
    }
}