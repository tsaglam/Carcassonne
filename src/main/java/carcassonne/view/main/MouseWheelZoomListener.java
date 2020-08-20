package carcassonne.view.main;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Extended {@link MouseWheelListener} that calls one of two zoom functions on a mouse wheel event with CTRL or META
 * pressed. All other events are delegated to a dedicated parent component.
 * @author Timur Saglam
 */
public class MouseWheelZoomListener implements MouseWheelListener {
    private static final int ZOOM_IN_THRESHOLD = -3;
    private static final int ZOOM_OUT_THRESHOLD = 1;
    private final Runnable zoomIn;
    private final Runnable zoomOut;
    private final Component parent;
    private double scrollProgress;

    /**
     * Creates the listener.
     * @param zoomIn is the zoom-in function to call on a fitting mouse wheel event.
     * @param zoomOut is the zoom-out function to call on a fitting mouse wheel event.
     * @param parent is the dedicated parent component where non-zoom events are delegated to.
     */
    public MouseWheelZoomListener(Runnable zoomIn, Runnable zoomOut, Component parent) {
        super();
        this.zoomIn = zoomIn;
        this.zoomOut = zoomOut;
        this.parent = parent;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) { // TODO (HIGH) this is now optimized for a mac touchpads and not for mouse wheels
        if (event.isControlDown() || event.isMetaDown()) {
            scrollProgress += event.getPreciseWheelRotation();
            System.err.println(scrollProgress + " " + event.getPreciseWheelRotation() + " " + event.getScrollAmount() + " " + event.getUnitsToScroll()
                    + " " + event.getScrollType());
            if (scrollProgress > ZOOM_OUT_THRESHOLD) {
                zoomOut.run();
                scrollProgress = 0;
            } else if (scrollProgress < ZOOM_IN_THRESHOLD) {
                zoomIn.run();
                scrollProgress = 0;
            }
        } else {
            parent.dispatchEvent(event);
        }
    }
}
