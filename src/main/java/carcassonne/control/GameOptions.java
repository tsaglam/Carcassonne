package carcassonne.control;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

/**
 * Class that stores the game options and other information.
 * @author Timur Saglam
 */
public final class GameOptions {
    private static GameOptions instance;

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

    private int taskBarHeight;

    /**
     * Simple constructor that loads the information.
     */
    private GameOptions() {
        initSystemProperties();
        gridHeight = resolutionHeight / GameProperties.TILE_SIZE;
        gridWidth = resolutionWidth / GameProperties.TILE_SIZE;
        gridResolutionHeight = gridHeight * GameProperties.TILE_SIZE;
        gridResolutionWidth = gridWidth * GameProperties.TILE_SIZE;
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
