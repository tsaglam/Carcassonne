package carcassonne.view;

/**
 * Interface for the direct notification of UI components.
 * @author Timur Saglam
 */
public interface NotifiableUI {
    /**
     * Notifies the notifiable UI element about changes in the game options. The changes need to be retrieved directly.
     */
    void notifyChange();
}
