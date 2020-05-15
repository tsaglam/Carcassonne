package carcassonne.view.menubar;

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

import carcassonne.control.MainController;
import carcassonne.settings.GameSettings;
import carcassonne.settings.Notifiable;
import carcassonne.view.main.MainGUI;
import carcassonne.view.tertiary.GridSizeDialog;

/**
 * The menu bar for the main GUI.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar implements Notifiable {

    private static final String GRID_SIZE = "Change Grid Size";
    private static final String ABORT = "Abort Current Game";
    private static final String AMOUNT = "Amount of Players";
    private static final String CHAOS_MODE = "Enable Chaos Mode";
    private static final String GAME = "Game";
    private static final String LARGE_SPACE = "          ";
    private static final String NEW_ROUND = "Start New Round";
    private static final String OPTIONS = "Options";
    private static final String PLAYERS = " Players";
    private static final long serialVersionUID = -599734693130415390L;
    private static final String SETTINGS = "Player Settings";
    private static final String SETTINGS_OF = "Settings of ";
    private static final String VIEW = "View";
    private final MainController controller;
    private JMenuItem itemAbortRound;
    private JMenuItem itemNewRound;
    private JMenuItem[] itemSettings;
    private final MainGUI mainUI;
    private JMenu menuGame;
    private JMenu menuOptions;
    private JMenu menuPlayers;
    private JMenu menuSettings;
    private JMenu menuView;
    private final Scoreboard scoreboard;
    private final GameSettings settings;

    /**
     * Simple constructor creating the menu bar.
     * @param controller sets the connection to game the controller.
     * @param mainUI is the main GUI instance.
     */
    public MainMenuBar(MainController controller, MainGUI mainUI) {
        super();
        this.controller = controller;
        this.mainUI = mainUI;
        settings = controller.getSettings();
        settings.registerNotifiable(this);
        scoreboard = new Scoreboard(settings, mainUI);
        buildGameMenu();
        buildOptionsMenu();
        buildViewMenu();
        add(new JLabel(LARGE_SPACE));
        add(scoreboard);
    }

    /**
     * Enables the start button and disables the abort button.
     */
    public void enableStart() {
        itemNewRound.setEnabled(true);
        itemAbortRound.setEnabled(false);
    }

    /**
     * Grants access to the scoreboard of the menu bar.
     * @return the scoreboard.
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void notifyChange() {
        for (int i = 0; i < itemSettings.length; i++) {
            Color color = settings.getPlayerColor(i).textColor();
            String name = settings.getPlayerName(i);
            itemSettings[i].setForeground(color);
            itemSettings[i].setText(SETTINGS_OF + name);
        }
    }

    // adds labels of the scoreboard to the menu bar.
    private void add(Scoreboard scoreboard) {
        for (JLabel label : scoreboard.getLabels()) {
            add(label);
        }
    }

    private void buildGameMenu() {
        itemNewRound = new JMenuItem(NEW_ROUND);
        itemAbortRound = new JMenuItem(ABORT);
        itemAbortRound.setEnabled(false);
        itemNewRound.addMouseListener(new NewRoundMouseAdapter(controller, itemNewRound, itemAbortRound));
        itemAbortRound.addMouseListener(new AbortRoundMouseAdapter(controller, itemNewRound, itemAbortRound));
        // build menu:
        menuGame = new JMenu(GAME);
        menuGame.add(itemNewRound);
        menuGame.add(itemAbortRound);
        add(menuGame);
    }

    private void buildOptionsMenu() {
        buildPlayerMenu();
        buildSettingsMenu();
        notifyChange(); // set colors
        menuOptions = new JMenu(OPTIONS);
        menuOptions.add(menuPlayers);
        menuOptions.add(menuSettings);
        menuOptions.addSeparator();
        JCheckBoxMenuItem itemChaosMode = new JCheckBoxMenuItem(CHAOS_MODE);
        itemChaosMode.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                settings.setChaosMode(!itemChaosMode.isSelected());
            }
        });
        menuOptions.add(itemChaosMode);
        JMenuItem itemGridSize = new JMenuItem(GRID_SIZE);
        GridSizeDialog dialog = new GridSizeDialog(settings);
        itemGridSize.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                dialog.showDialog();
            }
        });
        menuOptions.add(itemGridSize);
        add(menuOptions);
    }

    private void buildPlayerMenu() {
        menuPlayers = new JMenu(AMOUNT);
        JRadioButtonMenuItem[] itemPlayerCount = new JRadioButtonMenuItem[GameSettings.MAXIMAL_PLAYERS - 1];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemPlayerCount.length; i++) {
            itemPlayerCount[i] = new JRadioButtonMenuItem((i + 2) + PLAYERS);
            itemPlayerCount[i].addMouseListener(new MenuPlayersMouseAdapter(settings, (i + 2)));
            group.add(itemPlayerCount[i]);
            menuPlayers.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
    }

    private void buildSettingsMenu() { // TODO (MEDIUM) reduce duplication
        itemSettings = new JMenuItem[GameSettings.MAXIMAL_PLAYERS];
        menuSettings = new JMenu(SETTINGS);
        for (int i = 0; i < itemSettings.length; i++) {
            itemSettings[i] = new JMenuItem();
            itemSettings[i].addMouseListener(scoreboard.getSettingsMouseListener(i));
            menuSettings.add(itemSettings[i]);
        }
    }

    private void buildViewMenu() {
        ZoomSlider slider = new ZoomSlider(mainUI);
        menuView = new JMenu(VIEW);
        menuView.add(slider.getZoomIn());
        menuView.add(slider);
        menuView.add(slider.getZoomOut());
        add(menuView);
    }
}
