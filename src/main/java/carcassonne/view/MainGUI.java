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
    /**
     * Main method for testing. //TODO (LOWEST) remove main method sometime.
     * @param args are the arguments.
     * @throws InterruptedException exception.
     */
    public static void main(String[] args) throws InterruptedException {
        MainGUI gui = new MainGUI(null);
        int pause = 15;
        Thread.sleep(50 * pause);
        TerrainType[] terrain = { TerrainType.ROAD, TerrainType.CASTLE, TerrainType.MONASTRY };
        Tile tile = TileFactory.create(TileType.CastleWallCurveRight);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 12; x++) {
                gui.set(tile, x, y);
                for (GridDirection dir : GridDirection.values()) {
                    Thread.sleep(pause / 3);
                    gui.setMeeple(x, y, dir, y % 4, terrain[x % 3]);
                }
                tile.rotateRight();
                Thread.sleep(pause);
            }
        }
        // Thread.sleep(100 * pause);
        // gui.rebuildLabelGrid();
        // Thread.sleep(100 * pause);
        // for (int y = 0; y < 7; y++) {
        // for (int x = 0; x < 12; x++) {
        // gui.set(tile, x, y);
        // Thread.sleep(pause);
        // for (GridDirection dir : GridDirection.values()) {
        // Thread.sleep(pause/3);
        // gui.setMeeple(x, y, dir, x % 4, terrain[y % 3]);
        // }
        // tile.rotateRight();
        // Thread.sleep(pause);
        // }
        // }
    }

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
     * @param meeple is the meeple to draw.
     * @param x is the x position of the tile.
     * @param y is the y position of the tile.
     * @param position is the position of the meeple on the specific tile.
     * @param playerNumber is the number of the current player.
     * @param terrain is the type of the meeple, depending on the terrain.
     */
    public void setMeeple(int x, int y, GridDirection position, int playerNumber, TerrainType terrain) {
        // TODO (HIGHEST) let this method take only a player and a tile.
        int xpos = GridDirection.addX(x * 3 + 1, position);
        int ypos = GridDirection.addY(y * 3 + 1, position);
        meepleGrid[xpos][ypos].setIcon(new ImageIcon(options.buildImagePath(terrain, playerNumber)));
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