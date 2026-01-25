package carcassonne.view.menubar;

import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import carcassonne.view.main.MainView;
import carcassonne.view.main.ZoomMode;
import carcassonne.view.util.ZoomConfig;

/**
 * Custom {@link JSlider} for the zoom functionality. Additionally, this class creates the zoom in/out menu items.
 * @author Timur Saglam
 */
public class ZoomSlider extends JSlider {
    private static final long serialVersionUID = -5518487902213410121L;

    // UI CONSTANTS:
    private static final int MAJOR_TICK = 64;
    private static final int MINOR_TICK = 4;
    private static final String ZOOM_OUT = "Zoom Out (-)";
    private static final String ZOOM_IN = "Zoom In (+)";

    // FIELDS:
    private final JMenuItem zoomOut;
    private final JMenuItem zoomIn;
    private final ZoomSliderListener zoomListener;

    /**
     * Creates the slider.
     * @param mainView is the correlating main user interface.f
     */
    public ZoomSlider(MainView mainView) {
        super(ZoomConfig.MIN_LEVEL.pixels(), ZoomConfig.MAX_LEVEL.pixels(), mainView.getZoom());
        setPaintTicks(true);
        setOrientation(SwingConstants.VERTICAL);
        setMinorTickSpacing(MINOR_TICK);
        setMajorTickSpacing(MAJOR_TICK);
        setSnapToTicks(true);
        zoomListener = new ZoomSliderListener(mainView, this);
        addMouseListener(zoomListener);
        addChangeListener(zoomListener);
        zoomIn = new JMenuItem(ZOOM_IN);
        zoomOut = new JMenuItem(ZOOM_OUT);
        zoomIn.addActionListener(event -> {
            setValueSneakily(getValue() + ZoomConfig.STEP.pixels());
            mainView.zoomIn(ZoomMode.SMOOTH);
        });
        zoomOut.addActionListener(event -> {
            setValueSneakily(getValue() - ZoomConfig.STEP.pixels());
            mainView.zoomOut(ZoomMode.SMOOTH);
        });
    }

    /**
     * Sets the slider value without triggering the zoom slider listener event.
     * @param value is the value to set.
     * @see JSlider#setValue(int)
     */
    public void setValueSneakily(int value) {
        zoomListener.setBlockingEvents(true);
        setValue(value);
        zoomListener.setBlockingEvents(false);
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
