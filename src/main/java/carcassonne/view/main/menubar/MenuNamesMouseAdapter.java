package carcassonne.view.main.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import carcassonne.control.GameOptions;
import carcassonne.view.GameMessage;

/**
 * A simple mouse adapter for the menu selection menu items.
 * @author Timur Saglam
 */
public class MenuNamesMouseAdapter extends MouseAdapter {
    private final JMenuItem item;
    private final int player;

    /**
     * Simple constructor.
     * @param player sets player whose name gets changed.
     * @param item is the correlating menu item.
     */
    public MenuNamesMouseAdapter(int player, JMenuItem item) {
        super();
        this.player = player;
        this.item = item;
    }

    /**
     * Calls method on main menu bar for setting the player name.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        String name = GameMessage.getUserInput("Please enter a new name for player " + item.getText() + "!");
        if (name != null) { // if not canceled.
            if (name.isEmpty()) {
                GameMessage.showMessage("Invalid name, please try again!");
                mousePressed(e); // try again
            } else {
                GameOptions.getInstance().playerNames[player] = name;
                item.setText(name);
            }
        }
    }

}
