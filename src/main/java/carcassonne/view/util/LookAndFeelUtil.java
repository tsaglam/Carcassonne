package carcassonne.view.util;

import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.view.main.LayeredScrollPane;

/**
 * Utility class for functionality regarding the {@link LookAndFeel} and the {@link UIManager}.
 * @author Timur Saglam
 */
public final class LookAndFeelUtil {
    private static final String WINDOWS = "Windows";
    private static final String OS_NAME_PROPERTY = "os.name";

    private LookAndFeelUtil() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Creates a modified {@link JScrollPane} that uses the default {@link LookAndFeel} instead of the current one (e.g.
     * Nimbus). If the operating system is not Windows, a normal {@link JScrollPane} is created, as the Nimbus scroll bars
     * are not deeply broken on decent operating systems.
     * @return the modified {@link JScrollPane} that always has good-looking scroll bars.
     */
    public static LayeredScrollPane createModifiedScrollPane() {
        if (System.getProperty(OS_NAME_PROPERTY).startsWith(WINDOWS)) {
            return createScrollPaneWithFixedScrollbars();
        }
        return new LayeredScrollPane();
    }

    /*
     * Switches the LookAndFeel to the default one, creates a JScrollPane with that LookAndFeel and then switches back to
     * the original LookAndFeel.
     */
    private static LayeredScrollPane createScrollPaneWithFixedScrollbars() {
        LayeredScrollPane modifiedScrollPane = null;
        try {
            LookAndFeel previousLF = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            modifiedScrollPane = new LayeredScrollPane();
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        return modifiedScrollPane;
    }
}
