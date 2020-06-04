package carcassonne.view.tertiary;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.settings.GameSettings;

/**
 * User interface that shows all tiles and how often they are used in a standard game (two players, chaos mode
 * disabled.)
 * @author Timur Saglam
 */
public class TileDistributionGUI extends JDialog {
    private static final String CLICK_TO_ROTATE = "Click to rotate the tile of type ";
    private static final String TITLE = "Standard Two-Player Game Tile Distribution";
    private static final long serialVersionUID = 1805511300999150753L;
    private static final String TIMES = "x ";
    private static final int BORDER_SIZE = 2;
    private static final int GRID_WIDTH = 11;
    private static final int GRID_HEIGHT = 3;
    private static final int TILE_SIZE = 90;
    private static final int PADDING = 5;
    private final Border border;

    /**
     * Creates the UI and shows it.
     */
    public TileDistributionGUI() {
        border = new LineBorder(Color.DARK_GRAY, BORDER_SIZE);
        buildPanel();
        buildWindow();
    }

    /*
     * Builds the panel of tiles for all tiles that appear in the game.
     */
    private void buildPanel() {
        JPanel tilePanel = new JPanel();
        tilePanel.setBackground(Color.GRAY);
        tilePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        for (TileType tileType : TileType.values()) {
            if (tileType.getAmount() > 0) {
                tilePanel.add(buildTileLabel(tileType), constraints);
                constraints.gridx++;
                if (constraints.gridx >= GRID_WIDTH) {
                    constraints.gridx = 0;
                    constraints.gridy++;
                }
            }
        }
        getContentPane().add(tilePanel);
    }

    /*
     * Shows and resizes the window.
     */
    private void buildWindow() {
        setTitle(TITLE);
        setVisible(true);
        pack();
        setSize(getWidth() + PADDING * GRID_WIDTH, getHeight() + PADDING * GRID_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /*
     * Creates a single label for a tile based on the given tyle type.
     */
    private JLabel buildTileLabel(TileType tileType) {
        Tile tile = new Tile(tileType);
        JLabel label = new JLabel(tile.getScaledIcon(TILE_SIZE));
        label.setBackground(GameSettings.GUI_COLOR);
        label.setOpaque(true);
        label.setBorder(border);
        label.setToolTipText(CLICK_TO_ROTATE + tileType.readableRepresentation());
        label.setText(tileType.getAmount() + TIMES);
        Font font = label.getFont();
        label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                tile.rotateRight();
                label.setIcon(tile.getScaledIcon(TILE_SIZE));
            }
        });
        return label;
    }
}
