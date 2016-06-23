package carcassonne.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main gui class, extending a JPanel.
 * @author Timur
 */
public class MainGUI extends JPanel {

    private static final long serialVersionUID = -8750891542665009043L;
    JFrame mainFrame;
    MainMenuBar menuBar;

    /**
     * Basic constructor.
     */
    public MainGUI() {
        super();
        menuBar = new MainMenuBar();

        mainFrame = new JFrame("CARC by Timur S.");
        mainFrame.getContentPane().add(this);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.setSize(1280, 768); // TODO make gui size variable
        mainFrame.setLocationRelativeTo(null);
        setBackground(new Color(165, 200, 245)); // background color

    }

    /**
     * Main method for testing.
     * @param args are the arguments.
     */
    public static void main(String[] args) {
        new MainGUI();
    }

}
