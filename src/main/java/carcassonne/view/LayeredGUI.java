package carcassonne.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import carcassonne.control.GameOptions;

public class LayeredGUI {
    private JFrame frame;
    private JLayeredPane layeredPane;
    private JPanel panelBack;
    private JPanel panelFront;
    private MainMenuBar menuBar;
    private GameOptions options;
    
    public LayeredGUI() {
        options = GameOptions.getInstance();
        frame = new JFrame();
        layeredPane = new JLayeredPane();
        panelBack = new JPanel();
        panelFront = new JPanel();
        frame.setPreferredSize(new Dimension(options.resolutionWidth, options.resolutionHeight));
        frame.setLayout(new BorderLayout());
        
        menuBar = new MainMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        
        frame.add(layeredPane, BorderLayout.CENTER);   
        
        layeredPane.setSize(options.resolutionWidth, options.resolutionHeight);
        panelBack.setSize(options.resolutionWidth, options.resolutionHeight);
        panelBack.setOpaque(true);
        panelBack.setLayout(new GridBagLayout());
        panelBack.add(new JButton()); // TODO remove
        
        panelFront.setSize(options.resolutionWidth, options.resolutionHeight);
        panelFront.setOpaque(true);
        panelFront.setLayout(new GridBagLayout());
        panelFront.add(new JButton()); // TODO remove
        layeredPane.add(panelBack, 0, 0);
        layeredPane.add(panelFront, 1, 0);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LayeredGUI();
    }

}