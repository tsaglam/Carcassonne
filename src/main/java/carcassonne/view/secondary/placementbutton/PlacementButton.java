package carcassonne.view.secondary.placementbutton;

import javax.swing.JButton;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;

/**
 * Is a simple class derived form JButton, which stores (additionally to the JButton functions) the coordinates of the
 * button on the button grid. It also uses a little hack to allow the GUI to set the background color of a
 * placementButton (while using Nimbus LookAndFeel), even if it is disabled. Without the hack it's not so easy to
 * accomplish that functionality.
 * @author Timur Saglam
 */
public class PlacementButton extends JButton {
    private static final long serialVersionUID = -4580099806988033224L;
    private boolean enabled; // own enabled variable for fixing the isEnabled() method.
    private final GameOptions options;

    /**
     * Simple constructor calling the <codeJButton>JButton()</code> constructor.
     * @param controller is the controller of the GUI.
     * @param x sets the x coordinate.
     * @param y sets the y coordinate.
     */
    public PlacementButton(MainController controller, int x, int y) {
        super();
        options = GameOptions.getInstance();
        setup(controller, x, y);
    }

    /**
     * Method checks whether the button is enabled or not. On MAC OS X it uses the normal JButton functionality. On other
     * systems it checks a custom variable set by the custom setEnabled method.
     * @return true if the button is enabled.
     */
    public boolean isHackyEnabled() { // TODO (HIGH) is this hack still necesary?
        if (options.operatingSystemName.startsWith("Mac") || options.operatingSystemName.equals("Windows 10")) {
            return isEnabled(); // normal function on mac os x
        } else {
            // own implementation to fix the functionality which is destroyed by the hack. If the
            // original isEnabled method is overwritten, it breaks some functionality (e.g.updating
            // the background):
            return enabled;
        }
    }

    @Override
    public void setEnabled(boolean b) {
        if (options.operatingSystemName.startsWith("Mac") || options.operatingSystemName.equals("Windows 10")) {
            super.setEnabled(b); // normal function on mac os x
        } else {
            // Hacky method, some variated code from the class javax.swing.AbstractButton.
            if (!b && model.isRollover()) {
                model.setRollover(false);
            }
            // super.setEnabled(b); // This is the missing line from the original method.
            // model.setEnabled(b);
            enabled = b; // set own enabled variable.
            repaint();
        }
    }

    private void setup(MainController controller, int x, int y) {
        addMouseListener(new PlacementButtonMouseAdapter(GridDirection.values2D()[x][y], controller, this));
    }
}
