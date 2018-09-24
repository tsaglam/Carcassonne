package carcassonne.view.main.tilelabel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import carcassonne.control.MainController;

/**
 * Is a simple class derived form JLabel, which stores (additionally to the JLabel functions) the coordinates of the
 * label on the label grid.
 * @author Timur Saglam
 */
public class TileLabel extends JLabel {
    private static final long serialVersionUID = -9104123100195977262L;

    /**
     * Simple constructor calling the <codeJLabel>JLabel(ImageIcon image)</code> constructor.
     * @param image sets the ImageIcon of the label.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileLabel(ImageIcon image, MainController controller, int x, int y) {
        super(image);
        setup(controller, x, y);
    }

    /**
     * Simple constructor calling the <code>JLabel()</code> constructor.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public TileLabel(MainController controller, int x, int y) {
        super();
        setup(controller, x, y);
    }

    private void setup(MainController controller, int x, int y) {
        addMouseListener(new TileLabelMouseAdapter(controller, x, y));
    }
}
