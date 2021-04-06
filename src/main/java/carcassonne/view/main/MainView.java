package carcassonne.view.main;

import static carcassonne.view.main.ZoomMode.FAST;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;

import carcassonne.control.ControllerFacade;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.NotifiableView;
import carcassonne.view.PaintShop;
import carcassonne.view.menubar.MainMenuBar;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.util.LookAndFeelUtil;
import carcassonne.view.util.ThreadingUtil;

/**
 * The main user interface, showing the grid and the menu bar.
 * @author Timur Saglam
 */
public class MainView extends JFrame implements NotifiableView {
    private static final long serialVersionUID = 5684446992452298030L; // generated UID
    private static final int DEFAULT_ZOOM_LEVEL = 125;
    private static final int MAX_ZOOM_LEVEL = 300;
    private static final int MIN_ZOOM_LEVEL = 25;
    private static final int ZOOM_STEP_SIZE = 25;
    private static final Dimension MINIMAL_WINDOW_SIZE = new Dimension(640, 480);
    private final ControllerFacade controller;
    private Player currentPlayer;
    private int gridHeight;
    private int gridWidth;
    private MeepleLayer meepleLayer;
    private TileLayer tileLayer;
    private MainMenuBar menuBar;
    private LayeredScrollPane scrollPane;
    private int zoomLevel;

    /**
     * Constructor of the main view. creates the view, menu bar, and scoreboard.
     * @param controller sets the connection to the game controller.
     */
    public MainView(ControllerFacade controller) {
        this.controller = controller;
        gridWidth = controller.getSettings().getGridWidth();
        gridHeight = controller.getSettings().getGridHeight();
        zoomLevel = DEFAULT_ZOOM_LEVEL;
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
        validate(); // might not be required all the time but is fast so it does not matter
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
        scrollPane.validateAndCenter();
    }

    /**
     * Removes meeple on a tile on the grid.
     */
    public void removeMeeple(int x, int y) {
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
        scrollPane.validateAndCenter();
    }

    /**
     * Returns the controller with which this UI is communicating.
     * @return the controller instance.
     */
    public ControllerFacade getController() {
        return controller;
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
     * @param mode determines the zoom mode, which affects image quality and performance.
     * @param updateHightlights determines if the highlight tiles are also scaled.
     */
    public synchronized void zoomIn(ZoomMode mode) {
        if (zoomLevel < MAX_ZOOM_LEVEL) {
            zoomLevel += ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(mode);
            menuBar.getZoomSlider().setValueSneakily(zoomLevel); // ensures the slider is updated when using key bindings.
        }
    }

    /**
     * Zooms out if the minimum zoom level has not been reached.
     * @param updateHightlights determines if the highlight tiles are also scaled.
     */
    public synchronized void zoomOut(ZoomMode mode) {
        if (zoomLevel > MIN_ZOOM_LEVEL) {
            zoomLevel -= ZOOM_STEP_SIZE;
            updateToChangedZoomLevel(mode);
            menuBar.getZoomSlider().setValueSneakily(zoomLevel); // ensures the slider is updated when using key bindings.
        }
    }

    /**
     * Updates the zoom level if it is in the valid range.
     * @param level is the zoom level.
     * @param mode determines the zoom mode, which affects image quality and performance.
     */
    public synchronized void setZoom(int level, ZoomMode mode) {
        if (level <= MAX_ZOOM_LEVEL && level >= MIN_ZOOM_LEVEL) {
            zoomLevel = level;
            updateToChangedZoomLevel(mode);
        }
    }

    /**
     * Updates the view to a changed zoom level. Centers the view.
     * @param mode determines the zoom mode, which affects image quality and performance.
     */
    public void updateToChangedZoomLevel(ZoomMode mode) { // TODO (HIGH) [THREADING] Better threading for zooming?
        if (currentPlayer != null) { // only update highlights when there is an active round
            tileLayer.refreshHighlight(PaintShop.getColoredHighlight(currentPlayer, zoomLevel, mode == FAST));
        }
        tileLayer.changeZoomLevel(zoomLevel, mode == FAST); // Executed in parallel for improved performance
        meepleLayer.synchronizeLayerSizes(gridWidth, gridHeight, zoomLevel); // IMPORTANT: Ensures that the meeples are on the tiles.
        meepleLayer.changeZoomLevel(zoomLevel);
        ThreadingUtil.runInBackground(() -> scrollPane.validateAndCenter());
        scrollPane.repaintLayers(); // IMPORTANT: Prevents meeples from disappearing.
    }

    /**
     * Notifies the the main view about a (new) current player. This allows the UI to adapt color schemes to the player.
     * @param currentPlayer is the current {@link Player}.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        if (this.currentPlayer == null) {
            scrollPane.validateAndCenter(); // ensures centered view on game start for heterogenous multi-monitor setups
        }
        this.currentPlayer = currentPlayer;
        ImageIcon newHighlight = PaintShop.getColoredHighlight(currentPlayer, zoomLevel, false);
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
        scrollPane.repaintLayers(); // This is required! TODO (MEDIUM) Is there a better way for AI moves than this?
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
     * Draws the tile on a specific position on the view.
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

    private void buildFrame() {
        tileLayer = new TileLayer(controller, gridHeight, gridWidth, zoomLevel);
        meepleLayer = new MeepleLayer(controller, gridWidth, gridHeight, zoomLevel);
        menuBar = new MainMenuBar(controller, this);
        setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        scrollPane = LookAndFeelUtil.createModifiedScrollpane();
        scrollPane.addLayers(meepleLayer, tileLayer);
        scrollPane.addZoomListener(() -> zoomIn(FAST), () -> zoomOut(FAST));
        add(scrollPane, BorderLayout.CENTER);
        setMinimumSize(MINIMAL_WINDOW_SIZE);
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