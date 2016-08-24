package carcassonne.view.secondary;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import carcassonne.control.MainController;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.tile.TileType;

/**
 * @author Timur Saglam
 */
public class GameStatisticsGUI {

    protected MainController controller;
    private JFrame frame;
    private JButton buttonClose;
    private JTable table;

    public static void main(String[] args) {
        new GameStatisticsGUI(null, new Round(4, new Grid(5, 5, TileType.CastleCenter)));
    }

    /**
     * TODO (HIGHEST) comment class.
     * @param controller
     */
    public GameStatisticsGUI(MainController controller, Round round) {
        this.controller = controller;
        table = new JTable(new GameStatisticsModel(round));
        buildButtonClose();
        buildFrame();
    }

    private void buildButtonClose() {
        buttonClose = new JButton("Close");
        buttonClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.requestSkip()) {
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
    }

    private void buildFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO (HIGHEST) remove line
        // frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); TODO (HIGHEST) enable line
        frame.setLayout(new BorderLayout());
        frame.add(table.getTableHeader(), BorderLayout.PAGE_START);
        frame.add(table, BorderLayout.CENTER);
        frame.add(buttonClose, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
