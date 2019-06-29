package carcassonne.view.secondary;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;

/**
 * The class is a specific mouse adapter for a <code>MeepleButton</code>. It connects the buttons with the
 * <code>MainController</code>. The class is a subclass of <code>java.awt.event.MouseAdapter</code>.
 * @author Timur Saglam
 */
public class PlacementButtonMouseAdapter extends MouseAdapter {

    private final GridDirection direction;
    private final MainController controller;
    private final PlacementButton button;

    /**
     * Basic constructor with the button and the controller to set.
     * @param direction is the direction on a tile the adapter places meeples on.
     * @param controller sets the controller that is notified.
     * @param button is the button which uses the adapter.
     */
    public PlacementButtonMouseAdapter(GridDirection direction, MainController controller, PlacementButton button) {
        super();
        this.direction = direction;
        this.controller = controller;
        this.button = button;
    }

    /**
     * Method for processing mouse clicks on the <code>MeepleButton</code> of the class. notifies the
     * <code>MainController</code> of the class.
     * @param event is the <code>MouseEvent</code> of the click.
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        if (button.isHackyEnabled() && SwingUtilities.isLeftMouseButton(event)) {
            controller.requestMeeplePlacement(direction);
        }
    }

}
