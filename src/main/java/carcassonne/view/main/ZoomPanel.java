package carcassonne.view.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

/**
 * Specialized {@link JPanel} that can be zoomed and dragged.
 * @author Timur Saglam
 */
public class ZoomPanel extends JPanel {

    private static final long serialVersionUID = 5955569383721340729L;
    private double zoomFactor = 1;
    private double previousZoomFactor = 1;
    private boolean unpaintedZoom;
    private boolean unpaintedDrag;
    private boolean dragging;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xAxisDrag;
    private int yAxisDrag;

    /**
     * Creates a transparent {@link JPanel} that can be zoomed.
     */
    public ZoomPanel() {
        super();
        setOpaque(false);
    }

    /**
     * Sets the current drag.
     * @param xAxisDrag is the X-axis drag.
     * @param yAxisDrag is the Y-axis drag.
     */
    public void setDrag(int xAxisDrag, int yAxisDrag) {
        this.xAxisDrag = xAxisDrag;
        this.yAxisDrag = yAxisDrag;
        unpaintedDrag = true;
    }

    /**
     * Sets the current zoom factor.
     * @param zoomFactor is the current zoom factor.
     */
    public void setZoom(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        unpaintedZoom = true;
    }

    /**
     * Notifies the panel if it is currently dragged or not.
     * @param dragging specifies if it is dragged.
     */
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Notifies the panel to update the positional offset based on the drag.
     */
    public void updateOffset() {
        xOffset += xAxisDrag;
        yOffset += yAxisDrag;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (unpaintedZoom) {
            AffineTransform affineTransform = new AffineTransform();
            double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
            double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
            double zoomRatio = zoomFactor / previousZoomFactor;
            xOffset = (zoomRatio) * (xOffset) + (1 - zoomRatio) * xRel;
            yOffset = (zoomRatio) * (yOffset) + (1 - zoomRatio) * yRel;
            affineTransform.translate(xOffset, yOffset);
            affineTransform.scale(zoomFactor, zoomFactor);
            previousZoomFactor = zoomFactor;
            graphics2D.transform(affineTransform);
            unpaintedZoom = false;
        }
        if (unpaintedDrag) {
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.translate(xOffset + xAxisDrag, yOffset + yAxisDrag);
            affineTransform.scale(zoomFactor, zoomFactor);
            graphics2D.transform(affineTransform);
            if (!dragging) {
                unpaintedDrag = false;
            }
        }
        super.paintComponent(graphics2D);
    }
}
