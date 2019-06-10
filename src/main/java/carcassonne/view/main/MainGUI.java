package carcassonne.view.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.settings.Notifiable;
import carcassonne.settings.SystemProperties;
import carcassonne.view.PaintShop;
import carcassonne.view.main.menubar.MainMenuBar;
import carcassonne.view.main.menubar.Scoreboard;

/**
 * The main GUI class.
 * @author Timur Saglam
 */
public class MainGUI implements Notifiable {
    private static final int MEEPLE_FACTOR = 3; // Meeples per tile length.
    private static final Color GUI_COLOR = new Color(190, 190, 190);
    private GridBagConstraints constraints;
    private final MainController controller;
    private final Scoreboard scoreboard;
    private final PaintShop paintShop;
    private final JFrame frame;
    private final int tileSize;
    private final int gridHeight;
    private final int gridWidth;
    private int meepleGridHeight;
    private int meepleGridWidth;
    private TileLabel[][] labelGrid;
    private List<TileLabel> tileLabels;
    private MeepleLabel[][] meepleGrid;
    private List<MeepleLabel> meepleLabels;
    private Player currentPlayer;

    /**
     * Constructor of the main GUI. creates the GUI with a scoreboard.
     * @param scoreboard sets the scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainGUI(Scoreboard scoreboard, MainController controller) {
        this.scoreboard = scoreboard;
        this.controller = controller;
        SystemProperties systemProperties = new SystemProperties();
        paintShop = new PaintShop();
        frame = new JFrame();
        tileSize = GameSettings.TILE_SIZE;
        gridWidth = systemProperties.getResolutionWidth() / tileSize;
        gridHeight = systemProperties.getResolutionHeight() / tileSize;
        JPanel tilePanel = buildTilePanel();
        JPanel meeplePanel = buildMeeplePanel();
        JLayeredPane layeredPane = buildLayeredPane(meeplePanel, tilePanel);
        buildFrame(layeredPane);
    }

    /**
     * Refreshes the meeple labels to get the new colors.
     */
    @Override
    public void notifyChange() {
        meepleLabels.forEach(it -> it.refresh());
        frame.repaint();
        if (currentPlayer != null) {
            setCurrentPlayer(currentPlayer);
        }
    }

    /**
     * Rebuilds the label grid and the meeple grid if the game should be restarted.
     */
    public void rebuildGrids() {
        meepleLabels.forEach(it -> it.reset());
        tileLabels.forEach(it -> it.reset());
        frame.repaint();
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
        for (int y = 0; y < MEEPLE_FACTOR; y++) {
            for (int x = 0; x < MEEPLE_FACTOR; x++) {
                meepleGrid[spot.getX() * MEEPLE_FACTOR + x][spot.getY() * MEEPLE_FACTOR + y].reset();
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
        int xpos = position.addX(spot.getX() * MEEPLE_FACTOR + 1);
        int ypos = position.addY(spot.getY() * MEEPLE_FACTOR + 1);
        meepleGrid[xpos][ypos].setIcon(tile.getTerrain(position), owner);
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    public void resetMeeplePreview(Tile tile) {
        checkParameters(tile);
        int xBase = tile.getGridSpot().getX() * MEEPLE_FACTOR;
        int yBase = tile.getGridSpot().getY() * MEEPLE_FACTOR;
        for (int y = 0; y < MEEPLE_FACTOR; y++) {
            for (int x = 0; x < MEEPLE_FACTOR; x++) {
                meepleGrid[xBase + x][yBase + y].reset();
            }
        }
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    public void setMeeplePreview(Tile tile, Player currentPlayer) {
        checkParameters(tile, currentPlayer);
        int xBase = tile.getGridSpot().getX() * MEEPLE_FACTOR;
        int yBase = tile.getGridSpot().getY() * MEEPLE_FACTOR;
        GridDirection[][] directions = GridDirection.values2D();
        for (int y = 0; y < MEEPLE_FACTOR; y++) {
            for (int x = 0; x < MEEPLE_FACTOR; x++) {
                if (tile.hasMeepleSpot(directions[x][y]) && controller.requestPlacementStatus(directions[x][y])) {
                    meepleGrid[xBase + x][yBase + y].setPreview(tile.getTerrain(directions[x][y]), currentPlayer);
                }
            }
        }
        frame.repaint(); // This is required! Removing this will paint black background.
    }

    /**
     * Notifies the the main GUI about a (new) current player. This allows the UI to adapt color schemes to the player.
     * @param currentPlayer is the current {@link Player}.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        ImageIcon newHighlight = paintShop.getColoredHighlight(currentPlayer);
        tileLabels.forEach(it -> it.setColoredHighlight(newHighlight));
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

    private void buildFrame(JLayeredPane layeredPane) {
        MainMenuBar menuBar = new MainMenuBar(scoreboard, controller);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JLayeredPane buildLayeredPane(JPanel meeplePanel, JPanel tilePanel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(tilePanel, Integer.valueOf(0), 1);
        layeredPane.add(meeplePanel, Integer.valueOf(0), 0);
        return layeredPane;
    }

    private JPanel buildMeeplePanel() {
        JPanel meeplePanel = new JPanel();
        meeplePanel.setSize(gridWidth * tileSize, gridHeight * tileSize);
        meeplePanel.setOpaque(false);
        meeplePanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        meepleGridWidth = gridWidth * MEEPLE_FACTOR;
        meepleGridHeight = gridHeight * MEEPLE_FACTOR;
        constraints.weightx = 1;
        constraints.weighty = 1;
        meepleLabels = new ArrayList<>();
        meepleGrid = new MeepleLabel[meepleGridWidth][meepleGridHeight]; // build array of labels.
        for (int x = 0; x < meepleGridWidth; x++) {
            for (int y = 0; y < meepleGridHeight; y++) {
                meepleGrid[x][y] = new MeepleLabel(paintShop, controller, GridDirection.values2D()[x % MEEPLE_FACTOR][y % MEEPLE_FACTOR], frame);
                meepleLabels.add(meepleGrid[x][y]);
                constraints.gridx = x;
                constraints.gridy = y;
                meeplePanel.add(meepleGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
        return meeplePanel;
    }

    /*
     * Creates the grid of labels.
     */
    private JPanel buildTilePanel() {
        JPanel tilePanel = new JPanel();
        tilePanel.setSize(gridWidth * tileSize, gridHeight * tileSize);
        tilePanel.setBackground(GUI_COLOR);
        tilePanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        tileLabels = new ArrayList<>();
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new TileLabel(controller, x, y);
                tileLabels.add(labelGrid[x][y]);
                constraints.gridx = x;
                constraints.gridy = y;
                tilePanel.add(labelGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
        return tilePanel;
    }
}