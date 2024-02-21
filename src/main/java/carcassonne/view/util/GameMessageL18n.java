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
    private static String TITLE = "Carcassonne";

    private GameMessageL18n() {
        // private constructor ensures non-instantiability!
    }

    public static void showGameInfo(String about, String title) {
        JOptionPane.showMessageDialog(null, about, title, JOptionPane.DEFAULT_OPTION, ImageLoadingUtil.SPLASH.createHighDpiImageIcon());
    }

	/*
    public static void showError(String messageText) {
        show(messageText, JOptionPane.ERROR_MESSAGE);
    }

    private static void show(String messageText, int type) {
        JOptionPane.showMessageDialog(null, messageText, TITLE, type, getGameIcon());
    }

    public static Icon getGameIcon() {
        return new Tile(TileType.Null).getScaledIcon(GAME_ICON_SIZE);
    }
	*/
}
