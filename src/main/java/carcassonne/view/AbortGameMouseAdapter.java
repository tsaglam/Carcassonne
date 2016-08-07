package carcassonne.view;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A simple mouse adapter for the abort game button.
 * @author Timur
 */
public class AbortGameMouseAdapter extends MouseAdapter {
    private MainMenuBar menuBar;

    /**
     * Simple constructor.
     * @param menuBar sets the controller
     */
    public AbortGameMouseAdapter(MainMenuBar menuBar) {
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
