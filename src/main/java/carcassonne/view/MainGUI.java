package carcassonne.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import carcassonne.model.tile.Tile;
import carcassonne.model.tile.TileFactory;
import carcassonne.model.tile.TileType;

/**
 * The main gui class, extending a JPanel.
 * @author Timur
 */
public class MainGUI extends JPanel {

    private static final long serialVersionUID = -8750891542665009043L;
    private JFrame frame;
    private MainMenuBar menuBar;
    private JLabel[][] labelGrid;
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
        buildFrame(frameWidth, frameHeight);
        buildLabelGrid(frameWidth, frameHeight);
    }

    /**
     * Draws the tile on a specific position on the GUI.
     * @param tile is the tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     */
    public void paint(Tile tile, int x, int y) {
        if (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight) {
            labelGrid[x][y].setIcon(tile.getImage());
        } else {
            throw new IllegalArgumentException("Invalid label grid position (" + x + ", " + y + ")");
        }
    }

    /*
     * Builds the frame.
     */
    private void buildFrame(int frameWidth, int frameHeight) {
        menuBar = new MainMenuBar();
        frame = new JFrame("Carcasonne by Timur S.");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(165, 200, 245)); // background color
    }

    /*
     * Creates the grid of labels.
     */
    private void buildLabelGrid(int frameWidth, int frameHeight) {
        gridWidth = frameWidth / 100;
        gridHeight = frameHeight / 100;
        System.out.println(gridWidth + " " + gridHeight);
        labelGrid = new JLabel[gridWidth][gridHeight]; // build array of labels.
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                labelGrid[x][y] = new JLabel(); // create empty label
                constraints.gridx = x; // set x coordinate.
                constraints.gridy = y; // set y coordinate.
                add(labelGrid[x][y], constraints); // add label with constraints
            }
        }
    }

    /**
     * Main method for testing.
     * @param args are the arguments.
     */
    public static void main(String[] args) { // TODO remove sometime
        int x = 1280;
        int y = 768;
        MainGUI gui = new MainGUI(x, y);
        Tile tile;
        int rndX;
        int rndY;
        for (int i = 0; i < 50000; i++) {
            for (TileType tileType : TileType.values()) {
                tile = TileFactory.createTile(tileType);
                rndX = (int) Math.round(Math.random() * ((x / 100) - 1));
                rndY = (int) Math.round(Math.random() * ((y / 100) - 1));
                gui.paint(tile, rndX, rndY);
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
