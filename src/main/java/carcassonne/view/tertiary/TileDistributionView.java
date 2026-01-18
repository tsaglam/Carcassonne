package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import carcassonne.model.tile.TileDistribution;
import carcassonne.model.tile.TileDistributionPreset;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.view.util.MouseClickListener;
import carcassonne.view.util.ThreadingUtil;

/**
 * User interface that shows all tiles and how often they are used in a standard game.
 * @author Timur Saglam
 */
public class TileDistributionView extends JDialog {
    private static final long serialVersionUID = 1805511300999150753L;
    private static final String MULTIPLIER_LABEL = "Tile Stack Size Multiplier: ";
    private static final String STACK_SIZE_PREFIX = "\t (";
    private static final String STACK_SIZE_SUFFIX = " tiles on the stack)";
    private static final String PRESETS_LABEL = "Tile Presets:";
    private static final String SHUFFLE = "Shuffle";
    private static final String ACCEPT = "Accept";
    private static final String TITLE = "Tile Distribution";
    private static final int GRID_WIDTH = 11;
    private static final int GRID_HEIGHT = 3;
    private static final int PADDING = 5;
    private static final int MAX_MULTIPLIER = 6;

    private final TileDistribution distribution;
    private final List<TileQuantityPanel> quantityPanels;
    private final GameSettings settings;
    private int stackSizeMultiplier;
    private JLabel stackSizeLabel;

    /**
     * Creates the UI and shows it.
     * @param settings are the game settings.
     */
    public TileDistributionView(GameSettings settings) {
        this.settings = settings;
        distribution = settings.getTileDistribution();
        distribution.createBackup();
        quantityPanels = new ArrayList<>();
        stackSizeMultiplier = settings.getStackSizeMultiplier();
        buildUI();
        setupWindowListeners();
    }

    /**
     * Recalculates the stack size preview.
     */
    public void updateStackSizePreview() {
        if (stackSizeLabel != null) {
            int totalSize = calculateTotalStackSize(stackSizeMultiplier);
            stackSizeLabel.setText(STACK_SIZE_PREFIX + totalSize + STACK_SIZE_SUFFIX);
            validate();
        }
    }

    private void buildUI() {
        JPanel mainPanel = createMainPanel();
        GridBagConstraints constraints = createInitialConstraints();

        addTilePanels(mainPanel, constraints);
        addMultiplierPanel(mainPanel, constraints);
        addPresetControls(mainPanel, constraints);
        addActionButtons(mainPanel, constraints);

        getContentPane().add(mainPanel);
        configureWindow();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setLayout(new GridBagLayout());
        setBackground(Color.GRAY);
        return panel;
    }

    private GridBagConstraints createInitialConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        return constraints;
    }

    private void addTilePanels(JPanel mainPanel, GridBagConstraints constraints) {
        for (TileType tileType : TileType.enabledTiles()) {
            TileQuantityPanel panel = new TileQuantityPanel(tileType, distribution.getQuantity(tileType), this);
            quantityPanels.add(panel);
            mainPanel.add(panel, constraints);

            constraints.gridx++;
            if (constraints.gridx >= GRID_WIDTH) {
                constraints.gridx = 0;
                constraints.gridy++;
            }
        }
    }

    private void addMultiplierPanel(JPanel mainPanel, GridBagConstraints constraints) {
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buildMultiplierPanel(), constraints);
    }

    private void addPresetControls(JPanel mainPanel, GridBagConstraints constraints) {
        constraints.gridwidth = 1;
        constraints.gridx = 6;
        constraints.fill = GridBagConstraints.NONE;
        mainPanel.add(createPresetLabel(), constraints);

        constraints.gridwidth = 2;
        constraints.gridx = 7;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(createPresetDropdown(), constraints);
    }

    private void addActionButtons(JPanel mainPanel, GridBagConstraints constraints) {
        constraints.gridwidth = 1;
        constraints.gridx = 9;
        constraints.fill = GridBagConstraints.NONE;

        for (JButton button : createActionButtons()) {
            mainPanel.add(button, constraints);
            constraints.gridx++;
        }
    }

    private JPanel buildMultiplierPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.add(new JLabel(MULTIPLIER_LABEL));

        ButtonGroup buttonGroup = new ButtonGroup();
        stackSizeLabel = new JLabel();
        Dimension size = new Dimension(160, 24);
        stackSizeLabel.setPreferredSize(size);
        stackSizeLabel.setMinimumSize(size);
        stackSizeLabel.setMaximumSize(size);
        updateStackSizePreview();

        for (int multiplier = 1; multiplier <= MAX_MULTIPLIER; multiplier++) {
            addMultiplierButton(panel, buttonGroup, multiplier);
        }

        panel.add(stackSizeLabel);
        return panel;
    }

    private void addMultiplierButton(JPanel panel, ButtonGroup buttonGroup, int multiplier) {
        JRadioButton button = new JRadioButton(multiplier + "x");
        button.setSelected(settings.getStackSizeMultiplier() == multiplier);
        button.addMouseListener((MouseClickListener) event -> {
            stackSizeMultiplier = multiplier;
            updateStackSizePreview();
        });
        buttonGroup.add(button);
        panel.add(button);
    }

    private JLabel createPresetLabel() {
        JLabel label = new JLabel(PRESETS_LABEL);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(java.awt.Font.BOLD));
        label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        return label;
    }

    private JComboBox<TileDistributionPreset> createPresetDropdown() {
        JComboBox<TileDistributionPreset> comboBox = new JComboBox<>(TileDistributionPreset.values());
        new PresetDropdownHandler(comboBox, distribution, quantityPanels);
        return comboBox;
    }

    private List<JButton> createActionButtons() {
        JButton shuffleButton = createShuffleButton();
        JButton acceptButton = createAcceptButton();
        return List.of(shuffleButton, acceptButton);
    }

    private JButton createShuffleButton() {
        JButton button = new JButton(SHUFFLE);
        button.addMouseListener((MouseClickListener) event -> ThreadingUtil.runAndCallback(() -> {
            applyUserChangesToDistribution();
            distribution.shuffle();
        }, this::updateFromDistribution));
        return button;
    }

    private JButton createAcceptButton() {
        JButton button = new JButton(ACCEPT);
        button.addMouseListener((MouseClickListener) event -> {
            dispose();
            ThreadingUtil.runInBackground(() -> {
                settings.setStackSizeMultiplier(stackSizeMultiplier);
                applyUserChangesToDistribution();
            });
        });
        return button;
    }

    private void applyUserChangesToDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            int quantity = panel.getQuantity();
            if (quantity >= 0) {
                distribution.setQuantity(panel.getTileType(), quantity);
            }
        }
    }

    private void updateFromDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            panel.setQuantity(distribution.getQuantity(panel.getTileType()));
        }
    }

    private int calculateTotalStackSize(int multiplier) {
        int totalSize = 0;
        for (TileQuantityPanel panel : quantityPanels) {
            int quantity = panel.getQuantity();
            if (quantity >= 0) {
                totalSize += quantity;
            } else {
                totalSize += distribution.getQuantity(panel.getTileType());
            }
        }
        return totalSize * multiplier;
    }

    private void setupWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                distribution.restoreLastBackup();
            }

            @Override
            public void windowActivated(WindowEvent event) {
                distribution.createBackup();
                updateFromDistribution();
            }
        });
    }

    private void configureWindow() {
        setTitle(TITLE);
        setResizable(false);
        pack();
        setSize(getWidth() + PADDING * GRID_WIDTH, getHeight() + PADDING * GRID_HEIGHT);
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
    }
}