package carcassonne.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
        // panel back:
        panelBack = new JPanel();
        panelBack.setSize(options.resolutionWidth, options.resolutionHeightWindow);
        panelBack.setBackground(options.colorGUImain);
        panelBack.setLayout(new GridBagLayout());
        panelBack.add(new JButton()); // TODO remove
        // panel front:
        panelFront = new JPanel();
        // TODO fix size:
        panelFront.setSize(options.resolutionWidth - 100, options.resolutionHeightWindow - 100);
        panelFront.setBackground(new Color(0, 0, 100, 100));
        panelFront.setLayout(new GridBagLayout());
        panelFront.add(new JButton()); // TODO remove
        // layered pane:
        layeredPane = new JLayeredPane();
        layeredPane.add(panelBack, 0, 0);
        layeredPane.add(panelFront, 1, 0);
        // frame building:
        frame = new JFrame();
        menuBar = new MainMenuBar();
        frame.setJMenuBar(menuBar);
        frame.setPreferredSize(new Dimension(options.resolutionWidth, options.resolutionHeightWindow));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // LATER:
        buildPanelBack();
        buildPanelFront();
        buildLayeredPane();
        buildFrame();
    }

    public static void main(String[] args) {
        new LayeredGUI();
    }

    private void buildPanelBack() {
        // TODO (HIGHEST) use method.
    }

    private void buildPanelFront() {
        // TODO (HIGHEST) use method.
    }

    private void buildLayeredPane() {
        // TODO (HIGHEST) use method.
    }

    private void buildFrame() {
        // TODO (HIGHEST) use method.
    }

}