package carcassonne.view.main;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.stream.Stream;

import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.OverlayLayout;

/**
 * {@link JScrollPane} that depicts the layers of a {@link JLayeredPane}. It is just a scroll pane that manages its own
 * layered pane.
 * @author Timur Saglam
 */
public class LayeredScrollPane extends JScrollPane {
    private static final int PERFORMANCE_THRESHOLD = 2000; // if there are more tiles UI validation gets expensive
    private static final double SIZE_FACTOR = 0.75; // affects validation, see shouldValidate()
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
     * Centers the scroll pane view to show the center of the grid. Uses calculated grid information to avoid validation.
     * @param width is the width of the tile grid in pixels.
     * @param height is the height of the tile grid in pixels.
     */
    public void centerView(int width, int height) {
        if (width * height < PERFORMANCE_THRESHOLD || shouldValidate(width, height)) {
            validateAndCenterView();
        } else {
            centerViewManually(width, height);
        }
    }

    /**
     * Centers the scroll pane view to show the center of the grid. Uses calculated grid information to avoid validation.
     * @param width is the width of the tile grid in pixels.
     * @param height is the height of the tile grid in pixels.
     */
    public void centerViewManually(int width, int height) {
        Rectangle bounds = getViewport().getViewRect();
        int horizontal = Math.max(0, (width - bounds.width) / 2);
        int vertical = Math.max(0, (height - bounds.height) / 2);
        getHorizontalScrollBar().setValue(horizontal);
        getVerticalScrollBar().setValue(vertical);
    }

    /**
     * Centers the scroll pane view to show the center of the grid. Since this method revalidates the viewport it can be
     * expensive if the scroll pane contains complex content (e.g. a very large grid).
     */
    public void validateAndCenterView() { // TODO (HIGH) zoom based on view center and not grid center?
        getViewport().validate(); // IMPORTANT: required to allow proper centering
        Rectangle bounds = getViewport().getViewRect();
        JScrollBar horizontal = getHorizontalScrollBar();
        horizontal.setValue((horizontal.getMaximum() - bounds.width) / 2);
        JScrollBar vertical = getVerticalScrollBar();
        vertical.setValue((vertical.getMaximum() - bounds.height) / 2);
    }

    /*
     * Determines if the tile grid is just large enough to be a little bit bigger as the scroll pane view.
     */
    private boolean shouldValidate(int width, int height) {
        Rectangle visible = getViewport().getVisibleRect();
        return width * SIZE_FACTOR < visible.width && width >= visible.width || height * SIZE_FACTOR < visible.height && height >= visible.height;
    }
}
