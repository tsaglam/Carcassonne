package carcassonne.view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Is a simple class derived form JLabel, which stores (additionally to the JLabel functions) the
 * coordinates of the label on the label grid.
 * @author Timur
 */
public class TileLabel extends JLabel {
    private static final long serialVersionUID = -9104123100195977262L;
    private int posX;
    private int posY;

    /**
     * Simple constructor calling the <codeJLabel>JLabel(ImageIcon image)</code> constructor.
     * @param image sets the ImageIcon of the label.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileLabel(ImageIcon image, int x, int y) {
        super(image);
        this.posX = x;
        this.posY = y;
    }

    /**
     * Simple constructor calling the <code>JLabel()</code> constructor.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileLabel(int x, int y) {
        super();
        this.posX = x;
        this.posY = y;
    }

    /**
     * getter for the label position x.
     * @return the labelPositionX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * getter for the label position x.
     * @return the labelPositionY
     */
    public int getPosY() {
        return posY;
    }
}
