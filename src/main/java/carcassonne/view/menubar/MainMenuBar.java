package carcassonne.view.menubar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
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
import carcassonne.view.tertiary.TileDistributionGUI;

/**
 * The menu bar for the main GUI.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar implements Notifiable {
    private static final long serialVersionUID = -599734693130415390L;
    private static final String DISTRIBUTION = "Change Tile Distribution";
    private static final String CLASSIC = " (Classic)";
    private static final String TILES_PER_PLAYER = " Tiles on the Hand";
    private static final String HAND_SETTINGS = "Hand of Tiles";
    private static final String GRID_SIZE = "Change Grid Size";
    private static final String ABORT = "Abort Current Game";
    private static final String AMOUNT = "Amount of Players";
    private static final String GAME = "Game";
    private static final String LARGE_SPACE = "          ";
    private static final String NEW_ROUND = "Start New Round";
    private static final String OPTIONS = "Options";
    private static final String PLAYERS = " Players";
    private static final String COLOR_SETTINGS = "Color Settings";
    private static final String SETTINGS_OF = "Settings of ";
    private static final String VIEW = "View";
    private final MainController controller;
    private JMenuItem itemAbortRound;
    private JMenuItem itemNewRound;
    private JMenuItem[] itemSettings;
    private final MainGUI mainUI;
    private JMenu menuPlayers;
    private JMenu menuColor;
    private final Scoreboard scoreboard;
    private final GameSettings settings;
    private JMenu menuHand;
    private ZoomSlider slider;

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

    /**
     * Grants access to the zoom slider of the menu bar.
     * @return the slider.
     */
    public ZoomSlider getZoomSlider() {
        return slider;
    }

    @Override
    public final void notifyChange() {
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
        JMenu menuGame = new JMenu(GAME);
        menuGame.add(itemNewRound);
        menuGame.add(itemAbortRound);
        add(menuGame);
    }

    private void buildOptionsMenu() {
        buildPlayerMenu();
        buildHandMenu();
        buildColorMenu();
        notifyChange(); // set colors
        JMenu menuOptions = new JMenu(OPTIONS);
        menuOptions.add(menuPlayers);
        menuOptions.add(menuHand);
        menuOptions.add(menuColor);
        menuOptions.addSeparator();
        JMenuItem itemGridSize = new JMenuItem(GRID_SIZE);
        GridSizeDialog dialog = new GridSizeDialog(settings);
        itemGridSize.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                dialog.showDialog();
            }
        });
        menuOptions.add(itemGridSize);
        JMenuItem itemDistribution = new JMenuItem(DISTRIBUTION);
        itemDistribution.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                new TileDistributionGUI(settings.getTileDistribution());
            }
        });
        menuOptions.add(itemDistribution);
        add(menuOptions);
    }

    private void buildPlayerMenu() {
        menuPlayers = new JMenu(AMOUNT);
        JRadioButtonMenuItem[] itemPlayerCount = new JRadioButtonMenuItem[GameSettings.MAXIMAL_PLAYERS - 1];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemPlayerCount.length; i++) {
            itemPlayerCount[i] = new JRadioButtonMenuItem(i + 2 + PLAYERS);
            itemPlayerCount[i].addMouseListener(new MenuPlayersMouseAdapter(settings, i + 2));
            group.add(itemPlayerCount[i]);
            menuPlayers.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
    }

    private void buildColorMenu() { // TODO (MEDIUM) reduce duplication
        itemSettings = new JMenuItem[GameSettings.MAXIMAL_PLAYERS];
        menuColor = new JMenu(COLOR_SETTINGS);
        for (int i = 0; i < itemSettings.length; i++) {
            itemSettings[i] = new JMenuItem();
            itemSettings[i].addMouseListener(scoreboard.getSettingsMouseListener(i));
            menuColor.add(itemSettings[i]);
        }
    }

    private void buildHandMenu() {
        JMenuItem[] itemTiles = new JRadioButtonMenuItem[GameSettings.MAXIMAL_TILES_ON_HAND];
        menuHand = new JMenu(HAND_SETTINGS);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemTiles.length; i++) {
            int numberOfTiles = i + 1;
            String itemText = numberOfTiles + TILES_PER_PLAYER;
            if (numberOfTiles == 1) {
                itemText += CLASSIC;
            }
            itemTiles[i] = new JRadioButtonMenuItem(itemText);
            itemTiles[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    settings.setTilesPerPlayer(numberOfTiles);
                }
            });
            group.add(itemTiles[i]);
            menuHand.add(itemTiles[i]);
        }
        itemTiles[settings.getTilesPerPlayer() - 1].setSelected(true);
    }

    private void buildViewMenu() {
        slider = new ZoomSlider(mainUI);
        JMenu menuView = new JMenu(VIEW);
        menuView.add(slider.getZoomIn());
        menuView.add(slider);
        menuView.add(slider.getZoomOut());
        add(menuView);
    }
}
