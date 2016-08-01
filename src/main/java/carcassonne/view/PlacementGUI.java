package carcassonne.view;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import carcassonne.control.MainController;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * A GUI for the placement of Meeples on the Tile that was placed previously.
 * @author Timur
 */
public class PlacementGUI extends SecondaryGUI {
    private static final long serialVersionUID = 1449264387665531286L;

    public static void main(String[] args) { // TODO (LOWEST) remove main method sometime.
        PlacementGUI g = new PlacementGUI(null);
        g.setTile(TileFactory.createTile(TileType.CastleEdgeRoad));
    }

    private JButton[][] button;

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
        constraints.ipady = 40;
        button = new JButton[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                button[x][y] = new JButton();
                button[x][y].setToolTipText("Place Meeple on the " + toolTipText[x][y] + " of the tile.");
                button[x][y].setFont(options.buttonFont);
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
                terrain = tile.getTerrainAt(directions[x][y]);
                System.out.println(directions[x][y]+" --> "+terrain);
                if (terrain == null) {
                    button[x][y].setEnabled(false);
                } else {
                    button[x][y].setEnabled(true);
                    button[x][y].setText(terrain.toString().substring(0, 4));
                }
            }
        }
        finishFrame();
    }
}