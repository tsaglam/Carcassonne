package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainGUI;
import carcassonne.view.tertiary.PlayerSettingsGUI;

/**
 * A mouse adapter for the selection of player colors. Listens to menu item clicks, event change and button pressing at
 * the same time.
 * @author Timur Saglam
 */
public class MenuSettingsMouseAdapter extends MouseAdapter {
    private final PlayerSettingsGUI colorChooser;

    /**
     * Simple constructor that creates the correlating color chooser UI.
     * @param playerNumber is the number of the player whose color gets chosen.
     * @param settings are the {@link GameSettings}.
     * @param mainUI is the main user interface.
     */
    public MenuSettingsMouseAdapter(int playerNumber, GameSettings settings, MainGUI mainUI) {
        super();
        colorChooser = new PlayerSettingsGUI(playerNumber, settings, mainUI);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        colorChooser.updateAndShow();
    }
}
