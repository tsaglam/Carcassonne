package carcassonne.view.meepleButton;

import javax.swing.JButton;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;

/**
 * Is a simple class derived form JButton, which stores (additionally to the JButton functions) the
 * coordinates of the button on the button grid.
 * @author Timur
 */
public class MeepleButton extends JButton {
    private static final long serialVersionUID = -4580099806988033224L;

    /**
     * Simple constructor calling the <codeJButton>JButton()</code> constructor.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public MeepleButton(MainController controller, int x, int y) {
        setup(controller, x, y);
    }

    private void setup(MainController controller, int x, int y) {
        addMouseListener(new MeepleButtonMouseAdapter(GridDirection.values2D()[x][y], controller, this));
    }
}
