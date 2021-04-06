package carcassonne.view.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import carcassonne.control.ControllerFacade;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.view.util.RigidRectangle;

/**
 * A UI component that contains multiple meeple depiction panels.
 * @author Timur Saglam
 */
public class MeepleLayer extends JPanel {
    private static final long serialVersionUID = -843137441362337953L;
    private List<MeepleDepictionPanel> meeplePanels;
    private MeepleDepictionPanel[][] meeplePanelGrid;
    private JComponent[][] placeholderGrid;
    private final List<RigidRectangle> placeholders;
    private int zoomLevel;
    private ControllerFacade controller;

    /**
     * Creates the meeple layer.
     * @param controller is the main controller.
     * @param gridWidth is the width of the grid in tiles.
     * @param gridHeight is the height of the grid in tile.
     * @param zoomLevel is the zoom level, and therefore the tile size.
     */
    public MeepleLayer(ControllerFacade controller, int gridWidth, int gridHeight, int zoomLevel) {
        this.controller = controller;
        this.zoomLevel = zoomLevel;
        setOpaque(false);
        setLayout(new GridBagLayout());
        synchronizeLayerSizes(gridWidth, gridHeight, zoomLevel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1; // evenly distributes meeple grid
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        meeplePanels = new ArrayList<>();
        meeplePanelGrid = new MeepleDepictionPanel[gridWidth][gridHeight]; // build array of labels.
        placeholders = new ArrayList<>();
        placeholderGrid = new JComponent[gridWidth][gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                constraints.gridx = x;
                constraints.gridy = y;
                RigidRectangle rectangle = new RigidRectangle(zoomLevel);
                placeholderGrid[x][y] = rectangle;
                placeholders.add(rectangle);
                add(rectangle, constraints);
            }
        }
    }

    private void initializeLazily(int x, int y) {
        if (meeplePanelGrid[x][y] == null) {
            remove(placeholderGrid[x][y]);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = 1; // evenly distributes meeple grid
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.gridx = x;
            constraints.gridy = y;
            meeplePanelGrid[x][y] = new MeepleDepictionPanel(zoomLevel, controller);
            meeplePanels.add(meeplePanelGrid[x][y]);
            add(meeplePanelGrid[x][y], constraints); // add label with constraints
            validate();
        }
    }

    /**
     * Adapts the meeples in this layer to a new zoom level.
     * @param zoomLevel is the new zoom level.
     */
    public void changeZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
        placeholders.parallelStream().forEach(it -> it.changeShape(zoomLevel));
        meeplePanels.forEach(it -> it.setSize(zoomLevel));
    }

    /**
     * Enables the meeple preview on a specific panel correlating to a tile.
     * @param x is the x-coordinate of that panel.
     * @param y is the y-coordinate of that panel.
     * @param tile is the correlating tile.
     * @param currentPlayer is the player who is currently active.
     */
    public void enableMeeplePreview(int x, int y, Tile tile, Player currentPlayer) {
        initializeLazily(x, y);
        meeplePanelGrid[x][y].setMeeplePreview(tile, currentPlayer);
    }

    /**
     * Places a meeple on a panel (correlating to a tile) on a specific position.
     * @param x is the x-coordinate of that panel.
     * @param y is the y-coordinate of that panel.
     * @param terrain determines the meeple type.
     * @param position is the position where the meeple is placed on the panel.
     * @param owner is the player who owns the meeple.
     */
    public void placeMeeple(int x, int y, TerrainType terrain, GridDirection position, Player owner) {
        initializeLazily(x, y);
        meeplePanelGrid[x][y].placeMeeple(terrain, position, owner);
    }

    /**
     * Refreshes all meeple labels in this layer. This updates the images to color changes.
     */
    public void refreshLayer() {
        meeplePanels.parallelStream().forEach(it -> it.refreshAll());
    }

    /**
     * Resets all meeples in the layer.
     */
    public void resetLayer() {
        meeplePanels.parallelStream().forEach(it -> it.resetAll());
    }

    /**
     * Resets all meeple in a specific panel correlating to a tile.
     * @param x is the x-coordinate of that panel.
     * @param y is the y-coordinate of that panel.
     */
    public void resetPanel(int x, int y) {
        meeplePanelGrid[x][y].resetAll();
    }

    /**
     * Adapts the size of this layer to the tile grid.
     * @param gridWidth is the width of the grid in tiles.
     * @param gridHeight is the height of the grid in tile.
     * @param zoomLevel is the zoom level, and therefore the tile size.
     */
    public void synchronizeLayerSizes(int gridWidth, int gridHeight, int zoomLevel) {
        Dimension layerSize = new Dimension(gridWidth * zoomLevel, gridHeight * zoomLevel);
        setMaximumSize(layerSize);
        setPreferredSize(layerSize);
        setMinimumSize(layerSize);
    }

}
