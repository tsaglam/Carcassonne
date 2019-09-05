package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.secondary.PlayerSettingsGUI;

/**
 * A mouse adapter for the selection of player colors. Listens to menu item clicks, event change and button pressing at
 * the same time.
 * @author Timur Saglam
 */
public class MenuColorsMouseAdapter extends MouseAdapter {
    private final PlayerSettingsGUI colorChooser;

    /**
     * Simple constructor that creates the correlating color chooser UI.
     * @param playerNumber is the number of the player whose color gets chosen.
     * @param settings are the {@link GameSettings}.
     */
    public MenuColorsMouseAdapter(int playerNumber, GameSettings settings) {
        super();
        colorChooser = new PlayerSettingsGUI(playerNumber, settings);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        colorChooser.updateAndShow();
    }
}
