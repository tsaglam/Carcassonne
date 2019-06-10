package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A simple mouse adapter for the player selection menu items.
 * @author Timur Saglam
 */
public class MenuPlayersMouseAdapter extends MouseAdapter {

    private final MainMenuBar menuBar;
    private final int playerCount;

    /**
     * Simple constructor.
     * @param menuBar sets the menu bar.
     * @param playerCount sets the amount of players.s
     */
    public MenuPlayersMouseAdapter(MainMenuBar menuBar, int playerCount) {
        super();
        this.menuBar = menuBar;
        this.playerCount = playerCount;
    }

    /**
     * Calls method on main menu bar for setting the player count.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        menuBar.setPlayerCount(playerCount);
    }

}
