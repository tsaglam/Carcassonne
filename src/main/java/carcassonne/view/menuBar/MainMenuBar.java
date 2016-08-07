package carcassonne.view.menuBar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import carcassonne.control.MainController;

/**
 * The menu bar for the main gui.
 * @author Timur
 */
public class MainMenuBar extends JMenuBar {

    private static final long serialVersionUID = -599734693130415390L;
    private MainController controller;
    private int playerCount;
    private JMenu menuGame;
    private JMenu menuOptions;
    private JMenu menuPlayers;
    private JMenuItem itemNewGame;
    private JMenuItem itemAbortGame;
    private JRadioButtonMenuItem[] itemPlayerCount;

    /**
     * Simple constructor
     */
    public MainMenuBar(MainController controller) {
        super();
        this.controller = controller; // TODO (HIGHEST) Fix menu!
        playerCount = 2;
        buildMenuGame();
        buildMenuOptions();
    }

    /**
     * Starts a new game with the controller.
     */
    public void newGame() {
        controller.requestNewGame(playerCount);
    }

    /**
     * Aborts the current game with the controller.
     */
    public void abortGame() {
        controller.requestAbortGame();
    }

    /**
     * Sets the amount of players for the next game.
     * @param players sets the amount of players.
     */
    public void setPlayerCount(int players) {
        playerCount = players;
    }

    private void buildMenuGame() {
        // build items:
        itemNewGame = new JMenuItem("Start new game");
        itemAbortGame = new JMenuItem("Abort current game");
        itemNewGame.addMouseListener(new NewGameMouseAdapter(this));
        itemAbortGame.addMouseListener(new AbortGameMouseAdapter(this));
        // build menu:
        menuGame = new JMenu("Game");
        menuGame.add(itemNewGame);
        menuGame.add(itemAbortGame);
        add(menuGame);
    }

    private void buildMenuOptions() {
        // build player menu
        menuPlayers = new JMenu("Players");
        itemPlayerCount = new JRadioButtonMenuItem[3];
        for (int i = 0; i < itemPlayerCount.length; i++) {
            itemPlayerCount[i] = new JRadioButtonMenuItem((i + 2) + " Players");
            itemPlayerCount[i].addMouseListener(new MenuPlayersMouseAdapter(this, (i + 2)));
            menuPlayers.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
        // build options menu:
        menuOptions = new JMenu("Options");
        menuOptions.add(menuPlayers);
        add(menuOptions);
    }
}
