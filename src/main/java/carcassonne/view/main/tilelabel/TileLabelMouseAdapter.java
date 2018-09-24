package carcassonne.view.main.tilelabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.control.MainController;

/**
 * The class is a specific mouse adapter for a <code>TileLabel</code>. It connects the labels with the
 * <code>MainController</code>. The class is a subclass of <code>java.awt.event.MouseAdapter</code>.
 * @author Timur Saglam
 */
public class TileLabelMouseAdapter extends MouseAdapter {

    private final MainController controller;
    private final int x;
    private final int y;

    /**
     * Basic constructor, creates the adapter.
     * @param controller sets the controller that is notified.
     * @param x is the x coordinate of the label.
     * @param y is the y coordinate of the label.
     */
    public TileLabelMouseAdapter(MainController controller, int x, int y) {
        super();
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
