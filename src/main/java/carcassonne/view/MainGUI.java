package carcassonne.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;
import carcassonne.view.tileLabel.TileLabel;

/**
 * The main GUI class, extending a JPanel.
 * @author Timur
 */
public class MainGUI extends JPanel {

    private static final long serialVersionUID = -8750891542665009043L;

    /**
     * Main method for testing. //TODO (LOWEST) remove main method sometime.
     * @param args are the arguments.
     * @throws InterruptedException exception
     */
    public static void main(String[] args) throws InterruptedException {
        int pause = 15;
        MainGUI gui = new MainGUI(null);
        Tile tile = TileFactory.create(TileType.CastleWallCurveRight);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 12; x++) {
                gui.set(tile, x, y);
                tile.rotateRight();
                Thread.sleep(pause);
            }
        }
        gui.rebuildLabelGrid();
        Thread.sleep(100*pause);
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 12; x++) {
                gui.set(tile, x, y);
                tile.rotateRight();
                Thread.sleep(pause);
            }
        }
    }

    private MainController controller;
    private GameOptions options;
    private JFrame frame;
    private MainMenuBar menuBar;
    private TileLabel[][] labelGrid;
    private GridBagConstraints constraints;
    private int gridWidth;
    private int gridHeight;

    /**
     * Basic constructor.
     */
    public MainGUI(MainController controller) {
        super(new GridBagLayout());
        this.controller = controller;
        options = GameOptions.getInstance();
        constraints = new GridBagConstraints();
        buildLabelGrid();
        buildFrame();
    }

    /**
     * Rebuilds the label grid if the game should be restarted.
     */
    public void rebuildLabelGrid() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                remove(labelGrid[x][y]);
            }
        }
        buildLabelGrid();
        frame.pack();
    }

    /**
     * Draws meeple on a tile on the grid.
     * @param meeple is the meeple to draw.
     * @param x is the x position of the tile.
     * @param y is the y position of the tile.
     * @param position is the position of the meeple on the specific tile.
     */
    public void set(Meeple meeple, int x, int y, GridDirection position) {
        // TODO (MEDIUM) implement paint meeple method.
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

    /*
     * Builds the frame.
     */
    private void buildFrame() {
        menuBar = new MainMenuBar();
        frame = new JFrame("Carcasonne");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        setBackground(options.colorGUImain); // grey
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setResizable(false);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
                add(labelGrid[x][y], constraints); // add label with constraints
            }
        }
    }

}
