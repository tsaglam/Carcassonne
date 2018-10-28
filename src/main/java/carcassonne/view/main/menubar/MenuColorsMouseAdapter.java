package carcassonne.view.main.menubar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

import carcassonne.control.GameOptions;

/**
 * A simple mouse adapter for the menu selection menu items.
 * @author Timur Saglam
 */
public class MenuColorsMouseAdapter extends MouseAdapter {
    private final int player;
    private final GameOptions options;

    /**
     * Simple constructor.
     * @param menuBar sets the menu bar.
     * @param playerCount sets the amount of players.s
     */
    public MenuColorsMouseAdapter(int player) {
        super();
        this.player = player;
        options = GameOptions.getInstance();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose Background Color", options.getPlayerColor(player));
        if (newColor != null) { // TODO (HIGH) use own color picker
            options.setPlayerColor(newColor, player);
        }
    }

}
