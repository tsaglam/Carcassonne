package carcassonne.view.main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import carcassonne.control.GameOptions;
import carcassonne.control.PaintShop;
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
    private TerrainType terrain;

    /**
     * Creates a blank meeple label.
     * @param paintShop is the paint shop for the meeple generation.
     */
    public MeepleLabel(PaintShop paintShop) {
        super();
        imageEmpty = new ImageIcon(GameOptions.getInstance().getMeeplePath(TerrainType.OTHER, false));
        reset();
        this.paintShop = paintShop;
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER) {
            ImageIcon icon = paintShop.getColoredMeeple(terrain, playerNumber);
            setIcon(icon);
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        playerNumber = -1;
        setIcon(imageEmpty);
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

}
