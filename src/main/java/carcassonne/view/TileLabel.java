package carcassonne.view;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * TODO comment the TileLabel
 * @author Timur
 */
public class TileLabel extends JLabel {
    private static final long serialVersionUID = -9104123100195977262L;
    private int posX;
    private int posY;

    /**
     * TODO comment the TileLabel
     */
    public TileLabel(int x, int y) {
        super();
        this.posX = x;
        this.posY = y;
    }

    /**
     * TODO comment the TileLabel
     * @param image
     * @param x
     * @param y
     */
    public TileLabel(ImageIcon image, int x, int y) {
        super(image);
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
