package carcassonne.control;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.CastleWallRoad;
import carcassonne.model.tile.Tile;

/**
 * Singleton that stores the game options and other information. There is only one option instance at a time. The use of
 * singletons is heavily discussed.
 * @author Timur Saglam
 */
public final class GameOptions {
    private static GameOptions instance;

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
    public Class<? extends Tile> foundationType;

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
    public final String operatingSystemName;

    /**
     * is the height value of the resolution.
     */
    public final int resolutionHeight;

    /**
     * is the height value of the resolution without the taskbar height.
     */
    public final int resolutionHeightWindow;

    /**
     * is the width value of the resolution.
     */
    public final int resolutionWidth;

    /**
     * width and height of a tile in pixels.
     */
    public final int tileSize;

    /**
     * Names of the players.
     */
    public final String[] playerNames = { "BLUE", "RED", "GREEN", "YELLOW" };

    private final Color[] playerColor = { new Color(30, 26, 197), new Color(151, 4, 12), new Color(14, 119, 25), new Color(255, 214, 0) };

    private final Color[] playerColorLight = { new Color(143, 143, 214), new Color(220, 129, 134), new Color(98, 164, 105),
            new Color(230, 213, 125) };

    private final int taskBarHeight;

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
        foundationType = CastleWallRoad.class;
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

    private void check(int playerNumber) {
        if (playerNumber < 0 || playerNumber >= playerColorLight.length) {
            throw new IllegalArgumentException(playerNumber + " is a illegal player number for a player color.");
        }
    }
}
