package carcassonne.view;

/**
 * Interface for the direct notification of view components.
 * @author Timur Saglam
 */
public interface NotifiableView {
    /**
     * Notifies the notifiable view element about changes in the game options. The changes need to be retrieved directly.
     */
    void notifyChange();
}
