package carcassonne.view.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
import carcassonne.util.LookAndFeelUtil;
import carcassonne.view.PaintShop;
import carcassonne.view.menubar.MainMenuBar;
import carcassonne.view.menubar.Scoreboard;

/**
 * The main GUI class.
 * @author Timur Saglam
 */
// TODO (HIGH) Custom classes for the two layers.
public class MainGUI extends JFrame implements Notifiable {
    private static final Color GUI_COLOR = new Color(190, 190, 190);
    private static final int MAX_ZOOM_LEVEL = 300;
    private static final int MIN_ZOOM_LEVEL = 50;
    private static final long serialVersionUID = 5684446992452298030L; // generated UID
    private static final int ZOOM_STEP_SIZE = 25;
    private static final Dimension MINIMAL_WINDOW_SIZE = new Dimension(640, 480);
    private GridBagConstraints constraints;
    private final MainController controller;
    private Player currentPlayer;
    private int gridHeight;
    private int gridWidth;
    private JLayeredPane layeredPane;
    private List<MeepleDepictionPanel> meeplePanels;
    private JPanel meepleLayer;
    private MeepleDepictionPanel[][] meeplePanelGrid;
    private MainMenuBar menuBar;
    private JScrollPane scrollPane;
    private TileDepiction[][] tileLabelGrid;
    private List<TileDepiction> tileLabels;
    private JPanel tileLayer;
    private final int tileSize;
    private int zoomLevel;

    /**
     * Constructor of the main GUI. creates the GUI with a scoreboard.
     * @param scoreboard sets the scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainGUI(MainController controller) {
        this.controller = controller;
        updateGridSize();
        tileSize = GameSettings.TILE_SIZE;
        zoomLevel = 125;
        JPanel tilePanel = buildTileLayer();
        JPanel meeplePanel = buildMeepleLayer();
        layeredPane = buildLayeredPane(meeplePanel, tilePanel);
        buildFrame(layeredPane);
    }

    /**
     * Resets the tile grid and the meeple grid to return to the initial state.
     */
    public void resetGrid() {
        meeplePanels.forEach(it -> it.forEach(label -> label.reset()));
        tileLabels.forEach(it -> it.reset());
        revalidate();
    }

    /**
     * Rebuilds the grid to adapt it to a changed grid size. Therefore, the grid size is updated before rebuilding.
     */
    public void rebuildGrid() {
        updateGridSize();
        layeredPane.remove(tileLayer);
        layeredPane.remove(meepleLayer);
        layeredPane.add(buildTileLayer(), 1);
        layeredPane.add(buildMeepleLayer(), 0);
        centerScrollPaneView();
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
        int x = spot.getX();
        int y = spot.getY();
        checkCoordinates(x, y);
        meeplePanelGrid[x][y].forEach(it -> it.reset());
    }

    /**
     * Resets the meeple preview on one specific {@link Tile}.
     * @param tile is the specific {@link Tile}.
     */
    public void resetMeeplePreview(Tile tile) {
        checkParameters(tile);
        int x = tile.getGridSpot().getX();
        int y = tile.getGridSpot().getY();
        checkCoordinates(x, y);
        meeplePanelGrid[x][y].forEach(it -> it.reset());
    }

    public void resetMenuState() {
        menuBar.enableStart(); // TODO (MEDIUM) Find better solution.
    }

    public void showUI() {
        setVisible(true);
        centerScrollPaneView();
    }

    public Scoreboard getScoreboard() {
        return menuBar.getScoreboard(); // TODO (MEDIUM) Find better solution.
    }

    /**
     * Grants access to the current zoom level of the UI.
     * @return the zoom level.
     */
    public int getZoom() {
        return zoomLevel;
    }

    /**
     * Zooms in if the maximum zoom level has not been reached.
     */
    public void zoomIn() {
        if (zoomLevel < MAX_ZOOM_LEVEL) {
            zoomLevel += ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(false);
        }
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

    /**
     * Updates the zoom level if it is in the valid range.
     * @param level is the zoom level.
     * @param preview specifies whether fast calculations for preview purposes should be used.
     */
    public void setZoom(int level, boolean preview) {
        if (level <= MAX_ZOOM_LEVEL && level >= MIN_ZOOM_LEVEL) {
            zoomLevel = level;
            updateToChangedZoomLevel(preview);
        }
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

    /**
     * Highlights a position on the grid to indicate that the tile is a possible placement spot.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void setHighlight(int x, int y) {
        checkCoordinates(x, y);
        tileLabelGrid[x][y].highlight();
    }

    /**
     * Draws meeple on a tile on the grid.
     * @param tile is the tile where the meeple gets drawn.
     * @param position is the position on the tile where the meeple gets drawn.
     * @param owner is the player that owns the meeple.
     */
    public void setMeeple(Tile tile, GridDirection position, Player owner) {
        checkParameters(tile, position, owner);
        int x = tile.getGridSpot().getX();
        int y = tile.getGridSpot().getY();
        checkCoordinates(x, y);
        meeplePanelGrid[x][y].getMeepleLabel(position).setIcon(tile.getTerrain(position), owner);
    }

    /**
     * Enables the meeple preview on one specific {@link Tile}.
     * @param tile is the specific {@link Tile}.
     * @param currentPlayer determines the color of the preview.
     */
    public void setMeeplePreview(Tile tile, Player currentPlayer) {
        checkParameters(tile, currentPlayer);
        int x = tile.getGridSpot().getX();
        int y = tile.getGridSpot().getY();
        checkCoordinates(x, y);
        meeplePanelGrid[x][y].setMeeplePreview(tile, currentPlayer);
        layeredPane.repaint(); // This is required! Removing this will paint black background.
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
        tileLabelGrid[x][y].setTile(tile);
    }
    
    /**
     * Refreshes the meeple labels to get the new colors.
     */
    @Override
    public void notifyChange() {
        meeplePanels.forEach(it -> it.forEach(label -> label.refresh()));
        if (currentPlayer != null) {
            setCurrentPlayer(currentPlayer);
        }
    }

    private void updateGridSize() {
        gridWidth = controller.getSettings().getGridWidth();
        gridHeight = controller.getSettings().getGridHeight();
    }

    private void buildFrame(JLayeredPane layeredPane) {
        menuBar = new MainMenuBar(controller, this);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        scrollPane = LookAndFeelUtil.createModifiedScrollpane(layeredPane);
        scrollPane.setPreferredSize(new Dimension(gridWidth * tileSize, gridHeight * tileSize));
        add(scrollPane, BorderLayout.CENTER);
        setMinimumSize(MINIMAL_WINDOW_SIZE);
        setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
        addWindowListener(new WindowMaximizationAdapter(this));
        pack();

    }

    private JLayeredPane buildLayeredPane(JPanel meeplePanel, JPanel tilePanel) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(tilePanel, 1);
        layeredPane.add(meeplePanel, 0);
        return layeredPane;
    }

    private JPanel buildMeepleLayer() {
        meepleLayer = new JPanel();
        meepleLayer.setOpaque(false);
        meepleLayer.setLayout(new GridBagLayout());
        synchronizeLayerSizes();
        constraints = new GridBagConstraints();
        constraints.weightx = 1; // evenly distributes meeple grid
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        meeplePanels = new ArrayList<>();
        meeplePanelGrid = new MeepleDepictionPanel[gridWidth][gridHeight]; // build array of labels.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                meeplePanelGrid[x][y] = new MeepleDepictionPanel(zoomLevel, controller, this);
                meeplePanels.add(meeplePanelGrid[x][y]);
                constraints.gridx = x;
                constraints.gridy = y;
                meepleLayer.add(meeplePanelGrid[x][y], constraints); // add label with constraints
            }
        }
        return meepleLayer;
    }

    /*
     * Creates the grid of labels.
     */
    private JPanel buildTileLayer() {
        tileLayer = new JPanel();
        tileLayer.setBackground(GUI_COLOR);
        tileLayer.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        tileLabels = new ArrayList<>();
        tileLabelGrid = new TileDepiction[gridWidth][gridHeight]; // build array of labels.
        Tile defaultTile = new Tile(TileType.Null);
        Tile highlightTile = new Tile(TileType.Null);
        defaultTile.rotateRight();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                tileLabelGrid[x][y] = new TileDepiction(zoomLevel, defaultTile, highlightTile, controller, x, y);
                tileLabels.add(tileLabelGrid[x][y]);
                constraints.gridx = x;
                constraints.gridy = y;
                tileLayer.add(tileLabelGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
        return tileLayer;
    }

    private void centerScrollPaneView() {
        revalidate(); // IMPORTANT: required to prevent a shaking view!
        Dimension size = new Dimension(gridWidth * zoomLevel, gridHeight * zoomLevel);
        Rectangle bounds = scrollPane.getViewport().getViewRect();
        int x = (int) (Math.round((size.width - bounds.width) / 2.0));
        int y = (int) (Math.round((size.height - bounds.height) / 2.0));
        scrollPane.getViewport().setViewPosition(new Point(x, y));
    }

    private void checkCoordinates(int x, int y) {
        if (x < 0 && x >= gridWidth || y < 0 && y >= gridHeight) {
            throw new IllegalArgumentException("Invalid label grid position (" + x + ", " + y + ")");
        }
    }

    private void checkParameters(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null) {
                throw new IllegalArgumentException("Parameters such as Tile, Meeple, and Player cannot be null!");
            }
        }
    }

    private void updateToChangedZoomLevel(boolean preview) {
        if (currentPlayer != null && !preview) { // only update highlights when there is an active round
            ImageIcon newHighlight = PaintShop.getColoredHighlight(currentPlayer, zoomLevel);
            tileLabels.forEach(it -> it.setColoredHighlight(newHighlight));
        }
        // Parallel stream for improved performance when grid is full:
        Arrays.stream(tileLabelGrid).parallel().forEach(column -> IntStream.range(0, column.length) // columns
                .forEach(i -> column[i].setTileSize(zoomLevel, preview))); // rows
        synchronizeLayerSizes(); // IMPORTANT: Ensures that the meeples are on the tiles.
        meeplePanels.forEach(it -> it.setSize(zoomLevel));
        centerScrollPaneView(); // TODO (HIGH) zoom based on view center and not grid center?
        layeredPane.repaint(); // IMPORTANT: Prevents meeples from disappearing.
    }

    private void synchronizeLayerSizes() {
        Dimension layerSize = new Dimension(gridWidth * zoomLevel, gridHeight * zoomLevel);
        meepleLayer.setMaximumSize(layerSize);
        meepleLayer.setPreferredSize(layerSize);
        meepleLayer.setMinimumSize(layerSize);
    }
}