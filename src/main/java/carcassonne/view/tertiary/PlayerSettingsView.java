package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.TileDistribution;
import carcassonne.settings.GameSettings;
import carcassonne.util.MinkowskiDistance;
import carcassonne.view.NotifiableView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.util.FixedSizeLabel;
import carcassonne.view.util.MouseClickListener;
import carcassonne.l18n.view.util.ConvStringL18n;

/**
 * All-in-one settings UI for the player settings. This includes the player name, the player type and the number of
 * players.
 * @author Timur Saglam
 */
public class PlayerSettingsView extends JDialog implements NotifiableView {

    // GENERAL:
    private static final long serialVersionUID = -4633728570151183720L;

    // UI LAYOUT:
    private static final int PADDING = 5;
    private static final int PLAYER_LABEL_WIDTH = 75;
    private static final int PLAYER_LABEL_POSITION = 1;
    private static final int BUTTON_VERTICAL_STRUT = 200;
    private static final int CLOSE_BUTTON_WIDTH = 125;

    // TEXT:
    private String AESTHETIC = "AI Aesthetic:";
    private String AESTHETIC_TOOL_TIP = "Affects how AI players place tiles. The effect is most pronounced with only AI players and a bigger tile stack.";
    private String HAND_TOOL_TIP = "Number of tiles on the player's hand";
    private String HAND = "Hand of Tiles:";
    private String MEEPLE_RULES = "Meeple Placement on:";
    private String MEEPLE_RULES_TOOL_TIP = "Allow or forbid placing meeples on certain terrain";
    private String FORTIFYING = " Allow Fortifying Own Patterns:";
    private String FORTIFYING_TOOL_TIP = "Allow or forbid directly placing meeples on own patterns";
    private String ENCLAVE = "Allow Enclaves of Free Spots";
    private String ENCLAVE_TOOL_TIP = "Allow or forbid enclosing free spots in the grid, leading to these spots forming enclaves.";
    private String SCORE_SPLITTING = " Split Score on Shared Patterns:";
    private String SCORE_SPLITTING_TOOL_TIP = "Split score among dominant players of pattern instead of warding the full score.";
    private String MULTI_TILE = " Tiles";
    private String CLASSIC = " Tile (Classic)";
    private String CUSTOMIZE = "Customize";
    private String AI_PLAYER = "AI player";
    private String PLAYERS = "Players:";
    private String CLOSE = "Close";
    private String TITLE = "Player Settings";
    private String COLON = ":";
    private String PLAYER = "Player ";
    private String SPACE = " ";

    // STATE:
    private final GameSettings settings;
    private final Scoreboard scoreboard;
    private final List<JPanel> playerPanels;
    private final List<JLabel> playerLabels;

    /**
     * Creates the UI and shows it.
     * @param distribution is the {@link TileDistribution} to show in the UI.
     */
    public PlayerSettingsView(GameSettings settings, Scoreboard scoreboard) {
		initResource();
        this.settings = settings;
        this.scoreboard = scoreboard;
        playerPanels = new ArrayList<>();
        playerLabels = new ArrayList<>();
        buildPanels();
        buildWindow();
        settings.registerNotifiable(this);
    }

    @Override
    public void notifyChange() {
        for (int player = 0; player < GameSettings.MAXIMAL_PLAYERS; player++) {
            JLabel label = new FixedSizeLabel(SPACE + settings.getPlayerName(player), PLAYER_LABEL_WIDTH);
            label.setForeground(settings.getPlayerColor(player));
            if (playerLabels.size() == GameSettings.MAXIMAL_PLAYERS) {
                playerPanels.get(player).remove(playerLabels.remove(player));
            }
            playerPanels.get(player).add(label, PLAYER_LABEL_POSITION);
            playerLabels.add(player, label);
        }
        validate();
    }

    private void addWithBox(JPanel panel, JComponent component) {
        panel.add(component);
        panel.add(Box.createRigidArea(new Dimension(0, PADDING)));
    }

    private void buildPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(layout);
        addWithBox(mainPanel, createPlayerNumberPanel());
        addWithBox(mainPanel, createHandOfTilePanel());
        addWithBox(mainPanel, createPlayerPanel());
        addWithBox(mainPanel, createAIAestheticPanel());
        addWithBox(mainPanel, createPlacementRulePanel());
        addWithBox(mainPanel, createScoreSplittingPanel());
        addWithBox(mainPanel, createDualPanel());
        mainPanel.add(createCloseButton());
        getContentPane().add(mainPanel);
    }

    private void buildWindow() {
        setTitle(TITLE);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        pack();
        setLocationRelativeTo(null);
    }

    private void createAIAestheticButton(MinkowskiDistance distanceMeasure, JPanel panel, ButtonGroup group) {
		String key = "MD_" + distanceMeasure.getDescription().toUpperCase().replaceAll(" ","_") +".text";
		ConvStringL18n csL18n = new ConvStringL18n("RBUtil", key);
        JRadioButton button = new JRadioButton(csL18n.get(key));
        button.setSelected(settings.getDistanceMeasure() == distanceMeasure);
        button.addMouseListener((MouseClickListener) event -> settings.setDistanceMeasure(distanceMeasure));
        group.add(button);
        panel.add(button);
    }

    private JPanel createAIAestheticPanel() {
        JPanel panel = createBasicPanel(AESTHETIC);
        panel.setToolTipText(AESTHETIC_TOOL_TIP);
        ButtonGroup group = new ButtonGroup();
        for (MinkowskiDistance distanceMeasure : MinkowskiDistance.values()) {
            createAIAestheticButton(distanceMeasure, panel, group);
        }
        return panel;
    }

    private JPanel createBasicPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return panel;
    }

    private JPanel createBasicPanel(String labelText) {
        JPanel panel = createBasicPanel();
        panel.add(embolden(new JLabel(labelText + SPACE)));
        return panel;
    }

    private JPanel createCloseButton() {
        JButton closeButton = new JButton(CLOSE);
        closeButton.addActionListener(it -> dispose());
        closeButton.setPreferredSize(new Dimension(CLOSE_BUTTON_WIDTH, closeButton.getPreferredSize().height));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton); // Weirdly, the button needs to be in a panel or it will not be centered.
        return buttonPanel;
    }

    private JPanel createDualPanel() {
        JPanel dualPanel = new JPanel();
        dualPanel.setOpaque(false);
        dualPanel.setLayout(new BoxLayout(dualPanel, BoxLayout.X_AXIS));
        dualPanel.add(createFortificationPanel());
        dualPanel.add(Box.createRigidArea(new Dimension(PADDING, 0)));
        dualPanel.add(createEnclavePanel());
        return dualPanel;
    }

    private JPanel createFortificationPanel() {
        JPanel panel = createBasicPanel(FORTIFYING);
        panel.setToolTipText(FORTIFYING_TOOL_TIP);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(settings.isAllowingFortifying());
        checkBox.addActionListener(event -> settings.setAllowFortifying(checkBox.isSelected()));
        panel.add(checkBox);
        return panel;
    }

    private JPanel createEnclavePanel() {
        JPanel panel = createBasicPanel(ENCLAVE);
        panel.setToolTipText(ENCLAVE_TOOL_TIP);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(settings.isAllowingEnclaves());
        checkBox.addActionListener(event -> settings.setAllowEnclaves(checkBox.isSelected()));
        panel.add(checkBox);
        return panel;
    }

    private JPanel createScoreSplittingPanel() {
        JPanel panel = createBasicPanel(SCORE_SPLITTING);
        panel.setToolTipText(SCORE_SPLITTING_TOOL_TIP);
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(settings.getSplitPatternScore());
        checkBox.addActionListener(event -> settings.setSplitPatternScore(checkBox.isSelected()));
        panel.add(checkBox);
        return panel;
    }

    private void createHandOfTileButton(int numberOfTiles, JPanel panel, ButtonGroup group) {
        String description = numberOfTiles + (numberOfTiles == 1 ? CLASSIC : MULTI_TILE);
        JRadioButton button = new JRadioButton(description);
        button.setSelected(settings.getTilesPerPlayer() == numberOfTiles);
        button.addMouseListener((MouseClickListener) event -> settings.setTilesPerPlayer(numberOfTiles));
        group.add(button);
        panel.add(button);
    }

    private JPanel createHandOfTilePanel() {
        JPanel panel = createBasicPanel(HAND);
        panel.setToolTipText(HAND_TOOL_TIP);
        ButtonGroup group = new ButtonGroup();
        for (int tiles = 1; tiles <= GameSettings.MAXIMAL_TILES_ON_HAND; tiles++) {
            createHandOfTileButton(tiles, panel, group);
        }
        return panel;
    }

    private void createPlacementRuleButton(TerrainType type, JPanel panel) {
		// add l18n
		String key = "TT_" + type.toReadableString().toUpperCase() + ".text";
		ConvStringL18n csL18n = new ConvStringL18n("RBModel", key);
        JCheckBox button = new JCheckBox(csL18n.get(key));
		//
        button.setSelected(settings.getMeepleRule(type));
        button.addMouseListener((MouseClickListener) event -> settings.toggleMeepleRule(type));
        panel.add(button);
    }

    private JPanel createPlacementRulePanel() {
        JPanel panel = createBasicPanel(MEEPLE_RULES);
        panel.setToolTipText(MEEPLE_RULES_TOOL_TIP);
        for (TerrainType type : TerrainType.basicTerrain()) {
            createPlacementRuleButton(type, panel);
        }
        return panel;
    }

    private void createPlayerNumberButton(int numberOfPlayers, JPanel panel, ButtonGroup group) {
        JRadioButton button = new JRadioButton(Integer.toString(numberOfPlayers) + SPACE + PLAYERS);
        button.setSelected(settings.getNumberOfPlayers() == numberOfPlayers);
        button.addMouseListener((MouseClickListener) event -> settings.setNumberOfPlayers(numberOfPlayers));
        group.add(button);
        panel.add(button);
    }

    private JPanel createPlayerNumberPanel() {
        JPanel panel = createBasicPanel(PLAYERS);
        ButtonGroup group = new ButtonGroup();
        for (int numberOfPlayers = 2; numberOfPlayers <= GameSettings.MAXIMAL_PLAYERS; numberOfPlayers++) {
            createPlayerNumberButton(numberOfPlayers, panel, group);
        }
        return panel;
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = createBasicPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < GameSettings.MAXIMAL_PLAYERS; i++) {
            playerPanel.add(createPlayerRow(i));
        }
        notifyChange(); // set label text and color
        return playerPanel;
    }

    private JPanel createPlayerRow(int playerNumber) {
        JPanel panel = new JPanel();
        playerPanels.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.add(embolden(new JLabel(PLAYER + (playerNumber + 1) + COLON)));
        JCheckBox checkBox = new JCheckBox(AI_PLAYER);
        checkBox.setSelected(settings.isPlayerComputerControlled(playerNumber));
        checkBox.addActionListener(event -> settings.setPlayerComputerControlled(checkBox.isSelected(), playerNumber));
        panel.add(checkBox);
        panel.add(Box.createHorizontalStrut(BUTTON_VERTICAL_STRUT));
        JButton configurationButton = new JButton(CUSTOMIZE);
        configurationButton.addActionListener(scoreboard.getSettingsListener(playerNumber));
        panel.add(configurationButton);
        return panel;
    }

    private JLabel embolden(JLabel label) {
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        return label;
    }

	private void initResource() {
		LoadTextPlayerSettingsView properties = new LoadTextPlayerSettingsView();

		AESTHETIC = properties.get("AESTHETIC");
		AESTHETIC_TOOL_TIP = properties.get("AESTHETIC_TOOL_TIP");
		HAND = properties.get("HAND");
		HAND_TOOL_TIP = properties.get("HAND_TOOL_TIP");
		MEEPLE_RULES = properties.get("MEEPLE_RULES");
		MEEPLE_RULES_TOOL_TIP = properties.get("MEEPLE_RULES_TOOL_TIP");
		FORTIFYING = properties.get("FORTIFYING");
		FORTIFYING_TOOL_TIP = properties.get("FORTIFYING_TOOL_TIP");
		ENCLAVE = properties.get("ENCLAVE");
		ENCLAVE_TOOL_TIP = properties.get("ENCLAVE_TOOL_TIP");
		SCORE_SPLITTING = properties.get("SCORE_SPLITTING");
		SCORE_SPLITTING_TOOL_TIP = properties.get("SCORE_SPLITTING_TOOL_TIP");
		MULTI_TILE = properties.get("MULTI_TILE");
		CLASSIC = properties.get("CLASSIC");
		CUSTOMIZE = properties.get("CUSTOMIZE");
		AI_PLAYER = properties.get("AI_PLAYER");
		PLAYERS = properties.get("PLAYERS");
		CLOSE = properties.get("CLOSE");
		TITLE = properties.get("TITLE");
		COLON = properties.get("COLON");
		PLAYER = properties.get("PLAYER");
		SPACE = properties.get("SPACE");
	}
}
