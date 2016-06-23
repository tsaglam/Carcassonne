package carcassonne.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * The menu bar for the main gui.
 * @author Timur
 */
public class MainMenuBar extends JMenuBar {

    private static final long serialVersionUID = -599734693130415390L;
    private JMenu menuFile;
    private JMenu menuOptions;
    private JMenu menuDeveloperTools;

    /**
     * Simple constructor
     */
    public MainMenuBar() {
        super();

        menuFile = new JMenu("Game");
        menuOptions = new JMenu("Options");
        menuDeveloperTools = new JMenu("Developer Tools");
        add(menuFile);
        add(menuOptions);
        add(menuDeveloperTools);
    }

}
