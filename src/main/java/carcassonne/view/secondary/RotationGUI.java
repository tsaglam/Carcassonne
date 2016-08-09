package carcassonne.view.secondary;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UnsupportedLookAndFeelException;

import carcassonne.control.MainController;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * GUI class for the tile rotation. It lets the user look at the tile to place and rotate it both
 * right and left.
 * @author Timur
 */
public class RotationGUI extends SecondaryGUI {
    private static final long serialVersionUID = -5179683977081970564L;

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); // Simulate
        // Windows
        RotationGUI g = new RotationGUI(null); // TODO (LOWEST) remove main method sometime.
        g.setTile(TileFactory.create(TileType.CastleEdgeRoad));
    }

    private JButton buttonSkip;
    private JLabel tileLabel;
    private JButton buttonRotateLeft;
    private JButton buttonRotateRight;

    /**
     * Simple constructor which uses the constructor of the <code>SmallGUI</code>.
     * @param controller is the game controller.
     */
    public RotationGUI(MainController controller) {
        super(controller, "");
        buildContent();
        finishFrame();
    }

    /**
     * Getter for the tile.
     * @return the tile.
     */
    public Tile getTile() {
        return tile;
    }

    // build the GUI content
    private void buildContent() {
        tileLabel = new JLabel(TileFactory.create(TileType.Null).getImage());
        // create buttons:
        buttonSkip = new JButton(new ImageIcon("src/main/ressources/icons/skip.png"));
        buttonRotateLeft = new JButton(new ImageIcon("src/main/ressources/icons/left.png"));
        buttonRotateRight = new JButton(new ImageIcon("src/main/ressources/icons/right.png"));
        // TODO (HIGH) add GUI compatibility for windows
        if (!options.operatingSystemName.startsWith("Mac")) {
            System.out.println("NOT MAC OS");
            buttonSkip.setBorder(null);
            buttonRotateLeft.setBorder(null);
            buttonRotateRight.setBorder(null);
            constraints.ipady = 10;
            constraints.weightx = 0.5;
            constraints.weightx = 0.5;
        }
        // set tool tips:
        buttonSkip.setToolTipText("Don't place tile and skip turn");
        buttonRotateLeft.setToolTipText("Rotate left");
        buttonRotateRight.setToolTipText("Rotate right");
        // set listeners:
        addMouseAdapters();
        // set constraints:
        constraints.fill = GridBagConstraints.BOTH;
        // add buttons:
        add(buttonRotateLeft, constraints);
        add(buttonSkip, constraints);
        add(buttonRotateRight, constraints);
        // change constraints and add label:
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.ipady = 0;
        constraints.gridwidth = 3;
        add(tileLabel, constraints);
    }

    // simple mouser adapters TODO (LOW) make own classes
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
                tile.rotateLeft();
                update();
            }
        });
        buttonRotateRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tile.rotateRight();
                update();
            }
        });
    }

    /**
     * Primitive operation for the template method <code>setTile()</code>. Uses the tile to update
     * the GUI content according to the tiles properties.
     */
    @Override
    protected void update() {
        tileLabel.setIcon(tile.getImage());
    }

}