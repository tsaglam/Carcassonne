package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;

/**
 * A simple mouse adapter for the menu selection menu items.
 * @author Timur Saglam
 */
public class MenuNamesMouseAdapter extends MouseAdapter {
    private final int player;
    private final GameSettings settings;

    /**
     * Simple constructor.
     * @param item is the correlating menu item.
     */
    public MenuNamesMouseAdapter(int player, GameSettings settings) {
        super();
        this.player = player;
        this.settings = settings;
    }

    /**
     * Calls method on main menu bar for setting the player name.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        String name = GameMessage.getUserInput("Please enter a new name for player " + settings.getPlayerName(player) + "!");
        if (name != null) { // if not canceled.
            if (name.isEmpty()) {
                GameMessage.showMessage("Invalid name, please try again!");
                mousePressed(e); // try again
            } else {
                settings.setPlayerName(name, player);
            }
        }
    }

}
