package carcassonne.view.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;

import carcassonne.control.MainController;
import carcassonne.model.Meeple;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.tile.Tile;
import carcassonne.settings.Notifiable;
import carcassonne.util.LookAndFeelUtil;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.PaintShop;
import carcassonne.view.menubar.MainMenuBar;
import carcassonne.view.menubar.Scoreboard;

/**
 * The main GUI class.
 * @author Timur Saglam
 */
public class MainGUI extends JFrame implements Notifiable {
    private static final long serialVersionUID = 5684446992452298030L; // generated UID
    private static final int MAX_ZOOM_LEVEL = 300;
    private static final int MIN_ZOOM_LEVEL = 25;
    private static final int ZOOM_STEP_SIZE = 25;
    private static final Dimension MINIMAL_WINDOW_SIZE = new Dimension(640, 480);
    private final MainController controller;
    private Player currentPlayer;
    private int gridHeight;
    private int gridWidth;
    private MeepleLayer meepleLayer;
    private TileLayer tileLayer;
    private MainMenuBar menuBar;
    private LayeredScrollPane scrollPane;
    private int zoomLevel;

    /**
     * Constructor of the main GUI. creates the GUI with a scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainGUI(MainController controller) {
        this.controller = controller;
        gridWidth = controller.getSettings().getGridWidth();
        gridHeight = controller.getSettings().getGridHeight();
        zoomLevel = 125;
        buildFrame();
    }

    /**
     * Adds the global key bindings to this UI.
     * @param keyBindings are the global key bindings.
     */
    public void addKeyBindings(GlobalKeyBindingManager keyBindings) {
        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = scrollPane.getActionMap();
        keyBindings.addKeyBindingsToMaps(inputMap, actionMap);
    }

    /**
     * Resets the tile grid and the meeple grid to return to the initial state.
     */
    public void resetGrid() {
        meepleLayer.resetLayer();
        tileLayer.resetLayer();
        revalidate();
    }

    /**
     * Rebuilds the grid to adapt it to a changed grid size. Therefore, the grid size is updated before rebuilding.
     */
    public void rebuildGrid() {
        gridWidth = controller.getSettings().getGridWidth();
        gridHeight = controller.getSettings().getGridHeight();
        scrollPane.removeLayers(meepleLayer, tileLayer);
        tileLayer = new TileLayer(controller, gridHeight, gridWidth, zoomLevel);
        meepleLayer = new MeepleLayer(controller, gridWidth, gridHeight, zoomLevel);
        scrollPane.addLayers(meepleLayer, tileLayer);
        revalidate(); // IMPORTANT: required to prevent a shaking view!
        scrollPane.centerScrollPaneView(gridWidth, gridHeight, zoomLevel);
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
        meepleLayer.resetPanel(x, y);
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
        meepleLayer.resetPanel(x, y);
    }

    /**
     * Resets the state of the menu to allows restarting.
     */
    public void resetMenuState() {
        menuBar.enableStart(); // TODO (MEDIUM) Find better solution.
    }

    /**
     * Shows the UI and centers the scrollpane view.
     */
    public void showUI() {
        setVisible(true);
        revalidate(); // IMPORTANT: required to prevent a shaking view!
        scrollPane.centerScrollPaneView(gridWidth, gridHeight, zoomLevel);
    }

    /**
     * Grants access to the scoreboard.
     * @return the scoreboard.
     */
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
            menuBar.getZoomSlider().setValue(zoomLevel); // ensures the slider is updated when using key bindings.
        }
    }

    /**
     * Zooms out if the minimum zoom level has not been reached.
     */
    public void zoomOut() {
        if (zoomLevel > MIN_ZOOM_LEVEL) {
            zoomLevel -= ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(false);
            menuBar.getZoomSlider().setValue(zoomLevel); // ensures the slider is updated when using key bindings.
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
        tileLayer.refreshHighlight(newHighlight);
    }

    /**
     * Highlights a position on the grid to indicate that the tile is a possible placement spot.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void setHighlight(int x, int y) {
        checkCoordinates(x, y);
        tileLayer.highlightTile(x, y);
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
        meepleLayer.placeMeeple(x, y, tile.getTerrain(position), position, owner);
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
        meepleLayer.enableMeeplePreview(x, y, tile, currentPlayer);
        scrollPane.repaintLayers(); // This is required! Removing this will paint black background.
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
        tileLayer.placeTile(tile, x, y);
    }

    /**
     * Refreshes the meeple labels to get the new colors.
     */
    @Override
    public void notifyChange() {
        meepleLayer.refreshLayer();
        if (currentPlayer != null) {
            setCurrentPlayer(currentPlayer);
        }
    }

    private void updateToChangedZoomLevel(boolean preview) {
        if (currentPlayer != null && !preview) { // only update highlights when there is an active round
            ImageIcon newHighlight = PaintShop.getColoredHighlight(currentPlayer, zoomLevel);
            tileLayer.refreshHighlight(newHighlight);
        }
        tileLayer.changeZoomLevel(zoomLevel, preview); // Executed in parallel for improved performance
        meepleLayer.synchronizeLayerSizes(gridWidth, gridHeight, zoomLevel); // IMPORTANT: Ensures that the meeples are on the tiles.
        meepleLayer.changeZoomLevel(zoomLevel);
        revalidate(); // IMPORTANT: required to prevent a shaking view!
        scrollPane.centerScrollPaneView(gridWidth, gridHeight, zoomLevel); // TODO (HIGH) zoom based on view center and not grid center?
        scrollPane.repaintLayers(); // IMPORTANT: Prevents meeples from disappearing.
    }

    private void buildFrame() {
        tileLayer = new TileLayer(controller, gridHeight, gridWidth, zoomLevel);
        meepleLayer = new MeepleLayer(controller, gridWidth, gridHeight, zoomLevel);
        menuBar = new MainMenuBar(controller, this);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        scrollPane = LookAndFeelUtil.createModifiedScrollpane();
        scrollPane.addLayers(meepleLayer, tileLayer);
        add(scrollPane, BorderLayout.CENTER);
        setMinimumSize(MINIMAL_WINDOW_SIZE);
        setExtendedState(getExtendedState() | Frame.MAXIMIZED_BOTH);
        addWindowListener(new WindowMaximizationAdapter(this));
        pack();
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
}