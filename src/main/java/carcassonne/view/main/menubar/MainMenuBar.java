package carcassonne.view.main.menubar;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import carcassonne.control.MainController;

/**
 * The menu bar for the main GUI.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar {

    private static final long serialVersionUID = -599734693130415390L;
    private final MainController controller;
    private int playerCount;
    private JMenu menuGame;
    private JMenu menuOptions;
    private JMenu menuPlayers;
    private JMenuItem itemNewGame;
    private JMenuItem itemAbortGame;
    private JRadioButtonMenuItem[] itemPlayerCount;

    /**
     * Simple constructor creating the menu bar.
     * @param scoreboard sets the scoreboard of the menu bar.
     * @param controller sets the connection to game the controller.
     */
    public MainMenuBar(Scoreboard scoreboard, MainController controller) {
        super();
        this.controller = controller;
        playerCount = 2;
        buildMenuGame();
        buildMenuOptions();
        add(new JLabel("          "));
        add(scoreboard);
    }

    /**
     * Aborts the current game with the controller.
     */
    public void abortGame() {
        controller.requestAbortGame();
    }

    /**
     * Starts a new game with the controller.
     */
    public void newGame() {
        controller.requestNewGame(playerCount);
    }

    /**
     * Sets the amount of players for the next game.
     * @param players sets the amount of players.
     */
    public void setPlayerCount(int players) {
        playerCount = players;
    }

    // adds labels of the scoreboard to the menu bar.
    private void add(Scoreboard scoreboard) {
        for (JLabel label : scoreboard.getLabels()) {
            add(label);
        }
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
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemPlayerCount.length; i++) {
            itemPlayerCount[i] = new JRadioButtonMenuItem((i + 2) + " Players");
            itemPlayerCount[i].addMouseListener(new MenuPlayersMouseAdapter(this, (i + 2)));
            group.add(itemPlayerCount[i]);
            menuPlayers.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
        // build options menu:
        menuOptions = new JMenu("Options");
        menuOptions.add(menuPlayers);
        add(menuOptions);
    }
}
