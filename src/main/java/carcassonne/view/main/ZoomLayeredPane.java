package carcassonne.view.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/**
 * Custom {@link JLayeredPane} which allows zooming and dragging its content. Content that should be able to be zoomed
 * or dragged needs to be added in a {@link ZoomPanel}.
 * @author Timur Saglam
 */
public class ZoomLayeredPane extends JLayeredPane {
    private static final long serialVersionUID = -3506712384606634583L;
    private static final double ZOOM_STEP = 1.1;
    private static final double MAX_ZOOM_LEVEL = 0.1;
    private static final int MIN_ZOOM_LEVEL = 10;

    private final List<ZoomPanel> zoomablePanels;
    private Point dragStartingPoint;
    private double zoomFactor = 1;

    /**
     * Creates a {@link JLayeredPane} that allows zooming and dragging its content.
     */
    public ZoomLayeredPane() {
        new JLayeredPane();
        setLayout(new OverlayLayout(this));
        zoomablePanels = new LinkedList<>();
        initListeners();
        setVisible(true);
    }

    /**
     * Adds a {@link JPanel} to this {@link JLayeredPane} that can be zoomed and dragged.
     * @param panel is the this the {@link JPanel}.
     * @return the component argument, which is the return value of {@link Container#add(Component)}
     */
    public Component addZoomablePanel(ZoomPanel panel) {
        zoomablePanels.add(panel);
        return add(panel);
    }

    /**
     * Adds a {@link JPanel} to this {@link JLayeredPane} that can be zoomed and dragged.
     * @param panel is the this the {@link JPanel}.
     * @param index is the layering index, meaning where the panel is to be inserted between existing layers.
     * @return the component argument, which is the return value of {@link Container#add(Component)}
     */
    public Component addZoomablePanel(ZoomPanel panel, int index) {
        zoomablePanels.add(panel);
        return add(panel, index);
    }

    private void initListeners() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                Point currentPoint = event.getLocationOnScreen();
                int xAxisDelta = currentPoint.x - dragStartingPoint.x;
                int yAxisDelta = currentPoint.y - dragStartingPoint.y;
                zoomablePanels.forEach(it -> it.setDrag(xAxisDelta, yAxisDelta));
                repaint();
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if (event.getWheelRotation() < 0 && zoomFactor < MIN_ZOOM_LEVEL) {
                    zoomFactor *= ZOOM_STEP;
                } else if (event.getWheelRotation() > 0 && zoomFactor > MAX_ZOOM_LEVEL) {
                    zoomFactor /= ZOOM_STEP;
                }
                zoomablePanels.forEach(it -> it.setZoom(zoomFactor));
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                dragStartingPoint = MouseInfo.getPointerInfo().getLocation();
                zoomablePanels.forEach(it -> it.setDragging(true));
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                zoomablePanels.forEach(it -> it.setDragging(false));
                zoomablePanels.forEach(it -> it.updateOffset());
            }
        });
    }
}
