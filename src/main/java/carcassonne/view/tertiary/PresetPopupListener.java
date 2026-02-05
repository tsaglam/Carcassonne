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
    private final PresetDropdownHandler handler;

    /**
     * Creates a popup listener for a preset dropdown.
     * @param hoverListener the hover listener to activate/deactivate.
     * @param handler the handler for popup lifecycle actions.
     */
    public PresetPopupListener(PresetHoverListener hoverListener, PresetDropdownHandler handler) {
        this.hoverListener = hoverListener;
        this.handler = handler;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
        handler.onPopupOpening();
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
        handler.onPopupClosing();
    }
}