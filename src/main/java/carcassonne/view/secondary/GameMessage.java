package carcassonne.view.secondary;

import javax.swing.JOptionPane;

/**
 * Message class for showing the user small messages.
 * @author Timur Saglam
 */
public final class GameMessage {
    private static final String TITLE = "Carcassonne";

    private GameMessage() {
        // Private constructor for helper class.
    }

    /**
     * Asks the user for text input.
     * @param messageText is the custom message.
     * @return the user input string.
     */
    public static String getUserInput(String messageText) {
        return JOptionPane.showInputDialog(null, messageText, TITLE, JOptionPane.QUESTION_MESSAGE);
    }

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
        JOptionPane.showMessageDialog(null, messageText, TITLE, type);
    }
}
