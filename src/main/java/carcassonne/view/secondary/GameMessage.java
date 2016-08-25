package carcassonne.view.secondary;

import javax.swing.JOptionPane;

/**
 * Message class for showing the user small messages.
 * @author Timur Saglam
 */
public final class GameMessage {

    /**
     * Shows a custom error message.
     * @param messageText is the message text.
     */
    public static void showError(String messageText) {
        show(messageText, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a custom plain message.
     * @param messageText is the message text.
     */
    public static void showMessage(String messageText) {
        show(messageText, JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Shows a custom warning message.
     * @param messageText is the message text.
     */
    public static void showWarning(String messageText) {
        show(messageText, JOptionPane.WARNING_MESSAGE);
    }

    private static void show(String messageText, int type) {
        JOptionPane.showMessageDialog(null, messageText, "Carcassonne", type);
    }

    private GameMessage() {
        // Private constructor for helper class.
    }
}
