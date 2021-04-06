package carcassonne.view.main;

import java.awt.Component;
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
     * Adds a zoom listener that listens to the mouse wheel and then calls one of two zoom functions.
     * @param zoomIn the function to zoom in.
     * @param zoomOut the function to zoom out.
     */
    public void addZoomListener(Runnable zoomIn, Runnable zoomOut) {
        layeredPane.addMouseWheelListener(new MouseWheelZoomListener(zoomIn, zoomOut, layeredPane.getParent()));
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
     * Centers the scroll pane view to show the center of the grid. Since this method revalidates the viewport it can be
     * expensive if the scroll pane contains complex content (e.g. a very large grid).
     */
    public void validateAndCenter() { // TODO (HIGH) [ZOOMING] zoom based on view center and not grid center?
        validate(); // IMPORTANT: required to allow proper centering
        centerScrollBars(getHorizontalScrollBar().getMaximum(), getVerticalScrollBar().getMaximum());
    }

    /**
     * Centers the scroll bars by using the grid dimensions.
     * @param width is the width of the tile grid in pixels.
     * @param height is the height of the tile grid in pixels.
     */
    private void centerScrollBars(int width, int height) {
        Rectangle view = getViewport().getViewRect();
        getHorizontalScrollBar().setValue(Math.max(0, (width - view.width) / 2));
        getVerticalScrollBar().setValue(Math.max(0, (height - view.height) / 2));
    }
}
