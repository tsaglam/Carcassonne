package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import carcassonne.control.MainController;

/**
 * A simple mouse adapter for the abort game button.
 * @author Timur Saglam
 */
public class AbortRoundMouseAdapter extends MouseAdapter {
    private final MainController controller;
    private final JMenuItem itemNewRound;
    private final JMenuItem itemAbortRound;

    /**
     * Creates the mouse adapter.
     * @param controller is the main controller to request actions.
     * @param itemNewRound is the menu item to start a new round.
     * @param itemAbortRound is the menu item to abort the current round.
     */
    public AbortRoundMouseAdapter(MainController controller, JMenuItem itemNewRound, JMenuItem itemAbortRound) {
        this.controller = controller;
        this.itemNewRound = itemNewRound;
        this.itemAbortRound = itemAbortRound;
    }

    /**
     * Calls method on main menu bar for aborting the current game.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (itemAbortRound.isEnabled()) {
            controller.requestAbortGame();
            itemAbortRound.setEnabled(false);
            itemNewRound.setEnabled(true);
        }
    }
}
