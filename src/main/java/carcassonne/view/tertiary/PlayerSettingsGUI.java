package carcassonne.view.tertiary;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;
import carcassonne.view.PaintShop;
import carcassonne.view.main.MainGUI;

/**
 * Custom UI for the play settings. Allows changing the name and the color of a player.
 * @author Timur Saglam
 */
public class PlayerSettingsGUI extends JDialog implements ChangeListener, ActionListener {
    private static final int MEEPLE_PREVIEW_SIZE = 30;
    private static final long serialVersionUID = 1293883978626527260L; // generated serial UID
    private static final String CHANGE_COLOR = "Choose Meeple Color:";
    private static final String EMPTY_NAME = "The player name cannot be empty!";
    private static final String ACCEPT_CHANGES = "Accept Changes";
    private static final String CHANGE_NAME = "Choose Player Name:";
    private JColorChooser colorChooser;
    private Map<TerrainType, JLabel> labelMap;
    private final GameSettings settings;
    private final int playerNumber;
    private JTextField nameTextField;

    /**
     * Creates a new player settings UI for a specific player.
     * @param playerNumber is the number of the player.
     * @param settings are the game settings which are modified based on the user interaction with this UI.
     * @param mainUI is the main user interface.
     */
    public PlayerSettingsGUI(int playerNumber, GameSettings settings, MainGUI mainUI) {
        super(mainUI);
        this.playerNumber = playerNumber;
        this.settings = settings;
        setLayout(new BorderLayout());
        createNamePanel();
        createCloseButton();
    }

    /**
     * Updates the name and color of the correlating player and then makes the UI visible.
     */
    public void updateAndShow() {
        if (colorChooser == null) { // create JColorChoser on demand for optimal performance
            createColorChooser();
            pack(); // adapt UI size to content
            setLocationRelativeTo(null); // place UI in the center of the screen
        }
        colorChooser.setColor(settings.getPlayerColor(playerNumber));
        nameTextField.setText(settings.getPlayerName(playerNumber));
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        for (TerrainType terrain : TerrainType.basicTerrain()) {
            ImageIcon icon = PaintShop.getColoredMeeple(terrain, colorChooser.getColor(), MEEPLE_PREVIEW_SIZE);
            labelMap.get(terrain).setIcon(icon); // Update the preview labels.
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (nameTextField.getText().isEmpty()) {
            GameMessage.showMessage(EMPTY_NAME);
        } else {
            settings.setPlayerName(nameTextField.getText(), playerNumber);
            settings.setPlayerColor(colorChooser.getColor(), playerNumber);
            setVisible(false);
        }
    }

    private void createNamePanel() {
        nameTextField = new JTextField();
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.add(nameTextField);
        namePanel.setBorder(BorderFactory.createTitledBorder(CHANGE_NAME));
        add(namePanel, BorderLayout.NORTH);
    }

    private void createColorChooser() {
        colorChooser = new JColorChooser();
        colorChooser.setBorder(BorderFactory.createTitledBorder(CHANGE_COLOR));
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setPreviewPanel(createMeeplePreview());
        add(colorChooser, BorderLayout.CENTER);
    }

    private JPanel createMeeplePreview() {
        JPanel previewPanel = new JPanel();
        labelMap = new HashMap<>();
        for (TerrainType terrain : TerrainType.basicTerrain()) {
            JLabel label = new JLabel();
            labelMap.put(terrain, label);
            previewPanel.add(label);
        }
        return previewPanel;
    }

    private void createCloseButton() {
        JButton closeButton = new JButton(ACCEPT_CHANGES);
        closeButton.addActionListener(this);
        add(closeButton, BorderLayout.SOUTH);
    }
}
