package carcassonne.view.secondary;

import javax.swing.table.AbstractTableModel;

import carcassonne.control.GameOptions;
import carcassonne.model.Round;
import carcassonne.model.terrain.TerrainType;

/**
 * Model class for the game statistics GUI.
 * @author Timur Saglam
 */
public class GameStatisticsModel extends AbstractTableModel {

    private static final long serialVersionUID = -7138458001360243937L;
    private final Round round;
    private final String[] header = { "player", "castle", "road", "monastery", "field", "overall score" };
    private final GameOptions options;

    /**
     * Creates the game statistics model with the current round.
     * @param round is the current round.
     */
    public GameStatisticsModel(Round round) {
        super();
        this.round = round;
        options = GameOptions.getInstance();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    @Override
    public int getRowCount() {
        return round.getPlayerCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return options.playerNames[rowIndex];
        } else if (columnIndex == 5) {
            return round.getPlayer(rowIndex).getScore();
        } else {
            return round.getPlayer(rowIndex).getTerrainScore(TerrainType.values()[columnIndex - 1]);
        }
    }
}
