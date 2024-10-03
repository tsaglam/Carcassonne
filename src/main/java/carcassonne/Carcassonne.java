package carcassonne;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
import carcassonne.view.util.GameMessage;

/**
 * Carcassonne main class.
 * @author Timur Saglam
 */
public final class Carcassonne {
    private static final int TOOL_TIP_DISMISS_DELAY_IN_MILLISECONDS = 30000;
    private String LOOK_AND_FEEL_ERROR = "Could not use Nimbus LookAndFeel. Using default instead (";
    private String CLOSING_BRACKET = ").";
    private static final String NIMBUS = "Nimbus";
    private static final String MAC = "Mac";
    private static final String OS_NAME_KEY = "os.name";

    /**
     * Main method that starts the game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        setLookAndFeel();
        ToolTipManager.sharedInstance().setDismissDelay(TOOL_TIP_DISMISS_DELAY_IN_MILLISECONDS);
        new MainController().startGame();
    }

    private Carcassonne() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Tries to set a custom look and feel if the operating system is not Mac OS. This ensures a at least somewhat decent
     * user interface on Windows operating systems.
     */
    private static void setLookAndFeel() {
        if (!System.getProperty(OS_NAME_KEY).startsWith(MAC)) {
            for (LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
                if (NIMBUS.equals(lookAndFeel.getName())) {
                    try {
                        UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
						LoadTextCarcassonne prop = new LoadTextCarcassonne();
                        GameMessage.showError(prop.get("LOOK_AND_FEEL_ERROR") + exception.getMessage() + prop.get("CLOSING_BRACKET"));
                    }
                }
            }
        }
    }
}
