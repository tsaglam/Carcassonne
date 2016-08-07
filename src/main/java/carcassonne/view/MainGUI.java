package carcassonne.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;
import carcassonne.view.tileLabel.TileLabel;

/**
 * The main GUI class, extending a JPanel.
 * @author Timur Saglam
 */
public class MainGUI {

    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel panelBottom;
    private JPanel panelTop;
    private MainMenuBar menuBar;
    private GameOptions options;
    private MainController controller;
    private TileLabel[][] labelGrid;
    private JLabel[][] meepleGrid;
    private GridBagConstraints constraints;
    private int gridWidth;
    private int gridHeight;
    private int meepleGridWidth;
    private int meepleGridHeight;
    private Tile defaultTile;

    public MainGUI(MainController controller) {
        this.controller = controller;
        options = GameOptions.getInstance();
        buildPanelBack();
        buildPanelFront();
        buildLayeredPane();
        buildFrame();
    }

    /**
     * Rebuilds the label grid and the meeple grid if the game should be restarted.
     */
    public void rebuildGrids() {
        ImageIcon imageEmpty = new ImageIcon(options.buildImagePath(TerrainType.OTHER));
        for (int y = 0; y < meepleGridHeight; y++) {
            for (int x = 0; x < meepleGridWidth; x++) {
                if (x < gridWidth && y < gridHeight) {
                    labelGrid[x][y].setIcon(defaultTile.getImage());
                }
                meepleGrid[x][y] = new JLabel(imageEmpty);
            }
        }
    }

    /**
     * Draws meeple on a tile on the grid.
     * @param tile is the tile where the meeple gets drawn.
     * @param position is the position on the tile where the meeple gets drawn.
     * @param owner is the player that owns the meeple.
     */
    public void setMeeple(Tile tile, GridDirection position, Player owner) {
        int xpos = GridDirection.addX(tile.getX() * 3 + 1, position);
        int ypos = GridDirection.addY(tile.getY() * 3 + 1, position);
        ImageIcon icon = new ImageIcon(options.buildImagePath(tile.getTerrain(position), owner.getNumber()));
        meepleGrid[xpos][ypos].setIcon(icon);
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    /**
     * Draws the tile on a specific position on the GUI.
     * @param tile is the tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void set(Tile tile, int x, int y) {
        if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
            labelGrid[x][y].setIcon(tile.getImage());
        } else {
            throw new IllegalArgumentException("Invalid label grid position (" + x + ", " + y + ")");
        }
    }

    private void buildFrame() {
        frame = new JFrame();
        menuBar = new MainMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        // frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildLayeredPane() {
        layeredPane = new JLayeredPane();
        OverlayLayout layout = new OverlayLayout(layeredPane);
        layeredPane.setLayout(layout);
        layeredPane.add(panelBottom, new Integer(0), 1);
        layeredPane.add(panelTop, new Integer(0), 0);
    }

    private void buildMeepleGrid() {
        constraints = new GridBagConstraints();
        ImageIcon imageEmpty = new ImageIcon(options.buildImagePath(TerrainType.OTHER));
        meepleGridWidth = options.gridWidth * 3;
        meepleGridHeight = options.gridHeight * 3;
        constraints.weightx = 1;
        constraints.weighty = 1;
        meepleGrid = new JLabel[meepleGridWidth][meepleGridHeight]; // build array of labels.
        for (int x = 0; x < meepleGridWidth; x++) {
            for (int y = 0; y < meepleGridHeight; y++) {
                meepleGrid[x][y] = new JLabel(imageEmpty);
                constraints.gridx = x;
                constraints.gridy = y;
                panelTop.add(meepleGrid[x][y], constraints); // add label with constraints
            }
        }
    }

    private void buildPanelBack() {
        panelBottom = new JPanel();
        panelBottom.setSize(options.gridResolutionWidth, options.gridResolutionHeight);
        panelBottom.setBackground(options.colorGUImain);
        panelBottom.setLayout(new GridBagLayout());
        buildTileGrid();
    }

    private void buildPanelFront() {
        panelTop = new JPanel();
        panelTop.setSize(options.gridResolutionWidth, options.gridResolutionHeight);
        panelTop.setBackground(new Color(0, 0, 0, 0));
        panelTop.setLayout(new GridBagLayout());
        buildMeepleGrid();
    }

    /*
     * Creates the grid of labels.
     */
    private void buildTileGrid() {
        defaultTile = TileFactory.create(TileType.Null);
        constraints = new GridBagConstraints();
        gridWidth = options.gridWidth;
        gridHeight = options.gridHeight;
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
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