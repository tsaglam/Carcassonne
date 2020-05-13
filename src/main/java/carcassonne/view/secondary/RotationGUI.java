package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileType;
import carcassonne.view.main.MainGUI;

/**
 * GUI class for the tile orientation. It lets the user look at the tile to place and rotate it both right and left.
 * @author Timur Saglam
 */
public class RotationGUI extends SecondaryGUI {
    private static final int TILE_PREVIEW_SIZE = 100;
    private static final long serialVersionUID = -5179683977081970564L;
    private JButton buttonRotateLeft;
    private JButton buttonRotateRight;
    private JButton buttonSkip;
    private JLabel tileLabel;

    /**
     * Simple constructor which uses the constructor of the <code>SmallGUI</code>.
     * @param controller is the game controller.
     * @param ui is the main GUI.
     */
    public RotationGUI(MainController controller, MainGUI ui) {
        super(controller, ui);
        buildContent();
        finishFrame();
    }

    /**
     * If the UI is active, rotates the tile to the right.
     */
    public void rotateRight() {
        if (isVisible()) {
            tile.rotateRight();
            updateGUI();
        }
    }

    /**
     * If the UI is active, rotates the tile to the left.
     */
    public void rotateLeft() {
        if (isVisible()) {
            tile.rotateLeft();
            updateGUI();
        }
    }

    private void addMouseAdapters() {
        buttonSkip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.requestSkip();
            }
        });
        buttonRotateLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rotateLeft();
            }
        });
        buttonRotateRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rotateRight();
            }
        });
    }

    // build the GUI content
    private void buildContent() {
        ImageIcon defaultImage = new Tile(TileType.Null).getScaledIcon(TILE_PREVIEW_SIZE);
        tileLabel = new JLabel(defaultImage); // Important for the UI size
        // create buttons:
        buttonSkip = new JButton(new ImageIcon("src/main/ressources/icons/skip.png"));
        buttonRotateLeft = new JButton(new ImageIcon("src/main/ressources/icons/left.png"));
        buttonRotateRight = new JButton(new ImageIcon("src/main/ressources/icons/right.png"));
        // set tool tips:
        buttonSkip.setToolTipText("Don't place tile and skip turn");
        buttonRotateLeft.setToolTipText("Rotate left");
        buttonRotateRight.setToolTipText("Rotate right");
        // set listeners:
        addMouseAdapters();
        // set constraints:
        constraints.fill = GridBagConstraints.BOTH;
        // add buttons:
        panel.add(buttonRotateLeft, constraints);
        panel.add(buttonSkip, constraints);
        panel.add(buttonRotateRight, constraints);
        // change constraints and add label:
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.ipady = 0;
        constraints.gridwidth = 3;
        panel.add(tileLabel, constraints);
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update the GUI content according
     * to the tiles properties.
     */
    @Override
    protected void updateGUI() {
        tileLabel.setIcon(tile.getScaledIcon(TILE_PREVIEW_SIZE));
    }
}