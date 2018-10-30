package carcassonne.view.main.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import carcassonne.control.GameOptions;
import carcassonne.control.PaintShop;
import carcassonne.model.terrain.TerrainType;

/**
 * A mouse adapter for the selection of player colors. Listens to menu item clicks, event change and button pressing at
 * the same time.
 * @author Timur Saglam
 */
public class MenuColorsMouseAdapter extends MouseAdapter implements ChangeListener, ActionListener {
    private Map<TerrainType, JLabel> labelMap;
    private final GameOptions options;
    private final PaintShop paintShop;
    private final int player;
    private final JColorChooser colorChooser;

    /**
     * Simple constructor.
     * @param player is the player whose color gets chosen.
     */
    public MenuColorsMouseAdapter(int player) {
        super();
        this.player = player;
        options = GameOptions.getInstance();
        colorChooser = new JColorChooser();
        colorChooser.getSelectionModel().addChangeListener(this);
        colorChooser.setBorder(BorderFactory.createTitledBorder("Choose Meeple Color"));
        paintShop = new PaintShop();
        createPreview();

    }

    // gets called when the ok button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        options.setPlayerColor(colorChooser.getColor(), player);
    }

    // gets called when the menu entry is clicked.
    @Override
    public void mousePressed(MouseEvent e) {
        colorChooser.setColor(options.getPlayerColor(player));
        JColorChooser.createDialog(null, "Carcassonne", true, colorChooser, this, null).setVisible(true);
    }

    // gets called when the color is changed.
    @Override
    public void stateChanged(ChangeEvent e) {
        for (TerrainType terrain : TerrainType.basicTerrain()) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, colorChooser.getColor());
            labelMap.get(terrain).setIcon(icon); // Update the preview labels.
        }
    }

    /**
     * Creates the preview panel of the color chooser.
     */
    private void createPreview() {
        JPanel preview = new JPanel();
        labelMap = new HashMap<>();
        for (TerrainType terrain : TerrainType.basicTerrain()) {
            JLabel label = new JLabel();
            labelMap.put(terrain, label);
            preview.add(label);
        }
        colorChooser.setPreviewPanel(preview);
    }
}
