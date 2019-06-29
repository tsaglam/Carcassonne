package carcassonne;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
import carcassonne.view.GameMessage;

/**
 * Carcassonne main class for starting the game itself.
 * @author Timur Saglam
 */
public final class CarcasonneMain {
    private static final String NIMBUS = "Nimbus";
    private static final String MAC = "Mac";
    private static final String OS_NAME_KEY = "os.name";

    /**
     * Main method for the Carcassonne game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        if (!System.getProperty(OS_NAME_KEY).startsWith(MAC)) { // TODO (MEDIUM) is this still needed?
            for (LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
                if (NIMBUS.equals(lookAndFeel.getName())) {
                    try {
                        UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
                        GameMessage.showError("Could not use Nimbus LookAndFeel. Using default instead (" + exception.getMessage() + ").");
                    }
                }
            }
        }
        new MainController();
    }

    private CarcasonneMain() {
    }
}
