package carcassonne.control;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.TileType;

/**
 * Singleton that stores the game options and other information. There is only one option instance
 * at a time. The use of singletons is heavily discussed.
 * @author Timur
 */
public final class GameOptions {
    private static GameOptions instance;

    /**
     * Access method for the GameProperties instance. Secures that only one property object can
     * exist at a time.
     * @return the instance.
     */
    public static GameOptions getInstance() {
        if (instance == null) {
            instance = new GameOptions();
        }
        return instance;
    }

    /**
     * is the width value of the resolution.
     */
    public final int resolutionWidth;

    /**
     * is the height value of the resolution.
     */
    public final int resolutionHeight;

    /**
     * is the height value of the resolution without the taskbar height.
     */
    public final int resolutionHeightWindow;

    /**
     * is the name of the operating system.
     */
    public final String operatingSystemName;

    /**
     * maximal amount of players.
     */
    public final int maximalPlayers;

    /**
     * width and height of a tile in pixels.
     */
    public final int tileSize;

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
     * The width of the grid in tiles.
     */
    public int gridWidth;

    /**
     * The height of the grid in tiles.
     */
    public int gridHeight;

    /**
     * The width of the grid in pixel.
     */
    public int gridResolutionWidth;

    /**
     * The height of the grid in pixel.
     */
    public int gridResolutionHeight;

    /**
     * The x coordinates of the grid center.
     */
    public int gridCenterX;

    /**
     * The y coordinates of the grid center.
     */
    public int gridCenterY;

    /**
     * The tile type of the foundation.
     */
    public TileType foundationType;

    private int taskBarHeight;

    private final Color[] playerColor = { new Color(30, 26, 197), new Color(151, 4, 12), new Color(14, 119, 25), new Color(254, 220, 50) };

    /**
     * Simple constructor that loads the information.
     */
    private GameOptions() {
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
        buttonFont = new Font("Helvetica", Font.BOLD, 12);
        colorGUImain = new Color(190, 190, 190); // grey
        colorGUIsmall = new Color(217, 217, 217); // light grey
        maximalPlayers = 4;
        tileSize = 100;
        resolutionHeightWindow = resolutionHeight - taskBarHeight;
        gridHeight = (resolutionHeight - taskBarHeight) / tileSize;
        gridWidth = resolutionWidth / tileSize;
        gridResolutionHeight = gridHeight * tileSize;
        gridResolutionWidth = gridWidth * tileSize;
        gridCenterX = Math.round((gridWidth - 1) / 2);
        gridCenterY = Math.round((gridHeight - 1) / 2);
        foundationType = TileType.CastleWallRoad;
    }

    public Color playerColor(int playerNumber) {
        if (playerNumber < 0 || playerNumber >= playerColor.length) {
            throw new IllegalArgumentException(playerNumber + " is a illegal player number for a player color.");
        }
        return playerColor[playerNumber];
    }

    /**
     * Builds the path to the image of a specific meeple of a player.
     * @param type is the type of terrain the meeple occupies.
     * @param playerNumber is the number of the meeple owner. use -1 for a generic meeple.
     * @return the path as a String.
     */
    public String buildImagePath(TerrainType type, int playerNumber) {
        String pathBase = "src/main/ressources/meeple/meeple_" + type.toString().toLowerCase();
        if (playerNumber < 0 || type == TerrainType.OTHER || type == TerrainType.FIELDS) {
            return pathBase + ".png";
        } else {
            return pathBase + "_" + playerNumber + ".png";
        }
    }
}
