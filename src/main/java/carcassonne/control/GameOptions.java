package carcassonne.control;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.TileType;

/**
 * Class that stores the game options and other information.
 * @author Timur Saglam
 */
@Deprecated
public final class GameOptions {
    private static GameOptions instance;

    /**
     * Background color of the main GUI.
     */
    @Deprecated
    public final Color colorGUImain;

    /**
     * The tile type of the foundation.
     */
    @Deprecated
    public TileType foundationType;

    /**
     * The height of the grid in tiles.
     */
    @Deprecated
    public int gridHeight;

    /**
     * The height of the grid in pixel.
     */
    @Deprecated
    public int gridResolutionHeight;

    /**
     * The width of the grid in pixel.
     */
    @Deprecated
    public int gridResolutionWidth;

    /**
     * The width of the grid in tiles.
     */
    @Deprecated
    public int gridWidth;

    /**
     * is the name of the operating system.
     */
    @Deprecated
    public String operatingSystemName;

    /**
     * is the height value of the resolution without the taskbar height.
     */
    @Deprecated
    public int resolutionHeight;

    /**
     * is the width value of the resolution.
     */
    @Deprecated
    public int resolutionWidth;

    private boolean chaosMode;

    private int taskBarHeight;

    /**
     * Simple constructor that loads the information.
     */
    private GameOptions() {
        initSystemProperties();
        colorGUImain = new Color(190, 190, 190); // grey
        gridHeight = resolutionHeight / GameProperties.TILE_SIZE;
        gridWidth = resolutionWidth / GameProperties.TILE_SIZE;
        gridResolutionHeight = gridHeight * GameProperties.TILE_SIZE;
        gridResolutionWidth = gridWidth * GameProperties.TILE_SIZE;
        foundationType = TileType.CastleWallRoad;
    }

    /**
     * Builds the path to the image of a specific meeple type.
     * @param type is the type of terrain the meeple occupies.
     * @param isTemplate specifies whether the template image should be loaded.
     * @return the path as a String.
     */
    @Deprecated
    public String getMeeplePath(TerrainType type, boolean isTemplate) {
        return "src/main/ressources/meeple/meeple_" + type.toString().toLowerCase() + (isTemplate ? "_template" : "") + ".png";
    }

    /**
     * Checks whether chaos mode is enabled.
     * @return true if it is enabled.
     */
    @Deprecated
    public boolean isChaosMode() {
        return chaosMode;
    }

    /**
     * Sets the chaos mode setting.
     * @param chaosMode specifies whether chaos mode is active or not.
     */
    @Deprecated
    public void setChaosMode(boolean chaosMode) {
        this.chaosMode = chaosMode;
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

    /**
     * Access method for the GameProperties instance. Secures that only one property object can exist at a time.
     * @return the instance.
     */
    @Deprecated
    public static synchronized GameOptions getInstance() {
        if (instance == null) {
            instance = new GameOptions();
        }
        return instance;
    }
}
