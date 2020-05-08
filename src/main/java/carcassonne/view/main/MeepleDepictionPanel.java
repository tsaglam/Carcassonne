package carcassonne.view.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.tile.Tile;

/**
 * {@link JPanel} encapsulates the the nine {@link MeepleDepiction}s of a specific {@link Tile}.
 * @author Timur Saglam
 */
public class MeepleDepictionPanel extends JPanel implements Iterable<MeepleDepiction> {

    private static final int GRID_INDEX_OFFSET = 1;
    private static final long serialVersionUID = -1475325065701922699L;
    private MainController controller;
    private HashMap<GridDirection, MeepleDepiction> labels;
    private Dimension size;

    /**
     * Creates the meeple panel.
     * @param scalingFactor is the initial scaling factor.
     * @param controller is the responsible {@link MainController}.
     * @param frame is the responsible {@link JFrame}.
     */
    public MeepleDepictionPanel(int scalingFactor, MainController controller, JFrame frame) {
        this.controller = controller;
        labels = new HashMap<GridDirection, MeepleDepiction>(GridDirection.values().length);
        setOpaque(false);
        setLayout(new GridBagLayout());
        size = new Dimension(scalingFactor, scalingFactor);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1; // evenly distributes meeple grid
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        for (GridDirection direction : GridDirection.values()) {
            MeepleDepiction meepleLabel = new MeepleDepiction(scalingFactor, controller, direction, frame);
            constraints.gridx = direction.getX() + GRID_INDEX_OFFSET;
            constraints.gridy = direction.getY() + GRID_INDEX_OFFSET;
            labels.put(direction, meepleLabel);
            add(meepleLabel.getLabel(), constraints);

        }
    }

    /**
     * Returns the {@link MeepleDepiction} for a specific position on the correlating tile.
     * @param direction is the position on the tile.
     * @return the {@link MeepleDepiction}.
     */
    public MeepleDepiction getMeepleLabel(GridDirection direction) {
        return labels.get(direction);
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return size;
    }

    @Override
    public Iterator<MeepleDepiction> iterator() {
        return labels.values().iterator();
    }

    /**
     * Enables the meeple preview on one all meeples of a specific {@link Tile}.
     * @param tile is the specific {@link Tile}.
     * @param currentPlayer determines the color of the preview.
     */
    public void setMeeplePreview(Tile tile, Player currentPlayer) {
        for (GridDirection direction : GridDirection.values()) {
            if (tile.hasMeepleSpot(direction) && controller.requestPlacementStatus(direction)) {
                labels.get(direction).setPreview(tile.getTerrain(direction), currentPlayer);
            }
        }
    }

    /**
     * Updates the size of the panel with a scaling factor.
     * @param scalingFactor is the scaling factor in pixels.
     */
    public void setSize(int scalingFactor) {
        this.size = new Dimension(scalingFactor, scalingFactor);
        forEach(it -> it.setMeepleSize(scalingFactor));
    }
}
