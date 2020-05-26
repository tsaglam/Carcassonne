package carcassonne.view.secondary;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;
import carcassonne.settings.GameSettings;
import carcassonne.util.ImageLoadingUtil;
import carcassonne.view.main.MainGUI;

/**
 * A GUI for the placement of Meeples on the Tile that was placed previously.
 * @author Timur Saglam
 */
public class PlacementGUI extends SecondaryGUI {
    private static final long serialVersionUID = 1449264387665531286L;
    private PlacementButton[][] button;
    private JButton buttonSkip;
    private Color defaultButtonColor;
    private Tile tile;

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
        pack();
    }
    
    /**
     * Sets the tile of the GUI, updates the GUI and then makes it visible. Should be called to show the GUI. The method
     * implements the template method pattern using the method <code>update()</code>.
     * @param tile sets the tile.
     * @param currentPlayer sets the color scheme according to the player.
     */
    public void setTile(Tile tile, Player currentPlayer) {
        if (tile == null) {
            throw new IllegalArgumentException("Tried to set the tile of the " + getClass().getSimpleName() + " to null.");
        }
        this.tile = tile;
        setPlayerAndUpdateGUI(currentPlayer);
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
                dialogPanel.add(button[x][y], constraints);
            }
        }
    }

    private void buildButtonSkip() {
        buttonSkip = new JButton(ImageLoadingUtil.SKIP.createImageIcon());
        buttonSkip.setToolTipText("Don't place meeple and preserve for later use");
        defaultButtonColor = buttonSkip.getBackground();
        constraints.gridwidth = 3;
        dialogPanel.add(buttonSkip, constraints);
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
    protected void updateGUI() {
        GridDirection[][] directions = GridDirection.values2D();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                TerrainType terrain = tile.getTerrain(directions[x][y]);
                if (tile.hasMeepleSpot(directions[x][y])) {
                    button[x][y].setIcon(ImageLoadingUtil.createImageIcon(GameSettings.getMeeplePath(terrain, false)));
                } else {
                    button[x][y].setIcon(ImageLoadingUtil.createImageIcon(GameSettings.getMeeplePath(TerrainType.OTHER, false)));
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
    }
}