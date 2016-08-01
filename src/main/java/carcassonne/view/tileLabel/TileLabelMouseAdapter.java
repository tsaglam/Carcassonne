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

    private TileLabel tileLabel;
    private MainController controller;

    /**
     * Basic constructor with the label and the controller to set.
     * @param tileLabel should be the label that uses the adapter.
     * @param controller sets the controller that is notified.
     */
    public TileLabelMouseAdapter(TileLabel tileLabel, MainController controller) {
        this.tileLabel = tileLabel;
        this.controller = controller;
    }

    /**
     * Method for processing mouse clicks on the <code>TileLabel</code> of the class. notifies the
     * <code>MainController</code> of the class.
     * @param e is the <code>MouseEvent</code> of the click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        controller.requestTilePlacement(tileLabel.getPosX(), tileLabel.getPosY());
        // TODO (LOWEST) remove debug output.
        System.out.println("Clicked label at (" + tileLabel.getPosX() + "|" + tileLabel.getPosY() + ")");
    }

}
