package carcassonne.view.main;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.control.Notifiable;
import carcassonne.control.PaintShop;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.view.main.menubar.MainMenuBar;
import carcassonne.view.main.menubar.Scoreboard;

/**
 * The main GUI class.
 * @author Timur Saglam
 */
public class MainGUI implements Notifiable {

    private GridBagConstraints constraints;
    private final MainController controller;
    private final JFrame frame;
    private int gridHeight;
    private int gridWidth;
    private TileLabel[][] labelGrid;
    private JLayeredPane layeredPane;
    private MeepleLabel[][] meepleGrid;
    private int meepleGridHeight;
    private int meepleGridWidth;
    private final GameOptions options;
    private JPanel panelBottom;
    private JPanel panelTop;
    private final Scoreboard scoreboard;
    private final PaintShop paintShop;

    /**
     * Constructor of the main GUI. creates the GUI with a scoreboard.
     * @param scoreboard sets the scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainGUI(Scoreboard scoreboard, MainController controller) {
        this.scoreboard = scoreboard;
        this.controller = controller;
        options = GameOptions.getInstance();
        paintShop = new PaintShop();
        frame = new JFrame();
        buildTileGrid();
        buildMeepleGrid();
        buildLayeredPane();
        buildFrame();
    }

    /**
     * Refreshes the meeple labels to get the new colors.
     */
    @Override
    public void notifyChange() {
        for (int y = 0; y < meepleGridHeight; y++) {
            for (int x = 0; x < meepleGridWidth; x++) {
                meepleGrid[x][y].refresh();
            }
        }
        frame.repaint();
    }

    /**
     * Rebuilds the label grid and the meeple grid if the game should be restarted.
     */
    public void rebuildGrids() {
        for (int y = 0; y < meepleGridHeight; y++) {
            for (int x = 0; x < meepleGridWidth; x++) {
                if (x < gridWidth && y < gridHeight) {
                    labelGrid[x][y].reset();
                }
                meepleGrid[x][y].reset();
                frame.repaint();
            }
        }
    }

    /**
     * Removes meeple on a tile on the grid.
     * @param meeple is the meeple that should be removed.
     */
    public void removeMeeple(Meeple meeple) {
        checkParameters(meeple);
        GridSpot spot = meeple.getLocation().getGridSpot();
        if (spot == null) { // make sure meeple is placed
            throw new IllegalArgumentException("Meeple has to be placed to be removed from GUI: " + meeple);
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                meepleGrid[spot.getX() * 3 + x][spot.getY() * 3 + y].reset();
            }
        }
        frame.repaint();
    }

    /**
     * Grants access to the JFrame of the main GUI.
     * @return the {@link Jframe}.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Draws the tile on a specific position on the GUI.
     * @param tile is the tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void setTile(Tile tile, int x, int y) {
        checkParameters(tile);
        checkCoordinates(x, y);
        labelGrid[x][y].setTile(tile);
    }

    /**
     * Highlights a position on the grid to indicate that the tile is a possible placement spot.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void setHighlight(int x, int y) {
        checkCoordinates(x, y);
        labelGrid[x][y].highlight();
    }

    /**
     * Draws meeple on a tile on the grid.
     * @param tile is the tile where the meeple gets drawn.
     * @param position is the position on the tile where the meeple gets drawn.
     * @param owner is the player that owns the meeple.
     */
    public void setMeeple(Tile tile, GridDirection position, Player owner) {
        checkParameters(tile, position, owner);
        GridSpot spot = tile.getGridSpot();
        int xpos = position.addX(spot.getX() * 3 + 1);
        int ypos = position.addY(spot.getY() * 3 + 1);
        meepleGrid[xpos][ypos].setIcon(tile.getTerrain(position), owner);
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    public void resetMeepleHighlight(Tile tile) {
        checkParameters(tile);
        int xBase = tile.getGridSpot().getX() * 3;
        int yBase = tile.getGridSpot().getY() * 3;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                meepleGrid[xBase + x][yBase + y].reset();
            }
        }
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    public void setMeepleHighlight(Tile tile, Player currentPlayer) {
        checkParameters(tile, currentPlayer);
        int xBase = tile.getGridSpot().getX() * 3;
        int yBase = tile.getGridSpot().getY() * 3;
        GridDirection[][] directions = GridDirection.values2D();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (tile.hasMeepleSpot(directions[x][y]) && controller.requestPlacementStatus(directions[x][y])) {
                    meepleGrid[xBase + x][yBase + y].setHighlight(tile.getTerrain(directions[x][y]), currentPlayer);
                }
            }
        }
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    private void checkParameters(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null) {
                throw new IllegalArgumentException("Parameters such as Tile, Meeple, and Player cannot be null!");
            }
        }
    }

    private void checkCoordinates(int x, int y) {
        if (x < 0 && x >= gridWidth || y < 0 && y >= gridHeight) {
            throw new IllegalArgumentException("Invalid label grid position (" + x + ", " + y + ")");
        }
    }

    private void buildFrame() {
        MainMenuBar menuBar = new MainMenuBar(scoreboard, controller);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void buildLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(panelBottom, Integer.valueOf(0), 1);
        layeredPane.add(panelTop, Integer.valueOf(0), 0);
    }

    private void buildMeepleGrid() {
        panelTop = new JPanel();
        panelTop.setSize(options.gridResolutionWidth, options.gridResolutionHeight);
        panelTop.setOpaque(false);
        panelTop.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        meepleGridWidth = options.gridWidth * 3;
        meepleGridHeight = options.gridHeight * 3;
        constraints.weightx = 1;
        constraints.weighty = 1;
        meepleGrid = new MeepleLabel[meepleGridWidth][meepleGridHeight]; // build array of labels.
        for (int x = 0; x < meepleGridWidth; x++) {
            for (int y = 0; y < meepleGridHeight; y++) {
                meepleGrid[x][y] = new MeepleLabel(paintShop, controller, GridDirection.values2D()[x % 3][y % 3], frame);
                constraints.gridx = x;
                constraints.gridy = y;
                panelTop.add(meepleGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
    }

    /*
     * Creates the grid of labels.
     */
    private void buildTileGrid() {
        panelBottom = new JPanel();
        panelBottom.setSize(options.gridResolutionWidth, options.gridResolutionHeight);
        panelBottom.setBackground(options.colorGUImain);
        panelBottom.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        gridWidth = options.gridWidth;
        gridHeight = options.gridHeight;
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new TileLabel(controller, x, y);
                constraints.gridx = x;
                constraints.gridy = y;
                panelBottom.add(labelGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
    }

}