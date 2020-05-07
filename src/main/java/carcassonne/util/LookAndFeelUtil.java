package carcassonne.util;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Utility class for functionality regarding the {@link LookAndFeel} and the {@link UIManager}.
 * @author Timur Saglam
 */
public class LookAndFeelUtil {
    private static final String WINDOWS = "WINDOWS";
    private static final String OS_NAME_PROPERTY = "os.name";

    /**
     * Creates a modified {@link JScrollPane} that uses the default {@link LookAndFeel} instead of the current one (e.g.
     * Nimbus). If the operating system is not Windows, a normal {@link JScrollPane} is created, as the Nimbus scroll bars
     * are not deeply broken on decent operating systems.
     * @param component is the component to display in the scrollpane's viewport.
     * @return the modified {@link JScrollPane} that always has good looking scroll bars.
     */
    public static JScrollPane createModifiedScrollpane(Component component) {
        if (System.getProperty(OS_NAME_PROPERTY).startsWith(WINDOWS)) {
            return createScrollpaneWithFixedScrollbars(component);
        }
        return new JScrollPane(component);
    }

    /*
     * Switches the LookAndFeel to the default one, creates a JScrollPane with that LookAndFeel and then switches back to
     * the original LookAndFeel.
     */
    private static JScrollPane createScrollpaneWithFixedScrollbars(Component component) {
        JScrollPane modifiedScrollPane = null;
        try {
            LookAndFeel previousLF = UIManager.getLookAndFeel();
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            modifiedScrollPane = new JScrollPane(component);
            UIManager.setLookAndFeel(previousLF);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        return modifiedScrollPane;
    }
}
