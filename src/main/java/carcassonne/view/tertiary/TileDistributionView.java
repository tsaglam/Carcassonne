package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import carcassonne.model.tile.TileDistribution;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.view.util.MouseClickListener;
import carcassonne.view.util.ThreadingUtil;

/**
 * User interface that shows all tiles and how often they are used in a standard game (two players, chaos mode
 * disabled.)
 * @author Timur Saglam
 */
public class TileDistributionView extends JDialog {
    private static final long serialVersionUID = 1805511300999150753L;
    private String MULTIPLIER = "Tile Stack Size Multiplier: ";
    private String BRACKET = "\t (";
    private String STACK_SIZE = " tiles on the stack)";
    private String SHUFFLE = "Shuffle";
    private String RESET = "Reset";
    private String ACCEPT = "Accept";
    private String TITLE = "Tile Distribution";
    private static final int GRID_WIDTH = 11;
    private static final int GRID_HEIGHT = 3;
    private static final int PADDING = 5;
    private final TileDistribution distribution;
    private final List<TileQuantityPanel> quantityPanels;
    private final GameSettings settings;
    private int stackSizeMultiplier;
    private JLabel sizeLabel;

    /**
     * Creates the UI and shows it.
     * @param distribution is the {@link TileDistribution} to show in the UI.
     */
    public TileDistributionView(GameSettings settings) {
		initResource();
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

    /**
     * Recalculates the stack size preview.
     */
    public void updateStackSizePreview() {
        if (sizeLabel != null) {
            Integer size = calculateStackSize(stackSizeMultiplier);
            sizeLabel.setText(BRACKET + size + STACK_SIZE);
            validate();
        }
    }

    /*
     * Builds the main panel and lays out its subcomponents in a grid.
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
        builtTilePanels(tilePanel, constraints);
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        tilePanel.add(buildMultiplierPanel(), constraints);
        constraints.gridwidth = 1;
        constraints.gridx = 8;
        for (JButton button : createButtons()) {
            tilePanel.add(button, constraints);
            constraints.gridx++;
        }
        getContentPane().add(tilePanel);
    }

    private void builtTilePanels(JPanel tilePanel, GridBagConstraints constraints) {
        for (TileType tileType : TileType.enabledTiles()) {
            TileQuantityPanel quantityPanel = new TileQuantityPanel(tileType, distribution.getQuantity(tileType), this);
            quantityPanels.add(quantityPanel);
            tilePanel.add(quantityPanel, constraints);
            constraints.gridx++;
            if (constraints.gridx >= GRID_WIDTH) {
                constraints.gridx = 0;
                constraints.gridy++;
            }
        }
    }

    private JPanel buildMultiplierPanel() {
        JPanel multiplierPanel = new JPanel();
        multiplierPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        multiplierPanel.setBackground(Color.LIGHT_GRAY);
        multiplierPanel.add(new JLabel(MULTIPLIER));
        ButtonGroup group = new ButtonGroup();
        sizeLabel = new JLabel();
        updateStackSizePreview();
        for (int multiplier = 1; multiplier <= 4; multiplier++) {
            createMultiplierButton(multiplier, multiplierPanel, group);
        }
        multiplierPanel.add(sizeLabel);
        return multiplierPanel;
    }

    private void createMultiplierButton(int multiplier, JPanel multiplierPanel, ButtonGroup group) {
        JRadioButton button = new JRadioButton(multiplier + "x");
        button.setSelected(settings.getStackSizeMultiplier() == multiplier);
        button.addMouseListener((MouseClickListener) event -> {
            stackSizeMultiplier = multiplier;
            updateStackSizePreview();
        });
        group.add(button);
        multiplierPanel.add(button);
    }

    private List<JButton> createButtons() {
        JButton shuffleButton = new JButton(SHUFFLE);
        shuffleButton.addMouseListener((MouseClickListener) event -> {
            ThreadingUtil.runAndCallback(() -> {
                applyChangesToDistribution();
                distribution.shuffle();
            }, this::updateFromDistribution);
        });
        JButton resetButton = new JButton(RESET);
        resetButton.addMouseListener((MouseClickListener) event -> ThreadingUtil.runAndCallback(distribution::reset, this::updateFromDistribution));
        JButton acceptButton = new JButton(ACCEPT);
        acceptButton.addMouseListener((MouseClickListener) event -> {
            dispose();
            ThreadingUtil.runInBackground(() -> {
                settings.setStackSizeMultiplier(stackSizeMultiplier);
                applyChangesToDistribution();
            });
        });
        return List.of(shuffleButton, resetButton, acceptButton);
    }

    private void applyChangesToDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            if (panel.getQuantity() >= 0) {
                distribution.setQuantity(panel.getTileType(), panel.getQuantity());
            }
        }
    }

    private void updateFromDistribution() {
        for (TileQuantityPanel panel : quantityPanels) {
            panel.setQuantity(distribution.getQuantity(panel.getTileType()));
        }
    }

    private int calculateStackSize(int multiplier) {
        int size = 0;
        for (TileQuantityPanel panel : quantityPanels) {
            if (panel.getQuantity() >= 0) {
                size += panel.getQuantity();
            } else {
                size += distribution.getQuantity(panel.getTileType());
            }

        }
        return size * multiplier;
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
        setModalityType(ModalityType.APPLICATION_MODAL);
    }

	private void initResource() {
		LoadTextTileDistributionView properties = new LoadTextTileDistributionView();
		MULTIPLIER	= properties.get("MULTIPLIER");
		BRACKET		= properties.get("BRACKET");
		STACK_SIZE	= properties.get("STACK_SIZE");
		SHUFFLE		= properties.get("SHUFFLE");
		RESET		= properties.get("RESET");
		ACCEPT		= properties.get("ACCEPT");
		TITLE		= properties.get("TITLE");
	}
}
