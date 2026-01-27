package carcassonne.view.tertiary;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.ComboPopup;

import carcassonne.model.tile.TileDistribution;
import carcassonne.model.tile.TileDistributionPreset;
import carcassonne.model.tile.TileType;
import carcassonne.view.util.ThreadingUtil;

/**
 * Handles the preset dropdown logic including hover previews and application of presets.
 * @author Timur Saglam
 */
public class PresetDropdownHandler {
    private final JComboBox<TileDistributionPreset> comboBox;
    private final TileDistribution distribution;
    private final List<TileQuantityPanel> quantityPanels;
    private Map<TileType, Integer> originalQuantitiesBeforePreview;
    private boolean isSelectingPreset = false;

    /**
     * Creates a handler for a preset dropdown.
     * @param comboBox the combo box to handle.
     * @param distribution the tile distribution to modify.
     * @param quantityPanels the panels displaying tile quantities.
     */
    public PresetDropdownHandler(JComboBox<TileDistributionPreset> comboBox, TileDistribution distribution, List<TileQuantityPanel> quantityPanels) {
        this.comboBox = comboBox;
        this.distribution = distribution;
        this.quantityPanels = quantityPanels;
        setupListeners();
    }

    private void setupListeners() {
        comboBox.addActionListener(event -> handlePresetSelection());

        Object child = comboBox.getUI().getAccessibleChild(comboBox, 0);
        if (child instanceof ComboPopup popup) {
            JList<?> list = popup.getList();
            PresetHoverListener hoverListener = new PresetHoverListener(list, this::previewPreset);
            PresetPopupListener popupListener = new PresetPopupListener(hoverListener, createPopupCallback());

            list.addMouseMotionListener(hoverListener);
            comboBox.addPopupMenuListener(popupListener);
        }
    }

    private PresetPopupListener.PresetPopupCallback createPopupCallback() {
        return new PresetPopupListener.PresetPopupCallback() {
            @Override
            public void onPopupOpening() {
                storeOriginalQuantities();
            }

            @Override
            public void onPopupClosing() {
                originalQuantitiesBeforePreview = null;
                if (!isSelectingPreset) {
                    clearAllPreviews();
                }
                isSelectingPreset = false;
            }
        };
    }

    private void storeOriginalQuantities() {
        originalQuantitiesBeforePreview = new EnumMap<>(TileType.class);
        for (TileQuantityPanel panel : quantityPanels) {
            originalQuantitiesBeforePreview.put(panel.getTileType(), panel.getQuantity());
        }
    }

    private void previewPreset(TileDistributionPreset preset) {
        if (originalQuantitiesBeforePreview == null) {
            return;
        }

        TileDistribution temporaryDistribution = new TileDistribution();
        preset.applyTo(temporaryDistribution);

        for (TileQuantityPanel panel : quantityPanels) {
            int originalQuantity = originalQuantitiesBeforePreview.get(panel.getTileType());
            int previewQuantity = temporaryDistribution.getQuantity(panel.getTileType());
            panel.showPreviewHighlight(originalQuantity, previewQuantity);
        }
    }

    private void clearAllPreviews() {
        for (TileQuantityPanel panel : quantityPanels) {
            panel.clearPreviewHighlight();
        }
    }

    private void handlePresetSelection() {
        if (comboBox.getSelectedItem() instanceof TileDistributionPreset preset) {
            isSelectingPreset = true; // avoids resetting preview and thus flickering

            Map<TileType, Integer> quantitiesBeforeApplication;
            if (originalQuantitiesBeforePreview != null) {
                quantitiesBeforeApplication = new EnumMap<>(originalQuantitiesBeforePreview);
            } else {
                quantitiesBeforeApplication = captureCurrentQuantities();
            }

            ThreadingUtil.runAndCallback(() -> preset.applyTo(distribution), () -> {
                for (TileQuantityPanel panel : quantityPanels) {
                    panel.setQuantity(distribution.getQuantity(panel.getTileType()));
                }
                highlightChangedTiles(quantitiesBeforeApplication);
            });
        }
    }

    private Map<TileType, Integer> captureCurrentQuantities() {
        Map<TileType, Integer> quantities = new EnumMap<>(TileType.class);
        for (TileQuantityPanel panel : quantityPanels) {
            quantities.put(panel.getTileType(), panel.getQuantity());
        }
        return quantities;
    }

    private void highlightChangedTiles(Map<TileType, Integer> quantitiesBeforeApplication) {
        for (TileQuantityPanel panel : quantityPanels) {
            int previousQuantity = quantitiesBeforeApplication.getOrDefault(panel.getTileType(), panel.getTileType().getAmount());
            int newQuantity = panel.getQuantity();
            panel.highlightChange(previousQuantity, newQuantity);
        }
    }
}