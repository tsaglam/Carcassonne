package carcassonne.view.tileLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.control.MainController;

/**
 * The class is a specific mouse adapter for a <code>TileLabel</code>. It connects the labels with
 * the <code>MainController</code>. The class is a subclass of
 * <code>java.awt.event.MouseAdapter</code>.
 * @author Timur
 */
public class TileLabelMouseAdapter extends MouseAdapter {

    private MainController controller;
    private int x;
    private int y;

    /**
     * Basic constructor with the label and the controller to set.
     * @param tileLabel should be the label that uses the adapter.
     * @param controller sets the controller that is notified.
     */
    public TileLabelMouseAdapter(MainController controller, int x, int y) {
        this.controller = controller;
        this.x = x;
        this.y = y;
    }

    /**
     * Method for processing mouse clicks on the <code>TileLabel</code> of the class. notifies the
     * <code>MainController</code> of the class.
     * @param e is the <code>MouseEvent</code> of the click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        controller.requestTilePlacement(x, y);
    }

}
