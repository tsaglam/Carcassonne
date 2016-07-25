package carcassonne.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
     * @param frameWidth is the width of the frame
     * @param frameHeight is the height of the frame
     */
    public MainGUI(int frameWidth, int frameHeight) {
        super(new GridBagLayout());
        constraints = new GridBagConstraints();
        buildLabelGrid(frameWidth, frameHeight);
        buildFrame(frameWidth, frameHeight);
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
    private void buildFrame(int frameWidth, int frameHeight) {
        menuBar = new MainMenuBar();
        frame = new JFrame("Carcasonne");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setBackground(new Color(165, 200, 245)); // light blue
        setBackground(new Color(190, 190, 190)); // grey
    }

    /*
     * Creates the grid of labels.
     */
    private void buildLabelGrid(int frameWidth, int frameHeight) {
        gridWidth = frameWidth / 100;
        gridHeight = frameHeight / 100;
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
        MainGUI gui = new MainGUI(1280, 768);
    }

}
