package carcassonne.view.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * {@link JPanel} encapsulates the the nine {@link MeepleDepiction}s of a specific {@link Tile}.
 * @author Timur Saglam
 */
public class MeepleDepictionPanel extends JPanel {

    private static final int GRID_INDEX_OFFSET = 1;
    private static final long serialVersionUID = -1475325065701922699L;
    private final MainController controller;
    private final Map<GridDirection, MeepleDepiction> labels;
    private Dimension size;

    /**
     * Creates the meeple panel.
     * @param scalingFactor is the initial scaling factor.
     * @param controller is the responsible {@link MainController}.
     */
    public MeepleDepictionPanel(int scalingFactor, MainController controller) {
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
            MeepleDepiction meepleLabel = new MeepleDepiction(scalingFactor, controller, direction);
            constraints.gridx = direction.getX() + GRID_INDEX_OFFSET;
            constraints.gridy = direction.getY() + GRID_INDEX_OFFSET;
            labels.put(direction, meepleLabel);
            add(meepleLabel.getLabel(), constraints);
        }
    }

    /**
     * Places a meeple in a specific position on this panel.
     * @param terrain specifies the meeple type.
     * @param position is the specific position where the meeple is placed, correlating to the position on the tile.
     * @param owner is the player that owns the meeple.
     */
    public void placeMeeple(TerrainType terrain, GridDirection position, Player owner) {
        labels.get(position).setIcon(terrain, owner);
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
        size = new Dimension(scalingFactor, scalingFactor);
        labels.values().forEach(it -> it.setMeepleSize(scalingFactor));
    }

    /**
     * Refreshes all meeple labels in this panel. This updates the images to color changes.
     */
    public void refreshAll() {
        labels.values().forEach(MeepleDepiction::refresh);
    }

    /**
     * Resets all meeples in this panel.
     */
    public void resetAll() {
        labels.values().forEach(MeepleDepiction::reset);
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMaximumSize() {
        return size;
    }
}
