package carcassonne.view.main.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;

import carcassonne.control.GameOptions;
import carcassonne.view.secondary.GameMessage;

/**
 * A simple mouse adapter for the menu selection menu items.
 * @author Timur Saglam
 */
public class MenuNamesMouseAdapter extends MouseAdapter {
    private final JMenuItem item;
    private final int player;

    /**
     * Simple constructor.
     * @param menuBar sets the menu bar.
     * @param playerCount sets the amount of players.s
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
        String newName = GameMessage.getUserInput("Please enter a new name for player " + item.getText() + "!");
        GameOptions.getInstance().playerNames[player] = newName;
        item.setText(newName);
    }

}
