package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import carcassonne.control.MainController;

/**
 * A simple mouse adapter for the new game button.
 * @author Timur Saglam
 */
public class NewRoundMouseAdapter extends MouseAdapter {
    private final MainController controller;
    private final JMenuItem itemNewRound;
    private final JMenuItem itemAbortRound;

    /**
     * Creates the mouse adapter.
     * @param controller is the main controller to request actions.
     * @param itemNewRound is the menu item to start a new round.
     * @param itemAbortRound is the menu item to abort the current round.
     */
    public NewRoundMouseAdapter(MainController controller, JMenuItem itemNewRound, JMenuItem itemAbortRound) {
        this.controller = controller;
        this.itemNewRound = itemNewRound;
        this.itemAbortRound = itemAbortRound;
    }

    /**
     * Calls method on main menu bar for a new game.
     */
    @Override
    public void mousePressed(MouseEvent event) {
        if (itemNewRound.isEnabled()) {
            controller.requestNewRound();
            itemAbortRound.setEnabled(true);
            itemNewRound.setEnabled(false);
        }
    }
}
