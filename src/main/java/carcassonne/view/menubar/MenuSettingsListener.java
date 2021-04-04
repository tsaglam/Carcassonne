package carcassonne.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainView;
import carcassonne.view.tertiary.PlayerSettingsView;

/**
 * A mouse adapter and action listener for the selection of player colors. Listens to menu item clicks, event change and
 * button pressing at the same time.
 * @author Timur Saglam
 */
public class MenuSettingsListener extends MouseAdapter implements ActionListener {
    private final PlayerSettingsView colorChooser;

    /**
     * Simple constructor that creates the correlating color chooser UI.
     * @param playerNumber is the number of the player whose color gets chosen.
     * @param settings are the {@link GameSettings}.
     * @param mainView is the main user interface.
     */
    public MenuSettingsListener(int playerNumber, GameSettings settings, MainView mainView) {
        super();
        colorChooser = new PlayerSettingsView(playerNumber, settings, mainView);
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
