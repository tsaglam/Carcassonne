package carcassonne.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.control.MainController;

/**
 * @author Timur
 */
public class TileLabelMouseAdapter extends MouseAdapter {

    private TileLabel tileLabel;
    private MainController controller;

    /**
     * TODO comment mouse adapter.
     */
    public TileLabelMouseAdapter(TileLabel tileLabel, MainController controller) {
        this.tileLabel = tileLabel;
        this.controller = controller;
    }

    /**
     * TODO comment mouseClicked(MouseEvent e)
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO notify controller with:
        tileLabel.getPosX();
        tileLabel.getPosY();
        System.out.println("Clicked label at (" + tileLabel.getPosX() + "|" + tileLabel.getPosY() + ")");
    }

}
