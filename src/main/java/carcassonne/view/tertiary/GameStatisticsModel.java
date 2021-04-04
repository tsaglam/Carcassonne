package carcassonne.view.tertiary;

import javax.swing.table.AbstractTableModel;

import carcassonne.model.Round;
import carcassonne.model.terrain.TerrainType;

/**
 * Model class for the game statistics view. Acts as an adapter that offers the data of the round to the game statistics
 * view.
 * @author Timur Saglam
 */
public class GameStatisticsModel extends AbstractTableModel {

    private static final long serialVersionUID = -7138458001360243937L; // generated UID
    private final Round round;
    private static final String[] HEADER = { "Player", "Castle", "Road", "Monastery", "Field", "SCORE" };

    /**
     * Creates the game statistics model with the current round.
     * @param round is the current round.
     */
    public GameStatisticsModel(Round round) {
        super();
        this.round = round;
    }

    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    @Override
    public String getColumnName(int column) {
        return HEADER[column];
    }

    @Override
    public int getRowCount() {
        return round.getPlayerCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return round.getPlayer(rowIndex).getName();
        } else if (columnIndex == 5) {
            return round.getPlayer(rowIndex).getScore();
        } else {
            return round.getPlayer(rowIndex).getTerrainScore(TerrainType.values()[columnIndex - 1]);
        }
    }
}
