package carcassonne.view.main.menubar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.view.Notifiable;

/**
 * The menu bar for the main GUI.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar implements Notifiable {

    private static final long serialVersionUID = -599734693130415390L;
    private final MainController controller;
    private int playerCount;
    private JMenu menuGame;
    private JMenu menuOptions;
    private JMenu menuPlayers;
    private JMenu menuNames;
    private JMenu menuColors;
    private JMenuItem itemNewGame;
    private JMenuItem itemAbortGame;
    private final GameOptions options;
    private JMenuItem[] itemColors;
    private JMenuItem[] itemNames;

    /**
     * Simple constructor creating the menu bar.
     * @param scoreboard sets the scoreboard of the menu bar.
     * @param controller sets the connection to game the controller.
     */
    public MainMenuBar(Scoreboard scoreboard, MainController controller) {
        super();
        this.controller = controller;
        options = GameOptions.getInstance();
        options.register(this);
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
        buildMenuPlayers();
        buildMenuNames();
        buildMenuColors();
        notifyChange(); // set colors
        // build options menu:
        menuOptions = new JMenu("Options");
        menuOptions.add(menuPlayers);
        menuOptions.add(menuNames);
        menuOptions.add(menuColors);
        JCheckBoxMenuItem itemChaosMode = new JCheckBoxMenuItem("Enable Chaos Mode");
        itemChaosMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                options.setChaosMode(!itemChaosMode.isSelected());
            }
        });
        menuOptions.add(itemChaosMode);
        add(menuOptions);
    }

    private void buildMenuPlayers() {
        menuPlayers = new JMenu("Players");
        JRadioButtonMenuItem[] itemPlayerCount = new JRadioButtonMenuItem[options.maximalPlayers - 1];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemPlayerCount.length; i++) {
            itemPlayerCount[i] = new JRadioButtonMenuItem((i + 2) + " Players");
            itemPlayerCount[i].addMouseListener(new MenuPlayersMouseAdapter(this, (i + 2)));
            group.add(itemPlayerCount[i]);
            menuPlayers.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
    }

    private void buildMenuNames() {
        itemNames = new JMenuItem[options.maximalPlayers];
        menuNames = new JMenu("Set Names");
        for (int i = 0; i < itemNames.length; i++) {
            itemNames[i] = new JMenuItem();
            itemNames[i].addMouseListener(new MenuNamesMouseAdapter(i));
            menuNames.add(itemNames[i]);
        }
    }

    private void buildMenuColors() { // TODO (MEDIUM) reduce duplication
        itemColors = new JMenuItem[options.maximalPlayers];
        menuColors = new JMenu("Set Colors");
        for (int i = 0; i < itemColors.length; i++) {
            itemColors[i] = new JMenuItem();
            itemColors[i].addMouseListener(new MenuColorsMouseAdapter(i));
            menuColors.add(itemColors[i]);
        }
    }

    @Override
    public void notifyChange() {
        for (int i = 0; i < itemColors.length; i++) {
            Color color = options.getPlayerColorText(i);
            itemColors[i].setForeground(color);
            itemColors[i].setText("Color of " + options.getPlayerName(i));
            itemNames[i].setForeground(color);
            itemNames[i].setText(options.getPlayerName(i));
        }
    }

}
