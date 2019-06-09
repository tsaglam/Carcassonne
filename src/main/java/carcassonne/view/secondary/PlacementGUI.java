package carcassonne.view.secondary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import carcassonne.control.GameProperties;
import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.view.main.MainGUI;
import carcassonne.view.secondary.placementbutton.PlacementButton;

/**
 * A GUI for the placement of Meeples on the Tile that was placed previously.
 * @author Timur Saglam
 */
public class PlacementGUI extends SecondaryGUI {
    private static final long serialVersionUID = 1449264387665531286L;
    private PlacementButton[][] button;
    private JButton buttonSkip;
    private Color defaultButtonColor;

    /**
     * Simple constructor which uses the constructor of the <code>SmallGUI</code>.
     * @param controller is the game controller.
     * @param ui is the main GUI.
     */
    public PlacementGUI(MainController controller, MainGUI ui) {
        super(controller, ui);
        constraints.fill = GridBagConstraints.BOTH;
        buildButtonSkip();
        buildButtonGrid();
        finishFrame();
    }

    // build button grid
    private void buildButtonGrid() {
        constraints.gridwidth = 1;
        button = new PlacementButton[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                button[x][y] = new PlacementButton(controller, x, y);
                button[x][y].setToolTipText("Place Meeple on the " + GridDirection.values2D()[x][y].toReadableString() + " of the tile.");
                constraints.gridx = x;
                constraints.gridy = y + 1;
                add(button[x][y], constraints);
            }
        }
    }

    private void buildButtonSkip() {
        buttonSkip = new JButton(new ImageIcon("src/main/ressources/icons/skip.png"));
        buttonSkip.setToolTipText("Don't place meeple and preserve for later use");
        defaultButtonColor = buttonSkip.getBackground();
        constraints.gridwidth = 3;
        add(buttonSkip, constraints);
        buttonSkip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.requestSkip();
            }
        });
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update the GUI content according
     * to the tiles properties.
     */
    @Override
    protected void update() {
        GridDirection[][] directions = GridDirection.values2D();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                TerrainType terrain = tile.getTerrain(directions[x][y]);
                if (tile.hasMeepleSpot(directions[x][y])) {
                    button[x][y].setIcon(new ImageIcon(GameProperties.getMeeplePath(terrain, false)));
                } else {
                    button[x][y].setIcon(new ImageIcon(GameProperties.getMeeplePath(TerrainType.OTHER, false)));
                }
                if (controller.requestPlacementStatus(directions[x][y]) && tile.hasMeepleSpot(directions[x][y])) {
                    button[x][y].setEnabled(true);
                    button[x][y].setBackground(defaultButtonColor);
                } else {
                    button[x][y].setEnabled(false);
                    button[x][y].setBackground(currentPlayer.getColor().lightColor());
                }
            }
        }
        finishFrame();
    }
}