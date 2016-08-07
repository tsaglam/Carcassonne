package carcassonne.view.menuBar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A simple mouse adapter for the new game button.
 * @author Timur Saglam
 */
public class NewGameMouseAdapter extends MouseAdapter {
    private MainMenuBar menuBar;

    /**
     * Simple constructor.
     * @param menuBar sets the controller
     */
    public NewGameMouseAdapter(MainMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    /**
     * Calls method on main menu bar for a new game.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        menuBar.newGame();
    }
}
