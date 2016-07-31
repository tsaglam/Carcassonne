package carcassonne.view;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import carcassonne.control.MainController;

/**
 * A GUI for the placement of Meeples on the Tile that was placed previously.
 * @author Timur
 */
public class PlacementGUI extends SmallGUI {
    private static final long serialVersionUID = 1449264387665531286L;
    private JButton[][] button;

    public static void main(String[] args) {
        new PlacementGUI(null);
    }

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
    private void buildContent() { // TODO add real dynamic tile placement.
        String[][] text = { { "", "Top", "" }, { "Left", "Middle", "Right" }, { "", "Bottom", "" } };
        constraints.fill = GridBagConstraints.BOTH;
        constraints.ipadx = 0;
        constraints.ipady = 30;
        button = new JButton[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (Math.abs(x - 1) != Math.abs(y - 1) || x == 1) {
                    button[x][y] = new JButton(text[x][y]);
                    button[x][y].setToolTipText("Place Meeple on the " + text[x][y].toLowerCase() + " of the tile.");
                    constraints.gridx = x;
                    constraints.gridy = y;
                    add(button[x][y], constraints);
                }
            }
        }
    }
}