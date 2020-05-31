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
    private static final int MAXIMUM_VALUE = 300;
    private static final int MINIMUM_VALUE = 25;
    private static final int SLIDER_STEP_SIZE = 25;
    private JMenuItem zoomOut;
    private JMenuItem zoomIn;
    private ZoomSliderListener zoomListener;

    /**
     * Creates the slider.
     * @param mainUI is the correlatin main user interface.f
     */
    public ZoomSlider(MainGUI mainUI) {
        super(MINIMUM_VALUE, MAXIMUM_VALUE, mainUI.getZoom());
        setPaintTicks(true);
        setOrientation(SwingConstants.VERTICAL);
        setMinorTickSpacing(5);
        setMajorTickSpacing(50);
        setSnapToTicks(true);
        zoomListener = new ZoomSliderListener(mainUI, this);
        addMouseListener(zoomListener);
        addChangeListener(zoomListener);
        zoomIn = new JMenuItem("Zoom In (+)");
        zoomOut = new JMenuItem("Zoom Out (-)");
        zoomIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                setValue(getValue() + SLIDER_STEP_SIZE);
            }
        });
        zoomOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                setValue(getValue() - SLIDER_STEP_SIZE);
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
