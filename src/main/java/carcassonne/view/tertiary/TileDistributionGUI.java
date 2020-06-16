package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import carcassonne.model.tile.TileDistribution;
import carcassonne.model.tile.TileType;

/**
 * User interface that shows all tiles and how often they are used in a standard game (two players, chaos mode
 * disabled.)
 * @author Timur Saglam
 */
public class TileDistributionGUI extends JDialog {
    private static final long serialVersionUID = 1805511300999150753L;
    private static final String TITLE = "Standard Two-Player Game Tile Distribution";
    private static final int GRID_WIDTH = 11;
    private static final int GRID_HEIGHT = 3;
    private static final int PADDING = 5;
    private final TileDistribution distribution;
    private final List<TileQuantityPanel> quantityPanels;

    /**
     * Creates the UI and shows it.
     * @param distribution is the {@link TileDistribution} to show in the UI.
     */
    public TileDistributionGUI(TileDistribution distribution) {
        this.distribution = distribution;
        distribution.createBackup();
        quantityPanels = new ArrayList<>();
        buildPanel();
        buildWindow();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                distribution.restoreLastBackup();
            }
        });
    }

    /*
     * Builds the panel of tiles for all tiles that appear in the game.
     */
    private void buildPanel() {
        JPanel tilePanel = new JPanel();
        tilePanel.setBackground(Color.GRAY);
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
        buildButtons(tilePanel, constraints);
        getContentPane().add(tilePanel);
    }

    private void buildButtons(JPanel tilePanel, GridBagConstraints constraints) {
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                quantityPanels.forEach(it -> distribution.setQuantity(it.getTileType(), it.getQuantity()));
                distribution.shuffle();
                quantityPanels.forEach(it -> it.setQuantity(distribution.getQuantity(it.getTileType())));
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                distribution.reset();
                quantityPanels.forEach(it -> it.setQuantity(distribution.getQuantity(it.getTileType())));
            }
        });
        JButton acceptButton = new JButton("Accept");
        acceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                dispose();
                quantityPanels.forEach(it -> distribution.setQuantity(it.getTileType(), it.getQuantity()));
            }
        });
        constraints.gridx = 4;
        tilePanel.add(shuffleButton, constraints);
        constraints.gridx = 5;
        tilePanel.add(resetButton, constraints);
        constraints.gridx = 6;
        tilePanel.add(acceptButton, constraints);
    }

    /*
     * Shows and resizes the window.
     */
    private void buildWindow() {
        setTitle(TITLE);
        setVisible(true);
        pack();
        setSize(getWidth() + PADDING * GRID_WIDTH, getHeight() + PADDING * GRID_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}
