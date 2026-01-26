package carcassonne.view.tertiary;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JList;

import carcassonne.model.tile.TileDistributionPreset;

/**
 * Handles hover preview logic for preset dropdowns.
 */
public class PresetHoverListener extends MouseMotionAdapter {

    private final JList<?> comboBoxList;
    private final PresetPreviewCallback callback;

    private boolean active = false;
    private TileDistributionPreset lastHovered = null;

    public PresetHoverListener(JList<?> comboBoxList, PresetPreviewCallback callback) {
        this.comboBoxList = comboBoxList;
        this.callback = callback;
    }

    public void activate() {
        active = true;
        lastHovered = null;
    }

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
                callback.previewPreset(preset);
            }
        }
    }

    /**
     * Small callback interface to avoid tight coupling.
     */
    public interface PresetPreviewCallback {
        void previewPreset(TileDistributionPreset preset);
    }
}
