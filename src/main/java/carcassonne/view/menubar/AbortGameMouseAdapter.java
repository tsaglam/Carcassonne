package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A simple mouse adapter for the abort game button.
 * @author Timur Saglam
 */
public class AbortGameMouseAdapter extends MouseAdapter {
    private final MainMenuBar menuBar;

    /**
     * Simple constructor.
     * @param menuBar sets the controller
     */
    public AbortGameMouseAdapter(MainMenuBar menuBar) {
        super();
        this.menuBar = menuBar;
    }

    /**
     * Calls method on main menu bar for aborting the current game.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        menuBar.abortGame();
    }
}
