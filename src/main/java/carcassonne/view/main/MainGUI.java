package carcassonne.view.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;

import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;
import carcassonne.settings.Notifiable;
import carcassonne.settings.SystemProperties;
import carcassonne.view.PaintShop;
import carcassonne.view.menubar.MainMenuBar;
import carcassonne.view.menubar.Scoreboard;

/**
 * The main GUI class.
 * @author Timur Saglam
 */
// TODO (HIGH) Custom classes for the two panels.
public class MainGUI extends JFrame implements Notifiable {
    private static final int ZOOM_STEP_SIZE = 25; // TODO (HIGH) find a decent step size
    private static final int MAX_ZOOM_LEVEL = 300;
    private static final int MIN_ZOOM_LEVEL = 50;
    private static final long serialVersionUID = 5684446992452298030L; // generated UID
    private static final int MEEPLE_FACTOR = 3; // Meeples per tile length.
    private static final Color GUI_COLOR = new Color(190, 190, 190);
    private GridBagConstraints constraints;
    private final MainController controller;
    private MainMenuBar menuBar;
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
    private JLayeredPane layeredPane;
    private final SystemProperties systemProperties;
    private int zoomLevel;

    /**
     * Constructor of the main GUI. creates the GUI with a scoreboard.
     * @param scoreboard sets the scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainGUI(MainController controller) {
        this.controller = controller;
        systemProperties = new SystemProperties();
        tileSize = GameSettings.TILE_SIZE;
        zoomLevel = 100;
        gridWidth = systemProperties.getResolutionWidth() / tileSize;
        gridHeight = systemProperties.getResolutionHeight() / tileSize;
        JPanel tilePanel = buildTilePanel();
        JPanel meeplePanel = buildMeeplePanel();
        layeredPane = buildLayeredPane(meeplePanel, tilePanel);
        buildFrame(layeredPane);
    }

    /**
     * Zooms in if the maximum zoom level has not been reached.
     */
    public void zoomIn() { // TODO (HIGH) use listener or move to class that encapsulates the scrollpane and layered pane
        if (zoomLevel < MAX_ZOOM_LEVEL) {
            zoomLevel += ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(false);
        }
    }

    /**
     * Updates the zoom level if it is in the valid range.
     * @param level is the zoom level.
     * @param preview specifies whether fast calculations for preview purposes should be used.
     */
    public void setZoom(int level, boolean preview) { // TODO (HIGH) use listener or move to class that encapsulates the scrollpane and layered pane
        if (level <= MAX_ZOOM_LEVEL && level >= MIN_ZOOM_LEVEL) {
            zoomLevel = level;
            updateToChangedZoomLevel(preview);
        }
    }

    /**
     * Grants access to the current zoom level of the UI.
     * @return the zoom level.
     */
    public int getZoom() {
        return zoomLevel;
    }

    /**
     * Zooms out if the minimum zoom level has not been reached.
     */
    public void zoomOut() {
        if (zoomLevel > MIN_ZOOM_LEVEL) {
            zoomLevel -= ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(false);
        }
    }

    private void updateToChangedZoomLevel(boolean preview) { // TODO (HIGH) test putting a time limit here
        if (currentPlayer != null && !preview) { // only update highlights when there is an active round
            ImageIcon newHighlight = PaintShop.getColoredHighlight(currentPlayer, zoomLevel);
            tileLabels.forEach(it -> it.setColoredHighlight(newHighlight));
        }
        tileLabels.forEach(it -> it.setTileSize(zoomLevel, preview));
    }

    /**
     * Refreshes the meeple labels to get the new colors.
     */
    @Override
    public void notifyChange() {
        meepleLabels.forEach(it -> it.refresh());
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
    }

    /**
     * Resets the meeple preview on one specific {@link Tile}.
     * @param tile is the specific {@link Tile}.
     */
    public void resetMeeplePreview(Tile tile) {
        checkParameters(tile);
        int xBase = tile.getGridSpot().getX() * MEEPLE_FACTOR;
        int yBase = tile.getGridSpot().getY() * MEEPLE_FACTOR;
        for (int y = 0; y < MEEPLE_FACTOR; y++) {
            for (int x = 0; x < MEEPLE_FACTOR; x++) {
                meepleGrid[xBase + x][yBase + y].reset();
            }
        }
    }

    /**
     * Enables the meeple preview on one specific {@link Tile}.
     * @param tile is the specific {@link Tile}.
     * @param currentPlayer determines the color of the preview.
     */
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
        layeredPane.repaint(); // This is required! Removing this will paint black background.
    }

    /**
     * Notifies the the main GUI about a (new) current player. This allows the UI to adapt color schemes to the player.
     * @param currentPlayer is the current {@link Player}.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        ImageIcon newHighlight = PaintShop.getColoredHighlight(currentPlayer, zoomLevel);
        tileLabels.forEach(it -> it.setColoredHighlight(newHighlight));
    }

    public Scoreboard getScoreboard() {
        return menuBar.getScoreboard(); // TODO (MEDIUM) Find better solution.
    }

    public void resetMenuState() {
        menuBar.enableStart(); // TODO (MEDIUM) Find better solution.
    }

    /**
     * Enslaves sub user interfaces, they are minimized with this GUI.
     * @param userInterfaces are the user interfaces to enslave.
     */
    public void addSubInterfaces(Component... userInterfaces) {
        this.addWindowListener(new SubComponentAdapter(userInterfaces));
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
        menuBar = new MainMenuBar(controller, this);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(layeredPane); // TODO (HIGH) Fix scroll bar visual weirdness on win 10
        scrollPane.setPreferredSize(new Dimension(gridWidth * tileSize, gridHeight * tileSize));
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        // center scroll pane
        Rectangle bounds = scrollPane.getViewport().getViewRect();
        Dimension size = scrollPane.getViewport().getViewSize();
        int x = (size.width - bounds.width) / 2;
        int y = (size.height - bounds.height) / 2;
        scrollPane.getViewport().setViewPosition(new Point(x, y));
    }

    private JLayeredPane buildLayeredPane(JPanel meeplePanel, JPanel tilePanel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(tilePanel, 1);
        layeredPane.add(meeplePanel, 0);
        return layeredPane;
    }

    private JPanel buildMeeplePanel() {
        JPanel meeplePanel = new JPanel();
        meeplePanel.setOpaque(false);
        meeplePanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        meepleGridWidth = gridWidth * MEEPLE_FACTOR;
        meepleGridHeight = gridHeight * MEEPLE_FACTOR;
        constraints.weightx = 1; // evenly distributes meeple grid
        constraints.weighty = 1;
        meepleLabels = new ArrayList<>();
        meepleGrid = new MeepleLabel[meepleGridWidth][meepleGridHeight]; // build array of labels.
        for (int x = 0; x < meepleGridWidth; x++) {
            for (int y = 0; y < meepleGridHeight; y++) {
                meepleGrid[x][y] = new MeepleLabel(controller, GridDirection.values2D()[x % MEEPLE_FACTOR][y % MEEPLE_FACTOR], this);
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
        tilePanel.setBackground(GUI_COLOR);
        tilePanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        tileLabels = new ArrayList<>();
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
        Tile defaultTile = new Tile(TileType.Null);
        Tile highlightTile = new Tile(TileType.Null);
        defaultTile.rotateRight();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new TileLabel(zoomLevel, defaultTile, highlightTile, controller, x, y);
                tileLabels.add(labelGrid[x][y]);
                constraints.gridx = x;
                constraints.gridy = y;
                tilePanel.add(labelGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
        return tilePanel;
    }
}