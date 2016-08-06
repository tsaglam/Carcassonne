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
import carcassonne.model.Meeple;
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
public class LayeredGUI {
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

    public LayeredGUI(MainController controller) {
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
    public void rebuildLabelGrid() { // TODO (MEDIUM) rename to rebuildGrid()
        ImageIcon empty = new ImageIcon(options.buildImagePath(TerrainType.OTHER));
        for (int y = 0; y < meepleGridHeight; y++) {
            for (int x = 0; x < meepleGridWidth; x++) {
                if (x < gridWidth && y < gridHeight) {
                    labelGrid[x][y].setIcon(defaultTile.getImage());
                }
                meepleGrid[x][y] = new JLabel(new ImageIcon(options.buildImagePath(TerrainType.OTHER)));
            }
        }
    }

    /**
     * Draws meeple on a tile on the grid.
     * @param meeple is the meeple to draw.
     * @param x is the x position of the tile.
     * @param y is the y position of the tile.
     * @param position is the position of the meeple on the specific tile.
     * @param playerNumber TODO
     * @param terrain TODO
     */
    public void set(Meeple meeple, int x, int y, GridDirection position, int playerNumber, TerrainType terrain) {
        ;
        int xpos = GridDirection.addX(x * 3 + 1, position);
        int ypos = GridDirection.addY(y * 3 + 1, position);
        meepleGrid[xpos][ypos].setIcon(new ImageIcon(options.buildImagePath(terrain, playerNumber)));
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

    /**
     * Main method for testing. //TODO (LOWEST) remove main method sometime.
     * @param args are the arguments.
     * @throws InterruptedException exception.
     */
    public static void main(String[] args) throws InterruptedException {
        LayeredGUI gui = new LayeredGUI(null);
        int pause = 15;
        Thread.sleep(50 * pause);

        Tile tile = TileFactory.create(TileType.CastleWallCurveRight);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 12; x++) {
                gui.set(tile, x, y);
                for (GridDirection dir : GridDirection.values()) {
                    Thread.sleep(pause / 3);
                    gui.set(new Meeple(new Player()), x, y, dir, 1, TerrainType.ROAD);
                }
                tile.rotateRight();
                Thread.sleep(pause);
            }
        }
        Thread.sleep(100 * pause);
        gui.rebuildLabelGrid();
        Thread.sleep(100 * pause);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 12; x++) {
                gui.set(tile, x, y);
                Thread.sleep(pause);
                gui.set(new Meeple(new Player()), x, y, GridDirection.MIDDLE, 1, TerrainType.CASTLE);
                tile.rotateRight();
                Thread.sleep(pause);
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

    private void buildLayeredPane() {
        layeredPane = new JLayeredPane();
        OverlayLayout layout = new OverlayLayout(layeredPane);
        layeredPane.setLayout(layout);
        layeredPane.add(panelBottom, new Integer(0), 1);
        layeredPane.add(panelTop, new Integer(0), 0);
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

    private void buildMeepleGrid() {
        constraints = new GridBagConstraints();
        meepleGridWidth = options.gridWidth * 3;
        meepleGridHeight = options.gridHeight * 3;
        constraints.weightx = 1;
        constraints.weighty = 1;
        meepleGrid = new JLabel[meepleGridWidth][meepleGridHeight]; // build array of labels.
        for (int x = 0; x < meepleGridWidth; x++) {
            for (int y = 0; y < meepleGridHeight; y++) {
                meepleGrid[x][y] = new JLabel(new ImageIcon(options.buildImagePath(TerrainType.OTHER)));
                constraints.gridx = x;
                constraints.gridy = y;
                panelTop.add(meepleGrid[x][y], constraints); // add label with constraints
            }
        }
    }

}