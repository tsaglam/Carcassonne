package carcassonne.view.util;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import java.util.ResourceBundle;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.util.ImageLoadingUtil;

/**
 * Message class for showing the user small messages.
 * @author Timur Saglam
 */
public class GameMessageL18n {
	private static ResourceBundle resource;
    private static final int GAME_ICON_SIZE = 75;
    private String ABOUT;
    private String TITLE = "Carcassonne";

    private GameMessageL18n() {
        // private constructor ensures non-instantiability!
    }

    public static void showGameInfo(String about, String title) {
        JOptionPane.showMessageDialog(null, about, title, JOptionPane.DEFAULT_OPTION, ImageLoadingUtil.SPLASH.createHighDpiImageIcon());
    }
}
