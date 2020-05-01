package carcassonne;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.GameMessage;

/**
 * Carcassonne main class.
 * @author Timur Saglam
 */
public final class Carcassonne {
    private static final String LOOK_AND_FEEL_ERROR = "Could not use Nimbus LookAndFeel. Using default instead (";
    private static final String CLOSING_BRACKET = ").";
    private static final String NIMBUS = "Nimbus";
    private static final String MAC = "Mac";
    private static final String OS_NAME_KEY = "os.name";

    /**
     * Main method that starts the game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        setLookAndFeel();
        preloadImages();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainController controller = new MainController();
                controller.startGame();
            }
        });
    }

    private Carcassonne() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Tries to set a custom look and feel if the operating system is not Mac OS. This ensures a at least somewhat decent UI
     * on Windows operating systems.
     */
    private static void setLookAndFeel() {
        if (!System.getProperty(OS_NAME_KEY).startsWith(MAC)) { // TODO (MEDIUM) is this still needed?
            for (LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
                if (NIMBUS.equals(lookAndFeel.getName())) {
                    try {
                        UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
                        GameMessage.showError(LOOK_AND_FEEL_ERROR + exception.getMessage() + CLOSING_BRACKET);
                    }
                }
            }
        }
    }

    /**
     * Loads images in another thread before the game starts. This removes the delay when starting the first round.
     */
    private static void preloadImages() {
        Thread loader = new Thread(new Runnable() {
            public void run() {
                for (TileType tileType : TileType.values()) {
                    new Tile(tileType);
                }
            }
        });
        loader.start();
    }
}
