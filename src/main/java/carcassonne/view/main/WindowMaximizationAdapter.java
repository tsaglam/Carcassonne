package carcassonne.view.main;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * {@link WindowAdapter} that fixes an issue with proper window maximization on Windows 10. When adding this adapter to
 * a {@link JFrame} it ensures its proper maximization (once) on activation. Just another little hack to fix Windows
 * issues.
 * @author Timur Saglam
 */
public class WindowMaximizationAdapter extends WindowAdapter {
    private static final String OS_NAME_KEY = "os.name";
    private static final String WINDOWS = "Windows";

    private final JFrame frame;
    private boolean maximized;

    /**
     * Creates the adapter for a specific {@link JFrame}
     * @param frame is the specific {@link JFrame} to be maximized.
     */
    public WindowMaximizationAdapter(JFrame frame) {
        this.frame = frame;
        maximized = false;
    }

    @Override
    public void windowActivated(WindowEvent event) {
        if (!maximized && System.getProperty(OS_NAME_KEY).startsWith(WINDOWS)) {
            maximized = true;
            frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        }
    }
}
