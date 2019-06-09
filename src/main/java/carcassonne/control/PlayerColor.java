package carcassonne.control;

import java.awt.Color;

/**
 * Supplies the color scheme for a specific color.
 * @author Timur Saglam
 */
public class PlayerColor extends Color {
    private static final long serialVersionUID = 7146171711123557361L;
    private static final int HUE = 0;
    private static final int SATURATION = 1;
    private static final int BRIGHTNESS = 2;
    private static final float BRIGHTEN_FACTOR = 0.75f;
    private static final double DESATURATION_FACTOR = 0.6;

    /**
     * Creates a new player color with RGB values between 0 and 255.
     * @see Color#Color(int, int, int)
     */
    public PlayerColor(int red, int green, int blue) {
        super(red, green, blue);
    }

    /**
     * Creates a new player color based on a existing {@link Color}.
     */
    public PlayerColor(Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Returns the lighter and and desaturated version of the player color.
     */
    public Color lightColor() {
        float[] hsb = Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
        hsb[SATURATION] *= DESATURATION_FACTOR; // reduce saturation
        hsb[BRIGHTNESS] = 1 - ((1 - hsb[BRIGHTNESS]) * BRIGHTEN_FACTOR); // increase brightness
        return new Color(Color.HSBtoRGB(hsb[HUE], hsb[SATURATION], hsb[BRIGHTNESS])); // convert to RGB color
    }

    /**
     * Returns the no-alpha version of the player color.
     */
    public Color textColor() {
        return new Color(getRGB(), false); // remove transparency
    }
}
