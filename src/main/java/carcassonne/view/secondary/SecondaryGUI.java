package carcassonne.view.secondary;

import java.awt.GridBagLayout;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.settings.Notifiable;
import carcassonne.view.GlobalKeyBindingManager;
import carcassonne.view.main.MainGUI;

/**
 * Super class for all other smaller GUI beneath the main GUI.
 * @author Timur Saglam
 */
public abstract class SecondaryGUI extends JDialog implements Notifiable {
    private static final int INITIAL_X = 100;
    private static final int INITIAL_Y = 150;
    private static final long serialVersionUID = 4056347951568551115L;
    protected MainController controller;
    protected Player currentPlayer;
    protected JPanel dialogPanel;

    /**
     * Constructor for the class. Sets the controller of the GUI and the window title.
     * @param controller sets the {@link MainController}.
     * @param ui is the main graphical user interface.
     */
    public SecondaryGUI(MainController controller, MainGUI ui) {
        super(ui);
        dialogPanel = new JPanel(new GridBagLayout());
        this.controller = controller;
        buildFrame();
    }

    /**
     * Adds the global key bindings to this UI.
     * @param keyBindings are the global key bindings.
     */
    public void addKeyBindings(GlobalKeyBindingManager keyBindings) {
        InputMap inputMap = dialogPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = dialogPanel.getActionMap();
        keyBindings.addKeyBindingsToMaps(inputMap, actionMap);
    }

    @Override
    public void notifyChange() {
        if (currentPlayer != null) { // only if UI is in use.
            dialogPanel.setBackground(currentPlayer.getColor().lightColor());
        }
    }

    /*
     * Builds the frame and sets its properties.
     */
    private void buildFrame() {
        getContentPane().add(dialogPanel);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(INITIAL_X, INITIAL_Y);
    }

    /**
     * Sets the current player and updates the background color.
     * @param currentPlayer is the player who is supposed to play his turn.
     */
    protected void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        dialogPanel.setBackground(currentPlayer.getColor().lightColor());
        setBackground(currentPlayer.getColor().lightColor());
    }

    /**
     * Show the secondary UI and bring it to the front.
     */
    protected void showUI() {
        pack();
        setVisible(true);
        toFront(); // sets the focus on the secondary GUI, removes need for double clicks
    }

}
