package carcassonne.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainGUI;
import carcassonne.view.tertiary.PlayerSettingsGUI;

/**
 * A mouse adapter and action listener for the selection of player colors. Listens to menu item clicks, event change and
 * button pressing at the same time.
 * @author Timur Saglam
 */
public class MenuSettingsListener extends MouseAdapter implements ActionListener {
    private final PlayerSettingsGUI colorChooser;

    /**
     * Simple constructor that creates the correlating color chooser UI.
     * @param playerNumber is the number of the player whose color gets chosen.
     * @param settings are the {@link GameSettings}.
     * @param mainUI is the main user interface.
     */
    public MenuSettingsListener(int playerNumber, GameSettings settings, MainGUI mainUI) {
        super();
        colorChooser = new PlayerSettingsGUI(playerNumber, settings, mainUI);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        colorChooser.updateAndShow();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        colorChooser.updateAndShow();
    }
}
