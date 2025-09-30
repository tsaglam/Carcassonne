package carcassonne.view.menubar;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import carcassonne.control.ControllerFacade;
import carcassonne.settings.GameSettings;
import carcassonne.view.main.MainView;
import carcassonne.view.tertiary.GridSizeDialog;
import carcassonne.view.tertiary.PlayerSettingsView;
import carcassonne.view.tertiary.TileDistributionView;
import carcassonne.view.util.GameMessage;

/**
 * The menu bar for the main view.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar {
    // ID:
    private static final long serialVersionUID = -599734693130415390L;

    // TEXT:
    private static final String DISTRIBUTION = "Change Tile Distribution";
    private static final String GRID_SIZE = "Change Grid Size";
    private static final String ABORT = "Abort Current Game";
    private static final String GAME = "Game";
    private static final String LARGE_SPACE = "          ";
    private static final String NEW_ROUND = "Start New Round";
    private static final String OPTIONS = "Options";
    private static final String PLAYER_SETTINGS = "Player Settings";
    private static final String VIEW = "View";
    private static final String ABOUT = "About";

    // STATE:
    private final ControllerFacade controller;
    private JMenuItem itemAbortRound;
    private JMenuItem itemNewRound;
    private final MainView mainView;
    private final Scoreboard scoreboard;
    private final GameSettings settings;
    private ZoomSlider slider;
    private final TileDistributionView tileDistributionUI;
    private final PlayerSettingsView playerView;

    /**
     * Simple constructor creating the menu bar.
     * @param controller sets the connection to game the controller.
     * @param mainView is the main view instance.
     */
    public MainMenuBar(ControllerFacade controller, MainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        settings = controller.getSettings();
        scoreboard = new Scoreboard(settings, mainView);
        tileDistributionUI = new TileDistributionView(settings);
        playerView = new PlayerSettingsView(settings, scoreboard);
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

    // adds labels of the scoreboard to the menu bar.
    private void add(Scoreboard scoreboard) {
        for (JLabel label : scoreboard.getLabels()) {
            add(label);
        }
    }

    private void buildGameMenu() {
        itemNewRound = new JMenuItem(NEW_ROUND);
        itemAbortRound = new JMenuItem(ABORT);
        JMenuItem itemAbout = new JMenuItem(ABOUT);
        itemAbortRound.setEnabled(false);
        itemAbout.addActionListener(event -> GameMessage.showGameInfo());
        itemNewRound.addActionListener(new NewRoundListener(controller, itemNewRound, itemAbortRound));
        itemAbortRound.addActionListener(new AbortRoundListener(controller, itemNewRound, itemAbortRound));
        JMenu menuGame = new JMenu(GAME);
        menuGame.add(itemNewRound);
        menuGame.add(itemAbortRound);
        menuGame.addSeparator();
        menuGame.add(itemAbout);
        add(menuGame);
    }

    private void buildOptionsMenu() {
        JMenu menuOptions = new JMenu(OPTIONS);
        JMenuItem itemPlayerSettings = new JMenuItem(PLAYER_SETTINGS);
        itemPlayerSettings.addActionListener(it -> playerView.setVisible(true));
        menuOptions.add(itemPlayerSettings);
        menuOptions.addSeparator();
        JMenuItem itemGridSize = new JMenuItem(GRID_SIZE);
        GridSizeDialog dialog = new GridSizeDialog(settings);
        itemGridSize.addActionListener(event -> dialog.showDialog());
        menuOptions.add(itemGridSize);
        JMenuItem itemDistribution = new JMenuItem(DISTRIBUTION);
        itemDistribution.addActionListener(event -> tileDistributionUI.setVisible(true));
        menuOptions.add(itemDistribution);
        add(menuOptions);
    }

    private void buildViewMenu() {
        slider = new ZoomSlider(mainView);
        JMenu menuView = new JMenu(VIEW);
        menuView.add(slider.getZoomIn());
        menuView.add(slider);
        menuView.add(slider.getZoomOut());
        add(menuView);
    }
}
