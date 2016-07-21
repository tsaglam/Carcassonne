package carcassonne.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import carcassonne.model.tile.Tile;

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

    /**
     * Basic constructor.
     */
    public MainGUI(int frameWidth, int frameHeight) {
        super(new GridBagLayout());
        int gridWidth = frameWidth % 100;
        int gridHeight = frameHeight % 100;
        constraints = new GridBagConstraints();
        menuBar = new MainMenuBar();
        buildFrame(frameWidth, frameHeight);
        setBackground(new Color(165, 200, 245)); // background color
        buildLabelGrid(gridWidth, gridHeight);
    }

    /**
     * Draws the tile on the GUI.
     * @param tile is the tile.
     */
    public void addTile(Tile tile) {
        // TODO implement me.
    }

    /*
     * Builds the frame.
     */
    private void buildFrame(int frameWidth, int frameHeight) {
        frame = new JFrame("Carcasonne by Timur S.");
        frame.getContentPane().add(this);
        frame.setJMenuBar(menuBar);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight); // TODO make gui size variable
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
     * Creates the grid of labels.
     */
    private void buildLabelGrid(int gridWidth, int gridHeight) {
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
    public static void main(String[] args) {
        // TODO remove sometime
        new MainGUI(1280, 768);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
