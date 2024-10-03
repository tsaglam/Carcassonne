package carcassonne.view.secondary;

import javax.swing.JButton;

import carcassonne.control.ControllerFacade;
import carcassonne.model.grid.GridDirection;

/**
 * This is a simple class derived form JButton, which stores (additionally to the JButton functions) the coordinates of
 * the button on the button grid. It also uses a little hack to allow the view to set the background color of a
 * placementButton (while using Nimbus LookAndFeel), even if it is disabled. Without the hack it's not so easy to
 * accomplish that functionality.
 * @author Timur Saglam
 */
public class PlacementButton extends JButton {
    private static final long serialVersionUID = -4580099806988033224L;
    private static final String WINDOWS_10 = "Windows 10";
    private static final String OS_NAME = "os.name";
    private static final String MAC = "Mac";
    private boolean enabled; // own enabled variable for fixing the isEnabled() method.

    /**
     * Simple constructor calling the <codeJButton>JButton()</code> constructor.
     * @param controller is the controller of the view.
     * @param direction is the direction of the correlating meeple of the button on the tile.
     */
    public PlacementButton(ControllerFacade controller, GridDirection direction) {
        super();
        addMouseListener(new PlacementButtonMouseAdapter(direction, controller, this));
    }

    /**
     * Method checks whether the button is enabled or not. On MAC OS X it uses the normal JButton functionality. On other
     * systems it checks a custom variable set by the custom setEnabled method.
     * @return true if the button is enabled.
     */
    public boolean isHackyEnabled() {
        String osName = System.getProperty(OS_NAME);
        if (osName.startsWith(MAC) || WINDOWS_10.equals(osName)) {
            return isEnabled(); // normal function on mac os x
        }
        // own implementation to fix the functionality which is destroyed by the hack. If the
        // original isEnabled method is overwritten, it breaks some functionality (e.g.updating
        // the background):
        return enabled;
    }

    @Override
    public void setEnabled(boolean value) {
        String osName = System.getProperty(OS_NAME);
        if (osName.startsWith(MAC) || WINDOWS_10.equals(osName)) {
            super.setEnabled(value); // normal function on mac os x
        } else {
            // Hacky method, some variated code from the class javax.swing.AbstractButton.
            if (!value && model.isRollover()) {
                model.setRollover(false);
            }
            enabled = value; // set own enabled variable.
            repaint();
        }
    }
}
