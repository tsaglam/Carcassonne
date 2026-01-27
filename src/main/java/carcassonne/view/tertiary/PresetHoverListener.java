package carcassonne.view.tertiary;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JList;

import carcassonne.model.tile.TileDistributionPreset;

/**
 * Handles hover preview logic for preset dropdowns.
 * @author Timur Saglam
 */
public class PresetHoverListener extends MouseMotionAdapter {

    private final JList<?> comboBoxList;
    private final PresetDropdownHandler handler;

    private boolean active = false;
    private TileDistributionPreset lastHovered = null;

    /**
     * Creates a hover listener for a preset dropdown.
     * @param comboBoxList the list component of the combo box.
     * @param handler the handler for preview actions.
     */
    public PresetHoverListener(JList<?> comboBoxList, PresetDropdownHandler handler) {
        this.comboBoxList = comboBoxList;
        this.handler = handler;
    }

    /**
     * Activates the hover listener.
     */
    public void activate() {
        active = true;
        lastHovered = null;
    }

    /**
     * Deactivates the hover listener.
     */
    public void deactivate() {
        active = false;
        lastHovered = null;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (!active) {
            return;
        }

        int index = comboBoxList.locationToIndex(event.getPoint());
        if (index >= 0 && index < TileDistributionPreset.values().length) {
            TileDistributionPreset preset = TileDistributionPreset.values()[index];
            if (preset != lastHovered) {
                lastHovered = preset;
                handler.previewPreset(preset);
            }
        }
    }
}