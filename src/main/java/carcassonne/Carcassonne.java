package carcassonne;

import java.util.List;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
import carcassonne.view.util.GameMessage;

/**
 * Carcassonne main class.
 * @author Timur Saglam
 */
public final class Carcassonne {
    private static final int TOOL_TIP_DISMISS_DELAY_IN_MILLISECONDS = 30000;
    private static final String LOOK_AND_FEEL_ERROR = "Could not use Nimbus LookAndFeel. Using default instead (";
    private static final String CLOSING_BRACKET = ").";
    private static final String NIMBUS = "Nimbus";
    private static final String MAC = "Mac";
    private static final String OS_NAME_KEY = "os.name";
    private static final String METAL_PIPELINE_KEY = "sun.java2d.metal";
    private static final String FALSE = "false";
    private static final String VENDOR_KEY = "java.vendor";
    private static final String EMPTY = "";

    /**
     * Runtime vendors whose Metal rendering pipeline stalls the user interface on macOS: the AppKit thread blocks in
     * <code>CAMetalLayer nextDrawable</code> while holding the AWT lock, so the event dispatch thread waits. Add a vendor
     * after confirming the stall.
     */
    private static final List<String> RUNTIME_VENDORS_WITH_METAL_STALL = List.of("Oracle Corporation", "Homebrew");

    /**
     * Main method that starts the game.
     * @param args are not used.
     */
    public static void main(String[] args) {
        disableMetalOnMac();
        setLookAndFeel();
        ToolTipManager.sharedInstance().setDismissDelay(TOOL_TIP_DISMISS_DELAY_IN_MILLISECONDS);
        new MainController().startGame();
    }

    private Carcassonne() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Switches Java2D to the OpenGL rendering pipeline on macOS for runtimes with a stalling Metal pipeline, unless
     * <code>sun.java2d.metal</code> is set explicitly (e.g. <code>-Dsun.java2d.metal=true</code> to opt out). Must run
     * before AWT or Swing is initialized.
     */
    private static void disableMetalOnMac() {
        if (System.getProperty(OS_NAME_KEY).startsWith(MAC) && System.getProperty(METAL_PIPELINE_KEY) == null
                && RUNTIME_VENDORS_WITH_METAL_STALL.contains(System.getProperty(VENDOR_KEY, EMPTY))) {
            System.setProperty(METAL_PIPELINE_KEY, FALSE);
        }
    }

    /**
     * Tries to set a custom look and feel if the operating system is not macOS. This ensures an at least somewhat decent
     * user interface on Windows operating systems.
     */
    private static void setLookAndFeel() {
        if (!System.getProperty(OS_NAME_KEY).startsWith(MAC)) {
            for (LookAndFeelInfo lookAndFeel : UIManager.getInstalledLookAndFeels()) {
                if (NIMBUS.equals(lookAndFeel.getName())) {
                    try {
                        UIManager.setLookAndFeel(lookAndFeel.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
                        GameMessage.showError(LOOK_AND_FEEL_ERROR + exception.getMessage() + CLOSING_BRACKET);
                    }
                }
            }
        }
    }
}
