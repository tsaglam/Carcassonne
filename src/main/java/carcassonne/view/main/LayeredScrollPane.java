package carcassonne.view.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.stream.Stream;

import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;

/**
 * {@link JScrollPane} that depicts the layers of a {@link JLayeredPane}. It is just a scroll pane that manages its own
 * layered pane.
 * @author Timur Saglam
 */
public class LayeredScrollPane extends JScrollPane {

    private static final long serialVersionUID = 7863596860273426396L;
    private static final int SCROLL_SPEED = 15;
    private final JLayeredPane layeredPane;

    /**
     * Creates a layered scroll pane and centers it for a certain grid size.
     */
    public LayeredScrollPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        setViewportView(layeredPane);
        getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);
    }

    /**
     * Adds any number of components as layers in the given order.
     * @param components the components to add as layers.
     */
    public void addLayers(Component... components) {
        for (int i = 0; i < components.length; i++) {
            layeredPane.add(components[i], i);
        }
    }

    /**
     * Removes any number of layers if they are part of this scroll pane.
     * @param components are the components and therefore layers to remove.
     */
    public void removeLayers(Component... components) {
        Stream.of(components).forEach(layeredPane::remove);
    }

    /**
     * Repaint the layered pane and its layers.
     */
    public void repaintLayers() {
        layeredPane.repaint();
    }

    /**
     * Determines for a given grid size and zoom level of the full grid is shown in the view.
     * @param gridWidth is the width of the grid in tiles.
     * @param gridHeight is the height of the grid in tiles.
     * @param zoomLevel is the zoom level and also the tile edge length in pixel.
     * @return true if it shows the full grid
     */
    public boolean showsFullGrid(int gridWidth, int gridHeight, int zoomLevel) {
        Rectangle bounds = getViewport().getViewRect();
        return gridWidth * zoomLevel < bounds.width || gridHeight * zoomLevel < bounds.height;
    }

    /**
     * Centers the scroll pane view to show the center of the grid. Make sure to call revalidate on the UI root before
     * calling this method to avoid incorrect centering.
     * @param gridWidth is the current grid with in tiles.
     * @param gridHeight is the current height with in tiles.
     * @param zoomLevel is the current zoom level and therefore also the tile size.
     */
    public void centerScrollPaneView(int gridWidth, int gridHeight, int zoomLevel) { // TODO (HIGH) zoom based on view center and not grid center?
        Dimension size = new Dimension(gridWidth * zoomLevel, gridHeight * zoomLevel);
        Rectangle bounds = getViewport().getViewRect();
        int x = (int) (Math.round((size.width - bounds.width) / 2.0));
        int y = (int) (Math.round((size.height - bounds.height) / 2.0));
        getViewport().setViewPosition(new Point(x, y));
    }
}
