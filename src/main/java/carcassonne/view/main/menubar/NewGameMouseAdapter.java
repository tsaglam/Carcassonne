package carcassonne.view.main.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A simple mouse adapter for the new game button.
 * @author Timur Saglam
 */
public class NewGameMouseAdapter extends MouseAdapter {
    private final MainMenuBar menuBar;

    /**
     * Simple constructor.
     * @param menuBar sets the controller
     */
    public NewGameMouseAdapter(MainMenuBar menuBar) {
        super();
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
