package carcassonne.view.menubar;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import carcassonne.control.ControllerFacade;
import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.NotifiableUI;
import carcassonne.view.main.MainGUI;
import carcassonne.view.tertiary.GridSizeDialog;
import carcassonne.view.tertiary.TileDistributionGUI;
import carcassonne.view.util.GameMessage;

/**
 * The menu bar for the main GUI.
 * @author Timur Saglam
 */
public class MainMenuBar extends JMenuBar implements NotifiableUI {
    private static final long serialVersionUID = -599734693130415390L;
    private static final String ALLOW_FORTIFYING = "Allow direct meeple placement on own patterns";
    private static final String MEEPLE_RULE_SUFFIX = " Meeples";
    private static final String MEEPLE_RULES = "Meeple Placement Rules";
    private static final String DISTRIBUTION = "Change Tile Distribution";
    private static final String CLASSIC = " (Classic)";
    private static final String TILES_PER_PLAYER = " Tiles";
    private static final String HAND_SETTINGS = "Hand of Tiles";
    private static final String GRID_SIZE = "Change Grid Size";
    private static final String ABORT = "Abort Current Game";
    private static final String AMOUNT = "Amount of Players";
    private static final String GAME = "Game";
    private static final String LARGE_SPACE = "          ";
    private static final String NEW_ROUND = "Start New Round";
    private static final String OPTIONS = "Options";
    private static final String PLAYERS = " Players";
    private static final String PLAYER_SETTINGS = "Player Settings";
    private static final String SETTINGS_OF = "Settings of ";
    private static final String VIEW = "View";
    private static final String ABOUT = "About";
    private final ControllerFacade controller;
    private JMenuItem itemAbortRound;
    private JMenuItem itemNewRound;
    private JMenuItem[] itemSettings;
    private final MainGUI mainUI;
    private final Scoreboard scoreboard;
    private final GameSettings settings;
    private ZoomSlider slider;
    private final TileDistributionGUI tileDistributionUI;

    /**
     * Simple constructor creating the menu bar.
     * @param controller sets the connection to game the controller.
     * @param mainUI is the main GUI instance.
     */
    public MainMenuBar(ControllerFacade controller, MainGUI mainUI) {
        super();
        this.controller = controller;
        this.mainUI = mainUI;
        settings = controller.getSettings();
        settings.registerNotifiable(this);
        scoreboard = new Scoreboard(settings, mainUI);
        tileDistributionUI = new TileDistributionGUI(settings);
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
        menuOptions.add(buildPlayerMenu());
        menuOptions.add(buildHandMenu());
        menuOptions.add(buildMeepleRuleMenu());
        menuOptions.add(buildColorMenu());
        notifyChange(); // set colors
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

    private JMenu buildPlayerMenu() {
        JMenu menu = new JMenu(AMOUNT);
        JRadioButtonMenuItem[] itemPlayerCount = new JRadioButtonMenuItem[GameSettings.MAXIMAL_PLAYERS - 1];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemPlayerCount.length; i++) {
            int players = i + 2;
            itemPlayerCount[i] = new JRadioButtonMenuItem(players + PLAYERS);
            itemPlayerCount[i].addActionListener(event -> settings.setAmountOfPlayers(players));
            group.add(itemPlayerCount[i]);
            menu.add(itemPlayerCount[i]);
        }
        itemPlayerCount[0].setSelected(true);
        return menu;
    }

    private JMenu buildColorMenu() {
        itemSettings = new JMenuItem[GameSettings.MAXIMAL_PLAYERS];
        JMenu menu = new JMenu(PLAYER_SETTINGS);
        for (int i = 0; i < itemSettings.length; i++) {
            itemSettings[i] = new JMenuItem();
            itemSettings[i].addActionListener(scoreboard.getSettingsListener(i));
            menu.add(itemSettings[i]);
        }
        return menu;
    }

    private JMenu buildHandMenu() {
        JMenuItem[] itemTiles = new JRadioButtonMenuItem[GameSettings.MAXIMAL_TILES_ON_HAND];
        JMenu menu = new JMenu(HAND_SETTINGS);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < itemTiles.length; i++) {
            int numberOfTiles = i + 1;
            String itemText = numberOfTiles + TILES_PER_PLAYER;
            if (numberOfTiles == 1) {
                itemText += CLASSIC;
            }
            itemTiles[i] = new JRadioButtonMenuItem(itemText);
            itemTiles[i].addActionListener(event -> settings.setTilesPerPlayer(numberOfTiles));
            group.add(itemTiles[i]);
            menu.add(itemTiles[i]);
        }
        itemTiles[settings.getTilesPerPlayer() - 1].setSelected(true);
        return menu;
    }

    private JMenu buildMeepleRuleMenu() {
        JMenu menu = new JMenu(MEEPLE_RULES);
        for (TerrainType type : TerrainType.basicTerrain()) {
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(type.toReadableString() + MEEPLE_RULE_SUFFIX);
            item.setSelected(settings.getMeepleRule(type));
            item.addActionListener(event -> settings.toggleMeepleRule(type));
            menu.add(item);
        }
        menu.add(new JSeparator());
        JCheckBoxMenuItem fortifyingItem = new JCheckBoxMenuItem(ALLOW_FORTIFYING);
        fortifyingItem.setSelected(settings.isAllowingFortifying());
        fortifyingItem.addActionListener(event -> settings.setAllowFortifying(fortifyingItem.isSelected()));
        menu.add(fortifyingItem);
        return menu;
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
