package carcassonne.view.secondary;

import javax.swing.table.AbstractTableModel;

import carcassonne.model.Round;

public class GameStatisticsModel extends AbstractTableModel {

    private static final long serialVersionUID = -7138458001360243937L;
    private Round round;
    private String[] header = { "player", "castle points", "road points", "field points", "overall score" };

    public GameStatisticsModel(Round round) {
        this.round = round;
    }

    public String[] getHeader() {
        return header;
    }

    @Override
    public int getRowCount() {
        return round.getPlayerCount();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return "Player " + (rowIndex + 1);
        } else {
            return round.getPlayer(rowIndex).getAllScores()[columnIndex - 1];
        }
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

}
