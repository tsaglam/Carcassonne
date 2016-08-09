package carcassonne.view.meepleButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;

/**
 * The class is a specific mouse adapter for a <code>MeepleButton</code>. It connects the buttons
 * with the <code>MainController</code>. The class is a subclass of
 * <code>java.awt.event.MouseAdapter</code>.
 * @author Timur
 */
public class MeepleButtonMouseAdapter extends MouseAdapter {

    private GridDirection direction;
    private MainController controller;
    private MeepleButton button;

    /**
     * Basic constructor with the button and the controller to set.
     * @param meepleButton should be the button that uses the adapter.
     * @param controller sets the controller that is notified.
     */
    public MeepleButtonMouseAdapter(GridDirection direction, MainController controller, MeepleButton button) {
        this.direction = direction;
        this.controller = controller;
        this.button = button;
    }

    /**
     * Method for processing mouse clicks on the <code>MeepleButton</code> of the class. notifies
     * the <code>MainController</code> of the class.
     * @param e is the <code>MouseEvent</code> of the click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (button.isEnabled()) {
            controller.requestMeeplePlacement(direction);
        }
    }

}
