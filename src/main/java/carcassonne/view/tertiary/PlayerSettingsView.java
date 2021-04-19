package carcassonne.view.tertiary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import carcassonne.model.tile.TileDistribution;
import carcassonne.settings.GameSettings;
import carcassonne.util.MinkowskiDistance;
import carcassonne.view.NotifiableView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.util.MouseClickListener;

/**
 * All-in-one settings UI for the player settings. This includes the player name, the player type and the number of
 * players.
 * @author Timur Saglam
 */
public class PlayerSettingsView extends JDialog implements NotifiableView {

    private static final long serialVersionUID = -4633728570151183720L;
    private static final Dimension SPACING_DIMENSION = new Dimension(0, 5);
    private static final int PADDING = 5;
    private static final int PLAYER_LABEL_WIDTH = 100;
    private static final String SPACE = " ";
    private static final String AESTHETIC = "AI Aesthetic:";
    private static final String AESTHETIC_TOOL_TIP = "Affects how AI players place tiles. The effect is most pronounced with only AI players and a bigger tile stack.";
    private static final String CUSTOMIZE = "Customize";
    private static final String AI_PLAYER = "AI player";
    private static final String PLAYERS = "Players: ";
    private static final String OK = "Ok";
    private static final String TITLE = "Player Settings";
    private final GameSettings settings;
    private final Scoreboard scoreboard;
    private final List<JLabel> playerLabels;

    /**
     * Creates the UI and shows it.
     * @param distribution is the {@link TileDistribution} to show in the UI.
     */
    public PlayerSettingsView(GameSettings settings, Scoreboard scoreboard) {
        this.settings = settings;
        this.scoreboard = scoreboard;
        playerLabels = new ArrayList<>();
        buildPanels();
        buildWindow();
        settings.registerNotifiable(this);
    }

    @Override
    public void notifyChange() {
        for (int player = 0; player < GameSettings.MAXIMAL_PLAYERS; player++) {
            playerLabels.get(player).setText(SPACE + settings.getPlayerName(player));
            playerLabels.get(player).setForeground(settings.getPlayerColor(player));
        }
    }

    private void buildPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(createPlayerNumberPanel());
        mainPanel.add(Box.createRigidArea(SPACING_DIMENSION));
        mainPanel.add(createPlayerPanel());
        mainPanel.add(Box.createRigidArea(SPACING_DIMENSION));
        mainPanel.add(createAIAestheticPanel());
        mainPanel.add(Box.createRigidArea(SPACING_DIMENSION));
        mainPanel.add(createOkButton());
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
        JRadioButton button = new JRadioButton(distanceMeasure.getDescription());
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
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return panel;
    }

    private JPanel createBasicPanel(String labelText) {
        JPanel panel = createBasicPanel();
        panel.add(new JLabel(labelText));
        return panel;
    }

    private JPanel createOkButton() {
        JButton closeButton = new JButton(OK);
        closeButton.addActionListener(it -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton); // Weirdly, the button needs to be in a panel or it will not be centered.
        return buttonPanel;
    }

    private void createPlayerNumberButton(int numberOfPlayers, JPanel panel, ButtonGroup group) {
        JRadioButton button = new JRadioButton(Integer.toString(numberOfPlayers));
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
        playerLabels.forEach(it -> it.setPreferredSize(new Dimension(PLAYER_LABEL_WIDTH, it.getHeight())));
        return playerPanel;
    }

    private JPanel createPlayerRow(int playerNumber) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(PADDING, PADDING));
        panel.setOpaque(false);
        JLabel label = new JLabel();
        playerLabels.add(label);
        panel.add(label, BorderLayout.LINE_START);
        JCheckBox checkBox = new JCheckBox(AI_PLAYER);
        checkBox.setSelected(settings.isPlayerComputerControlled(playerNumber));
        checkBox.addActionListener(event -> settings.setPlayerComputerControlled(checkBox.isSelected(), playerNumber));
        panel.add(checkBox, BorderLayout.CENTER);
        JButton configurationButton = new JButton(CUSTOMIZE);
        configurationButton.addActionListener(scoreboard.getSettingsListener(playerNumber));
        panel.add(configurationButton, BorderLayout.LINE_END);
        return panel;
    }
}
