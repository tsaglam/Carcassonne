package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import carcassonne.control.GameOptions;
import carcassonne.control.MainController;
import carcassonne.control.PaintShop;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;

/**
 * Special {@link JLabel} for showing meeples.
 * @author Timur Saglam
 */
public class MeepleLabel extends JLabel {
    private static final long serialVersionUID = 3333971053086379413L;
    private final ImageIcon imageEmpty;
    private final PaintShop paintShop;
    private int playerNumber;
    private final MouseAdapter mouseAdapter;
    private TerrainType terrain;

    /**
     * Creates a blank meeple label.
     * @param paintShop is the paint shop for the meeple generation.
     */
    public MeepleLabel(PaintShop paintShop, MainController controller, GridDirection direction) {
        super();
        imageEmpty = new ImageIcon(GameOptions.getInstance().getMeeplePath(TerrainType.OTHER, false));
        reset();
        this.paintShop = paintShop;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    controller.requestMeeplePlacement(direction);
                }
            }
        };
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, playerNumber);
            super.setIcon(icon);
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        playerNumber = -1;
        super.setIcon(imageEmpty);
        removeMouseListener(mouseAdapter);
    }

    /**
     * Sets the icon of the meeple label according to the player number and terrain type.
     * @param terrain is the terrain type and affects the meeple type.
     * @param playerNumber is the player number and affects the color.
     */
    public void setIcon(TerrainType terrain, int playerNumber) {
        this.terrain = terrain;
        this.playerNumber = playerNumber;
        refresh();
    }

    @Override
    public void setIcon(Icon icon) {
        addMouseListener(mouseAdapter);
        super.setIcon(icon);
    }

}
