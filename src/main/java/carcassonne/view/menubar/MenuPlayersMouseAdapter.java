package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;

/**
 * A simple mouse adapter for the player selection menu items.
 * @author Timur Saglam
 */
public class MenuPlayersMouseAdapter extends MouseAdapter {

    private final GameSettings settings;
    private final int playerCount;

    /**
     * Simple constructor.
     * @param settings are settings that are changed.
     * @param playerCount sets the amount of players.
     */
    public MenuPlayersMouseAdapter(GameSettings settings, int playerCount) {
        super();
        this.settings = settings;
        this.playerCount = playerCount;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        settings.setAmountOfPlayers(playerCount);
    }

}
