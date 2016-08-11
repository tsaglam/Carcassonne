package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;

/**
 * Super class for all other smaller GUI beneath the main GUI.
 * @author Timur
 */
public abstract class SecondaryGUI extends JPanel {
    private static final long serialVersionUID = 4056347951568551115L;
    protected MainController controller;
    protected GameOptions options;
    protected JFrame frame;
    protected GridBagConstraints constraints;
    protected Tile tile;
    protected int currentPlayer;

    /**
     * Constructor for the class. Sets the controller of the GUI and the window title.
     * @param controller sets the controller.
     * @param title sets the window title.
     */
    public SecondaryGUI(MainController controller, String title) {
        super(new GridBagLayout());
        this.controller = controller;
        options = GameOptions.getInstance();
        constraints = new GridBagConstraints();
        currentPlayer = -1;
        buildFrame(title);
    }

    /**
     * Sets the tile of the GUI, updates the GUI and then makes it visible. Should be called to show
     * the GUI. The method implements the template method pattern using the method
     * <code>update()</code>.
     * @param tile
     */
    public void setTile(Tile tile, int currentPlayer) {
        if (tile == null) {
            throw new IllegalArgumentException("Tried to set the tile of the " + getClass().getSimpleName() + " to null.");
        }
        this.tile = tile;
        this.currentPlayer = currentPlayer;
        setBackground(options.getPlayerColor(currentPlayer));
        update();
        frame.setVisible(true);
    }

    /*
     * Builds the frame and sets its properties.
     */
    private void buildFrame(String title) {
        frame = new JFrame(title);
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocation(options.resolutionWidth / 20, options.resolutionHeight / 5);
    }

    /**
     * Should be called at the end of a constructor of a subclass.
     */
    protected void finishFrame() {
        frame.pack();
    }

    /**
     * Hides the GUI.
     */
    public void disableFrame() {
        frame.setVisible(false);
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update
     * the GUI content according to the tiles properties.
     */
    protected abstract void update();

}
