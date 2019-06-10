package carcassonne.view.secondary;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import carcassonne.control.MainController;
import carcassonne.model.Round;

/**
 * A class for the game statistics GUI.
 * @author Timur Saglam
 */
public class GameStatisticsGUI {

    protected MainController controller;
    private JFrame frame;
    private JButton buttonClose;
    private JTable table;

    /**
     * Creates the GUI and extracts the data from the current round.
     * @param controller is the game controller.
     * @param round is the current round.
     */
    public GameStatisticsGUI(MainController controller, Round round) {
        this.controller = controller;
        buildTable(round);

        buildButtonClose();
        buildFrame();
    }

    private void buildTable(Round round) {
        table = new JTable(new GameStatisticsModel(round));
        // Header:
        DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
        defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getTableHeader().setDefaultRenderer(defaultRenderer);
        // Columns:
        TableColumnModel model = table.getColumnModel();
        CustomCellRenderer renderer = new CustomCellRenderer(round);
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setCellRenderer(renderer);
        }
    }

    /**
     * Hides and disposes the GUI.
     */
    public void closeGUI() {
        frame.setVisible(false);
        frame.dispose();
    }

    private void buildButtonClose() {
        buttonClose = new JButton("Close");
        buttonClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.requestSkip();
            }
        });
    }

    private void buildFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(table.getTableHeader(), BorderLayout.PAGE_START);
        frame.add(table, BorderLayout.CENTER);
        frame.add(buttonClose, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    /**
     * Custom renderer that sets the colors for the player names.
     * @author Timur Saglam
     */
    private class CustomCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1280736206678504709L;

        private final Round round;

        public CustomCellRenderer(Round round) {
            this.round = round;
            setHorizontalAlignment(SwingConstants.CENTER); // Centers the text in cells.
        }

        /**
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
         * boolean, boolean, int, int)
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) { // if selected the color white is more readable
                component.setForeground(round.getPlayer(row).getColor().textColor());
            }
            return component;
        }
    }
}
