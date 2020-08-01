package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import carcassonne.model.tile.TileDistribution;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.util.MouseClickListener;

/**
 * User interface that shows all tiles and how often they are used in a standard game (two players, chaos mode
 * disabled.)
 * @author Timur Saglam
 */
public class TileDistributionGUI extends JDialog {
    private static final long serialVersionUID = 1805511300999150753L;
    private static final String TITLE = "Tile Distribution";
    private static final int GRID_WIDTH = 11;
    private static final int GRID_HEIGHT = 3;
    private static final int PADDING = 5;
    private final TileDistribution distribution;
    private final List<TileQuantityPanel> quantityPanels;
    private final GameSettings settings;
    private int stackSizeMultiplier;

    /**
     * Creates the UI and shows it.
     * @param distribution is the {@link TileDistribution} to show in the UI.
     */
    public TileDistributionGUI(GameSettings settings) {
        this.settings = settings;
        distribution = settings.getTileDistribution();
        distribution.createBackup();
        quantityPanels = new ArrayList<>();
        stackSizeMultiplier = settings.getStackSizeMultiplier();
        buildPanel();
        buildWindow();
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

    /*
     * Builds the panel of tiles for all tiles that appear in the game.
     */
    private void buildPanel() {
        JPanel tilePanel = new JPanel();
        tilePanel.setBackground(Color.GRAY);
        setBackground(Color.GRAY);
        tilePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        for (TileType tileType : TileType.enabledTiles()) {
            TileQuantityPanel quantityPanel = new TileQuantityPanel(tileType, distribution.getQuantity(tileType));
            quantityPanels.add(quantityPanel);
            tilePanel.add(quantityPanel, constraints);
            constraints.gridx++;
            if (constraints.gridx >= GRID_WIDTH) {
                constraints.gridx = 0;
                constraints.gridy++;
            }
        }
        constraints.gridwidth = GRID_WIDTH;
        JPanel multiplierPanel = new JPanel();
        multiplierPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        multiplierPanel.setBackground(Color.LIGHT_GRAY);
        multiplierPanel.add(new JLabel("Tile Stack Size Multiplier: "));
        ButtonGroup group = new ButtonGroup();
        for (int i = 1; i <= 4; i++) {
            createMultiplierButton(i, multiplierPanel, group);
        }
        tilePanel.add(multiplierPanel, constraints);
        constraints.gridy++;
        tilePanel.add(createButtonPanel(), constraints);
        getContentPane().add(tilePanel);
    }

    private void createMultiplierButton(int multiplier, JPanel multiplierPanel, ButtonGroup buttonGroup) {
        JRadioButton button = new JRadioButton(multiplier + "x");
        button.setSelected(settings.getStackSizeMultiplier() == multiplier);
        button.addMouseListener((MouseClickListener) event -> stackSizeMultiplier = multiplier);
        buttonGroup.add(button);
        multiplierPanel.add(button);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addMouseListener((MouseClickListener) event -> {
            applyChangesToDistribution();
            distribution.shuffle();
            updateFromDistribution();
        });
        JButton resetButton = new JButton("Reset Distribution");
        resetButton.addMouseListener((MouseClickListener) event -> {
            distribution.reset();
            updateFromDistribution();
        });
        JButton acceptButton = new JButton("Accept");
        acceptButton.addMouseListener((MouseClickListener) event -> {
            dispose();
            settings.setStackSizeMultiplier(stackSizeMultiplier);
            applyChangesToDistribution();
        });
        buttonPanel.add(shuffleButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(acceptButton);
        return buttonPanel;
    }

    private void applyChangesToDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            distribution.setQuantity(panel.getTileType(), panel.getQuantity());
        }
    }

    private void updateFromDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            panel.setQuantity(distribution.getQuantity(panel.getTileType()));
        }
    }

    /*
     * Shows and resizes the window.
     */
    private void buildWindow() {
        setTitle(TITLE);
        setResizable(false);
        pack();
        setSize(getWidth() + PADDING * GRID_WIDTH, getHeight() + PADDING * GRID_HEIGHT);
        setLocationRelativeTo(null);
    }
}
