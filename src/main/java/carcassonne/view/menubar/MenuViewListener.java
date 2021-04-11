package carcassonne.view.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainView;
import carcassonne.view.tertiary.PlayerEstheticsView;

/**
 * A mouse adapter and action listener for the UI that allows the selection of a player's colors and name.
 * @author Timur Saglam
 */
public class MenuViewListener extends MouseAdapter implements ActionListener {
    private final PlayerEstheticsView playerView;

    /**
     * Simple constructor that creates the correlating player view.
     * @param playerNumber is the number of the player.
     * @param settings are the {@link GameSettings}.
     * @param mainView is the main user interface.
     */
    public MenuViewListener(int playerNumber, GameSettings settings, MainView mainView) {
        super();
        playerView = new PlayerEstheticsView(playerNumber, settings, mainView);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        playerView.updateAndShow();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        playerView.updateAndShow();
    }
}
