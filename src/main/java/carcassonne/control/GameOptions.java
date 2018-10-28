package carcassonne.control;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.List;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.TileType;
import carcassonne.view.Notifiable;

/**
 * Singleton that stores the game options and other information. There is only one option instance at a time. The use of
 * singletons is heavily discussed.
 * @author Timur Saglam
 */
public final class GameOptions {
    private static GameOptions instance; // singleton instance
    private List<Notifiable> changeListeners;

    /**
     * Font type of the button.
     */
    public final Font buttonFont;

    /**
     * Background color of the main GUI.
     */
    public final Color colorGUImain;

    /**
     * Background color of the small GUIs.
     */
    public final Color colorGUIsmall;

    /**
     * The tile type of the foundation.
     */
    public TileType foundationType;

    /**
     * The x coordinates of the grid center.
     */
    public int gridCenterX;

    /**
     * The y coordinates of the grid center.
     */
    public int gridCenterY;

    /**
     * The height of the grid in tiles.
     */
    public int gridHeight;

    /**
     * The height of the grid in pixel.
     */
    public int gridResolutionHeight;

    /**
     * The width of the grid in pixel.
     */
    public int gridResolutionWidth;

    /**
     * The width of the grid in tiles.
     */
    public int gridWidth;

    /**
     * maximal amount of players.
     */
    public final int maximalPlayers;

    /**
     * is the name of the operating system.
     */
    public String operatingSystemName;

    /**
     * Names of the players.
     */
    public String[] playerNames = { "BLUE", "RED", "GREEN", "ORANGE", "PURPLE" }; // TODO (HIGH) use getter/setter with notify

    /**
     * is the height value of the resolution without the taskbar height.
     */
    public int resolutionHeight;

    /**
     * is the width value of the resolution.
     */
    public int resolutionWidth;

    /**
     * width and height of a tile in pixels.
     */
    public final int tileSize;

    private final Color[] playerColor = { new Color(30, 26, 197), new Color(151, 4, 12), new Color(14, 119, 25), new Color(216, 124, 0),
            new Color(96, 0, 147) };

    private final Color[] playerColorLight = { new Color(143, 143, 214), new Color(220, 129, 134), new Color(98, 164, 105), new Color(235, 175, 96),
            new Color(173, 134, 221) };

    private int taskBarHeight;

    /**
     * Simple constructor that loads the information.
     */
    private GameOptions() {
        initSystemProperties();
        changeListeners = new LinkedList<>();
        buttonFont = new Font("Helvetica", Font.BOLD, 12);
        colorGUImain = new Color(190, 190, 190); // grey
        colorGUIsmall = new Color(217, 217, 217); // light grey
        maximalPlayers = 5;
        tileSize = 100;
        gridHeight = resolutionHeight / tileSize;
        gridWidth = resolutionWidth / tileSize;
        gridResolutionHeight = gridHeight * tileSize;
        gridResolutionWidth = gridWidth * tileSize;
        gridCenterX = Math.round((gridWidth - 1) / 2);
        gridCenterY = Math.round((gridHeight - 1) / 2);
        foundationType = TileType.CastleWallRoad;
    }

    /**
     * Builds the path to the image of a specific meeple type.
     * @param type is the type of terrain the meeple occupies.
     * @param isTemplate specifies whether the template image should be loaded.
     * @return the path as a String.
     */
    public String getMeeplePath(TerrainType type, boolean isTemplate) {
        return "src/main/ressources/meeple/meeple_" + type.toString().toLowerCase() + (isTemplate ? "_template" : "") + ".png";
    }

    /**
     * Getter for the player colors.
     * @param playerNumber is the number of the player whose color is requested.
     * @return the color of the player.
     */
    public Color getPlayerColor(int playerNumber) {
        check(playerNumber);
        return playerColor[playerNumber];
    }

    /**
     * Getter for the light player colors.
     * @param playerNumber is the number of the player whose color is requested.
     * @return the light color of the player.
     */
    public Color getPlayerColorLight(int playerNumber) {
        check(playerNumber);
        return playerColorLight[playerNumber];
    }

    /**
     * Registers a UI element that wants to listen to changes.
     * @param notifiable is the UI element.
     */
    public void register(Notifiable notifiable) {
        changeListeners.add(notifiable);
    }

    /**
     * Setter for the player colors.
     * @param color is the new color.
     * @param playerNumber is the number of the player whose color is requested.
     */
    public void setPlayerColor(Color color, int playerNumber) {
        check(playerNumber);
        playerColor[playerNumber] = color;
        notifyListeners();
    }

    private void check(int playerNumber) {
        if (playerNumber < 0 || playerNumber >= playerColorLight.length) {
            throw new IllegalArgumentException(playerNumber + " is a illegal player number for a player color.");
        }
    }

    private void initSystemProperties() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = environment.getDefaultScreenDevice().getDisplayMode();
        resolutionWidth = displayMode.getWidth();
        resolutionHeight = displayMode.getHeight();
        operatingSystemName = System.getProperty("os.name");
        switch (operatingSystemName) {
        case "Windows 7":
            taskBarHeight = 40;
            break;
        case "Mac OS X":
            taskBarHeight = 27;
            break;
        default:
            taskBarHeight = 50;
        }
        resolutionHeight -= taskBarHeight;
    }

    private void notifyListeners() {
        for (Notifiable notifiable : changeListeners) {
            notifiable.notifyChange();
        }
    }

    /**
     * Access method for the GameProperties instance. Secures that only one property object can exist at a time.
     * @return the instance.
     */
    public static synchronized GameOptions getInstance() {
        if (instance == null) {
            instance = new GameOptions();
        }
        return instance;
    }
}
