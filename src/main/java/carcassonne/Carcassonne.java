package carcassonne;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
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
     * Main method that starts the game. Ensures right look and feel.
     * @param args are not used.
     */
    public static void main(String[] args) {
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
        MainController controller = new MainController();
        controller.startGame();
    }

    private Carcassonne() {
        // private constructor ensure non-instantiability!
    }
}
