package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.tile.Tile;
import carcassonne.settings.Notifiable;
import carcassonne.settings.SystemProperties;
import carcassonne.view.main.MainGUI;

/**
 * Super class for all other smaller GUI beneath the main GUI.
 * @author Timur Saglam
 */
public abstract class SecondaryGUI extends JDialog implements Notifiable {
    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 150;
    private static final long serialVersionUID = 4056347951568551115L;
    protected GridBagConstraints constraints;
    protected MainController controller;
    protected Player currentPlayer;
    protected JPanel panel;
    protected SystemProperties systemProperties;
    protected Tile tile;

    /**
     * Constructor for the class. Sets the controller of the GUI and the window title.
     * @param controller sets the {@link MainController}.
     * @param ui is the main graphical user interface.
     */
    public SecondaryGUI(MainController controller, MainGUI ui) {
        super(ui);
        panel = new JPanel(new GridBagLayout());
        this.controller = controller;
        systemProperties = new SystemProperties();
        constraints = new GridBagConstraints();
        buildFrame(ui);
    }

    /**
     * Sets the tile of the GUI, updates the GUI and then makes it visible. Should be called to show the GUI. The method
     * implements the template method pattern using the method <code>update()</code>.
     * @param tile sets the tile.
     * @param currentPlayer sets the color scheme according to the player.
     */
    public void setTile(Tile tile, Player currentPlayer) {
        if (tile == null) {
            throw new IllegalArgumentException("Tried to set the tile of the " + getClass().getSimpleName() + " to null.");
        }
        this.tile = tile;
        this.currentPlayer = currentPlayer;
        panel.setBackground(currentPlayer.getColor().lightColor());
        update(); // TODO (MEDIUM) rename method, be more specific
        setVisible(true);
        toFront(); // sets the focus on the secondary GUI, removes need for double clicks
    }

    /*
     * Builds the frame and sets its properties.
     */
    private void buildFrame(MainGUI ui) {
        getContentPane().add(panel);
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(INITIAL_X, INITIAL_Y);
    }

    /**
     * Should be called at the end of a constructor of a subclass.
     */
    protected void finishFrame() {
        pack();
    }

    @Override
    public void notifyChange() {
        if (currentPlayer != null) { // only if UI is in use.
            panel.setBackground(currentPlayer.getColor().lightColor());
        }
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update the GUI content according
     * to the tiles properties.
     */
    protected abstract void update();

}
