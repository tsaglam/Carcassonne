package carcassonne.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;
import carcassonne.view.tileLabel.TileLabel;

public class LayeredGUI {
    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel panelBottom;
    private JPanel panelTop;
    private MainMenuBar menuBar;
    private GameOptions options;
    // added from MainGUI:
    private MainController controller;
    private TileLabel[][] labelGrid;
    private GridBagConstraints constraints;
    private int gridWidth;
    private int gridHeight;

    public LayeredGUI(MainController controller) {
        this.controller = controller;
        options = GameOptions.getInstance();
        constraints = new GridBagConstraints();
        // panel back:
        panelBottom = new JPanel();
        panelBottom.setSize(options.gridResolutionWidth, options.gridResolutionHeight);
        panelBottom.setBackground(options.colorGUImain);
        panelBottom.setLayout(new GridBagLayout());
        buildLabelGrid();
        panelBottom.setSize(panelBottom.getMinimumSize());
        panelBottom.revalidate();
        // panel front:
        panelTop = new JPanel();
        panelTop.setSize(options.gridResolutionWidth, options.gridResolutionHeight - 1);
        panelTop.setBackground(new Color(0, 0, 100, 100));
        panelTop.setLayout(new GridBagLayout());
        // layered pane:
        layeredPane = new JLayeredPane();
        layeredPane.add(panelBottom, 0, 0);
        layeredPane.add(panelTop, 1, 0);
        // frame building:
        frame = new JFrame();
        menuBar = new MainMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setPreferredSize(new Dimension(options.gridResolutionWidth, options.resolutionHeightWindow - 29));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // LATER:
        buildPanelBack();
        buildPanelFront();
        buildLayeredPane();
        buildFrame();
    }

    public static void main(String[] args) {
        new LayeredGUI(null);
    }

    private void buildPanelBack() {
        // TODO (HIGHEST) use method.
        buildLabelGrid();
    }

    private void buildPanelFront() {
        // TODO (HIGHEST) use method.
    }

    private void buildLayeredPane() {
        // TODO (HIGHEST) use method.
    }

    private void buildFrame() {
        // TODO (HIGHEST) use method.
    }

    /*
     * Creates the grid of labels.
     */
    private void buildLabelGrid() {
        gridWidth = options.gridWidth;
        gridHeight = options.gridHeight;
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
        Tile defaultTile = TileFactory.create(TileType.Null);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new TileLabel(defaultTile.getImage(), controller, x, y);
                constraints.gridx = x;
                constraints.gridy = y;
                panelBottom.add(labelGrid[x][y], constraints); // add label with constraints
            }
        }
    }

}