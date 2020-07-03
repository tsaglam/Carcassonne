package carcassonne.view.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * A UI component that contains multiple tile panels.
 * @author Timur Saglam
 */
public class TileLayer extends JPanel {
    private static final long serialVersionUID = 1503933201337556131L;
    private List<TileDepiction> tileLabels;
    private TileDepiction[][] tileLabelGrid;

    /**
     * Creates the tile layer.
     * @param controller is the main controller.
     * @param gridWidth is the width of the grid in tiles.
     * @param gridHeight is the height of the grid in tile.
     * @param zoomLevel is the zoom level, and therefore the tile size.
     */
    public TileLayer(MainController controller, int gridHeight, int gridWidth, int zoomLevel) {
        setBackground(GameSettings.GUI_COLOR);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
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
                add(tileLabelGrid[x][y].getLabel(), constraints); // add label with constraints
            }
        }
    }

    /**
     * Adapts the layer to a new zoom level.
     * @param zoomLevel is the zoom level, and therefore also the tile size.
     * @param preview determines if the tiles are rendered in preview mode (fast but ugly).
     */
    public void changeZoomLevel(int zoomLevel, boolean preview) {
        tileLabels.parallelStream().forEach(it -> it.setTileSize(zoomLevel, preview));
    }

    /**
     * Highlights a specific tile.
     * @param x is the x-coordinate of that tile.
     * @param y is the y-coordinate of that tile.
     */
    public void highlightTile(int x, int y) {
        tileLabelGrid[x][y].highlight();
    }

    /**
     * Places a tile, updating the correlating tile label.
     * @param tile is the tile to place.
     * @param x is the x-coordinate of that tile.
     * @param y is the y-coordinate of that tile.
     */
    public void placeTile(Tile tile, int x, int y) {
        tileLabelGrid[x][y].setTile(tile);
    }

    /**
     * Refreshes the highlight image.
     * @param newHighlight is the new image.
     */
    public void refreshHighlight(ImageIcon newHighlight) {
        tileLabels.parallelStream().forEach(it -> it.setColoredHighlight(newHighlight));
    }

    /**
     * Resets every tile label in this layer.
     */
    public void resetLayer() {
        tileLabels.parallelStream().forEach(it -> it.reset());
    }
}
