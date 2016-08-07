package carcassonne.view;

import java.awt.GridBagConstraints;

import javax.swing.ImageIcon;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;
import carcassonne.view.meepleButton.MeepleButton;

/**
 * A GUI for the placement of Meeples on the Tile that was placed previously.
 * @author Timur
 */
public class PlacementGUI extends SecondaryGUI {
    private static final long serialVersionUID = 1449264387665531286L;

    public static void main(String[] args) { // TODO (LOWEST) remove main method sometime.
        PlacementGUI g = new PlacementGUI(null);
        g.setTile(TileFactory.create(TileType.CastleEdgeRoad));
    }

    private MeepleButton[][] button;

    /**
     * Simple constructor which uses the constructor of the <code>SmallGUI</code>.
     * @param controller is the game controller.
     */
    public PlacementGUI(MainController controller) {
        super(controller, "");
        buildContent();
        finishFrame();
    }

    // build the GUI content
    private void buildContent() {
        String[][] toolTipText = { { "top left", "top", "top right" }, { "left", "middle", "right" }, { "bottom left", "bottom", "bottom right" } };
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        if (!options.operatingSystemName.startsWith("Mac")) {
            constraints.ipady = 15;
            constraints.weightx = 0.5;
            constraints.weightx = 0.5;
        }
        button = new MeepleButton[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                button[x][y] = new MeepleButton(controller, x, y);
                button[x][y].setToolTipText("Place Meeple on the " + toolTipText[x][y] + " of the tile.");
                button[x][y].setBorder(null);
                constraints.gridx = x;
                constraints.gridy = y;
                add(button[x][y], constraints);
            }
        }
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update
     * the GUI content according to the tiles properties.
     */
    @Override
    protected void update() {
        GridDirection[][] directions = GridDirection.values2D();
        TerrainType terrain;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                terrain = tile.getTerrain(directions[x][y]);
                if (terrain == null || terrain == TerrainType.CASTLE_AND_ROAD) {
                    button[x][y].setEnabled(false);
                    terrain = TerrainType.OTHER;
                } else {
                    button[x][y].setEnabled(true);
                }
                button[x][y].setIcon(new ImageIcon(options.buildImagePath(terrain)));
            }
        }
        finishFrame();
    }
}