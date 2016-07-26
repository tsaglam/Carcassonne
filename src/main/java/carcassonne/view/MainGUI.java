package carcassonne.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import carcassonne.control.SystemProperties;
import carcassonne.model.Meeple;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * The main GUI class, extending a JPanel.
 * @author Timur
 */
public class MainGUI extends JPanel {

    private static final long serialVersionUID = -8750891542665009043L;
    private JFrame frame;
    private MainMenuBar menuBar;
    private TileLabel[][] labelGrid;
    private GridBagConstraints constraints;
    private int gridWidth;
    private int gridHeight;

    /**
     * Basic constructor.
     */
    public MainGUI() {
        super(new GridBagLayout());
        constraints = new GridBagConstraints();
        buildLabelGrid();
        buildFrame();
    }

    /**
     * Draws the tile on a specific position on the GUI.
     * @param tile is the tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void setTile(Tile tile, int x, int y) {
        if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
            labelGrid[x][y].setIcon(tile.getImage());
        } else {
            throw new IllegalArgumentException("Invalid label grid position (" + x + ", " + y + ")");
        }
    }

    /**
     * TODO comment paint meeple method.
     * @param meeple
     * @param x
     * @param y
     * @param rotation
     */
    public void paint(Meeple meeple, int x, int y, GridDirection rotation) {
        // TODO implement paint meeple method.
    }

    /*
     * Builds the frame.
     */
    private void buildFrame() {
        menuBar = new MainMenuBar();
        frame = new JFrame("Carcasonne");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        setBackground(new Color(190, 190, 190)); // grey
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setResizable(false);
        // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*
     * Creates the grid of labels.
     */
    private void buildLabelGrid() {
        SystemProperties p = new SystemProperties(); // TODO make this pretty
        gridWidth = p.getResolutionWidth() / 100;
        gridHeight = (p.getResolutionHeight() - p.getTaskBarHeight()) / 100;
        labelGrid = new TileLabel[gridWidth][gridHeight]; // build array of labels.
        Tile defaultTile = TileFactory.createTile(TileType.Null);
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new TileLabel(defaultTile.getImage(), x, y);
                labelGrid[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) { // TODO maybe move to TileLabel
                        TileLabel label = (TileLabel) e.getSource(); // TODO whatever a click does
                        System.out.println("Clicked label at (" + label.getPosX() + "|" + label.getPosY() + ")");
                    }
                });
                constraints.gridx = x;
                constraints.gridy = y;
                add(labelGrid[x][y], constraints); // add label with constraints
            }
        }
    }

    /**
     * Main method for testing.
     * @param args are the arguments.
     */
    public static void main(String[] args) { // TODO remove sometime
        MainGUI gui = new MainGUI();
        Tile tile = TileFactory.createTile(TileType.CastleWallCurveRight);
        for (int i = 0; i < 12; i++) {
            gui.setTile(tile, i, 0); // TODO fix rotation
            tile.rotate();
        }
    }

}
