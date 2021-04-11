package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import carcassonne.model.tile.TileDistribution;
import carcassonne.settings.GameSettings;
import carcassonne.view.NotifiableView;
import carcassonne.view.menubar.Scoreboard;
import carcassonne.view.util.MouseClickListener;

public class PlayerSettingsView extends JDialog implements NotifiableView { // TODO (HIGH) [AI/UI] Prototypical UI, needs to be refined.
    private static final long serialVersionUID = -4633728570151183720L;
    private static final Dimension SPACING_DIMENSION = new Dimension(0, 5);
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
        buildPanel();
        buildWindow();
        settings.registerNotifiable(this);
    }

    @Override
    public void notifyChange() {
        for (int player = 0; player < GameSettings.MAXIMAL_PLAYERS; player++) {
            playerLabels.get(player).setText(settings.getPlayerName(player));
            playerLabels.get(player).setForeground(settings.getPlayerColor(player));
        }
    }

    private void buildPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(createPlayerNumberPanel());
        mainPanel.add(Box.createRigidArea(SPACING_DIMENSION));
        mainPanel.add(createPlayerPanel());
        mainPanel.add(Box.createRigidArea(SPACING_DIMENSION));
        JButton closeButton = new JButton(OK);
        closeButton.addActionListener(it -> dispose());
        mainPanel.add(closeButton);
        getContentPane().add(mainPanel);
    }

    private void buildWindow() {
        setTitle(TITLE);
        setResizable(false);
        setAlwaysOnTop(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void createPlayerNumberButton(int numberOfPlayers, JPanel panel, ButtonGroup group) {
        JRadioButton button = new JRadioButton(Integer.toString(numberOfPlayers));
        button.setSelected(settings.getAmountOfPlayers() == numberOfPlayers);
        button.addMouseListener((MouseClickListener) event -> settings.setAmountOfPlayers(numberOfPlayers));
        if (settings.getAmountOfPlayers() == numberOfPlayers) {
            button.setSelected(true);
        }
        group.add(button);
        panel.add(button);
    }

    private JPanel createPlayerNumberPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        panel.add(new JLabel(PLAYERS));
        ButtonGroup group = new ButtonGroup();
        JLabel sizeLabel = new JLabel();
        for (int numberOfPlayers = 2; numberOfPlayers <= GameSettings.MAXIMAL_PLAYERS; numberOfPlayers++) {
            createPlayerNumberButton(numberOfPlayers, panel, group);
        }
        panel.add(sizeLabel);
        return panel;
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.LIGHT_GRAY);
        playerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < GameSettings.MAXIMAL_PLAYERS; i++) {
            playerPanel.add(createPlayerRow(i));
        }
        notifyChange(); // set label text and color
        return playerPanel;
    }

    private JPanel createPlayerRow(int playerNumber) { // TODO (HIGH) [UI] make player name and color update
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        JLabel label = new JLabel();
        playerLabels.add(label);
        panel.add(label);
        JCheckBox checkBox = new JCheckBox(AI_PLAYER);
        checkBox.setSelected(settings.isPlayerComputerControlled(playerNumber));
        checkBox.addActionListener(event -> settings.setPlayerComputerControlled(checkBox.isSelected(), playerNumber));
        panel.add(checkBox);
        JButton configurationButton = new JButton(CUSTOMIZE);
        configurationButton.addActionListener(scoreboard.getSettingsListener(playerNumber));
        panel.add(configurationButton);
        return panel;
    }
}
