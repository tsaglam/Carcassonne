package carcassonne.view.menubar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import carcassonne.model.terrain.TerrainType;
import carcassonne.settings.GameSettings;
import carcassonne.view.GameMessage;
import carcassonne.view.PaintShop;

public class PlayerColorChooser extends JFrame implements ChangeListener, ActionListener {
    private static final long serialVersionUID = 1293883978626527260L; // generated serial UID
    private static final String CHANGE_COLOR = "Choose Meeple Color:";
    private static final String EMPTY_NAME = "The player name cannot be empty!";
    private static final String ACCEPT_CHANGES = "Accept Changes";
    private static final String CHANGE_NAME = "Choose Player Name:";
    private JColorChooser colorChooser;
    private Map<TerrainType, JLabel> labelMap;
    private final PaintShop paintShop;
    private final GameSettings settings;
    private final int playerNumber;
    private JTextField nameTextField;

    public PlayerColorChooser(int playerNumber, GameSettings settings) {
        this.playerNumber = playerNumber;
        this.settings = settings;
        paintShop = new PaintShop();
        setLayout(new BorderLayout());
        createNamePanel();
        createColorChooser();
        createCloseButton();
        pack();
        setLocationRelativeTo(null); // place in the center of the screen
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        for (TerrainType terrain : TerrainType.basicTerrain()) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, colorChooser.getColor());
            labelMap.get(terrain).setIcon(icon); // Update the preview labels.
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (nameTextField.getText().isEmpty()) {
            GameMessage.showMessage(EMPTY_NAME);
        } else {
            settings.setPlayerName(nameTextField.getText(), playerNumber);
            settings.setPlayerColor(colorChooser.getColor(), playerNumber);
            setVisible(false);
        }
    }

    private final void createNamePanel() {
        nameTextField = new JTextField(settings.getPlayerName(playerNumber));
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.add(nameTextField);
        namePanel.setBorder(BorderFactory.createTitledBorder(CHANGE_NAME));
        add(namePanel, BorderLayout.NORTH);
    }

    private final void createColorChooser() {
        colorChooser = new JColorChooser();
        colorChooser.setBorder(BorderFactory.createTitledBorder(CHANGE_COLOR));
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setPreviewPanel(createMeeplePreview());
        colorChooser.setColor(settings.getPlayerColor(playerNumber));
        add(colorChooser, BorderLayout.CENTER);
    }

    private final JPanel createMeeplePreview() {
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
