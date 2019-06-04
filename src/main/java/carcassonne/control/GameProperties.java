package carcassonne.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for the management of the Carcassonne game properties.
 * @author Timur Saglam
 */
public class GameProperties {
    public final static int TILE_SIZE = 100;
    public final static int MAXIMAL_PLAYERS = 5;
    private static final Color[] DEFAULT_COLORS = { new Color(30, 26, 197), new Color(151, 4, 12), new Color(14, 119, 25), new Color(216, 124, 0),
            new Color(96, 0, 147) };
    private static final String[] DEFAULT_NAMES = { "ONE", "TWO", "THREE", "FOUR", "FIVE" };
    private final List<Notifiable> changeListeners;
    private final ArrayList<String> names;
    private final ArrayList<Color> colors;

    /**
     * Basic constructor. Loads the properties file, creates a new one if the file does not exist.
     * @param FILE_NAME is the name of the property file.
     * @param fileComment is the description in the property file.
     * @param bundleName is the symbolic name of the {@link Bundle}.
     */
    public GameProperties() {
        colors = new ArrayList<>(Arrays.asList(DEFAULT_COLORS));
        names = new ArrayList<>(Arrays.asList(DEFAULT_NAMES));
        changeListeners = new ArrayList<Notifiable>();
    }

    /**
     * Registers a UI element that wants to listen to changes.
     * @param notifiable is the UI element.
     */
    public void registerNotifiable(Notifiable notifiable) {
        changeListeners.add(notifiable);
    }

    public void notifyListeners() {
        for (Notifiable notifiable : changeListeners) {
            notifiable.notifyChange();
        }
    }

    public String getName(int playerNumber) { // TODO (HIGH) rename methods, e.g. getPlayerName
        return names.get(playerNumber);
    }

    public void setName(String name, int playerNumber) {
        names.set(playerNumber, name);
        notifyListeners();
    }

    public void setColor(Color color, int playerNumber) {
        colors.set(playerNumber, color);
        notifyListeners();
    }

    public Color getColor(int playerNumber) {
        return colors.get(playerNumber);
    }

    public Color getLightColor(int playerNumber) {
        Color color = getColor(playerNumber);
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[1] *= 0.5; // reduce saturation
        hsb[2] = 1 - ((1 - hsb[2]) * 0.5f); // increase brightness // TODO (HIGH) reduce brightness a little
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])); // convert to rgb color
    }

    public Color getTextColor(int playerNumber) {
        return new Color(getColor(playerNumber).getRGB(), false); // remove transparency
    }

}