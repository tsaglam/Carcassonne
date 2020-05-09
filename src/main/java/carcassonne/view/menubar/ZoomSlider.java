package carcassonne.view.menubar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import carcassonne.view.main.MainGUI;

/**
 * Custom {@link JSlider} for the zoom functionality. Additionally, this class creates the zoom in/out menu items.
 * @author Timur Saglam
 */
public class ZoomSlider extends JSlider {
    private static final long serialVersionUID = -5518487902213410121L;
    private JMenuItem zoomOut;
    private JMenuItem zoomIn;

    public ZoomSlider(MainGUI mainUI) {
        super(50, 300, mainUI.getZoom());
        setPaintTicks(true);
        setOrientation(SwingConstants.VERTICAL);
        setMinorTickSpacing(5);
        setMajorTickSpacing(50);
        setSnapToTicks(true);
        ZoomSliderListener zoomListener = new ZoomSliderListener(mainUI, this);
        addMouseListener(zoomListener);
        addChangeListener(zoomListener);
        zoomIn = new JMenuItem("Zoom In");
        zoomOut = new JMenuItem("Zoom Out");
        zoomIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                mainUI.zoomIn();
                zoomListener.setBlocked(true);
                setValue(mainUI.getZoom());
                zoomListener.setBlocked(false);
            }
        });
        zoomOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                mainUI.zoomOut();
                zoomListener.setBlocked(true);
                setValue(mainUI.getZoom());
                zoomListener.setBlocked(false);
            }
        });
    }

    /**
     * Grants access to a menu item for a zoom out step. It updates the slider when clicked and guarantees non-interference.
     * @return the zoomOut the the
     */
    public JMenuItem getZoomOut() {
        return zoomOut;
    }

    /**
     * Grants access to a menu item for a zoom in step. It updates the slider when clicked and guarantees non-interference.
     * @return the zoomIn menu item.
     */
    public JMenuItem getZoomIn() {
        return zoomIn;
    }
}
