package carcassonne.control;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * Class which to access system information.
 * @author Timur
 */
public class SystemProperties {
    int resolutionWidth;
    int resolutionHeight;
    private String operatingSystemName;
    int taskBarHeight;

    /**
     * Simple constructor that loads the information.
     */
    public SystemProperties() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = environment.getDefaultScreenDevice();
        DisplayMode displayMode = graphicsDevice.getDisplayMode();
        resolutionWidth = displayMode.getWidth();
        resolutionHeight = displayMode.getHeight();
        operatingSystemName = System.getProperty("os.name");
        switch (operatingSystemName) {
        case "Windows 7":
            taskBarHeight = 40;
            break;
        case "Mac OS X":
            taskBarHeight = 22;
            break;
        default:
            taskBarHeight = 40;
        }
    }

    /**
     * @return the resolutionWidth
     */
    public int getResolutionWidth() {
        return resolutionWidth;
    }

    /**
     * @return the resolutionHeight
     */
    public int getResolutionHeight() {
        return resolutionHeight;
    }

    /**
     * @return the operatingSystemName
     */
    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    /**
     * @return the taskBarHeight
     */
    public int getTaskBarHeight() {
        return taskBarHeight;
    }

}
