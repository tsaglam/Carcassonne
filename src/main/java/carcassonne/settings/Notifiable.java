package carcassonne.settings;

/**
 * Interface for the notification of UI components.
 * @author Timur Saglam
 */
public interface Notifiable {
    /**
     * Notifies the notifiable UI element about changes in the game options.
     */
    public void notifyChange();
}
