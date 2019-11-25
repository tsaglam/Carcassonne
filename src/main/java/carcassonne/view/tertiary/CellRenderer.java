package carcassonne.view.tertiary;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import carcassonne.model.Round;

/**
 * Custom renderer that customizes the cells of the game statistics table.
 * @author Timur Saglam
 */
public class CellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1280736206678504709L; // generated UID
    private final Round round;

    /**
     * Creates the renderer.
     * @param round is the round which is depicted in the game statistics table.
     */
    CellRenderer(Round round) {
        this.round = round;
        setHorizontalAlignment(SwingConstants.CENTER); // Center text in cells.
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
     * boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == GameStatisticsGUI.SCORE_COLUMN) {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
        }
        component.setForeground(round.getPlayer(row).getColor().textColor());
        component.setBackground(column == 0 ? GameStatisticsGUI.HEADER_COLOR : GameStatisticsGUI.BODY_COLOR);
        return component;
    }
}