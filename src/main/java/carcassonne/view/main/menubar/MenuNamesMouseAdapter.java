package carcassonne.view.main.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.control.GameProperties;
import carcassonne.view.GameMessage;

/**
 * A simple mouse adapter for the menu selection menu items.
 * @author Timur Saglam
 */
public class MenuNamesMouseAdapter extends MouseAdapter {
    private final int player;
    private final GameProperties properties;

    /**
     * Simple constructor.
     * @param item is the correlating menu item.
     */
    public MenuNamesMouseAdapter(int player, GameProperties properties) {
        super();
        this.player = player;
        this.properties = properties;
    }

    /**
     * Calls method on main menu bar for setting the player name.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        String name = GameMessage.getUserInput("Please enter a new name for player " + properties.getName(player) + "!");
        if (name != null) { // if not canceled.
            if (name.isEmpty()) {
                GameMessage.showMessage("Invalid name, please try again!");
                mousePressed(e); // try again
            } else {
                properties.setName(name, player);
            }
        }
    }

}
