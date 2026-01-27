package carcassonne.view.tertiary;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * Handles popup menu lifecycle events for preset dropdowns. Manages activation and deactivation of hover previews, and
 * cleanup when the popup closes.
 * @author Timur Saglam
 */
public class PresetPopupListener implements PopupMenuListener {

    private final PresetHoverListener hoverListener;
    private final PresetPopupCallback callback;

    /**
     * Creates a popup listener for a preset dropdown.
     * @param hoverListener the hover listener to activate/deactivate.
     * @param callback the callback for popup lifecycle events.
     */
    public PresetPopupListener(PresetHoverListener hoverListener, PresetPopupCallback callback) {
        this.hoverListener = hoverListener;
        this.callback = callback;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
        callback.onPopupOpening();
        hoverListener.activate();
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
        cleanupAfterPopupClose();
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent event) {
        cleanupAfterPopupClose();
    }

    private void cleanupAfterPopupClose() {
        hoverListener.deactivate();
        callback.onPopupClosing();
    }

    /**
     * Callback interface for popup lifecycle events.
     */
    public interface PresetPopupCallback {
        /**
         * Called when the popup menu is about to open.
         */
        void onPopupOpening();

        /**
         * Called when the popup menu is closing or has been canceled.
         */
        void onPopupClosing();
    }
}